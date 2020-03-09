
package com.rac021.jaxy.api.streamers ;

import java.util.List ;
import java.util.ArrayList ;
import javax.ejb.Singleton ;
import javax.xml.bind.Marshaller ;
import javax.xml.bind.JAXBContext ;
import javax.xml.bind.JAXBException ;
import javax.annotation.PostConstruct ;
import javax.enterprise.inject.Produces ;
import javax.enterprise.context.ApplicationScoped ;
import com.rac021.jaxy.api.qualifiers.MarshallerType ;

/**
 *
 * @author ryahiaoui
 */

@Singleton
public class MarshallerXmlJson {
    
     JAXBContext jc     ;
    
     List<Class> dtos   ;
     
     @PostConstruct
     public void init() {
        dtos = new ArrayList<>()  ;
        dtos.add(EmptyPojo.class) ;
     }
     
     public void registerDto( Class dto ) throws JAXBException {
        dtos.add(dto) ;
     }
     
     public void instanciateJaxbContext()  {
         
         try {
             jc  = JAXBContext.newInstance( dtos.toArray(new Class[dtos.size()]) ) ;
         } catch (JAXBException ex) {
             throw new RuntimeException(ex) ;
         }
     }
    
     @Produces
     @ApplicationScoped
     @MarshallerType("JSON")
     public Marshaller getMarshallerWithJSONProperties() {
        
        Marshaller marshaller= null ;
        
        try {
        
            marshaller = jc.createMarshaller()                                       ;
            marshaller.setProperty("eclipselink.media-type", "application/json")     ;
            marshaller.setProperty("eclipselink.json.include-root" , Boolean.FALSE)  ;
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE)   ;
        } catch (JAXBException ex)         {
            throw new RuntimeException(ex) ;
        }
        
        return marshaller     ;
    }
    
    @Produces
    @ApplicationScoped
    @MarshallerType("XML")
    public Marshaller getMashellerWithXMLProperties() {
       
        Marshaller marshaller = null ;
        
        try {
            marshaller        = jc.createMarshaller()                               ;
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE)  ;
            marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "")               ;
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE)          ;
            
        } catch (JAXBException ex)        {
           throw new RuntimeException(ex) ;
        }
        
        return marshaller                 ;
    }
   
}
