package com.khadim.audioplayer;

import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {

    private Cursor cursor;

    public SongAdapter(Cursor cursor) {
        this.cursor =cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
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
    public View getView(int position, View v, ViewGroup parent) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lstlayout,null);
        TextView tname= v.findViewById(R.id.tvname);
        cursor.moveToPosition(position);
        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        tname.setText(name);
        return v;
    }
}
