package miage.fr.gestionprojet;


import android.content.Context;

import com.activeandroid.app.Application;

public class MyApplication extends com.activeandroid.app.Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

}
