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
package alpha.forms.designer.view.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import alpha.forms.form.validation.Validator;
import alpha.forms.form.validation.ValidatorFactory;
import alpha.forms.form.validation.ValidatorGroup;
import alpha.forms.widget.model.FormWidget;

/**
 * The Class ValidatorDialog.
 */
public class ValidatorDialog extends JDialog implements ActionListener {

	/** The validator. */
	private Validator validator;

	/** The group. */
	private ValidatorGroup group;

	/** The available validators. */
	private List<Validator> availableValidators = new ArrayList<Validator>();

	/** The validator choice. */
	private JComboBox validatorChoice;

	/** The panel. */
	private JPanel panel;

	/** The save. */
	private JButton save;

	/** The cancel. */
	private JButton cancel;

	/** The was cancled. */
	private boolean wasCancled;

	/** The was saved. */
	private boolean wasSaved;

	/**
	 * Creates the.
	 * 
	 * @param v
	 *            the v
	 * @param group
	 *            the group
	 * @param w
	 *            the w
	 * @return the validator dialog
	 */
	public static ValidatorDialog create(Validator v, ValidatorGroup group,
			FormWidget w) {
		ValidatorDialog dialog = new ValidatorDialog(w, group, v);
		// dialog.setGroup(group);
		// dialog.setValidator(v);
		return dialog;
	}

	/**
	 * Instantiates a new validator dialog.
	 * 
	 * @param w
	 *            the w
	 * @param group
	 *            the group
	 * @param validator
	 *            the validator
	 */
	public ValidatorDialog(FormWidget w, ValidatorGroup group,
			Validator validator) {
		super((Frame) null, "Validator:", true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);

		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		panel.add(new JSeparator(), BorderLayout.CENTER);

		List<Validator> validatorList = ValidatorFactory
				.getAvailableValidators();

		for (Validator v : validatorList) {
			if (!group.contains(v) && v.isCompatibleWith(w.getClass())) {
				availableValidators.add(v);
			}
		}

		JPanel buttonPanel = new JPanel(new FlowLayout());

		save = new JButton("Save");
		save.addActionListener(this);
		save.setActionCommand("save");
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");

		buttonPanel.add(save);
		buttonPanel.add(cancel);

		add(panel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(save);

		setGroup(group);
		setValidator(validator);
	}

	/**
	 * Sets the validator.
	 * 
	 * @param validator
	 *            the new validator
	 */
	public void setValidator(Validator validator) {
		if (validator != null) {
			this.validator = validator;
			validatorChoice.addItem(validator);
			validatorChoice.setSelectedItem(validator);
			validatorChoice.setEnabled(false);
			save.setEnabled(true);
		} else if (validatorChoice.getModel().getSize() > 0) {
			validatorChoice.setSelectedIndex(0);
		}
	}

	/**
	 * Gets the validator.
	 * 
	 * @return the validator
	 */
	public Validator getValidator() {
		return validator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	public void setVisible(boolean flag) {
		if (flag == true) {
			pack();
			wasCancled = false;
			wasSaved = false;
		}
		super.setVisible(flag);
	}

	/**
	 * Sets the group.
	 * 
	 * @param group
	 *            the new group
	 */
	public void setGroup(ValidatorGroup group) {
		this.group = group;

		validatorChoice = new JComboBox(availableValidators.toArray());

		if (availableValidators.size() == 0) {
			validatorChoice.setEnabled(false);
			save.setEnabled(false);
		}

		validatorChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Validator v = (Validator) validatorChoice.getSelectedItem();
				if (v != null) {
					validator = v;
					panel.add(v.getOptionUI(), BorderLayout.SOUTH);
					pack();
				}
			}
		});

		panel.add(validatorChoice, BorderLayout.NORTH);
	}

	/**
	 * Was cancled.
	 * 
	 * @return true, if successful
	 */
	public boolean wasCancled() {
		return wasCancled;
	}

	/**
	 * Was saved.
	 * 
	 * @return true, if successful
	 */
	public boolean wasSaved() {
		return wasSaved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			setVisible(false);
			wasSaved = true;
		} else if (e.getActionCommand().equals("cancel")) {
			setVisible(false);
			wasCancled = true;
		}
	}
}
