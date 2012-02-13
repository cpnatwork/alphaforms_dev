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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;

import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.Group;

/**
 * The Class GroupUI.
 */
public class GroupUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	private Group model;

	/**
	 * Instantiates a new group ui.
	 * 
	 * @param model
	 *            the model
	 */
	public GroupUI(Group model) {
		super(model);
		this.model = model;
		compose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		// super.compose();
		setLayout(null);
		removeAll();
		for (FormWidget w : model.getChildren()) {
			add(w.getUi());
		}
		doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.doLayout();
		switch (model.getBorder()) {
		case TITLED:
			setBorder(BorderFactory.createTitledBorder(model.getTitle()));
			break;
		case UNTITLED:
			setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
			break;
		case NONE:
		default:
			setBorder(null);
		}
		for (FormWidget w : model.getChildren()) {
			w.getUi().doLayout();
		}
		super.setSize(model.getSize());
		this.setVisible(model.isVisible());
	}

	/**
	 * Find component.
	 * 
	 * @param name
	 *            the name
	 * @return the form widget
	 */
	public FormWidget findComponent(String name) {
		for (Component c : getComponents()) {
			if (c instanceof FormWidgetUI
					&& ((FormWidgetUI) c).model.getName().equals(name)) {
				return (FormWidget) ((FormWidgetUI) c).model;
			}
		}
		return null;
	}

	/**
	 * Adds the.
	 * 
	 * @param w
	 *            the w
	 */
	public void add(FormWidget w) {
		add(w.getUi());
		validate();
		doLayout();
	}

	/**
	 * Removes the.
	 * 
	 * @param w
	 *            the w
	 */
	public void remove(FormWidget w) {
		remove(w.getUi());
		validate();
		doLayout();
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
