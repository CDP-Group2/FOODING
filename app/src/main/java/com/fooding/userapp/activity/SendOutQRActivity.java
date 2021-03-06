package com.fooding.userapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.HttpConnection;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Food;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SendOutQRActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private HttpConnection httpConn = HttpConnection.getInstance();
    public String responseString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_out_qr);

        sendData(); // 웹 서버로 데이터 전송


    }

    private void sendData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                httpConn.requestWebServer("데이터1","데이터2", callback);
            }
        });
    }

    private final Callback callback = new Callback() {
        private String body;
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "콜백오류:"+e.getMessage());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            body = response.body().string();
            Log.d(TAG, "서버에서 응답한 Body:"+body);
        }
    };
}
