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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.util.WidgetFactory;

/**
 * The Class AlphaFormMemento.
 */
public class AlphaFormMemento implements XMLSerializeableMemento {

	/** The title. */
	protected String title;

	/** The widgets. */
	protected List<FormWidget> widgets = new ArrayList<FormWidget>();

	/** The widget states. */
	protected Map<String, List<WidgetMemento>> widgetStates = new HashMap<String, List<WidgetMemento>>();

	/** The attributes. */
	protected Map<String, Object> attributes = new HashMap<String, Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.XMLSerializeableMemento#getXML()
	 */
	@Override
	public String getXML() {
		List<WidgetMemento> widgetMementoList = new ArrayList<WidgetMemento>();
		for (FormWidget w : widgets) {
			if (w instanceof MementoOriginator) {
				MementoOriginator o = (MementoOriginator) w;
				widgetMementoList.add(o.createWidgetMemento());
			}
		}
		return compileXML(widgetMementoList, null, null);
	}

	/**
	 * Gets the xML.
	 * 
	 * @param state
	 *            the state
	 * @return the xML
	 */
	public String getXML(String state) {
		List<WidgetMemento> widgetMementoList = widgetStates.get(state);
		List<DynamicAttributeMemento> deltaList = getAttributeDelta(state);
		List<ValueMemento> valueList = new ArrayList<ValueMemento>();
		for (FormWidget w : widgets) {
			MementoOriginator o = (MementoOriginator) w;
			ValueMemento vm = o.createValueMemento();
			if (vm != null)
				valueList.add(vm);
		}
		return compileXML(widgetMementoList, deltaList, valueList);
	}

	/**
	 * Compile xml.
	 * 
	 * @param widgetList
	 *            the widget list
	 * @param deltaList
	 *            the delta list
	 * @param valueList
	 *            the value list
	 * @return the string
	 */
	protected String compileXML(List<WidgetMemento> widgetList,
			List<DynamicAttributeMemento> deltaList,
			List<ValueMemento> valueList) {
		StringBuilder out = new StringBuilder();
		out.append(new XMLFragment(new XMLFragment(title).wrapIn("title"))
				.wrapIn("meta"));
		String pbox = "";
		for (WidgetMemento wm : widgetList) {
			pbox += wm.getXML();
		}
		out.append(new XMLFragment(pbox).wrapIn("pbox"));

		if (deltaList != null) {
			String sbox = "";
			for (DynamicAttributeMemento dam : deltaList) {
				sbox += dam.getXML();
			}
			out.append(new XMLFragment(sbox).wrapIn("sbox"));
		}

		if (valueList != null) {
			String vbox = "";
			for (ValueMemento vm : valueList) {
				vbox += vm.getXML();
			}
			out.append(new XMLFragment(vbox).wrapIn("vbox"));
		}

		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
				+ new XMLFragment(out.toString()).wrapIn("alphaForm")
						.withAttributes(attributes);
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
		if (xml.getSectionName().equals("alphaForm")) {
			XMLDocumentSection metaSection = xml.getSingleSection("meta[1]");
			if (metaSection != null) {
				title = metaSection.getNodeValue("title");
			}
			for (Entry<String, String> e : xml.getAttributes().entrySet()) {
				attributes.put(e.getKey(), e.getValue());
			}
			List<XMLDocumentSection> widgetSectionList = xml
					.getDocumentSections("pbox/widget");
			for (XMLDocumentSection widgetSection : widgetSectionList) {
				String widgetName = widgetSection.getAttribute("name");
				String widgetClass = widgetSection.getAttribute("type");
				FormWidget w = WidgetFactory.createWidget(widgetClass,
						widgetName);

				WidgetMemento m = ((MementoOriginator) w).createWidgetMemento();
				m.loadXML(widgetSection);

				((MementoOriginator) w).setWidgetMemento(m);
				widgets.add(w);
			}
			XMLDocumentSection xmlSBox = xml.getSingleSection("sbox");
			if (xmlSBox != null) {
				List<XMLDocumentSection> sboxList = xmlSBox
						.getDocumentSections("smemento");
				for (XMLDocumentSection smem : sboxList) {
					String wname = smem.getAttribute("for");
					FormWidget w = getWidgetByName(wname);
					if (w != null) {
						DynamicAttributeMemento m = new DynamicAttributeMemento();
						m.loadXML(smem);
						((MementoOriginator) w).setDynamicMemento(m);
					}
				}

			}
			XMLDocumentSection xmlVBox = xml.getSingleSection("vbox");
			if (xmlVBox != null) {
				List<XMLDocumentSection> vboxList = xmlVBox
						.getDocumentSections("vmemento");
				for (XMLDocumentSection vmem : vboxList) {
					String wname = vmem.getAttribute("for");
					FormWidget w = getWidgetByName(wname);
					if (w != null) {
						ValueMemento m = ((MementoOriginator) w)
								.createValueMemento();
						m.loadXML(vmem);
						((MementoOriginator) w).setValueMemento(m);
					}
				}

			}
		}
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the attribute.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	/**
	 * Gets the attribute.
	 * 
	 * @param key
	 *            the key
	 * @return the attribute
	 */
	public Object getAttribute(String key) {
		return attributes.get(key);
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
	 * Gets the widgets.
	 * 
	 * @return the widgets
	 */
	public List<FormWidget> getWidgets() {
		return widgets;
	}

	/**
	 * Sets the widgets.
	 * 
	 * @param widgets
	 *            the new widgets
	 */
	public void setWidgets(List<FormWidget> widgets) {
		this.widgets = widgets;
	}

	/**
	 * Gets the widget by name.
	 * 
	 * @param name
	 *            the name
	 * @return the widget by name
	 */
	protected FormWidget getWidgetByName(String name) {
		for (FormWidget w : widgets) {
			if (w.getName().equals(name)) {
				return w;
			}
		}
		return null;
	}

	/**
	 * Gets the widget states.
	 * 
	 * @return the widget states
	 */
	public Map<String, List<WidgetMemento>> getWidgetStates() {
		return widgetStates;
	}

	/**
	 * Sets the widget states.
	 * 
	 * @param widgetStates
	 *            the widget states
	 */
	public void setWidgetStates(Map<String, List<WidgetMemento>> widgetStates) {
		this.widgetStates = widgetStates;
	}

	/**
	 * Calculates the delta between the current AlphaForm widgets and previous
	 * saved state identified by the name parameter.
	 * 
	 * @param name
	 *            identifier for the state against which the delta is calculated
	 * @return list of DynamicAttributeMemento objects
	 */
	public List<DynamicAttributeMemento> getAttributeDelta(String name) {
		List<WidgetMemento> oldList = widgetStates.get(name);
		List<DynamicAttributeMemento> deltaList = new ArrayList<DynamicAttributeMemento>();
		for (FormWidget w : widgets) {
			WidgetMemento mCur = ((MementoOriginator) w).createWidgetMemento();
			for (WidgetMemento mOld : oldList) {
				if (mCur.getName().equals(mOld.getName())) {
					DynamicAttributeMemento delta = new DynamicAttributeMemento();
					delta.setName(mCur.getName());
					Map<String, Object> mCurAttributes = mCur.getAttributes();
					for (Entry<String, Object> eOld : mOld.getAttributes()
							.entrySet()) {
						if (!eOld.getValue().equals(
								mCurAttributes.get(eOld.getKey()))) {
							delta.addAttribute(eOld.getKey(),
									mCurAttributes.get(eOld.getKey()));
						}
					}
					if (!delta.getAttributes().isEmpty()) {
						deltaList.add(delta);
					}
				}
			}
		}
		return deltaList;
	}

}
