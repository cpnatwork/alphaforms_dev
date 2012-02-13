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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import alpha.forms.form.validation.NumberInputVerifier;

/**
 * The Class AddItemDialog.
 */
public class AddItemDialog extends JDialog implements ActionListener {

	/** The items. */
	private List<Item> items = new ArrayList<Item>();

	/** The panel. */
	private JPanel panel;

	/** The save. */
	private JButton save;

	/** The cancel. */
	private JButton cancel;

	/** The was cancled. */
	private boolean wasCancled;

	/** The was saved. */
	private boolean wasSaved;

	/**
	 * Creates the.
	 * 
	 * @param type
	 *            the type
	 * @param instance
	 *            the instance
	 * @return the adds the item dialog
	 */
	public static AddItemDialog create(Class type, Object instance) {
		AddItemDialog dialog = new AddItemDialog();

		for (Method m : type.getMethods()) {
			if (m.getName().startsWith("set")
					&& m.getParameterTypes().length == 1) {
				String name = m.getName().substring(3);

				Method g = null;

				try {
					g = type.getMethod("get" + name);
				} catch (Exception e) {
				}
				if (g == null) {
					try {
						g = type.getMethod("is" + name);
					} catch (Exception e) {
					}
				}
				if (g == null) {
					continue;
				}

				Object value = null;
				try {
					value = g.invoke(instance);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dialog.addItem(name, value);
			}
		}

		return dialog;
	}

	/**
	 * Instantiates a new adds the item dialog.
	 */
	public AddItemDialog() {
		super((Frame) null, "Add item:", true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		JPanel buttonPanel = new JPanel(new FlowLayout());

		save = new JButton("Save");
		save.addActionListener(this);
		save.setActionCommand("save");
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");

		buttonPanel.add(save);
		buttonPanel.add(cancel);

		add(panel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(save);
	}

	/**
	 * Adds the item.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void addItem(String name, Object value) {
		Item i = new Item();
		i.name = name;
		i.value = value;
		items.add(i);
	}

	/**
	 * Adds the item.
	 * 
	 * @param name
	 *            the name
	 */
	public void addItem(String name) {
		addItem(name, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	public void setVisible(boolean flag) {
		if (flag == true) {
			panel.removeAll();
			for (Item i : items) {
				i.createUI(panel);
			}
			pack();
			wasCancled = false;
			wasSaved = false;
		}
		super.setVisible(flag);
	}

	/**
	 * Was cancled.
	 * 
	 * @return true, if successful
	 */
	public boolean wasCancled() {
		return wasCancled;
	}

	/**
	 * Was saved.
	 * 
	 * @return true, if successful
	 */
	public boolean wasSaved() {
		return wasSaved;
	}

	/**
	 * Gets the value map.
	 * 
	 * @return the value map
	 */
	public Map<String, Object> getValueMap() {
		Map<String, Object> out = new HashMap<String, Object>();
		for (Item i : items) {
			out.put(i.name, i.getValue());
		}
		return out;
	}

	/**
	 * The Class Item.
	 */
	protected class Item {

		/** The name. */
		public String name;

		/** The value. */
		public Object value;

		/** The component. */
		private JComponent component;

		/**
		 * Creates the ui.
		 * 
		 * @param parent
		 *            the parent
		 */
		public void createUI(JPanel parent) {
			GridBagConstraints c = new GridBagConstraints();
			JLabel label = new JLabel(name);

			if (value != null
					&& (value.getClass().equals(boolean.class) || value
							.getClass().equals(Boolean.class))) {
				JCheckBox checkBox = new JCheckBox();
				checkBox.setSelected((Boolean) value);
				component = checkBox;
			} else if (value != null && value.getClass().isEnum()) {
				component = new JComboBox(value.getClass().getEnumConstants());
			} else {
				JTextField textField = new JTextField();
				textField.setPreferredSize(new Dimension(200, 22));
				textField.setText(value.toString());
				if (value != null
						&& (value.getClass().equals(Integer.class)
								|| value.getClass().equals(Double.class) || value
								.getClass().equals(Float.class))) {
					textField.setInputVerifier(new NumberInputVerifier());
				}
				component = textField;
			}

			c.gridx = 0;
			c.anchor = GridBagConstraints.LINE_START;
			c.weightx = 0;
			c.insets = new Insets(4, 4, 4, 4);

			parent.add(label, c);

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.weightx = 0.2;
			c.insets = new Insets(4, 4, 4, 4);

			parent.add(component, c);
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public Object getValue() {
			if (component != null && component instanceof JTextField) {
				String val = ((JTextField) component).getText();
				if (value != null && value.getClass().equals(Integer.class)) {
					return Integer.parseInt(val);
				} else if (value != null
						&& value.getClass().equals(Double.class)) {
					Double.parseDouble(val);
				} else if (value != null
						&& value.getClass().equals(Float.class)) {
					Float.parseFloat(val);
				}
				return val;
			} else if (component != null && component instanceof JCheckBox) {
				return ((JCheckBox) component).isSelected();
			} else if (component != null && component instanceof JComboBox) {
				return ((JComboBox) component).getSelectedItem();
			}
			return new String();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			setVisible(false);
			wasSaved = true;
		} else if (e.getActionCommand().equals("cancel")) {
			setVisible(false);
			wasCancled = true;
		}

	}
}
