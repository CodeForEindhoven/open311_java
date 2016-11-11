package org.codeforamerica.open311.facade.data;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Created by miblon on 11/11/16.
 */

public class Upload {
    @SerializedName(DataParser.UPLOAD_URL_TAG)
    private String url;
    @SerializedName(DataParser.UPLOAD_PROTOCOL_TAG)
    private String protocol;

    public String getProtocol(){
        return protocol;
    }
    public String getUrl(){
        return url;
    }

}
