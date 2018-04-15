package com.company.serverexample.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.company.serverexample.dao.GuestBookDao;
import com.company.serverexample.helpers.DatabaseConnectionManager;
import com.company.serverexample.model.GuestBookEntry;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RootHandler implements HttpHandler {

    public static String DB_PATH;

    public void handle(HttpExchange httpExchange) throws IOException {

        /* get response method */
        String method = httpExchange.getRequestMethod();

        List<GuestBookEntry> entries = new ArrayList<>();

        /* GET - load the entries and prepare display */
        if (method.equals("GET")) {

            entries = getEntries();
        }

        /* POST - retrieve the data and put it to database, then prepare display */
        if (method.equals("POST")) {

            handlePost(httpExchange);

            entries = getEntries();
        }

        List<String> textualEntries = entriesToText(entries);

        /* apply jTwig template */
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/main.twig");

        JtwigModel model = JtwigModel.newModel().with("entries", textualEntries);

        String response = template.render(model);

        /* respond */
        byte[] bytes = response.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {

        /* read form data*/
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        /* parse key=value&another_key=another_value and escaped unicode codepoints into usable form */
        Map<String, String> inputData = parseFormData(formData);

        /* insert to the database */
        DatabaseConnectionManager dbConMgr = new DatabaseConnectionManager(DB_PATH);
        GuestBookDao dao = new GuestBookDao(dbConMgr.getConnection());

        dao.insert(new GuestBookEntry(inputData.get("author_name"), inputData.get("message")));

        dbConMgr.closeConnection();

    }

    private List<String> entriesToText(List<GuestBookEntry> entries) {

        List<String> textual = new ArrayList<>();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (GuestBookEntry entry : entries) {

            String current = "<table class=\"entry\">"
                            +     "<tr><td class=\"text\">Message: " + entry.getMessage() + "</td></tr>"
                            +     "<tr><td class=\"name\">Author: " + entry.getAuthorName() + "</td></tr>"
                            +     "<tr><td class=\"date\">Date: " + df.format(entry.getDateAdded()) + "</td></tr>"
                            + "</table>";

            textual.add(current);
        }

        return textual;
    }

    private List<GuestBookEntry> getEntries() {

        List<GuestBookEntry> entries = new ArrayList<>();

        /* get all entries from the the database */
        DatabaseConnectionManager dbConMgr = new DatabaseConnectionManager(DB_PATH);
        GuestBookDao dao = new GuestBookDao(dbConMgr.getConnection());

        entries = dao.getAllEntries();

        dbConMgr.closeConnection();

        return entries;
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {

        Map<String, String> data = new HashMap<>();

        String[] pairs = formData.split("&");
        for (String pair : pairs) {

            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            data.put(keyValue[0], value);
        }

        return data;
    }
}
