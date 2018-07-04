package com.example.eric.base;

/**
 * Created by Eric on 2018/6/24.
 */

public interface BasePresenter  {

    // 可以用来执行从Model中获取数据，并调用View接口显示
    void startTask();

    // 数据加载完成
    void finishTask(BaseModel result);
}
