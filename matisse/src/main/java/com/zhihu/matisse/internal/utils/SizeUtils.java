package com.zhihu.matisse.internal.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @desc:   尺寸相关工具类
 * @author: Leo
 * @date:   2016/09/26
 */
public class SizeUtils {

    //设置设计稿的尺寸
    public static int UI_WIDTH = 1280;
    public static int UI_HEIGHT = 720;
    public static float UI_DENSITY = 2;

    private SizeUtils() {
        throw new UnsupportedOperationException("error...");
    }

    /**
     * 描述：dip转换为px.
     * @param context  the context
     * @param dipValue the dip value
     * @return px值
     */
    public static float dp2px(Context context, float dipValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, mDisplayMetrics);
    }

    /**
     * 描述：px转换为dip.
     * @param context the context
     * @param pxValue the px value
     * @return dip值
     */
    public static float px2dp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.density;
    }

    /**
     * 描述：sp转换为px.
     * @param context the context
     * @param spValue the sp value
     * @return sp值
     */
    public static float sp2px(Context context, float spValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mDisplayMetrics);
    }

    /**
     * 描述：px转换为sp.
     * @param context the context
     * @param pxValue the px value
     * @return sp值
     */
    public static float px2sp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.scaledDensity;
    }

    /**
     * 获取屏幕尺寸与密度.
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();
        } else {
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    /**
     * 各种单位转换
     * <p>该方法存在于TypedValue</p>
     * @param unit    单位
     * @param value   值
     * @param metrics DisplayMetrics
     * @return 转换结果
     */
    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    public static int scaleValue(Context context, float value) {
        DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);

        int width = mDisplayMetrics.widthPixels;
        int height = mDisplayMetrics.heightPixels;

        if (width > height) {
            width = mDisplayMetrics.heightPixels;
            height = mDisplayMetrics.widthPixels;
        }
        if (mDisplayMetrics.scaledDensity >= UI_DENSITY) {
            if (width > UI_WIDTH)
                value *= (1.3F - 1.0F / mDisplayMetrics.scaledDensity);
            else if (width < UI_WIDTH)
                value *= (1.0F - 1.0F / mDisplayMetrics.scaledDensity);
        } else {
            float offset = UI_DENSITY - mDisplayMetrics.scaledDensity;
            if (offset > 0.5F)
                value *= 0.9F;
            else {
                value *= 0.95F;
            }
        }

        return scale(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, value);
    }

    public static int scale(int displayWidth, int displayHeight, float pxValue) {
        if (pxValue == 0.0F) {
            return 0;
        }
        float scale = 1.0F;
        try {
            int width = displayWidth;
            int height = displayHeight;

            if (width > height) {
                width = displayHeight;
                height = displayWidth;
            }
            float scaleWidth = width / UI_WIDTH;
            float scaleHeight = height / UI_HEIGHT;
            scale = Math.min(scaleWidth, scaleHeight);
        } catch (Exception localException) {
        }
        return Math.round(pxValue * scale + 0.5F);
    }

    public static void setViewSize(View view, int widthPixels, int heightPixels) {
        int scaledWidth = scaleValue(view.getContext(), widthPixels);
        int scaledHeight = scaleValue(view.getContext(), heightPixels);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            return;
        }
        if (widthPixels != -2147483648) {
            params.width = scaledWidth;
        }
        if ((heightPixels != -2147483648) && (heightPixels != 1)) {
            params.height = scaledHeight;
        }
        view.setLayoutParams(params);
    }

    public static void setPadding(View view, int left, int top, int right, int bottom) {
        int scaledLeft = scaleValue(view.getContext(), left);
        int scaledTop = scaleValue(view.getContext(), top);
        int scaledRight = scaleValue(view.getContext(), right);
        int scaledBottom = scaleValue(view.getContext(), bottom);
        view.setPadding(scaledLeft, scaledTop, scaledRight, scaledBottom);
    }

    public static void setMargin(View view, int left, int top, int right, int bottom) {
        int scaledLeft = scaleValue(view.getContext(), left);
        int scaledTop = scaleValue(view.getContext(), top);
        int scaledRight = scaleValue(view.getContext(), right);
        int scaledBottom = scaleValue(view.getContext(), bottom);

        if ((view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();
            if (mMarginLayoutParams != null) {
                if (left != -2147483648) {
                    mMarginLayoutParams.leftMargin = scaledLeft;
                }
                if (right != -2147483648) {
                    mMarginLayoutParams.rightMargin = scaledRight;
                }
                if (top != -2147483648) {
                    mMarginLayoutParams.topMargin = scaledTop;
                }
                if (bottom != -2147483648) {
                    mMarginLayoutParams.bottomMargin = scaledBottom;
                }
                view.setLayoutParams(mMarginLayoutParams);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static void scaleView(View view) {
        if ((view instanceof TextView)) {
            TextView textView = (TextView) view;
            setTextSize(textView, textView.getTextSize());
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (null != params) {
            int width = -2147483648;
            int height = -2147483648;
            if ((params.width != -2) && (params.width != -1)) {
                width = params.width;
            }

            if ((params.height != -2) && (params.height != -1)) {
                height = params.height;
            }

            setViewSize(view, width, height);

            setPadding(view, view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }

        if ((view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();
            if (mMarginLayoutParams != null) {
                setMargin(view, mMarginLayoutParams.leftMargin, mMarginLayoutParams.topMargin, mMarginLayoutParams.rightMargin, mMarginLayoutParams.bottomMargin);
            }
        }

        if (Build.VERSION.SDK_INT >= 16) {
            int minWidth = scaleValue(view.getContext(), view.getMinimumWidth());
            int minHeight = scaleValue(view.getContext(), view.getMinimumHeight());
            view.setMinimumWidth(minWidth);
            view.setMinimumHeight(minHeight);
        }
    }

    public static int scaleTextValue(Context context, float value) {
        return scaleValue(context, value);
    }

    public static void setSPTextSize(TextView textView, float size) {
        float scaledSize = scaleTextValue(textView.getContext(), size);
        textView.setTextSize(scaledSize);
    }

    public static void setTextSize(TextView textView, float sizePixels) {
        float scaledSize = scaleTextValue(textView.getContext(), sizePixels);
        textView.setTextSize(0, scaledSize);
    }

    public static void setTextSize(Context context, TextPaint textPaint, float sizePixels) {
        float scaledSize = scaleTextValue(context, sizePixels);
        textPaint.setTextSize(scaledSize);
    }

    public static void setTextSize(Context context, Paint paint, float sizePixels) {
        float scaledSize = scaleTextValue(context, sizePixels);
        paint.setTextSize(scaledSize);
    }

    public static void scaleContentView(ViewGroup contentView) {
        scaleView(contentView);
        if (contentView.getChildCount() > 0)
            for (int i = 0; i < contentView.getChildCount(); i++) {
                View view = contentView.getChildAt(i);
                if (!(view instanceof ViewGroup)) {
                    scaleView(contentView.getChildAt(i));
                }
            }
    }

    public static void scaleContentView(View parent, int id) {
        ViewGroup contentView = null;
        View view = parent.findViewById(id);
        if ((view instanceof ViewGroup)) {
            contentView = (ViewGroup) view;
            scaleContentView(contentView);
        }
    }

    public static void scaleContentView(Context context, int id) {
        ViewGroup contentView = null;
        View view = ((Activity) context).findViewById(id);
        if ((view instanceof ViewGroup)) {
            contentView = (ViewGroup) view;
            scaleContentView(contentView);
        }
    }

    /**
     * 在onCreate()即可强行获取View的尺寸
     * <p>需回调onGetSizeListener接口，在onGetSize中获取view宽高</p>
     * <p>用法示例如下所示</p>
     * <pre>{@code
     * SizeUtils.forceGetViewSize(view);
     * SizeUtils.setListener(new SizeUtils.onGetSizeListener() {
     *     public void onGetSize(View view) {
     *         Log.d("tag", view.getWidth() + " " + view.getHeight());
     *     }
     * });}
     * </pre>
     *
     * @param view 视图
     */
    public static void forceGetViewSize(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onGetSize(view);
                }
            }
        });
    }

    /**
     * 获取到View尺寸的监听
     */
    public interface onGetSizeListener {
        void onGetSize(View view);
    }

    public static void setListener(onGetSizeListener listener) {
        mListener = listener;
    }

    private static onGetSizeListener mListener;

    /**
     * 测量这个view
     * 最后通过getMeasuredWidth()获取宽度和高度.
     * @param view 要测量的view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 获得这个View的宽度
     * 测量这个view，最后通过getMeasuredWidth()获取宽度.
     * @param view 要测量的view
     * @return 测量过的view的宽度
     */
    public static int getViewWidth(View view) {
        measureView(view);
        return view.getMeasuredWidth();
    }

    /**
     * 获得这个View的高度
     * 测量这个view，最后通过getMeasuredHeight()获取高度.
     * @param view 要测量的view
     * @return 测量过的view的高度
     */
    public static int getViewHeight(View view) {
        measureView(view);
        return view.getMeasuredHeight();
    }

    /**
     * ListView中提前测量View尺寸，如headerView
     * <p>用的时候去掉注释拷贝到ListView中即可</p>
     * <p>参照以下注释代码</p>
     *
     * @param view 视图
     */
    public static void measureViewInLV(View view) {
        Log.i("tips", "U should copy the following code.");
        /*
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight,
                    MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
        */
    }

    /**
     * 获取运行屏幕宽度
     * @param context 上下文
     * @return 屏幕尺寸
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.widthPixels;
    }

    /**
     * 获取控件宽
     * @param view 指定控件
     * @return 控件宽度
     */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /**
     * 获取状态栏高度
     * @param context  上下文
     * @return  状态栏高度
     */
    public static int getStatuBarHeight(Context context) {
        /**
         * 获取状态栏高度——方法
         */
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }
}