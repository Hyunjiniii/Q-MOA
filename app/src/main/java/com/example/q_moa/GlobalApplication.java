package com.example.q_moa;

import android.app.Activity;
import android.app.Application;

import com.example.q_moa.login.kakao.KakaoSDKAdapter;
import com.google.firebase.FirebaseApp;
import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {
    private static volatile GlobalApplication instance = null;
    private static volatile Activity currentActivity = null;
    private static GlobalApplication self;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());

        self = this;
        FirebaseApp.initializeApp(this);
//        KakaoSDK.init(new KakaoAdapter() {
//            @Override
//            public IApplicationConfig getApplicationConfig() {
//                return new IApplicationConfig() {
//                    @Override
//                    public Activity getTopActivity() {
//                        return null;
//                    }
//
//                    @Override
//                    public Context getApplicationContext() {
//                        return self;
//                    }
//                };
//            }
//        });
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
