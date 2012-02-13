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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import alpha.forms.form.AlphaForm;
import alpha.forms.signal.model.PropertyUpdatedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.ContainerWidget;

/**
 * The Class FormDesignerPanel.
 */
public class FormDesignerPanel extends JPanel implements ComponentListener,
		Subscriber {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private AlphaForm model;

	/** The form title. */
	final private JLabel formTitle = new JLabel();

	/** The document states. */
	private JComboBox documentStates = new JComboBox();

	/** The header. */
	private JPanel header;

	/** The canvas. */
	private FormCanvas canvas;

	/**
	 * Instantiates a new form designer panel.
	 * 
	 * @param model
	 *            the model
	 */
	public FormDesignerPanel(final AlphaForm model) {
		this.model = model;
		this.setBorder(BorderFactory.createTitledBorder("Form"));
		this.setMinimumSize(new Dimension(40, 40));
		this.setLayout(new BorderLayout());

		header = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.2;

		formTitle.setFont(formTitle.getFont().deriveFont(18.0f));

		header.add(formTitle, c);

		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 0.0;

		if (model.getDocumentStates().isEmpty()) {
			documentStates = new JComboBox(new String[] { "NONE" });
			documentStates.setEditable(false);
			documentStates.setEnabled(false);
		} else {
			documentStates = new JComboBox(model.getDocumentStates().toArray());
			documentStates.setEditable(false);
		}
		documentStates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if (documentStates.getSelectedItem() != null) {
					model.setActiveDocumentState(documentStates
							.getSelectedItem().toString());
				}
			}
		});

		header.add(documentStates, c);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

		header.add(separator, c);

		this.add(header, BorderLayout.NORTH);

		canvas = new FormCanvas(this, model);
		JScrollPane scroll = new JScrollPane(canvas);
		scroll.setBorder(null);
		this.add(scroll, BorderLayout.CENTER);

		SignalManager.getInstance().subscribeSink(this, "propertyEditor");

		this.updateUI();
		// setup standard form elements
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent ev) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent
	 * )
	 */
	@Override
	public void componentMoved(ComponentEvent ev) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent ev) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent
	 * )
	 */
	@Override
	public void componentShown(ComponentEvent ev) {
		// TODO Auto-generated method stub

	}

	/**
	 * Adds the widget.
	 * 
	 * @param w
	 *            the w
	 * @param p
	 *            the p
	 */
	public void addWidget(FormWidget w, Point p) {
		FormWidget container = canvas.findWidget(p);
		if (container != null && container instanceof ContainerWidget
				&& ((ContainerWidget) container).doesAcceptWidget(w)) {
			Point gl = canvas.translatePoint(p);
			w.setX(gl.x - container.getX());
			w.setY(gl.y - container.getY());
			// w.setWidth(w.getMiniumWidth());
			// w.setHeight(w.getMinimumHeight());
			((ContainerWidget) container).addChild(w);
			w.setParent(container);
			canvas.stopContainerHighlight();
		} else {
			Point gl = canvas.translatePoint(p);
			w.setX(gl.x);
			w.setY(gl.y);
			// w.setWidth(w.getMiniumWidth());
			// w.setHeight(w.getMinimumHeight());
			model.addWidget(w);
		}
	}

	/**
	 * Gets the drop area.
	 * 
	 * @return the drop area
	 */
	public Rectangle getDropArea() {
		return canvas.getBounds();
	}

	/**
	 * Gets the drop target component.
	 * 
	 * @return the drop target component
	 */
	public Component getDropTargetComponent() {
		return canvas;
	}

	/**
	 * Gets the selected items.
	 * 
	 * @return the selected items
	 */
	public List<FormWidget> getSelectedItems() {
		return canvas.getSelectedItems();
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
		if (s instanceof PropertyUpdatedSignal) {
			this.updateUI();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JPanel#updateUI()
	 */
	@Override
	public void updateUI() {
		if (model != null) {
			formTitle.setText(model.getTitle());
			if (model.getDocumentStates().isEmpty()) {
				documentStates = new JComboBox(new String[] { "NONE" });
				documentStates.setEditable(false);
				documentStates.setEnabled(false);
			} else {
				documentStates = new JComboBox(model.getDocumentStates()
						.toArray());
				documentStates.setEditable(false);
			}
		}
		super.updateUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(500, 600);
	}

}
