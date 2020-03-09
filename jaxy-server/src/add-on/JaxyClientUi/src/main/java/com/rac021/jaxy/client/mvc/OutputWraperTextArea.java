
package com.rac021.jaxy.client.mvc;

/**
 *
 * @author ryahiaoui
 */

import javax.swing.JTextArea ;
import javax.swing.text.JTextComponent ;

public class OutputWraperTextArea implements IOutputWraper {
    
    private final JTextArea textArea  ;
    
    public OutputWraperTextArea( final JTextComponent output ) {
        textArea = (JTextArea) output ;
    }
 
    @Override
    public void write( String text )  {
      textArea.append( text )         ;
      textArea.setCaretPosition( textArea.getText().length() ) ;
    }

    @Override
    public String get()              {
      return this.textArea.getText() ;
    }
    
}