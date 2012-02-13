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

import java.util.List;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.Group;

/**
 * The Class GroupValueMemento.
 */
public class GroupValueMemento extends ContainerValueMemento implements
		XMLSerializeableMemento {

	/**
	 * Instantiates a new group value memento.
	 * 
	 * @param parent
	 *            the parent
	 */
	public GroupValueMemento(Group parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.ValueMemento#getXML()
	 */
	@Override
	public String getXML() {
		List<FormWidget> childList = (List<FormWidget>) value;
		StringBuilder sb = new StringBuilder();
		for (FormWidget w : childList) {
			ValueMemento m = ((MementoOriginator) w).createValueMemento();
			sb.append(m.getXML());
		}
		return new XMLFragment(
				new XMLFragment(sb.toString()).wrapIn("children"))
				.wrapIn("vmemento").withAttribute("for", name).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.ValueMemento#loadXML(alpha.forms.util.xml.
	 * XMLDocumentSection)
	 */
	@Override
	public void loadXML(XMLDocumentSection xml) {
		if (parent == null)
			return;
		List<XMLDocumentSection> valueSectionList = xml
				.getDocumentSections("children/vmemento");
		for (XMLDocumentSection widgetSection : valueSectionList) {
			String widgetName = widgetSection.getAttribute("for");
			FormWidget w = parent.getWidgetByName(widgetName);

			ValueMemento m = ((MementoOriginator) w).createValueMemento();
			m.loadXML(widgetSection);

			((MementoOriginator) w).setValueMemento(m);
		}
	}

}
