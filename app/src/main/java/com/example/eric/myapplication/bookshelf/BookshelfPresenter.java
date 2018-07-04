package com.example.eric.myapplication.bookshelf;

import android.util.Log;

import com.example.eric.Http.HttpClient;
import com.example.eric.Http.HttpTypeHelper;
import com.example.eric.Http.ResultModel;
import com.example.eric.Http.TaskCallback;

import io.reactivex.annotations.NonNull;

/**
 * Created by Eric
 */

public class BookshelfPresenter implements BookshelfContract.Presenter,TaskCallback {

    public static final String MoviceTop250_TaskTag = "/v2/movie/top250";

    private static String mBaseUrl = "";

    private int count = 0;

    BookshelfContract.View mView;

    public BookshelfPresenter(@NonNull BookshelfContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadBookshelfData() {
        new HttpClient.Builder()
                .setParams(null)
                .setBodyDataType(HttpTypeHelper.BodyDataType.JSON_OBJECT)
                .setParseClass(MovieListEntity.class)
                .build()
                .start(this,MoviceTop250_TaskTag);
    }

    @Override
    public void deleteBook() {

    }

    @Override
    public void cancelTask() {
        HttpClient.getInstance().cancelAllTask(this);
    }

    @Override
    public void taskStart(Object tag) {
        mView.startLoadData(tag);

        Log.d("BookshelfPresenter","加载数据 :" + count++ + "次");
    }

    @Override
    public void taskFinish(Object tag, ResultModel result) {
        mView.loadDataFinish(tag,result);

        if (result.getError() == null) {
            MovieListEntity entity = (MovieListEntity) result.getResult();

            Log.d("BookshelfPresenter",entity.getTitle());
        }
    }
}
