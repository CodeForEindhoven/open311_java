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
 * This class is a special implementation of the {@link Cache} interface which
 * is useful if you don't want to do caching.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class NoCache implements Cache {

	public NoCache() {

	}

	@Override
	public void saveCitiesInfo(String data) {
	}

	@Override
	public String retrieveCitiesInfo() {
		return null;
	}

	@Override
	public void saveServiceDiscovery(Server server,
									 ServiceDiscoveryInfo serviceDiscovery) {
	}

	@Override
	public ServiceDiscoveryInfo retrieveCachedServiceDiscoveryInfo(Server server) {
		return null;
	}

	@Override
	public void saveListOfServices(String endpointUrl, List<Service> services) {
	}

	@Override
	public List<Service> retrieveCachedServiceList(String endpointUrl) {
		return null;
	}

	@Override
	public void saveServiceDefinition(String endpointUrl, String serviceCode,
			ServiceDefinition serviceDefinition) {
	}

	@Override
	public ServiceDefinition retrieveCachedServiceDefinition(
			String endpointUrl, String serviceCode) {
		return null;
	}

	@Override
	public void saveServiceRequestList(String endpointUrl,
			GETServiceRequestsFilter filter, List<ServiceRequest> requests) {
	}

	@Override
	public List<ServiceRequest> retrieveCachedServiceRequests(
			String endpointUrl, GETServiceRequestsFilter filter) {
		return null;
	}

	@Override
	public void saveSingleServiceRequest(String endpointUrl,
			String serviceRequestId, ServiceRequest request) {
	}

	@Override
	public ServiceRequest retrieveCachedServiceRequest(String endpointUrl,
			String serviceRequestId) {
		return null;
	}

	@Override
	public void deleteCache() {
	}

	@Override
	public void setCustomTimeToLive(CacheableOperation operation,
			int timeToLiveInHours) {
	}

}
