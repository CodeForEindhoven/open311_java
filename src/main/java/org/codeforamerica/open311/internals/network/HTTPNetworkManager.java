package org.codeforamerica.open311.internals.network;

import android.graphics.Bitmap;

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
import java.util.Map;
import java.util.Map.Entry;

import org.codeforamerica.open311.facade.Format;

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
    private static final String FILENAME = "media.jpg";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    public HTTPNetworkManager() {
        this.okhttpClient = new OkHttpClient();
    }
    public HTTPNetworkManager(Format format) {
        this.format = format;
        this.okhttpClient = new OkHttpClient();
    }
    public HTTPNetworkManager(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.okhttpClient = new OkHttpClient();
    }

    @Override
    public String doGet(HttpUrl url) throws IOException {
        Request request = new Request.Builder()
                .header(ACCEPT_HEADER, format.getHTTPContentType())
                .header(CONTENT_TYPE_HEADER, format.getHTTPContentType())
                .url(url)
                .build();
        Response response = okhttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return response.message();
        }
    }

    @Override
    public String doPost(HttpUrl url, Map<String, String> parameters) throws IOException {
        if(this.bitmap != null){
            return doPost(url, parameters, bitmap);
        }
        try {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (Entry<String, String> param : parameters.entrySet()) {
                formBuilder.add(param.getKey(), param.getValue());
            }
            RequestBody body = formBuilder.build();
            Request request = new Request.Builder()
                    .header(ACCEPT_HEADER, format.getHTTPContentType())
                    .url(url)
                    .post(body)
                    .build();
            Response response = null;

            response = okhttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return response.message();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

        // todo add the image to the body
        //entity.addBinaryBody("media", binaryData, contentType, FILENAME);
        builder.addFormDataPart("media", FILENAME, RequestBody.create(MediaType.parse("image/jpeg"), binaryData));

        //Create the RequestBody
        RequestBody requestBody = builder.build();


        // Create the Request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okhttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return response.message();
        }

    }

    @Override
    public void setFormat(Format format) {
        this.format = format;
    }
}
