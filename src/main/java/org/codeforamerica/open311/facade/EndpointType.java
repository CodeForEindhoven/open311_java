package org.codeforamerica.open311.facade;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Represents the different types of endpoints.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public enum EndpointType {
    @SerializedName("production")
    PRODUCTION,
    @SerializedName("test")
    TEST,
    @SerializedName("acceptation")
    ACCEPTATION,
    @SerializedName("unknown")
    UNKNOWN;

    /**
     * Builds an instance from a string.
     *
     * @param type String representing a type.
     * @return <code>null</code> if the given type isn't recognized.
     */
    public static EndpointType getFromString(String type) {
        type = type.toLowerCase();
        if (type.equals("production")) {
            return PRODUCTION;
        }
        if (type.equals("test")) {
            return TEST;
        }
        if (type.equals("acceptation")) {
            return ACCEPTATION;
        }
        return null;

    }
}
