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
package alpha.forms.util.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.util.SimpleListSerializer;

/**
 * The Class XMLFragment.
 */
public class XMLFragment {

	/** The attributes. */
	private Map<String, Object> attributes = new HashMap<String, Object>();

	/** The value. */
	private String value;

	/** The tag. */
	private String tag;

	/**
	 * Instantiates a new xML fragment.
	 * 
	 * @param value
	 *            the value
	 */
	public XMLFragment(Object value) {
		if (value != null) {
			this.value = value.toString();
		}
	}

	/**
	 * Instantiates a new xML fragment.
	 */
	public XMLFragment() {
		this.value = null;
	}

	/**
	 * With attributes.
	 * 
	 * @param attributes
	 *            the attributes
	 * @return the xML fragment
	 */
	public XMLFragment withAttributes(Map<String, Object> attributes) {
		this.attributes.putAll(attributes);
		return this;
	}

	/**
	 * With attribute.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the xML fragment
	 */
	public XMLFragment withAttribute(String key, Object value) {
		this.attributes.put(key, value);
		return this;
	}

	/**
	 * Wrap in.
	 * 
	 * @param tag
	 *            the tag
	 * @return the xML fragment
	 */
	public XMLFragment wrapIn(String tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * Wrap in cdata.
	 * 
	 * @return the xML fragment
	 */
	public XMLFragment wrapInCDATA() {
		this.value = "<![CDATA[" + this.value + "]]>";
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(tag);
		for (Entry<String, Object> e : attributes.entrySet()) {
			sb.append(" ").append(e.getKey()).append("=\"");
			if (e.getValue() instanceof Collection) {
				SimpleListSerializer sl = new SimpleListSerializer();
				sb.append(sl.serialize((Collection<?>) e.getValue()));
			} else {
				sb.append(e.getValue());
			}
			sb.append("\"");
		}
		if (value != null) {
			sb.append(">").append(value).append("</").append(tag).append(">");
		} else {
			sb.append(" />");
		}
		return sb.toString();
	}

}
