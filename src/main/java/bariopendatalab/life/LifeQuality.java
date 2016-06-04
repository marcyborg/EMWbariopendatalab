/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bariopendatalab.life;

import bariopendatalab.db.DBAccess;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author pierpaolo;
 */
public class LifeQuality {

    private double[][] w;

    private final List<String> labels;

    private final DBAccess dba;

    public LifeQuality(File matrixFile, DBAccess dba) {
        this.dba = dba;
        w = new double[10][4];
        labels = new ArrayList<>();
        try {
        BufferedReader reader = new BufferedReader(new FileReader(matrixFile));
        if (reader.ready()) {
            reader.readLine();
        }
        int r = 0;
        while (reader.ready()) {
            String[] split = reader.readLine().split("\\s+");
            labels.add(split[0]);
            for (int i = 1; i < split.length; i++) {
                w[r][i - 1] = Double.parseDouble(split[i]);
            }
            r++;
        }
        reader.close();
        } catch (IOException ioex) {
            Logger.getLogger(LifeQuality.class.getName()).log(Level.SEVERE, null, ioex);
        }
    }

    public double score(double[] userW, int id) throws Exception {
        double score = 0;
        List<Document> documents = dba.poiListByMunicipio(id);
        Map<String, Integer> map = new HashMap<>();
        for (Document doc : documents) {
            String type = doc.getString("type");
            Integer v = map.get(type);
            if (v == null) {
                map.put(type, 1);
            } else {
                map.put(type, v + 1);
            }
        }
        for (int i = 0; i < userW.length; i++) {
            double s = 0;
            for (int j = 0; j < labels.size(); j++) {
                Integer lw = map.get(labels.get(j));
                if (lw==null)
                    lw=0;
                s += lw.doubleValue() * w[j][i];
            }
            score += userW[i] * s;
        }
        return score;
    }

}
