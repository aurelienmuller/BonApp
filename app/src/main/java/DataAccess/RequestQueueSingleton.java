package DataAccess;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.quentin.bonapp.MyApplication;

/**
 * Created by aurelienmuller on 2/12/15.
 */
public class RequestQueueSingleton {
    private static RequestQueueSingleton mInstance=null;
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