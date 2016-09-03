package org.codeforamerica.open311.facade.data;

/**
 * Created by miblon on 9/1/16.
 */
public class Value {
    private String key;

    public Value(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
