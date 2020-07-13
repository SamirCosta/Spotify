package com.samir.spotifyapi.loader;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadParam extends AsyncTaskLoader<String> {
    String mParam;

    public LoadParam(@NonNull Context context, String param) {
        super(context);
        mParam = param;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        /*String JSONSearch = null;
        try {
            String JSONtoken = Requester.tokenReturn();
            JSONObject json = new JSONObject(JSONtoken);
            String token = json.getString("access_token");
            JSONSearch = null;
            if (token != null) {
                JSONSearch = Requester.searchJSON(mParam);
            }else{
                Toast.makeText(getContext(), "Token inválido", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return Requester.searchJSON(mParam);
    }
}
