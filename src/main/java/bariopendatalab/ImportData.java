/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab;

import bariopendatalab.db.DBAccess;
import bariopendatalab.db.Utils;
import com.mongodb.MongoClient;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author pierpaolo
 */
public class ImportData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0;
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            DBAccess dbaccess = new DBAccess(client);
            dbaccess.dropDB();
            dbaccess.createDB();
            FileReader reader = new FileReader(new File(args[0]));
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withIgnoreEmptyLines().withDelimiter(',').parse(reader);
            i = 2;
            for (CSVRecord record : records) {
                String type = record.get("Tipologia");
                String description = record.get("Nome");
                String address = record.get("Indirizzo");
                String civ = record.get("Civ");
                if (address != null && civ != null) {
                    address += ", " + civ;
                }
                String note = record.get("Note");
                String longitudine = record.get("Longitudine");
                String latitudine = record.get("Latitudine");
                if (longitudine != null && latitudine != null) {
                    if (longitudine.length() > 0 && latitudine.length() > 0) {
                        try {
                            dbaccess.insert(type, description, address, note, Utils.get2DPoint(Double.parseDouble(latitudine), Double.parseDouble(longitudine)));
                        } catch (NumberFormatException nex) {
                            dbaccess.insert(type, description, address, note, null);
                        }
                    } else {
                        dbaccess.insert(type, description, address, note, null);
                    }
                } else {
                    dbaccess.insert(type, description, address, note, null);
                }
                i++;
            }
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(ImportData.class.getName()).log(Level.SEVERE, "Error line " + i, ex);
        }
    }

}
