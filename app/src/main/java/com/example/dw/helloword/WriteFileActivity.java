package com.example.dw.helloword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by DW on 2016/7/21.
 */

public class WriteFileActivity extends Activity {

    private TextView mTvPlease;
    private EditText mEtFileContent;
    private Button mBtnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_file);

        mTvPlease=(TextView)findViewById(R.id.tv_please_input);
        mBtnConfirm=(Button)findViewById(R.id.btn_confirm);
        mEtFileContent=(EditText)findViewById(R.id.et_content);

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=mEtFileContent.getText().toString();
                if(content!=null&&content.length()>0){
                    String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"good.txt";
                    try {
                        FileOutputStream fout = new FileOutputStream(path,true);
                        fout.write(content.getBytes());
                        fout.close();
                    }catch (IOException exception){}

                    Intent it=new Intent();
                    it.setClass(WriteFileActivity.this,MainActivity.class);
                    startActivity(it);
                }
            }
        });

    }
}
