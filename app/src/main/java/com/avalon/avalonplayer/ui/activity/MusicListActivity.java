package com.avalon.avalonplayer.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import com.avalon.avalonplayer.callback.RecyclerViewItemOnClickListener;
import com.avalon.avalonplayer.data.MusicItemData;
import com.avalon.avalonplayer.databinding.ActivityMusiclistBinding;
import com.avalon.avalonplayer.db.MusicInfo;
import com.avalon.avalonplayer.service.PlayService;

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
        mAdapter = new MusicListAdapter(this,musicItemDatas,listener);
        mBinding.rvMusicList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvMusicList.setAdapter(mAdapter);
        getMusicList();
    }

    RecyclerViewItemOnClickListener listener = new RecyclerViewItemOnClickListener() {
        @Override
        public void onClick(int position) {
            showToast(musicItemDatas.get(position).getUrl());
            Intent intent = new Intent(MusicListActivity.this, PlayService.class);
            Bundle bundle = new Bundle();
            bundle.putString("music_url",musicItemDatas.get(position).getUrl());
            intent.putExtras(bundle);
            startService(intent);
        }
    };

    private void getMusicList() {
        RealmResults<MusicInfo> realmResults = realm.where(MusicInfo.class).findAll();
        for (MusicInfo m : realmResults) {
            Log.d("wqq", m.getSongName() + " " + m.getSingerName());
            MusicItemData data = new MusicItemData(m.getSongName(), m.getSingerName());
            data.setUrl(m.getUrl());
            musicItemDatas.add(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

        public MusicListAdapter(Context context, List<MusicItemData> list,RecyclerViewItemOnClickListener listener) {
            this.context = context;
            this.list = list;
            this.mListener = listener;
        }

        private Context context;
        private List<MusicItemData> list;
        private RecyclerViewItemOnClickListener mListener;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_musiclist, parent, false);
            ViewHolder holder = new ViewHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.getBinding().setVariable(BR.music, list.get(position));
            holder.getBinding().executePendingBindings();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(position);
                }
            });
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
