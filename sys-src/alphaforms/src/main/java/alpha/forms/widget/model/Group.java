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
package alpha.forms.widget.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.GroupValueMemento;
import alpha.forms.memento.model.GroupWidgetMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.model.container.AbstractContainerWidget;
import alpha.forms.widget.util.WidgetFactory;
import alpha.forms.widget.view.GroupUI;

/**
 * The Class Group.
 */
public class Group extends AbstractContainerWidget implements MementoOriginator {

	/** The title. */
	@WidgetProperty
	private String title;

	/** The border. */
	@WidgetProperty
	private BorderType border = BorderType.TITLED;

	/**
	 * Instantiates a new group.
	 * 
	 * @param name
	 *            the name
	 */
	public Group(String name) {
		super(name);
		title = name;
		width = 150;
		height = 60;
		ui = new GroupUI(this);
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
	 * Gets the border.
	 * 
	 * @return the border
	 */
	public BorderType getBorder() {
		return border;
	}

	/**
	 * Sets the border.
	 * 
	 * @param border
	 *            the new border
	 */
	public void setBorder(BorderType border) {
		this.border = border;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMinimumHeight()
	 */
	@Override
	public int getMinimumHeight() {
		return 55;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getMiniumWidth()
	 */
	@Override
	public int getMiniumWidth() {
		return 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#getDescription()
	 */
	@Override
	public String getDescription() {
		return "A option group.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createWidgetMemento()
	 */
	@Override
	public WidgetMemento createWidgetMemento() {
		WidgetMemento m = new GroupWidgetMemento();
		m.setName(name);
		m.setType(this.getClass());
		m.setValidators(validators.createMemento());
		m.addAttribute("title", title);
		m.addAttribute("border", border);
		m.addAttribute("x", x);
		m.addAttribute("y", y);
		m.addAttribute("width", width);
		m.addAttribute("height", height);
		m.addAttribute("ui", ui.getClass().getName());
		List<FormWidget> copyChildList = new ArrayList<FormWidget>();
		for (FormWidget w : childWidgets) {
			copyChildList.add(WidgetFactory.cloneWidget(w));
		}
		m.setValue(copyChildList);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setWidgetMemento(alpha.forms
	 * .memento.model.WidgetMemento)
	 */
	@Override
	public void setWidgetMemento(WidgetMemento m) {
		if (m != null) {
			Iterator<FormWidget> i = childWidgets.iterator();
			while (i.hasNext()) {
				FormWidget w = i.next();
				i.remove();
				removeChild(w);
			}
			name = m.getName();
			for (FormWidget w : (List<FormWidget>) m.getValue()) {
				addChild(WidgetFactory.cloneWidget(w));
			}
			Map<String, Object> attributes = m.getAttributes();
			title = attributes.get("title").toString();
			border = BorderType.valueOf(attributes.get("border").toString());
			x = Integer.parseInt(attributes.get("x").toString());
			y = Integer.parseInt(attributes.get("y").toString());
			width = Integer.parseInt(attributes.get("width").toString());
			height = Integer.parseInt(attributes.get("height").toString());
			setSize(width, height);
			setX(x);
			setY(y);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.AbstractContainerWidget#addChild(alpha
	 * .forms.widget.model.FormWidget)
	 */
	@Override
	public void addChild(FormWidget w) {
		super.addChild(w);
		((GroupUI) ui).add(w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.widget.model.container.AbstractContainerWidget#removeChild
	 * (alpha.forms.widget.model.FormWidget)
	 */
	@Override
	public void removeChild(FormWidget w) {
		((GroupUI) ui).remove(w);
		super.removeChild(w);
		ui.doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#createDynamicAttributeMemento
	 * (alpha.forms.memento.model.WidgetMemento)
	 */
	@Override
	public DynamicAttributeMemento createDynamicAttributeMemento(
			WidgetMemento ref) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setDynamicMemento(alpha.forms
	 * .memento.model.DynamicAttributeMemento)
	 */
	@Override
	public void setDynamicMemento(DynamicAttributeMemento m) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		ValueMemento m = new GroupValueMemento(this);
		m.setName(name);
		List<FormWidget> clonedWidgets = new ArrayList<FormWidget>();
		for (FormWidget w : childWidgets) {
			clonedWidgets.add(WidgetFactory.cloneWidget(w));
		}
		m.setValue(new ArrayList<FormWidget>(clonedWidgets));
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setValueMemento(alpha.forms
	 * .memento.model.ValueMemento)
	 */
	@Override
	public void setValueMemento(ValueMemento m) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.model.FormWidget#validate()
	 */
	@Override
	public Set<ValidationFailure> validate() {
		Set<ValidationFailure> failures = new HashSet<ValidationFailure>();
		for (FormWidget w : childWidgets) {
			failures.addAll(w.validate());
		}
		return failures;
	}

}
