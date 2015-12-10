package DataAccess;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bonapp.app.MyApplication;

/**
 * Created by aurelienmuller on 2/12/15.
 */
public class RequestQueueSingleton {
    private static RequestQueueSingleton mInstance = null;
    private RequestQueue mRequestQueue;

    private RequestQueueSingleton(){
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    public static RequestQueueSingleton getInstance() {
        if(mInstance==null) {
            mInstance = new RequestQueueSingleton();
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

}