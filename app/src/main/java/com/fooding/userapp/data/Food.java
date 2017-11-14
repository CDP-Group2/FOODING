package com.fooding.userapp.data;


import android.net.http.RequestQueue;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Food {
    private String name="";
    private Map<String, String> ingredient=new LinkedHashMap<String, String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getIngredient(){ return ingredient; }

    public void setIngredient(Map<String, String> nIngredient){
        this.ingredient.putAll(nIngredient);
    }

    public void addIngredient(String key, String name) {
        this.ingredient.put(key, name);
    }

    //여따가 서버연결 코드

    /*public String request(String key) {
        String response = "";

        try {
            String keyValue = URLEncoder.encode(key, "UTF-8");

            // create http client object to send request to server
            HttpClient Client = new DefaultHttpClient();

            // create URL string
            String url = "http://poerty.co.kr/httpget.php?key="+key;

            try {
                String SetServerString = "";

                // create request to server and get response
                HttpGet httpget = new HttpGet(url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                //SetServerString = Client.execute(httpget, responseHandler);

                HttpResponse resp = Client.execute(httpget);
                String body = responseHandler.handleResponse(resp);

                response = body;

                // response = SetServerString;
            } catch (Exception ex) {
                response = "fail1";
            }
        } catch (UnsupportedEncodingException e) {
            response = "fail";
        }

        return response;
    }*/
}