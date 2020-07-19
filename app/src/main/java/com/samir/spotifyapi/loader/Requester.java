package com.samir.spotifyapi.loader;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Requester {
    private static final String LOG_TAG = Requester.class.getSimpleName();
    private static final String SPOTIFY_URL = "https://api.spotify.com/v1/search?";
    private static final String QUERY_PARAM = "q";
    private static final String TYPE = "type";
    private static final String URL_TOKEN = "https://accounts.spotify.com/api/token";
    private static final String GRANT_TYPE = "grant_type=client_credentials";

    public static String searchJSON(String param, String token) {
        String TOKEN = token;
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
        Log.d(LOG_TAG, bookJSONString2);
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

//            URL url = new URL ("https://reqres.in/api/users");
            /*HttpURLConnection con = (HttpURLConnection)requestURL.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);*/

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Authorization", "Basic " + credentials);

            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(GRANT_TYPE.getBytes());
            outputStream.flush();
            outputStream.close();

            //urlConnection.connect();

            //urlConnection.getResponseCode();

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
