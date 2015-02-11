package eu.locloud.hackathon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        int i=0;
//        for(String s: downloadData){
//            saveFile(s, "file" + i++);
//        }

        SolrIndexer indexer = new SolrIndexer();
        for(String s: downloadData){

            String coverage = adjustCoverage(getCoverage(s));
            indexer.sendData(s,  "lo_" + i++,coverage );
        }

//        String cov = "34.938019, 32.976517";
//        for(int x=0;x<10;x++) {
//            System.out.println((cov));
//            System.out.println(adjustCoverage(cov));
//        }
    }

    public static String adjustCoverage(String coverage){
        String[] parts = coverage.split(",");

        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        Random r = new Random();
        double rangeMin = -0.8;
        double rangeMax = 0.8;
        x += (rangeMin + (rangeMax - rangeMin) * r.nextDouble());
        y += (rangeMin + (rangeMax - rangeMin) * r.nextDouble());

        return String.valueOf(x) + ", " + String.valueOf(y);

    }



    public static String getCoverage(String data){
        String regex = "<dc:coverage>(.*)</dc:coverage>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find())
           return matcher.group(1);
        return "";
    }


    public static void saveFile(String content, String name){
        try {
            File file = new File("/home/ola/IdeaProjects/LolTrip/java-app/target/data/"+ name + ".xml");

//            if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            System.out.println(file.getAbsoluteFile());
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
