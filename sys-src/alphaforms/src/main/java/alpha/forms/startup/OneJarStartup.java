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
package alpha.forms.startup;

import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.CodeSource;

import javax.swing.JFrame;
import javax.swing.UIManager;

import alpha.forms.AlphaFormsFacade;
import alpha.forms.application.util.AlphaFormsFactory;
import alpha.forms.util.FormSaveListener;

/**
 * The Class OneJarStartup.
 */
public class OneJarStartup {

	/** The Constant DEFAULT_FORM_NAME. */
	protected final static String DEFAULT_FORM_NAME = "default.a-form.xml";

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		CodeSource codeSource = OneJarStartup.class.getProtectionDomain()
				.getCodeSource();
		System.out.println("alphaForms OneJar");
		String jarDir = null;
		String jarFileName = null;
		try {
			String jar = codeSource.getLocation().toURI().toString()
					.replace("jar:", "").replace("file:", "")
					.replaceFirst("!/.*", "");
			File jarFile = new File(jar);
			jarFileName = jarFile.getName();
			jarDir = jarFile.getParentFile().getPath();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		AlphaFormsFacade alphaForms = AlphaFormsFactory
				.createAlphaFormsApplication();
		final String alphaFormFile;

		if (args.length > 0) {
			String name = args[0];
			File file = new File(name);
			try {
				FileInputStream fs = new FileInputStream(file);
				alphaForms.start(fs);
			} catch (FileNotFoundException e) {
				alphaForms.start();
			}
			alphaFormFile = name;
		} else {
			alphaFormFile = jarDir
					+ File.separator
					+ ((jarFileName == null) ? DEFAULT_FORM_NAME : jarFileName
							.replaceFirst(".jar$", "") + ".a-form.xml");
			File file = new File(alphaFormFile);
			try {
				FileInputStream fs = new FileInputStream(file);
				alphaForms.start(fs);
			} catch (FileNotFoundException e) {
				alphaForms.start();
			}
		}

		alphaForms.registerSaveListener(new FormSaveListener() {

			@Override
			public void save(ByteArrayOutputStream form) {
				try {
					File out = new File(alphaFormFile);
					FileOutputStream fs = new FileOutputStream(out);
					form.writeTo(fs);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		System.out.println("alpha-form file: " + alphaFormFile);

		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame window = new JFrame("alphaForms");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(new BorderLayout());

		window.getContentPane().add(alphaForms.getView());
		window.setSize(alphaForms.getView().getMinimumSize());
		window.setVisible(true);
	}

}
