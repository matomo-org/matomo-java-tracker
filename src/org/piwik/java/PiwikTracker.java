/* 
 * Piwik Java Tracker
 * 
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java;

import java.io.IOException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * A class that sends {@link PiwikRequest}s to a specified Piwik server.
 * @author brettcsorba
 */
public class PiwikTracker{
    private static final String AUTH_TOKEN = "token_auth";
    private static final String REQUESTS = "requests";
    private final UriBuilder uriBuilder;
    
    /**
     * Creates a tracker that will send {@link PiwikRequest}s to the specified
     * Tracking HTTP API endpoint.
     * @param hostUrl url endpoint to send requests to.  Usually in the format
     * <strong>http://your-piwik-domain.tld/piwik.php</strong>.
     */
    public PiwikTracker(String hostUrl){
        uriBuilder = UriBuilder.fromPath(hostUrl);
    }
    
    /**
     * Send a request.
     * @param request request to send
     * @return the response from this request
     * @throws IOException thrown if there was a problem with this connection
     */
    public HttpResponse sendRequest(PiwikRequest request) throws IOException{
        HttpClient client = getHttpClient();
        uriBuilder.replaceQuery(request.getUrlEncodedQueryString());
        HttpGet get = new HttpGet(uriBuilder.build());
        
        return client.execute(get);
    }
    
    /**
     * Send multiple requests in a single HTTP call.  More efficient than sending
     * several individual requests.
     * @param requests the requests to send
     * @return the response from these requests
     * @throws IOException thrown if there was a problem with this connection
     */
    public HttpResponse sendBulkRequest(Iterable<PiwikRequest> requests) throws IOException{
        return sendBulkRequest(requests, null);
    }
    
    /**
     * Send multiple requests in a single HTTP call.  More efficient than sending
     * several individual requests.  Specify the AuthToken if parameters that require
     * an auth token is used.
     * @param requests the requests to send
     * @param authToken specify if any of the parameters use require AuthToken
     * @return the response from these requests
     * @throws IOException thrown if there was a problem with this connection
     */
    public HttpResponse sendBulkRequest(Iterable<PiwikRequest> requests, String authToken) throws IOException{
        if (authToken != null && authToken.length() != PiwikRequest.AUTH_TOKEN_LENGTH){            
            throw new IllegalArgumentException(authToken+" is not "+PiwikRequest.AUTH_TOKEN_LENGTH+" characters long.");
        }
        
        JsonObjectBuilder ob = Json.createObjectBuilder();
        JsonArrayBuilder ab = Json.createArrayBuilder();
        
        for (PiwikRequest request : requests){
            ab.add("?"+request.getQueryString());
        }
        
        ob.add(REQUESTS, ab);
        
        if (authToken != null){
            ob.add(AUTH_TOKEN, authToken);
        }
        
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(uriBuilder.build());
        post.setEntity(new StringEntity(ob.build().toString(),
                ContentType.APPLICATION_JSON));
        
        return client.execute(post);
    }
    
    /**
     * Get a HTTP client.
     * @return a HTTP client
     */
    protected HttpClient getHttpClient(){
        return new DefaultHttpClient();
    }
}
