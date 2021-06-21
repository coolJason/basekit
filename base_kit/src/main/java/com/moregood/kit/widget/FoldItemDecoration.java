package com.moregood.kit.widget;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Locale;

/**
 * @author Rico.lo
 * @date 2021/2/4
 * Description:
 */
public class FoldItemDecoration extends RecyclerView.ItemDecoration {
    boolean isRlt;
    int spanCount = 1;
    SparseArray<Config> headerSparseArray = new SparseArray<>();
    SparseArray<Config> itemSparseArray = new SparseArray<>();


    public FoldItemDecoration(int spanCount) {
        isRlt = isRlt();
        this.spanCount = spanCount;
    }

    public FoldItemDecoration() {
        isRlt = isRlt();
    }

    boolean isRlt() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())
                == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        FoldViewAdapter adapter = (FoldViewAdapter) parent.getAdapter();
        int viewType = adapter.getItemViewType(position);
        Config config = null;
        if (viewType == FoldViewAdapter.TYPE_SECTION_HEADER) {
            if (position == parent.getAdapter().getItemCount() - 1 && headerSparseArray.get(-4) != null)
                config = headerSparseArray.get(-4);
            else {
                int sectionPosition = adapter.getSectionPosition(position);
                config = headerSparseArray.get(sectionPosition);
            }
            if (config == null) config = headerSparseArray.get(-1);
        } else if (viewType == FoldViewAdapter.TYPE_ITEM) {
            if (position == parent.getAdapter().getItemCount() - 1 && itemSparseArray.get(-3) != null)
                config = itemSparseArray.get(-3);
            else {
                int itemPosition = adapter.getItemPosition(position);
                int itemCount = adapter.getItemCountForSection(adapter.getSectionPosition(position));
                if (itemPosition == itemCount - 1 && itemSparseArray.get(-2) != null)
                    config = itemSparseArray.get(-2);
                else
                    config = itemSparseArray.get(itemPosition % spanCount);
            }
            if (config == null) config = itemSparseArray.get(-1);
        }
        if (config != null) {
            if (config.outRectTop != 0) outRect.top = config.outRectTop;
            if (config.outRectLeft != 0) {
                if (isRlt)
                    outRect.right = config.outRectLeft;
                else
                    outRect.left = config.outRectLeft;
            }
            if (config.outRectRight != 0) {
                if (isRlt)
                    outRect.left = config.outRectRight;
                else
                    outRect.right = config.outRectRight;
            }
            if (config.outRectBottom != 0) outRect.bottom = config.outRectBottom;
        } else {

        }
    }

    public void addHeaderConfig(int left, int top, int right, int bottom) {
        Config config = new Config(left, top, right, bottom);
        headerSparseArray.put(config.position, config);
    }

    public void addHeaderConfig(int left, int top, int right, int bottom, int position) {
        Config config = new Config(left, top, right, bottom, position);
        headerSparseArray.put(config.position, config);
    }

    public void addItemRelative(int left, int top, int right, int bottom, int position) {
        if (position >= 0) {
            Config config = new Config(left, top, right, bottom, position);
            itemSparseArray.put(config.position, config);
        }
    }

    public void addItemRelative(int left, int top, int right, int bottom) {
        Config config = new Config(left, top, right, bottom);
        itemSparseArray.put(config.position, config);
    }

    public void setItemLastItem(int left, int top, int right, int bottom) {
        Config config = new Config(left, top, right, bottom, -2);
        itemSparseArray.put(config.position, config);
    }

    /**
     * 绝对位置
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setItemAbsolutelyLastItem(int left, int top, int right, int bottom) {
        Config config = new Config(left, top, right, bottom, -3);
        itemSparseArray.put(config.position, config);
    }

    /**
     * 绝对位置
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setHeaderAbsolutelyLastItem(int left, int top, int right, int bottom) {
        Config config = new Config(left, top, right, bottom, -4);
        headerSparseArray.put(config.position, config);
    }

//    public void setHeaderLastConfig(int top, int left, int right, int bottom) {
//        Config config = new Config(left, top, right, bottom, -101);
//        headerSparseArray.put(config.position, config);
//    }

//    public void setItemLastConfig(int top, int left, int right, int bottom) {
//        Config config = new Config(left, top, right, bottom, -101);
//        itemSparseArray.put(config.position, config);
//    }


    public class Config {
        public int outRectTop;
        public int outRectLeft;
        public int outRectRight;
        public int outRectBottom;
        public int position = -1;

        public Config(int left, int top, int right, int bottom) {
            this.outRectTop = top;
            this.outRectLeft = left;
            this.outRectRight = right;
            this.outRectBottom = bottom;
        }

        public Config(int left, int top, int right, int bottom, int position) {
            this.outRectTop = top;
            this.outRectLeft = left;
            this.outRectRight = right;
            this.outRectBottom = bottom;
            this.position = position;
        }
    }

}
