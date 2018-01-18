package com.guobi.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.guobi.gfc.gbmiscutils.log.GBLogUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenq on 2016/12/23.
 */
public class DrawableDecoder {

    /**
     * @param context
     * @param resName
     * @return
     */
    public static final Drawable createFromResource(Context context, String resName) {
        try {
            Resources res = context.getResources();
            final int resId = res.getIdentifier(
                    resName,
                    "drawable",
                    context.getPackageName()
            );
            if (resId <= 0) {
                return null;
            }
            return createFromResource(context, resId);
        } catch (Exception e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param pkgName
     * @param res
     * @param resName
     * @return
     */
    public static final Drawable createFromResource(String pkgName, Resources res, String resName) {
        try {
            final int resId = res.getIdentifier(
                    resName,
                    "drawable",
                    pkgName
            );
            if (resId <= 0) {
                return null;
            }
            return createFromResource(res, resId);
        } catch (Exception e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从资源创建
     *
     * @param context
     * @param resId
     * @return
     */
    public static final Drawable createFromResource(Context context, int resId) {
        return createFromResource(context.getResources(), resId);
    }

    /**
     * @param res
     * @param resId
     * @return
     */
    public static final Drawable createFromResource(Resources res, int resId) {

        InputStream is = null;
        try {
            is = res.openRawResource(resId);
            TypedValue value = new TypedValue();
            res.getValue(resId, value, true);
            return Drawable.createFromResourceStream(res, value, is, res.getResourceName(resId));
        } catch (Exception e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                if (GBLogUtils.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static final Drawable createFromResource(Resources res, int resId, int dpi) {

        InputStream is = null;
        try {
            is = res.openRawResource(resId);
            TypedValue value = new TypedValue();
            res.getValue(resId, value, true);
            value.density = dpi;
            return Drawable.createFromResourceStream(res, value, is, res.getResourceName(resId));
        } catch (Exception e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                if (GBLogUtils.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从文件创建
     *
     * @param context
     * @param filePath
     * @return
     */
    public static final Drawable createFromFile(Context context, String filePath) {

        InputStream is = null;
        try {
            Resources res = context.getResources();
            is = new FileInputStream(filePath);
            TypedValue value = new TypedValue();
            value.density = DisplayMetrics.DENSITY_HIGH;
            return Drawable.createFromResourceStream(res, value, is, filePath);
        } catch (Exception e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (GBLogUtils.DEBUG) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                if (GBLogUtils.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private DrawableDecoder() {
    }
}
