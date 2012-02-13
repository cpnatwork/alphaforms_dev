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
package alpha.forms.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alpha.forms.designer.util.DraggedWidget;
import alpha.forms.form.naming.WidgetNameExistsException;
import alpha.forms.form.naming.WidgetNamingService;
import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.form.validation.ValidationLevel;
import alpha.forms.memento.model.AlphaFormMemento;
import alpha.forms.memento.model.FormMementoOriginator;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class AlphaForm.
 */
public class AlphaForm implements FormMementoOriginator, WidgetNamingService {

	/** The children. */
	private Map<String, FormWidget> children = new HashMap<String, FormWidget>();

	/** The document states. */
	private List<String> documentStates = new ArrayList<String>();

	/** The active document state. */
	private String activeDocumentState = null;

	/** The widget states. */
	protected Map<String, List<WidgetMemento>> widgetStates = new HashMap<String, List<WidgetMemento>>();

	/** The title. */
	@WidgetProperty(description = "The name of the form.")
	private String title;

	/** The width. */
	@WidgetProperty
	private int width = 600;

	/** The height. */
	@WidgetProperty
	private int height = 400;

	/** The validation level. */
	@WidgetProperty
	private ValidationLevel validationLevel = ValidationLevel.WARN;

	/** The min width. */
	private int minWidth = 10;

	/** The min height. */
	private int minHeight = 10;

	/**
	 * Instantiates a new alpha form.
	 */
	public AlphaForm() {
		title = "AlphaForm1";
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
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the min width.
	 * 
	 * @return the min width
	 */
	public int getMinWidth() {
		return minWidth;
	}

	/**
	 * Gets the min height.
	 * 
	 * @return the min height
	 */
	public int getMinHeight() {
		return minHeight;
	}

	/**
	 * Gets the widgets.
	 * 
	 * @return the widgets
	 */
	public Collection<FormWidget> getWidgets() {
		return children.values();
	}

	/**
	 * Adds the widget.
	 * 
	 * @param w
	 *            the w
	 */
	public void addWidget(FormWidget w) {
		if (isWidget(w.getName())) {

		} else {
			children.put(w.getName(), w);
			recalculateDimensions(w);
		}
	}

	/**
	 * Checks if is widget.
	 * 
	 * @param name
	 *            the name
	 * @return true, if is widget
	 */
	public boolean isWidget(String name) {
		return (children.get(name) != null);
	}

	/**
	 * Gets the widget.
	 * 
	 * @param name
	 *            the name
	 * @return the widget
	 */
	public FormWidget getWidget(String name) {
		return children.get(name);
	}

	/**
	 * Gets the document states.
	 * 
	 * @return the document states
	 */
	public List<String> getDocumentStates() {
		return documentStates;
	}

	/**
	 * Adds the document states.
	 * 
	 * @param states
	 *            the states
	 */
	public void addDocumentStates(Collection<String> states) {
		documentStates.addAll(states);
	}

	/**
	 * Adds the document state.
	 * 
	 * @param state
	 *            the state
	 */
	public void addDocumentState(String state) {
		documentStates.add(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.FormMementoOriginator#createMemento()
	 */
	@Override
	public AlphaFormMemento createMemento() {
		AlphaFormMemento m = new AlphaFormMemento();
		m.setTitle(title);
		m.setAttribute("height", height);
		m.setAttribute("width", width);
		m.setWidgets(new ArrayList<FormWidget>(children.values()));
		m.setWidgetStates(widgetStates);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.FormMementoOriginator#setMemento(alpha.forms
	 * .memento.model.AlphaFormMemento)
	 */
	@Override
	public void setMemento(AlphaFormMemento m) {
		children.clear();
		if (m != null) {
			title = m.getTitle();
			width = Integer.parseInt(m.getAttribute("width").toString());
			height = Integer.parseInt(m.getAttribute("height").toString());
			for (FormWidget w : m.getWidgets()) {
				children.put(w.getName(), w);
			}
		}
	}

	/**
	 * Creates the state.
	 * 
	 * @param name
	 *            the name
	 */
	public void createState(String name) {
		List<WidgetMemento> m = new ArrayList<WidgetMemento>();
		for (FormWidget w : children.values()) {
			if (w instanceof MementoOriginator) {
				m.add(((MementoOriginator) w).createWidgetMemento());
			}
		}
		widgetStates.put(name, m);
	}

	/**
	 * Delete state.
	 * 
	 * @param name
	 *            the name
	 */
	public void deleteState(String name) {
		widgetStates.remove(name);
	}

	/**
	 * Revert to state.
	 * 
	 * @param name
	 *            the name
	 */
	public void revertToState(String name) {
		List<WidgetMemento> mList = widgetStates.get(name);
		if (mList != null) {
			for (WidgetMemento m : mList) {
				FormWidget w = children.get(m.getName());
				if (w != null && w instanceof MementoOriginator) {
					((MementoOriginator) w).setWidgetMemento(m);
				}
			}
		}
	}

	/**
	 * Recalculate dimensions.
	 */
	public void recalculateDimensions() {
		minWidth = 10;
		minHeight = 10;
		width = minWidth;
		height = minHeight;
		for (Entry<String, FormWidget> e : children.entrySet()) {
			recalculateDimensions(e.getValue());
		}
		width += 10;
		height += 10;
	}

	/**
	 * Recalculate dimensions.
	 * 
	 * @param w
	 *            the w
	 */
	public void recalculateDimensions(FormWidget w) {
		minWidth = Math.max(minWidth, w.getX() + w.getWidth());
		minHeight = Math.max(minHeight, w.getY() + w.getHeight());
		width = Math.max(width, minWidth);
		height = Math.max(height, minHeight);
	}

	/**
	 * Recalculate dimensions.
	 * 
	 * @param w
	 *            the w
	 */
	public void recalculateDimensions(DraggedWidget w) {
		minWidth = Math.max(minWidth, w.getX() + w.getWidth());
		minHeight = Math.max(minHeight, w.getY() + w.getHeight());
		width = Math.max(width, minWidth);
		height = Math.max(height, minHeight);
	}

	/**
	 * Gets the active document state.
	 * 
	 * @return the active document state
	 */
	public String getActiveDocumentState() {
		return activeDocumentState;
	}

	/**
	 * Sets the active document state.
	 * 
	 * @param activeDocumentState
	 *            the new active document state
	 */
	public void setActiveDocumentState(String activeDocumentState) {
		if (documentStates.contains(activeDocumentState)) {
			this.activeDocumentState = activeDocumentState;
		}
	}

	/**
	 * Gets the validation level.
	 * 
	 * @return the validation level
	 */
	public ValidationLevel getValidationLevel() {
		return validationLevel;
	}

	/**
	 * Sets the validation level.
	 * 
	 * @param validationLevel
	 *            the new validation level
	 */
	public void setValidationLevel(ValidationLevel validationLevel) {
		this.validationLevel = validationLevel;
	}

	/**
	 * Validate form.
	 * 
	 * @return the list
	 */
	public List<ValidationFailure> validateForm() {
		List<ValidationFailure> validationFailedList = new ArrayList<ValidationFailure>();
		for (FormWidget w : children.values()) {
			validationFailedList.addAll(w.validate());
		}
		return validationFailedList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.naming.WidgetNamingService#checkName(java.lang.String,
	 * alpha.forms.widget.model.FormWidget)
	 */
	@Override
	public String checkName(String name, FormWidget w)
			throws WidgetNameExistsException {
		if (checkNameExists(name))
			throw new WidgetNameExistsException("Widget with name '" + name
					+ "' already exists.");
		String oldName = w.getName();
		FormWidget storedWidget = children.get(oldName);
		if (storedWidget == w && children.remove(oldName) != null) {
			children.put(name, w);
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.naming.WidgetNamingService#checkNameExists(java.lang
	 * .String)
	 */
	@Override
	public boolean checkNameExists(String name) {
		return children.containsKey(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.naming.WidgetNamingService#checkName(java.lang.String)
	 */
	@Override
	public String checkName(String name) throws WidgetNameExistsException {
		if (checkNameExists(name))
			throw new WidgetNameExistsException("Widget with name '" + name
					+ "' already exists.");
		return name;
	}

}
