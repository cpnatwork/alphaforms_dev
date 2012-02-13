/**************************************************************************
 * alpha-Forms
 * ==============================================
 * Copyright (C) 2011-2012 by 
 *   - Christoph P. Neumann (http://www.chr15t0ph.de)
 *   - Florian Wagner
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
package alpha.forms.form.naming;

import alpha.forms.widget.model.FormWidget;

/**
 * The Interface WidgetNamingService.
 */
public interface WidgetNamingService {

	/**
	 * Check name exists.
	 * 
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	public boolean checkNameExists(String name);

	/**
	 * Check name.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 * @throws WidgetNameExistsException
	 *             the widget name exists exception
	 */
	public String checkName(String name) throws WidgetNameExistsException;

	/**
	 * Check name.
	 * 
	 * @param name
	 *            the name
	 * @param w
	 *            the w
	 * @return the string
	 * @throws WidgetNameExistsException
	 *             the widget name exists exception
	 */
	public String checkName(String name, FormWidget w)
			throws WidgetNameExistsException;
}
