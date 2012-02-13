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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import alpha.forms.designer.util.DraggedWidget;
import alpha.forms.designer.view.popup.FormPopupListener;
import alpha.forms.designer.view.popup.FormPopupMenu;
import alpha.forms.form.AlphaForm;
import alpha.forms.signal.model.PropertyUpdatedSignal;
import alpha.forms.signal.model.SelectionChangedSignal;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.signal.model.WidgetTemplateSignal;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.AbstractContainerWidget;
import alpha.forms.widget.model.container.ContainerWidget;
import alpha.forms.widget.view.FormWidgetUI;

/**
 * The Class FormCanvas.
 */
public class FormCanvas extends JPanel implements MouseListener,
		MouseMotionListener, Subscriber, Scrollable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The parent. */
	private FormDesignerPanel parent;

	/** The model. */
	private AlphaForm model;

	/** The show grid. */
	private boolean showGrid = true;

	/** The grid size. */
	private int gridSize = 10;

	/** The selected element. */
	private FormWidget selectedElement = null;

	/** The selection. */
	SelectionState selection = null;

	/** The active container. */
	private ContainerWidget activeContainer = null;

	/** The active area. */
	private FocusArea activeArea = null;

	/** The is resize form. */
	boolean isResizeForm = false;

	/** The dragged widget. */
	DraggedWidget draggedWidget = null;

	/**
	 * Instantiates a new form canvas.
	 * 
	 * @param parent
	 *            the parent
	 * @param model
	 *            the model
	 */
	public FormCanvas(FormDesignerPanel parent, final AlphaForm model) {
		this.parent = parent;
		this.model = model;
		this.setLayout(null);
		this.setAutoscrolls(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				"deleteWidget");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteWidget");
		this.getActionMap().put("deleteWidget", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				if (selection != null) {
					FormWidget widget = selection.subject;
					if (widget.getParent() != null) {
						FormWidget parent = widget.getParent();
						((ContainerWidget) parent).removeChild(widget);
						parent.getUi().revalidate();
						parent.getUi().updateUI();
					} else {
						model.getWidgets().remove(widget);
					}
					selection = null;
					activeArea.updateUI();
					repaint();
				}

			}

		});
		this.activeArea = new FocusArea(model, null, this);

		FormPopupMenu formPopup = new FormPopupMenu(model, this);
		FormPopupListener formPopupListener = new FormPopupListener(formPopup);
		this.addMouseListener(formPopupListener);

		SignalManager.getInstance().subscribeSink(this, "propertyEditor");
		SignalManager.getInstance().subscribeSink(this, "canvas");
	}

	/**
	 * Gets the parent panel.
	 * 
	 * @return the parent panel
	 */
	public FormDesignerPanel getParentPanel() {
		return parent;
	}

	/**
	 * Gets the selected items.
	 * 
	 * @return the selected items
	 */
	public List<FormWidget> getSelectedItems() {
		List<FormWidget> selList = new ArrayList<FormWidget>();
		if (selection != null) {
			selList.add(selection.subject);
		}
		return selList;
	}

	/**
	 * Translate point.
	 * 
	 * @param pin
	 *            the pin
	 * @return the point
	 */
	public Point translatePoint(Point pin) {
		if (showGrid) {
			int x = ((pin.x + gridSize / 2) / gridSize) * gridSize;
			int y = ((pin.y + gridSize / 2) / gridSize) * gridSize;
			return new Point(x, y);
		} else
			return new Point(pin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
	 */
	@Override
	protected void paintChildren(Graphics g) {
		for (FormWidget w : model.getWidgets()) {
			FormWidgetUI ui = w.getUi();
			Rectangle clipBounds = g.getClipBounds();
			Rectangle cr = w.getBounds();
			SwingUtilities.computeIntersection(clipBounds.x, clipBounds.y,
					clipBounds.width, clipBounds.height, cr);
			Graphics gComp = g.create(cr.x, cr.y, cr.width, cr.height);
			gComp.setFont(getFont());
			gComp.setColor(getForeground());
			ui.doLayout();
			ui.addNotify();
			ui.validate();
			ui.printAll(gComp);
			gComp.dispose();
		}
		if (activeContainer != null) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1.0f));
			Color c = SystemColor.textHighlight;
			Color o = new Color(c.getRed(), c.getGreen(), c.getBlue(), 120);
			g2d.setPaint(o);
			g2d.fill(((FormWidget) activeContainer).getBounds());
		}
		if (selection != null) {
			selection.drawSelection((Graphics2D) g, activeArea);
		}
		if (draggedWidget != null) {
			draggedWidget.render((Graphics2D) g);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);
		Dimension d = this.getSize();
		g.setColor(SystemColor.control);
		g.fillRect(0, 0, d.width, d.height);
		if (showGrid) {
			g.setColor(getForeground());
			for (int x = 0; x <= d.width; x += gridSize) {
				for (int y = 0; y <= d.height; y += gridSize) {
					g.fillRect(x, y, 1, 1);
				}
			}
		}
		if (selection == null) {
			g.setColor(getForeground());
			g.drawLine(0, d.height - 1, d.width - 1, d.height - 1);
			g.drawLine(d.width - 1, 0, d.width - 1, d.height - 1);
			g.fillRect(d.width - 4, d.height - 4, 4, 4);
		}
	}

	/**
	 * Find widget.
	 * 
	 * @param p
	 *            the p
	 * @return the form widget
	 */
	public FormWidget findWidget(Point p) {
		return findWidget(p, 0);
	}

	/**
	 * Find widget.
	 * 
	 * @param p
	 *            the p
	 * @param d
	 *            the d
	 * @return the form widget
	 */
	public FormWidget findWidget(Point p, int d) {
		for (FormWidget w : model.getWidgets()) {
			Rectangle b = w.getBounds();
			b.height += 2 * d;
			b.width += 2 * d;
			b.x -= d;
			b.y -= d;
			if (b.contains(p)) {
				return w;
			}
		}
		return null;
	}

	/**
	 * Find widget deep.
	 * 
	 * @param p
	 *            the p
	 * @return the form widget
	 */
	public FormWidget findWidgetDeep(Point p) {
		FormWidget w = findWidget(p);
		if (w != null && w instanceof ContainerWidget) {
			FormWidget cw = ((ContainerWidget) w).getWidgetByLocation(p);
			if (cw != null)
				return cw;
		}
		return w;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent ev) {
		if (ev.getClickCount() == 2) {
			System.out.println("Double click");
			FormWidget fw = findWidgetDeep(ev.getPoint());
			if (selection != null && selection.subject == fw
					&& fw.getUi().getSubselection() != null) {
				selection.subselect = fw.getUi().getSubselection();
				updateUI();
			}
		}
	}

	/**
	 * Gets the active area.
	 * 
	 * @return the active area
	 */
	public FocusArea getActiveArea() {
		return activeArea;
	}

	/**
	 * Drag over event.
	 * 
	 * @param p
	 *            the p
	 * @param w
	 *            the w
	 */
	public void dragOverEvent(Point p, FormWidget w) {
		FormWidget container = findWidget(p);
		if (w.getParent() == container)
			return;
		if (container != null && container instanceof ContainerWidget
				&& ((ContainerWidget) container).doesAcceptWidget(w)) {
			activeContainer = (ContainerWidget) container;
		} else {
			activeContainer = null;
		}
		updateUI();
	}

	/**
	 * Stop container highlight.
	 */
	public void stopContainerHighlight() {
		activeContainer = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent ev) {
		selectedElement = findWidget(ev.getPoint(), 3);
		AbstractContainerWidget container = null;
		if (selectedElement != null) {
			if (selectedElement instanceof ContainerWidget) {
				FormWidget cw = ((ContainerWidget) selectedElement)
						.getWidgetByLocation(ev.getPoint());
				if (cw != null) {
					container = (AbstractContainerWidget) selectedElement;
					selectedElement = cw;
				}
			}
			if (selection != null && selection.subject == selectedElement)
				return;
			activeArea = new FocusArea(model, container, this);
			setSelection(Arrays.asList(selectedElement));			
		} else {
			setSelection(null);
			activeContainer = null;
			activeArea = new FocusArea(model, null, this);
		}
		updateUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent ev) {
		isResizeForm = false;
		if (selection != null) {
			FormWidget w = selection.subject;
			if (draggedWidget != null) {
				Point p = ev.getPoint();
				p.translate(-selection.mouseOffset.x, -selection.mouseOffset.y);
				p = translatePoint(p);
				FormWidget container = findWidget(ev.getPoint());
				if (container != null && container instanceof ContainerWidget
						&& container != w) {
					draggedWidget.finalize(p.x, p.y,
							(ContainerWidget) container);
					activeArea = new FocusArea(model,
							(AbstractContainerWidget) container, this);
				} else {
					draggedWidget.finalize(p.x, p.y, null);
					activeArea = new FocusArea(model, null, this);
				}
				draggedWidget = null;
			}
			if (activeContainer != null && activeContainer != w.getParent()) {
				stopContainerHighlight();
			}
			selection.isMoving = false;
			selection.isResizing = false;
			selection.resizeDirection = -1;
			activeContainer = null;
			this.setCursor(Cursor.getDefaultCursor());
			updateUI();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(MouseEvent ev) {
		System.out.println("mouseDragged: " + ev.getPoint());
		if (model.getWidth() - ev.getPoint().x <= 5
				&& model.getHeight() - ev.getPoint().y <= 5) {
			isResizeForm = true;
		} else if (selection != null && !selection.isMoving
				&& !selection.isResizing) {
			Point p = ev.getPoint();
			int dir = selection.getResizingDirection(p);
			if (dir >= 0) {
				selection.isResizing = true;
				selection.resizeDirection = dir;
			} else {
				selection.isMoving = true;
				Point mouse = activeArea.translatePoint(ev.getPoint());
				draggedWidget = new DraggedWidget(selection.subject, model);
				selection.mouseOffset = new Point(mouse.x
						- selection.subject.getX(), mouse.y
						- selection.subject.getY());
			}
		}
		if (isResizeForm) {
			Point p = translatePoint(ev.getPoint());
			if (model.getMinWidth() < p.x && model.getMinHeight() < p.y) {
				model.setWidth(p.x);
				model.setHeight(p.y);
			} else {
				model.setWidth(model.getMinWidth() + 10);
				model.setHeight(model.getMinHeight() + 10);
			}
			revalidate();
		}
		if (selection != null && selection.isMoving) {
			Point mouse = ev.getPoint();
			Point np = new Point(mouse.x - selection.mouseOffset.x, mouse.y
					- selection.mouseOffset.y);
			Point p = translatePoint(np);
			if (p.x >= 0
					&& p.x <= this.getWidth() - selection.subject.getWidth()
							+ 40) {
				draggedWidget.setX(p.x);
			}
			if (p.y >= 0
					&& p.y <= this.getHeight() - selection.subject.getHeight()
							+ 40) {
				draggedWidget.setY(p.y);
			}
			model.recalculateDimensions(draggedWidget);
			dragOverEvent(mouse, selection.subject);
			updateUI();
		}
		if (selection != null && selection.isResizing) {
			Point mouse = activeArea.translatePoint(translatePoint(ev
					.getPoint()));
			if (selection.subselect != null) {
				Rectangle r = activeArea.translateRect(selection.subject
						.getUi().getSubselection());
				if (selection.resizeDirection == SelectionState.NORTH) {
					int delta = r.y - mouse.y;
					selection.subject.getUi().updateSubselectionSize(delta,
							selection.resizeDirection);
				} else if (selection.resizeDirection == SelectionState.EAST) {
					int delta = mouse.x - r.x;
					selection.subject.getUi().updateSubselectionSize(delta,
							selection.resizeDirection);
				} else if (selection.resizeDirection == SelectionState.SOUTH) {
					int delta = mouse.y - r.y;
					selection.subject.getUi().updateSubselectionSize(delta,
							selection.resizeDirection);
				} else if (selection.resizeDirection == SelectionState.WEST) {
					int delta = r.x - mouse.x;
					selection.subject.getUi().updateSubselectionSize(delta,
							selection.resizeDirection);
				}
			} else if (selection.resizeDirection == SelectionState.NORTH) {
				int delta = selection.subject.getY() - mouse.y;
				int height = selection.subject.getHeight() + delta;
				if (height > 0 && selection.subject.getY() - delta >= 0) {
					selection.subject.setY(selection.subject.getY() - delta);
					selection.subject.setHeight(height);
				}
			} else if (selection.resizeDirection == SelectionState.EAST) {
				int width = mouse.x - selection.subject.getX();
				if (width > 0
						&& selection.subject.getX() + width <= this.getWidth()) {
					selection.subject.setWidth(width);
				}
			} else if (selection.resizeDirection == SelectionState.SOUTH) {
				int height = mouse.y - selection.subject.getY();
				if (height > 0
						&& selection.subject.getY() + height <= this
								.getHeight()) {
					selection.subject.setHeight(height);
				}
			} else if (selection.resizeDirection == SelectionState.WEST) {
				int delta = selection.subject.getX() - mouse.x;
				int width = selection.subject.getWidth() + delta;
				if (width > 0 && selection.subject.getX() - delta >= 0) {
					selection.subject.setX(selection.subject.getX() - delta);
					selection.subject.setWidth(width);
				}
			}
			model.recalculateDimensions(selection.subject);
			updateUI();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent ev) {

		if (selection != null) {
			int rdir = selection.getResizingDirection(ev.getPoint());
			if (rdir >= 0) {
				if (rdir == SelectionState.NORTH) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				} else if (rdir == SelectionState.EAST) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				} else if (rdir == SelectionState.SOUTH) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
				} else if (rdir == SelectionState.WEST) {
					this.setCursor(Cursor
							.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				}
			} else if (selection.subject == findWidget(ev.getPoint())) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			} else {
				this.setCursor(Cursor.getDefaultCursor());
			}
		} else {
			this.setCursor(Cursor.getDefaultCursor());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.signal.model.Subscriber#signalReceived(alpha.forms.signal
	 * .model.Signal)
	 */
	@Override
	public void signalReceived(Signal s) {
		if (s instanceof PropertyUpdatedSignal) {
			updateUI();
		} else if (s instanceof WidgetTemplateSignal) {

		} else if(s instanceof SelectionChangedSignal) {
			setSelection(((SelectionChangedSignal) s).getSelection());
			updateUI();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(model.getWidth(), model.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle,
	 * int, int)
	 */
	@Override
	public int getScrollableBlockIncrement(Rectangle view, int orientation,
			int direction) {
		if (orientation == SwingConstants.HORIZONTAL) {
			return view.width - 20;
		} else {
			return view.height - 20;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle,
	 * int, int)
	 */
	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 10;
	}

	/**
	 * Checks if is show grid.
	 * 
	 * @return true, if is show grid
	 */
	public boolean isShowGrid() {
		return showGrid;
	}

	/**
	 * Sets the show grid.
	 * 
	 * @param showGrid
	 *            the new show grid
	 */
	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	/**
	 * Gets the grid size.
	 * 
	 * @return the grid size
	 */
	public int getGridSize() {
		return gridSize;
	}

	/**
	 * Sets the grid size.
	 * 
	 * @param gridSize
	 *            the new grid size
	 */
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}
	
	/**
	 * Set a new selection
	 * 
	 * @param newSelection the elements going to be selected
	 */
	protected void setSelection(List<FormWidget> newSelection) {
		SelectionChangedSignal s = new SelectionChangedSignal();
		if(newSelection == null || newSelection.size() == 0) {
			selection = null;
			s.setSelection(null);
		} else {
			selection = new SelectionState(this);
			selection.subject = newSelection.get(0);
			
			s.setSelection(new ArrayList<FormWidget>());
			s.getSelection().add(selection.subject);
		}
		s.setSource(this);
		SignalManager.getInstance().sendSignal(s, "formCanvas");		
	}

}