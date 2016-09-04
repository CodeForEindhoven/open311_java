package org.codeforamerica.open311.internals.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.data.Endpoint;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.json.JSONArray;

/**
 * Implementation of a {@link DataParser} which takes JSON data as input.
 * List<Service> result = new LinkedList<Service>();
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class JSONParser extends AbstractParser {

    @Override
    public List<Service> parseServiceList(String rawData)
            throws DataParsingException {
        List<Service> result;
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Service>>() {
            }.getType();
            result = gson.fromJson(rawData, listType);
        } catch (Exception e) {
            throw new DataParsingException(e.getMessage());
        }
        return result;
    }

    /**
     * Builds a {@link ServiceDefinition} object from an {@link String} with
     * Valid JSON.
     *
     * @param rawData A valid JSON of a service definition.
     * @return An ServiceDefinition
     */
    @Override
    public ServiceDefinition parseServiceDefinition(String rawData)
            throws DataParsingException {
        ServiceDefinition result;
        try {
            Gson gson = new Gson();
            result = gson.fromJson(rawData, ServiceDefinition.class);
        } catch (Exception e) {
            throw new DataParsingException(e.getMessage());
        }
        return result;
    }

    @Override
    public ServiceRequestIdResponse parseServiceRequestIdFromAToken(
            String rawData) throws DataParsingException {
        List<ServiceRequestIdResponse> result;
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ServiceRequestIdResponse>>() {
            }.getType();
            result = gson.fromJson(rawData, listType);

            if (result.size() == 0) {
                return null;
            } else {
                return result.get(0);
            }
        } catch (Exception e) {
            throw new DataParsingException(e.getMessage());
        }
    }

    @Override
    public List<ServiceRequest> parseServiceRequests(String rawData)
            throws DataParsingException {
        List<ServiceRequest> result;
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            Type listType = new TypeToken<ArrayList<ServiceRequest>>() {
            }.getType();
            result = gson.fromJson(rawData, listType);
            return result;
        } catch (Exception e) {
            throw new DataParsingException(e.getMessage());
        }
    }

    @Override
    public POSTServiceRequestResponse parsePostServiceRequestResponse(
            String rawData) throws DataParsingException {
        List<POSTServiceRequestResponse> result;
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<POSTServiceRequestResponse>>() {
            }.getType();
            result = gson.fromJson(rawData, listType);

            if (result.size() == 0) {
                return null;
            } else {
                return result.get(0);
            }
        } catch (Exception e) {
            throw new DataParsingException(e.getMessage());
        }
    }

    @Override
    public GeoReportV2Error parseGeoReportV2Errors(String rawData)
            throws DataParsingException {
        List<GeoReportV2Error> result;
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<GeoReportV2Error>>() {
            }.getType();
            result = gson.fromJson(rawData, listType);
            if (result.size() == 0) {
                throw new DataParsingException(
                        "The obtained response is not an error object");
            } else {
                return result.get(0);
            }
        } catch (Exception e) {
            throw new DataParsingException(e.getMessage());
        }
    }

    @Override
    public ServiceDiscoveryInfo parseServiceDiscovery(String rawData)
            throws DataParsingException {
        ServiceDiscoveryInfo result;
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .create();
            result = gson.fromJson(rawData, ServiceDiscoveryInfo.class);
            return result;
        } catch (Exception e) {
            throw new DataParsingException(e.getMessage());
        }
    }

    private class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            DateParser dateParser = new DateParser();
            Date result = dateParser.parseDate(jsonElement.getAsString());
            return result;
        }
    }
}
