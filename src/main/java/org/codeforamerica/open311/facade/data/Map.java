package org.codeforamerica.open311.facade.data;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Created by miblon on 11/11/16.
 */

public class Map {
    @SerializedName(DataParser.MAP_LAT_TAG)
    private float lat;
    @SerializedName(DataParser.MAP_LON_TAG)
    private float lon;
    @SerializedName(DataParser.MAP_ZOOM_TAG)
    private int zoom;
    @SerializedName("request_map")
    private Boolean requestMap;

    public float getLat() {
        return lat;
    }

    public Boolean getRequestMapEnabled() {
        if (requestMap == null) return true;
        return requestMap;
    }

    public float getLon() {
        return lon;
    }

    public int getZoom() {
        return zoom;
    }

}
