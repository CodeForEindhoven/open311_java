package org.codeforamerica.open311.internals.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.EndpointType;
import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.data.AttributeInfo;
import org.codeforamerica.open311.facade.data.Endpoint;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.data.Value;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.codeforamerica.open311.internals.parsing.DateParser;

/**
 * Mock {@link NetworkManager}, useful for testing.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class MockNetworkManager implements NetworkManager {
    private Format format = Format.XML;

    public MockNetworkManager(Format format) {
        this.format = format;
    }

    @Override
    public String doGet(HttpUrl url) throws IOException {
        if (url.toString().contains("simulateIOException")) {
            throw new IOException();
        }
        if (format == Format.XML) {
            return XMLResponse(url);
        }
        if (format == Format.JSON) {
            return JSONResponse(url);
        }
        return "";
    }

    @Override
    public String doPost(HttpUrl url, Map<String, String> parameters)
            throws IOException {
        if (url.toString().contains("simulateIOException")) {
            throw new IOException();
        }
        if (format == Format.XML) {
            return XMLPOSTResponse(url);
        }
        if (format == Format.JSON) {
            return JSONPOSTResponse(url);
        }
        return "";

    }

    /**
     * Selects the response if the format is XML.
     *
     * @param url Request URL.
     * @return Empty if it doesn't find any suitable response.
     */
    private String XMLResponse(HttpUrl url) {
        if (url.toString().contains("simulateAPIError")) {
            return errorXML();
        }
        if (url.toString().contains("discovery")) {
            return discoveryXML();
        }
        if (url.toString().contains("services.xml")) {
            return serviceListXML();
        }
        if (url.toString().contains("services/")) {
            return serviceDefinitionXML();
        }
        if (url.toString().contains("tokens/")) {
            return serviceRequestIdFromATokenXML();
        }
        if (url.toString().contains("requests")) {
            return serviceRequestsXML();
        }
        return "";
    }

    /**
     * Selects the response if the format is JSON.
     *
     * @param url Request URL.
     * @return Empty if it doesn't find any suitable response.
     */
    private String JSONResponse(HttpUrl url) {
        if (url.toString().contains("simulateAPIError")) {
            return errorJSON();
        }
        if (url.toString().contains("discovery")) {
            return discoveryJSON();
        }
        if (url.toString().contains("services.json")) {
            return serviceListJSON();
        }
        if (url.toString().contains("services/")) {
            return serviceDefinitionJSON();
        }
        if (url.toString().contains("tokens/")) {
            return serviceRequestIdFromATokenJSON();
        }
        if (url.toString().contains("requests")) {
            return serviceRequestsJSON();
        }
        return "";
    }

    /**
     * Selects the response if the format is JSON.
     *
     * @param url Request URL.
     * @return Empty if it doesn't find any suitable response.
     */
    private String XMLPOSTResponse(HttpUrl url) {
        if (url.toString().contains("simulateAPIError")) {
            return errorXML();
        }
        if (url.toString().contains("requests.xml")) {
            // Test api key
            if (url.toString().contains("api_key")) {
                if (url.toString().contains("api_key=key")) {
                    return postServiceRequestResponseXML();
                } else
                    return errorXML();
            } else {
                return postServiceRequestResponseXML();
            }
        }
        return "";
    }

    /**
     * Selects the response if the format is JSON.
     *
     * @param url Request URL.
     * @return Empty if it doesn't find any suitable response.
     */
    private String JSONPOSTResponse(HttpUrl url) {
        if (url.toString().contains("simulateAPIError")) {
            return errorJSON();
        }
        if (url.toString().contains("requests.json")) {
            // Test api key
            if (url.toString().contains("api_key")) {
                if (url.toString().contains("api_key=key")) {
                    return postServiceRequestResponseJSON();
                } else
                    return errorJSON();
            } else {
                return postServiceRequestResponseJSON();
            }
        }
        return "";
    }

    @Override
    public void setFormat(Format format) {
        if (format != null) {
            this.format = format;
        }
    }

    public Format getFormat() {
        return format;
    }

    /**
     * Mock service list.
     *
     * @return service list XML.
     */
    private String serviceListXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><services><service><service_code>"
                + "001</service_code><service_name>Cans left out 24x7</service_name><description>"
                + "Garbage or recycling cans that have been left out for more than 24 hours after"
                + " collection. Violators will be cited.</description><metadata>true</metadata>"
                + "<type>realtime</type><keywords>lorem, ipsum, dolor</keywords><group>sanitation"
                + "</group></service><service><service_code>002</service_code><metadata>true</metadata>"
                + "<type>realtime</type><keywords>lorem, ipsum, dolor</keywords><group>street</group>"
                + "<service_name>Construction plate shifted</service_name>"
                + "<description>Metal construction plate covering the street or sidewalk has been moved."
                + "</description></service></services>";
    }

    private String serviceListJSON() {
        Gson gson = new Gson();
        Service[] services = new Service[2];
        services[0] = new Service(
                "001",
                "Cans left out 24x7",
                "Garbage or recycling cans that have been left out for more than 24 hours after collection. Violators will be cited.",
                true,
                Service.Type.getFromString("realtime"),
                "lorem, ipsum, dolor",
                "sanitation");
        services[1] = new Service(
                "002",
                "Construction plate shifted",
                "Metal construction plate covering the street or sidewalk has been moved.",
                true,
                Service.Type.getFromString("realtime"),
                "lorem, ipsum, dolor",
                "street");
        return gson.toJson(services);
    }

    /**
     * Mock service definition.
     *
     * @return service definition XML.
     */
    private String serviceDefinitionXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_definition>"
                + "<service_code>DMV66</service_code><attributes><attribute>"
                + "<variable>true</variable><code>WHISHETN</code><datatype>singlevaluelist</datatype>"
                + "<required>true</required><datatype_description></datatype_description><order>1</order>"
                + "<description>What is the ticket/tag/DL number?</description><values><value>"
                + "<key>123</key><name>Ford</name></value><value><key>124</key><name>Chrysler</name>"
                + "</value></values></attribute></attributes></service_definition>";
    }

    /**
     * Mock service definition.
     *
     * @return service definition JSON.
     */
    private String serviceDefinitionJSON() {
        Gson gson = new Gson();
        List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
        Value[] values = new Value[2];
        values[0] = new Value("123", "Ford");
        values[1] = new Value("124", "Chrysler");
        attributes.add(new AttributeInfo(
                true,
                "WHISHETN",
                AttributeInfo.Datatype.getFromString("singlevaluelist"),
                true,
                null,
                1,
                "What is the ticket/tag/DL number?",
                values
        ));
        ServiceDefinition serviceDefinition = new ServiceDefinition(
                "DMV66",
                attributes
        );
        return gson.toJson(serviceDefinition);
    }

    /**
     * Mock service request id from a token.
     *
     * @return XML.
     */
    private String serviceRequestIdFromATokenXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests>"
                + "<request><service_request_id>638344</service_request_id>"
                + "<token>12345</token></request></service_requests>";
    }

    /**
     * Mock service request id from a token.
     *
     * @return JSON.
     */
    private String serviceRequestIdFromATokenJSON() {
        Gson gson = new Gson();
        ServiceRequestIdResponse[] serviceRequestIdResponses = new ServiceRequestIdResponse[2];
        serviceRequestIdResponses[0] = new ServiceRequestIdResponse("638344", "12345");
        serviceRequestIdResponses[1] = new ServiceRequestIdResponse("111", "12345");
        return gson.toJson(serviceRequestIdResponses);
    }

    /**
     * Mock service requests.
     *
     * @return XML.
     */
    private String serviceRequestsXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests>"
                + "<request><service_request_id>638344</service_request_id>"
                + "<status>closed</status><status_notes>Duplicate request."
                + "</status_notes><service_name>Sidewalk and Curb Issues</service_name>"
                + "<service_code>006</service_code><description></description>"
                + "<agency_responsible></agency_responsible><service_notice></service_notice>"
                + "<requested_datetime>2010-04-14T06:37:38-08:00</requested_datetime>"
                + "<updated_datetime>2010-04-14T06:37:38-08:00</updated_datetime>"
                + "<expected_datetime>2010-04-15T06:37:38-08:00</expected_datetime><address>"
                + "8TH AVE and JUDAH ST</address>"
                + "<address_id>545483</address_id><zipcode>94122</zipcode>"
                + "<lat>37.762221815</lat><long>-122.4651145</long>"
                + "<media_url>http://city.gov.s3.amazonaws.com/requests/media/638344.jpg "
                + "</media_url></request>"
                + "<request><service_request_id>638349</service_request_id><status>open</status"
                + "><status_notes></status_notes><service_name>Sidewalk and Curb Issues</service_name>"
                + "<service_code>006</service_code><description></description><agency_responsible>"
                + "</agency_responsible>"
                + "<service_notice></service_notice><requested_datetime>2010-04-19T06:37:38-08:00"
                + "</requested_datetime><updated_datetime>2010-04-19T06:37:38-08:00</updated_datetime>"
                + "<expected_datetime>2010-04-19T06:37:38-08:00</expected_datetime>"
                + "<address>8TH AVE and JUDAH ST</address><address_id>545483"
                + "</address_id><zipcode>94122</zipcode>"
                + "<lat>37.762221815</lat><long>-122.4651145</long>"
                + "<media_url>http://city.gov.s3.amazonaws.com/requests/media/638349.jpg </media_url>"
                + "</request></service_requests>";
    }

    /**
     * Mock service requests.
     *
     * @return JSON.
     */
    private String serviceRequestsJSON() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        DateParser dateParser = new DateParser();
        ServiceRequest[] serviceRequests = new ServiceRequest[2];
        try {
            serviceRequests[0] = new ServiceRequest(
                    "638344",
                    ServiceRequest.Status.getFromString("closed"),
                    "Duplicate request.",
                    "Sidewalk and Curb Issues",
                    "006",
                    null,
                    null,
                    null,
                    dateParser.parseDate("2010-04-14T06:37:38-08:00"),
                    dateParser.parseDate("2010-04-14T06:37:38-08:00"),
                    dateParser.parseDate("2010-04-15T06:37:38-08:00"),
                    "8TH AVE and JUDAH ST",
                    (long) 545483,
                    94122,
                    (float) 37.762221815,
                    (float) -122.4651145,
                    new URL("http://city.gov.s3.amazonaws.com/requests/media/638344.jpg")
            );
            serviceRequests[1] = new ServiceRequest(
                    "638344",
                    ServiceRequest.Status.getFromString("open"),
                    "Duplicate request",
                    "Sidewalk and Curb Issues",
                    "006",
                    null,
                    null,
                    null,
                    dateParser.parseDate("2010-04-14T06:37:38-08:00"),
                    dateParser.parseDate("2010-04-14T06:37:38-08:00"),
                    dateParser.parseDate("2010-04-14T06:37:38-08:00"),
                    "8TH AVE and JUDAH ST",
                    (long) 545483,
                    94122,
                    (float) 37.762221815,
                    (float) -122.4651145,
                    new URL("http://city.gov.s3.amazonaws.com/requests/media/638349.jpg")
            );
            return gson.toJson(serviceRequests);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Mock post service request response.
     *
     * @return XML.
     */
    public String postServiceRequestResponseXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests><request>"
                + "<service_request_id>293944</service_request_id><service_notice>"
                + "The City will inspect and require the responsible party to correct "
                + "within 24 hours and/or issue a Correction Notice or Notice of Violation "
                + "of the Public Works Code</service_notice><account_id/></request>"
                + "</service_requests>";
    }

    /**
     * Mock post service request response.
     *
     * @return JSON.
     */
    public String postServiceRequestResponseJSON() {
        Gson gson = new Gson();
        POSTServiceRequestResponse[] postServiceRequestResponses = new POSTServiceRequestResponse[1];
        postServiceRequestResponses[0] = new POSTServiceRequestResponse(
                "293944",
                null,
                "The City will inspect and require the responsible party to correct within 24 hours and/or issue a Correction Notice or Notice of Violation of the Public Works Code",
                null
        );
        return gson.toJson(postServiceRequestResponses);
    }

    /**
     * Mock error.
     *
     * @return XML.
     */
    public String errorXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><errors><error><code>403</code><description>Invalid api_key received -- can't proceed with create_request.</description></error>"
                + "</errors>";
    }

    /**
     * Mock error.
     *
     * @return JSON.
     */
    public String errorJSON() {
        Gson gson = new Gson();
        GeoReportV2Error[] geoReportV2Errors = new GeoReportV2Error[1];
        geoReportV2Errors[0] = new GeoReportV2Error(
                "403",
                "Invalid api_key received -- can't proceed with create_request."
        );
        return gson.toJson(geoReportV2Errors);
    }

    /**
     * Mock service discovery.
     *
     * @return XML.
     */
    public String discoveryXML() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><discovery>"
                + "<changeset>2011-04-05T17:48:34Z</changeset><contact>Please email "
                + "( content.311@sfgov.org )  or call ( 415-701-2311 ) for assistance "
                + "or to report bugs</contact><key_service>To get an API_KEY please "
                + "visit this website:  http://apps.sfgov.org/Open311API/?page_id=486"
                + "</key_service><endpoints>"
                + "<endpoint><specification>http://wiki.open311.org/GeoReport_v2"
                + "</specification><url>https://open311.sfgov.org/dev/v2</url>"
                + "<changeset>2011-04-20T17:48:34Z</changeset>"
                + "<type>test</type><formats><format>text/XML</format></formats>"
                + "</endpoint><endpoint><specification>http://wiki.open311.org/GeoReport_v2</specification>"
                + "<url>https://open311.sfgov.org/v2</url><changeset>"
                + "2011-04-25T17:48:34Z</changeset><type>production</type><formats>"
                + "<format>text/XML</format></formats></endpoint>"
                + "<endpoint><specification>http://wiki.open311.org/GeoReport_v1</specification>"
                + "<url>https://open311.sfgov.org/dev/v1</url>"
                + "<changeset>2011-04-20T17:48:34Z</changeset><type>test</type><formats>"
                + "<format>text/XML</format></formats></endpoint>"
                + "<endpoint><specification>http://wiki.open311.org/GeoReport_v1"
                + "</specification><url>https://open311.sfgov.org/v1</url>"
                + "<changeset>2011-04-25T17:48:34Z</changeset>"
                + "<type>production</type><formats><format>text/xml</format></formats>"
                + "</endpoint></endpoints></discovery>";
    }

    /**
     * Mock service discovery.
     *
     * @return JSON.
     */
    public String discoveryJSON() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        DateParser dateParser = new DateParser();
        List<Endpoint> endpoints = new ArrayList<Endpoint>();
        List<Format> formats = new ArrayList<Format>();
        formats.add(Format.JSON);
        formats.add(Format.XML);
        endpoints.add(
                new Endpoint(
                        "http://wiki.open311.org/GeoReport_v2",
                        "https://open311.sfgov.org/dev/v2",
                        dateParser.parseDate("2011-04-20T17:48:34Z"),
                        "teSt",
                        formats
                )
        );
        endpoints.add(
                new Endpoint(
                        "http://wiki.open311.org/GeoReport_v2",
                        "https://open311.sfgov.org/v2",
                        dateParser.parseDate("2011-04-25T17:48:34Z"),
                        "PRODUCTION",
                        formats
                )
        );
        endpoints.add(
                new Endpoint(
                        "http://wiki.open311.org/GeoReport_v1",
                        "https://open311.sfgov.org/dev/v1",
                        dateParser.parseDate("2011-04-20T17:48:34Z"),
                        "test",
                        formats
                )
        );
        endpoints.add(
                new Endpoint(
                        "http://wiki.open311.org/GeoReport_v1",
                        "https://open311.sfgov.org/v1",
                        dateParser.parseDate("2011-04-25T17:48:34Z"),
                        "PrOduction",
                        formats
                )
        );
        ServiceDiscoveryInfo serviceDiscoveryInfo = new ServiceDiscoveryInfo(
                dateParser.parseDate("2011-04-05T17:48:34Z"),
                "Please email ( content.311@sfgov.org )  or call ( 415-701-2311 ) for assistance or to report bugs",
                "To get an API_KEY please visit this website:  http://apps.sfgov.org/Open311API/?page_id=486",
                endpoints
        );
        return gson.toJson(serviceDiscoveryInfo);
    }
}
