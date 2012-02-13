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
package alpha.forms.form.event;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.ContainerWidget;

/**
 * The Class ScriptedAction.
 */
public class ScriptedAction implements Action {

	/** The script code. */
	protected String scriptCode = "";

	/** The js engine. */
	protected ScriptEngine jsEngine;

	/**
	 * Instantiates a new scripted action.
	 * 
	 * @param jsEngine
	 *            the js engine
	 */
	public ScriptedAction(ScriptEngine jsEngine) {
		this.jsEngine = jsEngine;
	}

	/**
	 * Gets the script code.
	 * 
	 * @return the script code
	 */
	public String getScriptCode() {
		return scriptCode;
	}

	/**
	 * Sets the script code.
	 * 
	 * @param scriptCode
	 *            the new script code
	 */
	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Action#execute(alpha.forms.form.event.Event)
	 */
	@Override
	public void execute(Event event) {
		String funcName = "action_" + this.hashCode();
		for (FormWidget w : event.getAlphaForm().getWidgets()) {
			jsEngine.put(w.getName(), w);
			if (w instanceof ContainerWidget) {
				ContainerWidget co = (ContainerWidget) w;
				for (FormWidget cw : co.getChildren()) {
					jsEngine.put(w.getName() + "$" + cw.getName(), cw);
				}
			}
		}
		if (jsEngine != null) {
			try {
				StringBuilder sb = new StringBuilder();
				// afe__execute is a wrapper to make "this" point to the widget
				// in the action's javascript.
				// ctx is the AlphaForm
				sb.append("function afe__execute(funcName, widget, context) {"
						+ "this[funcName].call(widget, context);" + "}");
				sb.append("function " + funcName + "(ctx) {" + scriptCode + "}");
				jsEngine.eval(sb.toString());
				((Invocable) jsEngine).invokeFunction("afe__execute", funcName,
						event.getSource(), event.getAlphaForm());
			} catch (ScriptException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "action_" + this.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.form.event.Action#createMemento()
	 */
	@Override
	public ActionMemento createMemento() {
		ActionMemento m = new ActionMemento();
		m.setName(toString());
		m.setScriptCode(scriptCode);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.form.event.Action#setMemento(alpha.forms.form.event.ActionMemento
	 * )
	 */
	@Override
	public void setMemento(ActionMemento m) {
		scriptCode = m.getScriptCode();
	}

}
