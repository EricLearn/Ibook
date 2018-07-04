package com.example.eric.base;

/**
 * Created by Eric on 2018/6/24.
 */

public interface BaseView<T> {

    void  setPresenter(T presenter);

    // 开始加载数据UI回调
    void  startLoad();

    // 加载完成的UI回调
    void  loadFinish(BaseModel result);
}
