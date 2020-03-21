/*
 * Piwik Java Tracker
 *
 * @link https://github.com/piwik/piwik-java-tracker
 * @license https://github.com/piwik/piwik-java-tracker/blob/master/LICENSE BSD-3 Clause
 */
package org.piwik.java.tracking;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 *
 * @author brettcsorba
 */
public class PiwikTrackerTest{
    PiwikTracker piwikTracker;

    public PiwikTrackerTest(){
    }

    @BeforeClass
    public static void setUpClass(){
    }

    @AfterClass
    public static void tearDownClass(){
    }

    @Before
    public void setUp(){
        piwikTracker = spy(new PiwikTracker("http://test.com"));
    }

    @After
    public void tearDown(){
    }

    /**
     * Test of addParameter method, of class PiwikTracker.
     */
    @Test
    public void testAddParameter(){
    }

    /**
     * Test of sendRequest method, of class PiwikTracker.
     */
    @Test
    public void testSendRequest() throws Exception{
        PiwikRequest request = mock(PiwikRequest.class);
        HttpClient client = mock(HttpClient.class);
        HttpResponse response = mock(HttpResponse.class);

        doReturn(client).when(piwikTracker).getHttpClient();
        doReturn("query").when(request).getQueryString();
        doReturn(response).when(client)
                .execute(argThat(new CorrectGetRequest("http://test.com?query")));

        assertEquals(response, piwikTracker.sendRequest(request));
    }

    /**
     * Test of sendRequestAsync method, of class PiwikTracker.
     */
    @Test
    public void testSendRequestAsync() throws Exception{
        PiwikRequest request = mock(PiwikRequest.class);
        CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
        HttpResponse response = mock(HttpResponse.class);
        Future<HttpResponse> future = mock(Future.class);

        doReturn(client).when(piwikTracker).getHttpAsyncClient();
        doReturn("query").when(request).getQueryString();
        doReturn(response).when(future).get();
        doReturn(true).when(future).isDone();
        doReturn(future).when(client)
                .execute(argThat(new CorrectGetRequest("http://test.com?query")), any());

        assertEquals(response, piwikTracker.sendRequestAsync(request).get());
    }

    static class CorrectGetRequest implements ArgumentMatcher<HttpGet> {
        String url;

        public CorrectGetRequest(String url){
            this.url = url;
        }

        @Override
        public boolean matches(HttpGet get){
            return url.equals(get.getURI().toString());
        }
    }

    /**
     * Test of sendBulkRequest method, of class PiwikTracker.
     */
    @Test
    public void testSendBulkRequest_Iterable() throws Exception{
        List<PiwikRequest> requests = new ArrayList<>();
        HttpResponse response = mock(HttpResponse.class);

        doReturn(response).when(piwikTracker).sendBulkRequest(requests, null);

        assertEquals(response, piwikTracker.sendBulkRequest(requests));
    }

    /**
     * Test of sendBulkRequest method, of class PiwikTracker.
     */
    @Test
    public void testSendBulkRequest_Iterable_StringTT() throws Exception{
        try{
            List<PiwikRequest> requests = new ArrayList<>();
            HttpClient client = mock(HttpClient.class);
            PiwikRequest request = mock(PiwikRequest.class);

            doReturn("query").when(request).getQueryString();
            requests.add(request);
            doReturn(client).when(piwikTracker).getHttpClient();

            piwikTracker.sendBulkRequest(requests, "1");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1 is not 32 characters long.", e.getLocalizedMessage());
        }
    }
    @Test
    public void testSendBulkRequest_Iterable_StringFF() throws Exception{
        List<PiwikRequest> requests = new ArrayList<>();
        HttpClient client = mock(HttpClient.class);
        PiwikRequest request = mock(PiwikRequest.class);
        HttpResponse response = mock(HttpResponse.class);

        doReturn("query").when(request).getQueryString();
        requests.add(request);
        doReturn(client).when(piwikTracker).getHttpClient();
        doReturn(response).when(client).execute(argThat(new CorrectPostRequest("{\"requests\":[\"?query\"]}")));

        assertEquals(response, piwikTracker.sendBulkRequest(requests, null));
    }
    @Test
    public void testSendBulkRequest_Iterable_StringFT() throws Exception{
        List<PiwikRequest> requests = new ArrayList<>();
        HttpClient client = mock(HttpClient.class);
        PiwikRequest request = mock(PiwikRequest.class);
        HttpResponse response = mock(HttpResponse.class);

        doReturn("query").when(request).getQueryString();
        requests.add(request);
        doReturn(client).when(piwikTracker).getHttpClient();
        doReturn(response).when(client)
                .execute(argThat(new CorrectPostRequest("{\"requests\":[\"?query\"],\"token_auth\":\"12345678901234567890123456789012\"}")));

        assertEquals(response, piwikTracker.sendBulkRequest(requests, "12345678901234567890123456789012"));
    }

    /**
     * Test of sendBulkRequestAsync method, of class PiwikTracker.
     */
    @Test
    public void testSendBulkRequestAsync_Iterable() throws Exception{
        List<PiwikRequest> requests = new ArrayList<>();
        HttpResponse response = mock(HttpResponse.class);
        Future<HttpResponse> future = mock(Future.class);
        doReturn(response).when(future).get();
        doReturn(true).when(future).isDone();

        doReturn(future).when(piwikTracker).sendBulkRequestAsync(requests, null);

        assertEquals(response, piwikTracker.sendBulkRequestAsync(requests).get());
    }

    /**
     * Test of sendBulkRequestAsync method, of class PiwikTracker.
     */
    @Test
    public void testSendBulkRequestAsync_Iterable_StringTT() throws Exception{
        try{
            List<PiwikRequest> requests = new ArrayList<>();
            CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
            PiwikRequest request = mock(PiwikRequest.class);

            doReturn("query").when(request).getQueryString();
            requests.add(request);
            doReturn(client).when(piwikTracker).getHttpAsyncClient();

            piwikTracker.sendBulkRequestAsync(requests, "1");
            fail("Exception should have been thrown.");
        }
        catch(IllegalArgumentException e){
            assertEquals("1 is not 32 characters long.", e.getLocalizedMessage());
        }
    }
    @Test
    public void testSendBulkRequestAsync_Iterable_StringFF() throws Exception{
        List<PiwikRequest> requests = new ArrayList<>();
        CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
        PiwikRequest request = mock(PiwikRequest.class);
        HttpResponse response = mock(HttpResponse.class);
        Future<HttpResponse> future = mock(Future.class);
        doReturn(response).when(future).get();
        doReturn(true).when(future).isDone();

        doReturn("query").when(request).getQueryString();
        requests.add(request);
        doReturn(client).when(piwikTracker).getHttpAsyncClient();
        doReturn(future).when(client)
                .execute(argThat(new CorrectPostRequest("{\"requests\":[\"?query\"]}")), any());

        assertEquals(response, piwikTracker.sendBulkRequestAsync(requests, null).get());
    }
    @Test
    public void testSendBulkRequestAsync_Iterable_StringFT() throws Exception{
        List<PiwikRequest> requests = new ArrayList<>();
        CloseableHttpAsyncClient client = mock(CloseableHttpAsyncClient.class);
        PiwikRequest request = mock(PiwikRequest.class);
        HttpResponse response = mock(HttpResponse.class);
        Future<HttpResponse> future = mock(Future.class);
        doReturn(response).when(future).get();
        doReturn(true).when(future).isDone();

        doReturn("query").when(request).getQueryString();
        requests.add(request);
        doReturn(client).when(piwikTracker).getHttpAsyncClient();
        doReturn(future).when(client)
                .execute(argThat(new CorrectPostRequest("{\"requests\":[\"?query\"],\"token_auth\":\"12345678901234567890123456789012\"}")), any());

        assertEquals(response, piwikTracker.sendBulkRequestAsync(requests, "12345678901234567890123456789012").get());
    }

    static class CorrectPostRequest implements ArgumentMatcher<HttpPost> {
        String body;

        public CorrectPostRequest(String body){
            this.body = body;
        }

        @Override
        public boolean matches(HttpPost post){
            try{
                InputStream bais = post.getEntity().getContent();
                byte[] bytes = new byte[bais.available()];
                bais.read(bytes);
                String str = new String(bytes);
                return body.equals(str);
            }
            catch(IOException e){
                fail("Exception should not have been throw.");
            }
            return false;
        }
    }

    /**
     * Test of getHttpClient method, of class PiwikTracker.
     */
    @Test
    public void testGetHttpClient(){
        assertNotNull(piwikTracker.getHttpClient());
    }

    /**
     * Test of getHttpAsyncClient method, of class PiwikTracker.
     */
    @Test
    public void testGetHttpAsyncClient(){
        assertNotNull(piwikTracker.getHttpAsyncClient());
    }

    /**
     * Test of getHttpClient method, of class PiwikTracker, with proxy.
     */
    @Test
    public void testGetHttpClientWithProxy(){
        piwikTracker = new PiwikTracker("http://test.com", "http://proxy", 8080);
        HttpClient httpClient = piwikTracker.getHttpClient();

        assertNotNull(httpClient);
    }
}
