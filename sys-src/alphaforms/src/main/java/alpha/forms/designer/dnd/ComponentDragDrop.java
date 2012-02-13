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
package alpha.forms.designer.dnd;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JList;

import alpha.forms.designer.view.FormCanvas;
import alpha.forms.designer.view.FormDesignerPanel;
import alpha.forms.form.naming.WidgetNameGenerator;
import alpha.forms.template.model.WidgetTemplate;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class ComponentDragDrop.
 */
public class ComponentDragDrop implements DragGestureListener,
		DragSourceListener, DropTargetListener, Transferable {

	/** The Constant supportedFlavors. */
	static final DataFlavor[] supportedFlavors = { null };

	static {
		try {
			supportedFlavors[0] = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** The object. */
	Object object;

	/** The offset. */
	Point offset;

	/** The drag image. */
	Image dragImage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer
	 * .DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType)) {
			return object;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.
	 * datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent
	 * )
	 */
	@Override
	public void dragEnter(DropTargetDragEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragExit(DropTargetEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent
	 * )
	 */
	@Override
	public void dragOver(DropTargetDragEvent ev) {
		if (object != null) {
			FormCanvas cv = (FormCanvas) ev.getDropTargetContext()
					.getComponent();
			FormWidget w = null;
			if (object instanceof WidgetTemplate) {
				w = ((WidgetTemplate) object).createWidgetFromTemplate();
			} else if (object instanceof FormWidget) {
				w = (FormWidget) object;
			}
			cv.dragOverEvent(ev.getLocation(), w);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent ev) {
		ev.acceptDrop(ev.getDropAction());
		try {
			Object source = ev.getTransferable().getTransferData(
					supportedFlavors[0]);
			FormWidget cmp = null;
			FormCanvas canvas = (FormCanvas) ev.getDropTargetContext()
					.getComponent();
			FormDesignerPanel container = canvas.getParentPanel();
			Point loc = new Point(ev.getLocation().x + offset.x,
					ev.getLocation().y + offset.y);
			if (object instanceof WidgetTemplate) {
				cmp = ((WidgetTemplate) object).createWidgetFromTemplate();
				cmp.setName(WidgetNameGenerator
						.getName((WidgetTemplate) object));
			} else {
				FormWidget component = (FormWidget) source;
				try {
					Constructor<? extends FormWidget> c = component.getClass()
							.getConstructor(String.class);
					cmp = c.newInstance(WidgetNameGenerator.getName(component));
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
			container.addWidget(cmp, loc);
			container.repaint();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ev.dropComplete(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.
	 * DropTargetDragEvent)
	 */
	@Override
	public void dropActionChanged(DropTargetDragEvent ev) {
		// dropTargetDrag(ev);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent
	 * )
	 */
	@Override
	public void dragDropEnd(DragSourceDropEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent
	 * )
	 */
	@Override
	public void dragEnter(DragSourceDragEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragExit(DragSourceEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent
	 * )
	 */
	@Override
	public void dragOver(DragSourceDragEvent ev) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.
	 * DragSourceDragEvent)
	 */
	@Override
	public void dropActionChanged(DragSourceDragEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.
	 * DragGestureEvent)
	 */
	@Override
	public void dragGestureRecognized(DragGestureEvent ev) {
		if (ev.getComponent() instanceof JList) {
			JList list = (JList) ev.getComponent();
			if (!list.isSelectionEmpty()) {
				object = list.getSelectedValue();
				FormWidget w = null;
				if (object instanceof WidgetTemplate) {
					w = ((WidgetTemplate) object).createWidgetFromTemplate();
				} else {
					w = (FormWidget) object;
				}
				Image di = w.getUi().getAsImage();
				int idx = list.getSelectedIndex();
				Rectangle cell = list.getCellBounds(idx, idx);
				Point po = new Point(-ev.getDragOrigin().x + cell.x,
						-ev.getDragOrigin().y + cell.y);
				offset = po;
				dragImage = di;
				ev.startDrag(null, di, po, this, this);

			}
		}

	}

}
