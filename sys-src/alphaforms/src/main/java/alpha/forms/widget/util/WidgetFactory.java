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
package alpha.forms.widget.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.widget.model.FormWidget;

/**
 * A factory for creating Widget objects.
 */
public class WidgetFactory {

	/**
	 * Creates a new Widget object.
	 * 
	 * @param className
	 *            the class name
	 * @param name
	 *            the name
	 * @return the form widget
	 */
	public static FormWidget createWidget(String className, String name) {
		try {
			Class<? extends FormWidget> widgetClass = (Class<? extends FormWidget>) Class
					.forName(className);
			Constructor<? extends FormWidget> c = widgetClass
					.getConstructor(String.class);
			FormWidget w = c.newInstance(name);
			return w;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Clone widget.
	 * 
	 * @param widget
	 *            the widget
	 * @return the form widget
	 */
	public static FormWidget cloneWidget(FormWidget widget) {
		WidgetMemento m = ((MementoOriginator) widget).createWidgetMemento();
		FormWidget clone = createWidget(m.getType().getName(), m.getName());
		if (clone != null) {
			((MementoOriginator) clone).setWidgetMemento(m);
			return clone;
		} else {
			return null;
		}
	}
}
