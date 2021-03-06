/*******************************************************************************
 * Copyright  
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.mjrz.fm.ui.wizards.components;

import static net.mjrz.fm.utils.Messages.tr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JPanel;

import net.mjrz.fm.ui.utils.GuiUtilities;

/**
 * @author iFreeBudget ifreebudget@gmail.com
 * 
 */
public class UsernamePanel extends JPanel implements WizardComponent {

	private static final long serialVersionUID = 1L;
	private String msg = null;

	/** Creates new form LocationPanel */
	public UsernamePanel() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		password = new javax.swing.JTextField();
		GuiUtilities.removeCustomMouseListener(password);
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		setBackground(java.awt.Color.white);

		jLabel1.setText("Choose a username:");
		Font f = jLabel1.getFont().deriveFont(Font.BOLD);
		jLabel1.setFont(f);

		password.setEditable(true);

		jLabel2.setText("<html><p>User name must be between 4 and 20 characters<br>Cannot contain special characters</p></html>");
		// jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		//
		// jLabel3.setText("Cannot contain special characters");

		this.setLayout(new GridBagLayout());

		GridBagConstraints g1 = new GridBagConstraints();
		g1.gridx = 0;
		g1.gridy = 0;
		g1.weightx = 1;
		g1.fill = GridBagConstraints.BOTH;
		g1.insets = new Insets(10, 10, 10, 10);
		add(jLabel1, g1);

		g1 = new GridBagConstraints();
		g1.gridx = 0;
		g1.gridy = 1;
		g1.fill = GridBagConstraints.HORIZONTAL;
		g1.insets = new Insets(10, 10, 10, 10);
		add(password, g1);

		g1 = new GridBagConstraints();
		g1.gridx = 0;
		g1.gridy = 2;
		g1.fill = GridBagConstraints.HORIZONTAL;
		g1.insets = new Insets(10, 10, 10, 10);
		add(jLabel2, g1);

		// g1 = new GridBagConstraints();
		// g1.gridx = 0;
		// g1.gridy = 3;
		// g1.fill = GridBagConstraints.HORIZONTAL;
		// g1.insets = new Insets(10, 10, 10, 10);
		// add(jLabel3, g1);
	}

	// Variables declaration - do not modify
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JTextField password;

	// End of variables declaration

	private boolean validate(String uname) {
		boolean valid = true;
		if (uname.length() < 4 || uname.length() > 20) {
			msg = tr("Username must be between 4 and 20 characters\nCannot contain special characters.");
			valid = false;
			return valid;
		}
		if (valid) {
			for (int i = 0; i < uname.length(); i++) {
				if (Character.isWhitespace(uname.charAt(i))) {
					continue;
				}
				if (!Character.isLetterOrDigit(uname.charAt(i))) {
					msg = tr("Username must be between 4 and 20 characters\nCannot contain special characters.");
					valid = false;
					break;
				}
			}
		}
		if (net.mjrz.fm.Main.profileExists(uname)) {
			msg = "Profile " + uname + " already exists."
					+ tr("\nPlease choose another name");
			valid = false;
		}
		return valid;
	}

	public String[][] getValues() {
		String[][] ret = new String[1][1];

		String[] row = new String[2];
		row[0] = "USERNAME";
		row[1] = password.getText();

		ret[0] = row;

		return ret;
	}

	public boolean isComponentValid() {
		return validate(password.getText());
	}

	public String getMessage() {
		return msg;
	}

	public void setComponentFocus() {
		password.requestFocusInWindow();
	}

	public void updateComponentUI(HashMap<String, String[][]> values) {
	}
}
