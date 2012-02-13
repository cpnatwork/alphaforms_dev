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
package alpha.forms.propertyEditor.model;

import java.lang.reflect.Method;

/**
 * The Class WidgetProperty.
 */
public class WidgetProperty {

	/** The name. */
	private String name;

	/** The category. */
	private String category;

	/** The description. */
	private String description;

	/** The value. */
	private Object value;

	/** The readonly. */
	private boolean readonly = false;

	/** The setter. */
	private Method setter;

	/** The getter. */
	private Method getter;

	/** The type. */
	private Class type;

	/** The generic type. */
	private Class genericType;

	/** The is multiselect invalid. */
	private boolean isMultiselectInvalid = false;

	/**
	 * Checks if is multiselect invalid.
	 * 
	 * @return true, if is multiselect invalid
	 */
	public boolean isMultiselectInvalid() {
		return isMultiselectInvalid;
	}

	/**
	 * Sets the multiselect invalid.
	 * 
	 * @param isMultiselectInvalid
	 *            the new multiselect invalid
	 */
	public void setMultiselectInvalid(boolean isMultiselectInvalid) {
		this.isMultiselectInvalid = isMultiselectInvalid;
	}

	/**
	 * Gets the setter.
	 * 
	 * @return the setter
	 */
	public Method getSetter() {
		return setter;
	}

	/**
	 * Sets the setter.
	 * 
	 * @param setter
	 *            the new setter
	 */
	public void setSetter(Method setter) {
		this.setter = setter;
	}

	/**
	 * Gets the getter.
	 * 
	 * @return the getter
	 */
	public Method getGetter() {
		return getter;
	}

	/**
	 * Sets the getter.
	 * 
	 * @param getter
	 *            the new getter
	 */
	public void setGetter(Method getter) {
		this.getter = getter;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Class getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(Class type) {
		this.type = type;
	}

	/**
	 * Checks if is readonly.
	 * 
	 * @return true, if is readonly
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * Sets the readonly.
	 * 
	 * @param readonly
	 *            the new readonly
	 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the category.
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Gets the generic type.
	 * 
	 * @return the generic type
	 */
	public Class getGenericType() {
		return genericType;
	}

	/**
	 * Sets the generic type.
	 * 
	 * @param genericType
	 *            the new generic type
	 */
	public void setGenericType(Class genericType) {
		this.genericType = genericType;
	}

}