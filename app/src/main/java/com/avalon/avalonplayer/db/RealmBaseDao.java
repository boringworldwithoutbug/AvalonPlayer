package com.avalon.avalonplayer.db;

import org.json.JSONArray;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class RealmBaseDao {

    private Realm realm;

    public RealmBaseDao(Realm realm) {
        this.realm = realm;
    }

    /**
     * 添加(性能优于下面的saveOrUpdate（）方法)
     *
     * @param object
     * @return 保存或者修改是否成功
     */
    public boolean insert(RealmObject object) {
        try {
            realm.beginTransaction();
            realm.insert(object);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加(性能优于下面的saveOrUpdateBatch（）方法)
     *
     * @param list
     * @return 批量保存是否成功
     */
    public boolean insert(List<? extends RealmObject> list) {
        try {
            realm.beginTransaction();
            realm.insert(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加或者修改(性能优于下面的saveOrUpdate（）方法)
     *
     * @param object
     * @return 保存或者修改是否成功
     */
    public boolean insertOrUpdate(RealmObject object) {
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(object);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加或者修改(性能优于下面的saveOrUpdateBatch（）方法)
     *
     * @param list
     * @return 保存或者修改是否成功
     */
    public boolean insertOrUpdateBatch(List<? extends RealmObject> list) {
        try {
            realm.beginTransaction();
            realm.insertOrUpdate(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 添加或者修改
     *
     * @param object
     * @return 已经保存的对象
     */
    public <E extends RealmObject> E saveOrUpdate(E object) {
        E savedE = null;
        try {
            realm.beginTransaction();
            savedE = realm.copyToRealmOrUpdate(object);
            realm.commitTransaction();
            return savedE;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return savedE;
        }
    }

    /**
     * 批量添加或者修改
     *
     * @param list
     * @return 全部保存成功 或 全部失败
     */
    public boolean saveOrUpdateBatch(List<? extends RealmObject> list) {
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }


    /**
     * save or update RealmObject from json data
     *
     * @param jsonObject json数据
     * @param clazz      具体类型
     * @return 已经保存的对象
     */
    public RealmObject saveOrUpdateFromJSON(Class<? extends RealmObject> clazz, String jsonObject) {
        RealmObject RealmObject = null;
        try {
            realm.beginTransaction();
            RealmObject = realm.createOrUpdateObjectFromJson(clazz, jsonObject);
            realm.commitTransaction();
            return RealmObject;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return RealmObject;
        }
    }

    /**
     * batch save or update from json array
     *
     * @param json  json数组
     * @param clazz 类型
     * @return 批量保存json对象是否成功
     */
    public boolean saveOrUpdateFromJSONBatch(Class<? extends RealmObject> clazz, JSONArray json) {
        try {
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(clazz, json);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 删除当前表中所有数据
     *
     * @param clazz
     * @return
     */
    public boolean deleteAll(Class<? extends RealmObject> clazz) {
        try {
            realm.beginTransaction();
            realm.delete(clazz);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 按照id删除制定的记录
     *
     * @param clazz       类型
     * @param idFieldName id字段的名称
     * @param id          id字段值
     * @return
     */
    public boolean deleteById(Class<? extends RealmObject> clazz, String idFieldName, int id) {
        try {
            realm.beginTransaction();
            realm.where(clazz).equalTo(idFieldName, id).findAll().deleteFirstFromRealm();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }

    /**
     * 查询所有
     *
     * @return 返回结果集合
     */
    public RealmResults<? extends RealmObject> findAll(Class<? extends RealmObject> clazz) {
        return realm.where(clazz).findAll();
    }

    /**
     * 清空数据库
     *
     * @return
     */
    public boolean clearDatabase() {
        try {
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }
}
