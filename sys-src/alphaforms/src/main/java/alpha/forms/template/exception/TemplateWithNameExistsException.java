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
package alpha.forms.template.exception;

/**
 * The Class TemplateWithNameExistsException.
 */
public class TemplateWithNameExistsException extends Exception {

	/**
	 * Instantiates a new template with name exists exception.
	 */
	public TemplateWithNameExistsException() {
		super();
	}

	/**
	 * Instantiates a new template with name exists exception.
	 * 
	 * @param msg
	 *            the msg
	 */
	public TemplateWithNameExistsException(final String msg) {
		super(msg);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}
