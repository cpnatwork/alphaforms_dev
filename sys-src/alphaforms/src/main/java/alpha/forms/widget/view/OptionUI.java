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

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import alpha.forms.widget.model.Option;
import alpha.forms.widget.model.Option.OptionItem;
import alpha.forms.widget.util.OptionLayout;

/**
 * The Class OptionUI.
 */
public class OptionUI extends FormWidgetUI {

	/** The model. */
	private Option model;

	/**
	 * Instantiates a new option ui.
	 * 
	 * @param model
	 *            the model
	 */
	public OptionUI(Option model) {
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
		super.setBounds(model.getX(), model.getY(), model.getWidth(),
				model.getHeight());
		compose();
		this.setVisible(model.isVisible());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#getSubselection()
	 */
	@Override
	public Rectangle getSubselection() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#isSubselectionResizable(int)
	 */
	@Override
	public boolean isSubselectionResizable(int direction) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#updateSubselectionSize(int,
	 * int)
	 */
	@Override
	public void updateSubselectionSize(int delta, int direction) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		removeAll();
		super.compose();
		super.setOpaque(true);
		super.setSize(model.getSize());
		this.setLayout(null);
		int maxX = 0;
		int maxY = 0;
		ButtonGroup group = new ButtonGroup();
		for (OptionItem option : model.getOptions()) {
			AbstractButton comp;
			if (model.isMultiselect()) {
				comp = new JCheckBox(option.getName());
				comp.setSelected(option.isValue());
			} else {
				comp = new JRadioButton(option.getName());
				comp.setSelected(option.isValue());
				group.add(comp);
			}
			comp.setName(option.getName());
			comp.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ev) {
					AbstractButton btn = (AbstractButton) ev.getSource();
					OptionItem item = model.findOptionByName(btn.getName());
					item.setValue(btn.isSelected());
					model.getOnSelectionChanged().fire();
				}
			});
			comp.setSize(comp.getPreferredSize());
			comp.doLayout();
			int gap = model.getGap();
			if (model.getLayout() == OptionLayout.HORIZONTAL) {
				comp.setLocation(maxX + gap, 0);
				maxX += comp.getWidth() + gap;
				maxY = (comp.getHeight() > maxY) ? comp.getHeight() : maxY;
			} else if (model.getLayout() == OptionLayout.VERTICAL) {
				comp.setLocation(0, maxY + gap);
				maxY += comp.getHeight() + gap;
				maxX = (comp.getWidth() > maxX) ? comp.getWidth() : maxX;
			}
			add(comp);
		}
		minimumHeight = maxY;
		minimumWidth = maxX;
		super.setSize(maxX, maxY);
		super.setBounds(model.getX(), model.getY(), model.getWidth(),
				model.getHeight());
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
