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
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import alpha.forms.designer.view.FormCanvas;
import alpha.forms.form.AlphaForm;
import alpha.forms.template.exception.TemplateWithNameExistsException;
import alpha.forms.template.model.WidgetTemplateMngr;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class SaveAsTemplateAction.
 */
public class SaveAsTemplateAction extends AbstractAction {

	/** The form. */
	private AlphaForm form;

	/** The canvas. */
	private FormCanvas canvas;

	/**
	 * Instantiates a new save as template action.
	 * 
	 * @param text
	 *            the text
	 * @param form
	 *            the form
	 * @param canvas
	 *            the canvas
	 */
	public SaveAsTemplateAction(String text, AlphaForm form, FormCanvas canvas) {
		super(text);
		this.form = form;
		this.canvas = canvas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ev) {
		List<FormWidget> selectedItems = canvas.getSelectedItems();
		if (selectedItems.size() > 0) {
			FormWidget w = selectedItems.get(0);
			String templateName = (String) JOptionPane.showInputDialog(null,
					"Template Name:", "Create Template...",
					JOptionPane.QUESTION_MESSAGE);

			try {
				WidgetTemplateMngr.getInstance().saveAsTemplate(w,
						templateName, false);
			} catch (TemplateWithNameExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
