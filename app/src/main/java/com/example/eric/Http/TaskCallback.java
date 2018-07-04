package com.example.eric.Http;

/**
 * Created by Eric on 2018/7/3.
 */

public interface TaskCallback {

    void taskStart(Object tag);

    void taskFinish(Object tag, ResultModel result);
}
