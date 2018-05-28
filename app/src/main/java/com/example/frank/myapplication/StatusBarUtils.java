package com.example.frank.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

/**
 * Created by Frank on 2018/5/28.
 */

public class StatusBarUtils {


    /**
     * 绘制状态栏
     *
     * @param act
     * @param color
     */
    public static void drawableStatusBar(Activity act, int color) {
        transparentStatusBar(act, true);
        addStatusBar(act, color);
    }

    /**
     * 透明化状态栏
     *
     * @param act
     */
    public static void transparentStatusBar(Activity act, boolean fitWindow) {
        Window window = act.getWindow();

        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //获取根布局
        ViewGroup root = (ViewGroup) act.findViewById(android.R.id.content);
        View firstChild = root.getChildAt(0);
        if (firstChild != null) {
            //设置 root 的第一个子 View . 使其为系统 View 预留空间.
            firstChild.setFitsSystemWindows(fitWindow);
        }
    }

    /**
     * 添加状态栏
     *
     * @param act
     * @param color
     */
    public static void addStatusBar(Activity act, int color) {
        int resource = act.getResources().getColor(color);
        //创建状态栏
        View statusBar = createStatusBar(act, resource);

        //获取根布局
        ViewGroup root = (ViewGroup) act.findViewById(android.R.id.content);
        //将状态栏添加到根布局下的第一个子布局位置
        root.addView(statusBar, 0);
    }

    /**
     * 创建一个状态栏
     *
     * @param context
     * @param color
     * @return
     */
    public static View createStatusBar(Context context, int color) {

        //方法原理:
        //添加一个和状态栏高、宽相同的指定颜色的View来覆盖被透明化的状态栏
        View statusBar = new View(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(context));

        statusBar.setLayoutParams(params);
        statusBar.setBackgroundColor(color);

        return statusBar;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;

        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            //通过反射获取状态栏高度这个成员变量
            Field status_bar_height = clazz.getField("status_bar_height");

            Object obj = clazz.newInstance();
            int height = Integer.parseInt(status_bar_height.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusBarHeight;
    }

}
