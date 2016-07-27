package com.example.dw.helloword;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button mBtnCreateFile,mBtnCreateDir, mBtnDeleteFile,mBtnDeleteDir;
    private Button mBtnCreateDB,mBtnDeleteDB;

    private Button mBtnWrite,mBtnRead;
    private Button mBtnChooseFile;
    private Button mBtnReadAndShowContacts;

    private TextView mTvResult;

    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnCreateDir=(Button)findViewById(R.id.btn_two);
        mBtnCreateFile=(Button)findViewById(R.id.btn_one);
        mBtnDeleteFile =(Button)findViewById(R.id.btn_three);
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
        mBtnDeleteFile.setOnClickListener(this);
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
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "good.txt";
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(path));
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder str = new StringBuilder();
                    str.append(bufferedReader.readLine().toString());
                    mTvResult.setText(str);
                }catch (IOException exception){}
            }
        });


        mBtnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String[] items = {"文件", "文档", "图片", "音乐"};
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("请选择文件类型").setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                                Intent it;
                                switch(which){
                                    case 0:break;
                                    case 1:
                                        intent.setType("text/*");
                                        it=Intent.createChooser(intent,"文件包选择");
                                        startActivityForResult(it,1102);
                                        dialog.dismiss();
                                    case 2:
                                        intent.setType("image/*");
                                        it=Intent.createChooser(intent,"文件包选择");
                                        startActivityForResult(it,1102);
                                        dialog.dismiss();
                                    case 3:
                                        intent.setType("audio/mp3");
                                        it=Intent.createChooser(intent,"文件包选择");
                                        startActivityForResult(it,1102);
                                        dialog.dismiss();
                                        break;
                                    default:break;
                                }
                            }
                        }

                ).create();
                dialog.show();
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
                Uri uri = data.getData();
                String str = uri.toString();
                mTvResult.setText("文件路径："+str);

            }
        }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        SQLiteDatabase db = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File file = Environment.getExternalStorageDirectory();
            String path = file.getAbsolutePath();

            File[] files = file.listFiles();
            int n = files.length;
            for (int i = 0; i < n; i++) {
                System.out.println(files[i].getName());
            }

            switch (id) {
                case R.id.btn_one:
                    showDialog("请输入要创建的文件名");
                    String pathname = path + File.separator +name+ ".txt";
                    System.out.println(pathname);
                    File newFile = new File(pathname);
                    try {
                        boolean is = newFile.createNewFile();
                        if (is) {
                           showToast("创建成功");
                        } else {
                          showToast("创建失败");
                        }
                    } catch (IOException exception) {
                        System.out.println("exception!");
                    }
                    break;
                case R.id.btn_two:
                    showDialog("请输入要创建的文件夹");
                    File newDir = new File(path, name);
                    boolean is = newDir.mkdir();
                    if (is) {
                       showDialog("创建成功");
                    } else {
                        showToast("创建失败");
                    }
                    break;
                case R.id.btn_three:
                    showDialog("请输入要删除的文件");
                    File delfile = new File(path, name+".txt");
                    if (delfile.exists()) {
                        boolean ifdel=delfile.delete();
                        if(ifdel){
                            showToast("删除成功");
                        }
                    } else {
                        showToast("文件不存在");
                    }
                    break;

                case R.id.btn_four:
                    showDialog("请输入要删除的文件夹");
                    File delDir = new File(path, name);
                    if (delDir.exists()) {
                        boolean isdel=delDir.delete();
                        if(isdel){
                            showToast("删除成功");
                        }
                    } else {
                        showToast("文件夹不存在");
                    }
                    break;

                case R.id.btn_create_db:
                    db = openOrCreateDatabase("lhc.db", MODE_PRIVATE, null);
                    String createTable = "create table if not exists one (name varchar,age int) ";
                    db.execSQL(createTable);
                    ContentValues values = new ContentValues();
                    values.put("name", "lhc");
                    values.put("age", "21");
                    db.insert("one", null, values);
                    mTvResult.setText("创建数据库lhc.db成功!");
                    db.close();
                    break;
                case R.id.btn_delete_db:
                    showToast("你没有权限删除数据库！！！");
                    break;
                default:
            }
        }
    }

    void showDialog(String str){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        View view= LayoutInflater.from(this).inflate(R.layout.input_dialog,null);
        TextView tvTip=(TextView)view.findViewById(R.id.tv_tips);
        final EditText edtName=(EditText)view.findViewById(R.id.edt_input);
        Button btnComfirm=(Button)view.findViewById(R.id.btn_confirm);
        Button btnCancel=(Button)view.findViewById(R.id.btn_cancel);
        dialog.setView(view);
        dialog.show();
        tvTip.setText(str);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtName.setText("");
                dialog.dismiss();
            }
        });

        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=edtName.getText().toString();
                dialog.dismiss();
            }
        });
    }

    void showToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }


}
