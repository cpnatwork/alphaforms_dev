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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;

import alpha.forms.widget.model.FormWidget;

/**
 * The Class SelectionState.
 */
public class SelectionState {

	/** The form canvas. */
	private final FormCanvas formCanvas;

	/**
	 * Instantiates a new selection state.
	 * 
	 * @param formCanvas
	 *            the form canvas
	 */
	SelectionState(FormCanvas formCanvas) {
		this.formCanvas = formCanvas;
	}

	/** The Constant NORTH. */
	public static final int NORTH = 0;

	/** The Constant SOUTH. */
	public static final int SOUTH = 1;

	/** The Constant WEST. */
	public static final int WEST = 2;

	/** The Constant EAST. */
	public static final int EAST = 3;

	/** The subject. */
	public FormWidget subject;

	/** The subselect. */
	public Rectangle subselect;

	/** The is moving. */
	public boolean isMoving;

	/** The is resizing. */
	public boolean isResizing;

	/** The resize direction. */
	public int resizeDirection;

	/** The mouse offset. */
	public Point mouseOffset;

	/**
	 * Select widget.
	 * 
	 * @param w
	 *            the w
	 */
	public void selectWidget(FormWidget w) {

	}

	/**
	 * Unselect widget.
	 */
	public void unselectWidget() {

	}

	/**
	 * Gets the resizing direction.
	 * 
	 * @param p
	 *            the p
	 * @return the resizing direction
	 */
	public int getResizingDirection(Point p) {
		Rectangle r;
		if (subselect == null) {
			r = formCanvas.getActiveArea().translateRect(subject.getBounds());
		} else {
			r = formCanvas.getActiveArea().translateRect(subselect);
		}
		int delta = 3;
		int ncx = r.width / 2 + r.x;
		int ncy = r.y;
		if (p.x >= ncx - delta && p.x <= ncx + delta && p.y >= ncy - delta
				&& p.y <= ncy + delta) {
			return SelectionState.NORTH;
		}
		int ecx = r.x + r.width;
		int ecy = r.y + r.height / 2;
		if (p.x >= ecx - delta && p.x <= ecx + delta && p.y >= ecy - delta
				&& p.y <= ecy + delta) {
			return SelectionState.EAST;
		}
		int wcx = r.x;
		int wcy = r.y + r.height / 2;
		if (p.x >= wcx - delta && p.x <= wcx + delta && p.y >= wcy - delta
				&& p.y <= wcy + delta) {
			return SelectionState.WEST;
		}
		int scx = r.width / 2 + r.x;
		int scy = r.y + r.height;
		if (p.x >= scx - delta && p.x <= scx + delta && p.y >= scy - delta
				&& p.y <= scy + delta) {
			return SelectionState.SOUTH;
		}
		return -1;
	}

	/**
	 * Draw selection.
	 * 
	 * @param g
	 *            the g
	 * @param activeArea
	 *            the active area
	 */
	public void drawSelection(Graphics2D g, FocusArea activeArea) {
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		Color highlite = SystemColor.textHighlight;
		Color transparency = new Color(highlite.getRed(), highlite.getGreen(),
				highlite.getBlue(), 120);
		g.setPaint(transparency);
		FormWidget w = subject;
		Rectangle cr = activeArea.translateRect(w.getBounds());
		if (subselect == null) {
			g.fill(cr);
		}
		g.setPaint(highlite);
		g.draw(cr);
		if (subselect == null) {
			// g.setColor(new Color(255, 255, 255));
			g.setPaint(highlite.darker());
			g.fillOval(cr.x - 3, cr.y + (cr.height / 2 - 3), 5, 5);
			g.fillOval(cr.x - 3 + cr.width, cr.y + (cr.height / 2 - 3), 5, 5);
			g.fillOval(cr.x + (cr.width / 2 - 3), cr.y - 2, 5, 5);
			g.fillOval(cr.x + (cr.width / 2 - 3), cr.y - 3 + cr.height, 5, 5);

			/*
			 * g.setColor(new Color(0,0,0)); g.drawRect(cr.x-4, cr.y +
			 * (cr.height / 2 - 4), 5, 5); g.drawRect(cr.x-3 + cr.width, cr.y +
			 * (cr.height / 2 - 4), 5, 5); g.drawRect(cr.x + (cr.width /2 - 4),
			 * cr.y - 4, 5, 5); g.drawRect(cr.x + (cr.width /2 - 4), cr.y - 4 +
			 * cr.height, 5, 5);
			 */
		} else {
			drawSubselection(g, activeArea);
		}
	}

	/**
	 * Draw subselection.
	 * 
	 * @param g
	 *            the g
	 * @param activeArea
	 *            the active area
	 */
	public void drawSubselection(Graphics2D g, FocusArea activeArea) {
		FormWidget w = subject;
		Rectangle cr = activeArea.translateRect(w.getUi().getSubselection());
		g.setColor(SystemColor.textHighlight);
		g.drawRect(cr.x - 1, cr.y - 1, cr.width, cr.height);
		Color back = new Color(255, 255, 255);

		if (w.getUi().isSubselectionResizable(NORTH)) {
			g.setColor(back);
			g.fillRect(cr.x + (cr.width / 2 - 3), cr.y - 3, 4, 4);
			g.setColor(SystemColor.textHighlight);
			g.drawRect(cr.x + (cr.width / 2 - 4), cr.y - 4, 5, 5);
		}
		if (w.getUi().isSubselectionResizable(EAST)) {
			g.setColor(back);
			g.fillRect(cr.x - 2 + cr.width, cr.y + (cr.height / 2 - 3), 4, 4);
			g.setColor(SystemColor.textHighlight);
			g.drawRect(cr.x - 3 + cr.width, cr.y + (cr.height / 2 - 4), 5, 5);
		}
		if (w.getUi().isSubselectionResizable(SOUTH)) {
			g.setColor(back);
			g.fillRect(cr.x + (cr.width / 2 - 3), cr.y - 3 + cr.height, 4, 4);
			g.setColor(SystemColor.textHighlight);
			g.drawRect(cr.x + (cr.width / 2 - 4), cr.y - 4 + cr.height, 5, 5);
		}
		if (w.getUi().isSubselectionResizable(WEST)) {
			g.setColor(back);
			g.fillRect(cr.x - 3, cr.y + (cr.height / 2 - 3), 4, 4);
			g.setColor(SystemColor.textHighlight);
			g.drawRect(cr.x - 4, cr.y + (cr.height / 2 - 4), 5, 5);
		}

	}

}