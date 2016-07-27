package com.example.dw.helloword;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements RightView.OnTextViewChanges {
    private ListView mLvContacts;
    private List<String> mListNumber;
    private List<Name> mListName;
    private RightView mRightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mLvContacts = (ListView) findViewById(R.id.lv_contacts);
        mRightView = (RightView) findViewById(R.id.rv_one);
        mListName = new ArrayList<>();
        mListNumber = new ArrayList<>();

        ReadAndShowContacts();
        mRightView.setOnTextChanges(this);

    }

    void ReadAndShowContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        SortAccordingFirstLetter(cursor);
        ContactsAdapter adapter = new ContactsAdapter(getApplicationContext(), mListName, mListNumber);
        mLvContacts.setAdapter(adapter);
        cursor.close();
    }

    String ConvertChineseToPinyin(char first) {
        String[] py = null;

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        py = PinyinHelper.toHanyuPinyinStringArray(first);
        if (py != null) {
            return py[0];
        } else {
            return "";
        }


    }

    void SortAccordingFirstLetter(Cursor cursor) {
        int nameIndex=-1;
        int numberIndex=-1;
        if (cursor.getCount() > 0) {
            nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        }

        int count;
        char first = 0;
        Name contactName,emptyName;
        while (cursor.moveToNext()) {
            String number = cursor.getString(numberIndex);
            String name = cursor.getString(nameIndex);

            contactName = new Name();
            count = mListName.size();
            char firstChar;

            if (name != null) {
                firstChar = name.charAt(0);
                if (firstChar >= 65 && firstChar <= 90) {
                    first = String.valueOf(firstChar).toLowerCase().charAt(0);

                } else if (firstChar >= 97 && firstChar <= 122) {
                    first = firstChar;
                } else if (firstChar < 65 || (firstChar > 122 && firstChar < 126) || (firstChar > 90 && firstChar < 97)) {
                    first = 1000;
                } else {
                    String PyName = ConvertChineseToPinyin(firstChar);
                    if (PyName != null && PyName.length() > 0) {
                        first = PyName.charAt(0);
                    }
                }

                contactName.order = first;
                contactName.name = name;

                if (count == 0) {
                    emptyName = new Name();
                    emptyName.order = contactName.order;
                    emptyName.name = "$";
                    mListName.add(emptyName);
                    mListNumber.add("$");
                    mListName.add(contactName);
                    mListNumber.add(number);
                } else {
                    int i;
                    for (i = count - 1; i >= 0; i--) {
                        if (first >= mListName.get(i).order) {
                            mListName.add(i + 1, contactName);
                            mListNumber.add(i + 1, number);
                            if (mListName.get(i).order != contactName.order) {
                                emptyName = new Name();
                                emptyName.order = contactName.order;
                                emptyName.name = "$";
                                mListName.add(i + 1, emptyName);
                                mListNumber.add(i + 1, "$");
                            }
                            break;
                        }
                    }
                    if (i == -1) {
                        mListName.add(0, contactName);
                        mListNumber.add(0, number);
                        emptyName=new Name();
                        emptyName.order = contactName.order;
                        emptyName.name = "$";
                        mListName.add(0, emptyName);
                        mListNumber.add(0, "$");
                    }
                }
            }

        }
    }

    @Override
    public void onTextChange(String ch) {
        int i;
        int first = ch.toLowerCase().charAt(0);
        int count = mListName.size();
        for (i = 0; i < count; i++) {
            if (mListName.get(i).order == first) {
                break;
            }
        }
        if (i != count)
            mLvContacts.setSelection(i);
    }


}

