
package com.rac021.ui.beans ;

import java.net.URL ;
import java.util.Map ;
import java.util.List ;
import java.util.Arrays ;
import java.util.HashMap ;
import javax.inject.Named ;
import java.io.IOException ;
import java.util.ArrayList ;
import java.io.InputStream ;
import javax.inject.Inject ;
import java.io.OutputStream ;
import java.io.Serializable ;
import java.util.stream.Collectors ;
import javax.annotation.PostConstruct ;
import javax.faces.context.FacesContext ;
import javax.faces.context.ExternalContext ;
import javax.validation.constraints.NotNull ;
import java.io.UnsupportedEncodingException ;
import javax.faces.application.FacesMessage ;
import javax.enterprise.context.SessionScoped ;
import com.rac021.jaxy.api.root.ServicesManager ;
import org.apache.commons.text.StringEscapeUtils ;

/**
 *
 * @author ryahiaoui
 */

@SessionScoped
@Named("serviceDetails")

public class ServiceDetails implements Serializable            {
    
    @Inject
    transient ServiceSelector serviceSelector                  ;
    
    @Inject
    transient ServicesManager servicesManager                  ;
    
    @Inject
    transient GlobalConf      globalConf                       ;
     
    private   String          selectedExpression               ;

    private   boolean         disabledChangeFilerButtom = true ;
    
    @PostConstruct
    public void init() {
    }
    
    private final Map<String, List<Expression>> expressionsMap = new HashMap<>();

    List<String> expressions = Arrays.asList( "=", "&gt;", "&ge;", "&lt;", "&le;", "not" ) ;

    private String cipherItem      = null ;

    private String selectedFilter  = null ;

    private String selectedValue   = null ;

    private String acceptItem      = null ;
    
    private String keep            = ""   ;

    private String script          = null ;

    private String scriptDecryptor = null ;

    private String clientConf      = null ;
     
    
     public String getSelectedValue()     {
        return selectedValue ;
    }

    public void setSelectedValue(@NotNull String value) {
        this.selectedValue = value ;
    }
    
    public List setChainedExpresion() {

        expressions = Arrays.asList( " AND " + selectedFilter + " = "     ,
                                     " AND " + selectedFilter + " &gt; "  ,
                                     " AND " + selectedFilter + " &gt;= " , 
                                     " AND " + selectedFilter + " &lt; "  ,
                                     " AND " + selectedFilter + " &lt;= " ,
                                     " AND " + selectedFilter + " not "   , 
                                     " OR "  + selectedFilter + " = "     ,
                                     " OR "  + selectedFilter + " &gt; "  ,
                                     " OR "  + selectedFilter + " &gt;= " ,
                                     " OR "  + selectedFilter + " &lt; "  ,
                                     " OR "  + selectedFilter + " &lt;= " ,
                                     " OR "  + selectedFilter + " not " ) ;
        return expressions       ;
    }

    public List resetExpresion()             {

        expressions = Arrays.asList( "="     , 
                                     "&gt;"  , 
                                     "&gt;=" , 
                                     "&lt;"  ,
                                     "&le;"  ,
                                     "not" ) ;
        
        return expressions                   ;
    }

    public String getSelectedFilter()        {
        return selectedFilter                ;
    }

    public void setSelectedFilter(String filter) {
        this.selectedFilter = filter        ;
    }
    
    public void updateFilters()                  {
      
      if ( expressionsMap.keySet()
                         .contains (
                           selectedFilter))      {
             setChainedExpresion() ;
      }
         
    }
    
    public String getCipherItem()                {
        return cipherItem    ;
    }
      
    public List<String> getExpressions()         {
        return expressions   ;
    }
       
    public void isEmptyListExpression()          {
        selectedValue = null ;
    }
        
    public void setCipherItem(String cipherItem) {
        this.cipherItem = cipherItem ;
    }
     
    public String getAcceptItem() {
        return acceptItem ;
    }

    public void setAcceptItem(String acceptItem) {
        this.acceptItem = acceptItem ;
    }
    
    public void updateCipher()                   {

        if ( acceptItem != null && 
            ! acceptItem.contains("crypt"))      {
            cipherItem = null    ;
        } 
        
        else {
            
            if ( serviceSelector.getSelectedServiceDescription() != null &&
                 serviceSelector.getSelectedServiceDescription().getCiphers().size() > 0 ) {
        
                 cipherItem = serviceSelector.getSelectedServiceDescription()
                                             .getCiphers().iterator().next() ;
            }
        }
        if (acceptItem != null && acceptItem.contains("template")) {
            keep = "" ;
        }

    }
     
    public void updateTemplate() throws UnsupportedEncodingException {
        this.script = genScript() ;
    }
      
    public boolean disableFilterSelection() {

       return expressionsMap.containsKey( this.selectedFilter) ||
                                          this.serviceSelector.getSelectedServiceDescription()
                                                              .getParams().keySet().isEmpty() ;
    }

    public boolean disableSelectionForExpressionAnsValueAndBottom() {
        return serviceSelector.getSelectedServiceDescription()
                              .getParams().keySet().isEmpty()       ;
    }

    public Integer getSelectedServiceTotalLinesInScript() throws UnsupportedEncodingException {
        if (this.script == null)      {
            this.script = genScript() ;
        }
        return this.script == null ? 2 : this.script.split("\r\n|\r|\n").length + 2 ;
    }

    public Integer getSelectedServiceTotalLinesInScriptDecryptor() throws UnsupportedEncodingException {
        
        if (this.scriptDecryptor == null || this.scriptDecryptor.isEmpty() ) {
            this.scriptDecryptor = genScriptDecryptor() ;
        }
        return this.scriptDecryptor == null ? 2 : this.scriptDecryptor.split("\r\n|\r|\n").length + 2 ;
    }
    
    public Integer getClientConfTotalLines() throws UnsupportedEncodingException {

        if (this.clientConf == null || this.clientConf.isEmpty() ) {
            this.clientConf = genClientConfig() ;
        }
        return this.clientConf == null ? 2 : this.clientConf.split("\r\n|\r|\n").length + 2 ;
    }

    public String getKeep() {
        return keep ;
    }

    public void setKeep(String keep) throws UnsupportedEncodingException {
        this.keep = keep ;
        genScript()      ;
    }

    public void displayMessageCopy() {

        FacesMessage msg = new FacesMessage( FacesMessage.SEVERITY_INFO  ,
                                             "Script"                    ,
                                             " Script Copied to ClipBoard" ) ;

        FacesContext context = FacesContext.getCurrentInstance() ;
        context.addMessage("growlMsgCpy", msg)                   ;

    }

    public String enabledSecurity() {
        return servicesManager.getSecurityLevel().name() ;
    }
    
    
    public String getScript() throws UnsupportedEncodingException {

        this.script = genScript() ;
        return this.script        ;
    }

    public void setScript(String script) {
        this.script = script ;
    }

    private String genScript() throws UnsupportedEncodingException {

        if ( serviceSelector.getSelectedServiceDescription()
                            .getSecurity().toLowerCase().contains("custom") ) {

            return ScriptGenerator.generateScriptCUSTOM( serviceSelector.getSelectedServiceUriTemplate()               ,
                                                         buildFilters()                                                ,
                                                         keep.replaceAll(",", " - ")                                   ,
                                                         acceptItem, globalConf.getLoginSignature()                    ,
                                                         globalConf.getPasswordSignature()                             ,
                                                         globalConf.getTimeStampSignature()                            ,
                                                         globalConf.getAlgoSigneture()                                 ,
                                                         cipherItem                                                    ,
                                                         serviceSelector.getSelectedServiceDescription().getSecurity() ,
                                                         getSignatureTemplate_Simple() )                               ;
        }

        if ( serviceSelector.getSelectedServiceDescription().getSecurity().toLowerCase().contains("sso")) {

            return ScriptGenerator.generateScriptSSO( serviceSelector.getSelectedServiceUriTemplate()               , 
                                                      buildFilters()                                                , 
                                                      keep.replaceAll(",", " - ")                                   ,
                                                      acceptItem, globalConf.getLoginSignature()                    ,
                                                      globalConf.getPasswordSignature()                             ,
                                                      globalConf.getTimeStampSignature()                            ,
                                                      globalConf.getAlgoSigneture()                                 ,
                                                      cipherItem                                                    , 
                                                      serviceSelector.getSelectedServiceDescription().getSecurity() ,
                                                      globalConf. getKeyCloakUrl() ) ;
        }

        /** Public Service . */

        return ScriptGenerator.generateScriptPUBLIC( serviceSelector.getSelectedServiceUriTemplate() ,
                                                     buildFilters(), keep.replaceAll(",", " - ")     ,
                                                     acceptItem  )                                   ;
    }
     
    public String genScriptDecryptor() throws UnsupportedEncodingException {

        return ScriptGenerator.generateScriptDecryptor( serviceSelector.getSelectedServiceUriTemplate()               ,
                                                        "login"                                                       ,
                                                        "password"                                                    ,
                                                        buildFilters()                                                ,
                                                        keep.replaceAll(",", " - ")                                   ,
                                                        acceptItem, globalConf.getLoginSignature()                    ,
                                                        globalConf.getPasswordSignature()                             ,
                                                        globalConf.getTimeStampSignature()                            , 
                                                        globalConf.getAlgoSigneture()                                 ,
                                                        cipherItem                                                    ,
                                                        serviceSelector.getSelectedServiceDescription().getSecurity() ) ;
    }
    
    public String genClientConfig() throws UnsupportedEncodingException {

        return ScriptGenerator.generateClientConfig( serviceSelector.getSelectedServiceUriTemplate()               ,
                                                     globalConf. getKeyCloakUrl()                                  ,
                                                     buildFilters()                                                ,
                                                     keep.replaceAll(",", " - ")                                   ,
                                                     acceptItem                                                    , 
                                                     serviceSelector.getSelectedServiceDescription().getSecurity() ,
                                                     globalConf.getAlgoSigneture()                                 ,
                                                     globalConf.getLoginSignature()                                ,
                                                     globalConf.getPasswordSignature()                             ,
                                                     globalConf.getTimeStampSignature()                            ,
                                                     getSignatureTemplate_Simple()                                 ,
                                                     cipherItem                                                  ) ;
    }
 
    public String getSelectedExpression() {
        return selectedExpression ;
    }

    public void setSelectedExpression(String selectedExpression) {
        this.selectedExpression = selectedExpression ;
    }
    
 
      public void updateRequirementValidation() {
        selectedFilter     = null ;
        selectedExpression = null ;
        selectedValue      = null ;
        resetExpresion()          ;
    }

    public void undoLastFilter()  {
        
        if (!this.expressionsMap.isEmpty())                          {
            this.expressionsMap.clear()                              ;
            updateRequirementValidation()                            ;
            FacesContext context = FacesContext.getCurrentInstance() ;
            context.addMessage( null ,
                                new FacesMessage( FacesMessage.SEVERITY_WARN , 
                                                  "", 
                                                  " Filter Cleaned ") ) ;
        }
    }

    private String toExpressString(String filter, List<Expression> _expressions) {

        StringBuilder expr = new StringBuilder("")           ;

        _expressions.forEach((Expression e) ->               {

            String expression = e.getExpression()            ;
            String value = e.getValue().contains(" ") ? "'"  +
                           e.getValue() + "'" : e.getValue() ;
            String _filter = ""                              ;

            expression = StringEscapeUtils.unescapeHtml4(expression) ;

            String  operande  = "" ;
            String comparator = "" ;

            if (expression.trim().startsWith("AND") || expression.trim().startsWith("OR")) {

                operande   = "_" + expression.trim().split(" ")[0].trim(); // OR - AND
                comparator = "_" + expression.trim().split(" ")[1].trim() + "_"; // = > >= <
            } else if (   !expr.toString().isEmpty()           &&
                        ( !expression.trim().startsWith("AND") &&
                          !expression.trim().startsWith("OR")) ) {
                operande   = "&"        ;
                _filter    = filter     ;
                comparator = expression ;
            } else {
                _filter    = filter     ;
                comparator = expression ;
            }
            boolean b = expr.toString().isEmpty();

            if (comparator.equalsIgnoreCase("not")) {
                expression = "_NOT_" ;
            }

            if (!comparator.trim().equalsIgnoreCase("=")) {
                if (!_filter.isEmpty())
                    comparator = "=_" + comparator + "_"  ;
            }

            expr.append(operande).append(_filter)
                                 .append(comparator)
                                 .append(value)    ;
        }) ;

        return expr.toString()    ;
    }

    private String buildFilters() {

        return expressionsMap.entrySet().stream()
                             .map(s -> toExpressString(s.getKey(), s.getValue()))
                             .collect(Collectors.joining("&")) ;
    }

    public boolean isDisabledChangeFilerButtom() {
        return disabledChangeFilerButtom         ;
    }

    public void calculateExpression() throws UnsupportedEncodingException {

        if (this.expressionsMap.isEmpty()) {     }
        
        this.expressionsMap.computeIfAbsent(selectedFilter, k -> new ArrayList<>())
                           .add( new Expression( selectedFilter, 
                                                 selectedExpression.replace(selectedFilter + " ", ""), 
                                                 selectedValue) ) ;
        selectedValue = null  ;
        setChainedExpresion() ;
        genScript()           ;

        disabledChangeFilerButtom = false                             ;
        FacesContext context      = FacesContext.getCurrentInstance() ;
        context.addMessage( null,
                            new FacesMessage( FacesMessage.SEVERITY_INFO ,
                                              ""                         ,
                                              " Filter Included ")    )  ;
    }

    public void invock() {

    }

    public String getSignatureTemplate()          {
        
        return " Login␣" + "timeStamp␣<b>"        +
               globalConf.getAlgoSigneture()      +
               " </b> [<b> "                      +
               globalConf.getLoginSignature()     +
               "</b> ( Login )  <b>"              +
               globalConf.getPasswordSignature()  + 
               "</b> ( Password ) <b> "           + 
               globalConf.getTimeStampSignature() +
               "</b> ( timeStamp )  " + " ] "     ;
    }
    
    public void initData()             {
        this.selectedExpression = null ;
        this.selectedValue      = null ;
        this.selectedFilter     = null ;
        resetExpresion()               ;
        expressionsMap.clear()          ;
        
        if( serviceSelector.isSelectedService() ) {
           this.acceptItem = this.serviceSelector
                                 .getSelectedServiceAccepts()
                                 .get(0)                    ;
        }
    }
      
    private String getSignatureTemplate_Simple()          {
         
          return  " Login␣" + "timeStamp␣"                +
                  globalConf.getAlgoSigneture()           + 
                  "  [ " + globalConf.getLoginSignature() + 
                  " ( Login )  "                          +
                  globalConf.getPasswordSignature()       + 
                  " ( Password )  "                       + 
                  globalConf.getTimeStampSignature()      +
                  " ( timeStamp )  " + " ] "              ;
    }
 
     
    public void download() throws IOException                  {
        
        FacesContext fc    = FacesContext.getCurrentInstance() ;
        ExternalContext ec = fc.getExternalContext()           ;

        ec.responseReset()                                     ; 
        ec.setResponseContentType("application/octet-stream")  ;
        ec.setResponseHeader("Content-Disposition", "attachment;filename=jaxyClient.jar") ;
     
        ClassLoader classLoader = new ServiceDetails().getClass().getClassLoader()        ;
        URL    uri              = classLoader.getResource("jaxy-client/jaxyClient.jar")   ;
        
        if( uri == null )  return                                                         ;
        
        try ( OutputStream responseOutputStream = ec.getResponseOutputStream() ;
              InputStream fileInputStream       = uri.openStream() )           {
            
            byte[] bytesBuffer = new byte[2048]  ;
            int bytesRead                        ;
            
            while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0 ) {
                 responseOutputStream.write( bytesBuffer, 0, bytesRead ) ;
            }
            
            responseOutputStream.flush() ;
        }

       fc.responseComplete() ;
    }

}


