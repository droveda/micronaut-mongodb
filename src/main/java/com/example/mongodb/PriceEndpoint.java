package com.example.mongodb;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/prices")
public class PriceEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PriceEndpoint.class);

    private final MongoClient mongoClient;

    public PriceEndpoint(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Get("/")
    public Flowable<Document> fetch() {
        var collection = getCollection();
        return Flowable.fromPublisher(collection.find());
    }

    @Post("/")
    public Flowable<InsertOneResult> insert(@Body ObjectNode json) {
        var collection = getCollection();
        Document doc = Document.parse(json.toString());
        LOG.info("Insert {}", doc);
        return Flowable.fromPublisher(collection.insertOne(doc));
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("prices").getCollection("example");
    }

}
