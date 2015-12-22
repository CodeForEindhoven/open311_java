package org.codeforamerica.open311.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.codeforamerica.open311.facade.EndpointType;

/**
 * Contains information regarding to a city (endpoints, allowed formats...).
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class ServiceDiscoveryInfo implements Serializable, Parcelable {

	private static final long serialVersionUID = 5804902138488110319L;
	private final static String GEO_REPORT_V2_SPECIFICATION_URL = "http://wiki.open311.org/GeoReport_v2";
	private Date changeset;
	private String contact;
	private String keyService;
	List<Endpoint> endpoints;

	public ServiceDiscoveryInfo(Date changeset, String contact,
			String keyService, List<Endpoint> endpoints) {
		super();
		this.changeset = changeset;
		this.contact = contact;
		this.keyService = keyService;
		this.endpoints = endpoints;
	}

	public Date getChangeset() {
		return changeset;
	}

	public String getContact() {
		return contact;
	}

	public String getKeyService() {
		return keyService;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	/**
	 * Searches through the list of endpoints and tries to find a valid endpoint
	 * of the given type. Tries to get the GeoReport latest version (currently
	 * v2).
	 * 
	 * @param endpointType
	 *            Type of the desired endpoint.
	 * @return <code>null</code> if a suitable endpoint couldn't be found.
	 */
	public Endpoint getMoreSuitableEndpoint(EndpointType endpointType) {
		List<Endpoint> typeFilteredEndpoints = new LinkedList<>();
		for (Endpoint endpoint : endpoints) {
			if (endpoint.getType() == endpointType) {
				typeFilteredEndpoints.add(endpoint);
			}
		}
		Endpoint candidate = null;
		for (Endpoint endpoint : typeFilteredEndpoints) {
			// Some endpoints don't follows the specification so it does this
			// "double check", which has been proved working.
			if (endpoint.getSpecificationUrl().equals(GEO_REPORT_V2_SPECIFICATION_URL)) {
				if (endpoint.getUrl().contains("v2")) {
					return endpoint;
				}
				candidate = endpoint;
			}
		}
		return candidate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(changeset != null ? changeset.getTime() : -1);
		dest.writeString(this.contact);
		dest.writeString(this.keyService);
		dest.writeList(this.endpoints);
	}

	protected ServiceDiscoveryInfo(Parcel in) {
		long tmpChangeset = in.readLong();
		this.changeset = tmpChangeset == -1 ? null : new Date(tmpChangeset);
		this.contact = in.readString();
		this.keyService = in.readString();
		this.endpoints = new ArrayList<>();
		in.readList(this.endpoints, List.class.getClassLoader());
	}

	public static final Parcelable.Creator<ServiceDiscoveryInfo> CREATOR = new Parcelable.Creator<ServiceDiscoveryInfo>() {
		public ServiceDiscoveryInfo createFromParcel(Parcel source) {
			return new ServiceDiscoveryInfo(source);
		}

		public ServiceDiscoveryInfo[] newArray(int size) {
			return new ServiceDiscoveryInfo[size];
		}
	};
}
