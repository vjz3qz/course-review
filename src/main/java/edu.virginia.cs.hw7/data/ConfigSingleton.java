package edu.virginia.cs.hw7.data;

import org.json.JSONObject;

import java.io.*;

public class ConfigSingleton {
    private static final String configurationFileName = "src/main/resources/edu.virginia.cs.hw7/config.json";
    private static ConfigSingleton instance;
    private String databaseName;

    private ConfigSingleton() {
        setFieldsFromJSON();
    }

    public static ConfigSingleton getInstance() {
        if (instance == null) {
            instance = new ConfigSingleton();
            instance.setFieldsFromJSON();
        }
        return instance;
    }

    public String getDatabaseFilename() {
        return databaseName;
    }

    private void setFieldsFromJSON() {
        JSONObject json = null;
        try {
            json = getJSONObject();
        } catch (Exception e) {
            System.out.println("ERROR getting JSON from config.json file");
        }
        String database = json.getString("database");
        setDatabaseFilename(database);
    }

    private JSONObject getJSONObject() {
        BufferedReader br = getBufferedReader();
        StringBuilder sb = new StringBuilder();
        String line = getString(br);
        String text = getText(br, sb, line);
        return new JSONObject(text);
    }

    private static String getText(BufferedReader br, StringBuilder sb, String line) {
        while (line != null) {
            sb.append(line).append('\n');
            try {
                line = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String text = sb.toString();
        return text;
    }

    private static String getString(BufferedReader br) {
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line;
    }

    private static BufferedReader getBufferedReader() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(configurationFileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return br;
    }

    private void setDatabaseFilename(String database) {
        databaseName = database;
    }
}
