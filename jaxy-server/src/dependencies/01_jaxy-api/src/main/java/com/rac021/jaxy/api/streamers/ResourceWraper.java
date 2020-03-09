
package com.rac021.jaxy.api.streamers ;

import java.util.List ;
import java.util.ArrayList ;
import javax.persistence.EntityManager ;
import com.rac021.jaxy.api.manager.IDto ;
import com.rac021.jaxy.api.manager.IResource ;

/**
 *
 * @author ryahiaoui
 */

public class ResourceWraper extends com.rac021.jaxy.api.manager.ResourceManager {
   
    private   final IResource resource  ;
    private   final Class     dto       ;
    protected final String    query     ;
    
    
    public ResourceWraper( IResource resource , 
                           Class  dto         ,  
                           String query )     {
        
        this.resource    = resource  ;
        this.dto         = dto       ;
        this.query       = query     ;
    }

    public IResource getResource() {
        return resource ;
    }

    public Class getDto() {
        return dto ;
    }
    
    public List<IDto> getDtoIterable( EntityManager em , 
                                      int limit        , 
                                      List<String> filterdIndex ) {
       
        if( query != null && ! query.isEmpty() ) {
            
            return executeSQLQuery( em           ,
                                    query        , 
                                    limit        , 
                                    dto          , 
                                    filterdIndex ) ;
        }
        return new ArrayList<>() ;
    }


    public void initResource( int limit ) {
        decOffset(limit)     ;
        initQueryParameter() ;
    }

    public void setOffset( int offset ) {
        this.offset.set(offset) ;
    }

    private void initQueryParameter() {
    }

}

