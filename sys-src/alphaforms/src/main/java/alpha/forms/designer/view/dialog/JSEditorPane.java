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
package alpha.forms.designer.view.dialog;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.text.PlainDocument;

/**
 * The Class JSEditorPane.
 */
public class JSEditorPane extends JEditorPane {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new jS editor pane.
	 */
	public JSEditorPane() {
		super();
		setFont(new Font("Courier New", Font.PLAIN, 13));
		setBackground(new Color(100, 100, 100));
		setForeground(new Color(255, 255, 255));
		setSelectedTextColor(new Color(255, 255, 255));
		getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);
	}

}