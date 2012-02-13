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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * A factory for creating Action objects.
 */
public class ActionFactory {

	/** The sgm. */
	protected static ScriptEngineManager sgm = new ScriptEngineManager();

	/** The factory. */
	protected static ActionFactory factory;

	/** The engine. */
	protected ScriptEngine engine;

	/**
	 * Gets the single instance of ActionFactory.
	 * 
	 * @return single instance of ActionFactory
	 */
	public static ActionFactory getInstance() {
		if (factory == null)
			factory = new ActionFactory();
		return factory;
	}

	/**
	 * Instantiates a new action factory.
	 */
	private ActionFactory() {
	}

	/**
	 * Creates a new Action object.
	 * 
	 * @return the scripted action
	 */
	public ScriptedAction createScriptedAction() {
		return new ScriptedAction(getScriptEngine());
	}

	/**
	 * Gets the script engine.
	 * 
	 * @return the script engine
	 */
	public ScriptEngine getScriptEngine() {
		if (engine == null) {
			engine = sgm.getEngineByName("javascript");
		}
		return engine;
	}
}
