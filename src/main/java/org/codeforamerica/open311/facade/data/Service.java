package org.codeforamerica.open311.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Represents a GeoReport service.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */

public class Service implements Serializable, Parcelable {
    private static final long serialVersionUID = 1124990264010718071L;
    @SerializedName(DataParser.SERVICE_CODE_TAG)
    private String serviceCode;
    @SerializedName(DataParser.SERVICE_NAME_TAG)
    private String serviceName;
    @SerializedName(DataParser.DESCRIPTION_TAG)
    private String description;
    @SerializedName(DataParser.METADATA_TAG)
    private Boolean metadata;
    @SerializedName(DataParser.TYPE_TAG)
    private Type type;
    @SerializedName(DataParser.KEYWORDS_TAG)
    private String keywords;
    @SerializedName(DataParser.SERVICE_GROUP_TAG)
    private String group;

    public Service(String serviceCode, String serviceName, String description,
                   Boolean metadata, Type type, String keywords, String group) {
        super();
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        this.description = description;
        this.metadata = metadata;
        this.type = type;
        this.keywords = keywords;
        this.group = group;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public Boolean hasMetadata() {
        return metadata;
    }

    public Type getType() {
        return type;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getGroup() {
        return group;
    }

    /**
     * Retrieves the {@link ServiceDefinition} of this instance. This may take a
     * lot of time so it is a good idea not to do it in the UI thread.
     *
     * @return The <a
     * href="http://wiki.open311.org/GeoReport_v2#GET_Service_Definition"
     * >definition</a> of the service which represents the instance.
     * @throws APIWrapperException If there was any problem getting the definition (such as IO,
     *                             response parsing...) or if the instance wasn't created by an
     *                             {@link APIWrapperException}.
     */
    public ServiceDefinition getServiceDefinition() throws APIWrapperException {
        return RelationshipManager.getInstance()
                .getServiceDefinitionFromService(this);
    }

    /**
     * Different types of Service.
     */
    public enum Type {
        REALTIME, BATCH, BLACKBOX;

        /**
         * Returns an instance of this class from a given string.
         *
         * @param type Service Type
         * @return <code>null</code> if the string is not one of the contained
         * types.
         */
        public static Type getFromString(String type) {
            type = type.toLowerCase();
            if (type.equals("realtime")) {
                return REALTIME;
            }
            if (type.equals("batch")) {
                return BATCH;
            }
            if (type.equals("blackbox")) {
                return BLACKBOX;
            }
            return null;
        }
    }

    public String toString() {
        return "[" + this.serviceCode + "] " + this.serviceName + " ("
                + this.description + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serviceCode);
        dest.writeString(this.serviceName);
        dest.writeString(this.description);
        dest.writeValue(this.metadata);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.keywords);
        dest.writeString(this.group);
    }

    protected Service(Parcel in) {
        this.serviceCode = in.readString();
        this.serviceName = in.readString();
        this.description = in.readString();
        this.metadata = (Boolean) in.readValue(Boolean.class.getClassLoader());
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.keywords = in.readString();
        this.group = in.readString();
    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        public Service createFromParcel(Parcel source) {
            return new Service(source);
        }

        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}
