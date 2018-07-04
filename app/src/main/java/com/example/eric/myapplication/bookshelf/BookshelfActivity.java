package com.example.eric.myapplication.bookshelf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.eric.Http.ResultModel;
import com.example.eric.base.BaseActivity;
import com.example.eric.myapplication.R;

/**
 * Created by Eric
 */

public class BookshelfActivity extends BaseActivity implements BookshelfContract.View  {
    /*
    包含的对象： view、presenter、module

    1.绑定
    2.开始业务处理 比如 要获取数据
    3. M 获取数据完成  通知UI层
     */
    private BookshelfContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);

        /*
        A在构造的时候 传入B
        B有一个函数将 A绑定给B
         */
        mPresenter = new BookshelfPresenter(this);

        Button button = (Button)findViewById(R.id.start_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartButton();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelTask(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onStartButton() {
        // 我要开始加载数据
        // 问 P 拿数据
        //mPresenter.start();
        mPresenter.loadBookshelfData();
    }

    @Override
    public void setPresenter(BookshelfContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void startLoadData(Object tag) {
        if (tag.equals(BookshelfPresenter.MoviceTop250_TaskTag)) {

        }
    }

    @Override
    public void loadDataFinish(Object tag, ResultModel result) {

    }
}
