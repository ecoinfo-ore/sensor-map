
package com.rac021.sensormap.api.analyzer ;


import java.util.List ;
import java.util.Arrays ;
import java.util.ArrayList ;
import java.sql.Connection ;
import java.io.StringReader ;
import java.sql.SQLException ;
import java.util.logging.Level ;
import java.util.logging.Logger ;
import java.sql.ResultSetMetaData ;
import java.sql.PreparedStatement ;
import java.util.stream.Collectors ;
import net.sf.jsqlparser.schema.Column ;
import javax.ws.rs.core.MultivaluedMap ;
import com.rac021.sensormap.api.pojos.Query ;
import net.sf.jsqlparser.expression.Function ;
import net.sf.jsqlparser.JSQLParserException ;
import net.sf.jsqlparser.expression.Expression ;
import net.sf.jsqlparser.statement.select.Limit ;
import net.sf.jsqlparser.parser.CCJSqlParserUtil ;
import net.sf.jsqlparser.statement.select.Offset ;
import net.sf.jsqlparser.statement.select.Select ;
import net.sf.jsqlparser.parser.CCJSqlParserManager ;
import net.sf.jsqlparser.statement.select.SelectBody ;
import net.sf.jsqlparser.statement.select.SelectItem ;
import net.sf.jsqlparser.statement.select.AllColumns ;
import net.sf.jsqlparser.statement.select.PlainSelect ;
import net.sf.jsqlparser.statement.select.OrderByElement ;
import net.sf.jsqlparser.statement.select.SelectExpressionItem ;
import static com.rac021.sensormap.api.logger.LoggerFactory.getLogger ;

/**
 *
 * @author ryahiaoui
 */

public class SqlAnalyzer {
    
    private static final Logger LOGGER = getLogger() ; 
     
    public static Query buildQueryObject( Connection cnn, final String sqlQuery )     {
         
        LOGGER.log( Level.CONFIG , "  - Processing SQL Query "         ) ;
       
        String sub_sql = sqlQuery.length() > 60 ? 
                         sqlQuery.substring ( 0, 60 ) : sqlQuery         ;
           
        LOGGER.log( Level.CONFIG, "      - Extracted SQL Query : {0} ...", sub_sql)   ;
           
        try ( PreparedStatement ps = cnn.prepareStatement(sqlQuery) )   {

            CCJSqlParserManager parserManager = new CCJSqlParserManager()             ;
            Select select = (Select) parserManager.parse(new StringReader( sqlQuery)) ;
           
            SelectBody selectBody      = select.getSelectBody()         ;
            ResultSetMetaData metaData = ps.getMetaData()               ;
        
            int count = metaData.getColumnCount() ; //number of column
           
            Query query = new Query() ;
            query.setContainsAggregationFunction ( containsAggregationFuntion(sqlQuery)) ;
              
            List<String> orderColumnNames = new ArrayList()                              ;
               
            for (int i = 1; i <= count; i++) {
                  
                String columnName      = metaData.getColumnLabel(i)               ;
                String columnType      = metaData.getColumnClassName(i)           ;
                String fullNameColumn  = getFullNameSqlParamAt ( sqlQuery, i -1 ) ;
                  
                if(fullNameColumn == null ) fullNameColumn = columnName  ;
                
                query.register(columnName, fullNameColumn, columnType )  ;

                orderColumnNames.add(metaData.getColumnName(i) )         ;
            }
              
            ((PlainSelect) selectBody).setOrderByElements(Arrays.asList()) ;
            List orderElementsList = new ArrayList<>()                     ;

            orderColumnNames.stream()
                            .forEach(( String col) -> { 
                              try {
                                   Expression expr = CCJSqlParserUtil.parseCondExpression( col ) ;
                                   OrderByElement orderByElement =  new OrderByElement()         ;
                                   orderByElement.setExpression(expr)                            ;
                                   orderElementsList.add(orderByElement)                         ;
                               } catch (JSQLParserException ex )                                 {
                                   throw new RuntimeException(ex) ;
                               }
            }) ;
                     
            LOGGER.log( Level.CONFIG , "      - OrderElementsList : {0}", orderElementsList)  ;
             
            ((PlainSelect) selectBody).setOrderByElements(orderElementsList )                 ;

            query.setQuery(select.toString() )                                                ;
            
            LOGGER.log(Level.CONFIG , "      - SQL Object Query  : {0}", query )              ;
            
            LOGGER.log(Level.CONFIG , " ------------------------------- "  )                  ;

            
            return  query                                                                     ; 
              
          } catch( SQLException | JSQLParserException ex )    {
              throw new RuntimeException(ex) ;
          }
    }
   
    private static boolean containsAggregationFuntion( String query )             {
    
      try {
           CCJSqlParserManager parserManager = new CCJSqlParserManager()         ;
           Select select = (Select) parserManager.parse(new StringReader(query)) ;
           PlainSelect ps = (PlainSelect) select.getSelectBody() ;
              
           List<SelectItem> selectItems = ps.getSelectItems()    ;
               
           for( int i = 0; i < selectItems.size() ; i++)         {
                   
              if( ps.getSelectItems().get(i) instanceof AllColumns) {
                     return false ;
              }
              if ( ((SelectExpressionItem) ps.getSelectItems()
                                             .get(i)).getExpression() instanceof Function ) 
                    return true  ;
           }
           
       } catch (JSQLParserException ex)      {
              throw new RuntimeException(ex) ;
       }
           
       return false ;
    }
           
       
    private static String generateQueryWithFiledsFilters( Query query , List<String> fieldsFilters ) {
          
        if( query == null || fieldsFilters == null ) return null ;

        String joinedParams  = "( " + fieldsFilters.stream()
                                                   .collect( Collectors
                                                   .joining(" ) AND ( ") ) + " ) " ;
          
        try {
            
            CCJSqlParserManager parserManager = new CCJSqlParserManager() ;
            Select select  = (Select) parserManager.parse(new StringReader(query.getQuery())) ;
            PlainSelect ps = (PlainSelect) select.getSelectBody();
            
            if(joinedParams.replaceAll(" +", "").trim().equals("()")) {
                return select.toString() ;
            }
            
            String newQ ;

            if( query.isContainsAggregationFunction()) {
                
                Expression having = ps.getHaving()     ;

               if( having != null ) {
                 newQ = query.getQuery().replace( having.toString() , 
                                                 having.toString() + " AND " + joinedParams ) ;
               }
               else  {
                 String hav      =  joinedParams                             ;
                 Expression expr = CCJSqlParserUtil.parseCondExpression(hav) ;
                 ((PlainSelect) select.getSelectBody()).setHaving(expr)      ;
                 newQ =  select.toString()                                   ;
               }
           
            } else {
          
                Expression wher = ps.getWhere() ;

                if( wher != null ) {
                     newQ = query.getQuery().replace( wher.toString() , 
                                                      wher.toString() + " AND " + joinedParams ) ;
                }
                else  {       
                       try {
                            Expression expr = CCJSqlParserUtil.parseCondExpression(joinedParams) ;
                            ((PlainSelect) select.getSelectBody()).setWhere(expr) ; 
                            newQ = select.toString()      ;
                       } catch( JSQLParserException ex )  {
                           throw new RuntimeException(ex) ;
                       }
               }
           }
         
           return newQ ;
           
        } catch( JSQLParserException ex ) {
             throw  new RuntimeException(ex) ;
        }
    }

    private static String generateQueryApplyingFiledsFiltersIncludingLimitOffset ( final Query query   , 
                                                                                   List<String> fields ) {
            
        String queryApplyedFilters     = generateQueryWithFiledsFilters( query, fields )  ;
        String queryApplyedLimitOffset = appendLimitOffsetPaattern( queryApplyedFilters ) ;
          
        LOGGER.log(Level.CONFIG , " Query  --> {0}", query  ) ;
        LOGGER.log(Level.CONFIG , " Fields --> {0}", fields ) ;
        return /*collect */ queryApplyedLimitOffset           ;
    }

    public static Query getSqlParamsWithTypes( Connection cnn, String sqlQuery ) {
       
       Query query = new Query( sqlQuery ) ;
       
       try ( PreparedStatement ps = cnn.prepareStatement(sqlQuery) ) {
            
           ResultSetMetaData metaData = ps.getMetaData()             ;
        
           int count = metaData.getColumnCount() ; //number of column
             
           for ( int i = 1; i <= count; i++ ) {
                
               String columnName = metaData.getColumnLabel(i)                   ;
               String columnType = metaData.getColumnClassName(i)               ;
               String fullNameColumn = getFullNameSqlParamAt ( sqlQuery, i -1 ) ;
               if(fullNameColumn == null ) fullNameColumn = columnName          ;
               query.register(columnName, fullNameColumn, columnType)           ;
                  
           }
            
           ps.close() ;
            
       } catch( SQLException ex )       {
          throw new RuntimeException(ex) ;
       } 
       
      return query ;

    }
   
    public static String generateQueryAccordingFieldsFilters ( Query query , 
                                                               MultivaluedMap<String, String> filedsFilters ) {
        
        if(filedsFilters.isEmpty() ) return appendLimitOffsetPaattern(query.getQuery()) ;
        
        List<String> cleanedFilters = Lexer.cleanFieldsFilters( query, filedsFilters ) ;
        
        return SqlAnalyzer.generateQueryApplyingFiledsFiltersIncludingLimitOffset( query , 
                                                                                   cleanedFilters) ;
    }

    private static String getFullNameSqlParamAt ( String sql, int index ) {

       try {
            CCJSqlParserManager parserManager = new CCJSqlParserManager()        ;
            Select select  = (Select) parserManager.parse(new StringReader(sql)) ;
            PlainSelect ps = (PlainSelect) select.getSelectBody()                ;
            
            SelectItem get = ps.getSelectItems().get(0)                          ;
            
            if( get instanceof AllColumns) {
                return null                ;
            }
             
            Expression expression = (( SelectExpressionItem) 
                                     ps.getSelectItems().get(index))
                                                        .getExpression() ;
          
           if (expression instanceof Function) {
                 
               if( ((Function) ((SelectExpressionItem) ps.getSelectItems()
                                                         .get(index)).getExpression()) != null ) {
  
                   return ((Function) ((SelectExpressionItem) ps.getSelectItems()
                                                                .get(index)).getExpression()).toString() ;
               } 
           }
             
           return ((Column) ((SelectExpressionItem) ps.getSelectItems()
                                                      .get(index)).getExpression())
                                                      .getFullyQualifiedName() ;
       } catch( JSQLParserException ex )  {
           throw new RuntimeException(ex) ;
         }
    }

    private static String appendLimitOffsetPaattern(String query) {

        if( query == null) return null ;
          
        try  {
                CCJSqlParserManager parserManager = new CCJSqlParserManager()          ;
                Select select = (Select) parserManager.parse(new StringReader( query)) ;

                Limit limit   = ((PlainSelect) select.getSelectBody()).getLimit()      ;
                Offset offset = ((PlainSelect) select.getSelectBody()).getOffset()     ;

                String retQ = select.toString() ;
                retQ += offset == null ? " OFFSET ?1 " : " "                           ;
                retQ += limit  == null ? " LIMIT  ?2 " : " "                           ; 

                return retQ ;                   
            
        } catch( JSQLParserException ex )  {
            throw new RuntimeException(ex) ;
          }
    }

}
