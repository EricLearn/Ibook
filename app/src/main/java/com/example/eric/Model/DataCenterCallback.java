package com.example.eric.Model;

/**
 * Created by Eric on 2018/6/19.
 */

public interface DataCenterCallback <T> {

    void onResultLoaded(T result);

    void onResultFault(Errors error);
}
