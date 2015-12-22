package org.codeforamerica.open311.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a <a
 * href="http://wiki.open311.org/GeoReport_v2#GET_Service_Definition">service
 * definition</a>.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class ServiceDefinition implements Serializable, Parcelable {

	private static final long serialVersionUID = -2002090161736619870L;
	private String serviceCode;
	private List<AttributeInfo> attributes;

	public ServiceDefinition(String serviceCode, List<AttributeInfo> attributes) {
		super();
		this.serviceCode = serviceCode;
		this.attributes = attributes;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public List<AttributeInfo> getAttributes() {
		return attributes;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.serviceCode);
		dest.writeList(this.attributes);
	}

	protected ServiceDefinition(Parcel in) {
		this.serviceCode = in.readString();
		this.attributes = new ArrayList<>();
		in.readList(this.attributes, List.class.getClassLoader());
	}

	public static final Parcelable.Creator<ServiceDefinition> CREATOR = new Parcelable.Creator<ServiceDefinition>() {
		public ServiceDefinition createFromParcel(Parcel source) {
			return new ServiceDefinition(source);
		}

		public ServiceDefinition[] newArray(int size) {
			return new ServiceDefinition[size];
		}
	};
}
