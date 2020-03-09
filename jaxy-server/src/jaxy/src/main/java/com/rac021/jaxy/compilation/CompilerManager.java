
package com.rac021.jaxy.compilation ; 

import java.net.URL ;
import java.util.List ;
import java.util.Arrays ;
import java.sql.Connection ;
import java.util.ArrayList ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import com.rac021.jaxy.api.pojos.Query ;
import net.openhft.compiler.CompilerUtils ;
import com.rac021.jaxy.api.analyzer.SqlAnalyzer ;
import static com.rac021.jaxy.io.Reader.readFile ;
import static com.rac021.jaxy.messages.Displayer.message ;
import static com.rac021.jaxy.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */
public class CompilerManager {
    
   private static final String DTO_CLASS_NAME             = "Dto"                               ;
   private static final String SERVICE_PREFIX_CLASS_NAME  = "Service_"                          ;
   private static final String RESOURCE_CLASS_NAME        = "Resource"                          ;
   private static final String PACKAGE_NAME               = "com.rac021.jaxy.ghosts.services."  ;

    
   private static final Logger LOGGER = getLogger() ;
   
    
    private static String buildStringDtoClassFor( String packageName , 
                                                  String className   , 
                                                  String dtoTemplate , 
                                                  String sqlQuery    , 
                                                  Connection cnn     ) throws Exception {

        Query query = SqlAnalyzer.getSqlParamsWithTypes( cnn, sqlQuery) ;

        String dtoTemplate_1 = dtoTemplate.replace("{PACKAGE_NAME}", packageName )
                                          .replace("{CLASS_NAME}", className) 
                                          .replace("{TYPE}", packageName + ". "  + 
                                                             className )         ;
        int index = 0 ;

        for ( String column : query.getParameters().keySet() ) {

            dtoTemplate_1 = dtoTemplate_1.replace( "{SQL_FIELD}", "@ResultColumn(index=" +
                            index++ + ") " + " private " + query.getType(column)         +
                            " " + column + " ;" + "\n\n    {SQL_FIELD} " )               ;

            dtoTemplate_1 = dtoTemplate_1.replace( "{SQL_GETTER}" ,
                                                   generateGetter( query.getType(column), column)  + 
                                                   "\n    {SQL_GETTER} " )                         ;

            dtoTemplate_1 = dtoTemplate_1.replace( "{SQL_SETTER}" ,
                                                   generateSetter( query.getType(column), column ) +
                                                   "\n    {SQL_SETTER} " )                         ;
        }

        return dtoTemplate_1.replace("{SQL_FIELD}"  , "")
                            .replace("{SQL_GETTER}" , "")
                            .replace("{SQL_SETTER}" , "") ;

    }

    private static String buildStringResourceClassFor( String packageName     ,  
                                                       String className       ,
                                                       String contentResource ,
                                                       List<String> sqls   )  {

        String resourceTemplate_1 = contentResource.replace("{PACKAGE_NAME}" , packageName)
                                                   .replace( "{CLASS_NAME}"  , className)
                                                   .replace("{RESOURCE_CODE}", 
                                                             packageName + "." + className ) ;

        for (int i = 0; i < sqls.size(); i++) {

            String sql = sqls.get(i).replaceAll("\n"  , " ")
                                    .replace("\t"     , " ")
                                    .replaceAll("\t+" , " ")
                                    .replaceAll(" +"  , " ") ;

            resourceTemplate_1 = resourceTemplate_1.replace( "{SQL_QUERY}",
                                                             "@SqlQuery(\" Query_"     +
                                                             String.valueOf(i)         +
                                                             "\") "                    +
                                                             " private  String QUERY_" +
                                                             String.valueOf(i)         +
                                                              " = \"" + sql + "\" ; "  +
                                                             "\n\n    {SQL_QUERY} ")   ;
        }

        return resourceTemplate_1.replace("{SQL_QUERY}", "") ;

    }

    private static String buildStringServiceClassFor( String serviceCode          ,  
                                                      String serviceCodeSnakeName ,
                                                      String packageName          ,
                                                      String className            , 
                                                      String contentService       , 
                                                      Query query                 , 
                                                      String resourceName         , 
                                                      String dtoClass             ,
                                                      String security             ,
                                                      String ciphers            ) {

        String resourceTemplate_1 = contentService.replace("{PACKAGE_NAME}", packageName)
                                                  .replace("{SERVICE_NAME}", className)
                                                  .replace("{SERVICE_CODE}", serviceCode)
                                                  .replace("{SERVICE_CODE_SNAKE_NAME}", serviceCodeSnakeName)
                                                  .replace("{RESOURCE_CODE}", packageName + "." + resourceName)
                                                  .replace("{RESOURCE_NAME}", resourceName )
                                                  .replace("{DTO_CLASS}", dtoClass).replace("{CIPHERS}", ciphers ) ;

        if (security == null || security.equalsIgnoreCase("public"))    {
            resourceTemplate_1 = resourceTemplate_1.replace("{SECURITY}", "@Public")                                  ;
        } else if (security.equalsIgnoreCase("CustomSignOn"))           {
            resourceTemplate_1 = resourceTemplate_1.replace("{SECURITY}", "@Secured( policy = Policy.CustomSignOn )") ;
        } else if (security.equalsIgnoreCase("SSO"))                    {
            resourceTemplate_1 = resourceTemplate_1.replace("{SECURITY}", "@Secured( policy = Policy.SSO )")          ;
        } else                                                          {
            throw new IllegalArgumentException(" Unknown Authentication Mode for [ " + security + " ] ")              ;
        }

        List<String> params = new ArrayList()  ;

        for (String column : query.getParameters().keySet()) {
            params.add( "@QueryParam(\"" + column + "\") " + "java.lang.String" + " " + column ) ;
        }

        return resourceTemplate_1.replace(" {SQL_PARAMS}", String.join(" , ", params))           ;
    }

    private static String generateGetter(String type, String variable)      {

        return " public " + type + " get" + capitalizeFirstLetter(variable) +
               "() { \n " + "    return " + variable + " ; \n" + "     } "  ;
    }

    private static String capitalizeFirstLetter(String value)           {
        return value.substring(0, 1).toUpperCase() + value.substring(1) ;
    }

    private static String generateSetter(String type, String variable) {

        return " public void set" + capitalizeFirstLetter(variable) + "( "  +
               type + " " + variable + " ) { \n " + "    this. " + variable +
               " = " + variable + " ;\n" + "     } "                        ;
    }
        
    private static Class<?> compile( String className, String contentClass )  {
        
       try {
            
           return CompilerUtils.CACHED_COMPILER.loadFromJava( className    ,
                                                              contentClass )  ; 
        } catch( ClassNotFoundException ex ) {
            throw new RuntimeException(ex)   ;
        }
    }
    
    public static Class<?> compileDto ( String serviceCode      ,
                                        ClassLoader classLoader , 
                                        Connection cnn          ,
                                        String sql              ) throws Exception {
        
       URL    resourceDto            = classLoader.getResource("templates/Dto") ;
       String contentTemplateDto     = readFile(resourceDto)                    ;
       String stringInstanceDtoClass = buildStringDtoClassFor( PACKAGE_NAME   + serviceCode   ,  
                                                               DTO_CLASS_NAME                 ,
                                                               contentTemplateDto, sql, cnn ) ;
       
       LOGGER.log( Level.INFO, message("compiling_dto") ,
                   new Object[] { PACKAGE_NAME  , DTO_CLASS_NAME, serviceCode } ) ;

       Class<?> dto = compile( PACKAGE_NAME   + serviceCode + "." + DTO_CLASS_NAME ,
                               stringInstanceDtoClass                              ) ;
        LOGGER.log(Level.INFO, message("new_line"))                                  ;

       return dto ;

    }
    
    public static Class<?> compileResource ( String serviceCode      , 
                                             ClassLoader classLoader , 
                                             String sql              ) throws Exception {
        
          URL resourceResource   = classLoader.getResource("templates/Resource") ;
          String contentTemplateResource = readFile(resourceResource)            ;

          String stringInstanceResourceClass =
                  
                  buildStringResourceClassFor( PACKAGE_NAME  + serviceCode, RESOURCE_CLASS_NAME   ,
                                               contentTemplateResource, Arrays.asList(sql) )      ;
             
           LOGGER.log( Level.INFO,message("compiling_resource"),
                       new Object[] { PACKAGE_NAME  , serviceCode, RESOURCE_CLASS_NAME  } )       ;
             
           Class<?> _resource = compile ( PACKAGE_NAME  + serviceCode + "." + RESOURCE_CLASS_NAME , 
                                          stringInstanceResourceClass                           ) ;
           
           LOGGER.log(Level.INFO, message("new_line")) ;
           
           return _resource                            ;

    }
    
    public static Class<?> compileService( String serviceCode          , 
                                           String serviceCodeSnakeName ,
                                           ClassLoader classLoader     , 
                                           Query query                 , 
                                           String contentTeplateService, 
                                           String security             ,
                                           String cipherString   ) throws Exception {
        
           String serviceClassName  = SERVICE_PREFIX_CLASS_NAME +
                                      CompilerManager.capitalizeFirstLetter(serviceCode ) ;
         
           String stringInstanceServiceClass = buildStringServiceClassFor ( serviceCode                  , 
                                                                            serviceCodeSnakeName         ,
                                                                            PACKAGE_NAME   + serviceCode ,
                                                                            serviceClassName             , 
                                                                            contentTeplateService, query , 
                                                                            RESOURCE_CLASS_NAME          ,
                                                                            DTO_CLASS_NAME + ".class"    ,
                                                                            security                     ,
                                                                            cipherString               ) ;

             LOGGER.log( Level.INFO                   , 
                         message("compiling_service") ,
                         new Object[] { PACKAGE_NAME  , serviceCode, serviceClassName } ) ;
                     
            Class<?> serviceClazz = compile ( PACKAGE_NAME   + serviceCode + "." + serviceClassName ,
                                              stringInstanceServiceClass                         ) ;
            
            LOGGER.log(Level.INFO, message("new_line")) ;
            
            return serviceClazz                         ;
    }

    public static void addClassPath( String path ) {
       CompilerUtils.addClassPath( path )          ;
                        
    }
}

