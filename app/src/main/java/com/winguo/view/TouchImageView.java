package com.winguo.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aispeech.client.IFacadeListener;
import com.winguo.R;

/**
 * 测试秘书 可拖动图标
 * Created by admin on 2017/3/23.
 */
@Deprecated
public class TouchImageView extends ImageView {
    //缩放类型
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;
    // 默认边界宽度
    private static final int DEFAULT_BORDER_WIDTH = 0;
    // 默认边界颜色
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    //这个画笔最重要的是关联了mBitmapShader 使canvas在执行的时候可以切割原图片(mBitmapShader是关联了原图的bitmap的)
    private final Paint mBitmapPaint = new Paint();
    //这个描边，则与本身的原图bitmap没有任何关联，
    private final Paint mBorderPaint = new Paint();
    //这里定义了 圆形边缘的默认宽度和颜色
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader; // 位图渲染
    private int mBitmapWidth;   // 位图宽度
    private int mBitmapHeight;  // 位图高度

    private float mDrawableRadius;// 图片半径
    private float mBorderRadius;// 带边框的的图片半径

    private ColorFilter mColorFilter;
    //初始false
    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;

    //手势
    private GestureDetector mGestureDetector;
    //事件的回调，首先需要些一个接口，然后用接口创建一个对象，
    //添加get、set方法
    private OnDoubleClick onDoubleClickListener;
    private OnLongClick onLongClick;
    private WindowManager windowManager;
    private OnClick onClick;
    private boolean isDragLongClick;
    private boolean isDouble;

    public void setOnDoubleClickListener(OnDoubleClick onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    public void setOnLongListener(OnLongClick onlongListener) {
        this.onLongClick = onlongListener;
    }
    public void setOnClickListener(OnClick onListener) {
        this.onClick = onListener;
    }

    private Handler mBaseHandler = new Handler();
    /** 
     * 长按线程
     */
    private LongPressedThread mLongPressedThread;
    /**
     * 点击等待线程
     */
    private ClickThread clickThread;
    /**
     * 点击等待线程
     */

    private static final long CLICK_SPACING_TIME = 300;
    private static final long LONG_PRESS_TIME = 500;


    //构造函数
    public TouchImageView(Context context,WindowManager windowManager) {
        super(context);
        this.windowManager = windowManager;
        initWind();
        setImageResource(R.drawable.winguo_main_bt_speak);
        mGestureDetector = new GestureDetector(context,new ISimpleOnGestureListener());
        init();
    }
    //构造函数
    public TouchImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    /**
     * 构造函数
     */
    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //通过obtainStyledAttributes 获得一组值赋给 TypedArray（数组） , 这一组值来自于res/values/attrs.xml中的name="CircleImageView"的declare-styleable中。
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
        //通过TypedArray提供的一系列方法getXXXX取得我们在xml里定义的参数值；
        // 获取边界的宽度
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        // 获取边界的颜色
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_border_overlay, DEFAULT_BORDER_OVERLAY);
        //调用 recycle() 回收TypedArray,以便后面重用
        a.recycle();
        init();
    }
    /**
     * 作用就是保证第一次执行setup函数里下面代码要在构造函数执行完毕时调用
     */
    private void init() {
        //在这里ScaleType被强制设定为CENTER_CROP，就是将图片水平垂直居中，进行缩放。
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }
    /**
     * 这里明确指出 此种imageview 只支持CENTER_CROP 这一种属性
     *
     * @param scaleType
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //如果图片不存在就不画
        if (getDrawable() == null) {
            return;
        }
        //绘制内圆形 图片 画笔为mBitmapPaint
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
        //如果圆形边缘的宽度不为0 我们还要绘制带边界的外圆形 边界画笔为mBorderPaint
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public void setBorderColorResource(@ColorRes int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }

    /**
     * 以下四个函数都是
     * 复写ImageView的setImageXxx()方法
     * 注意这个函数先于构造函数调用之前调用
     * @param bm
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        System.out.println("setImageDrawable -- setup");
        setup();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }
    /**
     * Drawable转Bitmap
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            //通常来说 我们的代码就是执行到这里就返回了。返回的就是我们最原始的bitmap
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
    /**
     * 这个函数很关键，进行图片画笔边界画笔(Paint)一些重绘参数初始化：
     * 构建渲染器BitmapShader用Bitmap来填充绘制区域,设置样式以及内外圆半径计算等，
     * 以及调用updateShaderMatrix()函数和 invalidate()函数；
     */
    private void setup() {
        //因为mReady默认值为false,所以第一次进这个函数的时候if语句为真进入括号体内
        //设置mSetupPending为true然后直接返回，后面的代码并没有执行。
        if (!mReady) {
            mSetupPending = true;
            return;
        }
        //防止空指针异常
        if (mBitmap == null) {
            return;
        }
        // 构建渲染器，用mBitmap位图来填充绘制区域 ，参数值代表如果图片太小的话 就直接拉伸
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 设置图片画笔反锯齿
        mBitmapPaint.setAntiAlias(true);
        // 设置图片画笔渲染器
        mBitmapPaint.setShader(mBitmapShader);
        // 设置边界画笔样式
        mBorderPaint.setStyle(Paint.Style.STROKE);//设画笔为空心
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);    //画笔颜色
        mBorderPaint.setStrokeWidth(mBorderWidth);//画笔边界宽度
        //这个地方是取的原图片的宽高
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
        // 设置含边界显示区域，取的是CircleImageView的布局实际大小，为方形，查看xml也就是160dp(240px)  getWidth得到是某个view的实际尺寸
        mBorderRect.set(0, 0, getWidth(), getHeight());
        //计算 圆形带边界部分（外圆）的最小半径，取mBorderRect的宽高减去一个边缘大小的一半的较小值（这个地方我比较纳闷为什么求外圆半径需要先减去一个边缘大小）
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
        // 初始图片显示区域为mBorderRect（CircleImageView的布局实际大小）
        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            //demo里始终执行
            //通过inset方法  使得图片显示的区域从mBorderRect大小上下左右内移边界的宽度形成区域，查看xml边界宽度为2dp（3px）,所以方形边长为就是160-4=156dp(234px)
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        //这里计算的是内圆的最小半径，也即去除边界宽度的半径
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);
        //设置渲染器的变换矩阵也即是mBitmap用何种缩放形式填充
        updateShaderMatrix();
        //手动触发ondraw()函数 完成最终的绘制
        invalidate();
    }
    /**
     * 这个函数为设置BitmapShader的Matrix参数，设置最小缩放比例，平移参数。
     * 作用：保证图片损失度最小和始终绘制图片正中央的那部分
     */
    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);
        // 这里不好理解 这个不等式也就是(mBitmapWidth / mDrawableRect.width()) > (mBitmapHeight / mDrawableRect.height())
        //取最小的缩放比例
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            //y轴缩放 x轴平移 使得图片的y轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            //x轴缩放 y轴平移 使得图片的x轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        // shaeder的变换矩阵，我们这里主要用于放大或者缩小。
        mShaderMatrix.setScale(scale, scale);
        // 平移
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }



    //创建接口
    public interface OnDoubleClick {
        public void onDoubleClick(View view);
    }

    /**
     * 监听是否移动 如果移动了 移除长按事件
     */
    public interface OnMoveFloatViewClick {
        public void onMoveFloatViewClick();
    }
    public interface OnClick {
        public void onClick(View view);
    }
    public interface OnLongClick {
        public void onLongClick(View view);
    }


    public class LongPressedThread implements Runnable{
        @Override
        public void run() {
            //这里处理长按事件
            if (!isDragLongClick) {
                onLongClick.onLongClick(TouchImageView.this);
            }
        }
    }
    public class ClickThread implements Runnable{
        @Override
        public void run() {
            //这里处理长按事件
            if (!isDragLongClick) {
                onClick.onClick(TouchImageView.this);
            }
        }
    }


    class ISimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int action = e.getAction();
            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    Log.i("dnw", "onDoubleTap ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("dnw", "onDoubleTap ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("dnw", "onDoubleTap ACTION_UP");
                    break;
            }
            //设置双击事件的响应
            mBaseHandler.removeCallbacks(clickThread);
            if (onDoubleClickListener != null) {
                onDoubleClickListener.onDoubleClick(TouchImageView.this);
            }
            isDouble = true;
            Log.i("dnw", "点击了两次");

            return true;

        }

        //左滑，右滑等事件,滑动后才会响应
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e2.getX() - e1.getX()) > 50) {
                Log.i("dnw", "在X轴滑动");
                ObjectAnimator.ofFloat(TouchImageView.this, "translationX", getTranslationX(), e2.getX() - e1.getX()).setDuration(1000).start();

                return true;
            }
            Log.i("dnw", "onFling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i("dnw", "onScroll");
            int action1 = e1.getAction();
            int action2 = e2.getAction();
            switch (action1) {

                case MotionEvent.ACTION_DOWN:
                    Log.i("dnw", "onScroll action1 ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("dnw", "onScroll action1 ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("dnw", "onScroll action1 ACTION_UP");
                    break;
            }
            switch (action2) {

                case MotionEvent.ACTION_DOWN:
                    Log.i("dnw", "onScroll action2 ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("dnw", "onScroll action2 ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("dnw", "onScroll action2 ACTION_UP");
                    break;
            }
            setTranslationX(getTranslationX() + e2.getX() - e1.getX());
            setTranslationY(getTranslationY() + e2.getY() - e1.getY());
            mBaseHandler.removeCallbacks(clickThread);
            mBaseHandler.removeCallbacks(mLongPressedThread);
            return true;

        }



        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("dnw", "onDown");
            int action = e.getAction();
            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    Log.i("dnw", "onDown ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("dnw", "onDown ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("dnw", "onDown ACTION_UP");
                    break;
            }
            if (onClick != null && !isDouble) {
                isDragLongClick = false;
                clickThread = new ClickThread();
                mBaseHandler.postDelayed(clickThread,LONG_PRESS_TIME);
            }
            if (isDouble) {
                isDouble = false;
            }
            return super.onDown(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("dnw", "onLongPress");
            //长按事件
            // isDragLongClick = true;
            int action = e.getAction();
            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    Log.i("dnw", "onLongPress ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("dnw", "onLongPress ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("dnw", "onLongPress ACTION_UP");
                    break;
            }
            mLongPressedThread = new LongPressedThread();
            mBaseHandler.postDelayed(mLongPressedThread,CLICK_SPACING_TIME);
            super.onLongPress(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.i("dnw", "onShowPress");
            int action = e.getAction();
            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    Log.i("dnw", "onShowPress ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("dnw", "onShowPress ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("dnw", "onShowPress ACTION_UP");
                    break;
            }

            mBaseHandler.removeCallbacks(clickThread);
            super.onShowPress(e);
        }

    }

    private WindowManager.LayoutParams mLayoutParams;
    private int mCurrentX;
    private int mCurrentY;
    private static int mFloatViewWidth = 50;
    private static int mFloatViewHeight = 80;

    /**
     * 初始化秘书图标
     */
    private void initWind(){

        mLayoutParams = new WindowManager.LayoutParams();
        //设置View默认的摆放位置
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //设置window type
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //设置背景为透明
        mLayoutParams.format = PixelFormat.RGBA_8888;
        //注意该属性的设置很重要，FLAG_NOT_FOCUSABLE使浮动窗口不获取焦点,若不设置该属性，屏幕的其它位置点击无效，应为它们无法获取焦点
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置视图的显示位置，通过WindowManager更新视图的位置其实就是改变(x,y)的值
        mCurrentX = mLayoutParams.x = 50;
        mCurrentY = mLayoutParams.y = 50;
        //设置视图的宽、高
        mLayoutParams.width = 100;
        mLayoutParams.height = 100;
        //将视图添加到Window中
        windowManager.addView(TouchImageView.this, mLayoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //通过添加将按钮的双击事件传递给后面
        Log.i("dnw","onTouchEvent");
        mCurrentX = (int) event.getRawX() - mFloatViewWidth;
        mCurrentY = (int) event.getRawY() - mFloatViewHeight;
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //记录当前点击的时间
                Log.i("dnw ACTION_DOWN :","");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("dnw ACTION_MOVE :","");
                updateFloatView();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("dnw ACTION_UP :","");
                break;

        }

        mGestureDetector.onTouchEvent(event);
        return true;
        //  return super.onTouchEvent(event);

    }

    private void updateFloatView() {
        mLayoutParams.x = mCurrentX;
        mLayoutParams.y = mCurrentY;
        windowManager.updateViewLayout(TouchImageView.this, mLayoutParams);
    }

}
