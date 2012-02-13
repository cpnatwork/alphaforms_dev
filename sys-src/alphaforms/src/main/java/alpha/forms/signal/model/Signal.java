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

/**
 * The Class Signal.
 */
public abstract class Signal {

	/** The id. */
	protected int id;

	/** The payload. */
	protected Object payload;

	/** The sink. */
	protected SignalSink sink;

	/** The source. */
	protected Object source;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the payload.
	 * 
	 * @return the payload
	 */
	public Object getPayload() {
		return payload;
	}

	/**
	 * Sets the payload.
	 * 
	 * @param payload
	 *            the new payload
	 */
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	/**
	 * Gets the sink.
	 * 
	 * @return the sink
	 */
	public SignalSink getSink() {
		return sink;
	}

	/**
	 * Sets the sink.
	 * 
	 * @param sink
	 *            the new sink
	 */
	public void setSink(SignalSink sink) {
		this.sink = sink;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(Object source) {
		this.source = source;
	}
}
