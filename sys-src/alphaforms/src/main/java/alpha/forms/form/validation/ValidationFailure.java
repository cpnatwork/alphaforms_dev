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

import alpha.forms.widget.model.FormWidget;

/**
 * The Class ValidationFailure.
 */
public class ValidationFailure {

	/** The source. */
	private FormWidget source;

	/** The validator. */
	private Validator validator;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new validation failure.
	 * 
	 * @param source
	 *            the source
	 * @param validator
	 *            the validator
	 */
	public ValidationFailure(FormWidget source, Validator validator) {
		super();
		this.source = source;
		this.validator = validator;
	}

	/**
	 * Instantiates a new validation failure.
	 * 
	 * @param source
	 *            the source
	 * @param validator
	 *            the validator
	 * @param message
	 *            the message
	 */
	public ValidationFailure(FormWidget source, Validator validator,
			String message) {
		super();
		this.source = source;
		this.validator = validator;
		this.message = message;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public FormWidget getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(FormWidget source) {
		this.source = source;
	}

	/**
	 * Gets the validator.
	 * 
	 * @return the validator
	 */
	public Validator getValidator() {
		return validator;
	}

	/**
	 * Sets the validator.
	 * 
	 * @param validator
	 *            the new validator
	 */
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(source.getName()).append(".").append(validator.getHandle());
		sb.append(" (").append(source.getClass().getSimpleName()).append("): ");
		sb.append(message);
		return sb.toString();
	}

}
