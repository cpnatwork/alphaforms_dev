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

import alpha.forms.widget.model.FormWidget;

/**
 * The Class PropertyUpdatedSignal.
 */
public class PropertyUpdatedSignal extends Signal {

	/** The subject. */
	private FormWidget subject;

	/** The property name. */
	private String propertyName;

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	public FormWidget getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 * 
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(FormWidget subject) {
		this.subject = subject;
	}

	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the property name.
	 * 
	 * @param propertyName
	 *            the new property name
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

}
