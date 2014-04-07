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
package net.mjrz.fm.ui.dialogs;

import static net.mjrz.fm.utils.Messages.tr;

import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.mjrz.fm.actions.ActionRequest;
import net.mjrz.fm.actions.ActionResponse;
import net.mjrz.fm.actions.AddAlertAction;
import net.mjrz.fm.entity.AlertsEntityManager;
import net.mjrz.fm.entity.beans.Account;
import net.mjrz.fm.entity.beans.Alert;
import net.mjrz.fm.entity.beans.User;
import net.mjrz.fm.ui.FinanceManagerUI;

import org.apache.log4j.Logger;

public class SetupAlertDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private String[] conditionals = { Alert.EXCEEDS, Alert.FALLS_BELOW };
	private FinanceManagerUI parent;
	private User user;
	private Account account;

	// Variables declaration - do not modify
	private javax.swing.JLabel accountDisplayLbl;
	private javax.swing.JLabel accountLbl;
	private javax.swing.JLabel amountLbl;
	private javax.swing.JTextField amountTf;
	private javax.swing.JLabel balanceLbl;
	private javax.swing.JButton cancel;
	private javax.swing.JComboBox conditionCb;
	private javax.swing.JComboBox rangeCb;
	private javax.swing.JLabel rangeLbl;
	private javax.swing.JButton save;
	private javax.swing.JLabel titleLbl;
	private javax.swing.JButton help;
	// End of variables declaration

	public static final int ADD = 1;
	public static final int UPDATE = 2;

	private static Logger logger = Logger.getLogger(SetupAlertDialog.class
			.getName());

	private int type = ADD;

	private Long existingAlertId = 0L;

	/** Creates new form SetupAlertDialog */
	public SetupAlertDialog(int type, JFrame parent, User user, Account account) {
		super(parent, tr("Setup alert"), true);
		this.type = type;
		this.parent = (FinanceManagerUI) parent;
		this.user = user;
		this.account = account;
		initComponents();
		setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);

		if (account.getAccountType() != net.mjrz.fm.constants.AccountTypes.ACCT_TYPE_EXPENSE) {
			rangeLbl.setVisible(false);
			rangeCb.setVisible(false);
			help.setVisible(false);
		}
		if (type == UPDATE) {
			populateFields();
		}
		net.mjrz.fm.ui.utils.GuiUtilities.addWindowClosingActionMap(this);
	}

	private void populateFields() {
		Alert a = getAlert();
		if (a == null) {
			return;
		}
		amountTf.setText(a.getAmount().toPlainString());

		for (int i = 0; i < conditionals.length; i++) {
			if (conditionals[i].equalsIgnoreCase(a.getConditional())) {
				conditionCb.setSelectedIndex(i);
				break;
			}
		}
		for (int i = 0; i < Alert.RANGE.length; i++) {
			if (Alert.RANGE[i].equalsIgnoreCase(a.getRange())) {
				rangeCb.setSelectedIndex(i);
				break;
			}
		}
		existingAlertId = a.getId();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		titleLbl = new javax.swing.JLabel();
		accountLbl = new javax.swing.JLabel();
		accountDisplayLbl = new javax.swing.JLabel();
		balanceLbl = new javax.swing.JLabel();
		conditionCb = new javax.swing.JComboBox();
		amountLbl = new javax.swing.JLabel();
		amountTf = new javax.swing.JTextField();
		rangeLbl = new javax.swing.JLabel();
		rangeCb = new javax.swing.JComboBox();
		save = new javax.swing.JButton();
		cancel = new javax.swing.JButton();
		help = new javax.swing.JButton();

		titleLbl.setText("<html><font size=+1><b>" + tr("Warn me when...")
				+ "</b></font></html>");

		accountLbl.setText(tr("Account"));

		accountDisplayLbl.setText(account.getAccountName());

		balanceLbl.setText(tr("Balance"));

		conditionCb
				.setModel(new javax.swing.DefaultComboBoxModel(conditionals));

		amountLbl.setText(tr("Amount"));

		amountTf.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				amountTfActionPerformed(evt);
			}
		});

		rangeLbl.setText(tr("For"));

		rangeCb.setModel(new javax.swing.DefaultComboBoxModel(Alert.RANGE));

		save.setText(tr("Save"));
		save.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});

		cancel.setText(tr("Cancel"));
		cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dispose();
			}
		});

		help.setIcon(new javax.swing.ImageIcon("icons/help.png"));
		help.setPreferredSize(new java.awt.Dimension(22, 20));
		help.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JOptionPane
						.showMessageDialog(
								SetupAlertDialog.this,
								tr("Monitors the account balance over the\nspecified time interval"));
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														titleLbl,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														376, Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING,
																				false)
																				.addComponent(
																						rangeLbl,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						amountLbl,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						balanceLbl,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						accountLbl,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						66,
																						Short.MAX_VALUE))
																.addGap(42, 42,
																		42)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						accountDisplayLbl,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						268,
																						Short.MAX_VALUE)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.TRAILING,
																												false)
																												.addComponent(
																														rangeCb,
																														javax.swing.GroupLayout.Alignment.LEADING,
																														0,
																														javax.swing.GroupLayout.DEFAULT_SIZE,
																														Short.MAX_VALUE)
																												.addComponent(
																														amountTf,
																														javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														conditionCb,
																														javax.swing.GroupLayout.Alignment.LEADING,
																														0,
																														117,
																														Short.MAX_VALUE)
																												.addGroup(
																														javax.swing.GroupLayout.Alignment.LEADING,
																														layout.createSequentialGroup()
																																.addComponent(
																																		save,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		80,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																.addPreferredGap(
																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																.addComponent(
																																		cancel,
																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																		78,
																																		javax.swing.GroupLayout.PREFERRED_SIZE)))
																								// .addGap(18,
																								// 18,
																								// 18)
																								.addGap(18,
																										18,
																										18)
																								.addComponent(
																										help,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										javax.swing.GroupLayout.PREFERRED_SIZE)))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(titleLbl,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										30,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(30, 30, 30)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(accountLbl)
												.addComponent(accountDisplayLbl))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(balanceLbl)
												.addComponent(
														conditionCb,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(amountLbl)
												.addComponent(
														amountTf,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(rangeLbl)
												.addComponent(
														rangeCb,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(help))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										33, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(cancel)
												.addComponent(save))
								.addContainerGap()));

		pack();
	}// </editor-fold>

	private void amountTfActionPerformed(java.awt.event.ActionEvent evt) {
	}

	private String msg = "";

	private boolean validateFields() {
		msg = "";
		try {
			Double.parseDouble(amountTf.getText());
		}
		catch (NumberFormatException nfe) {
			msg = tr("Invalid value for amount");
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private Alert getAlert() {
		try {
			java.util.List alerts = new AlertsEntityManager().getAlerts(account
					.getAccountId());
			if (alerts == null || alerts.size() == 0)
				return null;

			Alert a = (Alert) alerts.get(0);
			return a;
		}
		catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private void saveActionPerformed(java.awt.event.ActionEvent evt) {
		if (!validateFields()) {
			JOptionPane.showMessageDialog(parent, msg,
					tr("Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			return;
		}
		Alert a = new Alert();

		if (type == UPDATE) {
			a.setId(existingAlertId);
		}

		a.setAccountId(account.getAccountId());
		a.setAlertType(Alert.ALERT_TYPE_ACCOUNT);
		a.setConditional((String) conditionCb.getSelectedItem());
		a.setRange((String) rangeCb.getSelectedItem());
		a.setAmount(new BigDecimal(amountTf.getText()));
		a.setStatus(Alert.ALERT_NONE);

		ActionRequest req = new ActionRequest();
		req.setActionName("addAlertAction");
		req.setProperty("ENTRY", a);

		AddAlertAction action = new AddAlertAction();
		try {
			ActionResponse response = action.executeAction(req);
			if (response.getErrorCode() != ActionResponse.NOERROR) {
				JOptionPane.showMessageDialog(parent,
						response.getErrorMessage(),
						tr("Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$				
			}
			parent.reloadAccountList(account, false);
			dispose();
		}
		catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							parent,
							"Unable to add alert", tr("Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			e.printStackTrace();
		}
	}
}