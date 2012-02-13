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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alpha.forms.form.event.Event;
import alpha.forms.form.event.EventFactory;
import alpha.forms.form.event.EventMemento;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.view.ButtonUI;

/**
 * The Class Button.
 */
public class Button extends FormWidget implements MementoOriginator {

	/** The label. */
	@WidgetProperty(description = "This text will show on the button.")
	protected String label;

	/** The on click. */
	@WidgetProperty(description = "This event will fire when the user clicks the button.")
	protected Event onClick;

	/**
	 * Instantiates a new button.
	 * 
	 * @param name
	 *            the name
	 */
	public Button(String name) {
		super(name);
		width = 130;
		height = 30;
		label = name;
		onClick = EventFactory.getInstance().createDefaultEvent(this);
		ui = new ButtonUI(this);
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
		ui.doLayout();
	}

	/**
	 * Gets the on click.
	 * 
	 * @return the on click
	 */
	public Event getOnClick() {
		return onClick;
	}

	/**
	 * Sets the on click.
	 * 
	 * @param onClick
	 *            the new on click
	 */
	public void setOnClick(Event onClick) {
		this.onClick = onClick;
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
		m.setValue("");
		m.addAttribute("label", label);
		m.addAttribute("x", x);
		m.addAttribute("y", y);
		m.addAttribute("width", width);
		m.addAttribute("height", height);
		m.addAttribute("visible", visible);
		m.addAttribute("ui", ui.getClass().getName());
		m.setValidators(validators.createMemento());
		List<EventMemento> events = new ArrayList<EventMemento>();
		EventMemento ev = onClick.createMemento();
		ev.setEventName("onClick");
		events.add(ev);
		m.setEvents(events);
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
			x = Integer.parseInt(attributes.get("x").toString());
			y = Integer.parseInt(attributes.get("y").toString());
			width = Integer.parseInt(attributes.get("width").toString());
			height = Integer.parseInt(attributes.get("height").toString());
			visible = Boolean
					.parseBoolean(attributes.get("visible").toString());
			setSize(width, height);
			setX(x);
			setY(y);
			validators.setMemento(m.getValidators());
			for (EventMemento em : m.getEvents()) {
				if (em.getEventName().equals("onClick")) {
					onClick.setMemento(em);
				}
			}
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		return null;
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
	}

}
