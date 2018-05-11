package com.example.eric.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eric.Http.ApiHelper;
import com.example.eric.Http.ApiSubscriber;
import com.example.eric.Http.ApiType;
import com.example.eric.Http.RtHttp;
import com.example.eric.Http.Translation;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class BookShelfActivity extends AppCompatActivity {

    TextView mTitle_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf);

        mTitle_tv = (TextView)findViewById(R.id.text_vw);

        HashMap map = new HashMap();
        map.put("key","value");

        RtHttp.with(this).startTask(ApiType.AppConfig,null);
    }

    private void showText(String text) {
        mTitle_tv.setText(text);
    }
}
