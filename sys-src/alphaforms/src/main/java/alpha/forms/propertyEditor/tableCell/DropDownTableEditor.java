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
package alpha.forms.propertyEditor.tableCell;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * The Class DropDownTableEditor.
 */
public class DropDownTableEditor extends DefaultCellEditor {

	/**
	 * Instantiates a new drop down table editor.
	 * 
	 * @param values
	 *            the values
	 */
	public DropDownTableEditor(Object[] values) {
		super(new JComboBox(values));
	}
}