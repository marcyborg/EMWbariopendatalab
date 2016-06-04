
package bariopendatalab.server;


import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author pierpaolo
 */
public class Server extends Thread {

    private final HttpServer server;

    /**
     * Avvia il server HTTP
     * @return @throws IOException
     */
    protected HttpServer startServer() throws IOException {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        final ResourceConfig rc = new ResourceConfig().packages(true, "bariopendatalab.server.api.v1").
                register(ResponseCorsFilter.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(ServerConfig.getInstance().getProperty("bind.address")), rc);
    }

    /**
     * Crea una nuova istanze del server HTTP per la gestione delle rechieste REST
     * @throws Exception
     */
    public Server() throws Exception {
        this("./server.config");
    }

    /**
     * Crea una nuova istanze del server HTTP per la gestione delle rechieste REST specificando il path completo del file di configurazione
     * @param propsFilename Il path completo del file di configurazione
     * @throws Exception
     */
    public Server(String propsFilename) throws Exception {
        //init config
        System.out.println("Starting server using config file: " + propsFilename);
        ServerConfig.getInstance(propsFilename);
        //init wrapper
        System.out.println("Init wrapper");
        server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl", ServerConfig.getInstance().getProperty("bind.address")));
        //attach a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            //this method is run when the service is stopped (SIGTERM)
            @Override
            public void run() {
                try {
                    server.shutdownNow();
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Esegue il server HTTP per le richieste REST.
     * Può essere passato come argomento il path completo del file di configurazione altrimenti viene caricato il file server.config.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Server service;
            if (args.length > 0) {
                service = new Server(args[0]);
            } else {
                service = new Server();
            }
            service.start();
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
