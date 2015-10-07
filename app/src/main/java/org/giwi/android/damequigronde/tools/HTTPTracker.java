/*************************************************************************
 * Giwi Softwares CONFIDENTIAL
 * __________________
 * <p/>
 * [2002] - [2013] Giwi Softwares
 * All Rights Reserved.
 * <p/>
 * NOTICE:  All information contained here is, and remains
 * the property of Giwi Softwares and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * here are proprietary to Giwi Softwares
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Giwi Softwares.
 */
package org.giwi.android.damequigronde.tools;

import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 * The Class HTTPTracker.
 */
@EBean(scope = EBean.Scope.Singleton)
public class HTTPTracker {
    /**
     * The constant TAG.
     */
    private static final String TAG = HTTPTracker.class.getName();
    /**
     * The constant LANG.
     */
    private static String LANG = Locale.getDefault().toString();
    /**
     * The constant BASE_URL.
     */
    private static String BASE_URL = "http://www.theblackout.fr/dqg.php";

    /**
     * The enum Methods.
     */
    public enum Methods {
        /**
         * The PUT.
         */
        PUT("PUT"), /**
         * The POST.
         */
        POST("POST"), /**
         * The GET.
         */
        GET("GET"), /**
         * The DELETE.
         */
        DELETE("DELETE");

        /**
         * The Name.
         */
        private String name;

        /**
         * Instantiates a new Methods.
         *
         * @param name the name
         */
        Methods(String name) {
            this.name = name;
        }
    }

    /**
     * Send rest response.
     *
     * @param method            the method
     * @param url               the url
     * @param body              the body
     * @param additionalHeaders the additional headers
     */
    public void send(Methods method, String url, String body, Map<String, String> additionalHeaders) {
        try {
            Log.i(TAG, method.name() + " : " + url);

            URL targetUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setRequestMethod(method.name());
            if (StringUtils.isNotBlank(body)) {
                Log.i(TAG, body);
                conn.setFixedLengthStreamingMode(body.getBytes().length);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
            }
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept-Language", LANG);
            if (additionalHeaders != null) {
                for (String key : additionalHeaders.keySet()) {
                    Log.i(TAG, "http headers : " + key + " : " + additionalHeaders.get(key));
                    conn.setRequestProperty(key, additionalHeaders.get(key));
                }
            }
            conn.connect();
            if (StringUtils.isNotBlank(body)) {
                DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
                printout.write(body.getBytes());
                printout.flush();
                printout.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * Store child.
     *
     * @param child the child
     * @throws JSONException the json exception
     */
    public void storeChild(final String child) throws JSONException {
        final JSONObject json = new JSONObject();
        json.put("childName", child);
        final JSONArray postjson = new JSONArray();
        postjson.put(json);
        send(Methods.POST, BASE_URL, "jsonpost=" + json.toString(), null);
    }

    /**
     * Store sentences.
     *
     * @param sentences the sentences
     * @throws JSONException the jSON exception
     */
    public void storeSentences(final String sentences) throws JSONException {
        final JSONObject json = new JSONObject();
        json.put("sentences", sentences);
        final JSONArray postjson = new JSONArray();
        postjson.put(json);
        send(Methods.POST, BASE_URL, "jsonpost=" + json.toString(), null);
    }

}
