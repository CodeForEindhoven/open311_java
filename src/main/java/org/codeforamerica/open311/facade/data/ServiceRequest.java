package org.codeforamerica.open311.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.codeforamerica.open311.internals.parsing.DataParser;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * Represents a Service Request.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class ServiceRequest implements Serializable, Parcelable {
    private static final long serialVersionUID = -3276480125287251085L;
    @SerializedName(DataParser.SERVICE_REQUEST_ID_TAG)
    private String serviceRequestId;
    @SerializedName(DataParser.STATUS_TAG)
    private Status status;
    @SerializedName(DataParser.STATUS_NOTES_TAG)
    private String statusNotes;
    @SerializedName(DataParser.SERVICE_NAME_TAG)
    private String serviceName;
    @SerializedName(DataParser.SERVICE_CODE_TAG)
    private String serviceCode;
    @SerializedName(DataParser.DESCRIPTION_TAG)
    private String description;
    @SerializedName(DataParser.AGENCY_RESPONSIBLE_TAG)
    private String agencyResponsible;
    @SerializedName(DataParser.SERVICE_NOTICE_TAG)
    private String serviceNotice;
    @SerializedName(DataParser.REQUESTED_DATETIME_TAG)
    private Date requestedDatetime;
    @SerializedName(DataParser.UPDATED_DATETIME_TAG)
    private Date updatedDatetime;
    @SerializedName(DataParser.EXPECTED_DATETIME_TAG)
    private Date expectedDatetime;
    @SerializedName(DataParser.ADDRESS_TAG)
    private String address;
    @SerializedName(DataParser.ADDRESS_ID_TAG)
    private Long addressId;
    @SerializedName(DataParser.ZIPCODE_TAG)
    private Integer zipCode;
    @SerializedName(DataParser.LATITUDE_TAG)
    private Float latitude;
    @SerializedName(DataParser.LONGITUDE_TAG)
    private Float longitude;
    @SerializedName(DataParser.MEDIA_URL_TAG)
    private URL mediaUrl;

    public ServiceRequest(String serviceRequestId, Status status,
                          String statusNotes, String serviceName, String serviceCode,
                          String description, String agencyResponsible, String serviceNotice,
                          Date requestedDatetime, Date updatedDatetime,
                          Date expectedDatetime, String address, Long addressId,
                          Integer zipCode, Float latitude, Float longitude, URL mediaUrl) {
        super();
        this.serviceRequestId = serviceRequestId;
        this.status = status;
        this.statusNotes = statusNotes;
        this.serviceName = serviceName;
        this.serviceCode = serviceCode;
        this.description = description;
        this.agencyResponsible = agencyResponsible;
        this.serviceNotice = serviceNotice;
        this.requestedDatetime = requestedDatetime;
        this.updatedDatetime = updatedDatetime;
        this.expectedDatetime = expectedDatetime;
        this.address = address;
        this.addressId = addressId;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mediaUrl = mediaUrl;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusNotes() {
        return statusNotes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getDescription() {
        return (description == null) ? "" : description;
    }

    public String getAgencyResponsible() {
        return (agencyResponsible == null) ? "" : agencyResponsible;
    }

    public String getServiceNotice() {
        return (serviceNotice == null) ? "" : serviceNotice;
    }

    public Date getRequestedDatetime() {
        return requestedDatetime;
    }

    public Date getUpdatedDatetime() {
        return updatedDatetime;
    }

    public Date getExpectedDatetime() {
        return expectedDatetime;
    }

    public String getAddress() {
        return address;
    }

    public Long getAddressId() {
        return addressId;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public URL getMediaUrl() {
        return mediaUrl;
    }

    public String toString() {
        return "[" + this.serviceRequestId + "] " + this.description + " ("
                + this.status + ")";
    }

    public enum Status {
        OPEN, CLOSED;

        public static Status getFromString(String status) {
            status = status.toLowerCase();
            if (status.equals("open")) {
                return OPEN;
            }
            if (status.equals("closed")) {
                return CLOSED;
            }
            return null;
        }

        public String toString() {
            return this.name().toLowerCase();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serviceRequestId);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeString(this.statusNotes);
        dest.writeString(this.serviceName);
        dest.writeString(this.serviceCode);
        dest.writeString(this.description);
        dest.writeString(this.agencyResponsible);
        dest.writeString(this.serviceNotice);
        dest.writeLong(requestedDatetime != null ? requestedDatetime.getTime() : -1);
        dest.writeLong(updatedDatetime != null ? updatedDatetime.getTime() : -1);
        dest.writeLong(expectedDatetime != null ? expectedDatetime.getTime() : -1);
        dest.writeString(this.address);
        dest.writeValue(this.addressId);
        dest.writeValue(this.zipCode);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeSerializable(this.mediaUrl);
    }

    protected ServiceRequest(Parcel in) {
        this.serviceRequestId = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : Status.values()[tmpStatus];
        this.statusNotes = in.readString();
        this.serviceName = in.readString();
        this.serviceCode = in.readString();
        this.description = in.readString();
        this.agencyResponsible = in.readString();
        this.serviceNotice = in.readString();
        long tmpRequestedDatetime = in.readLong();
        this.requestedDatetime = tmpRequestedDatetime == -1 ? null : new Date(tmpRequestedDatetime);
        long tmpUpdatedDatetime = in.readLong();
        this.updatedDatetime = tmpUpdatedDatetime == -1 ? null : new Date(tmpUpdatedDatetime);
        long tmpExpectedDatetime = in.readLong();
        this.expectedDatetime = tmpExpectedDatetime == -1 ? null : new Date(tmpExpectedDatetime);
        this.address = in.readString();
        this.addressId = (Long) in.readValue(Long.class.getClassLoader());
        this.zipCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.latitude = (Float) in.readValue(Float.class.getClassLoader());
        this.longitude = (Float) in.readValue(Float.class.getClassLoader());
        this.mediaUrl = (URL) in.readSerializable();
    }

    public static final Parcelable.Creator<ServiceRequest> CREATOR = new Parcelable.Creator<ServiceRequest>() {
        public ServiceRequest createFromParcel(Parcel source) {
            return new ServiceRequest(source);
        }

        public ServiceRequest[] newArray(int size) {
            return new ServiceRequest[size];
        }
    };
}
