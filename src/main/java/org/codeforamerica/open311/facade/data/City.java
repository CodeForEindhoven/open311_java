package org.codeforamerica.open311.facade.data;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.facade.Servers;
import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * List taken from http://wiki.open311.org/GeoReport_v2/Servers/
 * <p/>
 * Is checked by running the getHTTPNetworkDiscovery test. Needs to be updated periodically.
 */
public enum City {
    TORONTO("Toronto, ON"),
    GIESSEN("Gießen, Deutschland"),
    BONN("Bonn, Deutschland"),
    ROSTOCK("Rostock, Deutschland"),
    HELSINKI("Helsinki, Suomi"),
    //LAMIA("Lamía, Elláda"),
    BALTIMORE("Baltimore, MD"),
    BLOOMINGTON("Bloomington, IN"),
    BOSTON("Boston, MA"),
    BROOKLINE("Brookline, MA"),
    CHICAGO("Chicago, IL"),
    COLUMBUS("Columbus, IN"),
    GRAND_RAPIDS("Grand Rapids, MI"),
    PEORIA("Peoria, IL"),
    SAN_FRANCISCO("San Francisco, CA"),
    // Washington is no longer using the open311 phttp://wiki.open311.org/GeoReport_v2/Servers/rotocol.
    WASHINGTON("Washington D.C."),
    EINDHOVEN("Eindhoven, NL"),
    //Endpoints in Development
    QUEBEC("Quebec, QC"),
    SURREY("Surrey, BC"),
    ZURICH("Zürich, Schweiz"),
    ZARAGOZA("Zaragoza, España"),
    LISBOA("Lisboa, Portugal"),
    PHILADELPHIA("Philadelphia, PA"),
    // Cities from cities.json that are not available at ; http://wiki.open311.org/GeoReport_v2/Servers/
    ALPHARETTA("City of Alpharetta, GA"),
    BAINBRIDGE_ISLAND("Bainbridge Island, WA"),
    CORONA("City of Corona, CA"),
    DELEON("City of DeLeon, TX"),
    DUNWOODY("City of Dunwoody, GA"),
    HUNTSVILLE("City of Huntsville, AL"),
    NEWBERG("City of Newberg, OR"),
    NEW_HAVEN("City of New Haven, CT"),
    RICHMOND("City of Richmond"),
    RUSSELL_SPRINGS("City of Russell Springs, KY"),
    TUCSON("City of Tucson, AZ"),
    DARWIN("Darwin, Australia"),
    FONTANA("Fontana City Hall"),
    HOWARD("Howard County Department of Public Works, MD"),
    MANOR("Manor, TX"),
    NEWNAN("Newnan, GA"),
    OLATHE("Olathe, KS"),
    RALEIGH("Raleigh City Hall, NC"),
    ROOSEVELT("Roosevelt Island, NY"),
    HILLSBOROUGH("Town of Hillsborough, CA");

    @SerializedName(DataParser.CITY_TAG)
    private String cityName;
    private Server mock;
    Servers servers = new Servers();

    City(String cityName) {
        this.cityName = cityName;
        //lookup the city to get the rest of the properties
        this.mock = servers.getServer(this.cityName);
    }

    public String getCityName() {
        return cityName;
    }

    public String getDiscoveryUrl() {
        return this.mock.getDiscoveryUrl();
    }

    public String getJurisdictionId() {
        return this.mock.getJurisdictionId();
    }

    public String getBaseURL() {
        return this.mock.getBaseURL();
    }

    public String getApiKey() {
        return this.mock.getApiKey();
    }

    public static City fromString(String text) {
        if (text != null) {
            for (City b : City.values()) {
                if (text.equalsIgnoreCase(b.getCityName())) {
                    return b;
                }
            }
        }
        return null;
    }
}


