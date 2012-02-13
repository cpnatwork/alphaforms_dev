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
import alpha.forms.widget.model.Option.OptionItem;

/**
 * The Class OptionValueMemento.
 */
public class OptionValueMemento extends ValueMemento {

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.ValueMemento#getXML()
	 */
	@Override
	public String getXML() {
		StringBuilder out = new StringBuilder();
		for (OptionItem i : (List<OptionItem>) value) {
			out.append(new XMLFragment(i.getName()).wrapIn("option")
					.withAttribute("value", i.isValue()));
		}
		return new XMLFragment(new XMLFragment(out).wrapIn("options"))
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
		if (xml != null) {
			List<OptionItem> optionList = new ArrayList<OptionItem>();
			List<XMLDocumentSection> xmlOptionList = xml
					.getDocumentSections("options/option");
			for (XMLDocumentSection xmlOption : xmlOptionList) {
				OptionItem option = new OptionItem();
				option.setName(xmlOption.getTextContent());
				option.setValue(Boolean.parseBoolean(xmlOption
						.getAttribute("value")));
				optionList.add(option);
			}
			value = optionList;
		}
	}
}
