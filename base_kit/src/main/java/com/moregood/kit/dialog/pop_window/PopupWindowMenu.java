package com.moregood.kit.dialog.pop_window;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moregood.kit.R;
import com.moregood.kit.base.BaseApplication;
import com.moregood.kit.base.OnItemClickedListener;


/**
 * description :
 * author : yexifeng
 * email : ye_xi_feng@163.com
 * date : 2019/6/6 09:06
 */
public class PopupWindowMenu extends PopupWindow {

    public PopupWindowMenu(Activity context, int menu, OnItemClickedListener<Integer> listener, BindViewCallback callback) {
        super(context);
        setContentView(LayoutInflater.from(context).inflate(R.layout.popup_window_menu, null));
        RecyclerView recyclerView = getContentView().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context );
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(new NormalAdapter(parseMenu(context,menu),listener,callback));
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        setOutsideTouchable(true);
    }
    public PopupWindowMenu(Activity context, int menu, OnItemClickedListener<Integer> listener) {
        this(context,menu,listener,null);
    }

    private Menu parseMenu(Activity context, int menuId){
        MenuBuilder menu = new MenuBuilder(context);
        MenuInflater menuInflater = context.getMenuInflater();
       menuInflater.inflate(menuId,menu);
       return menu;
    }

    public void show(View anchor, int xoff, int yoff){
        showAsDropDown(anchor, -100, 0);
        bgAlpha(BaseApplication.getInstance().getLifecycleCallbacks().current(),0.7f);
    }


    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView title;
        public final TextView line;
        private final ImageView icon;

        public VH(View v) {
            super(v);
            title = v.findViewById(R.id.tvTitle);
            line = v.findViewById(R.id.tvLine);
            icon = v.findViewById(R.id.ivIcon);
        }
    }

    static class NormalAdapter extends RecyclerView.Adapter<VH>{
        private BindViewCallback mBindViewCallback;
        private final Menu menu;
        private final OnItemClickedListener<Integer> itemClickListener;



        public NormalAdapter(Menu menu, OnItemClickedListener<Integer> onItemClickedListener, BindViewCallback callback) {
            this.menu = menu;
            this.itemClickListener = onItemClickedListener;
            mBindViewCallback = callback;
        }

        //③ 在Adapter中实现3个方法
        @Override
        public void onBindViewHolder(VH holder, final int position) {
            final MenuItem menuItem = menu.getItem(position);
            holder.title.setText(menuItem.getTitle());
            if(mBindViewCallback!=null){
                mBindViewCallback.onBindView(holder,position);
            }
            holder.icon.setImageDrawable(menuItem.getIcon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(itemClickListener!=null){
                       itemClickListener.onItemClicked(v,menuItem.getItemId(),position);
                   }
                }
            });
            if(position==menu.size()-1){
                holder.line.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return menu.size();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_window_menu_item, parent, false);
            return new VH(v);
        }
    }

    public interface BindViewCallback{
        void onBindView(VH viewHolder, int position);
    }

    public void bgAlpha(Activity context, float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        bgAlpha(BaseApplication.getInstance().getLifecycleCallbacks().current(),1.0f);
    }
}
