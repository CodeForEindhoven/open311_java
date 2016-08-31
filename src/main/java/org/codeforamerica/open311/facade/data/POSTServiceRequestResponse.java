package org.codeforamerica.open311.facade.data;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Wraps a POST service request response. <a
 * href="http://wiki.open311.org/GeoReport_v2#POST_Service_Request">More
 * info.</a>.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class POSTServiceRequestResponse {
    @SerializedName(DataParser.SERVICE_REQUEST_ID_TAG)
    private String serviceRequestId;
    @SerializedName(DataParser.TOKEN_TAG)
    private String token;
    @SerializedName(DataParser.SERVICE_NOTICE_TAG)
    private String serviceNotice;
    @SerializedName(DataParser.ACCOUNT_ID_TAG)
    private String accountId;

    public POSTServiceRequestResponse(String serviceRequestId, String token,
                                      String serviceNotice, String accountId) {
        super();
        this.serviceRequestId = serviceRequestId;
        this.token = token;
        this.serviceNotice = serviceNotice;
        this.accountId = accountId;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public String getToken() {
        return token;
    }

    public String getServiceNotice() {
        return serviceNotice;
    }

    public String getAccountId() {
        return accountId;
    }

}
