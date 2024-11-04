package com.mtechsoft.bookapp.utils;

import android.content.Context;
import android.util.Log;

import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.models.Parameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class NetworkTask extends BackgroundTask {

    //private static final String prodHost = "http://192.168.10.14:8080/bookapp/public/api/";
    private static final String prodHost = "http://thecellpk.com/bookapp/public/api/";
    protected NetworkListener listener = null;
    protected String results = "";
    protected int statusCode = -1;
    String baseUrl = prodHost;
    //    String baseUrl = host + "api/";
    String type;
    ArrayList<Parameter> params;
    String url = "";
    String charset = "UTF-8";

    public NetworkTask(Context context, String type, String url, ArrayList<Parameter> params) {
        super(context);
        this.type = type;
        this.params = params;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        if (host.equals(devHost) || I_AM_DEV)
//            Log.d("NetworkTask", "URL: " + getAbsoluteUrl() + "?" + getParamsString());
    }

    @Override
    protected Void doInBackground(Void... param) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        InputStream inputStream = null, bis = null;
        try {
            String fullUrl = getAbsoluteUrl();
            if (type.equals("GET")) {
                fullUrl += ("?" + getParamsString());
            }
            URL urlObj = new URL(fullUrl);
            conn = (HttpURLConnection) urlObj.openConnection();
            if (!type.equals("GET")) conn.setDoOutput(true);

            conn.setRequestMethod(type);
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setRequestProperty("Accept", "application/json");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            if (!type.equals("GET")) {
                // required for post request only
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(getParamsString());
                wr.flush();
                wr.close();
            }

            //response from the server
            statusCode = conn.getResponseCode();
            inputStream = (statusCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream());
            bis = new BufferedInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bis));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            //conn.disconnect();
            results = result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            results = e.getMessage();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.d("NetworkTask", "URL: " + getAbsoluteUrl() + "?" + getParamsString());
        Log.d("NetworkTask", "Result " + results);
        try {
            String s = results.trim();
            JSONObject obj = new JSONObject(s);
            Object stat = obj.get("status");
            if (stat instanceof Integer) {
                int status = obj.getInt("status");
                if (status == HttpURLConnection.HTTP_OK) {
                    if (listener != null) {
                        listener.onSuccess(s);
                    }
                } else if (status == HttpURLConnection.HTTP_BAD_REQUEST) {
                    if (listener != null) {
                        listener.onError(s);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError("Invalid JSON response from server.");
            }
        }
    }

    protected String getAbsoluteUrl() {
        return baseUrl + url;
    }

    public void setListener(NetworkListener listener) {
        this.listener = listener;
    }

    public String getParamsString() {
        String p = "";
        String charset = "UTF-8";

        try {
            StringBuilder sbParams = new StringBuilder();
            for (Parameter param : params) {
                String val = param.getValue();
                if (val == null) val = ""; // to avoid NullPointer crash while encoding
                sbParams.append(param.getKey()).append("=")
                        .append(URLEncoder.encode(val, charset));
                sbParams.append("&");
            }
            p = sbParams.toString();
            if (p.length() > 0) {
                sbParams = sbParams.deleteCharAt(sbParams.toString().length() - 1);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }

        return p;
    }
}
