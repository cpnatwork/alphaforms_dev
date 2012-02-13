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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import alpha.forms.widget.model.Button;

/**
 * The Class ButtonUI.
 */
public class ButtonUI extends FormWidgetUI {

	/** The model. */
	Button model;

	/** The button. */
	JButton button;

	/**
	 * Instantiates a new button ui.
	 * 
	 * @param model
	 *            the model
	 */
	public ButtonUI(Button model) {
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
		button = new JButton(model.getLabel());
		doLayout();

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				model.getOnClick().fire();
			}
		});
		this.add(button);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.setBounds(model.getX(), model.getY(), model.getWidth(),
				model.getHeight());
		super.doLayout();

		button.setSize(model.getSize());
		button.setLocation(0, 0);
		button.setText(model.getLabel());
	}

}
