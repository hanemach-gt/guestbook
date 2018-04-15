package com.company.serverexample.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URL;

public class StaticHandler implements HttpHandler {


    public void handle(HttpExchange httpExchange) throws IOException {

        // get file path from url
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();

        // get file from resources folder, see: https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
        InputStream is = getClass().getResourceAsStream(path);

        if (is == null || !path.endsWith(".css")) {
            // Object does not exist or is not a file: reject with 404 error.
            send404(httpExchange);
        } else {
            // Object exists and is a file: accept with response code 200.
            sendFile(httpExchange, is);
        }

        if (is != null) {

            is.close();
        }
    }

    private void send404(HttpExchange httpExchange) throws IOException {
        String response = "404 (Not Found)\n";
        byte[] bytes = response.getBytes();

        httpExchange.sendResponseHeaders(404, bytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void sendFile(HttpExchange httpExchange, InputStream is) throws IOException {
        String mime = "text/css";

        httpExchange.getResponseHeaders().set("Content-Type", mime);
        httpExchange.sendResponseHeaders(200, 0);

        OutputStream os = httpExchange.getResponseBody();
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = is.read(buffer)) >= 0) {
            os.write(buffer,0,count);
        }
        os.close();
    }
}
