package com.rac021.sensormap.impl.service.sensorthings ;

/**
 *
 * @author ryahiaoui
 */
import java.util.Map ;
import java.util.List ;
import javax.ws.rs.GET ;
import javax.json.Json ;
import javax.ws.rs.Path ;
import java.util.HashMap ;
import java.net.URLDecoder ;
import java.sql.Connection ;
import java.util.ArrayList ;
import javax.ws.rs.OPTIONS ;
import javax.ws.rs.Produces ;
import javax.json.JsonObject ;
import javax.inject.Singleton ;
import java.sql.DriverManager ;
import java.util.logging.Level ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;
import javax.ws.rs.HeaderParam ;
import java.time.LocalDateTime ;
import java.util.logging.Logger ;
import io.vertx.core.AsyncResult ;
import javax.ws.rs.core.Response ;
import io.quarkus.arc.Unremovable ;
import io.reactiverse.pgclient.Row ;
import javax.json.JsonObjectBuilder ;
import io.reactiverse.pgclient.Tuple ;
import javax.annotation.PostConstruct ;
import io.reactiverse.pgclient.PgPool ;
import io.quarkus.runtime.StartupEvent ;
import javax.enterprise.event.Observes ;
import io.reactiverse.pgclient.PgStream ;
import io.reactiverse.pgclient.PgClient ;
import java.nio.charset.StandardCharsets ;
import java.time.format.DateTimeFormatter ;
import com.rac021.sensormap.impl.io.Writer ;
import java.util.concurrent.CountDownLatch ;
import com.rac021.sensormap.api.pojos.Query ;
import io.reactiverse.pgclient.PgConnection ;
import io.reactiverse.pgclient.PgPoolOptions ;
import io.reactiverse.pgclient.PgTransaction ;
import io.reactiverse.pgclient.PgPreparedQuery ;
import com.rac021.sensormap.api.security.Policy ;
import com.rac021.sensormap.api.security.Secured ;
import java.util.concurrent.atomic.AtomicInteger ;
import com.rac021.sensormap.api.analyzer.SqlAnalyzer ;
import com.rac021.sensormap.api.qualifiers.ServiceRegistry ;
import com.rac021.sensormap.impl.utils.checker.TokenManager ;
import static com.rac021.sensormap.impl.service.sensorthings.DataLoader.isReachable ;

/**
 *
 * @author R.Yahiaoui
 */
@ServiceRegistry("sensorThings")
@Singleton
@Secured(policy = Policy.CustomSignOn )
@Unremovable

public class ServiceSensorThings              {

    private PgPoolOptions options             ;
   
    void onStart(@Observes StartupEvent ev) { }

    @PostConstruct
    public void init()  {
      System.out.println( " + ServiceSensorThings Instanciation : " + this ) ;
    }

    @GET
    @OPTIONS
    @Produces({"xml/plain","json/plain", "xml/encrypted","json/encrypted"} )
    public Response fromDbToSensoThings( @HeaderParam("sensorType")    String _sensorType  ,
                                         @HeaderParam("sqlQuery")      String _sqlQuery    ,
                                         @HeaderParam("templateQuery") String _template    ,
                                         @HeaderParam("db_host")       String db_host      ,
                                         @HeaderParam("db_port")       String db_port      ,
                                         @HeaderParam("db_name")       String db_name      ,
                                         @HeaderParam("db_user")       String db_user      ,
                                         @HeaderParam("db_password")   String db_password  ,
                                         @HeaderParam("sensorthings_endpoint_url") String _sensorthings_endpoint_url ,
                                         @HeaderParam("API-key-Token") String  token ) throws Exception              {
        
        System.out.println(" Call : ServiceSensorThings... ") ;
      
        if( _sensorType == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "SensorType Can't be Null").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                    ;
        }
        
        if( _sqlQuery == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "Sql Query Can't be Null").build()  ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                    ;
        }
        
        if( _template == null || _template.equalsIgnoreCase("undefined")) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "Template Can't be Null. Select Template").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                                   ;
        }
        
        if( db_host == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB Host Can't be Null").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                 ;
        }
        
        if( db_port == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB Port Can't be Null").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                 ;
        }
        
        if( db_name == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB NAME Can't be Null").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                 ;
        }
        
        if( db_user == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB USER Can't be Null").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                 ;
        }
        
        if( db_password == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "DB PASSWORD Can't be Null").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                     ;
        }
        
        if( _sensorthings_endpoint_url == null ) {
           JsonObject respObj = Json.createObjectBuilder().add("Error", "SensorThings Endpoint Can't be Null").build() ;
           return Response.status(Response.Status.OK).entity(respObj.toString()).build()                               ;
        }
        
        String sqlQuery                      = URLDecoder.decode( _sqlQuery, StandardCharsets.UTF_8.toString())   ;
       
        String sensorType                    = URLDecoder.decode( _sensorType, StandardCharsets.UTF_8.toString()) ;
        
        String sensorthings_endpoint_url_dec =  URLDecoder.decode( _sensorthings_endpoint_url, StandardCharsets.UTF_8.toString()) ;

        String sensorthings_endpoint_url     = (( sensorthings_endpoint_url_dec != null && sensorthings_endpoint_url_dec.endsWith("/") ) ?
                                                  sensorthings_endpoint_url_dec + sensorType                 :
                                                  sensorthings_endpoint_url_dec + "/" + sensorType ).trim()  ;
        
        String template                      = removeDoubleQuotesIfNumber( URLDecoder.decode( _template, StandardCharsets.UTF_8.toString()) ) ;
        
        System.out.println("                                             " ) ;
        System.out.println(" sqlQuery                      : " + sqlQuery  ) ;
        System.out.println(" template                      : " + template  ) ;
        System.out.println(" sensorType                    : " + sensorType) ;
        System.out.println(" sensorthings_endpoint_url_dec : " + sensorthings_endpoint_url_dec ) ;
        System.out.println("                                             " ) ;
                
        PgPoolOptions option = getOptions( db_port, db_host, db_name, db_user, db_password )     ;

        CountDownLatch latch = new CountDownLatch(1 )              ;
        
        JsonObjectBuilder respBuilder = Json.createObjectBuilder() ;
         
        // Create the client pool
        PgPool client = PgClient.pool(option )           ;

        String  login     = TokenManager.getLogin(token) ;
        String  path_logs = TokenManager.builPathLog( "logger" /*configuration.getLoggerFile()*/, login ) ;
        Writer.createFile(path_logs)         ;
        List<String> exs = new ArrayList<>() ;
           
        String startedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss" ))  ;
        
        client.getConnection(ar1 -> {

            if (ar1.succeeded())    {

                PgConnection connection = ar1.result()  ;

                connection.cancelRequest(  (cx) ->      { 
                   System.out.println(" Cancel The Current Request") ;
                }) ;
                  
                connection.prepare( sqlQuery , (res) -> {

                    if (res.succeeded()) {

                        if( ! isReachable(sensorthings_endpoint_url) ) {
                             respBuilder.add("Exception", "Url " + sensorthings_endpoint_url + " Not Reachable !! ") ;
                             
                             latch.countDown() ;
                             
                             exs.add("Exception : Url " + sensorthings_endpoint_url + " Not Reachable !! ") ;
                             Writer.writeTextFile(exs, path_logs)                                           ;
                             exs.clear()                                                                    ;
                        
                             client.close() ;                             
                             return         ;
                        }
                        
                        exs.add("Sensor-map Start...    ID : " +  startedDate ) ;
                        Writer.writeTextFile(exs, path_logs)                    ;
                        exs.clear()                                             ;
                              
                        latch.countDown()                                       ;
                         
                        AsyncResult<PgPreparedQuery> pq = res ;

                        // Streams require to run within a transaction
                        PgTransaction tx = connection.begin() ;

                        // Fetch 10000 rows at a time
                        PgStream<Row> stream = pq.result().createStream( 10000 , Tuple.tuple() ) ;

                        // Use the stream
                        stream.exceptionHandler((err) -> {
                            System.out.println("Error: " + err.getMessage()) ;
                            tx.close()                                       ;
                        });

                        stream.endHandler( v -> {
                            tx.close()                                                              ;
                            exs.add("Sensor-map Finished... ID : " + startedDate + " => SUCCESS " ) ;
                            Writer.writeTextFile(exs, path_logs)                                    ;
                            exs.clear()                                                             ;
                            System.out.println("End of stream")                                     ;
                        });

                        AtomicInteger count = new AtomicInteger(0)           ;
                        final List<String> columnsNames = new ArrayList<>()  ;
                        Map<String, String > columnsValues = new HashMap<>() ;
                            
                        stream.handler( (row) -> {

                            if (count.getAndIncrement() == 0)              {
                                for (int i = 0; i < row.size(); i++)       {
                                    columnsNames.add(row.getColumnName(i)) ;
                                }
                            }

                            for (int colmnPos = 0; colmnPos < columnsNames.size(); colmnPos++) {

                                String colmnName = columnsNames.get(colmnPos)          ;
                                String colmnValue = row.getValue(colmnName).toString() ;
                                
                                columnsValues.put( colmnName , colmnValue)             ;
                            }
                            
                            String outData = applyValue( columnsValues, template )     ;
                           
                            // StringWriter errors = new StringWriter()                ;
                            
                            List<String> data = new ArrayList<>()                      ;
                            data.add(outData)                                          ;
                            
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
                                 
                                 respBuilder.add( "Exception", ex.getMessage() ) ;
                                 
                                 // ex.printStackTrace( new PrintWriter(errors)) ;
                                
                                 exs.add( "\nERROR : ID : "   +
                                          startedDate         +
                                          " [[ \n\n "         +
                                         ex.getMessage()      +
                                         // errors.toString() +
                                         "\n ]]\n")           ;
                                 
                                 Writer.writeTextFile(exs, path_logs)            ;
                                 exs.clear()                                     ;
                                 
                                 connection.close()                              ;
                                 stream.close()                                  ;
                                
                                 throw new RuntimeException(ex)                  ;
                                
                            }
                        }) ;
                       
                    } else  if (res.failed()) {
                        System.out.println("ERR res : " +res.cause().getMessage() ) ;
                        respBuilder.add( "Exception" , res.cause().getMessage()   ) ;
                        latch.countDown()                                           ;
                    }
                });

            } else if (ar1.failed()) {
                 System.out.println("ERR Ar1 : " + ar1.cause().getMessage()) ;
                 respBuilder.add("Exception" , ar1.cause().getMessage() )    ;
                 latch.countDown()                                           ;
                 client.close()                                              ; 
            }

        });
        
        latch.await()                                               ;
        
        JsonObject respObj = respBuilder.build()                    ;
        
        if(respObj.isEmpty() ) {
           respObj = respBuilder.add("Status", "Processed").build() ;
        } 
      
        return Response.status(Response.Status.OK)
                       .entity(respObj.toString())
                       .build() ;

    }
  
    private PgPoolOptions getOptions( String db_port   , 
                                      String db_host   , 
                                      String db_name   , 
                                      String db_user   , 
                                      String db_password ) throws NumberFormatException {
       
        if( options == null ) {
            
           options = new PgPoolOptions().setPort(Integer.parseInt(db_port))
                                        .setHost(db_host)
                                        .setDatabase(db_name)
                                        .setUser(db_user)
                                        .setPassword(db_password)
                                        .setMaxSize(1)  ;
           return options ;   
        }
        
        return options    ;
    }

    @GET
    @OPTIONS
    @Path("/headers")
    @Produces( { "xml/plaon", "json/plain", "xml/encrypted","json/encrypted" } )
    public Response getHeaders( @HeaderParam("sqlQuery")    String _sqlQuery   ,
                                @HeaderParam("db_host")     String db_host     ,
                                @HeaderParam("db_port")     String db_port     ,
                                @HeaderParam("db_name")     String db_name     ,
                                @HeaderParam("db_user")     String db_user     ,
                                @HeaderParam("db_password") String db_password ) throws Exception {
        
            
        String sqlQuery = URLDecoder.decode( _sqlQuery, StandardCharsets.UTF_8.toString()) ;

        if (  sqlQuery == null || sqlQuery.trim().replaceAll("(?m)^[ \t]*\r?\n", "").isEmpty() ) {
              JsonObject respObj = Json.createObjectBuilder().add("Error", "SQL Query Can't be Null").build() ;
              return Response.status(Response.Status.OK).entity(respObj.toString()).build()                   ;
        }
        
         Query buildQueryObject = null                              ;
         JsonObjectBuilder jsonBuilder = Json.createObjectBuilder() ;
         
         try ( Connection connection = DriverManager.getConnection( "jdbc:postgresql://" + db_host +":" + db_port + "/" + db_name ,
                                                                    db_user, db_password )) {
              connection.setReadOnly(true) ;
             
              try {
                    buildQueryObject = SqlAnalyzer.buildQueryObject( connection, 
                                                                     URLDecoder.decode( _sqlQuery, StandardCharsets.UTF_8.toString()) ) ;
              } catch( Exception ex )   {
                   ex.printStackTrace() ;
                   jsonBuilder.add("Exception",ex.getMessage()) ;
                   return Response.status(Response.Status.BAD_REQUEST).entity(jsonBuilder.build().toString()).build() ;
              }
        } catch( Exception ex )   {
             ex.printStackTrace() ;
             jsonBuilder.add("Exception",ex.getMessage()) ;
             return Response.status(Response.Status.BAD_REQUEST).entity(jsonBuilder.build().toString()).build() ;
        }
      
       buildQueryObject.getParameters().forEach((columneName, columnType) -> {
           jsonBuilder.add(columneName, columnType.get("TYPE"))              ;
       } ) ;
              
       JsonObject sqlObj = jsonBuilder.build() ;
           
       if( sqlObj.isEmpty() )    {
           sqlObj.put("", null ) ;
       }
       
       return Response.status(Response.Status.OK).entity(sqlObj.toString()).build() ;

    }
    
    private static String applyValue( final Map<String, String> map , 
                                     final String template          ) {
        
        if( map == null || template == null ) return template ;
        
        StringBuilder templateInstance = new StringBuilder(template) ;
        
        map.forEach(( k, v) -> {
            replaceAllPattern ( templateInstance                     , 
                                Pattern.compile("\\{\\{"+k+"\\}\\}") ,
                                v                                    ) ;
        }) ;
        
        return templateInstance.toString()                             ;
    }
    
     private static void replaceAllPattern( StringBuilder sb           , 
                                            Pattern       pattern      , 
                                            String        replacement  ) {
        Matcher m = pattern.matcher(sb) ;
        int start = 0                   ;
        while (m.find(start))  {
            sb.replace(m.start() , m.end(), replacement) ;
            start = m.start()    + replacement.length()  ;
        }
    }
    
    public static String removeDoubleQuotesIfNumber(String argTemplate ) {
        
        String template = argTemplate                                                    ;
            
        Pattern pattern = Pattern.compile( "\"\\{\\{.*?\\}\\}\\^Num\"", Pattern.DOTALL ) ;
        Matcher matcher = pattern.matcher( template )                                    ;
        
        while (matcher.find())  {
            String oldNumberVal = matcher.group( 0 )
                                         .replace("\"{{", "{{")
                                         .replace("}}^Num\"", "}}")     ;
            template = template.replace(matcher.group(0), oldNumberVal) ;
        }
          
        return template ;
    }
    
}

