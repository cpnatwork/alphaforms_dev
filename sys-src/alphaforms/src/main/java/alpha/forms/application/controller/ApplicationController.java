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
package alpha.forms.application.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import alpha.forms.AlphaFormsFacade;
import alpha.forms.application.view.AlphaFormsView;
import alpha.forms.application.view.panel.FormView;
import alpha.forms.clipboard.view.ClipboardView;
import alpha.forms.designer.view.DesignerView;
import alpha.forms.form.AlphaForm;
import alpha.forms.form.util.AlphaFormProvider;
import alpha.forms.form.validation.ValidatorFactory;
import alpha.forms.signal.model.Signal;
import alpha.forms.signal.model.SignalManager;
import alpha.forms.signal.model.Subscriber;
import alpha.forms.signal.model.SwitchViewSignal;
import alpha.forms.util.FormSaveListener;

/**
 * The Class ApplicationController.
 */
public class ApplicationController implements AlphaFormsFacade, Subscriber {

	/** The current view. */
	FormView currentView;

	/** The previous view. */
	FormView previousView;

	/** The form. */
	AlphaForm form;

	/** The parent. */
	AlphaFormsView parent;

	/** The save listeners. */
	protected Set<FormSaveListener> saveListeners = new HashSet<FormSaveListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#start()
	 */
	@Override
	public AlphaFormsView start() {
		parent = new AlphaFormsView();
		ValidatorFactory.setup();
		form = new AlphaForm();
		start(parent, form, false);
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#start(java.io.InputStream)
	 */
	@Override
	public AlphaFormsView start(InputStream forminput) {
		if (forminput == null)
			return null;
		parent = new AlphaFormsView();
		ValidatorFactory.setup();
		PersistenceController p = new PersistenceController(this);
		AlphaForm alphaForm = p.loadForm(forminput);
		this.form = alphaForm;
		start(parent, alphaForm, form.getWidgets().size() > 0);
		return parent;
	}

	/**
	 * Start.
	 * 
	 * @param parent
	 *            the parent
	 * @param form
	 *            the form
	 * @param showClipboard
	 *            the show clipboard
	 */
	protected void start(AlphaFormsView parent, AlphaForm form,
			boolean showClipboard) {
		this.parent = parent;

		AlphaFormProvider.setForm(form);

		if (showClipboard) {
			currentView = new ClipboardView(form, this);
		} else {
			currentView = new DesignerView(form, this);
		}
		currentView.show(parent);

		SignalManager.getInstance().subscribeSink(this, "designer");
		SignalManager.getInstance().subscribeSink(this, "clipboard");
		SignalManager.getInstance().subscribeSink(this, "application");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#registerSaveListener(alpha.forms.util.
	 * FormSaveListener)
	 */
	@Override
	public void registerSaveListener(FormSaveListener l) {
		saveListeners.add(l);
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
		if (s instanceof SwitchViewSignal) {
			SwitchViewSignal v = (SwitchViewSignal) s;
			if (v.getToView() == SwitchViewSignal.VIEWER) {
				currentView.pauseView();
				previousView = currentView;
				currentView = new ClipboardView(form, this);
				currentView.show(parent);
			} else if (v.getToView() == SwitchViewSignal.DESIGNER) {
				currentView.pauseView();
				form.revertToState("clipboardStart");
				currentView = new DesignerView(form, this);
				currentView.show(parent);
			}
			parent.updateUI();
		}
	}

	/**
	 * Persist form.
	 */
	public void persistForm() {
		persistForm(null);
	}

	/**
	 * Persist form.
	 * 
	 * @param state
	 *            the state
	 */
	public void persistForm(String state) {
		PersistenceController p = new PersistenceController(this);
		p.storeForm(form, state);
	}

	/**
	 * Fire save event.
	 * 
	 * @param data
	 *            the data
	 */
	public void fireSaveEvent(ByteArrayOutputStream data) {
		for (FormSaveListener l : saveListeners) {
			l.save(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#setDocumentStates(java.util.Collection)
	 */
	@Override
	public void setDocumentStates(Collection<String> states) {
		if (form != null) {
			form.addDocumentStates(states);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#addDocumentState(java.lang.String)
	 */
	@Override
	public void addDocumentState(String state) {
		if (form != null) {
			form.addDocumentState(state);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.AlphaFormsFacade#getView()
	 */
	@Override
	public AlphaFormsView getView() {
		return parent;
	}

}
