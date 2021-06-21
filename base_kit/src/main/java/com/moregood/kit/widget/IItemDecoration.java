package com.moregood.kit.widget;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;


/**
 * Created by Rico on 2019/12/12.
 * item间距设置
 */
public class IItemDecoration extends RecyclerView.ItemDecoration {
    SparseArray<Config> configSparseArray = new SparseArray<>();
    boolean isRlt;

    public IItemDecoration(int top, int left, int right, int bottom) {
        isRlt = isRlt();
        addConfig(top, left, right, bottom);
    }

    public IItemDecoration(int decoration) {
        isRlt = isRlt();
        addConfig(decoration, 0, decoration, 0);
    }

    public IItemDecoration() {
        isRlt = isRlt();
    }

    boolean isRlt() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())
                == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        Config config = configSparseArray.get(position);
        /**the last**/
        if (position == parent.getLayoutManager().getItemCount() - 1 && configSparseArray.get(-101) != null)
            config = configSparseArray.get(-101);
        if (config == null)
            config = configSparseArray.get(-1);
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
        }
    }

    /**
     * 添加配置
     *
     * @param top
     * @param left
     * @param right
     * @param bottom
     */
    public void addConfig(int top, int left, int right, int bottom) {
        Config config = new Config(top, left, right, bottom);
        configSparseArray.put(config.position, config);
    }

    /**
     * 添加配置
     *
     * @param top
     * @param left
     * @param right
     * @param bottom
     * @param position
     */
    public void addConfig(int top, int left, int right, int bottom, int position) {
        Config config = new Config(top, left, right, bottom, position);
        configSparseArray.put(config.position, config);
    }

    /**
     * 设置最后位置的Config
     *
     * @param top
     * @param left
     * @param right
     * @param bottom
     */
    public void setConfigOfLastItem(int top, int left, int right, int bottom) {
        Config config = new Config(top, left, right, bottom, -101);
        configSparseArray.put(config.position, config);
    }

    public class Config {
        public int outRectTop;
        public int outRectLeft;
        public int outRectRight;
        public int outRectBottom;
        public int position = -1;

        public Config(int top, int left, int right, int bottom) {
            this.outRectTop = top;
            this.outRectLeft = left;
            this.outRectRight = right;
            this.outRectBottom = bottom;
        }

        public Config(int top, int left, int right, int bottom, int position) {
            this.outRectTop = top;
            this.outRectLeft = left;
            this.outRectRight = right;
            this.outRectBottom = bottom;
            this.position = position;
        }
    }

    public int getDefaultValue() {
        return configSparseArray.get(-1).outRectTop;
    }

}
