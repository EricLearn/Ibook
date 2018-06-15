package com.example.eric.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eric.Http.ApiServer;
import com.example.eric.Http.ApiType;
import com.example.eric.Http.RtHttp;
import com.example.eric.Http.Translation;
import com.example.eric.Model.HandlerCenter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookShelfActivity extends AppCompatActivity implements Handler.Callback,HandlerCenter.Callback {
    
    private final String TAG = "BookShelfActivity";
    
    private final int HANDLERSTART = 1;
    
    TextView mTitle_tv;
    
    // looper  每个thread有一个looper、queue     在该thread创建handler    注册 
    // handler   创建message：handler与message关联   添加到queue     发送
    // message 
    // massagequeue  looper会不断的从queue取                        识别
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            messageHandler(msg);
        }
    };
    
    private Handler mHandler_1 = new Handler(this);

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case HANDLERSTART:

                Log.d(TAG,msg.getData().getString("name") + "接口处理");

                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case HANDLERSTART:

                Log.d(TAG,msg.getData().getString("name") + "中心接口回调处理");

                break;
            default:
                break;
        }
    }

    private void messageHandler(Message msg) {
        switch (msg.what) {
            case HANDLERSTART:
                
                Log.d(TAG,msg.getData().getString("name"));
                
                break;
            default:
                break;
        }
    }

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf);
        
        Button button = findViewById(R.id.start_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laoddata();
            }
        });

        mTitle_tv = (TextView)findViewById(R.id.text_vw);

        Log.d(TAG,"打印日志");

        new Thread() {
            @Override
            public void run() {
            }
        }.start();

       new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = HANDLERSTART;
                msg.getData().clear();
                Bundle bundle = new Bundle();
                bundle.putString("name","大头大头");
                msg.setData(bundle);
                //mHandler.sendMessage(msg);

                //msg = new Message();
                //msg.what = HANDLERSTART;
                //msg.setData(bundle);
                //mHandler_1.sendMessage(msg);
                
                HandlerCenter.center().getHandler().sendMessage(msg);
                
                Handler thread_handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                    }
                };

                Looper looper = thread_handler.getLooper();
                looper.getQueue();
            }
        }.start();
        
       HandlerCenter.center().setCallback(this);
        
    }
    
    private void laoddata() {
        HashMap map = new HashMap();
        map.put("key","value");

        //RtHttp.with(this).startTask(ApiType.AppConfig,null);
        
        //
        // rxjavaHandler();

        rxJavaOperation();
    }

    private void showText(String text) {
        mTitle_tv.setText(text);
    }
    private void subscribeHandler(String str) {
        Log.d(TAG,str);
    }
    
    
    private void rxJavaOperation() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ApiServer request = retrofit.create(ApiServer.class);

        // c. 采用Observable<...>形式 对 网络请求 进行封装
        Observable<Translation> observable = request.getcall();
        
        observable.subscribeOn(Schedulers.io()) // 切换到IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 切换回到主线程 处理请求结果
                .subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG,"开始监听");
                    }

                    @Override
                    public void onNext(Translation translation) {
                        Log.d(TAG,"网络访问完成");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"网络监听完成");
                    }
                });
        
        /*Observable.interval(2,1, TimeUnit.SECONDS).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                
                Log.d(TAG,"重复"+ aLong);
                
                
                observable.subscribe(new Observer<Translation>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation result) {
                        result.show() ;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG,"外部测试");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });*/
    }
    
    private void rxjavaHandler() {
        
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"observers 的使用" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        
        
        Observable<String> myObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                
                e.onNext("rxjava" +
                        "就是这么用的");
                e.onComplete();
            }
        });
        myObservable.subscribe(observer);
    }
}