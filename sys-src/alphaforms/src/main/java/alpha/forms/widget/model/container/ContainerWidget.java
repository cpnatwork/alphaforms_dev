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
package alpha.forms.widget.model.container;

import java.awt.Point;
import java.util.List;

import alpha.forms.widget.model.FormWidget;

/**
 * The Interface ContainerWidget.
 */
public interface ContainerWidget {

	/**
	 * Gets the children.
	 * 
	 * @return the children
	 */
	public List<FormWidget> getChildren();

	/**
	 * Sets the children.
	 * 
	 * @param widget
	 *            the new children
	 */
	public void setChildren(List<FormWidget> widget);

	/**
	 * Adds the child.
	 * 
	 * @param w
	 *            the w
	 */
	public void addChild(FormWidget w);

	/**
	 * Removes the child.
	 * 
	 * @param w
	 *            the w
	 */
	public void removeChild(FormWidget w);

	/**
	 * Does accept widget.
	 * 
	 * @param w
	 *            the w
	 * @return true, if successful
	 */
	public boolean doesAcceptWidget(FormWidget w);

	/**
	 * Gets the widget by location.
	 * 
	 * @param p
	 *            the p
	 * @return the widget by location
	 */
	public FormWidget getWidgetByLocation(Point p);
}
