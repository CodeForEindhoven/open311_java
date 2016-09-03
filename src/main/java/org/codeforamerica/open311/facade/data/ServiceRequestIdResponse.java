package org.codeforamerica.open311.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Represents an answer to the <a href=
 * "http://wiki.open311.org/GeoReport_v2#GET_service_request_id_from_a_token"
 * >GET service id from a token</a>.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class ServiceRequestIdResponse implements Parcelable {
	@SerializedName(DataParser.SERVICE_REQUEST_ID_TAG)
	private String serviceRequestId;
	@SerializedName(DataParser.TOKEN_TAG)
	private String token;

	public ServiceRequestIdResponse(String serviceRequestId, String token) {
		super();
		this.serviceRequestId = serviceRequestId;
		this.token = token;
	}

	public String getServiceRequestId() {
		return serviceRequestId;
	}

	public String getToken() {
		return token;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.serviceRequestId);
		dest.writeString(this.token);
	}

	protected ServiceRequestIdResponse(Parcel in) {
		this.serviceRequestId = in.readString();
		this.token = in.readString();
	}

	public static final Parcelable.Creator<ServiceRequestIdResponse> CREATOR = new Parcelable.Creator<ServiceRequestIdResponse>() {
		public ServiceRequestIdResponse createFromParcel(Parcel source) {
			return new ServiceRequestIdResponse(source);
		}

		public ServiceRequestIdResponse[] newArray(int size) {
			return new ServiceRequestIdResponse[size];
		}
	};
}
