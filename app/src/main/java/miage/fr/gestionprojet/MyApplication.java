package miage.fr.gestionprojet;


import android.content.Context;
import android.os.StrictMode;

import com.activeandroid.app.Application;

public class MyApplication extends com.activeandroid.app.Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public static Context getContext() {
        return mContext;
    }

}
