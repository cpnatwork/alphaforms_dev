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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.container.AbstractContainerWidget;

/**
 * The Class FocusArea.
 */
public class FocusArea {

	/** The model. */
	private AlphaForm model;

	/** The parent. */
	private AbstractContainerWidget parent;

	/** The canvas. */
	private FormCanvas canvas;

	/**
	 * Instantiates a new focus area.
	 * 
	 * @param model
	 *            the model
	 * @param parent
	 *            the parent
	 * @param canvas
	 *            the canvas
	 */
	public FocusArea(AlphaForm model, AbstractContainerWidget parent,
			FormCanvas canvas) {
		this.model = model;
		this.parent = parent;
		this.canvas = canvas;
	}

	/**
	 * Gets the focus area bounds.
	 * 
	 * @return the focus area bounds
	 */
	public Rectangle getFocusAreaBounds() {
		if (parent != null) {
			return parent.getBounds();
		} else {
			return canvas.getBounds();
		}
	}

	/**
	 * Translate point.
	 * 
	 * @param p
	 *            the p
	 * @return the point
	 */
	public Point translatePoint(Point p) {
		if (parent != null) {
			return new Point(p.x - parent.getX(), p.y - parent.getY());
		} else {
			return new Point(p);
		}
	}

	/**
	 * Translate rect.
	 * 
	 * @param rect
	 *            the rect
	 * @return the rectangle
	 */
	public Rectangle translateRect(Rectangle rect) {
		if (parent != null) {
			rect.translate(parent.getX(), parent.getY());
			return new Rectangle(rect);
		} else {
			return new Rectangle(rect);
		}
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		if (parent != null) {
			return parent.getWidth();
		} else {
			return canvas.getWidth();
		}
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		if (parent != null) {
			return parent.getHeight();
		} else {
			return canvas.getHeight();
		}
	}

	/**
	 * Update ui.
	 */
	public void updateUI() {
		if (parent != null) {
			parent.getUi().updateUI();
		} else {
			canvas.updateUI();
		}
	}

	/**
	 * Draw overlay outside.
	 * 
	 * @param g
	 *            the g
	 */
	public void drawOverlayOutside(Graphics2D g) {

	}

	/**
	 * Draw overlay inside.
	 * 
	 * @param g
	 *            the g
	 */
	public void drawOverlayInside(Graphics2D g) {

	}

}
