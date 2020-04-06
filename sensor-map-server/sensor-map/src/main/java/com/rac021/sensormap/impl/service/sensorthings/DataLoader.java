
package com.rac021.sensormap.impl.service.sensorthings ;

import java.net.URL ;
import java.util.List ;
import okhttp3.Request ;
import okhttp3.Response ;
import okhttp3.MediaType ;
import okhttp3.RequestBody ;
import java.io.IOException ;
import okhttp3.OkHttpClient ;
import java.net.HttpURLConnection ;
import java.util.concurrent.TimeUnit ;

/**
 *
 * @author ryahiaoui
 */

public class DataLoader {
    
    public static void postDatas ( String        url            ,
                                   List<String>  datas          ,
                                   String        mdiaType       ,
                                   int           connectTimeOut , 
                                   int           readTimeOut    ,
                                   int           writeTimeOut   ,
                                   boolean       verbose        ) throws Exception  {
        
        MediaType mediaType= MediaType.parse(mdiaType) ;
                 
        OkHttpClient client =  new OkHttpClient.Builder()
                                               .connectTimeout( connectTimeOut , TimeUnit.SECONDS)
                                               .writeTimeout( writeTimeOut , TimeUnit.SECONDS)
                                               .readTimeout( readTimeOut , TimeUnit.SECONDS)
                                               .build() ;
       
        if( datas != null && ! datas.isEmpty() ) { 
            
            for ( String data : datas ) {
                 loadDatas ( url        ,
                            mediaType   ,
                            data        ,
                            client      ,
                            verbose     ) ;
            }
        }
    }

    private static void loadDatas( String       url         , 
                                   MediaType    mediaType   , 
                                   String       data        ,
                                   OkHttpClient client      ,
                                   boolean      verbose     ) throws Exception  {

        RequestBody body = RequestBody.create(data, mediaType );

        Request request = new Request.Builder()
                                     .url(url)
                                     .post(body)
                                     .addHeader("content-type", "application/json")
                                     .build() ;


        try ( Response res = client.newCall(request).execute() )                {
            	   
            if ( !res.isSuccessful()) throw new IOException( "Unexpected code " +
                                                             res                +
                                                             "    "             +
                                                             ( res.body() != null ? res.body().string() : res.body() ) ) ;
            
        } catch ( Exception ex ) {
            throw ex ;
           // ex.printStackTrace() ;
        }
    }
      
    public static boolean isReachable(String url) {
 
		try {
			URL urlObj            = new URL(url)                                ;
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection() ;
			con.setRequestMethod( "GET" )    ;
                        // Set connection timeout
			con.setConnectTimeout( 2500 )    ;
			con.connect()                    ;
 
			int code = con.getResponseCode() ;
			if (code == 200)    {
				return true ;
			}
		} catch (IOException e )    {
			return false        ;
		}
                return false                ;
    }
}
