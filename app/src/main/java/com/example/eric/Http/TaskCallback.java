package com.example.eric.Http;

/**
 * Created by Eric
 */

public interface TaskCallback {

    void taskStart(Object tag);

    void taskFinish(Object tag, ResultModel result);
}
