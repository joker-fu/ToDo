package com.joker.core.recycler.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * DividerItemDecoration
 *
 * @author joker
 * @date 2019/1/20.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private int mPadding;
    private Drawable mDivider;

    private int mOrientation;
    private final Rect mBounds = new Rect();

    public DividerItemDecoration(Context context, int orientation) {
        this(context, orientation, 0);
    }

    public DividerItemDecoration(Context context, int orientation, int padding) {
        mPadding = padding;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider == null) {
            Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this "
                    + "DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }

    public void setDrawableColor(@ColorInt int color) {
        mDivider.setTint(color);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent, state);
        } else {
            drawHorizontal(c, parent, state);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + mPadding;
            right = parent.getWidth() - parent.getPaddingRight() - mPadding;
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = mPadding;
            right = parent.getWidth() - mPadding;
        }

        final int childCount = parent.getChildCount();
        final int lastPosition = state.getItemCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (position != lastPosition) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop() + mPadding;
            bottom = parent.getHeight() - parent.getPaddingBottom() - mPadding;
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = mPadding;
            bottom = parent.getHeight() - mPadding;
        }

        final int childCount = parent.getChildCount();
        final int lastPosition = state.getItemCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (position != lastPosition) {
                final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
                if (layoutManager != null) {
                    layoutManager.getDecoratedBoundsWithMargins(child, mBounds);
                }
                final int right = mBounds.right + Math.round(child.getTranslationX());
                final int left = right - mDivider.getIntrinsicWidth();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
