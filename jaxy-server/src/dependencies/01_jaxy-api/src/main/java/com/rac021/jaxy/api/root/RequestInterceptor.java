
 package com.rac021.jaxy.api.root ;

 import javax.ws.rs.ext.Provider ;
 import javax.ws.rs.core.Response ;
 import javax.ws.rs.container.PreMatching ;
 import javax.ws.rs.container.ContainerRequestFilter ;
 import javax.ws.rs.container.ContainerRequestContext ;

 /**
 *
 * @author ryahiaoui
 */

 @Provider
 @PreMatching
 public class RequestInterceptor implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        
        if ( requestContext.getMethod() != null  && 
             requestContext.getMethod().equals("OPTIONS")) {
            // Handling preflight requests - CORS 
            requestContext.abortWith( Response.status(Response.Status.OK )
                          .header("Access-Control-Allow-Credentials", "true")
                          .header("Access-Control-Allow-Headers", "*")
                          .header("Access-Control-Allow-Methods", "OPTIONS, HEAD, GET")
                          .build() ) ;
        }
        
        String accept = requestContext.getHeaders().getFirst("Accept")     ;
        if (accept == null || accept.equals("*/*")) {
             requestContext.getHeaders().putSingle("Accept", "json/plain") ;
        }
    }

 }