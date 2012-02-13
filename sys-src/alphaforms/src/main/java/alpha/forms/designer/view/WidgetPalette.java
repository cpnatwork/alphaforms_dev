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
package alpha.forms.designer.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class WidgetPalette.
 */
public class WidgetPalette extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list. */
	protected JList list;

	/**
	 * Instantiates a new widget palette.
	 */
	public WidgetPalette() {
		this.setBorder(BorderFactory.createTitledBorder("Widget Palette"));
		this.setLayout(new BorderLayout());
		list = new JList(new WidgetListModel());
		list.setCellRenderer(new WidgetListCellRenderer());
		// list.setFixedCellWidth(200);
		final JScrollPane scroll = new JScrollPane(list);
		this.add(scroll, BorderLayout.CENTER);
		final InfoPanel info = new InfoPanel();
		this.add(info, BorderLayout.SOUTH);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent ev) {
				JList list = (JList) ev.getSource();
				Object obj = list.getSelectedValue();
				if (obj instanceof FormWidget) {
					info.updateWith((FormWidget) obj);
				} else if (obj instanceof WidgetTemplate) {
					info.updateWith(((WidgetTemplate) obj)
							.createWidgetFromTemplate());
				}
				SelectionChangedSignal s = new SelectionChangedSignal();
				s.setSource(this);
				s.setSelection(null);
				SignalManager.getInstance().sendSignal(s, "canvas");
			}

		});
	}

	/**
	 * Register widget class.
	 * 
	 * @param widget
	 *            the widget
	 */
	public void registerWidgetClass(Class<? extends FormWidget> widget) {
		try {
			Constructor<? extends FormWidget> c = widget
					.getConstructor(String.class);
			FormWidget fw = c.newInstance(widget.getSimpleName());
			addWidget(fw);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds the widget.
	 * 
	 * @param fw
	 *            the fw
	 */
	protected void addWidget(FormWidget fw) {
		((WidgetListModel) list.getModel()).addWidget(fw);

	}

	/**
	 * Gets the drag source.
	 * 
	 * @return the drag source
	 */
	public Component getDragSource() {
		return list;
	}

	/**
	 * The Class WidgetListModel.
	 */
	protected class WidgetListModel extends AbstractListModel {

		/** The widgets. */
		private Vector<FormWidget> widgets = new Vector<FormWidget>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		@Override
		public Object getElementAt(int index) {
			return widgets.get(index);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.ListModel#getSize()
		 */
		@Override
		public int getSize() {
			return widgets.size();
		}

		/**
		 * Adds the widget.
		 * 
		 * @param w
		 *            the w
		 */
		public void addWidget(FormWidget w) {
			widgets.add(w);
			this.fireIntervalAdded(this, widgets.indexOf(w), widgets.indexOf(w));
		}

	}

	/**
	 * The Class WidgetListCellRenderer.
	 */
	protected class WidgetListCellRenderer implements ListCellRenderer {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
		 * .JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList list,
				Object object, int index, boolean isSelected, boolean hasFocus) {

			FormWidget w = (FormWidget) object;
			JPanel comp = new JPanel();
			comp.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

			if (isSelected) {
				comp.setOpaque(true);
				comp.setBackground(SystemColor.textHighlight);
			} else {
				comp.setOpaque(true);
			}

			Image im = w.getUi().getAsImage();

			Image im_100w = im.getScaledInstance(150, -1, Image.SCALE_SMOOTH);
			ImageIcon icon = null;
			if (im_100w.getHeight(null) > 50) {
				icon = new ImageIcon(im_100w.getScaledInstance(-1, 50,
						Image.SCALE_SMOOTH));
			} else {
				icon = new ImageIcon(im_100w);
			}
			JLabel img = new JLabel(icon);
			// img.setPreferredSize(new Dimension(icon.getIconWidth(),
			// icon.getIconHeight()));

			comp.add(img);
			// comp.setPreferredSize(new Dimension(icon.getIconWidth(),
			// icon.getIconHeight()));
			return comp;
		}

	}

	/**
	 * The Class InfoPanel.
	 */
	protected class InfoPanel extends JPanel {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The img. */
		private JLabel img = new JLabel();

		/** The title. */
		private JLabel title = new JLabel();

		/** The description. */
		private JLabel description = new JLabel();

		/**
		 * Instantiates a new info panel.
		 */
		public InfoPanel() {
			this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			this.setLayout(new GridLayout(3, 1, 4, 4));
			title.setFont(title.getFont().deriveFont(15.0f));
			this.add(title);
			this.add(img);
			this.add(description);
		}

		/**
		 * Update with.
		 * 
		 * @param w
		 *            the w
		 */
		public void updateWith(FormWidget w) {
			title.setText(w.getName() + " widget");

			Image im = w.getUi().getAsImage();
			Image im_100w = im.getScaledInstance(100, -1, Image.SCALE_SMOOTH);
			ImageIcon icon = null;
			if (im_100w.getHeight(null) > 50) {
				icon = new ImageIcon(im_100w.getScaledInstance(-1, 50,
						Image.SCALE_SMOOTH));
			} else {
				icon = new ImageIcon(im_100w);
			}
			img.setIcon(icon);
			description.setText(w.getDescription());
			this.updateUI();
		}

	}

}
