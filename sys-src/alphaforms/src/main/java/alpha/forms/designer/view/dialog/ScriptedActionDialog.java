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

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import alpha.forms.form.event.ActionFactory;
import alpha.forms.form.event.ScriptedAction;

/**
 * The Class ScriptedActionDialog.
 */
public class ScriptedActionDialog extends JDialog implements ActionListener {

	/** The action. */
	private ScriptedAction action;

	/** The code. */
	private JSEditorPane code;

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
	 * @param action
	 *            the action
	 * @return the scripted action dialog
	 */
	public static ScriptedActionDialog create(ScriptedAction action) {
		ScriptedActionDialog dialog = new ScriptedActionDialog();
		dialog.setAction(action);
		return dialog;
	}

	/**
	 * Instantiates a new scripted action dialog.
	 */
	public ScriptedActionDialog() {
		super((Frame) null, "Script Action:", true);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(true);

		code = new JSEditorPane();
		code.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		code.setSize(200, 300);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		save = new JButton("Save");
		save.addActionListener(this);
		save.setActionCommand("save");
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");

		buttonPanel.add(save);
		buttonPanel.add(cancel);

		add(new JScrollPane(code), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(save);
	}

	/**
	 * Sets the action.
	 * 
	 * @param action
	 *            the new action
	 */
	public void setAction(ScriptedAction action) {
		this.action = action;
		code.setText(action.getScriptCode());
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
			String source = code.getText();
			ScriptEngine engine = ActionFactory.getInstance().getScriptEngine();
			try {
				engine.eval("function stest(widget, form) {" + source + "}");
				setVisible(false);
				wasSaved = true;
				action.setScriptCode(code.getText());
			} catch (ScriptException ex) {
				JOptionPane.showMessageDialog(
						null,
						"There is an error in the script on line "
								+ ex.getLineNumber() + ":\n" + ex.getMessage(),
						"Script Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getActionCommand().equals("cancel")) {
			setVisible(false);
			wasCancled = true;
		}
	}
}
