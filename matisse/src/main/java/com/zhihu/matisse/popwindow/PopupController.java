package com.zhihu.matisse.popwindow;

/**
 * Created by 大灯泡 on 2017/1/13.
 */

interface PopupController {
    boolean onBeforeDismiss();
    boolean callDismissAtOnce();
}