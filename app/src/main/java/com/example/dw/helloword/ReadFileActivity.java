package com.example.dw.helloword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFileActivity extends Activity {

    private TextView mTvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);

        mTvResult=(TextView)findViewById(R.id.tv_result);

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "good.txt";
            InputStreamReader reader = new InputStreamReader(new FileInputStream(path));
            BufferedReader bufferedReader=new BufferedReader(reader);
            StringBuilder str = new StringBuilder();
            str.append(bufferedReader.readLine().toString());
            Intent intent=new Intent();
            intent.putExtra("result",str.toString());
            setResult(11,intent);
            mTvResult.setText(str);
        }catch (IOException exception){}




    }
}
