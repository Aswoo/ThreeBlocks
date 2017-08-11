package com.threeblocks.android.threeblocks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by seungwoo on 2017-08-09.
 */

public class URL_fragement extends Fragment{

    final static String TAG = "URL_fragment";

    private RecyclerView mURLRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_url_page,container,false);
        mURLRecyclerView = (RecyclerView) view
                .findViewById(R.id.fragment_list_rv);
        mURLRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //매니저가 없으면 동작하지 않는다.
        updateUI();

        return view;
    }

    private void updateUI() {
    }

    private class URLHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public URLHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class URLAdapter extends RecyclerView.Adapter<URLHolder> {

        @Override
        public URLHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(URLHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
