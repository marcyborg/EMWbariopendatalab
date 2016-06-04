/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;

/**
 *
 * @author pierpaolo
 */
public class DBAccess {

    private final MongoClient client;

    private static final String DBNAME = "opendatabari";

    private static final String COLLNAME = "poi";

    public DBAccess(MongoClient client) {
        this.client = client;
    }

    public void createDB() {
        MongoDatabase database = client.getDatabase(DBNAME);
        database.createCollection(COLLNAME);
        MongoCollection<Document> collection = database.getCollection(COLLNAME);
        collection.createIndex(new BsonDocument("loc", new BsonString("2dsphere")));
    }

    public void dropDB() {
        MongoDatabase database = client.getDatabase(DBNAME);
        database.drop();
    }

    public void insert(String type, String description, String address, String note, Document loc) {
        Document doc = new Document("type", type)
                .append("description", description);
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

}
