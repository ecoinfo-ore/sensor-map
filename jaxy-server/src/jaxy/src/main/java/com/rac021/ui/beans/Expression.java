
package com.rac021.ui.beans ;

/**
 *
 * @author ryahiaoui
 */
public class Expression             {

    private final String filter     ;
    private final String expression ;
    private final String value      ;

    public Expression( String filter, String expression, String value ) {
        this.filter     = filter      ;
        this.expression = expression  ;
        this.value      = value       ;
    }

    public String getFilter() {
        return filter ;
    }

    public String getExpression() {
        return expression ;
    }

    public String getValue() {
        return value ;
    }

}
