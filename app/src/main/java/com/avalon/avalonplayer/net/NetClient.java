package com.avalon.avalonplayer.net;

import android.util.Log;

import com.avalon.avalonplayer.data.TestData;
import com.avalon.avalonplayer.net.service.GithubService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.inject.Singleton;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by wuqiuqiang on 2017/1/22.
 */
@Singleton
public class NetClient {

    Retrofit retrofit;

    GithubService githubService;

    private Retrofit getRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(new OkHttpClient())
                    .baseUrl(NetContrat.TEST_API)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private GithubService getGithubService(){
        if (githubService == null){
            githubService = getRetrofit().create(GithubService.class);
        }
        return githubService;
    }

    public Observable<TestData> getNetWork(){
        Hashtable<String,String> param = new Hashtable<>();
        param.put("channel","wdj");
        param.put("version","4.0.2");
        param.put("uuid","ffffffff-a90e-706a-63f7-ccf973aae5ee");
        param.put("platform","android");
        return getGithubObserable(TestData.class,param);
    }

    private <T> Observable<T> getGithubObserable(Type mType,Hashtable<String, String> hashtable) {
        return Observable.create(new BaseObservable<T>(mType,getGettData(hashtable)));
    }


    private Observable<String> getGettData(Hashtable<String, String> hashtable) {
        if (hashtable == null)
            hashtable = new Hashtable<>();
        String data = getData(hashtable);
        return getGithubService().getIdList(data);
    }

    private String getData(Hashtable<String, String> hashtable) {
        StringBuilder newvalue = new StringBuilder();
        if (hashtable != null) {
            boolean first = true;
            Enumeration enume = hashtable.keys();
            while (enume.hasMoreElements()) {
                String key = (String) enume.nextElement();
                String value = hashtable.get(key);
                if (first) {
                    first = false;
                } else {
                    newvalue.append('&');
                }
                newvalue.append(key);
                newvalue.append("=");
                newvalue.append(value);
            }
            Log.i("function", "NEWVALUE " + newvalue);
        } else {
            newvalue.append("");
        }
        return newvalue.toString();
    }

}
