package com.example.dw.helloword;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button mBtnCreateFile,mBtnCreateDir,mBtnDelete,mBtnDeleteDir;
    private Button mBtnCreateDB,mBtnDeleteDB;

    private Button mBtnWrite,mBtnRead;
    private Button mBtnChooseFile;
    private Button mBtnReadAndShowContacts;
    private TextView mTvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnCreateDir=(Button)findViewById(R.id.btn_two);
        mBtnCreateFile=(Button)findViewById(R.id.btn_one);
        mBtnDelete=(Button)findViewById(R.id.btn_three);
        mBtnDeleteDir=(Button)findViewById(R.id.btn_four);

        mBtnCreateDB=(Button)findViewById(R.id.btn_create_db);
        mBtnDeleteDB=(Button)findViewById(R.id.btn_delete_db);

        mBtnWrite=(Button)findViewById(R.id.btn_write_to_file);
        mBtnRead=(Button)findViewById(R.id.btn_read_file);
        mBtnChooseFile=(Button)findViewById(R.id.btn_choose_file);
        mBtnReadAndShowContacts=(Button)findViewById(R.id.btn_read_show_contacts);


        mTvResult=(TextView)findViewById(R.id.tv_check_result) ;

        mBtnCreateFile.setOnClickListener(this);
        mBtnCreateDir.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnDeleteDir.setOnClickListener(this);
        mBtnDeleteDB.setOnClickListener(this);
        mBtnCreateDB.setOnClickListener(this);




        mBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,WriteFileActivity.class);
                startActivity(intent);

            }
        });

        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,ReadFileActivity.class);
                startActivityForResult(intent,1);
            }
        });


        mBtnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mp3");
                Intent it=Intent.createChooser(intent,"文件包选择");
                startActivityForResult(it,1102);
            }
        });

        mBtnReadAndShowContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,ContactsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null) {
            if (resultCode == 11) {
                String str = data.getStringExtra("result");
                System.out.println(str);
                mBtnRead.setText("!!!");
            } else {
                Uri uri = data.getData();
                String str = uri.toString();
                mTvResult.setText(str);

            }
        }

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        SQLiteDatabase db=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            System.out.println("have sd!");
        }
        File file= Environment.getExternalStorageDirectory();
        String path=file.getAbsolutePath();

        File[] files=file.listFiles();
        int n=files.length;
        for(int i=0;i<n;i++){
            System.out.println(files[i].getName());
        }
        switch(id){
            case R.id.btn_one:
                String pathname=path+File.separator+"good.txt";
                System.out.println(pathname);
                File newFile=new File(pathname);
                try {
                    boolean is=newFile.createNewFile();
                    if(is){
                        System.out.println("success");
                    }
                    else {
                        System.out.println("not success");
                    }
                }catch (IOException exception) {
                    System.out.println("exception!");
                }
                break;
            case R.id.btn_two:
                String dirName="good";
                File newDir=new File(path,dirName);
                boolean is=newDir.mkdir();
                if(is){
                    System.out.println("success");
                }
                else{
                    System.out.println("unseccess!");
                }
                break;
            case R.id.btn_three:
                File delfile=new File(path,"lhc.txt");
                if(delfile.exists()){
                    delfile.delete();
                }
                else{
                    System.out.println(" not Exist");
                }
                break;

            case R.id.btn_four:
                File delDir=new File(path,"lhc");
                if(delDir.exists()){
                    delDir.delete();
                }
                else{
                    System.out.println("not exists!");
                }
                break;

            case R.id.btn_create_db:
                db=openOrCreateDatabase("lhc.db",MODE_PRIVATE,null);
                String createTable="create table if not exists one(name varchar,age int) ";
                db.execSQL(createTable);
                ContentValues values=new ContentValues();
                values.put("name","lhc");
                values.put("age","21");
                db.insert("one",null,values);
                break;
            case R.id.btn_delete_db:
                break;

            default:
        }
    }
}
