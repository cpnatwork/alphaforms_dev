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
package alpha.forms.designer.view.popup;

import java.awt.Component;
import java.awt.Point;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import alpha.forms.designer.action.ChangeGridSizeAction;
import alpha.forms.designer.action.DeleteWidgetAction;
import alpha.forms.designer.action.SaveAsTemplateAction;
import alpha.forms.designer.action.ShrinkToFitAction;
import alpha.forms.designer.action.ToggleGridAction;
import alpha.forms.designer.view.FormCanvas;
import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class FormPopupMenu.
 */
public class FormPopupMenu extends JPopupMenu {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The form. */
	private AlphaForm form;

	/** The canvas. */
	private FormCanvas canvas;

	/** The widget. */
	private FormWidget widget;

	/** The delete widget. */
	private DeleteWidgetAction deleteWidget;

	/** The save template. */
	private SaveAsTemplateAction saveTemplate;

	/**
	 * Instantiates a new form popup menu.
	 * 
	 * @param form
	 *            the form
	 * @param canvas
	 *            the canvas
	 */
	public FormPopupMenu(AlphaForm form, FormCanvas canvas) {
		this.form = form;
		this.canvas = canvas;
		deleteWidget = new DeleteWidgetAction("Delete Widget", form, canvas);
		JMenuItem deleteWidgetItem = new JMenuItem(deleteWidget);
		this.add(deleteWidgetItem);
		saveTemplate = new SaveAsTemplateAction("Save as Template...", form,
				canvas);
		JMenuItem saveTemplateItem = new JMenuItem(saveTemplate);
		this.add(saveTemplateItem);
		this.addSeparator();
		JMenuItem shrinkForm = new JMenuItem(new ShrinkToFitAction(
				"Shrink To Fit", form, canvas));
		this.add(shrinkForm);
		this.addSeparator();
		JCheckBoxMenuItem toggleGrid = new JCheckBoxMenuItem(
				new ToggleGridAction("Show Grid", form, canvas));
		this.add(toggleGrid);
		JMenu gridSizeMenu = new JMenu("Grid Size");
		ButtonGroup gridSizeGroup = new ButtonGroup();
		JRadioButtonMenuItem gridSize5 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("5x5 Pixels", 5, form, canvas));
		gridSizeMenu.add(gridSize5);
		gridSizeGroup.add(gridSize5);
		JRadioButtonMenuItem gridSize10 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("10x10 Pixels", 10, form, canvas));
		gridSizeMenu.add(gridSize10);
		gridSizeGroup.add(gridSize10);
		JRadioButtonMenuItem gridSize15 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("15x15 Pixels", 15, form, canvas));
		gridSizeMenu.add(gridSize15);
		gridSizeGroup.add(gridSize15);
		JRadioButtonMenuItem gridSize20 = new JRadioButtonMenuItem(
				new ChangeGridSizeAction("20x20 Pixels", 20, form, canvas));
		gridSizeMenu.add(gridSize20);
		gridSizeGroup.add(gridSize20);
		this.add(gridSizeMenu);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JPopupMenu#show(java.awt.Component, int, int)
	 */
	@Override
	public void show(Component cmp, int x, int y) {
		widget = canvas.findWidgetDeep(new Point(x, y));

		deleteWidget.setWidget(widget);
		saveTemplate.setEnabled(widget != null);
		super.show(cmp, x, y);
	}

}
