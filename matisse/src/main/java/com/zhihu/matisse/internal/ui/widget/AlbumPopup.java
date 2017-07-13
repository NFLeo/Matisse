package com.zhihu.matisse.internal.ui.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.zhihu.matisse.R;
import com.zhihu.matisse.popwindow.BasePopupWindow;

public class AlbumPopup extends BasePopupWindow implements View.OnClickListener
{
    private ListView listView;
    private OnItemClickListener onItemClickListener;
    private View masker;

    public AlbumPopup(Activity context, BaseAdapter adapter) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        masker = findViewById(R.id.masker);
        masker.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (onItemClickListener != null) onItemClickListener.onItemClick(adapterView, view, position, l);
            }
        });
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public void showPopupWindow(View v) {
        setOffsetY(175);
        super.showPopupWindow(v);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.pop_folder);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_container);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setSelection(int selection) {
        listView.setSelection(selection);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> adapterView, View view, int position, long l);
    }
}