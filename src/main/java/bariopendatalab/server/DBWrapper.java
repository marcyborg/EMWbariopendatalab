/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.server;

import bariopendatalab.db.DBAccess;
import com.mongodb.MongoClient;

/**
 *
 * @author pierpaolo
 */
public class DBWrapper {

    private static DBWrapper dbwrapper;

    private final DBAccess dba;

    private DBWrapper() {
        MongoClient client = new MongoClient("localhost", 27017);
        dba = new DBAccess(client);
    }

    public static synchronized DBWrapper getInstance() {
        if (dbwrapper == null) {
            dbwrapper = new DBWrapper();
        }
        return dbwrapper;
    }

    public DBAccess getDba() {
        return dba;
    }

}
