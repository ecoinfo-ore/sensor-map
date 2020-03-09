
package com.rac021.jaxy.client.mvc ;

import java.awt.* ;
import java.beans.* ;
import javax.swing.* ;
import java.awt.event.* ;
import javax.swing.border.* ;
import javax.swing.event.*;
import net.miginfocom.swing.* ;
import com.jgoodies.forms.factories.* ;
import org.jdesktop.swingx.JXTaskPane ;
import org.jdesktop.swingx.JXHyperlink ;
import org.jdesktop.swingx.JXBusyLabel ;
import com.jgoodies.forms.layout.FormLayout ;
import org.jdesktop.swingx.JXTaskPaneContainer ;
import com.jgoodies.forms.factories.DefaultComponentFactory ;


public class MainFrame extends JFrame {
    
	public MainFrame() {		

            initComponents()                                ; 
            this.setExtendedState(MainFrame.MAXIMIZED_BOTH) ;

            JPanel panGraph = new JPanel()            ;
            panGraph.setLayout(new BorderLayout())    ;
            
            comboBox_Accept_Custom.setModel(new DefaultComboBoxModel<>(new String[] {
                                        "xml/plain"            ,
                                        "xml/encrypted"        ,
                                        "json/plain"           ,
                                        "json/encrypted"       ,
                                        "template/plain"       ,
                                        "template/encrypted"   ,
                                        "text/event-stream"
                                    }));

	}

        private void thisWindowClosing(WindowEvent e) {
            // TODO add your code here
        }

        private void xTaskPane1PropertyChange(PropertyChangeEvent e) {
            // TODO add your code here
        }

        private void HyperlinkPanelConfigurationActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        private void HyperlinkPanelSimulationActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        private void HyperlinkPanelResultatsActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        private void xTaskPane3PropertyChange(PropertyChangeEvent e) {
            // TODO add your code here
        }

        private void toggleButtonItemStateChanged(ItemEvent e) {
            // TODO add your code here
        }

        private void buttonArreterActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        public JLabel getLabel34() {
            return label34;
        }

        public void setLabel34(JLabel label34) {
            this.label34 = label34;
        }

        public JLabel getLabel16() {
            return label16;
        }

        public void setLabel16(JLabel label16) {
            this.label34 = label16;
        }

        public JCheckBox getCheckBoxHeader() {
            return checkBoxHeader;
        }

        public void setCheckBoxHeader(JCheckBox checkBoxHeader) {
            this.checkBoxHeader = checkBoxHeader;
        }

        public JCheckBox getCheckBoxVerb() {
            return checkBoxVerb;
        }

        public void setCheckBoxVerb(JCheckBox checkBoxVerb) {
            this.checkBoxVerb = checkBoxVerb;
        }

        public JComboBox<String> getComboBox_Cipher_Custom() {
            return comboBox_Cipher_Custom;
        }

        public JToggleButton getToggleButton() {
            return toggleButton;
        }
        
        public void setToggleButton(JToggleButton toggleButton) {
            this.toggleButton = toggleButton;
        }

        public JButton getButtonArreter() {
            return buttonArreter;
        }

        public void setButtonArreter(JButton buttonArreter) {
            this.buttonArreter = buttonArreter;
        }

        public JProgressBar getProgressBar() {
            return progressBar;
        }

        public void setProgressBar(JProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        public JXBusyLabel getXBusy() {
            return XBusy;
        }

        public void setXBusy(JXBusyLabel XBusy) {
            this.XBusy = XBusy;
        }

        public JTextField getTextField_URL_KEYCLOAK() {
            return textField_URL_KEYCLOAK;
        }

        public void setTextField_URL_KEYCLOAK(JTextField textField_URL_KEYCLOAK) {
            this.textField_URL_KEYCLOAK = textField_URL_KEYCLOAK;
        }

        public JTextField getTextField_RUL_SERVICE() {
            return textField_RUL_SERVICE;
        }

        public void setTextField_RUL_SERVICE(JTextField textField_RUL_SERVICE) {
            this.textField_RUL_SERVICE = textField_RUL_SERVICE;
        }

        public JButton getButton_Run_SSO() {
            return button_Run_SSO ;
        }

        public void setButton_Run(JButton button_Run_SSO ) {
            this.button_Run_SSO = button_Run_SSO ;
        }

        public JTextField getTextField_Username() {
            return textField_Username;
        }

        public void setTextField_Username(JTextField textField_Username) {
            this.textField_Username = textField_Username;
        }

        public JTextField getTextField_Client_id() {
            return textField_Client_id;
        }

        public void setTextField_Client_id(JTextField textField_Client_id) {
            this.textField_Client_id = textField_Client_id;
        }

        public JComboBox<String> getComboBox_Accept() {
            return comboBox_Accept;
        }

        public void setComboBox_Accept(JComboBox<String> comboBox_Accept) {
            this.comboBox_Accept = comboBox_Accept;
        }

        public JPasswordField getPasswordField_Password() {
            return passwordField_Password;
        }

        public void setPasswordField_Password(JPasswordField passwordField_Password) {
            this.passwordField_Password = passwordField_Password;
        }

        public JPasswordField getPasswordField_secret_id() {
            return passwordField_secret_id;
        }

        public void setPasswordField_secret_id(JPasswordField passwordField_secret_id) {
            this.passwordField_secret_id = passwordField_secret_id;
        }

        public JTextField getTextField_Params() {
            return textField_Params;
        }

        public void setTextField_Params(JTextField textField_Params) {
            this.textField_Params = textField_Params;
        }

        public JTextArea getTextArea_Result_SSO() {
            return textArea_Result_SSO ;
        }

        public void setTextArea_Result_SSO(JTextArea textArea_Result) {
            this.textArea_Result_SSO = textArea_Result;
        }

        public JButton getButton_Clear() {
            return button_Clear;
        }

        public void setButton_Clear(JButton button_Clear) {
            this.button_Clear = button_Clear;
        }

        private void button_RunActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        public JTextArea getTextArea_Token() {
            return textArea_Token;
        }

        public void setTextArea_Token(JTextArea textArea_Token) {
            this.textArea_Token = textArea_Token;
        }

        public JTextField getTextField_Keep_SSO() {
            return textField_Keep_SSO ;
        }

        public void setTextField_Keep_SSO (JTextField textField_Sort) {
            this.textField_Keep_SSO = textField_Sort;
        }

        public JCheckBox getCheckBox_Refresh_Token() {
            return checkBox_Refresh_Token;
        }

        public void setCheckBox_Refresh_Token(JCheckBox checkBox_Refresh_Token) {
            this.checkBox_Refresh_Token = checkBox_Refresh_Token;
        }

        private void button_ClearActionPerformed(ActionEvent e) {
        }

        public JXTaskPane getxTaskPane1() {
            return xTaskPane1;
        }

        public void setxTaskPane1(JXTaskPane xTaskPane1) {
            this.xTaskPane1 = xTaskPane1;
        }

        private void button_Script_SSOActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        public JButton getButton_Script_SSO() {
            return button_Script_SSO;
        }

        public void setButton_Script_SSO(JButton button_Script_SSO) {
            this.button_Script_SSO = button_Script_SSO;
        }

        public JButton getButton_Script_CUSTOM() {
            return button_Script_CUSTOM;
        }

        public void setButton_Script_CUSTOM(JButton button_Script_CUSTOM) {
            this.button_Script_CUSTOM = button_Script_CUSTOM;
        }

        public JTextField getTextField_RUL_SERVICE_Custom() {
            return textField_RUL_SERVICE_Custom;
        }

        public void setTextField_RUL_SERVICE_Custom(JTextField textField_RUL_SERVICE_Custom) {
            this.textField_RUL_SERVICE_Custom = textField_RUL_SERVICE_Custom;
        }

        public JTextField getTextField_Username_Custom() {
            return textField_Username_Custom;
        }

        public void setTextField_Username_Custom(JTextField textField_Username_Custom) {
            this.textField_Username_Custom = textField_Username_Custom;
        }

        public JTextField getTextField_TimeStamp() {
            return textField_TimeStamp;
        }

        public void setTextField_TimeStamp(JTextField textField_TimeStamp) {
            this.textField_TimeStamp = textField_TimeStamp;
        }

        public JCheckBox getCheckBox_TimeStamp() {
            return checkBox_TimeStamp;
        }

        public void setCheckBox_TimeStamp(JCheckBox checkBox_TimeStamp) {
            this.checkBox_TimeStamp = checkBox_TimeStamp;
        }

        public JTextField getTextField_Params_Custom() {
            return textField_Params_Custom;
        }

        public void setTextField_Params_Custom(JTextField textField_Params_Custom) {
            this.textField_Params_Custom = textField_Params_Custom;
        }

        public JTextField getTextField_Keep_Custom() {
            return textField_Keep_Custom;
        }

        public void setTextField_Keep_Custom(JTextField textField_Sort_Custom) {
            this.textField_Keep_Custom = textField_Sort_Custom;
        }

        public JComboBox<String> getComboBox_HashedLogin() {
            return comboBox_HashedLogin;
        }

        public void setComboBox_HashedLogin(JComboBox<String> comboBox_HashedLogin) {
            this.comboBox_HashedLogin = comboBox_HashedLogin;
        }

        public JComboBox<String> getComboBox_HashedPassword() {
            return comboBox_HashedPassword;
        }

        public void setComboBox_HashedPassword(JComboBox<String> comboBox_HashedPassword) {
            this.comboBox_HashedPassword = comboBox_HashedPassword;
        }

        public JComboBox<String> getComboBox_HashedTimeStamp() {
            return comboBox_HashedTimeStamp;
        }

        public void setComboBox_HashedTimeStamp(JComboBox<String> comboBox_HashedTimeStamp) {
            this.comboBox_HashedTimeStamp = comboBox_HashedTimeStamp;
        }

        public JComboBox<String> getComboBox_Accept_Custom() {
            return comboBox_Accept_Custom;
        }

        public void setComboBox_Accept_Custom(JComboBox<String> comboBox_Accept_Custom) {
            this.comboBox_Accept_Custom = comboBox_Accept_Custom;
        }

        public JTextArea getTextArea_Result_Custom() {
            return textArea_Result_Custom;
        }

        public void setTextArea_Result_Custom(JTextArea textArea_Result_Custom) {
            this.textArea_Result_Custom = textArea_Result_Custom;
        }

        public JButton getButton_Clear_Custom() {
            return button_Clear_Custom;
        }

        public void setButton_Clear_Custom(JButton button_Clear_Custom) {
            this.button_Clear_Custom = button_Clear_Custom;
        }

        public JButton getButton_Decrypt_Custom() {
            return button_Decrypt_Custom;
        }

        public JButton getButton_Run_Custom() {
            return button_Run_Custom;
        }

        public void setButton_Run_Custom(JButton button_Run_Custom) {
            this.button_Run_Custom = button_Run_Custom;
        }

        public JPasswordField getPasswordField_Password_Custom() {
            return passwordField_Password_Custom;
        }

        public void setPasswordField_Password_Custom(JPasswordField passwordField_Password_Custom) {
            this.passwordField_Password_Custom = passwordField_Password_Custom;
        }

        public JComboBox<String> getComboBox_AlgoSign() {
            return comboBox_AlgoSign;
        }

        public void setComboBox_AlgoSign(JComboBox<String> comboBox_AlgoSign) {
            this.comboBox_AlgoSign = comboBox_AlgoSign;
        }

        private void button_Decrypt_Custom_ActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        private void button_Script_CUSTOM_ActionPerformed(ActionEvent e) {
            // TODO add your code here
        }

        public JPanel getPanel35() {
            return panel35;
        }

        public void setPanel35(JPanel panel35) {
            this.panel35 = panel35;
        }

        public JButton getButton_Zoom_plus_customSign() {
            return button_Zoom_plus_customSign;
        }

        public JButton getButton_Zoom_minus_customSign() {
            return button_Zoom_minus_customSign;
        }

        public JButton getButton_Zoom_plus_sso() {
            return button_Zoom_plus_sso;
        }

        public JButton getButton_Zoom_minus_sso() {
            return button_Zoom_minus_sso;
        }

        public JXHyperlink getButton_Copy_ClipBoard_SSO() {
            return button_Copy_ClipBoard_SSO;
        }

        
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////

    public JXHyperlink getButton_Copy_ClipBoard_Custom() {
        return button_Copy_ClipBoard_Custom;
    }

    public void setButton_Copy_ClipBoard_Custom(JXHyperlink button_Copy_ClipBoard_Custom) {
        this.button_Copy_ClipBoard_Custom = button_Copy_ClipBoard_Custom;
    }

    private void tabbedPanePrincipalStateChanged(ChangeEvent e) {
       textArea_Token.setText("") ;
    }

        

        //////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	  
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
                // Generated using JFormDesigner Evaluation license - RAC YAH
                DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
                menuBar1 = new JMenuBar();
                menu1 = new JMenu();
                menuItemImportXML = new JMenuItem();
                menuItemExportXML = new JMenuItem();
                menuItemEXIT = new JMenuItem();
                menuConfig = new JMenu();
                menuItemConfigGle = new JMenuItem();
                menuItmChangPass = new JMenuItem();
                menuHelp = new JMenu();
                menuItemHelp = new JMenuItem();
                menuItemApropos = new JMenuItem();
                panel35 = new JPanel();
                label33 = new JLabel();
                scrollPane3 = new JScrollPane();
                xTaskPaneContainer1 = new JXTaskPaneContainer();
                xTaskPane1 = new JXTaskPane();
                separator4 = compFactory.createSeparator("");
                HyperlinkPanelConfiguration = new JXHyperlink();
                HyperlinkPanelSimulation = new JXHyperlink();
                HyperlinkPanelResult = new JXHyperlink();
                separator7 = compFactory.createSeparator("");
                xTaskPane4 = new JXTaskPane();
                separator9 = compFactory.createSeparator("");
                checkBoxHeader = new JCheckBox();
                checkBoxVerb = new JCheckBox();
                separator11 = compFactory.createSeparator("");
                toggleButton = new JToggleButton();
                separator10 = compFactory.createSeparator("");
                buttonArreter = new JButton();
                panel3 = new JPanel();
                label3 = new JLabel();
                scrollPane2 = new JScrollPane();
                textArea_Token = new JTextArea();
                Date_Header = new JPanel();
                label2 = new JLabel();
                LabelNbrThreads = new JLabel();
                label4 = new JLabel();
                LabelAsynch = new JLabel();
                progressBar = new JProgressBar();
                XBusy = new JXBusyLabel();
                tabbedPanePrincipal = new JTabbedPane();
                PanelConfig = new JPanel();
                PanelSSO3 = new JPanel();
                PanelConfig4 = new JPanel();
                label29 = new JLabel();
                separator8 = new JSeparator();
                label30 = new JLabel();
                label35 = new JLabel();
                label38 = new JLabel();
                label36 = new JLabel();
                label32 = new JLabel();
                textField_RUL_SERVICE_Custom = new JTextField();
                button_Run_Custom = new JButton();
                textField_Username_Custom = new JTextField();
                passwordField_Password_Custom = new JPasswordField();
                textField_TimeStamp = new JTextField();
                checkBox_TimeStamp = new JCheckBox();
                panel10 = new JPanel();
                label34 = new JLabel();
                textField_Params_Custom = new JTextField();
                label41 = new JLabel();
                label42 = new JLabel();
                label43 = new JLabel();
                label39 = new JLabel();
                panel11 = new JPanel();
                label37 = new JLabel();
                textField_Keep_Custom = new JTextField();
                comboBox_HashedLogin = new JComboBox<>();
                comboBox_HashedPassword = new JComboBox<>();
                comboBox_HashedTimeStamp = new JComboBox<>();
                comboBox_AlgoSign = new JComboBox<>();
                panel12 = new JPanel();
                label40 = new JLabel();
                comboBox_Accept_Custom = new JComboBox<>();
                panel13 = new JPanel();
                label44 = new JLabel();
                comboBox_Cipher_Custom = new JComboBox<>();
                button_Decrypt_Custom = new JXHyperlink();
                label5 = new JLabel();
                scrollPane5 = new JScrollPane();
                textArea_Result_Custom = new JTextArea();
                button_Script_CUSTOM = new JButton();
                button_Clear_Custom = new JButton();
                button_Copy_ClipBoard_Custom = new JXHyperlink();
                button_Zoom_plus_customSign = new JButton();
                button_Zoom_minus_customSign = new JButton();
                PanelSSO = new JPanel();
                PanelConfig2 = new JPanel();
                label10 = new JLabel();
                separator5 = new JSeparator();
                label11 = new JLabel();
                label7 = new JLabel();
                textField_URL_KEYCLOAK = new JTextField();
                label12 = new JLabel();
                textField_RUL_SERVICE = new JTextField();
                button_Run_SSO = new JButton();
                panel2 = new JPanel();
                label16 = new JLabel();
                textField_Params = new JTextField();
                label13 = new JLabel();
                textField_Username = new JTextField();
                label8 = new JLabel();
                textField_Client_id = new JTextField();
                panel5 = new JPanel();
                label17 = new JLabel();
                textField_Keep_SSO = new JTextField();
                label14 = new JLabel();
                passwordField_Password = new JPasswordField();
                label9 = new JLabel();
                passwordField_secret_id = new JPasswordField();
                panel1 = new JPanel();
                label15 = new JLabel();
                comboBox_Accept = new JComboBox<>();
                panel4 = new JPanel();
                checkBox_Refresh_Token = new JCheckBox();
                scrollPane1 = new JScrollPane();
                textArea_Result_SSO = new JTextArea();
                button_Script_SSO = new JButton();
                button_Clear = new JButton();
                button_Copy_ClipBoard_SSO = new JXHyperlink();
                button_Zoom_plus_sso = new JButton();
                button_Zoom_minus_sso = new JButton();
                PanelResult = new JPanel();
                label1 = new JLabel();

                //======== this ========
                setTitle("G-Jax-Client");
                setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                setMinimumSize(new Dimension(800, 770));
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        thisWindowClosing(e);
                    }
                });
                Container contentPane = getContentPane();
                contentPane.setLayout(new FormLayout(
                    "default, $lcgap, 80.0mm, 3*($lcgap, default), $lcgap, default:grow, $lcgap, default",
                    "default, $lgap, 15dlu, 3dlu, 9*($lgap, default), $lgap, default:grow, 2*($lgap, default)"));

                //======== menuBar1 ========
                {
                    menuBar1.setOpaque(false);
                    menuBar1.setInheritsPopupMenu(true);

                    //======== menu1 ========
                    {
                        menu1.setText(" File");
                        menu1.addSeparator();

                        //---- menuItemImportXML ----
                        menuItemImportXML.setText("Import XML Files");
                        menuItemImportXML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
                        menu1.add(menuItemImportXML);
                        menu1.addSeparator();

                        //---- menuItemExportXML ----
                        menuItemExportXML.setText("Exporte XML File");
                        menuItemExportXML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
                        menu1.add(menuItemExportXML);
                        menu1.addSeparator();

                        //---- menuItemEXIT ----
                        menuItemEXIT.setText("Exit");
                        menuItemEXIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK));
                        menu1.add(menuItemEXIT);
                        menu1.addSeparator();
                    }
                    menuBar1.add(menu1);

                    //======== menuConfig ========
                    {
                        menuConfig.setText("Configuration");
                        menuConfig.addSeparator();

                        //---- menuItemConfigGle ----
                        menuItemConfigGle.setText("Configuration G\u00e9n\u00e9rale");
                        menuItemConfigGle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, KeyEvent.CTRL_MASK));
                        menuConfig.add(menuItemConfigGle);
                        menuConfig.addSeparator();
                        menuConfig.addSeparator();

                        //---- menuItmChangPass ----
                        menuItmChangPass.setText("Modification du Mot de Passe");
                        menuItmChangPass.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
                        menuConfig.add(menuItmChangPass);
                        menuConfig.addSeparator();
                    }
                    menuBar1.add(menuConfig);

                    //======== menuHelp ========
                    {
                        menuHelp.setText("Help");
                        menuHelp.addSeparator();

                        //---- menuItemHelp ----
                        menuItemHelp.setText("Aide");
                        menuItemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK));
                        menuHelp.add(menuItemHelp);
                        menuHelp.addSeparator();
                        menuHelp.addSeparator();

                        //---- menuItemApropos ----
                        menuItemApropos.setText("A Propos");
                        menuItemApropos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK));
                        menuHelp.add(menuItemApropos);
                        menuHelp.addSeparator();
                    }
                    menuBar1.add(menuHelp);
                }
                setJMenuBar(menuBar1);

                //======== scrollPane3 ========
                {

                    //======== xTaskPaneContainer1 ========
                    {

                        //======== xTaskPane1 ========
                        {
                            xTaskPane1.setTitle("Configuration");
                            xTaskPane1.setToolTipText("yahiaoui rachid");
                            xTaskPane1.addPropertyChangeListener(e -> xTaskPane1PropertyChange(e));
                            Container xTaskPane1ContentPane = xTaskPane1.getContentPane();
                            xTaskPane1ContentPane.setLayout(new FormLayout(
                                "3*(default), $lcgap, default, $lcgap, default:grow",
                                "13*(default, $lgap), default"));
                            xTaskPane1ContentPane.add(separator4, CC.xywh(1, 3, 7, 1));

                            //---- HyperlinkPanelConfiguration ----
                            HyperlinkPanelConfiguration.setText("Configuration");
                            HyperlinkPanelConfiguration.setClickedColor(new Color(51, 51, 255));
                            HyperlinkPanelConfiguration.setFont(new Font("Tahoma", Font.BOLD, 12));
                            HyperlinkPanelConfiguration.addActionListener(e -> HyperlinkPanelConfigurationActionPerformed(e));
                            xTaskPane1ContentPane.add(HyperlinkPanelConfiguration, CC.xy(2, 9));

                            //---- HyperlinkPanelSimulation ----
                            HyperlinkPanelSimulation.setText("Simulation");
                            HyperlinkPanelSimulation.setClickedColor(new Color(51, 51, 255));
                            HyperlinkPanelSimulation.setFont(new Font("Tahoma", Font.BOLD, 12));
                            HyperlinkPanelSimulation.addActionListener(e -> HyperlinkPanelSimulationActionPerformed(e));
                            xTaskPane1ContentPane.add(HyperlinkPanelSimulation, CC.xy(2, 15));

                            //---- HyperlinkPanelResult ----
                            HyperlinkPanelResult.setText("Result");
                            HyperlinkPanelResult.setClickedColor(new Color(51, 51, 255));
                            HyperlinkPanelResult.setFont(new Font("Tahoma", Font.BOLD, 12));
                            HyperlinkPanelResult.addActionListener(e -> HyperlinkPanelSimulationActionPerformed(e));
                            xTaskPane1ContentPane.add(HyperlinkPanelResult, CC.xy(2, 19));
                            xTaskPane1ContentPane.add(separator7, CC.xywh(1, 25, 7, 1));
                        }
                        xTaskPaneContainer1.add(xTaskPane1);

                        //======== xTaskPane4 ========
                        {
                            xTaskPane4.setTitle("Process");
                            xTaskPane4.addPropertyChangeListener(e -> xTaskPane3PropertyChange(e));
                            Container xTaskPane4ContentPane = xTaskPane4.getContentPane();
                            xTaskPane4ContentPane.setLayout(new FormLayout(
                                "default, 6dlu, default, $lcgap, default:grow",
                                "default, $lgap, default, 7dlu, 2*(default, $lgap), 10dlu, 4*($lgap, default), 35dlu, default, $lgap, default"));
                            xTaskPane4ContentPane.add(separator9, CC.xywh(1, 3, 5, 1));

                            //---- checkBoxHeader ----
                            checkBoxHeader.setText(" Header");
                            checkBoxHeader.setBackground(Color.black);
                            checkBoxHeader.setForeground(Color.white);
                            xTaskPane4ContentPane.add(checkBoxHeader, CC.xy(3, 5));

                            //---- checkBoxVerb ----
                            checkBoxVerb.setText(" Verb");
                            checkBoxVerb.setBackground(Color.black);
                            checkBoxVerb.setForeground(Color.white);
                            xTaskPane4ContentPane.add(checkBoxVerb, CC.xy(3, 7));
                            xTaskPane4ContentPane.add(separator11, CC.xywh(1, 11, 5, 1));

                            //---- toggleButton ----
                            toggleButton.setText("Run");
                            toggleButton.addItemListener(e -> toggleButtonItemStateChanged(e));
                            xTaskPane4ContentPane.add(toggleButton, CC.xy(3, 15));
                            xTaskPane4ContentPane.add(separator10, CC.xywh(1, 17, 5, 1));

                            //---- buttonArreter ----
                            buttonArreter.setText("RAZ");
                            buttonArreter.setBackground(new Color(63, 255, 51));
                            buttonArreter.setForeground(Color.blue);
                            buttonArreter.setFont(new Font("Tahoma", Font.BOLD, 12));
                            buttonArreter.addActionListener(e -> {
			buttonArreterActionPerformed(e);
			buttonArreterActionPerformed(e);
		});
                            xTaskPane4ContentPane.add(buttonArreter, CC.xy(3, 21));
                        }
                        xTaskPaneContainer1.add(xTaskPane4);

                        //======== panel3 ========
                        {
                            panel3.setLayout(new MigLayout(
                                "filly,hidemode 3,aligny center",
                                // columns
                                "[grow,fill]",
                                // rows
                                "[]" +
                                "[360,fill]" +
                                "[fill]"));

                            //---- label3 ----
                            label3.setText("Token : ");
                            label3.setFont(new Font("DejaVu Sans", Font.BOLD, 13));
                            panel3.add(label3, "cell 0 0");

                            //======== scrollPane2 ========
                            {

                                //---- textArea_Token ----
                                textArea_Token.setLineWrap(true);
                                textArea_Token.setForeground(Color.blue);
                                textArea_Token.setEditable(false);
                                textArea_Token.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                                textArea_Token.setAutoscrolls(false);
                                scrollPane2.setViewportView(textArea_Token);
                            }
                            panel3.add(scrollPane2, "cell 0 1");
                        }
                        xTaskPaneContainer1.add(panel3);
                    }
                    scrollPane3.setViewportView(xTaskPaneContainer1);
                }
                contentPane.add(scrollPane3, CC.xywh(3, 3, 1, 22));

                //======== Date_Header ========
                {
                    Date_Header.setBackground(Color.black);
                    Date_Header.setLayout(new FormLayout(
                        "2*(default, $lcgap), left:12dlu, 30dlu, 2*(default, $lcgap), right:default:grow, 2*($lcgap, default), $lcgap, [2dlu,default]",
                        "2dlu, 1dlu, [15dlu,default,15dlu]"));

                    //---- label2 ----
                    label2.setText("         NB_CORES :");
                    label2.setForeground(Color.white);
                    label2.setFont(new Font("Tahoma", Font.PLAIN, 12));
                    Date_Header.add(label2, CC.xy(3, 3));

                    //---- LabelNbrThreads ----
                    LabelNbrThreads.setText("Default");
                    LabelNbrThreads.setForeground(Color.orange);
                    LabelNbrThreads.setFont(new Font("Tahoma", Font.BOLD, 12));
                    Date_Header.add(LabelNbrThreads, CC.xywh(5, 3, 2, 1));

                    //---- label4 ----
                    label4.setText("ASYNCH :");
                    label4.setForeground(Color.white);
                    label4.setFont(new Font("Tahoma", Font.PLAIN, 12));
                    Date_Header.add(label4, CC.xy(7, 3));

                    //---- LabelAsynch ----
                    LabelAsynch.setText("TRUE");
                    LabelAsynch.setForeground(Color.orange);
                    LabelAsynch.setFont(new Font("Tahoma", Font.BOLD, 12));
                    Date_Header.add(LabelAsynch, CC.xy(9, 3));

                    //---- progressBar ----
                    progressBar.setStringPainted(true);
                    progressBar.setForeground(Color.black);
                    progressBar.setFont(new Font("Tahoma", Font.PLAIN, 12));
                    progressBar.setBackground(Color.white);
                    Date_Header.add(progressBar, CC.xy(11, 3, CC.DEFAULT, CC.FILL));

                    //---- XBusy ----
                    XBusy.setText("Traitement des donne\u00e9s en cours,  Veuillez Pattienter...");
                    XBusy.setForeground(Color.orange);
                    Date_Header.add(XBusy, CC.xy(13, 3, CC.DEFAULT, CC.FILL));
                }
                contentPane.add(Date_Header, CC.xywh(5, 1, 7, 3, CC.DEFAULT, CC.FILL));

                //======== tabbedPanePrincipal ========
                {
                    tabbedPanePrincipal.setOpaque(true);
                    tabbedPanePrincipal.setBackground(new Color(255, 204, 102));
                    tabbedPanePrincipal.addChangeListener(e -> tabbedPanePrincipalStateChanged(e));

                    //======== PanelConfig ========
                    {
                        PanelConfig.setBorder(new CompoundBorder(
                            new TitledBorder(""),
                            new EmptyBorder(5, 5, 5, 5)));
                        PanelConfig.setLayout(new FormLayout(
                            "5*(default), 100dlu, 5dlu, default, 50dlu, 6*(default), default:grow",
                            "fill:default:grow"));

                        //======== PanelSSO3 ========
                        {
                            PanelSSO3.setLayout(new FormLayout(
                                "7dlu, 2*($lcgap, default), $lcgap, default:grow, 3*($lcgap, default), $lcgap, right:default:grow, 6dlu, default, 10dlu",
                                "4dlu, default, $lgap, default, 6dlu, default, 21dlu, default, $lgap, 25dlu, 3*($lgap, default), 15dlu:grow, 10dlu, 6dlu, 10dlu, fill:default, 11dlu"));

                            //======== PanelConfig4 ========
                            {
                                PanelConfig4.setBorder(new CompoundBorder(
                                    new TitledBorder(""),
                                    new EmptyBorder(5, 5, 5, 5)));
                                PanelConfig4.setBackground(SystemColor.window);
                                PanelConfig4.setLayout(new FormLayout(
                                    "2*(default), 10dlu, 2*(80dlu, 5dlu), 80dlu, 40dlu, default, 10dlu, 46dlu:grow, 2*(10dlu), default, 8dlu, default, 2dlu, 170dlu:grow, default, 8dlu, 40dlu, 5dlu",
                                    "default, 6dlu, 15dlu, 6dlu, 2*(default, $lgap), bottom:default, $lgap, default, $lgap, 20dlu, $lgap, default"));

                                //---- label29 ----
                                label29.setText("Authentication");
                                label29.setForeground(new Color(238, 30, 30));
                                label29.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
                                PanelConfig4.add(label29, CC.xy(4, 3));

                                //---- separator8 ----
                                separator8.setOrientation(SwingConstants.VERTICAL);
                                PanelConfig4.add(separator8, CC.xywh(14, 1, 1, 15));

                                //---- label30 ----
                                label30.setText("Service");
                                label30.setForeground(new Color(238, 30, 30));
                                label30.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
                                PanelConfig4.add(label30, CC.xy(17, 3));

                                //---- label35 ----
                                label35.setText("Login");
                                label35.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                PanelConfig4.add(label35, CC.xy(4, 5));

                                //---- label38 ----
                                label38.setText("Password");
                                label38.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                PanelConfig4.add(label38, CC.xy(6, 5));

                                //---- label36 ----
                                label36.setText("TimeStamp");
                                label36.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                PanelConfig4.add(label36, CC.xy(8, 5));

                                //---- label32 ----
                                label32.setText("URL");
                                label32.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                PanelConfig4.add(label32, CC.xy(17, 5));

                                //---- textField_RUL_SERVICE_Custom ----
                                textField_RUL_SERVICE_Custom.setFont(new Font("DejaVu Sans", Font.PLAIN, 15));
                                textField_RUL_SERVICE_Custom.setText("http://localhost:8080/rest/resources/infoServices");
                                PanelConfig4.add(textField_RUL_SERVICE_Custom, CC.xywh(19, 5, 2, 1));

                                //---- button_Run_Custom ----
                                button_Run_Custom.setText("Invoke");
                                button_Run_Custom.addActionListener(e -> button_RunActionPerformed(e));
                                PanelConfig4.add(button_Run_Custom, CC.xy(22, 5));

                                //---- textField_Username_Custom ----
                                textField_Username_Custom.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                                textField_Username_Custom.setText("admin");
                                PanelConfig4.add(textField_Username_Custom, CC.xy(4, 7));

                                //---- passwordField_Password_Custom ----
                                passwordField_Password_Custom.setText("admin");
                                PanelConfig4.add(passwordField_Password_Custom, CC.xy(6, 7));

                                //---- textField_TimeStamp ----
                                textField_TimeStamp.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                                textField_TimeStamp.setText("123456789");
                                PanelConfig4.add(textField_TimeStamp, CC.xy(8, 7));

                                //---- checkBox_TimeStamp ----
                                checkBox_TimeStamp.setText("Current TimeStamp ( AutoGen )");
                                checkBox_TimeStamp.setSelected(true);
                                PanelConfig4.add(checkBox_TimeStamp, CC.xywh(9, 7, 4, 1));

                                //======== panel10 ========
                                {
                                    panel10.setLayout(new MigLayout(
                                        "hidemode 3",
                                        // columns
                                        "[fill]" +
                                        "[grow,shrink 0,fill]" +
                                        "[fill]",
                                        // rows
                                        "[]"));

                                    //---- label34 ----
                                    label34.setText("Filter :  ");
                                    label34.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                    panel10.add(label34, "cell 0 0");

                                    //---- textField_Params_Custom ----
                                    textField_Params_Custom.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                                    textField_Params_Custom.setText("id=81_or_=_69");
                                    panel10.add(textField_Params_Custom, "cell 1 0 2 1,growx");
                                }
                                PanelConfig4.add(panel10, CC.xy(19, 7));

                                //---- label41 ----
                                label41.setText("Hashed Login");
                                label41.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                label41.setForeground(new Color(95, 97, 182));
                                PanelConfig4.add(label41, CC.xy(4, 9));

                                //---- label42 ----
                                label42.setText("Hashed Password");
                                label42.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                label42.setForeground(new Color(95, 97, 182));
                                PanelConfig4.add(label42, CC.xy(6, 9));

                                //---- label43 ----
                                label43.setText("Hashed TimeStamp");
                                label43.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                label43.setForeground(new Color(95, 97, 182));
                                PanelConfig4.add(label43, CC.xy(8, 9));

                                //---- label39 ----
                                label39.setText("Algo-Sign");
                                label39.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                label39.setForeground(new Color(226, 49, 49));
                                PanelConfig4.add(label39, CC.xy(12, 9));

                                //======== panel11 ========
                                {
                                    panel11.setLayout(new MigLayout(
                                        "hidemode 3",
                                        // columns
                                        "[fill]" +
                                        "[grow,shrink 0,fill]" +
                                        "[fill]",
                                        // rows
                                        "[]"));

                                    //---- label37 ----
                                    label37.setText("Keep :   ");
                                    label37.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                    panel11.add(label37, "cell 0 0");

                                    //---- textField_Keep_Custom ----
                                    textField_Keep_Custom.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                                    panel11.add(textField_Keep_Custom, "cell 1 0 2 1,growx");
                                }
                                PanelConfig4.add(panel11, CC.xy(19, 9));

                                //---- comboBox_HashedLogin ----
                                comboBox_HashedLogin.setModel(new DefaultComboBoxModel<>(new String[] {
                                    "PLAIN",
                                    "SHA1",
                                    "SHA2",
                                    "MD5"
                                }));
                                PanelConfig4.add(comboBox_HashedLogin, CC.xy(4, 11));

                                //---- comboBox_HashedPassword ----
                                comboBox_HashedPassword.setModel(new DefaultComboBoxModel<>(new String[] {
                                    "PLAIN",
                                    "SHA1",
                                    "SHA2",
                                    "MD5"
                                }));
                                comboBox_HashedPassword.setSelectedIndex(3);
                                PanelConfig4.add(comboBox_HashedPassword, CC.xy(6, 11));

                                //---- comboBox_HashedTimeStamp ----
                                comboBox_HashedTimeStamp.setModel(new DefaultComboBoxModel<>(new String[] {
                                    "PLAIN",
                                    "SHA1",
                                    "SHA2",
                                    "MD5"
                                }));
                                PanelConfig4.add(comboBox_HashedTimeStamp, CC.xy(8, 11));

                                //---- comboBox_AlgoSign ----
                                comboBox_AlgoSign.setModel(new DefaultComboBoxModel<>(new String[] {
                                    "SHA1",
                                    "SHA2",
                                    "MD5"
                                }));
                                comboBox_AlgoSign.setSelectedIndex(1);
                                PanelConfig4.add(comboBox_AlgoSign, CC.xywh(10, 11, 3, 1));

                                //======== panel12 ========
                                {
                                    panel12.setLayout(new MigLayout(
                                        "hidemode 3",
                                        // columns
                                        "[shrink 50,fill]" +
                                        "[grow,shrink 0,fill]",
                                        // rows
                                        "[]"));

                                    //---- label40 ----
                                    label40.setText("Accept : ");
                                    label40.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                    panel12.add(label40, "cell 0 0");

                                    //---- comboBox_Accept_Custom ----
                                    comboBox_Accept_Custom.setModel(new DefaultComboBoxModel<>(new String[] {
                                        "xml/plain"            ,
                                        "xml/encrypted"        ,
                                        "json/plain"           ,
                                        "json/encrypted"       ,
                                        "application/template" ,
                                        "text/event-stream"
                                    }));
                                    panel12.add(comboBox_Accept_Custom, "cell 1 0");
                                }
                                PanelConfig4.add(panel12, CC.xy(19, 11));

                                //======== panel13 ========
                                {
                                    panel13.setLayout(new MigLayout(
                                        "hidemode 3",
                                        // columns
                                        "[shrink 50,fill]" +
                                        "[grow,shrink 0,fill]",
                                        // rows
                                        "[]"));

                                    //---- label44 ----
                                    label44.setText("Cipher : ");
                                    label44.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                    panel13.add(label44, "cell 0 0");

                                    //---- comboBox_Cipher_Custom ----
                                    comboBox_Cipher_Custom.setModel(new DefaultComboBoxModel<>(new String[] {
                                        "AES_128_CBC",
                                        "AES_128_ECB",
                                        "AES_192_CBC",
                                        "AES_192_ECB",
                                        "AES_256_CBC",
                                        "AES_256_ECB",
                                        "DES_64_CBC",
                                        "DES_64_ECB",
                                        "DESede_192_CBC",
                                        "DESede_192_ECB"
                                    }));
                                    panel13.add(comboBox_Cipher_Custom, "cell 1 0");
                                }
                                PanelConfig4.add(panel13, CC.xy(19, 13));

                                //---- button_Decrypt_Custom ----
                                button_Decrypt_Custom.setText("Decrypt");
                                button_Decrypt_Custom.setClickedColor(new Color(51, 51, 255));
                                button_Decrypt_Custom.setFont(new Font("Tahoma", Font.BOLD, 12));
                                button_Decrypt_Custom.addActionListener(e -> HyperlinkPanelResultatsActionPerformed(e));
                                PanelConfig4.add(button_Decrypt_Custom, CC.xy(22, 13));

                                //---- label5 ----
                                label5.setText(" Decryption key Function for secured services = SHA-256 ( HashedPassword ( ... )  )");
                                label5.setForeground(new Color(91, 91, 91));
                                PanelConfig4.add(label5, CC.xywh(1, 15, 12, 1));
                            }
                            PanelSSO3.add(PanelConfig4, CC.xywh(2, 2, 16, 1));

                            //======== scrollPane5 ========
                            {

                                //---- textArea_Result_Custom ----
                                textArea_Result_Custom.setBackground(new Color(19, 19, 9));
                                textArea_Result_Custom.setForeground(new Color(248, 248, 248));
                                textArea_Result_Custom.setText("  Result ...");
                                textArea_Result_Custom.setTabSize(5);
                                textArea_Result_Custom.setLineWrap(true);
                                textArea_Result_Custom.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                                textArea_Result_Custom.setEditable(false);
                                scrollPane5.setViewportView(textArea_Result_Custom);
                            }
                            PanelSSO3.add(scrollPane5, CC.xywh(2, 6, 14, 16));

                            //---- button_Script_CUSTOM ----
                            button_Script_CUSTOM.setText("Script");
                            button_Script_CUSTOM.addActionListener(e -> button_Script_CUSTOM_ActionPerformed(e));
                            PanelSSO3.add(button_Script_CUSTOM, CC.xy(17, 6));

                            //---- button_Clear_Custom ----
                            button_Clear_Custom.setText("Clear ");
                            button_Clear_Custom.addActionListener(e -> button_ClearActionPerformed(e));
                            PanelSSO3.add(button_Clear_Custom, CC.xy(17, 8));

                            //---- button_Copy_ClipBoard_Custom ----
                            button_Copy_ClipBoard_Custom.setText("Copy_Text");
                            button_Copy_ClipBoard_Custom.setClickedColor(new Color(51, 51, 255));
                            button_Copy_ClipBoard_Custom.setFont(new Font("Tahoma", Font.BOLD, 12));
                            button_Copy_ClipBoard_Custom.addActionListener(e -> HyperlinkPanelResultatsActionPerformed(e));
                            PanelSSO3.add(button_Copy_ClipBoard_Custom, CC.xy(17, 12));

                            //---- button_Zoom_plus_customSign ----
                            button_Zoom_plus_customSign.setText("+");
                            button_Zoom_plus_customSign.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
                            button_Zoom_plus_customSign.addActionListener(e -> button_Decrypt_Custom_ActionPerformed(e));
                            PanelSSO3.add(button_Zoom_plus_customSign, CC.xy(17, 18, CC.DEFAULT, CC.CENTER));

                            //---- button_Zoom_minus_customSign ----
                            button_Zoom_minus_customSign.setText("-");
                            button_Zoom_minus_customSign.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
                            button_Zoom_minus_customSign.addActionListener(e -> button_Decrypt_Custom_ActionPerformed(e));
                            PanelSSO3.add(button_Zoom_minus_customSign, CC.xy(17, 20, CC.DEFAULT, CC.CENTER));
                        }
                        PanelConfig.add(PanelSSO3, CC.xywh(1, 1, 16, 1));
                    }
                    tabbedPanePrincipal.addTab("** Custom Jax-Client **", PanelConfig);

                    //======== PanelSSO ========
                    {
                        PanelSSO.setLayout(new FormLayout(
                            "7dlu, 2*($lcgap, default), $lcgap, default:grow, 3*($lcgap, default), $lcgap, right:default:grow, 6dlu, default, $lcgap, 10dlu",
                            "5dlu, 2*($lgap, default), 6dlu, default, $lgap, 15dlu, $lgap, default, $lgap, 25dlu, $lgap, default, $lgap, default:grow, $lgap, 10dlu, 6dlu, 10dlu, 15dlu, default"));

                        //======== PanelConfig2 ========
                        {
                            PanelConfig2.setBorder(new CompoundBorder(
                                new TitledBorder(""),
                                new EmptyBorder(5, 5, 5, 5)));
                            PanelConfig2.setBackground(SystemColor.window);
                            PanelConfig2.setLayout(new FormLayout(
                                "2*(default), 10dlu, 2*(default), 5dlu, 70dlu, 15dlu, default, 5dlu, 110dlu:grow, 2*(10dlu), default, 7dlu, default, 2dlu, 170dlu:grow, default, 8dlu, 40dlu, 2dlu, $lcgap, default",
                                "default, 6dlu, 15dlu, 6dlu, 4*(default, $lgap), 20dlu, $lgap, 10dlu"));

                            //---- label10 ----
                            label10.setText("Authorization ");
                            label10.setForeground(new Color(238, 30, 30));
                            label10.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
                            PanelConfig2.add(label10, CC.xy(4, 3));

                            //---- separator5 ----
                            separator5.setOrientation(SwingConstants.VERTICAL);
                            PanelConfig2.add(separator5, CC.xywh(13, 1, 1, 15));

                            //---- label11 ----
                            label11.setText("Service");
                            label11.setForeground(new Color(238, 30, 30));
                            label11.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
                            PanelConfig2.add(label11, CC.xy(16, 3));

                            //---- label7 ----
                            label7.setText("KeyCloak_URL");
                            label7.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                            PanelConfig2.add(label7, CC.xy(4, 5));

                            //---- textField_URL_KEYCLOAK ----
                            textField_URL_KEYCLOAK.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                            textField_URL_KEYCLOAK.setText("http://localhost:8180/auth/realms/serenity/protocol/openid-connect/token");
                            PanelConfig2.add(textField_URL_KEYCLOAK, CC.xywh(5, 5, 7, 1));

                            //---- label12 ----
                            label12.setText("URL");
                            label12.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                            PanelConfig2.add(label12, CC.xy(16, 5));

                            //---- textField_RUL_SERVICE ----
                            textField_RUL_SERVICE.setFont(new Font("DejaVu Sans", Font.PLAIN, 15));
                            textField_RUL_SERVICE.setText("http://localhost:8080/rest/resources/infoServices");
                            PanelConfig2.add(textField_RUL_SERVICE, CC.xywh(18, 5, 2, 1));

                            //---- button_Run_SSO ----
                            button_Run_SSO.setText("Invoke");
                            button_Run_SSO.addActionListener(e -> button_RunActionPerformed(e));
                            PanelConfig2.add(button_Run_SSO, CC.xy(21, 5));

                            //======== panel2 ========
                            {
                                panel2.setLayout(new MigLayout(
                                    "hidemode 3",
                                    // columns
                                    "[fill]" +
                                    "[grow,shrink 0,fill]" +
                                    "[fill]",
                                    // rows
                                    "[]"));

                                //---- label16 ----
                                label16.setText("Filter :  ");
                                label16.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                panel2.add(label16, "cell 0 0");

                                //---- textField_Params ----
                                textField_Params.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                                textField_Params.setText("id=81_or_=_69");
                                panel2.add(textField_Params, "cell 1 0 2 1,growx");
                            }
                            PanelConfig2.add(panel2, CC.xy(18, 7));

                            //---- label13 ----
                            label13.setText("user_name");
                            label13.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                            PanelConfig2.add(label13, CC.xy(5, 9));

                            //---- textField_Username ----
                            textField_Username.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                            textField_Username.setText("rac021");
                            PanelConfig2.add(textField_Username, CC.xy(7, 9));

                            //---- label8 ----
                            label8.setText("Client_id");
                            label8.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                            PanelConfig2.add(label8, CC.xy(9, 9));

                            //---- textField_Client_id ----
                            textField_Client_id.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                            textField_Client_id.setText("app-plane");
                            PanelConfig2.add(textField_Client_id, CC.xy(11, 9));

                            //======== panel5 ========
                            {
                                panel5.setLayout(new MigLayout(
                                    "hidemode 3",
                                    // columns
                                    "[fill]" +
                                    "[grow,shrink 0,fill]" +
                                    "[fill]",
                                    // rows
                                    "[]"));

                                //---- label17 ----
                                label17.setText("Keep :   ");
                                label17.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                panel5.add(label17, "cell 0 0");

                                //---- textField_Keep_SSO ----
                                textField_Keep_SSO.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                                panel5.add(textField_Keep_SSO, "cell 1 0 2 1,growx");
                            }
                            PanelConfig2.add(panel5, CC.xy(18, 9));

                            //---- label14 ----
                            label14.setText("Password");
                            label14.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                            PanelConfig2.add(label14, CC.xy(5, 11));

                            //---- passwordField_Password ----
                            passwordField_Password.setText("rac021");
                            PanelConfig2.add(passwordField_Password, CC.xy(7, 11));

                            //---- label9 ----
                            label9.setText("secret_id");
                            label9.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                            PanelConfig2.add(label9, CC.xy(9, 11));

                            //---- passwordField_secret_id ----
                            passwordField_secret_id.setText("6b64f7fa-a6ec-4f53-b941-2bdc74ef2640");
                            PanelConfig2.add(passwordField_secret_id, CC.xy(11, 11));

                            //======== panel1 ========
                            {
                                panel1.setLayout(new MigLayout(
                                    "hidemode 3",
                                    // columns
                                    "[shrink 50,fill]" +
                                    "[grow,shrink 0,fill]",
                                    // rows
                                    "[]"));

                                //---- label15 ----
                                label15.setText("Accept : ");
                                label15.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
                                panel1.add(label15, "cell 0 0");

                                //---- comboBox_Accept ----
                                comboBox_Accept.setModel(new DefaultComboBoxModel<>(new String[] {
                                    "xml/plain",
                                    "json/plain"
                                }));
                                panel1.add(comboBox_Accept, "cell 1 0");
                            }
                            PanelConfig2.add(panel1, CC.xy(18, 11));

                            //======== panel4 ========
                            {
                                panel4.setBackground(new Color(207, 233, 214));
                                panel4.setLayout(new MigLayout(
                                    "hidemode 3",
                                    // columns
                                    "[fill]" +
                                    "[grow,shrink 0,fill]" +
                                    "[fill]",
                                    // rows
                                    "[]"));

                                //---- checkBox_Refresh_Token ----
                                checkBox_Refresh_Token.setText("Refresh Token for each invokation");
                                checkBox_Refresh_Token.setSelected(true);
                                panel4.add(checkBox_Refresh_Token, "cell 2 0");
                            }
                            PanelConfig2.add(panel4, CC.xy(18, 13));
                        }
                        PanelSSO.add(PanelConfig2, CC.xywh(3, 3, 15, 1));

                        //======== scrollPane1 ========
                        {

                            //---- textArea_Result_SSO ----
                            textArea_Result_SSO.setBackground(new Color(19, 19, 9));
                            textArea_Result_SSO.setForeground(new Color(248, 248, 248));
                            textArea_Result_SSO.setText("  Result ...");
                            textArea_Result_SSO.setEditable(false);
                            textArea_Result_SSO.setTabSize(5);
                            textArea_Result_SSO.setLineWrap(true);
                            textArea_Result_SSO.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));
                            scrollPane1.setViewportView(textArea_Result_SSO);
                        }
                        PanelSSO.add(scrollPane1, CC.xywh(3, 7, 13, 15));

                        //---- button_Script_SSO ----
                        button_Script_SSO.setText("Script");
                        button_Script_SSO.addActionListener(e -> button_Script_SSOActionPerformed(e));
                        PanelSSO.add(button_Script_SSO, CC.xy(17, 7));

                        //---- button_Clear ----
                        button_Clear.setText("Clear ");
                        button_Clear.addActionListener(e -> button_ClearActionPerformed(e));
                        PanelSSO.add(button_Clear, CC.xy(17, 11));

                        //---- button_Copy_ClipBoard_SSO ----
                        button_Copy_ClipBoard_SSO.setText("Copy_Text");
                        button_Copy_ClipBoard_SSO.setClickedColor(new Color(51, 51, 255));
                        button_Copy_ClipBoard_SSO.setFont(new Font("Tahoma", Font.BOLD, 12));
                        button_Copy_ClipBoard_SSO.addActionListener(e -> HyperlinkPanelResultatsActionPerformed(e));
                        PanelSSO.add(button_Copy_ClipBoard_SSO, CC.xy(17, 15, CC.LEFT, CC.DEFAULT));

                        //---- button_Zoom_plus_sso ----
                        button_Zoom_plus_sso.setText("+");
                        button_Zoom_plus_sso.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
                        PanelSSO.add(button_Zoom_plus_sso, CC.xy(17, 19));

                        //---- button_Zoom_minus_sso ----
                        button_Zoom_minus_sso.setText("-");
                        button_Zoom_minus_sso.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
                        PanelSSO.add(button_Zoom_minus_sso, CC.xy(17, 21));
                    }
                    tabbedPanePrincipal.addTab("** KeyCloak-Jax-Client ( SSO ) **", PanelSSO);

                    //======== PanelResult ========
                    {
                        PanelResult.setLayout(new FormLayout(
                            "4*(default), default:grow",
                            "2*(default, $lgap), default"));
                    }
                    tabbedPanePrincipal.addTab("** Results **", PanelResult);

                    tabbedPanePrincipal.setSelectedIndex(0);
                }
                contentPane.add(tabbedPanePrincipal, CC.xywh(5, 5, 7, 22));
                pack();
                setLocationRelativeTo(getOwner());

                //---- label1 ----
                label1.setText("Achat Client");
                label1.setFont(new Font("Tahoma", Font.BOLD, 12));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - RAC YAH
        protected JMenuBar menuBar1;
        private JMenu menu1;
        private JMenuItem menuItemImportXML;
        private JMenuItem menuItemExportXML;
        private JMenuItem menuItemEXIT;
        private JMenu menuConfig;
        private JMenuItem menuItemConfigGle;
        private JMenuItem menuItmChangPass;
        private JMenu menuHelp;
        private JMenuItem menuItemHelp;
        private JMenuItem menuItemApropos;
        private JPanel panel35;
        private JLabel label33;
        protected JScrollPane scrollPane3;
        protected JXTaskPaneContainer xTaskPaneContainer1;
        protected JXTaskPane xTaskPane1;
        private JComponent separator4;
        private JXHyperlink HyperlinkPanelConfiguration;
        private JXHyperlink HyperlinkPanelSimulation;
        private JXHyperlink HyperlinkPanelResult;
        private JComponent separator7;
        protected JXTaskPane xTaskPane4;
        private JComponent separator9;
        private JCheckBox checkBoxHeader;
        private JCheckBox checkBoxVerb;
        private JComponent separator11;
        private JToggleButton toggleButton;
        private JComponent separator10;
        private JButton buttonArreter;
        private JPanel panel3;
        private JLabel label3;
        private JScrollPane scrollPane2;
        private JTextArea textArea_Token;
        private JPanel Date_Header;
        private JLabel label2;
        private JLabel LabelNbrThreads;
        private JLabel label4;
        private JLabel LabelAsynch;
        private JProgressBar progressBar;
        private JXBusyLabel XBusy;
        protected JTabbedPane tabbedPanePrincipal;
        private JPanel PanelConfig;
        protected JPanel PanelSSO3;
        private JPanel PanelConfig4;
        private JLabel label29;
        private JSeparator separator8;
        private JLabel label30;
        private JLabel label35;
        private JLabel label38;
        private JLabel label36;
        private JLabel label32;
        private JTextField textField_RUL_SERVICE_Custom;
        private JButton button_Run_Custom;
        private JTextField textField_Username_Custom;
        private JPasswordField passwordField_Password_Custom;
        private JTextField textField_TimeStamp;
        private JCheckBox checkBox_TimeStamp;
        private JPanel panel10;
        private JLabel label34;
        private JTextField textField_Params_Custom;
        private JLabel label41;
        private JLabel label42;
        private JLabel label43;
        private JLabel label39;
        private JPanel panel11;
        private JLabel label37;
        private JTextField textField_Keep_Custom;
        private JComboBox<String> comboBox_HashedLogin;
        private JComboBox<String> comboBox_HashedPassword;
        private JComboBox<String> comboBox_HashedTimeStamp;
        private JComboBox<String> comboBox_AlgoSign;
        private JPanel panel12;
        private JLabel label40;
        private JComboBox<String> comboBox_Accept_Custom;
        private JPanel panel13;
        private JLabel label44;
        private JComboBox<String> comboBox_Cipher_Custom;
        private JXHyperlink button_Decrypt_Custom;
        private JLabel label5;
        private JScrollPane scrollPane5;
        private JTextArea textArea_Result_Custom;
        private JButton button_Script_CUSTOM;
        private JButton button_Clear_Custom;
        private JXHyperlink button_Copy_ClipBoard_Custom;
        private JButton button_Zoom_plus_customSign;
        private JButton button_Zoom_minus_customSign;
        protected JPanel PanelSSO;
        private JPanel PanelConfig2;
        private JLabel label10;
        private JSeparator separator5;
        private JLabel label11;
        private JLabel label7;
        private JTextField textField_URL_KEYCLOAK;
        private JLabel label12;
        private JTextField textField_RUL_SERVICE;
        private JButton button_Run_SSO;
        private JPanel panel2;
        private JLabel label16;
        private JTextField textField_Params;
        private JLabel label13;
        private JTextField textField_Username;
        private JLabel label8;
        private JTextField textField_Client_id;
        private JPanel panel5;
        private JLabel label17;
        private JTextField textField_Keep_SSO;
        private JLabel label14;
        private JPasswordField passwordField_Password;
        private JLabel label9;
        private JPasswordField passwordField_secret_id;
        private JPanel panel1;
        private JLabel label15;
        private JComboBox<String> comboBox_Accept;
        private JPanel panel4;
        private JCheckBox checkBox_Refresh_Token;
        private JScrollPane scrollPane1;
        private JTextArea textArea_Result_SSO;
        private JButton button_Script_SSO;
        private JButton button_Clear;
        private JXHyperlink button_Copy_ClipBoard_SSO;
        private JButton button_Zoom_plus_sso;
        private JButton button_Zoom_minus_sso;
        protected JPanel PanelResult;
        private JLabel label1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
