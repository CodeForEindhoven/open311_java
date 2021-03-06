package org.codeforamerica.open311.facade;

import com.google.gson.annotations.SerializedName;

/**
 * Represent the different formats allowed by the library.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 *
 */
public enum Format {
    @SerializedName("application/xml")
	XML("xml", "application/xml"),
    @SerializedName("application/json")
    JSON("json", "application/json");

	private String description;
	private String httpContentType;

	Format(String description, String httpContentType) {
		this.description = description;
		this.httpContentType = httpContentType;
	}

	public String getDescription() {
		return description;
	}

	public String getHTTPContentType() {
		return httpContentType;
	}

	public String toString() {
		return getDescription();
	}

	/**
	 * Builds an instance from the content type.
	 *
	 * @param contentTypeString
	 *            A string representing a content type.
	 * @return <code>null</code> if the given content type is not allowed.
	 */
	public static Format getFromHTTPContentTypeString(String contentTypeString) {
		contentTypeString = contentTypeString.toLowerCase();
		if (contentTypeString.equals(JSON.httpContentType)) {
			return Format.JSON;
		}
		if (contentTypeString.equals(XML.httpContentType)) {
			return Format.XML;
		}
		return null;
	}
}
