/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.db;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

/**
 *
 * @author pierpaolo
 */
public class DBAccess {

    private final MongoClient client;

    private static final String DBNAME = "opendatabari";

    private static final String COLLNAME = "poi";

    private static final String COLLMUNICIPI = "municipi";

    public DBAccess(MongoClient client) {
        this.client = client;
    }

    public void createDB() {
        MongoDatabase database = client.getDatabase(DBNAME);
        database.createCollection(COLLNAME);
        MongoCollection<Document> collection = database.getCollection(COLLNAME);
        collection.createIndex(new BsonDocument("geometry", new BsonString("2dsphere")));
    }

    public void dropDB() {
        MongoDatabase database = client.getDatabase(DBNAME);
        database.drop();
    }

    public void insert(String type, String description, String address, String note, Document loc) {
        Document doc = new Document("type", type);
        if (description != null) {
            doc.append("description", description);
        }
        if (address != null) {
            doc.append("address", address);
        }
        if (note != null) {
            doc.append("note", note);
        }
        if (loc != null) {
            loc.append("loc", loc);
        }
        MongoDatabase database = client.getDatabase(DBNAME);
        MongoCollection<Document> collection = database.getCollection(COLLNAME);
        collection.insertOne(doc);
    }

    public void insertDocument(Document doc) {
        MongoDatabase database = client.getDatabase(DBNAME);
        MongoCollection<Document> collection = database.getCollection(COLLNAME);
        collection.insertOne(doc);
    }

    public void insertMunicipi(Document doc) {
        MongoDatabase database = client.getDatabase(DBNAME);
        MongoCollection<Document> collection = database.getCollection(COLLMUNICIPI);
        collection.insertOne(doc);
    }

    public String findByType(String type, int offset, int size) throws Exception {
        MongoDatabase database = client.getDatabase(DBNAME);
        MongoCollection<Document> collection = database.getCollection(COLLNAME);
        FindIterable<Document> documents = collection.find(new Document().append("type", type)).skip(offset).limit(size);
        List<Document> list = new ArrayList<>();
        for (Document doc : documents) {
            list.add(doc);
        }
        Document response = new Document();
        response.append("size", list.size());
        response.append("results", list);
        return response.toJson();
    }

    public String poiByMunicipio(int id) throws Exception {
        MongoDatabase database = client.getDatabase(DBNAME);
        MongoCollection<Document> collMunicipi = database.getCollection(COLLMUNICIPI);
        Document municipioDoc = collMunicipi.find(new Document().append("properties.OBJECTID", id)).first();
        MongoCollection<Document> collection = database.getCollection(COLLNAME);
        FindIterable<Document> documents = collection.find(new Document("geometry", new Document("$geoWithin", new Document("$geometry", municipioDoc.get("geometry")))));
        List<Document> list = new ArrayList<>();
        for (Document doc : documents) {
            list.add(doc);
        }
        Document response = new Document();
        response.append("size", list.size());
        response.append("results", list);
        return response.toJson();
    }

    public String getMunicipi() throws Exception {
        MongoDatabase database = client.getDatabase(DBNAME);
        MongoCollection<Document> collection = database.getCollection(COLLMUNICIPI);
        FindIterable<Document> documents = collection.find();
        List<Document> list = new ArrayList<>();
        for (Document doc : documents) {
            list.add(doc);
        }
        Document response = new Document();
        response.append("size", list.size());
        response.append("results", list);
        return response.toJson();
    }

}
