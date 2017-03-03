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
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;

import java.util.concurrent.ExecutionException;


/**
 * Created by tomas on 2/21/17.
 */

public class HttpClient {

    private static String result;
    private static boolean isSucces = true;

    public HttpClient() {
        result="Bad result";
        isSucces= true;
    }


    //getters and setters
    public static boolean getIsSucces() {
        return isSucces;
    }

    public static void setIsSucces(boolean succes) {
        HttpClient.isSucces = succes;
    }


    public static String getResult() {
        return result;
    }

    public static void setResult(String result) {
        HttpClient.result = result;
    }



/*
Zdroje:https://developer.android.com/training/volley/request.html
http://hmkcode.com/android-send-json-data-to-server/
 */
public static boolean makeRequestLogin(final Context context, RequestQueue queue, String url, final JSONObject request) {



    JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, url, request,
            new Response.Listener<JSONObject>() {


                @Override

                public void onResponse(JSONObject responseObj) {


                        String s = responseObj.toString();
                        setResult(s);
                        Toast.makeText(context, getResult(), Toast.LENGTH_LONG).show();
                        setIsSucces(true);



                        // Parsing json object response
                        /*
                        String message = responseObj.getString("id");

                        if (message.length()>0) {
                            // parsing the user profile information
                            JSONArray users = responseObj.getJSONArray("user_email");   // TATO metoda nefunguje !!!!!!!!!!!!!!!! ale inak to ide oka vsetko

                            //do what ever you want to do with your response

                            //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                             users.toString();


                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
*/


                }
            },
            new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Bad email or password", Toast.LENGTH_SHORT).show();
                    isSucces=false;
                }
            });
    queue.add(strReq);

   return isSucces;

}



    public static void sync(final Context context, RequestQueue queue, String url,final JSONObject json){
      //  String SIGNUP_URL = "http://85.93.125.205:8126/api/login";
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,  new Response.Listener<JSONObject>() {


            @Override

            public void onResponse(JSONObject responseObj) {


                String s = responseObj.toString();
                setResult(s);
                Toast.makeText(context, getResult(), Toast.LENGTH_LONG).show();
                setIsSucces(true);


            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Bad email or password", Toast.LENGTH_SHORT).show();
                        isSucces=false;
                    }
                });
        queue.add(request);

        try {
            JSONObject response = future.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }
    }
}