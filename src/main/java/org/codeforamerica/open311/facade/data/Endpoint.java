package org.codeforamerica.open311.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codeforamerica.open311.facade.EndpointType;
import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Wraps the information relative to a single endpoint.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class Endpoint implements Serializable, Parcelable {
    private static final long serialVersionUID = -5512681512770606630L;
    @SerializedName(DataParser.SPECIFICATION_TAG)
    private String specificationUrl;
    @SerializedName(DataParser.URL_TAG)
    private String url;
    @SerializedName(DataParser.CHANGESET_TAG)
    private Date changeset;
    @SerializedName(DataParser.TYPE_TAG)
    private String endpointType;
    @SerializedName(DataParser.FORMATS_TAG)
    private List<Format> formats;

    public Endpoint(String specificationUrl, String url, Date changeset,
                    String type, List<Format> formats) {
        super();
        this.specificationUrl = specificationUrl;
        this.url = url;
        this.endpointType = type;
        this.changeset = changeset;
        this.formats = formats;
    }

    public String getSpecificationUrl() {
        return specificationUrl;
    }

    public String getUrl() {
        return url;
    }

    public Date getChangeset() {
        return changeset;
    }

    public EndpointType getType() {
        return EndpointType.getFromString(endpointType);
    }

    /**
     * Returns the more suitable format. Tries JSON before XML.
     *
     * @return A format allowed by the endpoint.
     */
    public Format getBestFormat() {
        if (formats.contains(Format.JSON)) {
            return Format.JSON;
        } else {
            return Format.XML;
        }
    }

    /**
     * Returns whether it allows the given format or not.
     *
     * @param format Desired format.
     * @return <code>true</code> if it allows the given format, else
     * <code>false</code>.
     */
    public boolean isCompatibleWithFormat(Format format) {
        return formats.contains(format);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.specificationUrl);
        dest.writeString(this.url);
        dest.writeLong(changeset != null ? changeset.getTime() : -1);
        dest.writeInt(this.getType() == null ? -1 : this.getType().ordinal());
        dest.writeList(this.formats);
    }

    protected Endpoint(Parcel in) {
        this.specificationUrl = in.readString();
        this.url = in.readString();
        long tmpChangeset = in.readLong();
        this.changeset = tmpChangeset == -1 ? null : new Date(tmpChangeset);
        int tmpType = in.readInt();
        this.endpointType = tmpType == -1 ? null : EndpointType.values()[tmpType].toString();
        this.formats = new ArrayList<Format>();
        in.readList(this.formats, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<Endpoint> CREATOR = new Parcelable.Creator<Endpoint>() {
        public Endpoint createFromParcel(Parcel source) {
            return new Endpoint(source);
        }

        public Endpoint[] newArray(int size) {
            return new Endpoint[size];
        }
    };
}
