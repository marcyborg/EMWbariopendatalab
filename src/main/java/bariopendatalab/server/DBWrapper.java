/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.server;

import bariopendatalab.db.DBAccess;
import bariopendatalab.life.LifeQuality;
import com.mongodb.MongoClient;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class DBWrapper {

    private static DBWrapper dbwrapper;

    private final DBAccess dba;

    private final LifeQuality life;

    private DBWrapper() {
        MongoClient client = new MongoClient("localhost", 27017);
        dba = new DBAccess(client);
        life = new LifeQuality(new File("weight.matrix"), dba);

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

    public LifeQuality getLife() {
        return life;
    }
    
    

}
