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
package alpha.forms.designer.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import alpha.forms.designer.view.FormCanvas;
import alpha.forms.form.AlphaForm;

/**
 * The Class ToggleGridAction.
 */
public class ToggleGridAction extends AbstractAction {

	/** The form. */
	private AlphaForm form;

	/** The canvas. */
	private FormCanvas canvas;

	/**
	 * Instantiates a new toggle grid action.
	 * 
	 * @param text
	 *            the text
	 * @param form
	 *            the form
	 * @param canvas
	 *            the canvas
	 */
	public ToggleGridAction(String text, AlphaForm form, FormCanvas canvas) {
		super(text);
		this.form = form;
		this.canvas = canvas;
		putValue(SELECTED_KEY, canvas.isShowGrid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ev) {
		canvas.setShowGrid(!canvas.isShowGrid());
		putValue(SELECTED_KEY, canvas.isShowGrid());
		canvas.repaint();
	}

}
