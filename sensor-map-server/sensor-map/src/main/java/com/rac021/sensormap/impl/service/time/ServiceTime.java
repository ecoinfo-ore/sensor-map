
package com.rac021.sensormap.impl.service.time ;

/**
 *
 * @author ryahiaoui
 */

import javax.ws.rs.GET ;
import java.time.Instant ;
import javax.ws.rs.Produces ;
import javax.ws.rs.core.Response ;
import io.quarkus.arc.Unremovable ;
import javax.annotation.PostConstruct ;
import com.rac021.sensormap.api.qualifiers.Stateless ;
import com.rac021.sensormap.api.qualifiers.ServiceRegistry ;

/**
 *
 * @author R.Yahiaoui
 */


@ServiceRegistry("time")
@Stateless
// @Dependent
@Unremovable

public class ServiceTime {

    @PostConstruct
    public void init() {
    }

    public ServiceTime() {
    }

    @GET
    @Produces({ "xml/plain", "json/plain" })
    public Response getTime() throws InterruptedException {
        
       System.out.println(" Time Service : " + this ) ;
     
       return Response.status(Response.Status.OK)
                      .entity(String.valueOf(Instant.now().toEpochMilli()))
                      .build() ;
    }
}
