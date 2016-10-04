package com.example.wladek.wira.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeActivity extends Activity implements ZXingScannerView.ResultHandler {

//    SurfaceView scanningView;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    //    BarCodeProcessor barCodeProcessor;
    final int QRCODE_RESULT = 567;
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

//        scanningView = (SurfaceView) findViewById(R.id.scanningView);

//        barcodeDetector = new BarcodeDetector.Builder(this)
//                .setBarcodeFormats(Barcode.QR_CODE)
//                .build();
//
//        cameraSource = new CameraSource
//                .Builder(this, barcodeDetector)
//                .setAutoFocusEnabled(true)
//                .setRequestedPreviewSize(800, 800)
//                .setRequestedFps(new Float(1125))
//                .build();

//        barCodeProcessor = new BarCodeProcessor();

//        scanningView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(BarCodeActivity.this,
//                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    cameraSource.start(scanningView.getHolder());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                cameraSource.stop();
//            }
//        });

//        barcodeDetector.setProcessor(barCodeProcessor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        //Handle result here
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(" SCAN RESULT");
//        builder.setMessage(result.getText());
//        AlertDialog dialog = builder.create();
//        dialog.show();
        Intent intent = new Intent();
        intent.putExtra("qrCode", result.getText());//Returns value at zero
        setResult(CommonStatusCodes.SUCCESS, intent);
        mScannerView.stopCamera();
        finish();
    }

//    class BarCodeProcessor implements Detector.Processor {
//
//        @Override
//        public void release() {
//
//        }
//
//        @Override
//        public void receiveDetections(Detector.Detections detections) {
//            final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
//
//            if (barcodeSparseArray.size() > 0){
//                Intent intent = new Intent();
//                intent.putExtra("qrCode", barcodeSparseArray.valueAt(0).displayValue);//Returns value at zero
//                setResult(CommonStatusCodes.SUCCESS, intent);
//                finish();
//            }
//        }
//    }
}
