package com.samir.spotifyapi.loader;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Requester {
    private static final String LOG_TAG = Requester.class.getSimpleName();
    private static final String SPOTIFY_URL = "https://api.spotify.com/v1/search?";
    private static final String QUERY_PARAM = "q";
    private static final String TYPE = "type";
    private static final String TOKEN = "BQCHTKG3_RY67hPBRUH3aC6H6aD9MDWdN7UOyLNmbGuj6J4UKwoqYwYpdakMTmTHBjfFYU4Ei7TlI1YXL-q4hCF_kuyI4SvpDDQFUik2OxsuAbtlyd5WnB7QJyLojQQFcQFWRL85lXM3vg";
    private static final String URL_TOKEN = "https://accounts.spotify.com/api/token";
    private static final String CLIENT_ID = "88937418052d43a5ab4f99950f05a2bf";
    private static final String CLIENT_SECRET = "cb39035ded114ad5bb0e0f0ded110954";
    private static final String GRANT_TYPE = "grant_type";
    private static final String STRING_GRANT = "client_credentials";

    public static String searchJSON(String param) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString2 = null;

        try {
            Uri builtURI = Uri.parse(SPOTIFY_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, param)
                    .appendQueryParameter(TYPE, "track,artist,album")
                    //.appendQueryParameter(LIMIT, "1")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + TOKEN);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                builder.append(linha);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }
            bookJSONString2 = builder.toString();


        } catch (MalformedURLException | ProtocolException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Log.d(LOG_TAG, bookJSONString);
        return bookJSONString2;
    }

    public static String tokenReturn() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;
        try {

            URL requestURL = new URL(URL_TOKEN);

            /*String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
            byte[] data = credentials.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
*/
            //String encript = Base64.getEncoder().encodeToString(credentials.getBytes());
            String credentials ="ODg5Mzc0MTgwNTJkNDNhNWFiNGY5OTk1MGYwNWEyYmY6Y2IzOTAzNWRlZDExNGFkNWJiMGUwZjBkZWQxMTA5NTQ=";

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("POST");
            //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Authorization", "Basic " + credentials);
            urlConnection.setRequestProperty(GRANT_TYPE, STRING_GRANT);
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            Log.d(LOG_TAG, "AQUIIIIIIII" + inputStream.toString());

            reader = new BufferedReader(new InputStreamReader(inputStream));
            //Log.d(LOG_TAG, "AQUI"+reader.toString());

            StringBuilder builder = new StringBuilder();
            String linha;
            //Log.d(LOG_TAG, "AQUI"+builder.toString());

            while ((linha = reader.readLine()) != null) {
                builder.append(linha);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }
            bookJSONString = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log.d(LOG_TAG, "AQUI"+bookJSONString);
        return bookJSONString;
    }

}
