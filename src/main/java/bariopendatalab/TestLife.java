/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab;

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
public class TestLife {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            DBAccess dba = new DBAccess(client);
            LifeQuality life = new LifeQuality(new File("weight.matrix"), dba, NormalizationType.POI);
            System.out.println(life.score(new double[]{0.5, 0.1, 0.1, 0.3}, 1));
        } catch (Exception ex) {
            Logger.getLogger(TestLife.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
