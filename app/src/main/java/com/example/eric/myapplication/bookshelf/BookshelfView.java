package com.example.eric.myapplication.bookshelf;

/**
 * Created by Eric on 2018/6/19.
 */

public interface BookshelfView<T> {

    void setPresenter(BookshelfPresenter presenter);

    void showData();
}
