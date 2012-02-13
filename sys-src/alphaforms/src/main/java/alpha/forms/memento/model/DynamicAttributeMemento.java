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
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;

/**
 * The Class DynamicAttributeMemento.
 */
public class DynamicAttributeMemento implements XMLSerializeableMemento {

	/** The name. */
	protected String name;

	/** The attributes. */
	protected Map<String, Object> attributes = new HashMap<String, Object>();

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
	 * Gets the attribute.
	 * 
	 * @param name
	 *            the name
	 * @return the attribute
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Checks if is stored.
	 * 
	 * @param name
	 *            the name
	 * @return true, if is stored
	 */
	public boolean isStored(String name) {
		return (attributes.get(name) != null);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		String out = "";
		for (Entry<String, Object> e : attributes.entrySet()) {
			out += new XMLFragment(e.getValue()).wrapIn("attribute")
					.withAttribute("name", e.getKey());

		}
		return new XMLFragment(out).wrapIn("smemento")
				.withAttribute("for", name).toString();
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
		// TODO Auto-generated method stub

	}

}
