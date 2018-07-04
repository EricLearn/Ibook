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
    public void cancelTask(Object tag) {

    }

    @Override
    public void taskStart(Object tag) {
        mView.startLoadData(tag);
    }

    @Override
    public void taskFinish(Object tag, ResultModel result) {
        mView.loadDataFinish(tag,result);

        MovieListEntity entity = (MovieListEntity) result.getResult();

        Log.d("BookshelfPresenter",entity.getTitle());

    }
}
