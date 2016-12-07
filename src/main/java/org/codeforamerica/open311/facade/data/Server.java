package org.codeforamerica.open311.facade.data;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Created by miblon on 8/31/16.
 */
public class Server {
    @SerializedName(DataParser.NAME_TAG)
    private String name;
    @SerializedName(DataParser.TITLE_TAG)
    private String title;
    @SerializedName(DataParser.COUNTRY_TAG)
    private String country;
    @SerializedName(DataParser.DOCUMENTATION_TAG)
    private String documentation;
    @SerializedName(DataParser.ISSUETRACKER_TAG)
    private String issueTracker;
    @SerializedName(DataParser.KEYREQUEST_TAG)
    private String keyRequest;
    @SerializedName(DataParser.API_DISCOVERY_URL_TAG)
    private String discoveryUrl;
    @SerializedName(DataParser.JURISDICTION_ID_TAG)
    private String jurisdictionId;
    @SerializedName(DataParser.VERSION_TAG)
    private String version;
    @SerializedName(DataParser.BASE_URL_TAG)
    private String baseURL;
    @SerializedName(DataParser.TEST_URL_TAG)
    private String testURL;
    @SerializedName(DataParser.PRODUCTIONREADY_TAG)
    private Boolean productionReady;
    @SerializedName(DataParser.GOVDOMAIN_TAG)
    private Boolean govDomain;
    @SerializedName(DataParser.APIKEY_TAG)
    private String apiKey;
    @SerializedName(DataParser.UPLOAD_TAG)
    private Upload upload;
    @SerializedName(DataParser.MAP_TAG)
    private Map map;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getName() {
        return name;
    }

    public Server setName(String name) {
        this.name = name;
        return this;
    }

    public Map getMap() {
        return map;
    }

    public Upload getUpload() {
        return upload;
    }

    public String getCountry() {
        return country;
    }

    public Server setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getDocumentation() {
        return documentation;
    }

    public Server setDocumentation(String documentation) {
        this.documentation = documentation;
        return this;
    }

    public String getIssueTracker() {
        return issueTracker;
    }

    public Server setIssueTracker(String issueTracker) {
        this.issueTracker = issueTracker;
        return this;
    }

    public String getKeyRequest() {
        return keyRequest;
    }

    public Server setKeyRequest(String keyRequest) {
        this.keyRequest = keyRequest;
        return this;
    }

    public String getDiscoveryUrl() {
        return discoveryUrl;
    }

    public Server setDiscoveryUrl(String discoveryUrl) {
        this.discoveryUrl = discoveryUrl;
        return this;
    }

    public String getJurisdictionId() {
        return jurisdictionId;
    }

    public Server setJurisdictionId(String jurisdictionId) {
        this.jurisdictionId = jurisdictionId;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Server setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public Server setBaseURL(String baseURL) {
        this.baseURL = baseURL;
        return this;
    }

    public String getTestURL() {
        return testURL;
    }

    public Server setTestURL(String testURL) {
        this.testURL = testURL;
        return this;
    }

    public Boolean getProductionReady() {
        return productionReady;
    }

    public Server setProductionReady(Boolean productionReady) {
        this.productionReady = productionReady;
        return this;
    }

    public Boolean getGovDomain() {
        return govDomain;
    }

    public Server setGovDomain(Boolean govDomain) {
        this.govDomain = govDomain;
        return this;
    }

    /**
     * @deprecated Not for public use.
     * {@link #getName()}}
     */

    public String getCityName() {
        return this.name;
    }

    public String getTitle() {
        if(this.title != null) {
            return this.title;
        } else {
            return this.name;
        }
    }

    /**
     * @deprecated Not for public use.
     * {@link #setName(String)}}
     */
    public void setCityName(String name) {
        this.name = name;
    }

}
