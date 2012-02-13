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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import alpha.forms.form.AlphaForm;
import alpha.forms.form.event.Event;
import alpha.forms.form.event.EventFactory;
import alpha.forms.form.naming.WidgetNameExistsException;
import alpha.forms.form.naming.WidgetNameGenerator;
import alpha.forms.form.util.AlphaFormProvider;
import alpha.forms.form.validation.ValidationContext;
import alpha.forms.form.validation.ValidationContextImpl;
import alpha.forms.form.validation.ValidationFailure;
import alpha.forms.form.validation.ValidatorGroup;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.view.FormWidgetUI;

/**
 * The Class FormWidget.
 */
public abstract class FormWidget {

	/** The name. */
	@WidgetProperty(description = "The name of the widget.")
	protected String name;

	/** The x. */
	@WidgetProperty(description = "The x coordinate of the widget's left upper corner.")
	protected int x;

	/** The y. */
	@WidgetProperty(description = "The y coordinate of the widget's left upper corner.")
	protected int y;

	/** The width. */
	@WidgetProperty(description = "The width of the widget.")
	protected int width;

	/** The height. */
	@WidgetProperty(description = "The height of the widget.")
	protected int height;

	/** The visible. */
	@WidgetProperty(description = "Visibility of the widget.")
	protected boolean visible = true;

	/** The validators. */
	@WidgetProperty(description = "Validation")
	protected ValidatorGroup validators = new ValidatorGroup();

	/** The on commit change. */
	@WidgetProperty(description = "Will fire when a value is commited")
	protected Event onCommitChange;

	/** The on create. */
	@WidgetProperty(description = "Will fire when a widget is first created")
	protected Event onCreate;

	/** The on validation. */
	@WidgetProperty(description = "Will fire when a widget is validated")
	protected Event onValidation;

	/** The on validation success. */
	@WidgetProperty(description = "Will fire when validation for this widget was successfull")
	protected Event onValidationSuccess;

	/** The on validation failure. */
	@WidgetProperty(description = "Will fire when validation for this widget failed")
	protected Event onValidationFailure;

	/** The on save. */
	@WidgetProperty(description = "Will fire when a widget is saved")
	protected Event onSave;

	/** The ui. */
	protected FormWidgetUI ui;

	/** The parent. */
	protected FormWidget parent;

	/**
	 * Instantiates a new form widget.
	 * 
	 * @param name
	 *            the name
	 */
	public FormWidget(String name) {
		AlphaForm form = AlphaFormProvider.getForm();
		if (form != null) {
			try {
				this.name = form.checkName(name);
			} catch (WidgetNameExistsException e) {
				this.name = WidgetNameGenerator.getName(this);
			}
		}
		onCommitChange = EventFactory.getInstance().createDefaultEvent(this);
		onCreate = EventFactory.getInstance().createDefaultEvent(this);
		onValidation = EventFactory.getInstance().createDefaultEvent(this);
		onValidationSuccess = EventFactory.getInstance().createDefaultEvent(
				this);
		onValidationFailure = EventFactory.getInstance().createDefaultEvent(
				this);
		onSave = EventFactory.getInstance().createDefaultEvent(this);
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return "";
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
	public void setName(String name) throws WidgetNameExistsException {
		AlphaForm form = AlphaFormProvider.getForm();
		this.name = form.checkName(name, this);
	}

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 * 
	 * @param x
	 *            the new x
	 */
	public void setX(int x) {
		this.x = x;
		updateUI();
	}

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 * 
	 * @param y
	 *            the new y
	 */
	public void setY(int y) {
		this.y = y;
		updateUI();
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
		updateUI();
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
		updateUI();
	}

	/**
	 * Gets the ui.
	 * 
	 * @return the ui
	 */
	public FormWidgetUI getUi() {
		return ui;
	}

	/**
	 * Sets the ui.
	 * 
	 * @param ui
	 *            the new ui
	 */
	public void setUi(FormWidgetUI ui) {
		this.ui = ui;
		updateUI();
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public FormWidget getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	public void setParent(FormWidget parent) {
		this.parent = parent;
	}

	/**
	 * Checks if is visible.
	 * 
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible.
	 * 
	 * @param visible
	 *            the new visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		ui.doLayout();
	}

	/**
	 * Gets the minimum height.
	 * 
	 * @return the minimum height
	 */
	public int getMinimumHeight() {
		return 0;
	}

	/**
	 * Gets the minium width.
	 * 
	 * @return the minium width
	 */
	public int getMiniumWidth() {
		return 0;
	}

	/**
	 * Sets the size.
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		ui.doLayout();
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public Dimension getSize() {
		return new Dimension(width, height);
	}

	/**
	 * Gets the validators.
	 * 
	 * @return the validators
	 */
	public ValidatorGroup getValidators() {
		return validators;
	}

	/**
	 * Sets the validators.
	 * 
	 * @param validators
	 *            the new validators
	 */
	public void setValidators(ValidatorGroup validators) {
		this.validators = validators;
	}

	/**
	 * Gets the bounds.
	 * 
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Validate.
	 * 
	 * @return the sets the
	 */
	public Set<ValidationFailure> validate() {
		return new HashSet<ValidationFailure>();
	}

	/**
	 * Validate value.
	 * 
	 * @param value
	 *            the value
	 * @return the sets the
	 */
	protected Set<ValidationFailure> validateValue(Object value) {
		ValidationContext ctx = new ValidationContextImpl(
				AlphaFormProvider.getForm(), this);
		return validators.validateAll(ctx, value);
	}

	/**
	 * Update ui.
	 */
	protected void updateUI() {
		ui.setSize(new Dimension(width, height));
		ui.setLocation(x, y);
		ui.updateUI();
	}

	/**
	 * Gets the on commit change.
	 * 
	 * @return the on commit change
	 */
	public Event getOnCommitChange() {
		return onCommitChange;
	}

	/**
	 * Sets the on commit change.
	 * 
	 * @param onCommitChange
	 *            the new on commit change
	 */
	public void setOnCommitChange(Event onCommitChange) {
		this.onCommitChange = onCommitChange;
	}

	/**
	 * Gets the on create.
	 * 
	 * @return the on create
	 */
	public Event getOnCreate() {
		return onCreate;
	}

	/**
	 * Sets the on create.
	 * 
	 * @param onCreate
	 *            the new on create
	 */
	public void setOnCreate(Event onCreate) {
		this.onCreate = onCreate;
	}

	/**
	 * Gets the on validation.
	 * 
	 * @return the on validation
	 */
	public Event getOnValidation() {
		return onValidation;
	}

	/**
	 * Sets the on validation.
	 * 
	 * @param onValidation
	 *            the new on validation
	 */
	public void setOnValidation(Event onValidation) {
		this.onValidation = onValidation;
	}

	/**
	 * Gets the on validation success.
	 * 
	 * @return the on validation success
	 */
	public Event getOnValidationSuccess() {
		return onValidationSuccess;
	}

	/**
	 * Sets the on validation success.
	 * 
	 * @param onValidationSuccess
	 *            the new on validation success
	 */
	public void setOnValidationSuccess(Event onValidationSuccess) {
		this.onValidationSuccess = onValidationSuccess;
	}

	/**
	 * Gets the on validation failure.
	 * 
	 * @return the on validation failure
	 */
	public Event getOnValidationFailure() {
		return onValidationFailure;
	}

	/**
	 * Sets the on validation failure.
	 * 
	 * @param onValidationFailure
	 *            the new on validation failure
	 */
	public void setOnValidationFailure(Event onValidationFailure) {
		this.onValidationFailure = onValidationFailure;
	}

	/**
	 * Gets the on save.
	 * 
	 * @return the on save
	 */
	public Event getOnSave() {
		return onSave;
	}

	/**
	 * Sets the on save.
	 * 
	 * @param onSave
	 *            the new on save
	 */
	public void setOnSave(Event onSave) {
		this.onSave = onSave;
	}

}
