package com.rac021.jaxy.service.sensorthings;

/**
 *
 * @author ryahiaoui
 */
import java.util.Map;
import java.util.List;
import javax.ws.rs.GET;
import javax.json.Json;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.ArrayList;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Produces;
import javax.json.JsonObject;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.HeaderParam;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import io.vertx.core.AsyncResult;
import javax.ws.rs.core.Response;
import io.reactiverse.pgclient.Row;
import javax.json.JsonObjectBuilder;
import io.reactiverse.pgclient.Tuple;
import com.rac021.jaxy.coby.io.Writer;
import javax.annotation.PostConstruct;
import io.reactiverse.pgclient.PgPool;
import com.rac021.jaxy.api.pojos.Query;
import io.reactiverse.pgclient.PgStream;
import io.reactiverse.pgclient.PgClient;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import io.reactiverse.pgclient.PgConnection;
import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.pgclient.PgTransaction;
import io.reactiverse.pgclient.PgPreparedQuery;
import com.rac021.jaxy.coby.checker.TokenManager;
import java.util.concurrent.atomic.AtomicInteger;
import com.rac021.jaxy.api.analyzer.SqlAnalyzer;
import org.eclipse.microprofile.metrics.MetricUnits;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry;
import com.rac021.jaxy.api.qualifiers.security.Policy;
import com.rac021.jaxy.api.qualifiers.security.Secured;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.metrics.annotation.Counted;
import com.rac021.jaxy.coby.service.configuration.CobyConfiguration;
import static com.rac021.jaxy.service.sensorthings.DataLoader.isReachable;

/**
 *
 * @author R.Yahiaoui
 */
@ServiceRegistry("sensorThings")
@Singleton
@Secured(policy = Policy.CustomSignOn )

// @Secured(policy = Policy.Public)
// @Cipher(cipherType = { CipherTypes.AES_128_CBC, CipherTypes.AES_256_CBC })

// @CircuitBreaker(requestVolumeThreshold = 2, failureRatio = 0.50, delay = 5000, successThreshold = 2 )
// @Bulkhead(value = 2, waitingTaskQueue = 100) // maximum 2 concurrent requests allowed
// @Retry(delay = 100 , maxDuration = 3500, jitter = 400, maxRetries = 1_000_000 )
public class ServiceSensorThings {

    @Inject
    CobyConfiguration configuration ;
   
 //   @PersistenceContext(unitName = Streamer.PU )
   // private EntityManager        entityManager                          ;
    
//    @Inject
//    @Format(AcceptType.TEMPLATE_PLAIN)
//    private IStreamer streamerOutputTemplate;
    @PostConstruct
    public void init() {
    }

    public ServiceSensorThings() {
    }

    @GET
    @OPTIONS
    @Produces({"json/plain", "json/encrypted"})
    @Fallback(fallbackMethod = "retryLaterMethod")
    @Counted(name = "ServiceSensorThings_template_plain_counter_total", absolute = true, reusable = true, /* monotonic = true, */ unit = MetricUnits.NONE)
    @Timed(name = "ServiceSensorThings_template_plain_timer", absolute = true, unit = MetricUnits.MILLISECONDS)
    public Response getResourceASTemplateEncrypted( @HeaderParam("sensorType")    String _sensorType  ,
                                                    @HeaderParam("sqlQuery")      String _sqlQuery    ,
                                                    @HeaderParam("templateQuery") String _template    ,
                                                    @HeaderParam("db_host")       String db_host      ,
                                                    @HeaderParam("db_port")       String db_port      ,
                                                    @HeaderParam("db_name")       String db_name      ,
                                                    @HeaderParam("db_user")       String db_user      ,
                                                    @HeaderParam("db_password")   String db_password  ,
                                                    @HeaderParam("sensorthings_endpoint_url") String _sensorthings_endpoint_url ,
                                                    @HeaderParam("API-key-Token") String  token ) throws Exception {
        
        System.out.println(" In ServiceSensorThings Service .... ");
        
        if( _sensorType == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "SensorType Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( _sqlQuery == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "Sql Query Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( _template == null || _template.equalsIgnoreCase("undefined")) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "Template Can't be Null. Select Template").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( db_host == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB Host Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( db_port == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB Port Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( db_name == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB NAME Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( db_user == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB USER Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( db_password == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB PASSWORD Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        if( _sensorthings_endpoint_url == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "SensorThings Endpoint Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
        String sqlQuery = URLDecoder.decode( _sqlQuery, StandardCharsets.UTF_8.toString()) ;
        
        String template = URLDecoder.decode( _template, StandardCharsets.UTF_8.toString())     ;
        String sensorType = URLDecoder.decode( _sensorType, StandardCharsets.UTF_8.toString()) ;
        
        String sensorthings_endpoint_url_dec =  URLDecoder.decode( _sensorthings_endpoint_url, StandardCharsets.UTF_8.toString()) ;

        String sensorthings_endpoint_url = (( sensorthings_endpoint_url_dec != null && sensorthings_endpoint_url_dec.endsWith("/") ) ?
                                             sensorthings_endpoint_url_dec + sensorType : sensorthings_endpoint_url_dec + "/" + sensorType ).trim()  ;
        
        System.out.println(" sqlQuery                      : " + sqlQuery  ) ;
        System.out.println(" template                      : " + template  ) ;
        System.out.println(" sensorType                    : " + sensorType) ;
        System.out.println(" sensorthings_endpoint_url_dec : " + sensorthings_endpoint_url_dec ) ;
                
        PgPoolOptions options = new PgPoolOptions()
                                     .setPort(Integer.parseInt(db_port))
                                     .setHost(db_host)
                                     .setDatabase(db_name)
                                     .setUser(db_user)
                                     .setPassword(db_password)
                                     .setMaxSize(1);

        CountDownLatch latch = new CountDownLatch(1 );
        
        JsonObjectBuilder respBuilder = Json.createObjectBuilder();
        
       // JsonObject respObj = Json.createObjectBuilder().build() ;
         
        // Create the client pool
        PgPool client = PgClient.pool(options ) ;

        String  login     = TokenManager.getLogin(token) ;
        String  path_logs = TokenManager.builPathLog( configuration.getLoggerFile(), login ) ;
        Writer.createFile(path_logs) ;
        List<String> exs = new ArrayList<>() ;
           
        String startedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss" ))  ;
        
        client.getConnection(ar1 -> {

            if (ar1.succeeded()) {

                PgConnection connection = ar1.result();

                connection.cancelRequest( (cx) -> { 
                   System.out.println(" Cancel The Current Request");
                }) ;
                  
                connection.prepare( sqlQuery , (res) -> {

                    if (res.succeeded()) {

                        if( ! isReachable(sensorthings_endpoint_url) ) {
                             respBuilder.add("Exception", "Url " + sensorthings_endpoint_url + " Not Reachable !! ") ;
                             latch.countDown() ;
                             return            ;
                        }
                        
                        exs.add("Sensor-map Start...    ID : " +  startedDate ) ;
                        Writer.writeTextFile(exs, path_logs)             ;
                        exs.clear();
                              
                        latch.countDown();
                         
                        AsyncResult<PgPreparedQuery> pq = res;

                        // Streams require to run within a transaction
                        PgTransaction tx = connection.begin();

                        // Fetch 2 rows at a time
                        PgStream<Row> stream = pq.result().createStream(2, Tuple.tuple());

                        // Use the stream
                        stream.exceptionHandler((err) -> {
                            System.out.println("Error: " + err.getMessage());
                            tx.close();
                        });

                        stream.endHandler( v -> {
                            tx.close();
                            exs.add("Sensor-map Finished... ID : " + startedDate + " => SUCCESS " )  ;
                            Writer.writeTextFile(exs, path_logs)          ;
                            exs.clear();
                            System.out.println("End of stream");
                        });

                        AtomicInteger count = new AtomicInteger(0);
                        final List<String> columnsNames = new ArrayList<>();
                        Map<String, String > columnsValues = new HashMap<>();
                         
                        //boolean error = false ;
                            
                        stream.handler( (row) -> {

                            if (count.getAndIncrement() == 0)      {
                                for (int i = 0; i < row.size(); i++)       {
                                    columnsNames.add(row.getColumnName(i)) ;
                                }
                            }

                            for (int colmnPos = 0; colmnPos < columnsNames.size(); colmnPos++) {

                                String colmnName = columnsNames.get(colmnPos);
                                String colmnValue = row.getValue(colmnName).toString();
                                
                                columnsValues.put( colmnName , colmnValue) ;
                            }
                            
                            String outData = applyValue( columnsValues, template ) ;
                           
                            StringWriter errors = new StringWriter()     ;
                            
                            List<String> data = new ArrayList<>();
                            data.add(outData) ;
                            
                            try {
                               
                                DataLoader.postDatas( sensorthings_endpoint_url ,
                                                      data              ,
                                                      "application/json",
                                                      500               ,
                                                      500               ,
                                                      500               ,
                                                      true            ) ;
                                
                            } catch (Exception ex) {
                                
                                 Logger.getLogger(ServiceSensorThings.class.getName()).log(Level.SEVERE, null, ex) ;
                                 
                                 respBuilder.add("Exception", ex.getMessage() ) ;
                                 
                                      //StringWriter errors = new StringWriter()     ;
                                      //List<String> exs = new ArrayList<>()         ;
                                      ex.printStackTrace( new PrintWriter(errors)) ;
                                      exs.add( "\nERROR : ID : " + startedDate +" [[ \n" + errors.toString()  + "\n]]\n")    ;
                                     // exs.add(ex.getMessage() )                  ;
                                      // System.out.println(" ###### Write ERROR in " + configuration.getLoggerFile() ) ;
                                     
                                      Writer.writeTextFile(exs, path_logs)         ;
                                      exs.clear()   ;
                                      //  error = true ;
                                      
                                      connection.close();
                                      stream.close();
                                
                                      throw new RuntimeException(ex) ;
                                
                            }
                        }) ;
                       
                    } else  if (res.failed()) {
                        System.out.println("ERR res ==> " +res.cause().getMessage());
                        respBuilder.add("Exception", res.cause().getMessage() );
                        latch.countDown();
                    }
                });

            } else if (ar1.failed()) {
                 System.out.println("ERR ar1 ==> " +ar1.cause().getMessage());
                 respBuilder.add("Exception", ar1.cause().getMessage() );
                 latch.countDown();
                // Return the connection to the pool
                client.close();
            }

        });
        
        latch.await() ;
        
        JsonObject respObj = respBuilder.build() ;
        
        if(respObj.isEmpty() ) {
           respObj = respBuilder.add("Status", "Processed").build() ;
        } 
        //JsonObject respObj = Json.createObjectBuilder().add("Status", "Processed").build();
        return Response.status(Response.Status.OK)
                       .entity(respObj.toString()).build();

    }

    public Response retryLaterMethod(@HeaderParam("sql") String sqlQuery, @HeaderParam("template") String template) {

        return Response.status(Response.Status.OK)
                       .entity(" -- Retry Later -- ")
                       .build();
    }
    
    @GET
    @OPTIONS
    @Path("/headers")
    @Produces({ "json/plain", "json/encrypted"})
    @Fallback(fallbackMethod = "retryLaterMethod")
    @Counted(name = "ServiceSensorThingsHeaders_template_plain_counter_total", absolute = true, reusable = true, /* monotonic = true, */ unit = MetricUnits.NONE)
    @Timed(name = "ServiceSensorThingsHeaders_template_plain_timer", absolute = true, unit = MetricUnits.MILLISECONDS)
    public Response getHeaders( @HeaderParam("sqlQuery")    String _sqlQuery   ,
                                @HeaderParam("db_host")     String db_host     ,
                                @HeaderParam("db_port")     String db_port     ,
                                @HeaderParam("db_name")     String db_name     ,
                                @HeaderParam("db_user")     String db_user     ,
                                @HeaderParam("db_password") String db_password ) throws Exception {
        
            
        String sqlQuery = URLDecoder.decode( _sqlQuery, StandardCharsets.UTF_8.toString()) ;

        if (  sqlQuery == null || sqlQuery.trim().replaceAll("(?m)^[ \t]*\r?\n", "").isEmpty() ) {
             JsonObject respObj = Json.createObjectBuilder().add("Error", "SQL Query Can't be Null").build();
           return Response.status(Response.Status.OK).entity(respObj.toString()).build();
        }
        
         Query buildQueryObject = null ;
         JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
         
         try (Connection connection = DriverManager.getConnection( "jdbc:postgresql://" + db_host +":" + db_port + "/"+ db_name ,
                                                                   db_user, db_password )) {
              connection.setReadOnly(true);
             
              try {
                    buildQueryObject = SqlAnalyzer.buildQueryObject(connection, URLDecoder.decode( _sqlQuery, StandardCharsets.UTF_8.toString()));
              } catch( Exception ex ) {
                   jsonBuilder.add("Exception",ex.getMessage()) ;
                  return Response.status(Response.Status.BAD_REQUEST).entity(jsonBuilder.build().toString()).build();
              }
        } catch( Exception ex ){
             ex.printStackTrace();
             jsonBuilder.add("Exception",ex.getMessage()) ;
             return Response.status(Response.Status.BAD_REQUEST).entity(jsonBuilder.build().toString()).build();
        }
      
       buildQueryObject.getParameters().forEach((columneName, columnType) -> {
           jsonBuilder.add(columneName, columnType.get("TYPE")) ;
       });
              
       JsonObject sqlObj = jsonBuilder.build();
           
       if( sqlObj.isEmpty())     {
           sqlObj.put("", null ) ;
       }
       
       return Response.status(Response.Status.OK).entity(sqlObj.toString()).build();

    }
    
    
    private static String applyValue( final Map<String, String> map  , 
                                     final String template          ) {
        
        if( map == null || template == null ) return template ;
        
        StringBuilder templateInstance = new StringBuilder(template) ;
        
        map.forEach(( k, v) -> {
            replaceAllPattern ( templateInstance                     , 
                                Pattern.compile("\\{\\{"+k+"\\}\\}") ,
                                v                                    ) ;
        }) ;
        
        return templateInstance.toString() ;
    }
    
     private static void replaceAllPattern( StringBuilder sb          , 
                                            Pattern       pattern     , 
                                            String        replacement ) {
        Matcher m = pattern.matcher(sb) ;
        int start = 0                   ;
        while (m.find(start)) {
            sb.replace(m.start(), m.end(), replacement) ;
            start = m.start() + replacement.length()    ;
        }
    }

     
      public Response retryLaterMethod( @HeaderParam("sensorType") String sensorType    ,
                                        @HeaderParam("sqlQuery")   String sqlQuery      ,
                                        @HeaderParam("templateQuery") String template   ,
                                        @HeaderParam("db_host")     String db_host      ,
                                        @HeaderParam("db_port")     String db_port      ,
                                        @HeaderParam("db_name")     String db_name      ,
                                        @HeaderParam("db_user")     String db_user      ,
                                        @HeaderParam("db_password") String db_password  ,
                                        @HeaderParam("sensorthings_endpoint_url") String _sensorthings_endpoint,
                                         @HeaderParam("API-key-Token") String  token ) {
          
          return null ;
      }
      
      public Response retryLaterMethod( @HeaderParam("sensorType") String sensorType    ,
                                        @HeaderParam("sqlQuery")   String sqlQuery      ,
                                        @HeaderParam("templateQuery") String template   ,
                                        @HeaderParam("db_host")     String db_host      ,
                                        @HeaderParam("db_port")     String db_port      ,
                                        @HeaderParam("db_name")     String db_name      ,
                                        @HeaderParam("db_user")     String db_user      ,
                                        @HeaderParam("db_password") String db_password  ,
                                        @HeaderParam("sensorthings_endpoint_url") String _sensorthings_endpoint) {
          
          return null ;
      }
      
      public Response retryLaterMethod( @HeaderParam("sqlQuery")   String sqlQuery     ,
                                         @HeaderParam("db_host")     String db_host     ,
                                         @HeaderParam("db_port")     String db_port     ,
                                         @HeaderParam("db_name")     String db_name     ,
                                         @HeaderParam("db_user")     String db_user     ,
                                         @HeaderParam("db_password") String db_password ) {
          return null ;
      }
       
}

