package org.codeforamerica.open311.internals.caching;

import java.util.List;

import org.codeforamerica.open311.facade.data.City;
import org.codeforamerica.open311.facade.data.Server;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.operations.GETServiceRequestsFilter;

/**
 * Specifies all the operations needed to do data caching and avoid useless and
 * slow network requests.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public interface Cache {
    /**
     * Saves the endpoints-service discovery relationships.
     *
     * @param data A JSON list.
     */
    void saveCitiesInfo(String data);

    /**
     * Looks for the endpoints-service discovery relationships.
     *
     * @return A JSON list or <code>null</code> if it wasn't found.
     */
    String retrieveCitiesInfo();

    /**
     * Saves a {@link ServiceDiscoveryInfo object} related to a city.
     *
     * @param server             Server related to the requested service discovery.
     * @param serviceDiscovery The obtained service discovery.
     */
    void saveServiceDiscovery(Server server,
                              ServiceDiscoveryInfo serviceDiscovery);

    /**
     * Looks for the service discovery of a given city in the local cache.
     *
     * @param server City of interest.
     * @return The service discovery of the given city of <code>null</code> if
     * it isn't cached.
     */
    ServiceDiscoveryInfo retrieveCachedServiceDiscoveryInfo(Server server);

    /**
     * Saves a list of {@link Service} objects related to an endpoint.
     *
     * @param endpointUrl Url of the endpoint.
     * @param services    Obtained services.
     */
    void saveListOfServices(String endpointUrl, List<Service> services);

    /**
     * Looks for a cached list of services.
     *
     * @param endpointUrl Url of the endpoint.
     * @return The list of services or <code>null</code> if they aren't cached.
     */
    List<Service> retrieveCachedServiceList(String endpointUrl);

    /**
     * Saves a service definition.
     *
     * @param endpointUrl       Url of the endpoint.
     * @param serviceCode       Code of the service.
     * @param serviceDefinition Obtained definition from the server.
     */
    void saveServiceDefinition(String endpointUrl, String serviceCode,
                               ServiceDefinition serviceDefinition);

    /**
     * Looks for a cached service definition.
     *
     * @param endpointUrl Url of the endpoint.
     * @param serviceCode Service code of the desired service.
     * @return A service definition or <code>null</code> if it isn't cached.
     */
    ServiceDefinition retrieveCachedServiceDefinition(
            String endpointUrl, String serviceCode);

    /**
     * Saves a list of service requests.
     *
     * @param endpointUrl Url of the endpoint.
     * @param filter      Filter sent to the endpoint.
     * @param requests    Obtained list of requests.
     */
    void saveServiceRequestList(String endpointUrl,
                                GETServiceRequestsFilter filter, List<ServiceRequest> requests);

    /**
     * Looks for a cached GET service requests response.
     *
     * @param endpointUrl Url of the endpoint.
     * @param filter      The desired filter.
     * @return A list of ServiceRequest or <code>null</code> if they aren't
     * cached.
     */
    List<ServiceRequest> retrieveCachedServiceRequests(
            String endpointUrl, GETServiceRequestsFilter filter);

    /**
     * Saves a service request.
     *
     * @param endpointUrl      Url of the endpoint.
     * @param serviceRequestId Id of the requested service request.
     * @param request          Obtained service request.
     */
    void saveSingleServiceRequest(String endpointUrl,
                                  String serviceRequestId, ServiceRequest request);

    /**
     * Looks for a cached GET service request response.
     *
     * @param endpointUrl      Url of the endpoint.
     * @param serviceRequestId The service request's id.
     * @return A ServiceRequest or <code>null</code> if it wasn't cached.
     */
    ServiceRequest retrieveCachedServiceRequest(String endpointUrl,
                                                String serviceRequestId);

    /**
     * Deletes the cache.
     */
    void deleteCache();

    /**
     * Set a custom time to live to a given operation.
     *
     * @param operation         Operation which time to live will be changed.
     * @param timeToLiveInHours New time to live (in hours).
     */
    void setCustomTimeToLive(CacheableOperation operation,
                             int timeToLiveInHours);

    /**
     * Set of operations which will be cached.
     *
     * @author Santiago Munín <santimunin@gmail.com>
     */
    enum CacheableOperation {
        GET_SERVICE_DISCOVERY,
        GET_SERVICE_LIST,
        GET_SERVICE_DEFINITION,
        GET_SERVICE_REQUEST_LIST,
        GET_SINGLE_SERVICE_REQUEST,
        GET_CITIES_SERVICE_DISCOVERY_URLS
    }
}