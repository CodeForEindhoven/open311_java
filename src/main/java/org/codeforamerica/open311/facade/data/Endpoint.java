package org.codeforamerica.open311.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codeforamerica.open311.facade.EndpointType;
import org.codeforamerica.open311.facade.Format;

/**
 * Wraps the information relative to a single endpoint.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class Endpoint implements Serializable, Parcelable {
	private static final long serialVersionUID = -5512681512770606630L;
	private String specificationUrl;
	private String url;
	private Date changeset;
	private EndpointType type;
	private List<Format> formats;

	public Endpoint(String specificationUrl, String url, Date changeset,
			EndpointType type, List<Format> formats) {
		super();
		this.specificationUrl = specificationUrl;
		this.url = url;
		this.changeset = changeset;
		this.type = type;
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
		return type;
	}

	/**
	 * Returns the more suitable format. Tries JSON before XML.
	 * 
	 * @return A format allowed by the endpoint.
	 */
	public Format getBestFormat() {
		if (formats.contains(Format.JSON)) {
			return Format.JSON;
		}
		if (formats.contains(Format.XML)) {
			return Format.XML;
		}
		return null;
	}

	/**
	 * Returns whether it allows the given format or not.
	 * 
	 * @param format
	 *            Desired format.
	 * @return <code>true</code> if it allows the given format, else
	 *         <code>false</code>.
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
		dest.writeInt(this.type == null ? -1 : this.type.ordinal());
		dest.writeList(this.formats);
	}

	protected Endpoint(Parcel in) {
		this.specificationUrl = in.readString();
		this.url = in.readString();
		long tmpChangeset = in.readLong();
		this.changeset = tmpChangeset == -1 ? null : new Date(tmpChangeset);
		int tmpType = in.readInt();
		this.type = tmpType == -1 ? null : EndpointType.values()[tmpType];
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
