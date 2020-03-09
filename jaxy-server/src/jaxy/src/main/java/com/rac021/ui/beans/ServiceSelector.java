
package com.rac021.ui.beans ;

import java.util.List ;
import javax.inject.Named ;
import java.io.IOException ;
import java.util.ArrayList ;
import javax.inject.Inject ;
import java.io.Serializable ;
import javax.annotation.PostConstruct ;
import javax.faces.context.FacesContext ;
import javax.faces.context.ExternalContext ;
import javax.faces.application.FacesMessage ;
import javax.enterprise.context.SessionScoped ;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry ;
import com.rac021.jaxy.service_discovery.InfoServices ;
import com.rac021.jaxy.service_discovery.ServiceDescription ;
import com.rac021.jaxy.security_provider.configuration.YamlConfigurator ;

/**
 *
 * @author ryahiaoui
 */

@SessionScoped
@Named("serviceSelector")
public class ServiceSelector implements Serializable  {
    
    @Inject
    transient YamlConfigurator yamlConfigurator       ;

    private List<ServiceDescription> services = null  ;
      
    private ServiceDescription selectedService        ;

    @Inject
    @ServiceRegistry("infoServices")
    transient InfoServices infoServices               ;
    
    @PostConstruct
    public void init() {
      services = infoServices.extractServices()       ;
    }

     public boolean isSelectedService()     {
        return this.selectedService != null ;
    }

    public String getSelectedServiceName()  {
        return this.selectedService != null ?
               this.selectedService.getServiceName() : "--" ;
    }

    public String getSelectedServiceSecurity() {
        return this.selectedService != null    ? 
               this.selectedService.getSecurity() : "--"    ;
    }

    public String getSelectedServiceAcceptsAsString() {
        return this.selectedService != null           ? 
               this.selectedService.getAccepts().toString() : "--" ;
    }

    public List<String> getSelectedServiceAccepts() {
        return this.selectedService != null         ? 
               new ArrayList(this.selectedService.getAccepts())
               : null ;
    }

    public String getSelectedServiceCiphersAsString() {
        return this.selectedService != null           ? 
               this.selectedService.getCiphers().toString() : "--" ;
    }

    public List<String> getSelectedServiceCiphers() {
        return this.selectedService != null         ? 
               new ArrayList<>(this.selectedService.getCiphers())
               : null;
    }

    public String getSelectedServiceParamsAsString() {
        return this.selectedService != null          ? 
               this.selectedService.getParams().toString() : "--" ;
    }

    public List getSelectedServiceFilters() {
        return this.selectedService != null ?
               new ArrayList<>(this.selectedService.getParams().keySet()) :
               null ;
    }
    
    public String getSelectedServiceTemplate() {
        return this.selectedService != null ? 
               this.selectedService.getTemplate() : "--" ;
    }
      
     public Integer getSelectedServiceTotalLinesInTemplate() {
        return this.selectedService != null                  ?
               this.selectedService.getTemplate() != null    ?
               this.selectedService.getTemplate()
                   .split("\r\n|\r|\n").length  : 2  :  2    ;
    }

    public String getSelectedServiceMaxThreads() {
        return this.selectedService != null      ? 
               this.selectedService.getMaxThreads().toString()
               : "--" ;
    }

    public String getSelectedServiceUriTemplate() {

        return this.selectedService != null       ?
               yamlConfigurator.getTransport().toLowerCase() + "://"                 +
               yamlConfigurator.getHost() + ":" + yamlConfigurator.getSelectedPort() +
               yamlConfigurator.getRootApplicationContext() + "/rest/resources/"     +
               this.selectedService.getServiceName() : "--" ;
    }
    
      public void redirectIfEmptyService() {
          
        if (selectedService == null) {
            
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext() ;
            
            try {
                ec.redirect( ec.getRequestContextPath() + "/index.xhtml")  ;
            } catch( IOException ex ) {
                System.out.println(" Exception : " + ex.getMessage() )     ;
            }
        }
    }
      
    public String redirectToDetails(String pageRedirection) {

        if (selectedService != null) {
            return pageRedirection   ;
        }
        
        FacesContext context = FacesContext.getCurrentInstance() ;
        context.addMessage( null,
                            new FacesMessage( FacesMessage.SEVERITY_INFO ,
                                              ""                         ,
                                              " Select a Service before displaying Details ") ) ;
        return null ;

    }
    
    public String getKeyCloakUrlForSelectedService()          {
        return selectedService.getSecurity()
                              .toLowerCase().contains ( "sso" )
                ? yamlConfigurator.getKeycloakUrl() : ""      ;
    }
    
    public void selectedService(ServiceDescription sd)        {
        this.selectedService = sd ;
    }

    public ServiceDescription getSelectedServiceDescription() {
        return selectedService    ;
    }

    public void setSelectedServiceDescription(ServiceDescription ss) {
        this.selectedService = ss ;
    }
    
     public List<ServiceDescription> getServices() {
        return services           ;
    }
     
}
