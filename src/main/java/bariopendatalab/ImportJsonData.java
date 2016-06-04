/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab;

import bariopendatalab.db.DBAccess;
import com.mongodb.MongoClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BsonString;
import org.bson.Document;

/**
 *
 * @author pierpaolo
 */
public class ImportJsonData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            DBAccess dbaccess = new DBAccess(client);
            dbaccess.dropDB();
            dbaccess.createDB();
            File dirFile = new File(args[0]);
            File[] listFiles = dirFile.listFiles();
            for (File file : listFiles) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while (reader.ready()) {
                    line = reader.readLine();
                    if (line.length() > 0) {
                        if (file.getName().startsWith("municipi")) {
                            Document document = Document.parse(line);
                            dbaccess.insertMunicipi(document);
                        } else {
                            Document document = Document.parse(line);
                            document.put("type", new BsonString(file.getName().replace(".geojson", "")));
                            dbaccess.insertDocument(document);
                        }
                    }
                }
                reader.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(ImportJsonData.class.getName()).log(Level.SEVERE, "Error line ", ex);
        }
    }

}
