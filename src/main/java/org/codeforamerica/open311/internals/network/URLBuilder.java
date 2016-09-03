package org.codeforamerica.open311.internals.network;

import okhttp3.HttpUrl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.data.Attribute;

/**
 * Builds the necessary URLs to communicate with the endpoints.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class URLBuilder {

	private static final String GET_SERVICE_LIST = "services";
	private static final String GET_SERVICE_DEFINITION = "services";
	private static final String POST_SERVICE_REQUEST = "requests";
	private static final String GET_SERVICE_REQUEST_FROM_A_TOKEN = "tokens";
	private static final String GET_SERVICE_REQUESTS = "requests";
	private String baseUrl;
	private String jurisdictionId;
	private Format format;

	public URLBuilder(String baseUrl, String jurisdictionId, Format format) {
		this.baseUrl = baseUrl;
		this.jurisdictionId = jurisdictionId;
		this.format = format;
	}

	/**
	 * Builds a GET Service List URL.
	 * 
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public HttpUrl buildGetServiceListUrl() throws MalformedURLException {
		if (format == null){
			format = format.XML;
		}
		String url = baseUrl + "/" + GET_SERVICE_LIST + "." + format.toString();
		return HttpUrl.parse(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a GET Service Definition URL.
	 * 
	 * @param serviceCode
	 *            Code of the service to find the definition.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public HttpUrl buildGetServiceDefinitionUrl(String serviceCode)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_DEFINITION + "/" + serviceCode
				+ "." + format.toString();
		return HttpUrl.parse(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a POST Service Request URL.
	 * 
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public HttpUrl buildPostServiceRequestUrl() throws MalformedURLException {
		String base = baseUrl + "/" + POST_SERVICE_REQUEST + "." + format;
		return HttpUrl.parse(base);
	}

	/**
	 * Builds a GET Service Request ID From Token URL.
	 * 
	 * @param token
	 *            Token associated to a service request.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public HttpUrl buildGetServiceRequestIdFromATokenUrl(String token)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_REQUEST_FROM_A_TOKEN + "/"
				+ token + "." + format;
		return HttpUrl.parse(addJurisdictionId(url, jurisdictionId));
	}

	/**
	 * Builds a GET Service Requests URL.
	 * 
	 * @param arguments
	 *            Pairs (key, value) of optional arguments
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public HttpUrl buildGetServiceRequests(Map<String, String> arguments)
			throws MalformedURLException {
		if(jurisdictionId != null) {
            if (jurisdictionId.length() > 0) {
                arguments.put("jurisdiction_id", jurisdictionId);
            }
        }
		return buildUrl(baseUrl + "/" + GET_SERVICE_REQUESTS + "." + format,
				arguments);
	}

	/**
	 * Builds a GET Service Request URL.
	 * 
	 * @param serviceId
	 *            Id of the service.
	 * @return An URL ready to be accessed.
	 * @throws MalformedURLException
	 *             If one of the parts of the url (endpoint's base url,
	 *             format...) is not correct.
	 */
	public HttpUrl buildGetServiceRequest(String serviceId)
			throws MalformedURLException {
		String url = baseUrl + "/" + GET_SERVICE_REQUESTS + "/" + serviceId
				+ "." + format;
		url = addJurisdictionId(url, jurisdictionId);
		return buildUrl(url, null);
	}

	/**
	 * Builds the list of arguments of a POST service request.
	 * 
	 * @param arguments
	 *            List of (key, value) pairs.
	 * @param attributes
	 *            List of attributes.
	 * @return A list of (key, value) pairs with all the given data (arguments
	 *         and attributes).
	 * @throws MalformedURLException
	 *             If any of the arguments given is not allowed.
	 */
	public Map<String, String> buildPostServiceRequestBody(
			Map<String, String> arguments, List<Attribute> attributes)
			throws MalformedURLException {
		Map<String, String> attributesMap = buildAttributes(attributes);
		arguments = arguments == null ? new HashMap<String, String>()
				: arguments;
		arguments.putAll(attributesMap);
		return arguments;
	}

	/**
	 * Builds a map of (key, value) pairs from the attributes.
	 * 
	 * @param attributes
	 *            Request attributes.
	 * @return Map of (key, value) pairs.
	 */
	private Map<String, String> buildAttributes(List<Attribute> attributes) {
		Map<String, String> result = new HashMap<String, String>();
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				result.putAll(attribute.generatePOSTRequestParameter());
			}
		}
		return result;
	}

	/**
	 * Builds a &-separated parameter string.
	 * 
	 * @param parameters
	 *            List of pairs (key, value).
	 * @return A string of the form (key=value&key2=value2&...).
	 */
	private String buildParameterString(Map<String, String> parameters) {
		boolean first = true;
		if (parameters != null) {
			StringBuilder builder = new StringBuilder();
			for (String key : parameters.keySet()) {
				if (first) {
					first = false;
				} else {
					builder.append("&");
				}
				builder.append(key).append("=").append(parameters.get(key));
			}
			return builder.toString();
		}
		return "";
	}

	/**
	 * Builds an URL from a base plus a list of arguments.
	 * 
	 * @param base
	 *            Host address.
	 * @param arguments
	 *            List of pairs (key, value).
	 * @return a URL built from the given data.
	 * @throws MalformedURLException
	 *             If there was any problem building the URL.
	 */
	private HttpUrl buildUrl(String base, Map<String, String> arguments)
			throws MalformedURLException {
		if (arguments == null) {
			return HttpUrl.parse(base);
		} else {
			return HttpUrl.parse(base + "?" + buildParameterString(arguments));
		}
	}

	/**
	 * Adds a jurisdictionId to an url.
	 * 
	 * @param url
	 *            Base url.
	 * @param jurisdictionId
	 *            Given jurisdiction id.
	 * @return The new url.
	 */
	private String addJurisdictionId(String url, String jurisdictionId) {
		if (jurisdictionId != null && jurisdictionId.length() > 0) {
			return url += "?jurisdiction_id=" + jurisdictionId;
		}
		return url;
	}
}
