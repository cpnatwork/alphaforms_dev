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
package alpha.forms.widget.model;

import java.util.Map;
import java.util.Set;

import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.util.WidgetLabelPosition;
import alpha.forms.widget.view.TextFieldUI;

/**
 * The Class TextField.
 */
public class TextField extends FormWidget implements MementoOriginator {

	/** The label. */
	@WidgetProperty(description = "The label text.")
	protected String label;

	/** The show label. */
	@WidgetProperty(name = "labelOrientation", description = "Determines the position of the label in relation to the text input field.")
	protected WidgetLabelPosition showLabel = WidgetLabelPosition.LEFT;

	/** The text. */
	@WidgetProperty(description = "The content of this widget.")
	protected String text;

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
		ui.updateUI();
	}

	/**
	 * Instantiates a new text field.
	 * 
	 * @param name
	 *            the name
	 */
	public TextField(String name) {
		super(name);
		width = 200;
		height = 22;
		label = name;
		ui = new TextFieldUI(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#validate()
	 */
	@Override
	public Set<ValidationFailure> validate() {
		return validateValue(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getDescription()
	 */
	@Override
	public String getDescription() {
		return "A text input field.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMinimumHeight()
	 */
	@Override
	public int getMinimumHeight() {
		return ui.getMinimumSize().height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMiniumWidth()
	 */
	@Override
	public int getMiniumWidth() {
		return 300;
	}

	/**
	 * Gets the show label.
	 * 
	 * @return the show label
	 */
	public WidgetLabelPosition getShowLabel() {
		return showLabel;
	}

	/**
	 * Sets the show label.
	 * 
	 * @param showLabel
	 *            the new show label
	 */
	public void setShowLabel(WidgetLabelPosition showLabel) {
		this.showLabel = showLabel;
		ui.updateUI();
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
	 * Sets the label.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(String label) {
		this.label = label;
		ui.revalidate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createWidgetMemento()
	 */
	@Override
	public WidgetMemento createWidgetMemento() {
		WidgetMemento m = new WidgetMemento();
		m.setName(this.name);
		m.setType(this.getClass());
		m.setValue(this.getText());
		m.addAttribute("label", label);
		m.addAttribute("showLabel", showLabel);
		m.addAttribute("x", x);
		m.addAttribute("y", y);
		m.addAttribute("width", width);
		m.addAttribute("height", height);
		m.addAttribute("ui", ui.getClass().getName());
		m.addAttribute("visible", visible);
		m.setValidators(validators.createMemento());
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setWidgetMemento(alpha.forms
	 * .memento.model.WidgetMemento)
	 */
	@Override
	public void setWidgetMemento(WidgetMemento m) {
		if (m != null) {
			name = m.getName();
			Map<String, Object> attributes = m.getAttributes();
			label = attributes.get("label").toString();
			showLabel = WidgetLabelPosition.valueOf(attributes.get("showLabel")
					.toString());
			x = Integer.parseInt(attributes.get("x").toString());
			y = Integer.parseInt(attributes.get("y").toString());
			width = Integer.parseInt(attributes.get("width").toString());
			height = Integer.parseInt(attributes.get("height").toString());
			visible = Boolean
					.parseBoolean(attributes.get("visible").toString());
			setSize(width, height);
			setX(x);
			setY(y);
			text = (m.getValue() == null) ? "" : m.getValue().toString();
			validators.setMemento(m.getValidators());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#createDynamicAttributeMemento
	 * (alpha.forms.memento.model.WidgetMemento)
	 */
	@Override
	public DynamicAttributeMemento createDynamicAttributeMemento(
			WidgetMemento ref) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setDynamicMemento(alpha.forms
	 * .memento.model.DynamicAttributeMemento)
	 */
	@Override
	public void setDynamicMemento(DynamicAttributeMemento m) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		ValueMemento v = new ValueMemento();
		v.setName(name);
		v.setValue(text);
		return v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setValueMemento(alpha.forms
	 * .memento.model.ValueMemento)
	 */
	@Override
	public void setValueMemento(ValueMemento m) {
		text = m.getValue().toString();
	}

}
