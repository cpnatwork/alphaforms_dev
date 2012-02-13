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
package alpha.forms.designer.view.popup;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The listener interface for receiving formPopup events. The class that is
 * interested in processing a formPopup event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addFormPopupListener<code> method. When
 * the formPopup event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see FormPopupEvent
 */
public class FormPopupListener extends MouseAdapter {

	/** The menu. */
	private FormPopupMenu menu;

	/**
	 * Instantiates a new form popup listener.
	 * 
	 * @param menu
	 *            the menu
	 */
	public FormPopupListener(FormPopupMenu menu) {
		this.menu = menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent ev) {
		showMenu(ev);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent ev) {
		showMenu(ev);
	}

	/**
	 * Show menu.
	 * 
	 * @param ev
	 *            the ev
	 */
	private void showMenu(MouseEvent ev) {
		if (ev.isPopupTrigger()) {
			menu.show(ev.getComponent(), ev.getX(), ev.getY());
		}
	}
}
