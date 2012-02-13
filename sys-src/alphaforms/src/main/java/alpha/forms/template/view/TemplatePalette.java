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
package alpha.forms.template.view;

import java.awt.Component;

import javax.swing.JList;

import alpha.forms.designer.view.WidgetPalette;
import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.template.model.WidgetTemplateMngr;

/**
 * The Class TemplatePalette.
 */
public class TemplatePalette extends WidgetPalette {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list model. */
	private WidgetTemplateMngr listModel = WidgetTemplateMngr.getInstance();

	/**
	 * Instantiates a new template palette.
	 */
	public TemplatePalette() {
		super();
		this.list.setCellRenderer(new TemplateListCellRenderer());
		this.list.setModel(listModel);
	}

	/**
	 * The Class TemplateListCellRenderer.
	 */
	protected class TemplateListCellRenderer extends WidgetListCellRenderer {

		/*
		 * (non-Javadoc)
		 * 
		 * @see alpha.forms.designer.view.WidgetPalette.WidgetListCellRenderer#
		 * getListCellRendererComponent(javax.swing.JList, java.lang.Object,
		 * int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList list,
				Object object, int index, boolean isSelected, boolean hasFocus) {
			WidgetTemplate wt = (WidgetTemplate) object;
			return super.getListCellRendererComponent(list,
					wt.createWidgetFromTemplate(), index, isSelected, hasFocus);
		}
	}

}
