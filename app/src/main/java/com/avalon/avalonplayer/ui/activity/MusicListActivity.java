package com.avalon.avalonplayer.ui.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avalon.avalonplayer.BR;
import com.avalon.avalonplayer.R;
import com.avalon.avalonplayer.data.MusicItemData;
import com.avalon.avalonplayer.databinding.ActivityMusiclistBinding;
import com.avalon.avalonplayer.db.MusicInfo;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class MusicListActivity extends BaseActivity {

    ActivityMusiclistBinding mBinding;
    List<MusicItemData> musicItemDatas;
    MusicListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_musiclist);
        setTitle(getResources().getString(R.string.musiclist_title));
        musicItemDatas = new ArrayList<>();
        mAdapter = new MusicListAdapter(this,musicItemDatas);
        mBinding.rvMusicList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvMusicList.setAdapter(mAdapter);
        getMusicList();
    }

    private void getMusicList() {
        RealmResults<MusicInfo> realmResults = realm.where(MusicInfo.class).findAll();
        for (MusicInfo m : realmResults) {
            Log.d("wqq", m.getSongName() + " " + m.getSingerName());
            musicItemDatas.add(new MusicItemData(m.getSongName(), m.getSingerName()));
        }
        mAdapter.notifyDataSetChanged();
    }

    class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

        public MusicListAdapter(Context context, List<MusicItemData> list) {
            this.context = context;
            this.list = list;
        }

        private Context context;
        private List<MusicItemData> list;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_musiclist, parent, false);
            ViewHolder holder = new ViewHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.getBinding().setVariable(BR.music, list.get(position));
            holder.getBinding().executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewDataBinding getBinding() {
                return binding;
            }

            public void setBinding(ViewDataBinding binding) {
                this.binding = binding;
            }

            private ViewDataBinding binding;

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
