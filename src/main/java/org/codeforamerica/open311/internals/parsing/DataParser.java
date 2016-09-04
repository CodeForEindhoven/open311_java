package org.codeforamerica.open311.internals.parsing;

import java.util.List;

import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;

/**
 * Specifies required operations to the parsers.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public interface DataParser {
	String TEXT_FORMAT = "utf-8";
	String SERVICE_TAG = "service";
	String SERVICE_CODE_TAG = "service_code";
	String SERVICE_NAME_TAG = "service_name";
	String DESCRIPTION_TAG = "description";
	String METADATA_TAG = "metadata";
	String TYPE_TAG = "type";
	String KEYWORDS_TAG = "keywords";
	String KEYWORDS_SEPARATOR = ",";
	String SERVICE_GROUP_TAG = "group";
	String SERVICE_DEFINITION_TAG = "service_definition";
	String ATTRIBUTE_TAG = "attribute";
	String ATTRIBUTES_TAG = "attributes";
	String VARIABLE_TAG = "variable";
	String CODE_TAG = "code";
	String DATATYPE_TAG = "datatype";
	String REQUIRED_TAG = "required";
	String DATATYPE_DESCRIPTION_TAG = "datatype_description";
	String ORDER_TAG = "order";
	String VALUE_TAG = "value";
	String VALUES_TAG = "values";
	String KEY_TAG = "key";
	String NAME_TAG = "name";
	String SERVICE_REQUEST_TAG = "request";
	String SERVICE_REQUESTS_TAG = "service_requests";
	String TOKEN_TAG = "token";
	String SERVICE_REQUEST_ID_TAG = "service_request_id";
	String STATUS_TAG = "status";
	String STATUS_NOTES_TAG = "status_notes";
	String AGENCY_RESPONSIBLE_TAG = "agency_responsible";
	String SERVICE_NOTICE_TAG = "service_notice";
	String REQUESTED_DATETIME_TAG = "requested_datetime";
	String UPDATED_DATETIME_TAG = "updated_datetime";
	String EXPECTED_DATETIME_TAG = "expected_datetime";
	String ADDRESS_TAG = "address";
	String ADDRESS_ID_TAG = "address_id";
	String ZIPCODE_TAG = "zipcode";
	String LATITUDE_TAG = "lat";
	String LONGITUDE_TAG = "long";
	String MEDIA_URL_TAG = "media_url";
	String ACCOUNT_ID_TAG = "account_id";
	String ERROR_TAG = "error";
	String DISCOVERY_TAG = "discovery";
	String API_DISCOVERY_URL_TAG = "api_discovery";
    String JURISDICTION_ID_TAG = "jurisdiction_id";
	String CHANGESET_TAG = "changeset";
    String COUNTRY_TAG = "country";
	String CONTACT_TAG = "contact";
	String KEY_SERVICE_TAG = "key_service";
	String SPECIFICATION_TAG = "specification";
	String URL_TAG = "url";
	String FORMAT_TAG = "format";
    String FORMATS_TAG = "formats";
	String ENDPOINT_TAG = "endpoint";
    String ENDPOINTS_TAG = "endpoints";
	String START_DATE_TAG = "start_date";
	String END_DATE_TAG = "end_date";
	String EMAIL_TAG = "email";
	String DEVICE_ID_TAG = "device_id";
	String FIRST_NAME_TAG = "first_name";
	String LAST_NAME_TAG = "last_name";
	String PHONE_TAG = "phone";
    String DOCUMENTATION_TAG = "api_documentation";
    String ISSUETRACKER_TAG = "api_issue_tracker";
    String KEYREQUEST_TAG = "api_key_request";
    String VERSION_TAG = "version";
    String BASE_URL_TAG = "base_url";
    String TEST_URL_TAG = "test_url";
    String PRODUCTIONREADY_TAG = "production_ready";
    String GOVDOMAIN_TAG = "gov_domain";
    String CITY_TAG = "city";
    String ID_TAG = "id";
	String APIKEY_TAG = "api_key";

	/**
	 * Parses the response to the GET service list operation.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return A list of {@link Service} objects.
	 */
	List<Service> parseServiceList(String rawData)
			throws DataParsingException;

	/**
	 * Parses a service definition.
	 * 
	 * @param rawData
	 *            Text data.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 * 
	 * @return A service definition object.
	 */
	ServiceDefinition parseServiceDefinition(String rawData)
			throws DataParsingException;

	/**
	 * Parses the response to the GET service request id from a token.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return the given token and the service request id.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	ServiceRequestIdResponse parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException;

	/**
	 * Parses a list of service requests.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return A list of ServiceRequest objects.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException;

	/**
	 * Parses the response of a POST Service Request operation.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return an object containing the response information.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	POSTServiceRequestResponse parsePostServiceRequestResponse(
			String rawData) throws DataParsingException;

	/**
	 * Parses an error and returns an object with its information.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return Error information.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	GeoReportV2Error parseGeoReportV2Errors(String rawData)
			throws DataParsingException;

	/**
	 * Parses a service discovery and returns an object with its information.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return Service discovery information (endpoints and their formats).
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	ServiceDiscoveryInfo parseServiceDiscovery(String rawData)
			throws DataParsingException;
}
