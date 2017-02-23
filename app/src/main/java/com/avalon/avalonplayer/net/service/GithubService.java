package com.avalon.avalonplayer.net.service;

import com.avalon.avalonplayer.data.Repo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wuqiuqiang on 2017/1/23.
 */

public interface GithubService {

    //idlist/?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);

    @GET("idlist/")
    Observable<String> getIdList(@Query("data") String data);


}
