/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab;

import bariopendatalab.life.NormalizationType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 * @author pierpaolo
 */
public class Utils {

    public static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            sb.append(reader.readLine()).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static void saveFile(File file, String text) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.append(text);
        writer.close();
    }

    public static NormalizationType getNormalizationType(String value) {
        switch (value.toLowerCase()) {
            case "no":
                return NormalizationType.NO;
            case "poi":
                return NormalizationType.POI;
            case "city":
                return NormalizationType.CITY;
            default:
                return NormalizationType.NO;
        }
    }

    public static String multiPolygon2collection(String jsonString) {
        BsonDocument doc = BsonDocument.parse(jsonString);
        BsonDocument geoDoc = doc.getDocument("geometry");
        if (geoDoc.getString("type").getValue().equals("MultiPolygon")) {
            BsonArray geometries = new BsonArray();
            BsonArray array = geoDoc.getArray("coordinates");
            for (int i = 0; i < array.size(); i++) {
                BsonDocument d1 = new BsonDocument("type", new BsonString("Polygon"));
                d1.append("coordinates", array.get(i));
                geometries.add(d1);
            }
            geoDoc.put("type", new BsonString("GeometryCollection"));
            geoDoc.remove("coordinates");
            geoDoc.put("geometries", geometries);
            return doc.toJson();
        } else {
            return doc.toJson();
        }

    }

    public static void main(String[] args) {
        try {
            if (args.length == 3) {
                if (args[0].equals("-cmp")) {
                    String json = multiPolygon2collection(readFile(new File(args[1])));
                    saveFile(new File(args[2]), json);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
