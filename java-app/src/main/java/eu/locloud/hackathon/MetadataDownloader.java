package eu.locloud.hackathon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.owlike.genson.Genson;

public class MetadataDownloader {
    /**
     * TODO: 
     * 1. PROVIDE YOUR API KEY 
     * 2. PROVIDE PACKAGE ID
     * 3. PROVIDE SCHEMA
     */
    private final String API_KEY = "47nY4S9dkuV5aVI9DV62";
    private final String SCHEMA = "OAI_DC";

    // REST endpoint
    private final String BASE_URL = "http://more.locloud.eu:8080/MoRe_API/api/v1/packages/";

    // HEADERS
    public static final String HTTP_HEADER_API_KEY = "X-API-Key";

    public static void main(String[] args) {
        MetadataDownloader downloader = new MetadataDownloader();
        List<String> downloadData = downloader.downloadData(1367);
        System.out.println("sample data:" + downloadData.get(0));
    }

    public List<String> downloadData(int packageId) {
        String endpoint = BASE_URL + packageId + "/datastreams/" + SCHEMA;
        List<String> result = new ArrayList<String>();
        Datastream[] dsArray = getDatastreams(endpoint);
        for (Datastream datastream : dsArray) {
            System.out.println("Downloading " + datastream);
            result.add(getXmlItem(datastream.getUri()));
        }
        return result ;
    }
    
    /**
     * 
     * @param endpoint
     * @return 
     */
    private Datastream[] getDatastreams(String endpoint) {
        Datastream[] datastreamArray = null;
        
         // Get datastreams
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(endpoint);
        
        // Add request header for API key
        request.addHeader(HTTP_HEADER_API_KEY, API_KEY);
        
        try {
            HttpResponse response = client.execute(request);

            // If request is successfull
            if(response.getStatusLine().getStatusCode() == 200) {
                String jsonString = EntityUtils.toString(response.getEntity());
                Genson genson = new Genson();
                datastreamArray = genson.deserialize(jsonString, Datastream[].class);
            }
        } catch (IOException ex) {
            System.err.println("IOException");
        }

        return datastreamArray;
    }

    /**
     * 
     * @param endpoint
     * @return 
     */
    private String getXmlItem(String endpoint) {
        String xmlItem = null;
        
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(endpoint);
        
        // Add request header for API key
        request.addHeader(HTTP_HEADER_API_KEY, API_KEY);
        
        try {
            HttpResponse response = client.execute(request);
            
           if(response.getStatusLine().getStatusCode() == 200) {
                xmlItem = EntityUtils.toString(response.getEntity(), "UTF-8");        
           }
        } catch (IOException ex) {
            System.err.println("IOException");
        }
        
        return xmlItem;
    }
}
