package com.webnexs.tranznexsdriver.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.webnexs.tranznexsdriver.Banner;
import com.webnexs.tranznexsdriver.R;

import java.util.ArrayList;

public class FlipperAdapter extends BaseAdapter {

    private Context mCtx;
    private ArrayList<Banner> banners;


    public FlipperAdapter(Context mCtx, ArrayList<Banner> banners){
        this.mCtx = mCtx;
        this.banners = banners;
    }
    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Banner banner = banners.get(position);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.flipper_items, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(mCtx).load(banner.getImage()).into(imageView);
        return view;
    }
}

