
package com.rac021.jaxy.api.streamers ;

import javax.ws.rs.core.MultivaluedMap ;
import com.rac021.jaxy.api.manager.IResource ;

/**
 *
 * @author yahiaoui
 */

public interface IStreamer {

   void setStreamerConfigurator( IStreamerConfigurator iStreamerConfigurator ) ;
   
   public IStreamer wrapResource(  IResource resource       , 
                                   Class     dto            ,
                                   String    filteredNmames ,
                                   MultivaluedMap<String, String> ... sqlParams ) ;
   
}
