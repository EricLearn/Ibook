package com.example.eric.base;

/**
 * Created by Eric on 2018/6/26.
 */

public class TaskActivity extends BaseActivity implements BasePresenter {

    boolean isError;
    @Override
    public void startTask() {

    }

    @Override
    public void finishTask(BaseModel result) {
        if (result.getError() != null) {
            isError = true;
        }
    }
}
