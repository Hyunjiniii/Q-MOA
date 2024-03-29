package com.example.q_moa.floatingactionbutton;

import com.example.q_moa.R;
import com.example.q_moa.floatingactionbutton.FloatingScrollView.OnScrollChangedListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ScrollView;

/**
 * Android Google+ like floating action button which reacts on the attached list
 * view scrolling events.
 *
 * @author Oleksandr Melnykov
 * <p>
 * FloatingActionButton Update
 * @author Mir(whdghks913)
 */
public class FloatingActionButton extends ImageButton {
    private int TRANSLATE_DURATION_MILLIS = 200;

    /**
     * @android.R.color.holo_blue_light
     * @android.R.color.holo_purple
     * @android.R.color.holo_green_light
     * @android.R.color.holo_orange_light
     * @android.R.color.holo_red_light
     */
    public static final int Color_BLUE = -13388315;
    public static final int Color_PURPLE = -5609780;
    public static final int Color_GREEN = -6697984;
    public static final int Color_ORANGE = -17613;
    public static final int Color_RED = -48060;

    public @interface TYPE {
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MINI = 1;

    protected AbsListView mListView;
    protected FloatingScrollView mScrollView;

    private int mScrollY;
    private boolean mVisible;

    private int mColorNormal;
    private int mColorPressed;
    private boolean mShadow;
    private int mType;
    private boolean isAnimation = true;

    private Rect mRect;
    private boolean alwaysOnTop;

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingActionButton(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            int newScrollY = getListViewScrollY();

            if (newScrollY == mScrollY) {
                return;
            }

            if (newScrollY > mScrollY) {
                // Scrolling up
                if (!alwaysOnTop)
                    hide();
            } else if (newScrollY < mScrollY) {
                // Scrolling down
                show();
            }
            mScrollY = newScrollY;
        }
    };

    private final FloatingScrollView.OnScrollChangedListener mOnScrollViewScrollListener = new OnScrollChangedListener() {

        @Override
        public void onScrollChanged(ScrollView mScrollView, int x,
                                    int newScrollY, int oldx, int mScrollY) {

            if (checkIsLocatedAtFooter(mScrollView)) {
                if (!alwaysOnTop)
                    hide();
                return;
            }

            if (newScrollY == mScrollY) {
                return;
            }

            if (newScrollY > mScrollY) {
                // Scrolling up
                if (!alwaysOnTop)
                    hide();
            } else if (newScrollY < mScrollY) {
                // Scrolling down
                show();
            }
        }

    };

    private boolean checkIsLocatedAtFooter(ScrollView mScrollView) {
        if (mRect == null) {
            mRect = new Rect();
            mScrollView.getLocalVisibleRect(mRect);
            return false;
        }

        int oldBottom = mRect.bottom;
        int height = mScrollView.getMeasuredHeight();
        mScrollView.getLocalVisibleRect(mRect);

        View v = mScrollView.getChildAt(0);

        if (oldBottom > 0 && height > 0) {
            if (oldBottom != mRect.bottom
                    && mRect.bottom == (v.getMeasuredHeight() + getPaddingTop() + getPaddingBottom())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getDimension(mType == TYPE_NORMAL ? R.dimen.fab_size_normal
                : R.dimen.fab_size_mini);
        if (mShadow) {
            int shadowSize = getDimension(R.dimen.fab_shadow_size);
            size += shadowSize * 2;
        }
        setMeasuredDimension(size, size);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mScrollY = mScrollY;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mScrollY = savedState.mScrollY;
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        mVisible = true;
        mColorNormal = getColor(android.R.color.holo_blue_dark);
        mColorPressed = getColor(android.R.color.holo_blue_light);
        mType = TYPE_NORMAL;
        mShadow = true;
        if (attributeSet != null) {
            initAttributes(context, attributeSet);
        }
        updateBackground();
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet,
                R.styleable.FloatingActionButton);
        if (attr != null) {
            try {
                mColorNormal = attr.getColor(
                        R.styleable.FloatingActionButton_fab_colorNormal,
                        getColor(android.R.color.holo_blue_dark));
                mColorPressed = attr.getColor(
                        R.styleable.FloatingActionButton_fab_colorPressed,
                        getColor(android.R.color.holo_blue_light));
                mShadow = attr.getBoolean(
                        R.styleable.FloatingActionButton_fab_shadow, true);
                mType = attr.getInt(R.styleable.FloatingActionButton_fab_type,
                        TYPE_NORMAL);
            } finally {
                attr.recycle();
            }
        }
    }

    private void updateBackground() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},
                createDrawable(mColorPressed));
        drawable.addState(new int[]{}, createDrawable(mColorNormal));
        setBackgroundCompat(drawable);
    }

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);

        if (mShadow) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                    getResources().getDrawable(R.drawable.shadow),
                    shapeDrawable});
            int shadowSize = getDimension(mType == TYPE_NORMAL ? R.dimen.fab_shadow_size
                    : R.dimen.fab_mini_shadow_size);
            layerDrawable.setLayerInset(1, shadowSize, shadowSize, shadowSize,
                    shadowSize);
            return layerDrawable;
        } else {
            return shapeDrawable;
        }
    }

    private TypedArray getTypedArray(Context context,
                                     AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }

    private int getDimension(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    protected int getListViewScrollY() {
        View topChild = mListView.getChildAt(0);
        return topChild == null ? 0 : mListView.getFirstVisiblePosition()
                * topChild.getHeight() - topChild.getTop();
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    public void setColorNormal(int color) {
        if (color != mColorNormal) {
            mColorNormal = color;
            updateBackground();
        }
    }

    public void setColorNormalResId(int colorResId) {
        setColorNormal(getColor(colorResId));
    }

    public int getColorNormal() {
        return mColorNormal;
    }

    public void setColorPressed(int color) {
        if (color != mColorPressed) {
            mColorPressed = color;
            updateBackground();
        }
    }

    public void setColorPressedResId(int colorResId) {
        setColorPressed(getColor(colorResId));
    }

    public int getColorPressed() {
        return mColorPressed;
    }

    public void setShadow(boolean shadow) {
        if (shadow != mShadow) {
            mShadow = shadow;
            updateBackground();
        }
    }

    public boolean hasShadow() {
        return mShadow;
    }

    public void setType(int type) {
        if (type != mType) {
            mType = type;
            updateBackground();
        }
    }

    public int getType() {
        return mType;
    }

    protected AbsListView.OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate,
                        boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate && isAnimation) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                setTranslationY(translationY);
            }
        }
    }

    public void attachToListView(AbsListView listView) {
        if (listView == null) {
            throw new NullPointerException("AbsListView cannot be null.");
        } else if (mScrollView != null) {
            mScrollView = null;
        }
        mListView = listView;
        mListView.setOnScrollListener(mOnScrollListener);
    }

    public void attachToScrollView(FloatingScrollView mScrollView) {
        if (mScrollView == null) {
            throw new NullPointerException("ScrollView cannot be null.");
        } else if (mListView != null) {
            mListView = null;
        }
        this.mScrollView = mScrollView;
        this.mScrollView
                .setOnScrollChangedListener(mOnScrollViewScrollListener);
    }

    public void setDuration(int Duration) {
        TRANSLATE_DURATION_MILLIS = Duration;
    }

    public void setDrawable(Drawable mDrawable) {
        setBackgroundCompat(mDrawable);
    }

    public void setAnimationEnable(boolean setAnimation) {
        isAnimation = setAnimation;
    }

    public boolean getIsAnimation() {
        return isAnimation;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
    }

    public static class SavedState extends BaseSavedState {

        private int mScrollY;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mScrollY = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mScrollY);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
