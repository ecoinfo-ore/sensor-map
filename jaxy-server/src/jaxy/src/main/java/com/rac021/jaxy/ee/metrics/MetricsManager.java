
package com.rac021.jaxy.ee.metrics ;

import com.rac021.jaxy.api.metrics.Metrics ;
import org.eclipse.microprofile.metrics.Metadata ;
import org.eclipse.microprofile.metrics.MetadataBuilder;
import org.eclipse.microprofile.metrics.MetricType ;
import org.eclipse.microprofile.metrics.MetricUnits ;
import org.eclipse.microprofile.metrics.MetricRegistry ;

/**
 *
 * @author ryahiaoui
 */

public class MetricsManager {
    
    
    public static void registerMetric( MetricRegistry metrics , String serviceCodeSnakeName ) {
        
            /** Rename ServiceCode from camelCase to Snake_case . */
            Metadata counterMetadata_XML = new MetadataBuilder().withUnit(MetricUnits.NONE)
                                                                .withType(MetricType.COUNTER)
                                                                .withName(serviceCodeSnakeName + "_xml_plain_counter_total")
                                                                .withDisplayName(serviceCodeSnakeName + "_xml_plain_counter_total")
                                                                .build() ;
            metrics.counter(counterMetadata_XML ) ;
            
            Metadata counterMetadata_XML_ENCRYPTED = new MetadataBuilder().withUnit(MetricUnits.NONE)
                                                                          .withType(MetricType.COUNTER)
                                                                          .withName(serviceCodeSnakeName + "_xml_encrypted_counter_total")
                                                                          .build() ;
                    
            metrics.counter(counterMetadata_XML_ENCRYPTED) ;
            
            Metadata counterMetadata_JSON = new MetadataBuilder().withName(serviceCodeSnakeName + "_json_plain_counter_total" )
                                                                 .withDisplayName(serviceCodeSnakeName + "_json_plain_counter_total" )
                                                                 .withType(MetricType.COUNTER)
                                                                 .withUnit(MetricUnits.NONE )
                                                                 .build() ;
            metrics.counter(counterMetadata_JSON ) ;
            
            Metadata counterMetadata_JSON_ENCRYPTED = new MetadataBuilder().withName(serviceCodeSnakeName + "_json_encrypted_counter_total")
                                                                           .withDisplayName(serviceCodeSnakeName + "_json_encrypted_counter_total")
                                                                           .withType(MetricType.COUNTER )
                                                                           .withUnit( MetricUnits.NONE )
                                                                           .build() ;
            metrics.counter(counterMetadata_JSON_ENCRYPTED ) ;
            
            Metadata counterMetadata_TEMPLATE = new MetadataBuilder().withName(serviceCodeSnakeName + "_template_plain_counter_total")
                                                                     .withDisplayName(serviceCodeSnakeName + "_template_plain_counter_total")
                                                                     .withType( MetricType.COUNTER )
                                                                     .withUnit(MetricUnits.NONE )
                                                                     .build() ;
            metrics.counter(counterMetadata_TEMPLATE ) ;
            
            Metadata counterMetadata_TEMPLATE_ENCRYPTED = new MetadataBuilder().withName(serviceCodeSnakeName + "_template_encrypted_counter_total")
                                                                               .withDisplayName(serviceCodeSnakeName + "_template_encrypted_counter_total")
                                                                               .withType( MetricType.COUNTER)
                                                                               .withUnit( MetricUnits.NONE )
                                                                              .build() ;
            metrics.counter(counterMetadata_TEMPLATE_ENCRYPTED ) ;

            // TIMERS 
            
            Metadata timerMetadata_XML = new MetadataBuilder().withName( serviceCodeSnakeName + "_xml_plain_timer")
                                                              .withDisplayName(serviceCodeSnakeName + "_xml_plain_timer")
                                                              .withType( MetricType.TIMER)
                                                              .withUnit( MetricUnits.MILLISECONDS ) 
                                                              .build() ;
            
            Metrics.addTimerService( serviceCodeSnakeName + "_xml_plain_timer",
                                     metrics.timer(timerMetadata_XML ) )      ; 

            Metadata timerMetadata_XML_ENCRYPTED = new MetadataBuilder().withName( serviceCodeSnakeName + "_xml_encrypted_timer")
                                                                        .withDisplayName(serviceCodeSnakeName + "_xml_encrypted_timer") 
                                                                        .withType( MetricType.TIMER  ) 
                                                                        .withUnit(  MetricUnits.MILLISECONDS )
                                                                        .build();

            Metrics.addTimerService( serviceCodeSnakeName + "_xml_encrypted_timer" , 
                                     metrics.timer(timerMetadata_XML_ENCRYPTED ) ) ; 
            
            Metadata timerMetadata_JSON = new MetadataBuilder().withName( serviceCodeSnakeName + "_json_plain_timer")
                                                               .withDisplayName(serviceCodeSnakeName + "_json_plain_timer")
                                                               .withType( MetricType.TIMER  )
                                                               .withUnit( MetricUnits.MILLISECONDS )
                                                               .build() ;

            Metrics.addTimerService( serviceCodeSnakeName + "_json_plain_timer",
                                     metrics.timer(timerMetadata_JSON ) )      ; 

            Metadata timerMetadata_JSON_ENCRYPTED = new MetadataBuilder().withName( serviceCodeSnakeName + "_json_encrypted_timer")
                                                                         .withDisplayName(serviceCodeSnakeName + "_json_encrypted_timer")
                                                                         .withType( MetricType.TIMER ) 
                                                                         .withUnit(  MetricUnits.MILLISECONDS )
                                                                         .build() ;
            
            Metrics.addTimerService(  serviceCodeSnakeName + "_json_encrypted_timer", 
                                      metrics.timer(timerMetadata_JSON_ENCRYPTED ) ) ; 
            
            Metadata timerMetadata_TEMPLATE = new MetadataBuilder().withName( serviceCodeSnakeName + "_template_plain_timer")
                                                                   .withDisplayName(serviceCodeSnakeName + "_template_plain_timer")
                                                                   .withType( MetricType.TIMER )
                                                                   .withUnit( MetricUnits.MILLISECONDS )
                                                                   .build() ;
            
            Metrics.addTimerService(  serviceCodeSnakeName + "_template_plain_timer", 
                                      metrics.timer(timerMetadata_TEMPLATE ) )      ; 

            Metadata timerMetadata_TEMPLATE_ENCRYPTED = new MetadataBuilder().withName( serviceCodeSnakeName + "_template_encrypted_timer")
                                                                             .withDisplayName(serviceCodeSnakeName + "_template_encrypted_timer")
                                                                             .withType( MetricType.TIMER )
                                                                             .withUnit(  MetricUnits.MILLISECONDS )
                                                                             .build() ;
            
            Metrics.addTimerService(  serviceCodeSnakeName + "_template_encrypted_timer" , 
                                      metrics.timer(timerMetadata_TEMPLATE_ENCRYPTED ) ) ; 
        
    }
    
}
