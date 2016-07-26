package com.example.dw.helloword;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private ListView mLvContacts;
    private List<String> mListNumber;
    private List<Name> mListName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mLvContacts=(ListView)findViewById(R.id.lv_contacts);
        mListName=new ArrayList<>();
        mListNumber=new ArrayList<>();

        ReadAndShowContacts();
    }

    void ReadAndShowContacts(){
        ContentResolver resolver=getContentResolver();
        Cursor cursor=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        SortAccordingFirstLetter(cursor);
        ContactsAdapter adapter=new ContactsAdapter(getApplicationContext(),mListName,mListNumber);
        mLvContacts.setAdapter(adapter);
        cursor.close();



    }

    String ConvertChineseToPinyin(char first){
        String[] py=null;

       HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
       format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
       format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        py = PinyinHelper.toHanyuPinyinStringArray(first);


        if(py!=null) {
            return py[0];
        }
        else
        {
            return "";
        }


    }

    void SortAccordingFirstLetter(Cursor cursor) {
        int nameIndex = -1, numberIndex = -1;
        if (cursor.getCount() > 0) {
            nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        }

        int count;
        while (cursor.moveToNext()) {
            String number = cursor.getString(numberIndex);
            String name = cursor.getString(nameIndex);
            Name contactName = new Name();
            if (name != null) {
                String PyName = ConvertChineseToPinyin(name.charAt(0));
                if (PyName != null && PyName.length() > 0) {
                    char first = PyName.charAt(0);
                    contactName.order = first;
                    contactName.name = name;
                    count = mListName.size();
                    if (count == 0) {
                        mListName.add(contactName);
                        mListNumber.add(number);
                        Name emptyName = new Name();
                        emptyName.order = contactName.order;
                        emptyName.name = "#";
                        mListName.add(0,emptyName);
                        mListNumber.add(0,"#");
                    } else {
                        for (int i = count - 1; i >= 0; i--) {
                            if (first >= mListName.get(i).order) {
                                mListName.add(i + 1, contactName);
                                mListNumber.add(i + 1, number);
                                if (mListName.get(i).order != contactName.order) {
                                    Name emptyName = new Name();
                                    emptyName.order = contactName.order;
                                    emptyName.name = "#";
                                    mListName.add(i+1, emptyName);
                                    mListNumber.add(i+1, "#");
                                }
                                break;
                            }
                        }
                    }
                }


            }

        }
    }


}