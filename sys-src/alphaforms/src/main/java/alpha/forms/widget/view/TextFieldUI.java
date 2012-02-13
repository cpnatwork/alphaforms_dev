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
package alpha.forms.widget.view;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import alpha.forms.designer.view.SelectionState;
import alpha.forms.widget.model.TextField;
import alpha.forms.widget.util.WidgetLabelPosition;

/**
 * The Class TextFieldUI.
 */
public class TextFieldUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	protected TextField model;

	/** The label. */
	private JLabel label;

	/** The text. */
	private JTextField text;

	/** The hgap. */
	private int hgap = 5;

	/**
	 * Instantiates a new text field ui.
	 * 
	 * @param model
	 *            the model
	 */
	public TextFieldUI(TextField model) {
		super(model);
		this.model = model;
		compose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.doLayout();
		Dimension d = model.getSize();
		super.setSize(d);
		text.setText(model.getText());
		if (label != null
				&& this.model.getShowLabel() == WidgetLabelPosition.LEFT) {
			label.setText(model.getLabel());
			Dimension dl = label.getPreferredSize();
			label.setSize(dl);
			label.setLocation(0, d.height / 2 - dl.height / 2);
			text.setLocation(dl.width + hgap, 0);
			text.setSize(d.width - hgap - dl.width, d.height);
		} else if (label != null
				&& this.model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			label.setText(model.getLabel());
			Dimension dl = label.getPreferredSize();
			label.setSize(dl);
			label.setLocation(d.width - dl.width, d.height / 2 - dl.height / 2);
			text.setLocation(0, 0);
			text.setSize(d.width - hgap - dl.width, d.height);
		} else {
			text.setLocation(0, 0);
			text.setSize(d.width, d.height);
			label.setSize(0, 0);
		}
		this.setVisible(model.isVisible());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#getSubselection()
	 */
	@Override
	public Rectangle getSubselection() {
		if (model.getShowLabel() == WidgetLabelPosition.LEFT
				|| model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			Rectangle r = text.getBounds();
			r.translate(getLocation().x, getLocation().y);
			return r;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#isSubselectionResizable(int)
	 */
	@Override
	public boolean isSubselectionResizable(int direction) {
		return (direction == SelectionState.WEST && model.getShowLabel() == WidgetLabelPosition.LEFT)
				|| (direction == SelectionState.EAST && model.getShowLabel() == WidgetLabelPosition.RIGHT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#updateSubselectionSize(int,
	 * int)
	 */
	@Override
	public void updateSubselectionSize(int delta, int direction) {
		if (direction == SelectionState.WEST
				&& model.getShowLabel() == WidgetLabelPosition.LEFT) {
			int tmpgap = hgap - delta;
			if (tmpgap >= 5
					&& tmpgap + label.getWidth() < model.getSize().width) {
				hgap = tmpgap;
			}
		} else if (direction == SelectionState.EAST
				&& model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			int tmpgap = hgap - (delta - text.getWidth());
			if (tmpgap >= 5
					&& tmpgap + label.getWidth() < model.getSize().width) {
				hgap = tmpgap;
			}
		}
		doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		super.compose();
		this.setLayout(null);
		final TextField tf = this.model;
		text = new JTextField();
		text.setOpaque(this.isOpaque());
		text.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent ev) {
				tf.setText(text.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				tf.setText(text.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				tf.setText(text.getText());
			}

		});
		if (tf.getShowLabel() == WidgetLabelPosition.LEFT) {
			label = new JLabel(tf.getLabel());
			Dimension d = label.getPreferredSize();
			Dimension dt = text.getPreferredSize();

			label.setLocation(0, dt.height / 2 - d.height / 2);
			label.setSize(d.width, d.height);
			text.setSize(model.getMiniumWidth() - d.width - hgap, dt.height);
			text.setLocation(d.width + hgap, 0);
			this.add(label);
			this.add(text);
			minimumHeight = dt.height;
			minimumWidth = d.width + hgap + dt.width;
		} else if (tf.getShowLabel() == WidgetLabelPosition.RIGHT) {
			label = new JLabel(tf.getLabel());
			Dimension d = label.getMinimumSize();
			Dimension dt = text.getMinimumSize();
			text.setBounds(0, 0, tf.getMiniumWidth() - d.width - hgap,
					dt.height);
			label.setBounds(tf.getMiniumWidth() + hgap, dt.height / 2
					- d.height / 2, d.width, d.height);
			this.add(text);
			this.add(label);
			minimumHeight = dt.height;
			minimumWidth = tf.getMiniumWidth();
		} else {
			Dimension dt = text.getMinimumSize();
			text.setBounds(0, 0, tf.getMiniumWidth(), dt.height);
			this.add(text);
			minimumHeight = dt.height;
			minimumWidth = tf.getMiniumWidth();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		doLayout();
	}

}
