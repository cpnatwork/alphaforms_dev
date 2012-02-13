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

import java.util.ArrayList;
import java.util.List;

/**
 * The Class SignalSink.
 */
public class SignalSink {

	/** The id. */
	private String id;

	/** The subscribers. */
	private List<Subscriber> subscribers = new ArrayList<Subscriber>();

	/**
	 * Instantiates a new signal sink.
	 * 
	 * @param id
	 *            the id
	 */
	public SignalSink(String id) {
		this.id = id;
	}

	/**
	 * Subscribe.
	 * 
	 * @param s
	 *            the s
	 */
	public void subscribe(Subscriber s) {
		subscribers.add(s);
	}

	/**
	 * Unsubscribe.
	 * 
	 * @param s
	 *            the s
	 */
	public void unsubscribe(Subscriber s) {
		subscribers.remove(s);
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Send.
	 * 
	 * @param sig
	 *            the sig
	 */
	public void send(Signal sig) {
		sig.setSink(this);
		for (Subscriber s : subscribers) {
			s.signalReceived(sig);
		}
	}
}
