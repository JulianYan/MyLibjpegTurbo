package com.github.blizz2night.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.libjpegturbo.turbojpeg.TJ;
import org.libjpegturbo.turbojpeg.TJCompressor;
import org.libjpegturbo.turbojpeg.TJException;
import org.libjpegturbo.turbojpeg.YUVImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);


    }

    @Override
    protected void onResume() {
        super.onResume();
        TJCompressor tjc = null;

        try (InputStream inputStream = getAssets().open("I420_6144x8192.yuv")){
            int available = inputStream.available();
            Log.i(TAG, "run: available=" + available);
            byte[] yuvBuf = new byte[available];
            inputStream.read(yuvBuf);
            tjc = new TJCompressor();
            tjc.setJPEGQuality(95);
            int width = 6144;
            int height = 8192;
            YUVImage yuvImage = new YUVImage(yuvBuf, width, 1, height, TJ.SAMP_420);
            tjc.setSourceImage(yuvImage);
            int bufSize = TJ.bufSize(width, height, TJ.SAMP_420);
            Log.i(TAG, "run: bufSize=" + bufSize);
            byte[] dstBuf = new byte[bufSize];
            Log.i(TAG, "compress: +++");
            tjc.compress(dstBuf, 0);
            Log.i(TAG, "compress: ---");
            tjc.close();
            File cacheDir = getCacheDir();
            File file = new File(cacheDir, "test.jpeg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(dstBuf);
            fileOutputStream.close();
            /* Write the JPEG image to disk. */
        } catch (IOException e) {
            Log.e(TAG, "run: ", e);
        }

    }
}
