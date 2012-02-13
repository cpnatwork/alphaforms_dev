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
package alpha.forms.application.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import alpha.forms.form.AlphaForm;
import alpha.forms.memento.model.AlphaFormMemento;
import alpha.forms.memento.model.FormMementoOriginator;
import alpha.forms.util.xml.XMLDocumentSection;

/**
 * The Class PersistenceController.
 */
public class PersistenceController {

	/** The controller. */
	protected ApplicationController controller;

	/**
	 * Instantiates a new persistence controller.
	 * 
	 * @param controller
	 *            the controller
	 */
	public PersistenceController(ApplicationController controller) {
		this.controller = controller;
	}

	/**
	 * Store form.
	 * 
	 * @param form
	 *            the form
	 * @param state
	 *            the state
	 */
	public void storeForm(AlphaForm form, String state) {
		AlphaFormMemento m = ((FormMementoOriginator) form).createMemento();
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(data);
		if (state == null) {
			writer.print(m.getXML());
		} else {
			writer.print(m.getXML(state));
		}
		writer.close();
		controller.fireSaveEvent(data);
	}

	/**
	 * Load form.
	 * 
	 * @param data
	 *            the data
	 * @return the alpha form
	 */
	public AlphaForm loadForm(InputStream data) {
		AlphaForm form = new AlphaForm();
		try {
			Document xmlDoc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(data);
			xmlDoc.getDocumentElement().normalize();
			AlphaFormMemento m = new AlphaFormMemento();
			m.loadXML(new XMLDocumentSection(xmlDoc.getDocumentElement(),
					xmlDoc));
			form.setMemento(m);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		form.createState("loadFinished");
		return form;
	}

}
