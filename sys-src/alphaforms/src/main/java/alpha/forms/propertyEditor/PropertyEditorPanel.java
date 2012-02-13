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
package alpha.forms.propertyEditor;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import alpha.forms.form.AlphaForm;
import alpha.forms.propertyEditor.component.InfoPanel;
import alpha.forms.propertyEditor.component.PropertyTable;
import alpha.forms.propertyEditor.model.PropertyEditorModel;
import alpha.forms.propertyEditor.model.WidgetProperty;
import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class PropertyEditorPanel.
 */
public class PropertyEditorPanel extends JPanel implements Subscriber {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private AlphaForm model;

	/** The selected components. */
	private List<FormWidget> selectedComponents = null;

	/** The info. */
	private InfoPanel info;

	/** The table. */
	private PropertyTable table;

	/** The editor model. */
	private PropertyEditorModel editorModel;

	/**
	 * Instantiates a new property editor panel.
	 * 
	 * @param model
	 *            the model
	 */
	public PropertyEditorPanel(AlphaForm model) {
		this.model = model;

		this.setBorder(BorderFactory.createTitledBorder("Property Editor"));
		this.setLayout(new BorderLayout());

		editorModel = new PropertyEditorModel(model, this);

		editorModel.updateDataModel(null);

		table = new PropertyTable(this, editorModel);
		info = new InfoPanel();

		table.setRowHeight(23);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent ev) {
				WidgetProperty p = editorModel.getWidgetPropertyForRow(table
						.getSelectedRow());
				info.updateWith(p);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});

		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.add(info, BorderLayout.SOUTH);

		SignalManager.getInstance().subscribeSink(this, "formCanvas");
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
		if (s instanceof SelectionChangedSignal) {
			selectedComponents = ((SelectionChangedSignal) s).getSelection();
			editorModel.updateDataModel(selectedComponents);
		}
	}

	/**
	 * Stop table editing.
	 */
	public void stopTableEditing() {
		if (table != null && table.isEditing()) {
			table.getCellEditor().cancelCellEditing();
		}
	}

}
