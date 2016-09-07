package com.example.wladek.wira.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.wladek.wira.R;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarCodeActivity extends Activity {

    SurfaceView scanningView;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    BarCodeProcessor barCodeProcessor;
    final int QRCODE_RESULT = 567;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        scanningView = (SurfaceView) findViewById(R.id.scanningView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(300, 300)
                .setRequestedFps(new Float(1125))
                .build();

        barCodeProcessor = new BarCodeProcessor();

        scanningView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarCodeActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(scanningView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(barCodeProcessor);
    }

    class BarCodeProcessor implements Detector.Processor {

        @Override
        public void release() {

        }

        @Override
        public void receiveDetections(Detector.Detections detections) {
            final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();

            if (barcodeSparseArray.size() > 0){
                Intent intent = new Intent();
                intent.putExtra("qrCode", barcodeSparseArray.valueAt(0).displayValue);//Returns value at zero
                setResult(CommonStatusCodes.SUCCESS, intent);
                finish();
            }
        }
    }
}
