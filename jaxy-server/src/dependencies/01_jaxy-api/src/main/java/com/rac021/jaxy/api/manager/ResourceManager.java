
package com.rac021.jaxy.api.manager ;

import java.util.List ;
import java.util.Collections ;
import javax.persistence.Query ;
import java.util.logging.Logger ;
import java.util.stream.Collectors ;
import javax.persistence.EntityManager ;
import java.util.concurrent.atomic.AtomicInteger ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author yahiaoui
 */

public class ResourceManager  {

    private static final Logger  LOGGER  = getLogger()          ;

    protected AtomicInteger      offset  = new AtomicInteger(0) ;
    
    public ResourceManager() {  }

    private void setLimitOffsetSQLParameter( Query query, int limit, int offset) {
        query.setParameter( 1 , offset) ;
        query.setParameter( 2 , limit)  ;
    }
  
    protected List<IDto> executeSQLQuery( EntityManager manager      , 
                                          String        sqlQuery     ,
                                          int           limit        ,
                                          Class         dtoClass     ,
                                          List<String>  keepFields ) {
        
        if( sqlQuery == null || sqlQuery.isEmpty() ) 
          return Collections.emptyList()           ;

        try {

            Query createSQLQuery = manager.createNativeQuery(sqlQuery)   ;
            
            int incOffset        = incOffset(limit)                      ;
            
            setLimitOffsetSQLParameter(createSQLQuery, limit, incOffset) ;
           
            return 
                
            (List<IDto>) createSQLQuery.getResultStream()
                                       .map( obj  -> DtoMapper.map( (Object[]) obj ,
                                                                    dtoClass       ,
                                                                    keepFields )   )
                                       .collect(Collectors.toList())               ;
            
        } catch ( Exception ex)            {
            throw new RuntimeException(ex) ;
        }
    }

    private int incOffset(int limit)   {
        return offset.addAndGet(limit) ;
    }
    
    protected void decOffset(int limit) {
        offset.addAndGet( - limit )     ;
    }

}

