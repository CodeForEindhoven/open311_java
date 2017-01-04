package org.codeforamerica.open311.internals.network;

import android.graphics.Bitmap;
import android.util.Log;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownServiceException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.codeforamerica.open311.facade.Format;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Implementation using the <a href="http://square.github.io/okhttp/">
 * okHttp</> library.
 *
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class HTTPNetworkManager implements NetworkManager {
    private OkHttpClient okhttpClient;
    private Format format;
    private Bitmap bitmap;
    private String acceptLanguage;
    private static final String FILENAME = "media.jpg";

    public X509TrustManager provideX509TrustManager() {
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

    public OkHttpClient provideHttpClient(TLSSocketFactory sslSocketFactory, X509TrustManager trustManager) {
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
        this.acceptLanguage = locale.getLanguage() + "-" + locale.getCountry() + ", " + locale.getLanguage() + ";q=0.7, *;q=0.5";

    }

    public HTTPNetworkManager(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.okhttpClient = getHttpClient();
        Locale locale = Locale.getDefault();
        this.acceptLanguage = locale.getLanguage() + "-" + locale.getCountry() + ", " + locale.getLanguage() + ";q=0.7, *;q=0.5";

    }

    @Override
    public String doGet(HttpUrl url) throws IOException {
        Request request = new Request.Builder()
                .addHeader("Accept-Language", acceptLanguage)
                .url(url)
                .build();
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
        if (this.bitmap != null) {
            return doPost(url, parameters, bitmap);
        }
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Entry<String, String> param : parameters.entrySet()) {
            formBuilder.add(param.getKey(), param.getValue());
        }
        RequestBody body = formBuilder.build();
        Request request = new Request.Builder()
                .addHeader("Accept-Language", acceptLanguage)
                .url(url)
                .post(body)
                .build();
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

    public String doPost(HttpUrl url, Map<String, String> parameters, Bitmap bitmap) throws IOException {
        // Construct the multipart
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        // Add the parameters
        for (Entry<String, String> entry : parameters.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        // Construct the image
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
        byte[] binaryData = stream.toByteArray();

        //entity.addBinaryBody("media", binaryData, contentType, FILENAME);
        builder.addFormDataPart("media", FILENAME, RequestBody.create(MediaType.parse("image/jpeg"), binaryData));

        //Create the RequestBody
        RequestBody requestBody = builder.build();


        // Create the Request
        Request request = new Request.Builder()
                .addHeader("Accept-Language", acceptLanguage)
                .url(url)
                .post(requestBody)
                .build();
        Response response = okhttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException(
                    "Invalid response - " + response.message()
            );
        }

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
