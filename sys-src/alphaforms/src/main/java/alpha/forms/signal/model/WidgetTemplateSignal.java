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
package alpha.forms.signal.model;

import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class WidgetTemplateSignal.
 */
public class WidgetTemplateSignal extends Signal {

	/** The Constant CREATE. */
	public static final int CREATE = 1;

	/** The Constant LOAD. */
	public static final int LOAD = 2;

	/** The widget. */
	protected FormWidget widget;

	/** The template. */
	protected WidgetTemplate template;

	/** The action. */
	protected int action;

	/** The template name. */
	protected String templateName;

	/**
	 * Gets the widget.
	 * 
	 * @return the widget
	 */
	public FormWidget getWidget() {
		return widget;
	}

	/**
	 * Sets the widget.
	 * 
	 * @param widget
	 *            the new widget
	 */
	public void setWidget(FormWidget widget) {
		this.widget = widget;
	}

	/**
	 * Gets the template.
	 * 
	 * @return the template
	 */
	public WidgetTemplate getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 * 
	 * @param template
	 *            the new template
	 */
	public void setTemplate(WidgetTemplate template) {
		this.template = template;
	}

	/**
	 * Gets the action.
	 * 
	 * @return the action
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 * 
	 * @param action
	 *            the new action
	 */
	public void setAction(int action) {
		this.action = action;
	}

}
