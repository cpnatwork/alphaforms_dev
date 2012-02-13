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
package alpha.forms.propertyEditor.component;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import alpha.forms.propertyEditor.model.WidgetProperty;

/**
 * The Class InfoPanel.
 */
public class InfoPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The title. */
	private JLabel title;

	/** The description. */
	private JLabel description;

	/**
	 * Instantiates a new info panel.
	 */
	public InfoPanel() {
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.setLayout(new GridLayout(2, 1, 4, 4));
		title = new JLabel();
		title.setFont(title.getFont().deriveFont(15.0f));
		description = new JLabel();
		this.add(title);
		this.add(description);
	}

	/**
	 * Update with.
	 * 
	 * @param p
	 *            the p
	 */
	public void updateWith(WidgetProperty p) {
		title.setText(p.getName());
		description.setText("<strong>(" + p.getCategory() + ")</strong> "
				+ p.getDescription());
	}

}