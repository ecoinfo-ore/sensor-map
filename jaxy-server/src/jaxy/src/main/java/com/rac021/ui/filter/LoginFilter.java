
package com.rac021.ui.filter ;

/**
 *
 * @author ryahiaoui
 */

import java.io.IOException ;
import javax.inject.Inject ;
import javax.servlet.Filter ;
import javax.servlet.FilterChain ;
import javax.servlet.FilterConfig ;
import javax.servlet.ServletRequest ;
import javax.servlet.ServletResponse ;
import javax.servlet.ServletException ;
import javax.annotation.PostConstruct ;
import javax.servlet.annotation.WebFilter ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import com.rac021.ui.beans.LoginAuthenticator ;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

/**
 * Filter checks if LoginBean has loginIn property set to true
 * If it is not set then request is being redirected to the
 * login.xhml page .
 */

@SessionScoped
@WebFilter( urlPatterns = { "*.xhtml", "*.html" })
public class LoginFilter implements Filter, Serializable {

    @Inject
    LoginAuthenticator loginAuthenticator ;

    static String      CONTEXT            ;
    
    @PostConstruct
    public void init() {
      System.out.println(" *** Init Login_Filter " + this )    ;
      CONTEXT = loginAuthenticator.getYamlConfigurator()
                                  .getRootApplicationContext() ;
        
    }

    /**
     * Checks if user is logged in.If not it redirects to the login.xhtml page.
     * 
     * @param request
     * @param response
     * @param chain
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)   {

        try {
            
            String url = ((HttpServletRequest) request).getRequestURL().toString()              ;
            
            if ( loginAuthenticator.isSecuredUI() && ! url.contains("javax.faces.resource"))    {
                
                if ( loginAuthenticator.isLoged() && url.toLowerCase().endsWith("login.xhtml")) {
                    
                    ((HttpServletResponse) response).sendRedirect( CONTEXT + "/index.xhtml")    ;
                    
                } else if ( ! loginAuthenticator.isLoged() ) {
                    
                    if (request instanceof HttpServletRequest) {
                        if (!url.toLowerCase().endsWith( CONTEXT + "/login.xhtml")) {
                            (( HttpServletResponse ) response )
                                           .sendRedirect( CONTEXT + "/login.xhtml") ;
                        }
                    }
                }
            }
            
            chain.doFilter(request, response)        ;
            
        } catch ( IOException | ServletException ex) {
            System.out.println(" Exception : " + ex.getMessage() ) ;
        } 
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
