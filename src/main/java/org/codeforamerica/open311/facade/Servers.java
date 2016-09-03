package org.codeforamerica.open311.facade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.codeforamerica.open311.facade.data.Server;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Grab the servers.json file and create a java list of objects to be used to select a City or server object.
 */
public class Servers {

    List<Server> collection;

    public Servers() {
        try {
            /*
             * In IntelliJ IDEA or Android Studio:
             *   If you get strange errors on Servers or City, clean out the system caches:
             *     On the main menu, choose File | Invalidate Caches/Restart.
             *     The Invalidate Caches message appears informing you that the caches will be
             *     invalidated and rebuilt on the next start.
             *
             *     Use buttons in the dialog to invalidate caches, restart IntelliJ IDEA or both.
             */
            Reader reader = new InputStreamReader(Servers.class.getResourceAsStream("/servers.json"), "UTF-8");
            Gson gson = new GsonBuilder().create();
            Type listType = new TypeToken<ArrayList<Server>>() {
            }.getType();

            List<Server> result;

            result = gson.fromJson(reader, listType);
            this.setCollection(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Server getServer(String name) {
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getName().equals(name)) {
                return collection.get(i);
            }
        }
        return null;
    }

    public List<Server> getCollection() {
        return collection;
    }

    public Servers setCollection(List<Server> collection) {
        this.collection = collection;
        return this;
    }

}