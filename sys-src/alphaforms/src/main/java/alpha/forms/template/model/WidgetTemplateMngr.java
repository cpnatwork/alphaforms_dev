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
package alpha.forms.template.model;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import alpha.forms.template.WidgetTemplateManager;
import alpha.forms.template.exception.TemplateWithNameExistsException;
import alpha.forms.util.xml.XMLDocumentSection;
import alpha.forms.util.xml.XMLFragment;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.util.WidgetFactory;

/**
 * The Class WidgetTemplateMngr.
 */
public class WidgetTemplateMngr extends DefaultListModel implements
		WidgetTemplateManager {

	/** The widgets. */
	private Vector<WidgetTemplate> widgets = new Vector<WidgetTemplate>();

	/** The instance. */
	private static WidgetTemplateMngr instance = null;

	/**
	 * Gets the single instance of WidgetTemplateMngr.
	 * 
	 * @return single instance of WidgetTemplateMngr
	 */
	public static synchronized WidgetTemplateMngr getInstance() {
		if (instance == null) {
			instance = new WidgetTemplateMngr();
		}
		return instance;
	}

	/**
	 * Instantiates a new widget template mngr.
	 */
	private WidgetTemplateMngr() {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt(int index) {
		return widgets.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultListModel#getSize()
	 */
	@Override
	public int getSize() {
		return widgets.size();
	}

	/**
	 * Gets the.
	 * 
	 * @param name
	 *            the name
	 * @return the widget template
	 */
	public WidgetTemplate get(String name) {
		for (WidgetTemplate t : widgets) {
			if (t.getTemplateName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Adds the template.
	 * 
	 * @param w
	 *            the w
	 */
	public void addTemplate(WidgetTemplate w) {
		widgets.add(w);
		this.fireIntervalAdded(this, widgets.indexOf(w), widgets.indexOf(w));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.template.WidgetTemplateManager#saveAsTemplate(alpha.forms
	 * .widget.model.FormWidget, java.lang.String, boolean)
	 */
	@Override
	public void saveAsTemplate(FormWidget widget, String templateName,
			boolean forceOverwrite) throws TemplateWithNameExistsException {
		FormWidget clone = WidgetFactory.cloneWidget(widget);
		if (clone != null) {
			WidgetTemplate wt = new WidgetTemplate(clone, templateName);
			wt.setTemplateName(templateName);
			if (forceOverwrite == false && get(templateName) != null) {
				throw new TemplateWithNameExistsException(
						"A template with the name " + templateName
								+ " already exists.");
			}
			addTemplate(wt);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.template.WidgetTemplateManager#getWidgetFromTemplate(java
	 * .lang.String)
	 */
	@Override
	public FormWidget getWidgetFromTemplate(String templateName) {
		WidgetTemplate wt = get(templateName);
		return wt.createWidgetFromTemplate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.template.WidgetTemplateManager#storeTemplates(java.io.
	 * OutputStream)
	 */
	public void storeTemplates(OutputStream out) {
		BufferedOutputStream bout = new BufferedOutputStream(out);
		String templateOut = "";
		for (WidgetTemplate wt : widgets) {
			templateOut += wt.saveAsXML();
		}
		String data = new XMLFragment(templateOut).wrapIn("alphaFormTemplates")
				.toString();
		try {
			bout.write(data.getBytes("UTF-8"));
			bout.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.template.WidgetTemplateManager#loadTemplates(java.io.InputStream
	 * )
	 */
	public void loadTemplates(InputStream in) {
		try {
			Document xmlDoc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(in);
			xmlDoc.getDocumentElement().normalize();

			XMLDocumentSection templateContainer = new XMLDocumentSection(
					xmlDoc.getDocumentElement(), xmlDoc);
			for (XMLDocumentSection sec : templateContainer
					.getDocumentSections("widgetTemplate")) {
				addTemplate(WidgetTemplate.createFromXML(sec));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
