
package com.rac021.jaxy.client.mvc ;

import java.awt.Font ;
import java.time.Instant ;
import java.io.PrintWriter ;
import java.io.StringWriter ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.awt.event.ActionEvent ;
import java.io.UnsupportedEncodingException ;
import java.security.NoSuchAlgorithmException ;
import com.rac021.jaxy.client.security.Digestor ;
import com.rac021.jaxy.client.security.JceSecurity ;

/**
 *
 * @author ryahiaoui
 */

public class Controler       {

     private MainFrame frame ;

     String  token    = null ;
     
     
    public Controler() {
    }
     
    public Controler (MainFrame frame, Model model) {

        this.frame = frame     ;
       
        setListeners()         ;     
        
        frame.setVisible(true) ;
        
        JceSecurity.unlimit()  ;
    }
    
    private void setListeners()                 {
        
        button_Run_SSO_ActionPerformed()        ;
        button_Run_Custom_ActionPerformed()     ;
        button_ClearResult_ActionPerformed()    ;
        button_Gen_Script_SSO_ActionPerformed() ;
        button_Dectypt_ActionPerformed()        ;
        button_Clear_Custom_ActionPerformed()   ;
        button_Copy_ClipBoard_Custom()          ;
        button_Copy_ClipBoard_SSO()             ;
        button_Script_CUSTOM()                  ;
        button_Zoom_CUSTOM()                    ;
        button_Unzoom_CUSTOM()                  ;
        button_Zoom_SSO()                       ;
        button_Unzoom_SSO()                     ;
        button_Copy_ClipBoard_SSO()             ;

    }
    
    
    private void button_Run_SSO_ActionPerformed() {
        
        frame.getButton_Run_SSO().addActionListener(e -> {
            try {

                button_Run_ActionPerformed_SSO(e)        ;

            } catch (Exception ex) {
                Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex) ;
            }
        }) ;
    }
    
    private void button_Zoom_CUSTOM() {
        this.frame.getButton_Zoom_plus_customSign().addActionListener( e -> {
            if( frame.getTextArea_Result_Custom().getFont().getSize() < 50 ) {
                    frame.getTextArea_Result_Custom().setFont(new Font("DejaVu Sans", Font.PLAIN , 
                    frame.getTextArea_Result_Custom().getFont().getSize() + 1 )) ;
            }
        }) ;
    }
 
    private void button_Unzoom_CUSTOM() {
       this.frame.getButton_Zoom_minus_customSign().addActionListener( e ->  {
            if( frame.getTextArea_Result_Custom().getFont().getSize() > 15 ) {
                     frame.getTextArea_Result_Custom().setFont(new Font("DejaVu Sans", Font.PLAIN , 
                     frame.getTextArea_Result_Custom().getFont().getSize() - 1 )) ;
            }
      }) ;
    }
    
    private void button_Zoom_SSO() {
        this.frame.getButton_Zoom_plus_sso().addActionListener( e ->  {
            if( frame.getTextArea_Result_SSO().getFont().getSize() <  50 ) {
                     frame.getTextArea_Result_SSO().setFont(new Font("DejaVu Sans", Font.PLAIN , 
                     frame.getTextArea_Result_SSO().getFont().getSize() + 1 )) ;
            }
       }) ;
    }
 
    private void button_Unzoom_SSO() {
        this.frame.getButton_Zoom_minus_sso().addActionListener( e -> {
            if( frame.getTextArea_Result_SSO().getFont().getSize() > 15 ) {
                    frame.getTextArea_Result_SSO().setFont(new Font("DejaVu Sans", Font.PLAIN , 
                    frame.getTextArea_Result_SSO().getFont().getSize() - 1 )) ;
            }
        }) ;
    }

    private synchronized void button_Run_ActionPerformed_SSO(ActionEvent e) throws Exception {
         
        frame.getXBusy().setBusy(true)                ;
        frame.getProgressBar().setIndeterminate(true) ;
        frame.getTextArea_Result_SSO().setText("")    ;
        
        Runnable r = () -> {
        
            frame.getTextArea_Result_SSO().setText("") ;
         
            String keyCLoakUrl = frame.getTextField_URL_KEYCLOAK().getText()  ;
            String userName    = frame.getTextField_Username().getText()      ;
            String password    = frame.getPasswordField_Password().getText()  ;
            String client_id   = frame.getTextField_Client_id().getText()     ;
            String secret_id   = frame.getPasswordField_secret_id().getText() ;
            String keep        = frame.getTextField_Keep_SSO().getText()      ;

            if( frame.getCheckBox_Refresh_Token().isSelected() ||  this.token == null ) {
                
                try {
                    
                     this.token = Model.getToken( keyCLoakUrl, userName, password, client_id, secret_id) ;
                     
                     if( this.token == null || this.token.startsWith("_NULL_")) {
                         
                        frame.getTextArea_Result_SSO().setText(  frame
                             .getTextArea_Result_SSO().getText() + "\n" + 
                                      token.replace("_NULL_:", "  ") )  ;
                     }
                     
                } catch( Exception x ) {
                    
                     frame.getTextArea_Result_SSO().setText(" " + x.getMessage() )   ;
                     frame.getXBusy().setBusy(false)                                 ;
                     frame.getProgressBar().setIndeterminate(false)                  ;
                     
                     StringWriter errors = new StringWriter()                        ;
                     x.printStackTrace( new PrintWriter(errors))                     ;
                     frame.getTextArea_Result_SSO()
                      .setText( frame.getTextArea_Result_SSO().getText() + 
                                errors.toString() )                                  ;

                     return                                                          ;
                }
            }

            if( this.token != null && ! this.token.startsWith("_NULL_:"))             {

              frame.getTextArea_Token().setText(token.substring(0, 800 ) + "..." )    ;
              String url    = frame.getTextField_RUL_SERVICE().getText()              ;
              String accept = frame.getComboBox_Accept().getSelectedItem().toString() ;
              String params = frame.getTextField_Params().getText()                   ;

              
              try {

                   IOutputWraper out = new OutputWraperTextArea(frame.getTextArea_Result_SSO()) ;
                    
                   frame.getTextArea_Result_SSO()
                        .setText( Model.invokeService_Using_SSO( out         ,
                                                                 url         ,
                                                                 keyCLoakUrl ,
                                                                 params      ,
                                                                 accept      ,
                                                                 keep        ,
                                                                 client_id   ,
                                                                 secret_id   ,
                                                                 userName    ,
                                                                 password )  ) ;
                  
               } catch ( Exception ex ) {
                   
                  frame.getTextArea_Result_SSO().setText( "   " + ex.getMessage() ) ;
                  frame.getTextArea_Result_SSO().setText( ex.toString() )           ;
                  
                  StringWriter errors = new StringWriter()                          ;
                  ex.printStackTrace( new PrintWriter(errors))                      ;
                  frame.getTextArea_Result_SSO()
                      .setText( frame.getTextArea_Result_SSO().getText() + 
                                errors.toString() )                                 ;

                  System.out.println(ex)                                            ;

               } finally {
                  frame.getXBusy().setBusy(false)                ;
                  frame.getProgressBar().setIndeterminate(false) ;
               }
             }
              else {
                  frame.getXBusy().setBusy(false)                ;
                  frame.getProgressBar().setIndeterminate(false) ;
              }
        } ;
                 
        new Thread(r).start() ;
    }

    private void button_ClearResult_ActionPerformed()   {
          
        frame.getButton_Clear().addActionListener( e -> {
            try {
               this.frame.getTextArea_Result_SSO().setText("") ;
               this.frame.getTextArea_Token().setText("")      ;
            } catch (Exception ex) {
                Logger.getLogger( Controler.class.getName())
                                           .log(Level.SEVERE, null, ex) ;
            }
        }) ;
    }
    
    private void button_Gen_Script_SSO_ActionPerformed()     {
        
        frame.getButton_Script_SSO().addActionListener( e -> {
            
           try {
               
                String keyCLoakUrl = frame.getTextField_URL_KEYCLOAK().getText()  ;
                String userName    = frame.getTextField_Username().getText()      ;
                String password    = frame.getPasswordField_Password().getText()  ;
                String client_id   = frame.getTextField_Client_id().getText()     ;
                String secret_id   = frame.getPasswordField_secret_id().getText() ;
                String keep        = frame.getTextField_Keep_SSO().getText()      ;
                
                String url    = frame.getTextField_RUL_SERVICE().getText()              ;
                String params = frame.getTextField_Params().getText()                   ;
                String accept = frame.getComboBox_Accept().getSelectedItem().toString() ;
               
                frame.getTextArea_Result_SSO().setText ( Model.generateScriptSSO( keyCLoakUrl , 
                                                                                  userName    ,
                                                                                  password    ,
                                                                                  client_id   , 
                                                                                  secret_id   ,
                                                                                  keep        ,
                                                                                  url         ,
                                                                                  params      ,
                                                                                  accept )  ) ;
           } catch (UnsupportedEncodingException ex)                     {
                frame.getTextArea_Result_SSO().setText (ex.getMessage()) ;
            }
        }) ;
    }

    private void button_Run_Custom_ActionPerformed()        {
    
        frame.getButton_Run_Custom().addActionListener(e -> {
            try {
                button_Run_ActionPerformed_Custom(e) ;
            } catch (Exception ex) {
                Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex) ;
            }
        }) ;
    }

    private void button_Run_ActionPerformed_Custom( ActionEvent e ) 
                                                    throws NoSuchAlgorithmException, Exception {
       
        frame.getXBusy().setBusy(true)                ;
        frame.getProgressBar().setIndeterminate(true) ;
        frame.getTextArea_Result_Custom().setText("") ;
        frame.getTextArea_Token().setText("")         ;
        
        Runnable r = () -> {
           
          try {
                String params          = frame.getTextField_Params_Custom().getText()      ;
                String urlService      = frame.getTextField_RUL_SERVICE_Custom().getText() ;

                String keep      = frame.getTextField_Keep_Custom().getText()                     ;
                String accept    = frame.getComboBox_Accept_Custom().getSelectedItem().toString() ;

                String algoSign  = frame.getComboBox_AlgoSign().getSelectedItem().toString()      ;

                String login     =  hashMessage( frame.getTextField_Username_Custom().getText() , 
                                                 frame.getComboBox_HashedLogin().getSelectedItem().toString() )    ;

                String password  =  hashMessage( frame.getPasswordField_Password_Custom().getText() , 
                                                 frame.getComboBox_HashedPassword().getSelectedItem().toString())  ;

                String timeStamp =  hashMessage( frame.getTextField_TimeStamp().getText() , 
                                                 frame.getComboBox_HashedTimeStamp().getSelectedItem().toString()) ;
                
                String cipher    =  frame.getComboBox_Cipher_Custom().getSelectedItem().toString()                 ;
                 
                if(frame.getCheckBox_TimeStamp().isSelected())                  {
                     timeStamp = String.valueOf(Instant.now().getEpochSecond()) ;
                     frame.getTextField_TimeStamp().setText(timeStamp)          ;
                }
                
                String signe = null ;

                if(algoSign.equalsIgnoreCase("SHA1"))      {
                    signe = Digestor.toString(Digestor.toSHA1(login + password + timeStamp ))    ;
                }
                else if(algoSign.equalsIgnoreCase("MD5"))  {
                    signe = Digestor.toString( Digestor.toMD5(login + password + timeStamp ))    ;
                }
                else if(algoSign.equalsIgnoreCase("SHA2")) {
                    signe = Digestor.toString( Digestor.toSHA256(login + password + timeStamp )) ;
                }

                IOutputWraper out = new OutputWraperTextArea(frame.getTextArea_Result_Custom() ) ;
            
                Model.invokeService_Using_Custom( out        ,
                                                  urlService , 
                                                  params     ,
                                                  accept     , 
                                                  login + " " + timeStamp + " " + signe , 
                                                  keep         ,
                                                  cipher       ) ;
                
                frame.getXBusy().setBusy(false)                  ;
                frame.getProgressBar().setIndeterminate(false)   ;
          
          } catch( Exception x ) {

                frame.getXBusy().setBusy(false)                            ;
                frame.getProgressBar().setIndeterminate(false)             ;
                frame.getTextArea_Result_Custom().setText(x.getMessage())  ;
                
                StringWriter errors = new StringWriter()                   ;
                x.printStackTrace( new PrintWriter(errors))                ;
                frame.getTextArea_Result_Custom()
                     .setText( frame.getTextArea_Result_Custom().getText() + 
                               x.getMessage() )                            ;
          }    
        } ;
                
       new Thread(r).start() ;
       
    }

    private String hashMessage( String message, String hashAlog ) throws NoSuchAlgorithmException   {
        if(hashAlog.equalsIgnoreCase("SHA1")) return Digestor.toString( Digestor.toSHA1(message))   ;
        if(hashAlog.equalsIgnoreCase("SHA2")) return Digestor.toString( Digestor.toSHA256(message)) ;
        if(hashAlog.equalsIgnoreCase("MD5"))  return Digestor.toString( Digestor.toMD5(message))    ;
        // Else PLAIN
        return message ;
    }

    private void button_Dectypt_ActionPerformed() {
    
         frame. getButton_Decrypt_Custom().addActionListener( e -> {
            try {
                decryptMessage(e)  ;
            } catch (Exception ex) {
                 frame.getTextArea_Token().setText( ex.getMessage()) ;
            }
        }) ;
    }

    private void decryptMessage(ActionEvent e ) throws Exception {
        
        frame.getTextArea_Token().setBackground(java.awt.Color.WHITE )                 ;
        frame.getTextArea_Token().setText("")                                          ;
        String cipher = frame.getComboBox_Cipher_Custom().getSelectedItem().toString() ;
               
        String password = hashMessage( frame.getPasswordField_Password_Custom().getText()  , 
                                       frame.getComboBox_HashedPassword().getSelectedItem().toString()) ;
        try { 
            
            String decryptedMessage = Model.decrypt( cipher   , 
                                                     password ,
                                                     frame.getTextArea_Result_Custom()
                                                          .getText())    ;

            frame.getTextArea_Result_Custom().setText( decryptedMessage) ;
            
        } catch( Exception x ) {
            
            frame.getTextArea_Token().setText( " Exception When Trying to Decrypt.. \n " +
                                               x.getMessage())                           ;
        }
    }

    private void button_Clear_Custom_ActionPerformed() {
        
         frame.getButton_Clear_Custom().addActionListener(e -> {
            try {
               this.frame.getTextArea_Result_Custom().setText("") ;
               this.frame.getTextArea_Token().setText("")         ;
            } catch (Exception ex) {
                Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex) ;
            }
        }) ;
    }

    private void button_Script_CUSTOM() {
  
       frame.getButton_Script_CUSTOM().addActionListener(  e ->  {

         try {
              this.frame.getTextArea_Result_Custom().setText("") ;
              this.frame.getTextArea_Token().setText("")         ;
              
              String url           =  frame.getTextField_RUL_SERVICE_Custom().getText()  ;
              String login         =  frame.getTextField_Username_Custom().getText()     ;
              String password      =  frame.getPasswordField_Password_Custom().getText() ;

              String params        =  frame.getTextField_Params_Custom().getText()       ;
              String keep          =  frame.getTextField_Keep_Custom().getText()         ;

              String algoSign      =  frame .getComboBox_AlgoSign().getSelectedItem().toString()       ;
              
              String accept        =  frame.getComboBox_Accept_Custom().getSelectedItem().toString()   ;
              String hashLogin     =  frame.getComboBox_HashedLogin().getSelectedItem().toString()     ;
              String hashPassword  =  frame.getComboBox_HashedPassword().getSelectedItem().toString()  ;
              String hashTimeStamp =  frame.getComboBox_HashedTimeStamp().getSelectedItem().toString() ;
              String cipher        =  frame.getComboBox_Cipher_Custom().getSelectedItem().toString()   ;
              
              frame.getTextArea_Result_Custom().setText ( Model.generateScriptCUSTOM( url           , 
                                                                                      login         ,
                                                                                      password      ,
                                                                                      params        ,
                                                                                      keep          ,
                                                                                      accept        ,
                                                                                      hashLogin     ,
                                                                                      hashPassword  ,
                                                                                      hashTimeStamp ,
                                                                                      algoSign      ,   
                                                                                      cipher.trim() )) ;
           } catch ( UnsupportedEncodingException ex ) {
                frame.getTextArea_Result_SSO().setText (ex.getMessage()) ;
            }
        }) ;
    }

    private void button_Copy_ClipBoard_Custom() {
        frame.getButton_Copy_ClipBoard_Custom().addActionListener(e -> {
            try {
               Model.copyToClipBoard(this.frame.getTextArea_Result_Custom().getText())  ;
            } catch (Exception ex) {
                Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex) ;
            }
        }) ;
    }

    private void button_Copy_ClipBoard_SSO() {
        frame.getButton_Copy_ClipBoard_SSO().addActionListener(e -> {
            try {
               Model.copyToClipBoard(this.frame.getTextArea_Result_SSO().getText())     ;
            } catch (Exception ex) {
                Logger.getLogger(Controler.class.getName()).log(Level.SEVERE, null, ex) ;
            }
        }) ;
    }
    
}
