package com.avalon.avalonplayer.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wuqiuqiang on 2017/1/20.
 */

public class Person {

    public Person() {
        Log.i("person construct","person");
    }

    public void sayHello(Context context){
        Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();
    }
}
