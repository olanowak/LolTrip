/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.locloud.hackathon;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;

import java.io.IOException;

/**
 *
 * @author ola
 */
public class SolrIndexer {
    
    public static final String solrUrl = "http://localhost:8080/solr/default";
    
//    public void indexData(String data){
//
////        Soggith solr = new HttpSolrServer(solrUrl);
//        SolrServer solr = new HttpSolrServer(solrUrl);
//
//
//
//    }


    public void sendData(String data, String id, String coverage) {
        SolrServer server = new HttpSolrServer(solrUrl);
        try {
                ContentStreamUpdateRequest request = prepareDocument(data, id, coverage );
                server.request(request);
                server.commit();

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }

    private ContentStreamUpdateRequest prepareDocument(String record, String id, String coverage) {
        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
        ContentStream cs = new ContentStreamBase.StringStream(record);
        up.addContentStream(cs);
        up.setParam("uprefix", "attr_");
        up.setParam("fmap.content", "attr_content");
        up.setParam("literal.id", id);
        up.setParam("literal.coverage", coverage);

        return up;
    }




}
