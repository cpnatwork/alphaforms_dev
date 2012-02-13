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

import java.util.ArrayList;
import java.util.List;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.util.WidgetFactory;

/**
 * The Class GroupWidgetMemento.
 */
public class GroupWidgetMemento extends WidgetMemento implements
		XMLSerializeableMemento {

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.WidgetMemento#renderValue()
	 */
	@Override
	protected String renderValue() {
		List<FormWidget> childList = (List<FormWidget>) value;
		StringBuilder sb = new StringBuilder();
		for (FormWidget w : childList) {
			WidgetMemento m = ((MementoOriginator) w).createWidgetMemento();
			sb.append(m.getXML());
		}
		return new XMLFragment(sb.toString()).wrapIn("children").toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.WidgetMemento#loadValue(alpha.forms.util.xml
	 * .XMLDocumentSection)
	 */
	@Override
	protected void loadValue(XMLDocumentSection xml) {
		System.out.println("children");
		List<XMLDocumentSection> widgetSectionList = xml
				.getDocumentSections("children/widget");
		List<FormWidget> childList = new ArrayList<FormWidget>();
		for (XMLDocumentSection widgetSection : widgetSectionList) {
			String widgetName = widgetSection.getAttribute("name");
			String widgetClass = widgetSection.getAttribute("type");
			FormWidget w = WidgetFactory.createWidget(widgetClass, widgetName);

			WidgetMemento m = ((MementoOriginator) w).createWidgetMemento();
			m.loadXML(widgetSection);

			((MementoOriginator) w).setWidgetMemento(m);
			childList.add(w);
		}
		value = childList;
	}

}
