package com.moregood.kit.lottery;

import com.moregood.kit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @类名 LuckData
 * @描述
 * @作者 xifengye
 * @创建时间 2021/3/14 12:55
 * @邮箱 ye_xi_feng@163.com
 */
public class LuckData {
    private int index;
    private int color;
    private String title;
    private int icon;

    public LuckData(int index, int color, String title, int icon) {
        this.index = index;
        this.color = color;
        this.title = title;
        this.icon = icon;
    }

    public int getIndex() {
        return index;
    }

    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public static List<LuckData> demoData(){
        List<LuckData> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            LuckData luckData = new LuckData(i+1,i%2==0?0XFFFFD965: 0XFFFFFFFF,"奖项"+(i+1),i%2==0? R.drawable.ic_facebook: R.drawable.ic_google);
            list.add(luckData);
        }
        return list;
    }
}
