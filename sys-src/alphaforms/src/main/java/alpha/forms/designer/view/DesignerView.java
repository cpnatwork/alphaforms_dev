/**************************************************************************
 * alpha-Flow
 * ==============================================
 * Copyright (C) 2009-2011 by Christoph P. Neumann
 * (http://www.chr15t0ph.de)
 **************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 **************************************************************************
 * $Id$
 *************************************************************************/
package alpha.forms.designer.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import alpha.forms.application.controller.ApplicationController;
import alpha.forms.application.view.panel.FormView;
import alpha.forms.designer.dnd.ComponentDragDrop;
import alpha.forms.form.AlphaForm;
import alpha.forms.propertyEditor.PropertyEditorPanel;
import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.signal.model.SwitchViewSignal;
import alpha.forms.signal.model.UpdateStatusSignal;
import alpha.forms.template.model.WidgetTemplateMngr;
import alpha.forms.template.util.TemplateFileFilter;
import alpha.forms.template.view.TemplatePalette;
import alpha.forms.widget.model.AlphaList;
import alpha.forms.widget.model.Button;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.Group;
import alpha.forms.widget.model.Heading;
import alpha.forms.widget.model.Option;
import alpha.forms.widget.model.TextField;

/**
 * The Class DesignerView.
 */
public class DesignerView extends FormView implements Subscriber {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private AlphaForm model;

	/** The status label. */
	private JLabel statusLabel;

	/** The template palette. */
	private TemplatePalette templatePalette;

	/** The form panel. */
	private FormDesignerPanel formPanel;

	/**
	 * Instantiates a new designer view.
	 * 
	 * @param form
	 *            the form
	 * @param controller
	 *            the controller
	 */
	public DesignerView(AlphaForm form, final ApplicationController controller) {
		super(controller);
		model = form;

		this.setLayout(new BorderLayout());

		JSplitPane jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane jsp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp1.setBorder(null);
		jsp2.setBorder(null);

		final Component parentComponent = this;
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		JButton button1 = new JButton("Save");

		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.persistForm();
			}

		});
		JButton button2 = new JButton("Go to Fill-in mode");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				SwitchViewSignal s = new SwitchViewSignal();
				s.setFromView(SwitchViewSignal.DESIGNER);
				s.setToView(SwitchViewSignal.VIEWER);
				SignalManager.getInstance().sendSignal(s, "designer");
			}
		});
		toolbar.add(button1);
		toolbar.add(button2);
		toolbar.addSeparator();

		JButton btnStoreTemplates = new JButton("Export Templates");
		btnStoreTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				fc.setFileFilter(new TemplateFileFilter());
				int ret = fc.showSaveDialog(parentComponent);
				if (ret == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					try {
						FileOutputStream fout = new FileOutputStream(f);
						WidgetTemplateMngr.getInstance().storeTemplates(fout);
						fout.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		JButton btnLoadTemplates = new JButton("Import Templates");
		btnLoadTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				int ret = JOptionPane
						.showConfirmDialog(
								parentComponent,
								"This will replace all current templates in the template library. Continue?",
								"Replace templates?",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (ret == JOptionPane.YES_OPTION) {
					JFileChooser fc = new JFileChooser();
					fc.setMultiSelectionEnabled(false);
					fc.setFileFilter(new TemplateFileFilter());
					ret = fc.showOpenDialog(parentComponent);
					if (ret == JFileChooser.APPROVE_OPTION) {
						File f = fc.getSelectedFile();
						try {
							FileInputStream fin = new FileInputStream(f);
							WidgetTemplateMngr.getInstance().loadTemplates(fin);
							fin.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		toolbar.add(btnStoreTemplates);
		toolbar.add(btnLoadTemplates);

		JToolBar status = new JToolBar();
		status.setFloatable(false);
		statusLabel = new JLabel("Selected: None.");
		status.addSeparator();
		status.add(statusLabel);

		WidgetPalette widgetPalette = new WidgetPalette();
		widgetPalette.setMinimumSize(new Dimension(220, 300));
		widgetPalette.registerWidgetClass(TextField.class);
		widgetPalette.registerWidgetClass(AlphaList.class);
		widgetPalette.registerWidgetClass(Option.class);
		widgetPalette.registerWidgetClass(Group.class);
		widgetPalette.registerWidgetClass(Button.class);
		widgetPalette.registerWidgetClass(Heading.class);

		templatePalette = new TemplatePalette();
		templatePalette.setMinimumSize(new Dimension(220, 300));

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Widgets", widgetPalette);
		tabPane.addTab("Widget Templates", templatePalette);

		PropertyEditorPanel propertyPanel = new PropertyEditorPanel(model);
		propertyPanel.setMinimumSize(new Dimension(220, 300));

		formPanel = new FormDesignerPanel(model);

		// jsp1.setLeftComponent(widgetPalette);
		jsp1.setLeftComponent(tabPane);
		jsp1.setRightComponent(jsp2);
		jsp2.setLeftComponent(formPanel);
		jsp2.setRightComponent(propertyPanel);

		this.add(toolbar, BorderLayout.NORTH);
		this.add(status, BorderLayout.SOUTH);
		this.add(jsp1, BorderLayout.CENTER);

		SignalManager.getInstance().subscribeSink(this, "formCanvas");

		ComponentDragDrop dndListener = new ComponentDragDrop();
		// TemplateDragDrop dndTemplate = new TemplateDragDrop();
		DragSource dragSource1 = new DragSource();
		DragSource dragSource2 = new DragSource();
		DropTarget dropTarget1 = new DropTarget(
				formPanel.getDropTargetComponent(), DnDConstants.ACTION_COPY,
				dndListener);
		// DropTarget dropTarget2 = new
		// DropTarget(formPanel.getDropTargetComponent(),
		// DnDConstants.ACTION_COPY, dndTemplate);
		DragGestureRecognizer dndrec1 = dragSource1
				.createDefaultDragGestureRecognizer(
						widgetPalette.getDragSource(),
						DnDConstants.ACTION_COPY, dndListener);
		DragGestureRecognizer dndrec2 = dragSource2
				.createDefaultDragGestureRecognizer(
						templatePalette.getDragSource(),
						DnDConstants.ACTION_COPY, dndListener);

	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	/**
	 * Creates the template.
	 */
	protected void createTemplate() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.signal.model.Subscriber#signalReceived(alpha.forms.signal
	 * .model.Signal)
	 */
	@Override
	public void signalReceived(Signal s) {
		if (s instanceof UpdateStatusSignal) {
			setStatus(((UpdateStatusSignal) s).getStatus());
		} else if (s instanceof SelectionChangedSignal) {
			String status = "Selected: ";
			List<FormWidget> sl = ((SelectionChangedSignal) s).getSelection();
			if (sl == null) {
				status += "None";
			} else if (sl.size() == 1) {
				status += sl.get(0).getName();
			} else {
				status += sl.size() + " items";
			}
			setStatus(status);
		}
	}

}
