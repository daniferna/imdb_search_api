package com.searchpath.empathy.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Singleton;

/**
 * Class implementing the Singleton design pattern using @Singleton to access the ElasticSearch client
 * Access through @Inject tag
 */
@Singleton
public class ElasticClient {

    private RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9300, "http"))
    );;

    public RestHighLevelClient getClient() {
        return client;
    }
}
