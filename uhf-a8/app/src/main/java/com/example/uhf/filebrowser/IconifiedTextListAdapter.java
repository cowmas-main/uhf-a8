package com.example.uhf.filebrowser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

//使用BaseAdapter来存储取得的文件
public class IconifiedTextListAdapter extends BaseAdapter {
    private Context mContext = null;
    // 用于显示文件的列表
    private List<com.example.uhf.filebrowser.IconifiedText> mItems = new ArrayList<com.example.uhf.filebrowser.IconifiedText>();

    public IconifiedTextListAdapter(Context context) {
        mContext = context;
    }

    // 添加一项（一个文件）
    public void addItem(com.example.uhf.filebrowser.IconifiedText it) {
        mItems.add(it);
    }

    // 设置文件列表
    public void setListItems(List<com.example.uhf.filebrowser.IconifiedText> lit) {
        mItems = lit;
    }

    // 得到文件的数目,列表的个数
    public int getCount() {
        return mItems.size();
    }

    // 得到一个文件
    public Object getItem(int position) {
        return mItems.get(position);
    }

    // 能否全部选中
    public boolean areAllItemsSelectable() {
        return false;
    }

    // 判断指定文件是否被选中
    public boolean isSelectable(int position) {
        return mItems.get(position).isSelectable();
    }

    // 得到一个文件的ID
    public long getItemId(int position) {
        return position;
    }

    // 重写getView方法来返回一个IconifiedTextView（我们自定义的文件布局）对象
    public View getView(int position, View convertView, ViewGroup parent) {
        com.example.uhf.filebrowser.IconifiedTextView btv;
        if (convertView == null) {
            btv = new com.example.uhf.filebrowser.IconifiedTextView(mContext, mItems.get(position));
        } else {
            btv = (com.example.uhf.filebrowser.IconifiedTextView) convertView;
            btv.setText(mItems.get(position).getText());
            btv.setIcon(mItems.get(position).getIcon());
        }
        return btv;
    }
}
