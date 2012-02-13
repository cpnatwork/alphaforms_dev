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
package alpha.forms.widget.view;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import alpha.forms.designer.view.SelectionState;
import alpha.forms.widget.model.AlphaList;
import alpha.forms.widget.model.AlphaList.ListItem;
import alpha.forms.widget.util.WidgetLabelPosition;

/**
 * The Class AlphaListUI.
 */
public class AlphaListUI extends FormWidgetUI {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model. */
	protected AlphaList model;

	/** The label. */
	private JLabel label;

	/** The list. */
	private JComponent list;

	/** The hgap. */
	private int hgap = 5;

	/**
	 * Instantiates a new alpha list ui.
	 * 
	 * @param model
	 *            the model
	 */
	public AlphaListUI(AlphaList model) {
		super(model);
		this.model = model;
		compose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.doLayout();
		Dimension d = model.getSize();
		super.setSize(d);
		this.remove(list);
		if (model.isMultipleSelection()) {
			list = new JList(model.getItems().toArray());
			JList jlist = (JList) list;
			jlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			List<Integer> selectedItems = new ArrayList<Integer>();
			for (int i = 0; i < model.getItems().size(); i++) {
				if (model.getItems().get(i).isSelected()) {
					selectedItems.add(i);
				}
			}

			int[] selectedIndices = new int[selectedItems.size()];
			for (int i = 0; i < selectedItems.size(); i++) {
				selectedIndices[i] = selectedItems.get(i);
			}
			jlist.setSelectedIndices(selectedIndices);

			jlist.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent ev) {
							for (ListItem i : model.getItems()) {
								i.setSelected(false);
							}
							for (int i : ((JList) list).getSelectedIndices()) {
								model.getItems().get(i).setSelected(true);
							}
							model.getOnSelectionChanged().fire();
						}

					});
		} else {
			list = new JComboBox(new DefaultComboBoxModel(model.getItems()
					.toArray()));
			if (model.isEditable()) {
				((JComboBox) list).setEditable(true);
			}
			for (ListItem item : model.getItems()) {
				if (item.isSelected()) {
					((JComboBox) list).setSelectedItem(item);
					break;
				}
			}

			((JComboBox) list).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					int index = ((JComboBox) list).getSelectedIndex();
					int j = 0;
					for (ListItem item : model.getItems()) {
						item.setSelected(j == index);
						j++;
					}
					model.getOnSelectionChanged().fire();
				}
			});
		}
		this.add(list);
		if (label != null
				&& this.model.getShowLabel() == WidgetLabelPosition.LEFT) {
			label.setText(model.getLabel());
			Dimension dl = label.getPreferredSize();
			label.setSize(dl);
			label.setLocation(0, d.height / 2 - dl.height / 2);
			list.setLocation(dl.width + hgap, 0);
			list.setSize(d.width - hgap - dl.width, d.height);
		} else if (label != null
				&& this.model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			label.setText(model.getLabel());
			Dimension dl = label.getPreferredSize();
			label.setSize(dl);
			label.setLocation(d.width - dl.width, d.height / 2 - dl.height / 2);
			list.setLocation(0, 0);
			list.setSize(d.width - hgap - dl.width, d.height);
		} else {
			list.setLocation(0, 0);
			list.setSize(d.width, d.height);
			label.setSize(0, 0);
		}
		list.doLayout();
		this.setVisible(model.isVisible());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#getSubselection()
	 */
	@Override
	public Rectangle getSubselection() {
		if (model.getShowLabel() == WidgetLabelPosition.LEFT
				|| model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			Rectangle r = list.getBounds();
			r.translate(getLocation().x, getLocation().y);
			return r;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#isSubselectionResizable(int)
	 */
	@Override
	public boolean isSubselectionResizable(int direction) {
		return (direction == SelectionState.WEST && model.getShowLabel() == WidgetLabelPosition.LEFT)
				|| (direction == SelectionState.EAST && model.getShowLabel() == WidgetLabelPosition.RIGHT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#updateSubselectionSize(int,
	 * int)
	 */
	@Override
	public void updateSubselectionSize(int delta, int direction) {
		if (direction == SelectionState.WEST
				&& model.getShowLabel() == WidgetLabelPosition.LEFT) {
			int tmpgap = hgap - delta;
			if (tmpgap >= 5
					&& tmpgap + label.getWidth() < model.getSize().width) {
				hgap = tmpgap;
			}
		} else if (direction == SelectionState.EAST
				&& model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			int tmpgap = hgap - (delta - list.getWidth());
			if (tmpgap >= 5
					&& tmpgap + label.getWidth() < model.getSize().width) {
				hgap = tmpgap;
			}
		}
		doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		super.compose();
		super.setSize(model.getSize());
		this.setLayout(null);
		if (model.isMultipleSelection()) {
			list = new JList(model.getItems().toArray());
			JList jlist = (JList) list;
			jlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			List<Integer> selectedItems = new ArrayList<Integer>();
			for (int i = 0; i < model.getItems().size(); i++) {
				if (model.getItems().get(i).isSelected()) {
					selectedItems.add(i);
				}
			}

			int[] selectedIndices = new int[selectedItems.size()];
			for (int i = 0; i < selectedItems.size(); i++) {
				selectedIndices[i] = selectedItems.get(i);
			}
			jlist.setSelectedIndices(selectedIndices);

			jlist.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent ev) {
							for (ListItem i : model.getItems()) {
								i.setSelected(false);
							}
							for (int i : ((JList) list).getSelectedIndices()) {
								model.getItems().get(i).setSelected(true);
							}
							model.getOnSelectionChanged().fire();
						}

					});
		} else {
			list = new JComboBox(new DefaultComboBoxModel(model.getItems()
					.toArray()));
			if (model.isEditable()) {
				((JComboBox) list).setEditable(true);
			}
			((JComboBox) list).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					int index = ((JComboBox) list).getSelectedIndex();
					int j = 0;
					for (ListItem item : model.getItems()) {
						item.setSelected(j == index);
						j++;
					}
					model.getOnSelectionChanged().fire();
				}
			});
		}
		list.doLayout();
		list.setOpaque(this.isOpaque());
		if (model.getShowLabel() == WidgetLabelPosition.LEFT) {
			label = new JLabel(model.getLabel());
			Dimension d = label.getPreferredSize();
			Dimension dt = list.getPreferredSize();
			System.out.println(dt.width);
			this.add(label);
			this.add(list);
			label.setSize(d.width, d.height);
			label.setLocation(0, dt.height / 2 - d.height / 2);
			list.setSize(model.getMiniumWidth() - d.width - hgap, dt.height);
			list.setLocation(d.width + hgap, 0);

			minimumHeight = dt.height;
			minimumWidth = d.width + hgap + dt.width;
		} else if (model.getShowLabel() == WidgetLabelPosition.RIGHT) {
			label = new JLabel(model.getLabel());
			Dimension d = label.getMinimumSize();
			Dimension dt = list.getMinimumSize();
			list.setBounds(0, 0, model.getMiniumWidth() - d.width - hgap,
					dt.height);
			label.setBounds(model.getMiniumWidth() + hgap, dt.height / 2
					- d.height / 2, d.width, d.height);
			this.add(list);
			this.add(label);
			minimumHeight = dt.height;
			minimumWidth = model.getMiniumWidth();
		} else {
			Dimension dt = list.getMinimumSize();
			list.setBounds(0, 0, model.getMiniumWidth(), dt.height);
			this.add(list);
			minimumHeight = dt.height;
			minimumWidth = model.getMiniumWidth();
		}
		list.doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		doLayout();
	}

}
