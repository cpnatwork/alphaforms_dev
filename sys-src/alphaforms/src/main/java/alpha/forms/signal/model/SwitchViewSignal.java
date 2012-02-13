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
 * The Class SwitchViewSignal.
 */
public class SwitchViewSignal extends Signal {

	/** The Constant DESIGNER. */
	public final static int DESIGNER = 1;

	/** The Constant VIEWER. */
	public final static int VIEWER = 2;

	/** The to view. */
	private int toView;

	/** The from view. */
	private int fromView;

	/**
	 * Gets the to view.
	 * 
	 * @return the to view
	 */
	public int getToView() {
		return toView;
	}

	/**
	 * Sets the to view.
	 * 
	 * @param toView
	 *            the new to view
	 */
	public void setToView(int toView) {
		this.toView = toView;
	}

	/**
	 * Gets the from view.
	 * 
	 * @return the from view
	 */
	public int getFromView() {
		return fromView;
	}

	/**
	 * Sets the from view.
	 * 
	 * @param fromView
	 *            the new from view
	 */
	public void setFromView(int fromView) {
		this.fromView = fromView;
	}
}
