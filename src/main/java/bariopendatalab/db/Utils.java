/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.db;

import org.bson.Document;

/**
 *
 * @author pierpaolo
 */
public class Utils {

    public static Document get2DPoint(double latitude, double longitude) {
        Document doc = new Document("type", "Point").append("coordinates", new double[]{latitude, longitude});
        return doc;
    }

}
