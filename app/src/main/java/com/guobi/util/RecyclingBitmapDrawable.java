package com.guobi.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by chenq on 2016/12/23.
 */
public class RecyclingBitmapDrawable extends BitmapDrawable {
    private int mRefCount = 0;

    public RecyclingBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
        addRef();
    }

    public final void addRef() {
        synchronized (this) {
            mRefCount++;
        }
    }

    public final void releaseRef() {
        synchronized (this) {
            mRefCount--;
        }

        // Check to see if recycle() can be called
        checkState();
    }


    /**
     * Notify the drawable that the cache state has changed. Internally a count
     * is kept so that the drawable knows when it is no longer being cached.
     *
     * @param isCached - Whether the drawable is being cached or not
     */
    public final void setIsCached(boolean isCached) {
        synchronized (this) {
            if (isCached) {
                mRefCount++;
            } else {
                mRefCount--;
            }
        }

        // Check to see if recycle() can be called
        checkState();
    }

    private synchronized void checkState() {
        // If the drawable cache and display ref counts = 0, and this drawable
        // has been displayed, then recycle
        if (mRefCount <= 0
                //&& mDisplayRefCount <= 0
                //&& mHasBeenDisplayed
                && hasValidBitmap()) {


            Bitmap bm = getBitmap();
            if (bm != null && bm.isRecycled() == false) {
                bm.recycle();
            }
        }
    }

    private synchronized boolean hasValidBitmap() {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }


}
