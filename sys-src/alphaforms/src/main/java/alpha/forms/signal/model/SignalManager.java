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
package alpha.forms.signal.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class SignalManager.
 */
public class SignalManager {

	/** The instance. */
	private static SignalManager instance;

	/** The sink map. */
	private Map<String, SignalSink> sinkMap = new HashMap<String, SignalSink>();

	/**
	 * Instantiates a new signal manager.
	 */
	private SignalManager() {

	}

	/**
	 * Gets the single instance of SignalManager.
	 * 
	 * @return single instance of SignalManager
	 */
	public static SignalManager getInstance() {
		if (instance == null) {
			instance = new SignalManager();
		}
		return instance;
	}

	/**
	 * Subscribe sink.
	 * 
	 * @param s
	 *            the s
	 * @param sinkName
	 *            the sink name
	 */
	public void subscribeSink(Subscriber s, String sinkName) {
		SignalSink q = sinkMap.get(sinkName);
		if (q == null) {
			q = new SignalSink(sinkName);
			sinkMap.put(sinkName, q);
		}
		q.subscribe(s);
	}

	/**
	 * Send signal.
	 * 
	 * @param signal
	 *            the signal
	 * @param sinkName
	 *            the sink name
	 */
	public void sendSignal(Signal signal, String sinkName) {
		SignalSink q = sinkMap.get(sinkName);
		if (q == null) {
			q = new SignalSink(sinkName);
			sinkMap.put(sinkName, q);
		}
		q.send(signal);
	}

	/**
	 * Unsubscribe sink.
	 * 
	 * @param s
	 *            the s
	 * @param sinkName
	 *            the sink name
	 */
	public void unsubscribeSink(Subscriber s, String sinkName) {
		SignalSink q = sinkMap.get(sinkName);
		if (q != null) {
			q.unsubscribe(s);
		}
	}
}
