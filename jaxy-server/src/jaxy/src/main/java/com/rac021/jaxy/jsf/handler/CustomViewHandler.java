

package com.rac021.jaxy.jsf.handler ;

/**
 *
 * @author ryahiaoui
 */
import javax.faces.component.UIViewRoot ;
import javax.faces.context.FacesContext ;
import javax.faces.application.ViewHandler ;
import javax.faces.application.ViewHandlerWrapper ;

public class CustomViewHandler extends ViewHandlerWrapper {

    public CustomViewHandler(ViewHandler wrapped)         {
        super(wrapped) ;
    }

    @Override
    public UIViewRoot restoreView(FacesContext facesContext, String viewId)              {

        /*
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance()
                                                                     .getExternalContext()
                                                                     .getContext()       ;
        
        String serverRealPath    = servletContext.getRealPath("/")                       ;
        String serverContextPath = servletContext.getContextPath()                       ;
        */
        
        UIViewRoot root          = super.restoreView(facesContext, viewId)               ;

        if (root == null) {

            root = createView( facesContext, "/index.xhtml") ;
            facesContext.renderResponse()                    ;
        }
        
        return root                                          ;
    }
}

