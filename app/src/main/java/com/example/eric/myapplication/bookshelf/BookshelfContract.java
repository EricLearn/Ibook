package com.example.eric.myapplication.bookshelf;
import com.example.eric.Http.ResultModel;
import com.example.eric.base.DataLoadDelegate;
import com.example.eric.base.DataLoadViewDelegate;

/**
 * Created by Eric on 2018/6/24.
 *
 * 将View和Presenter的接口写在一起
 * 能更清晰的知道有哪些功能  方便维护
 *
 * 所以暴露出来的接口 view与model交互 全部亮在这里
 */

public interface BookshelfContract {

    /**
     * 对基础功能进行拓展
     */
    interface View extends DataLoadViewDelegate {

        void startLoadData(Object tag);
        void loadDataFinish(Object tag, ResultModel result);
    }

    interface Presenter extends DataLoadDelegate {

        void loadBookshelfData();
        void deleteBook();

        void cancelTask(Object tag);
    }
}
