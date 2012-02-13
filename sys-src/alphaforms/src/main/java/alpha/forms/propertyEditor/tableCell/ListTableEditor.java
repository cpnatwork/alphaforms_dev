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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import alpha.forms.designer.view.dialog.AddItemDialog;
import alpha.forms.propertyEditor.model.PropertyEditorModel;

/**
 * The Class ListTableEditor.
 */
public class ListTableEditor extends AbstractCellEditor implements
		TableCellRenderer, TableCellEditor {

	/** The property editor model. */
	private final PropertyEditorModel propertyEditorModel;

	/** The panel. */
	private JPanel panel;

	/** The label. */
	private JLabel label;

	/** The dialog. */
	private JDialog dialog;

	/** The contents. */
	private List<Object> contents = new ArrayList<Object>();

	/** The generic type. */
	private Class genericType;

	/**
	 * Instantiates a new list table editor.
	 * 
	 * @param propertyEditorModel
	 *            the property editor model
	 * @param values
	 *            the values
	 * @param genericType
	 *            the generic type
	 */
	public ListTableEditor(PropertyEditorModel propertyEditorModel,
			List<Object> values, final Class genericType) {

		this.propertyEditorModel = propertyEditorModel;
		this.genericType = (genericType == null) ? String.class : genericType;

		panel = new JPanel();
		label = new JLabel("...");
		JButton edit = new JButton("...");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialog.setVisible(true);
				fireEditingStopped();
			}

		});
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		panel.add(edit, BorderLayout.EAST);

		for (Object o : values) {
			contents.add(o);
		}
		dialog = new JDialog((JFrame) null, "List Editor", true);
		dialog.setLocationRelativeTo(this.propertyEditorModel.getPanel());
		dialog.setLayout(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		final DefaultListModel lm = new DefaultListModel();

		for (Object s : contents) {
			lm.addElement(s);
		}

		final JList list = new JList(lm);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);

		final JButton remove = new JButton("Remove");
		remove.setEnabled(false);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				Object o = list.getSelectedValue();
				contents.remove(o);
				lm.removeElement(o);
			}
		});

		final JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if (genericType.equals(String.class)) {
					String s = JOptionPane.showInputDialog("Insert new item:");
					if (s != null && !s.isEmpty()) {
						contents.add(s);
						lm.addElement(s);
					}
				} else {

					Object itemObject = null;

					try {
						Constructor c = genericType.getConstructor();

						itemObject = c.newInstance();
					} catch (Exception e) {
						System.err
								.print("Unable to instantiate generic type class: "
										+ e.getMessage());
						return;
					}
					if (itemObject == null)
						return;

					AddItemDialog input = AddItemDialog.create(genericType,
							itemObject);

					input.setVisible(true);
					if (input.wasSaved()) {
						Map<String, Object> values = input.getValueMap();
						for (String name : values.keySet()) {
							Method setter = null;
							try {
								setter = genericType.getMethod("set" + name,
										values.get(name).getClass());
								System.out.println(values.get(name).getClass());
								setter.invoke(itemObject, values.get(name));
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
						}
						contents.add(itemObject);
						lm.addElement(itemObject);
					}
				}
			}
		});

		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				dialog.setVisible(false);
			}

		});

		JPanel btnPanel = new JPanel(new FlowLayout());
		btnPanel.add(add);
		btnPanel.add(remove);
		btnPanel.add(ok);

		panel.add(btnPanel, BorderLayout.SOUTH);

		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() >= 2) {
					int index = list.locationToIndex(ev.getPoint());
					Object itemObject = list.getModel().getElementAt(index);
					AddItemDialog dialog = AddItemDialog.create(genericType,
							itemObject);
					dialog.setVisible(true);
					if (dialog.wasSaved()) {
						Map<String, Object> values = dialog.getValueMap();
						for (String name : values.keySet()) {
							Method setter = null;
							try {
								setter = genericType.getMethod("set" + name,
										values.get(name).getClass());
								System.out.println(values.get(name).getClass());
								setter.invoke(itemObject, values.get(name));
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent ev) {

				if (list.getSelectedIndex() != -1) {
					remove.setEnabled(true);
				} else {
					remove.setEnabled(false);
				}

			}
		});

		dialog.add(panel, BorderLayout.CENTER);
		dialog.pack();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
			panel.setForeground(table.getSelectionForeground());
		} else {
			panel.setBackground(table.getBackground());
			panel.setForeground(table.getForeground());
		}
		panel.setOpaque(true);
		if (((List) value).isEmpty()) {
			label.setText("(empty)");
		} else {
			label.setText(((List) value).toString());
		}
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int arg3, int arg4) {
		panel.setOpaque(true);
		if (((List) value).isEmpty()) {
			label.setText("(empty)");
		} else {
			label.setText(((List) value).toString());
		}
		return panel;
	}

}