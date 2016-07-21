package org.codeforamerica.open311.internals.network;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.Map;

import org.codeforamerica.open311.facade.Format;

/**
 * Specifies the required operations of a NetworkManager
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 */
public interface NetworkManager {

	String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";

	/**
	 * Sends a GET HTTP request.
	 * 
	 * @param url
	 *            Target.
	 * @return Server response.
	 * @throws IOException
	 *             If there was any problem with the connection.
	 */
	String doGet(HttpUrl url) throws IOException;

	/**
	 * Sends a POST HTTP request.
	 * 
	 * @param url
	 *            Target.
	 * @param parameters
	 *            Parameters of the POST operation.
	 * @return Server response.
	 * @throws IOException
	 *             If there was any problem with the connection.
	 */
	String doPost(HttpUrl url, Map<String, String> parameters)
			throws IOException;

	/**
	 * Sets the desired format of the requests.
	 * 
	 * @param format
	 *            A serialization format (XML or JSON).
	 */
	void setFormat(Format format);
}
