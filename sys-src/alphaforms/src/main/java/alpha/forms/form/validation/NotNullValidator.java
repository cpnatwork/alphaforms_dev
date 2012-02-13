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
package alpha.forms.form.validation;

import java.util.Map;

import javax.swing.JPanel;

import alpha.forms.memento.model.ValidatorMemento;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class NotNullValidator.
 */
public class NotNullValidator implements Validator {

	/** The error message. */
	protected final String errorMessage = "Field must not be null or empty.";

	/** The error. */
	protected String error = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#validate(alpha.forms.form.validation
	 * .ValidationContext, java.lang.Object)
	 */
	@Override
	public boolean validate(ValidationContext ctx, Object value) {
		if (value != null) {
			if (value instanceof String && ((String) value).isEmpty()) {
				error = errorMessage;
				return false;
			} else {
				return true;
			}
		} else {
			error = errorMessage;
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getOptionUI()
	 */
	@Override
	public JPanel getOptionUI() {
		return new JPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#isCompatibleWith(java.lang.Class)
	 */
	@Override
	public boolean isCompatibleWith(Class<? extends FormWidget> widgetType) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Validator clone() {
		return new NotNullValidator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getHandle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getSettingsMap()
	 */
	@Override
	public Map<String, String> getSettingsMap() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getHandle()
	 */
	@Override
	public String getHandle() {
		return "notNull";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#createMemento()
	 */
	@Override
	public ValidatorMemento createMemento() {
		ValidatorMemento m = new ValidatorMemento();
		m.setHandle(getHandle());
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#setMemento(alpha.forms.memento.
	 * model.ValidatorMemento)
	 */
	@Override
	public void setMemento(ValidatorMemento m) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getError()
	 */
	@Override
	public String getError() {
		return error;
	}

}
