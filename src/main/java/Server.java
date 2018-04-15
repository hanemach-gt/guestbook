import com.sun.net.httpserver.HttpServer;
import com.company.serverexample.handlers.RootHandler;
import com.company.serverexample.handlers.StaticHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) {

        if (args.length < 1) {

            System.out.println("please specify database path on the commandline");
            System.exit(-1);
        }

        RootHandler.DB_PATH = args[0];

        // create a server on port 8000
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set routes
        server.createContext("/", new RootHandler());
        server.createContext("/static", new StaticHandler());
        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}
