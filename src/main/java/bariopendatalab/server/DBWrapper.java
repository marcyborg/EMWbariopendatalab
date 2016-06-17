/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.server;

import bariopendatalab.Utils;
import bariopendatalab.db.DBAccess;
import bariopendatalab.life.LifeQuality;
import bariopendatalab.life.NormalizationType;
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

    private LifeQuality life;

    private DBWrapper() {
        MongoClient client = new MongoClient("localhost", 27017);
        dba = new DBAccess(client);
        String filename = "weight.matrix";
        try {
            filename = ServerConfig.getInstance().getProperty("matrix.file");
            life = new LifeQuality(new File(filename), dba, Utils.getNormalizationType(ServerConfig.getInstance().getProperty("mbd.normalization")));
        } catch (IOException ex) {
            Logger.getLogger(DBWrapper.class.getName()).log(Level.SEVERE, null, ex);
            life = new LifeQuality(new File(filename), dba, NormalizationType.NO);
        }
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
