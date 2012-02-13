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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import alpha.forms.memento.model.ValidatorMemento;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.TextField;

/**
 * The Class NumberValidator.
 */
public class NumberValidator implements Validator {

	/** The error message. */
	protected final String errorMessage = "'%s' is not a number.";

	/** The number type. */
	private List<NumberType> numberType = new ArrayList<NumberType>();

	/** The error. */
	private String error = "";

	/**
	 * Instantiates a new number validator.
	 */
	public NumberValidator() {
		super();
		for (NumberType t : NumberType.values()) {
			numberType.add(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#validate(alpha.forms.form.validation
	 * .ValidationContext, java.lang.Object)
	 */
	@Override
	public boolean validate(ValidationContext ctx, Object value) {
		boolean validates = false;
		if (numberType.contains(NumberType.ntInteger)) {
			try {
				Integer.parseInt((String) value);
				validates = true;
			} catch (Exception e) {
			}
		}
		if (numberType.contains(NumberType.ntDouble)) {
			try {
				Double.parseDouble((String) value);
				validates = true;
			} catch (Exception e) {
			}
		}
		if (numberType.contains(NumberType.ntFloat)) {
			try {
				Float.parseFloat((String) value);
				validates = true;
			} catch (Exception e) {
			}
		}
		if (!validates) {
			error = String.format(errorMessage,
					value == null ? null : value.toString());
		}
		return validates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getOptionUI()
	 */
	@Override
	public JPanel getOptionUI() {
		JPanel panel = new JPanel(new FlowLayout());
		for (final NumberType t : NumberType.values()) {
			final JCheckBox cb = new JCheckBox();
			cb.setText(t.getLabel());
			cb.setSelected(numberType.contains(t));
			cb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					if (cb.isSelected()) {
						numberType.add(t);
					} else {
						if (numberType.size() == 1) {
							cb.setSelected(true);
							return;
						}
						if (numberType.contains(t))
							numberType.remove(t);
					}
				}
			});
			panel.add(cb);
		}
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getSettingsMap()
	 */
	@Override
	public Map<String, String> getSettingsMap() {
		HashMap<String, String> settings = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (NumberType t : numberType) {
			if (!first) {
				sb.append(",");
			}
			sb.append(t.getValue());
			first = false;
		}
		settings.put("numberType", sb.toString());
		return settings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.validation.Validator#isCompatibleWith(java.lang.Class)
	 */
	@Override
	public boolean isCompatibleWith(Class<? extends FormWidget> widgetType) {
		return widgetType.equals(TextField.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.validation.Validator#getHandle()
	 */
	@Override
	public String getHandle() {
		return "number";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Validator clone() {
		return new NumberValidator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getHandle();
	}

	/**
	 * The Enum NumberType.
	 */
	protected enum NumberType {

		/** The nt double. */
		ntDouble("Double"),

		/** The nt float. */
		ntFloat("Float"),

		/** The nt integer. */
		ntInteger("Integer");

		/** The label. */
		private String label;

		/**
		 * Instantiates a new number type.
		 * 
		 * @param label
		 *            the label
		 */
		NumberType(String label) {
			this.label = label;
		}

		/**
		 * Gets the label.
		 * 
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public String getValue() {
			return this.name();
		}
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
		m.addAttribute("numberType", numberType);
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
		numberType.clear();
		System.out.println("setMemento");
		String valueString = m.getAttributes().get("numberType").toString();
		if (valueString != null && valueString.length() > 2) {
			valueString = valueString.substring(1, valueString.length() - 1);
			String[] values = valueString.split(",");
			for (String value : values) {
				numberType.add(NumberType.valueOf(value));
			}
		}
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
