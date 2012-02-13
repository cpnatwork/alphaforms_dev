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
package alpha.forms.designer.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

import alpha.forms.form.AlphaForm;
import alpha.forms.widget.model.FormWidget;
import alpha.forms.widget.model.container.ContainerWidget;

/**
 * The Class DraggedWidget.
 */
public class DraggedWidget {

	/** The widget. */
	protected FormWidget widget;

	/** The x. */
	protected int x;

	/** The y. */
	protected int y;

	/** The width. */
	protected int width;

	/** The height. */
	protected int height;

	/** The parent. */
	protected ContainerWidget parent = null;

	/** The model. */
	protected AlphaForm model = null;

	/**
	 * Instantiates a new dragged widget.
	 * 
	 * @param widget
	 *            the widget
	 * @param model
	 *            the model
	 */
	public DraggedWidget(FormWidget widget, AlphaForm model) {
		super();
		this.widget = widget;
		this.model = model;
		if (widget.getParent() == null) {
			x = widget.getX();
			y = widget.getY();

		} else {
			parent = (ContainerWidget) widget.getParent();
			x = widget.getX() + ((FormWidget) parent).getX();
			y = widget.getY() + ((FormWidget) parent).getY();
		}
		width = widget.getWidth();
		height = widget.getHeight();

	}

	/**
	 * Gets the widget.
	 * 
	 * @return the widget
	 */
	public FormWidget getWidget() {
		return widget;
	}

	/**
	 * Sets the widget.
	 * 
	 * @param widget
	 *            the new widget
	 */
	public void setWidget(FormWidget widget) {
		this.widget = widget;
	}

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 * 
	 * @param x
	 *            the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 * 
	 * @param y
	 *            the new y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 * 
	 * @param width
	 *            the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 * 
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public ContainerWidget getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	public void setParent(ContainerWidget parent) {
		this.parent = parent;
	}

	/**
	 * Render.
	 * 
	 * @param g
	 *            the g
	 */
	public void render(Graphics2D g) {
		g.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		Image img = widget.getUi().getAsImage();
		g.drawImage(img, x, y, null);
	}

	/**
	 * Show dropable overlay.
	 * 
	 * @param c
	 *            the c
	 * @return true, if successful
	 */
	public boolean showDropableOverlay(ContainerWidget c) {
		return (parent == null || parent != c);
	}

	/**
	 * Finalize.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param parent
	 *            the parent
	 */
	public void finalize(int x, int y, ContainerWidget parent) {
		if (parent == null) {
			if (this.parent != null) {
				this.parent.removeChild(widget);
				model.addWidget(widget);
				this.parent = null;
			}
			widget.setX(x);
			widget.setY(y);
		} else if (parent != this.parent) {
			if (this.parent == null) {
				model.getWidgets().remove(widget);
				parent.addChild(widget);
				this.parent = parent;
			} else {
				this.parent.removeChild(widget);
				parent.addChild(widget);
				this.parent = parent;
			}
			widget.setX(x - ((FormWidget) parent).getX());
			widget.setY(y - ((FormWidget) parent).getY());
		} else {
			widget.setX(x - ((FormWidget) parent).getX());
			widget.setY(y - ((FormWidget) parent).getY());
		}
	}

}
