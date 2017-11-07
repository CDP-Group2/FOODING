package com.fooding.userapp.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fooding.userapp.FoodingApplication;
import com.fooding.userapp.R;
import com.fooding.userapp.data.Food;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.EnumMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendOutQRActivity extends AppCompatActivity {
    @BindView(R.id.QRView) ImageView QRimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_out_qr);
        ButterKnife.bind(this);
        String codeString = getIntent().getStringExtra("Code");
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = barcodeWriter.encode(codeString, BarcodeFormat.QR_CODE,1000,1000);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            QRimage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Food food = FoodingApplication.getInstance().getCurrentFood();
        //사용방법은 카메라 액티비티 참고
    }
}
