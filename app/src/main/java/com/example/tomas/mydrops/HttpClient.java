package com.example.tomas.mydrops;

/*
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
*/
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by tomas on 2/21/17.
 */

public class HttpClient {


/*
Zdroje:https://developer.android.com/training/volley/request.html
http://hmkcode.com/android-send-json-data-to-server/
 */
public static void makeRequest(final Context context, RequestQueue queue, String url, JSONObject request) {

    JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, url, request,
            new Response.Listener<JSONObject>() {

                @Override

                public void onResponse(JSONObject responseObj) {
                    try {
                        String s = responseObj.toString();
                        // Parsing json object response
                        String message = responseObj.getString("json");

                        if (message.length()>0) {
                            // parsing the user profile information
                            JSONArray users = responseObj.getJSONArray("user_email");   // TATO metoda nefunguje !!!!!!!!!!!!!!!! ale inak to ide oka vsetko

                            //do what ever you want to do with your response

                            //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                             users.toString();


                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (JSONException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            },
            new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
    queue.add(strReq);
}



}