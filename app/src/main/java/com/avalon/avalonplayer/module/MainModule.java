package com.avalon.avalonplayer.module;

import android.content.Context;

import com.avalon.avalonplayer.data.Person;
import com.avalon.avalonplayer.net.NetClient;
import com.avalon.avalonplayer.utils.PermissionChecker;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by wuqiuqiang on 2017/1/20.
 */
@Module
public class MainModule {
    @Provides
    Person providerPerson() {
        return new Person();
    }

    @Provides
    NetClient providerNetClient() {
        return new NetClient();
    }

//    @Provides
//    Realm providerRealm() {
//        return Realm.getDefaultInstance();
//    }
}
