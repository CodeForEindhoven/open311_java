package org.codeforamerica.open311.internals.network;

import android.util.Log;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.UnknownServiceException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.data.Header;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Implementation using the <a href="http://square.github.io/okhttp/">
 * okHttp</> library.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 * @author Milo van der Linden <milo@dogodigi.net>
 */
public class HTTPNetworkManager implements NetworkManager {
    private OkHttpClient okhttpClient;
    private Format format;
    private List<Header> headers = new ArrayList<Header>();

    private X509TrustManager provideX509TrustManager() {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException exception) {
            Log.e(getClass().getSimpleName(), "no trust manager available", exception);
        } catch (KeyStoreException exception) {
            Log.e(getClass().getSimpleName(), "no trust manager available", exception);
        }

        return null;
    }

    private OkHttpClient provideHttpClient(TLSSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    private OkHttpClient getHttpClient() {
        try {
            TLSSocketFactory tlsSocketFactory = new TLSSocketFactory(provideX509TrustManager());
            return provideHttpClient(tlsSocketFactory, provideX509TrustManager());
        } catch (KeyManagementException e) {
            Log.e(getClass().getSimpleName(), "no tls ssl socket factory available", e);
        } catch (NoSuchAlgorithmException e) {
            Log.e(getClass().getSimpleName(), "no tls ssl socket factory available", e);
        }
        return null;
    }

    public HTTPNetworkManager() {
        this.okhttpClient = getHttpClient();
        Locale locale = Locale.getDefault();
        Header mHeader = new Header("Accept-Language", locale.getLanguage() + "-" + locale.getCountry() + ", " + locale.getLanguage() + ";q=0.7, *;q=0.5");
        this.headers.add(mHeader);
    }

    private Request.Builder setRequestBuilder() {
        Request.Builder requestBuilder = new Request.Builder();
        for (Header mH : this.headers) {
            requestBuilder.addHeader(mH.getKey(), mH.getValue());
        }
        return requestBuilder;
    }

    @Override
    public String doGet(HttpUrl url) throws IOException {
        Request.Builder mRequestbuilder = setRequestBuilder();
        mRequestbuilder.url(url);
        Request request = mRequestbuilder.build();
        Response response = okhttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            setFormatFromResponse(response);
            return response.body().string();
        } else {
            throw new IOException(
                    "Invalid response - " + response.message()
            );
        }
    }


    @Override
    public String doPost(HttpUrl url, Map<String, String> parameters) throws IOException {
        Request.Builder mRequestbuilder = setRequestBuilder();
        mRequestbuilder.url(url);
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            formBuilder.add(param.getKey(), param.getValue());
        }
        RequestBody body = formBuilder.build();
        mRequestbuilder.post(body);
        Request request = mRequestbuilder.build();
        Response response;

        response = okhttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            setFormatFromResponse(response);
            return response.body().string();
        } else {
            throw new IOException(
                    "Invalid response - " + response.message()
            );
        }

    }

    @Override
    public void setHeader(String key, String value) {
        Header mHeader = new Header(key, value);
        this.headers.add(mHeader);

    }

    @Override
    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return this.format;
    }

    private void setFormatFromResponse(Response response) throws UnknownServiceException {
        if (response.body().contentType().subtype().equals("xml")) {
            this.format = Format.XML;
        } else if (response.body().contentType().subtype().equals("json")) {
            this.format = Format.JSON;
        } else if (response.body().contentType().subtype().equals("plain")) {
            //TORONTO fix
            this.format = Format.JSON;
        } else {
            throw new UnknownServiceException(
                    "Invalid response type - " + response.body().contentType().subtype()
            );
        }
    }
}
