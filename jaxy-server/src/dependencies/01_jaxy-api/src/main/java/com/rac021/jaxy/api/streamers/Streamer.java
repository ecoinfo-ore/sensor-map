
package com.rac021.jaxy.api.streamers ;

import java.util.List ;
import java.util.ArrayList ;
import javax.inject.Inject ;
import java.util.logging.Logger ;
import java.util.stream.IntStream ;
import javax.annotation.PreDestroy ;
import java.util.stream.Collectors ;
import java.util.concurrent.Callable ;
import javax.annotation.PostConstruct ;
import javax.persistence.EntityManager ;
import javax.ws.rs.core.MultivaluedMap ;
import com.rac021.jaxy.api.manager.IDto ;
import java.util.concurrent.BlockingQueue ;
import javax.persistence.PersistenceContext ;
import com.rac021.jaxy.api.manager.IResource ;
import javax.enterprise.context.RequestScoped ;
import java.util.concurrent.ExecutionException ;
import java.util.concurrent.ArrayBlockingQueue ;
import com.rac021.jaxy.api.root.ServicesManager ;
import com.rac021.jaxy.api.analyzer.SqlAnalyzer ;
import com.rac021.jaxy.api.root.RuntimeServiceInfos ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;
import static com.rac021.jaxy.api.streamers.DefaultStreamerConfigurator.* ;

/**
 *
 * @author yahiaoui
 */

@RequestScoped
public abstract class Streamer implements IStreamer {
 
    public static final String PU = "MyPU"          ;
    
    @PersistenceContext  ( unitName  = Streamer.PU  )
    private EntityManager entityManager             ;

    @Inject 
    protected ServicesManager servicesManager       ;
 
    protected static final         Logger LOGGER   = getLogger()       ;
    
    protected  BlockingQueue<IDto> dtos                                ;
    
    protected  int                 maxThreads                          ;
    
    protected  ResourceWraper      resource                            ;
    
    private    List<String>        keepFieldsList          = null      ;
    
    protected  boolean             isFinishedProcess       = false     ;
    
    @PreDestroy   
    public void cleanup() {
    }
     
    @PostConstruct
    public void init() {
    }
    
    public Streamer() { }

    public long producerScheduler()                 {
        
        resource.initResource( selectSize * ratio ) ;

        List<Callable<Void>> jobs = IntStream.range( 0 , maxThreads )
                                             .mapToObj( i -> (Callable<Void>) new Producer() )
                                             .collect(Collectors.toList());
        try {
                return 
                poolProducer.invokeAll(jobs)
                            .stream()
                            .map ( future -> { try { return future.get() ; }
                                   catch ( InterruptedException | ExecutionException e) {
                                     throw new RuntimeException(e)      ;
                                   } } )
                            .count() ;

        } catch( RuntimeException  | InterruptedException e ) {
          throw new RuntimeException(e) ;
        } finally                       {
          isFinishedProcess = true      ;
        }
    }

    protected void configureStreamer() {
  
      this.maxThreads = servicesManager.getOrDefaultMaxThreadsFor ( RuntimeServiceInfos.SERVICE_NAME.get() ) ;
      
      dtos = new ArrayBlockingQueue<>( ratio * selectSize * this.maxThreads ) ;
       
    }

    protected class Producer implements Callable  {
        
        @Override
        public Void call()  {
           
                while ( ! isFinishedProcess )     {

                    try { 
                          long count = resource.getDtoIterable( entityManager, 
                                                                selectSize * ratio ,
                                                                keepFieldsList     )
                                               .stream()
                                               .map( (localDto)  ->           {
                                                     try {
                                                         dtos.put(localDto)   ;
                                                         return localDto      ;
                                                     } catch (InterruptedException ex)   {
                                                         throw new RuntimeException(ex)  ;
                                                     }})
                                               .count() ;

                         // sql result = 0 OR // sql result < selectSize
                         if ( count == 0 || count < selectSize ) {

                             isFinishedProcess = true ;
                             break                    ;
                         } 
                     
                    } catch ( Exception ex ) {
                       throw new RuntimeException(ex) ;
                   }
                }

                return null ;
        }
    }
    
    public void rootResourceWraper( IResource resource     ,
                                    Class     dto          , 
                                    String    keepFields   , 
                                    MultivaluedMap <String ,
                                    String> ... filedsFilters ) {
        
        this.keepFieldsList = toListNames(keepFields) ;
        
        String  queryWithAppliedFilters  = null       ;
        
        if( filedsFilters != null )                   {
            try {
            queryWithAppliedFilters = SqlAnalyzer.generateQueryAccordingFieldsFilters (
                                                     servicesManager.getQueriesByResourceName ( 
                                                      resource.getClass().getName() 
                                                     ) ,  filedsFilters[0] )                  ;
            } catch( Exception ex )            {
                throw new RuntimeException(ex) ;
            }
        }

        this.resource = new ResourceWraper( resource, dto, queryWithAppliedFilters ) ; 
    }
    
    protected List<String> toListNames( String  names )  {
        
      if( names != null && ! names.isEmpty() )        {
       
           List<String> l = new ArrayList<>()         ; 
           String[] split = names.split("-")          ;
       
           for ( String fieldName : split )           {
               l.add( fieldName.trim()
                               .replaceAll(" +", "")) ;
           }
       
           return l ;
      }
      
      return null ;
    }
      
    @Override
    public void setStreamerConfigurator( IStreamerConfigurator iStreamerConfigurator ) {
        this.maxThreads = iStreamerConfigurator.getMaxThreads() ;
    }    
    
}

