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

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import alpha.forms.designer.view.dialog.ScriptedActionDialog;
import alpha.forms.form.event.Action;
import alpha.forms.form.event.ActionFactory;
import alpha.forms.form.event.DefaultEvent;
import alpha.forms.form.event.ScriptedAction;
import alpha.forms.propertyEditor.model.PropertyEditorModel;

/**
 * The Class EventTableEditor.
 */
public class EventTableEditor extends AbstractCellEditor implements
		TableCellRenderer, TableCellEditor {

	/** The property editor model. */
	private final PropertyEditorModel propertyEditorModel;

	/** The panel. */
	private JPanel panel;

	/** The label. */
	private JLabel label;

	/** The dialog. */
	private JDialog dialog;

	/** The event. */
	private DefaultEvent event;

	/**
	 * Instantiates a new event table editor.
	 * 
	 * @param propertyEditorModel
	 *            the property editor model
	 * @param event
	 *            the event
	 */
	public EventTableEditor(PropertyEditorModel propertyEditorModel,
			final DefaultEvent event) {
		this.propertyEditorModel = propertyEditorModel;
		this.event = event;

		panel = new JPanel();
		label = new JLabel("(event)");

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

		dialog = new JDialog((JFrame) null, "Action List", true);
		dialog.setLocationRelativeTo(this.propertyEditorModel.getPanel());
		dialog.setLayout(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		final DefaultListModel lm = new DefaultListModel();

		for (Action a : event.getActions()) {
			if (a instanceof ScriptedAction) {
				lm.addElement(a);
			}
		}

		final JList list = new JList(lm);
		panel.add(new JScrollPane(list), BorderLayout.CENTER);

		final JButton remove = new JButton("Remove");
		remove.setEnabled(false);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				Action a = (Action) list.getSelectedValue();
				event.removeAction(a);
				lm.removeElement(a);
			}
		});

		final JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {

				ScriptedAction action = ActionFactory.getInstance()
						.createScriptedAction();

				ScriptedActionDialog input = ScriptedActionDialog
						.create(action);

				input.setVisible(true);
				if (input.wasSaved()) {
					event.addAction(action);
					lm.addElement(action);
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
					ScriptedAction action = (ScriptedAction) list.getModel()
							.getElementAt(index);
					ScriptedActionDialog dialog = ScriptedActionDialog
							.create(action);
					dialog.setVisible(true);
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
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return event;
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
		return panel;
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
		return panel;
	}

}