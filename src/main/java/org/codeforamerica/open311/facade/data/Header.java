package org.codeforamerica.open311.facade.data;

/**
 * Created by miblon on 1/26/17.
 */

public class Header {
    private String key;
    private String value;

    public Header(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Header setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Header setValue(String value) {
        this.value = value;
        return this;
    }
}
