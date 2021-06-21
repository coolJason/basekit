package com.moregood.kit.widget;

import android.view.View;

/**
 * @author Rico.lo
 * @date 2021/1/3
 * Description:
 */
public interface OnFoldItemClickListener<S> {

    void onFoldItemClick(View view, S s, int position);
}
