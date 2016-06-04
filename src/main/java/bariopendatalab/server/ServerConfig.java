/*
 * Questo software è stato sviluppato dal gruppo di ricerca SWAP del Dipartimento di Informatica dell'Università degli Studi di Bari.
 * Tutti i diritti sul software appartengono esclusivamente al gruppo di ricerca SWAP.
 * Il software non può essere modificato e utilizzato per scopi di ricerca e/o industriali senza alcun permesso da parte del gruppo di ricerca SWAP.
 * Il software potrà essere utilizzato a scopi di ricerca scientifica previa autorizzazione o accordo scritto con il gruppo di ricerca SWAP.
 * 
 * Bari, Marzo 2014
 */
package bariopendatalab.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Questa classe carica le proprietà da un file di configurazione e mette a
 * disposizione i metodi per accedere alle proprietò.
 *
 * @author pierpaolo
 */
public class ServerConfig {

    private static ServerConfig instance;

    private Properties props;

    private ServerConfig(String propsFilename) throws IOException {
        props = new Properties();
        props.load(new FileInputStream(propsFilename));
    }

    /**
     * Restituisce una nuova istanza di questo oggetto
     *
     * @return La nuova istanza di questo oggetto
     * @throws IOException
     */
    public synchronized static ServerConfig getInstance() throws IOException {
        return getInstance("./server.config");
    }

    /**
     * Restituisce una nuova istanza di questo oggetto passando come parametro
     * il path completo del file delle proprietà
     *
     * @param propsFilename Il path completo del file delle proprietà
     * @return La nuova istanza di questo oggetto
     * @throws IOException
     */
    public synchronized static ServerConfig getInstance(String propsFilename) throws IOException {
        if (instance == null) {
            instance = new ServerConfig(propsFilename);
        }
        return instance;
    }

    /**
     * Restituisce il valore di un proprietà
     *
     * @param key Il nome della proprietà
     * @return Il valore della proprietà
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * Restituisce il valore di una proprietà come intero
     *
     * @param key Il nome della proprietà
     * @return Il volore intero della proprietà
     */
    public int getInt(String key) throws NumberFormatException {
        return Integer.parseInt(getProperty(key));
    }

}
