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
package alpha.forms.form.event;

import java.util.ArrayList;
import java.util.List;

import alpha.forms.memento.model.XMLSerializeableMemento;
import alpha.forms.util.xml.XMLDocumentSection;

/**
 * The Class EventMemento.
 */
public class EventMemento implements XMLSerializeableMemento {

	/** The actions. */
	private List<ActionMemento> actions = new ArrayList<ActionMemento>();

	/** The event name. */
	private String eventName;

	/**
	 * Gets the event name.
	 * 
	 * @return the event name
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Sets the event name.
	 * 
	 * @param eventName
	 *            the new event name
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * Gets the actions.
	 * 
	 * @return the actions
	 */
	public List<ActionMemento> getActions() {
		return actions;
	}

	/**
	 * Adds the action.
	 * 
	 * @param m
	 *            the m
	 */
	public void addAction(ActionMemento m) {
		actions.add(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<event name=\"").append(eventName).append("\"");
		if (actions.size() > 0) {
			sb.append(">");
			for (ActionMemento m : actions) {
				sb.append(m.getXML());
			}
			sb.append("</event>");
		} else {
			sb.append(" />\n");
		}
		return sb.toString();
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
		eventName = xml.getAttribute("name");
		List<XMLDocumentSection> xmlActionList = xml
				.getDocumentSections("action");
		for (XMLDocumentSection xmlAction : xmlActionList) {
			ActionMemento m = new ActionMemento();
			m.loadXML(xmlAction);
			actions.add(m);
		}
	}

}
