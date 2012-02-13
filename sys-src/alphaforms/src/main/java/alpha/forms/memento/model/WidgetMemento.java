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
package alpha.forms.memento.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.form.event.EventMemento;
import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;

/**
 * The Class WidgetMemento.
 */
public class WidgetMemento implements XMLSerializeableMemento {

	/** The type. */
	protected Class type;

	/** The name. */
	protected String name;

	/** The attributes. */
	protected Map<String, Object> attributes = new HashMap<String, Object>();

	/** The value. */
	protected Object value;

	/** The validators. */
	protected List<ValidatorMemento> validators;

	/** The events. */
	protected List<EventMemento> events;

	/**
	 * Gets the validators.
	 * 
	 * @return the validators
	 */
	public List<ValidatorMemento> getValidators() {
		return validators;
	}

	/**
	 * Sets the validators.
	 * 
	 * @param validators
	 *            the new validators
	 */
	public void setValidators(List<ValidatorMemento> validators) {
		this.validators = validators;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		StringBuilder sb = new StringBuilder();
		for (ValidatorMemento m : validators) {
			sb.append(m.getXML()).append("\n");
		}
		if (events != null) {
			for (EventMemento m : events) {
				sb.append(m.getXML()).append("\n");
			}
		}
		sb.append(renderValue());
		return new XMLFragment(sb.toString()).wrapIn("widget")
				.withAttributes(attributes)
				.withAttribute("type", type.getName())
				.withAttribute("name", name).toString();
	}

	/**
	 * Render value.
	 * 
	 * @return the string
	 */
	protected String renderValue() {
		return new XMLFragment(value).wrapIn("value").toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.XMLSerializeableMemento#loadXML(alpha.forms
	 * .util.xml.XMLDocumentSection)
	 */
	@Override
	public void loadXML(XMLDocumentSection xml) {
		try {
			type = Class.forName(xml.getAttribute("type"));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		name = xml.getAttribute("name");
		for (Entry<String, String> e : xml.getAttributes().entrySet()) {
			attributes.put(e.getKey(), e.getValue());
		}
		List<XMLDocumentSection> validatorSectionList = xml
				.getDocumentSections("validator");
		for (XMLDocumentSection validatorSection : validatorSectionList) {
			ValidatorMemento m = new ValidatorMemento();
			m.loadXML(validatorSection);
			validators.add(m);
		}
		List<XMLDocumentSection> eventSectionList = xml
				.getDocumentSections("event");
		for (XMLDocumentSection eventSection : eventSectionList) {
			EventMemento m = new EventMemento();
			m.loadXML(eventSection);
			events.add(m);
		}
		loadValue(xml);
	}

	/**
	 * Load value.
	 * 
	 * @param xml
	 *            the xml
	 */
	protected void loadValue(XMLDocumentSection xml) {
		if (xml.getSingleSection("value") != null) {
			value = xml.getSingleSection("value").getTextContent();
		}
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Class getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(Class type) {
		this.type = type;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the attributes.
	 * 
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Adds the attribute.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void addAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	/**
	 * Sets the attributes.
	 * 
	 * @param attributes
	 *            the attributes
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Gets the events.
	 * 
	 * @return the events
	 */
	public List<EventMemento> getEvents() {
		return events;
	}

	/**
	 * Sets the events.
	 * 
	 * @param events
	 *            the new events
	 */
	public void setEvents(List<EventMemento> events) {
		this.events = events;
	}

}
