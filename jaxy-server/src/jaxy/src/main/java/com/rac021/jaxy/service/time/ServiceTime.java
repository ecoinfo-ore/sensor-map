
package com.rac021.jaxy.service.time ;

/**
 *
 * @author ryahiaoui
 */

import javax.ws.rs.GET ;
import java.time.Instant ;
import javax.ws.rs.Produces ;
import javax.ws.rs.core.Response ;
import javax.annotation.PostConstruct ;
import org.eclipse.microprofile.metrics.MetricUnits ;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry ;
import org.eclipse.microprofile.metrics.annotation.Timed ;
import org.eclipse.microprofile.metrics.annotation.Counted ;

/**
 *
 * @author R.Yahiaoui
 */


@ServiceRegistry("time")
// @Secured(policy = Policy.Public)
// @Cipher(cipherType = { CipherTypes.AES_128_CBC, CipherTypes.AES_256_CBC })

// @CircuitBreaker(requestVolumeThreshold = 2, failureRatio = 0.50, delay = 5000, successThreshold = 2 )
// @Bulkhead(value = 2, waitingTaskQueue = 100) // maximum 2 concurrent requests allowed
// @Retry(delay = 100 , maxDuration = 3500, jitter = 400, maxRetries = 1_000_000 )

public class ServiceTime {

    @PostConstruct
    public void init() {
    }

    public ServiceTime() {
    }

    @GET
    @Produces({ "xml/plain", "json/plain" })
    //@Fallback(fallbackMethod = "getTimeFallBack")
    @Timed(name = "service_timer_jaxy", absolute = true, unit = MetricUnits.MILLISECONDS)
    @Counted(name = "countServiceTime", absolute = true, reusable = true, /* monotonic = true, */ unit = MetricUnits.NONE )
    public Response getTime() throws InterruptedException {
       return Response.status(Response.Status.OK)
                      .entity(String.valueOf(Instant.now().toEpochMilli()))
                      .build() ;
    }

    public Response getTimeFallBack() throws InterruptedException {

       return Response.status(Response.Status.OK).entity(" -- Retry Later -- ").build() ;
    }

}
