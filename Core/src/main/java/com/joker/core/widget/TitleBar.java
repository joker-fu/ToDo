package com.joker.core.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.joker.core.R;
import com.joker.core.utils.SizeUtils;

import java.util.UUID;

/**
 * 通用标题栏
 * <p>
 * 源自【https://github.com/wuhenzhizao/android-titlebar】
 * 1 去掉沉浸状态栏处理
 * 2 布局居中处理
 *
 * @author joker
 * @date 2019/2/21.
 */
@SuppressWarnings("ResourceType")
public class TitleBar extends RelativeLayout implements View.OnClickListener {
    private View viewBottomLine;                        // 分隔线视图
    private View viewBottomShadow;                      // 底部阴影
    private RelativeLayout rlMain;                      // 主视图

    private TextView tvLeft;                            // 左边TextView
    private ImageButton btnLeft;                        // 左边ImageButton
    private View viewCustomLeft;
    private TextView tvRight;                           // 右边TextView
    private ImageButton btnRight;                       // 右边ImageButton
    private View viewCustomRight;
    private LinearLayout llMainCenter;
    private TextView tvCenter;                          // 标题栏文字
    private TextView tvCenterSub;                       // 副标题栏文字
    private ProgressBar progressCenter;                 // 中间进度条,默认隐藏
    private RelativeLayout rlMainCenterSearch;          // 中间搜索框布局
    private EditText etSearchHint;
    private ImageView ivSearch;
    private ImageView ivVoice;
    private View centerCustomView;                      // 中间自定义视图

    private int titleBarColor;                          // 标题栏背景颜色
    private int titleBarHeight;                         // 标题栏高度

    private boolean showBottomLine;                     // 是否显示底部分割线
    private int bottomLineColor;                        // 分割线颜色
    private float bottomShadowHeight;                   // 底部阴影高度

    private int leftType;                               // 左边视图类型
    private String leftText;                            // 左边TextView文字
    private int leftTextColor;                          // 左边TextView颜色
    private float leftTextSize;                         // 左边TextView文字大小
    private int leftDrawable;                           // 左边TextView drawableLeft资源
    private float leftDrawablePadding;                  // 左边TextView drawablePadding
    private int leftImageResource;                      // 左边图片资源
    private int leftCustomViewRes;                      // 左边自定义视图布局资源

    private int rightType;                              // 右边视图类型
    private String rightText;                           // 右边TextView文字
    private int rightTextColor;                         // 右边TextView颜色
    private float rightTextSize;                        // 右边TextView文字大小
    private int rightImageResource;                     // 右边图片资源
    private int rightCustomViewRes;                     // 右边自定义视图布局资源

    private int centerType;                             // 中间视图类型
    private String centerText;                          // 中间TextView文字
    private int centerTextColor;                        // 中间TextView字体颜色
    private float centerTextSize;                       // 中间TextView字体大小
    private boolean centerTextMarquee;                  // 中间TextView字体是否显示跑马灯效果
    private String centerSubText;                       // 中间subTextView文字
    private int centerSubTextColor;                     // 中间subTextView字体颜色
    private float centerSubTextSize;                    // 中间subTextView字体大小
    private boolean centerSearchEditable;                // 搜索框是否可输入
    private int centerSearchBgResource;                 // 搜索框背景图片
    private int centerSearchRightType;                  // 搜索框右边按钮类型  0: voice 1: delete
    private int centerCustomViewRes;                    // 中间自定义布局资源

    private int PADDING_5;
    private int PADDING_12;

    private OnTitleBarListener listener;
    private OnTitleBarDoubleClickListener doubleClickListener;

    private static final int TYPE_LEFT_NONE = 0;
    private static final int TYPE_LEFT_TEXTVIEW = 1;
    private static final int TYPE_LEFT_IMAGEBUTTON = 2;
    private static final int TYPE_LEFT_CUSTOM_VIEW = 3;
    private static final int TYPE_RIGHT_NONE = 0;
    private static final int TYPE_RIGHT_TEXTVIEW = 1;
    private static final int TYPE_RIGHT_IMAGEBUTTON = 2;
    private static final int TYPE_RIGHT_CUSTOM_VIEW = 3;
    private static final int TYPE_CENTER_NONE = 0;
    private static final int TYPE_CENTER_TEXTVIEW = 1;
    private static final int TYPE_CENTER_SEARCHVIEW = 2;
    private static final int TYPE_CENTER_CUSTOM_VIEW = 3;

    private static final int TYPE_CENTER_SEARCH_RIGHT_VOICE = 0;
    private static final int TYPE_CENTER_SEARCH_RIGHT_DELETE = 1;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(context, attrs);
        initGlobalViews(context);
        initMainViews(context);
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        PADDING_5 = SizeUtils.dp2px(context, 5);
        PADDING_12 = SizeUtils.dp2px(context, 12);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        titleBarColor = array.getColor(R.styleable.TitleBar_titleBarColor, Color.parseColor("#ffffff"));
        titleBarHeight = (int) array.getDimension(R.styleable.TitleBar_titleBarHeight, SizeUtils.dp2px(context, 44));

        showBottomLine = array.getBoolean(R.styleable.TitleBar_showBottomLine, true);
        bottomLineColor = array.getColor(R.styleable.TitleBar_bottomLineColor, Color.parseColor("#dddddd"));
        bottomShadowHeight = array.getDimension(R.styleable.TitleBar_bottomShadowHeight, SizeUtils.dp2px(context, 0));

        leftType = array.getInt(R.styleable.TitleBar_leftType, TYPE_LEFT_NONE);
        if (leftType == TYPE_LEFT_TEXTVIEW) {
            leftText = array.getString(R.styleable.TitleBar_leftText);
            leftTextColor = array.getColor(R.styleable.TitleBar_leftTextColor, getResources().getColor(R.color.titlebar_text_selector));
            leftTextSize = array.getDimension(R.styleable.TitleBar_leftTextSize, SizeUtils.dp2px(context, 16));
            leftDrawable = array.getResourceId(R.styleable.TitleBar_leftDrawable, 0);
            leftDrawablePadding = array.getDimension(R.styleable.TitleBar_leftDrawablePadding, 5);
        } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
            leftImageResource = array.getResourceId(R.styleable.TitleBar_leftImageResource, R.drawable.titlebar_reback_selector);
        } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
            leftCustomViewRes = array.getResourceId(R.styleable.TitleBar_leftCustomView, 0);
        }

        rightType = array.getInt(R.styleable.TitleBar_rightType, TYPE_RIGHT_NONE);
        if (rightType == TYPE_RIGHT_TEXTVIEW) {
            rightText = array.getString(R.styleable.TitleBar_rightText);
            rightTextColor = array.getColor(R.styleable.TitleBar_rightTextColor, getResources().getColor(R.color.titlebar_text_selector));
            rightTextSize = array.getDimension(R.styleable.TitleBar_rightTextSize, SizeUtils.dp2px(context, 16));
        } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
            rightImageResource = array.getResourceId(R.styleable.TitleBar_rightImageResource, 0);
        } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
            rightCustomViewRes = array.getResourceId(R.styleable.TitleBar_rightCustomView, 0);
        }

        centerType = array.getInt(R.styleable.TitleBar_centerType, TYPE_CENTER_NONE);
        if (centerType == TYPE_CENTER_TEXTVIEW) {
            centerText = array.getString(R.styleable.TitleBar_centerText);
            centerTextColor = array.getColor(R.styleable.TitleBar_centerTextColor, Color.parseColor("#333333"));
            centerTextSize = array.getDimension(R.styleable.TitleBar_centerTextSize, SizeUtils.dp2px(context, 18));
            centerTextMarquee = array.getBoolean(R.styleable.TitleBar_centerTextMarquee, true);
            centerSubText = array.getString(R.styleable.TitleBar_centerSubText);
            centerSubTextColor = array.getColor(R.styleable.TitleBar_centerSubTextColor, Color.parseColor("#666666"));
            centerSubTextSize = array.getDimension(R.styleable.TitleBar_centerSubTextSize, SizeUtils.dp2px(context, 11));
        } else if (centerType == TYPE_CENTER_SEARCHVIEW) {
            centerSearchEditable = array.getBoolean(R.styleable.TitleBar_centerSearchEditable, true);
            centerSearchBgResource = array.getResourceId(R.styleable.TitleBar_centerSearchBg, R.drawable.titlebar_search_gray_shape);
            centerSearchRightType = array.getInt(R.styleable.TitleBar_centerSearchRightType, TYPE_CENTER_SEARCH_RIGHT_VOICE);
        } else if (centerType == TYPE_CENTER_CUSTOM_VIEW) {
            centerCustomViewRes = array.getResourceId(R.styleable.TitleBar_centerCustomView, 0);
        }

        array.recycle();
    }

    private final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * 初始化全局视图
     *
     * @param context 上下文
     */
    private void initGlobalViews(Context context) {
        ViewGroup.LayoutParams globalParams = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        setLayoutParams(globalParams);


        // 构建主视图
        rlMain = new RelativeLayout(context);
        rlMain.setId(generateViewId());
        rlMain.setBackgroundColor(titleBarColor);
        LayoutParams mainParams = new LayoutParams(MATCH_PARENT, titleBarHeight);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        // 计算主布局高度
        if (showBottomLine) {
            mainParams.height = titleBarHeight - Math.max(1, SizeUtils.dp2px(context, 0.4f));
        } else {
            mainParams.height = titleBarHeight;
        }
        addView(rlMain, mainParams);
        setGravity(Gravity.CENTER_VERTICAL);

        // 构建分割线视图
        if (showBottomLine) {
            // 已设置显示标题栏分隔线,5.0以下机型,显示分隔线
            viewBottomLine = new View(context);
            viewBottomLine.setBackgroundColor(bottomLineColor);
            LayoutParams bottomLineParams = new LayoutParams(MATCH_PARENT, Math.max(1, SizeUtils.dp2px(context, 0.4f)));
            bottomLineParams.addRule(RelativeLayout.BELOW, rlMain.getId());

            addView(viewBottomLine, bottomLineParams);
        } else if (bottomShadowHeight != 0 && showBottomLine) {
            viewBottomShadow = new View(context);
            viewBottomShadow.setBackgroundResource(R.drawable.titlebar_bottom_shadow);
            LayoutParams bottomShadowParams = new LayoutParams(MATCH_PARENT, SizeUtils.dp2px(context, bottomShadowHeight));
            bottomShadowParams.addRule(RelativeLayout.BELOW, rlMain.getId());

            addView(viewBottomShadow, bottomShadowParams);
        }
    }

    /**
     * 初始化主视图
     *
     * @param context 上下文
     */
    private void initMainViews(Context context) {
        if (leftType != TYPE_LEFT_NONE) {
            initMainLeftViews(context);
        }
        if (rightType != TYPE_RIGHT_NONE) {
            initMainRightViews(context);
        }
        if (centerType != TYPE_CENTER_NONE) {
            initMainCenterViews(context);
        }
    }

    /**
     * 初始化主视图左边部分
     * -- add: adaptive RTL
     *
     * @param context 上下文
     */
    private void initMainLeftViews(Context context) {
        LayoutParams leftInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        leftInnerParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        leftInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (leftType == TYPE_LEFT_TEXTVIEW) {
            // 初始化左边TextView
            tvLeft = new TextView(context);
            tvLeft.setId(generateViewId());
            tvLeft.setText(leftText);
            tvLeft.setTextColor(leftTextColor);
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
            tvLeft.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            tvLeft.setSingleLine(true);
            tvLeft.setOnClickListener(this);
            // 设置DrawableLeft及DrawablePadding
            if (leftDrawable != 0) {
                tvLeft.setCompoundDrawablePadding((int) leftDrawablePadding);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tvLeft.setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawable, 0, 0, 0);
                } else {
                    tvLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
                }
            }
            tvLeft.setPadding(PADDING_12, 0, PADDING_12, 0);

            rlMain.addView(tvLeft, leftInnerParams);

        } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
            // 初始化左边ImageButton
            btnLeft = new ImageButton(context);
            btnLeft.setId(generateViewId());
            btnLeft.setBackgroundColor(Color.TRANSPARENT);
            btnLeft.setImageResource(leftImageResource);
            btnLeft.setPadding(PADDING_12, 0, PADDING_12, 0);
            btnLeft.setOnClickListener(this);

            rlMain.addView(btnLeft, leftInnerParams);

        } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
            // 初始化自定义View
            viewCustomLeft = LayoutInflater.from(context).inflate(leftCustomViewRes, rlMain, false);
            if (viewCustomLeft.getId() == View.NO_ID) {
                viewCustomLeft.setId(generateViewId());
            }
            rlMain.addView(viewCustomLeft, leftInnerParams);
        }
    }

    /**
     * 初始化主视图右边部分
     * -- add: adaptive RTL
     *
     * @param context 上下文
     */
    private void initMainRightViews(Context context) {
        LayoutParams rightInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        rightInnerParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        rightInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (rightType == TYPE_RIGHT_TEXTVIEW) {
            // 初始化右边TextView
            tvRight = new TextView(context);
            tvRight.setId(generateViewId());
            tvRight.setText(rightText);
            tvRight.setTextColor(rightTextColor);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            tvRight.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            tvRight.setSingleLine(true);
            tvRight.setPadding(PADDING_12, 0, PADDING_12, 0);
            tvRight.setOnClickListener(this);
            rlMain.addView(tvRight, rightInnerParams);

        } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
            // 初始化右边ImageBtn
            btnRight = new ImageButton(context);
            btnRight.setId(generateViewId());
            btnRight.setImageResource(rightImageResource);
            btnRight.setBackgroundColor(Color.TRANSPARENT);
            btnRight.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnRight.setPadding(PADDING_12, 0, PADDING_12, 0);
            btnRight.setOnClickListener(this);
            rlMain.addView(btnRight, rightInnerParams);

        } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
            // 初始化自定义view
            viewCustomRight = LayoutInflater.from(context).inflate(rightCustomViewRes, rlMain, false);
            if (viewCustomRight.getId() == View.NO_ID) {
                viewCustomRight.setId(generateViewId());
            }
            rlMain.addView(viewCustomRight, rightInnerParams);
        }
    }

    /**
     * 初始化主视图中间部分
     *
     * @param context 上下文
     */
    private void initMainCenterViews(Context context) {
        if (centerType == TYPE_CENTER_TEXTVIEW) {
            // 初始化中间子布局
            llMainCenter = new LinearLayout(context);
            llMainCenter.setId(generateViewId());
            llMainCenter.setGravity(Gravity.CENTER);
            llMainCenter.setOrientation(LinearLayout.VERTICAL);
            llMainCenter.setOnClickListener(this);

            LayoutParams centerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            centerParams.setMarginStart(PADDING_12);
            centerParams.setMarginEnd(PADDING_12);
            centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rlMain.addView(llMainCenter, centerParams);

            // 初始化标题栏TextView
            tvCenter = new TextView(context);
            tvCenter.setText(centerText);
            tvCenter.setTextColor(centerTextColor);
            tvCenter.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize);
            tvCenter.setGravity(Gravity.CENTER);
            tvCenter.setSingleLine(true);
            // 设置跑马灯效果
            int maxWidth = context.getResources().getDisplayMetrics().widthPixels;
            tvCenter.setMaxWidth((int) (maxWidth * 3 / 5.0));
            if (centerTextMarquee) {
                tvCenter.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tvCenter.setMarqueeRepeatLimit(-1);
                tvCenter.requestFocus();
                tvCenter.setSelected(true);
            }

            LinearLayout.LayoutParams centerTextParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            llMainCenter.addView(tvCenter, centerTextParams);

            // 初始化进度条, 显示于标题栏左边
            progressCenter = new ProgressBar(context);
            progressCenter.setIndeterminateDrawable(getResources().getDrawable(R.drawable.titlebar_progress_draw));
            progressCenter.setVisibility(View.GONE);
            int progressWidth = SizeUtils.dp2px(context, 18);
            LayoutParams progressParams = new LayoutParams(progressWidth, progressWidth);
            progressParams.addRule(RelativeLayout.CENTER_VERTICAL);
            progressParams.addRule(RelativeLayout.START_OF, llMainCenter.getId());
            rlMain.addView(progressCenter, progressParams);

            // 初始化副标题栏
            tvCenterSub = new TextView(context);
            tvCenterSub.setText(centerSubText);
            tvCenterSub.setTextColor(centerSubTextColor);
            tvCenterSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerSubTextSize);
            tvCenterSub.setGravity(Gravity.CENTER);
            tvCenterSub.setSingleLine(true);
            if (TextUtils.isEmpty(centerSubText)) {
                tvCenterSub.setVisibility(View.GONE);
            }

            LinearLayout.LayoutParams centerSubTextParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            llMainCenter.addView(tvCenterSub, centerSubTextParams);
        } else if (centerType == TYPE_CENTER_SEARCHVIEW) {
            // 初始化通用搜索框
            rlMainCenterSearch = new RelativeLayout(context);
            rlMainCenterSearch.setBackgroundResource(centerSearchBgResource);
            LayoutParams centerParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
            // 设置边距
            centerParams.topMargin = SizeUtils.dp2px(context, 7);
            centerParams.bottomMargin = SizeUtils.dp2px(context, 7);
            // 根据左边的布局类型来设置边距,布局依赖规则
            if (leftType == TYPE_LEFT_TEXTVIEW) {
                centerParams.addRule(RelativeLayout.END_OF, tvLeft.getId());
                centerParams.setMarginStart(PADDING_5);
            } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
                centerParams.addRule(RelativeLayout.END_OF, btnLeft.getId());
                centerParams.setMarginStart(PADDING_5);
            } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
                centerParams.addRule(RelativeLayout.END_OF, viewCustomLeft.getId());
                centerParams.setMarginStart(PADDING_5);
            } else {
                centerParams.setMarginStart(PADDING_12);
            }
            // 根据右边的布局类型来设置边距,布局依赖规则
            if (rightType == TYPE_RIGHT_TEXTVIEW) {
                centerParams.addRule(RelativeLayout.START_OF, tvRight.getId());
                centerParams.setMarginEnd(PADDING_5);
            } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
                centerParams.addRule(RelativeLayout.START_OF, btnRight.getId());
                centerParams.setMarginEnd(PADDING_5);
            } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
                centerParams.addRule(RelativeLayout.START_OF, viewCustomRight.getId());
                centerParams.setMarginEnd(PADDING_5);
            } else {
                centerParams.setMarginEnd(PADDING_12);
            }
            rlMain.addView(rlMainCenterSearch, centerParams);

            // 初始化搜索框搜索ImageView
            ivSearch = new ImageView(context);
            ivSearch.setId(generateViewId());
            ivSearch.setOnClickListener(this);
            int searchIconWidth = SizeUtils.dp2px(context, 15);
            LayoutParams searchParams = new LayoutParams(searchIconWidth, searchIconWidth);
            searchParams.addRule(RelativeLayout.CENTER_VERTICAL);
            searchParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            searchParams.setMarginStart(PADDING_12);
            rlMainCenterSearch.addView(ivSearch, searchParams);
            ivSearch.setImageResource(R.drawable.titlebar_search_normal);

            // 初始化搜索框语音ImageView
            ivVoice = new ImageView(context);
            ivVoice.setId(generateViewId());
            ivVoice.setOnClickListener(this);
            ivVoice.setPadding(PADDING_5, PADDING_5, PADDING_5, PADDING_5);
            LayoutParams voiceParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            voiceParams.addRule(RelativeLayout.CENTER_VERTICAL);
            voiceParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            voiceParams.setMarginEnd(PADDING_12);
            rlMainCenterSearch.addView(ivVoice, voiceParams);
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                ivVoice.setImageResource(R.drawable.titlebar_voice);
            } else {
                ivVoice.setImageResource(R.drawable.titlebar_delete_normal);
                ivVoice.setVisibility(View.GONE);
            }

            // 初始化文字输入框
            etSearchHint = new EditText(context);
            etSearchHint.setBackgroundColor(Color.TRANSPARENT);
            etSearchHint.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            etSearchHint.setHint(getResources().getString(R.string.titlebar_search_hint));
            etSearchHint.setTextColor(Color.parseColor("#666666"));
            etSearchHint.setHintTextColor(Color.parseColor("#999999"));
            etSearchHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeUtils.dp2px(context, 14));
            etSearchHint.setPadding(PADDING_5, 0, PADDING_5, 0);
            if (!centerSearchEditable) {
                etSearchHint.setCursorVisible(false);
                etSearchHint.clearFocus();
                etSearchHint.setFocusable(false);
                etSearchHint.setOnClickListener(this);
            } else {
                etSearchHint.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etSearchHint.setCursorVisible(true);
                    }
                });
            }
            etSearchHint.setCursorVisible(false);
            etSearchHint.setSingleLine(true);
            etSearchHint.setEllipsize(TextUtils.TruncateAt.END);
            etSearchHint.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            etSearchHint.addTextChangedListener(centerSearchWatcher);
            etSearchHint.setOnFocusChangeListener(focusChangeListener);
            etSearchHint.setOnEditorActionListener(editorActionListener);
            LayoutParams searchHintParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
            searchHintParams.addRule(RelativeLayout.END_OF, ivSearch.getId());
            searchHintParams.addRule(RelativeLayout.START_OF, ivVoice.getId());
            searchHintParams.addRule(RelativeLayout.CENTER_VERTICAL);
            searchHintParams.setMarginStart(PADDING_5);
            searchHintParams.setMarginEnd(PADDING_5);
            rlMainCenterSearch.addView(etSearchHint, searchHintParams);

        } else if (centerType == TYPE_CENTER_CUSTOM_VIEW) {
            // 初始化中间自定义布局
            centerCustomView = LayoutInflater.from(context).inflate(centerCustomViewRes, rlMain, false);
            if (centerCustomView.getId() == View.NO_ID) {
                centerCustomView.setId(generateViewId());
            }
            LayoutParams centerCustomParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            centerCustomParams.setMarginStart(PADDING_12);
            centerCustomParams.setMarginEnd(PADDING_12);
            centerCustomParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            if (leftType == TYPE_LEFT_TEXTVIEW) {
//                centerCustomParams.addRule(RelativeLayout.END_OF, tvLeft.getId());
//            } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
//                centerCustomParams.addRule(RelativeLayout.END_OF, btnLeft.getId());
//            } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
//                centerCustomParams.addRule(RelativeLayout.END_OF, viewCustomLeft.getId());
//            }
//            if (rightType == TYPE_RIGHT_TEXTVIEW) {
//                centerCustomParams.addRule(RelativeLayout.START_OF, tvRight.getId());
//            } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
//                centerCustomParams.addRule(RelativeLayout.START_OF, btnRight.getId());
//            } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
//                centerCustomParams.addRule(RelativeLayout.START_OF, viewCustomRight.getId());
//            }
            rlMain.addView(centerCustomView, centerCustomParams);
        }
    }

    private Window getWindow() {
        Context context = getContext();
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            activity = (Activity) ((ContextWrapper) context).getBaseContext();
        }
        if (activity != null) {
            return activity.getWindow();
        }
        return null;
    }

    private TextWatcher centerSearchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                if (TextUtils.isEmpty(s)) {
                    ivVoice.setImageResource(R.drawable.titlebar_voice);
                } else {
                    ivVoice.setImageResource(R.drawable.titlebar_delete_normal);
                }
            } else {
                if (TextUtils.isEmpty(s)) {
                    ivVoice.setVisibility(View.GONE);
                } else {
                    ivVoice.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_DELETE) {
                String input = etSearchHint.getText().toString();
                if (hasFocus && !TextUtils.isEmpty(input)) {
                    ivVoice.setVisibility(View.VISIBLE);
                } else {
                    ivVoice.setVisibility(View.GONE);
                }
            }
        }
    };

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (listener != null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                listener.onClicked(v, ACTION_SEARCH_SUBMIT, etSearchHint.getText().toString());
            }
            return false;
        }
    };

    private long lastClickMillis = 0;     // 双击事件中，上次被点击时间

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        if (v.equals(llMainCenter) && doubleClickListener != null) {
            long currentClickMillis = System.currentTimeMillis();
            if (currentClickMillis - lastClickMillis < 500) {
                doubleClickListener.onClicked(v);
            }
            lastClickMillis = currentClickMillis;
        } else if (v.equals(tvLeft)) {
            listener.onClicked(v, ACTION_LEFT_TEXT, null);
        } else if (v.equals(btnLeft)) {
            listener.onClicked(v, ACTION_LEFT_BUTTON, null);
        } else if (v.equals(tvRight)) {
            listener.onClicked(v, ACTION_RIGHT_TEXT, null);
        } else if (v.equals(btnRight)) {
            listener.onClicked(v, ACTION_RIGHT_BUTTON, null);
        } else if (v.equals(etSearchHint) || v.equals(ivSearch)) {
            listener.onClicked(v, ACTION_SEARCH, null);
        } else if (v.equals(ivVoice)) {
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                // 语音按钮被点击
                listener.onClicked(v, ACTION_SEARCH_VOICE, null);
            } else {
                etSearchHint.setText("");
                listener.onClicked(v, ACTION_SEARCH_DELETE, null);
            }
        } else if (v.equals(tvCenter)) {
            listener.onClicked(v, ACTION_CENTER_TEXT, null);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    @Override
    public void setBackgroundColor(int color) {
        rlMain.setBackgroundColor(color);
    }

    /**
     * 设置背景图片
     *
     * @param resource
     */
    @Override
    public void setBackgroundResource(int resource) {
        setBackgroundColor(Color.TRANSPARENT);
        super.setBackgroundResource(resource);
    }

    /**
     * 获取标题栏底部横线
     *
     * @return
     */
    public View getButtomLine() {
        return viewBottomLine;
    }

    /**
     * 获取标题栏左边TextView，对应leftType = textView
     *
     * @return
     */
    public TextView getLeftTextView() {
        return tvLeft;
    }

    /**
     * 获取标题栏左边ImageButton，对应leftType = imageButton
     *
     * @return
     */
    public ImageButton getLeftImageButton() {
        return btnLeft;
    }

    /**
     * 获取标题栏右边TextView，对应rightType = textView
     *
     * @return
     */
    public TextView getRightTextView() {
        return tvRight;
    }

    /**
     * 获取标题栏右边ImageButton，对应rightType = imageButton
     *
     * @return
     */
    public ImageButton getRightImageButton() {
        return btnRight;
    }

    public LinearLayout getCenterLayout() {
        return llMainCenter;
    }

    /**
     * 获取标题栏中间TextView，对应centerType = textView
     *
     * @return
     */
    public TextView getCenterTextView() {
        return tvCenter;
    }

    public TextView getCenterSubTextView() {
        return tvCenterSub;
    }

    /**
     * 获取搜索框布局，对应centerType = searchView
     *
     * @return
     */
    public RelativeLayout getCenterSearchView() {
        return rlMainCenterSearch;
    }

    /**
     * 获取搜索框内部输入框，对应centerType = searchView
     *
     * @return
     */
    public EditText getCenterSearchEditText() {
        return etSearchHint;
    }

    /**
     * 获取搜索框右边图标ImageView，对应centerType = searchView
     *
     * @return
     */
    public ImageView getCenterSearchRightImageView() {
        return ivVoice;
    }

    public ImageView getCenterSearchLeftImageView() {
        return ivSearch;
    }

    /**
     * 获取左边自定义布局
     *
     * @return
     */
    public View getLeftCustomView() {
        return viewCustomLeft;
    }

    /**
     * 获取右边自定义布局
     *
     * @return
     */
    public View getRightCustomView() {
        return viewCustomRight;
    }

    /**
     * 获取中间自定义布局视图
     *
     * @return
     */
    public View getCenterCustomView() {
        return centerCustomView;
    }

    /**
     * @param leftView
     */
    public void setLeftView(View leftView) {
        if (leftView.getId() == View.NO_ID) {
            leftView.setId(generateViewId());
        }
        LayoutParams leftInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        leftInnerParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        leftInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlMain.addView(leftView, leftInnerParams);
    }

    /**
     * @param centerView
     */
    public void setCenterView(View centerView) {
        if (centerView.getId() == View.NO_ID) {
            centerView.setId(generateViewId());
        }
        LayoutParams centerInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        centerInnerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        centerInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlMain.addView(centerView, centerInnerParams);
    }

    /**
     * @param rightView
     */
    public void setRightView(View rightView) {
        if (rightView.getId() == View.NO_ID) {
            rightView.setId(generateViewId());
        }
        LayoutParams rightInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        rightInnerParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        rightInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rlMain.addView(rightView, rightInnerParams);
    }

    /**
     * 显示中间进度条
     */
    public void showCenterProgress() {
        progressCenter.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏中间进度条
     */
    public void dismissCenterProgress() {
        progressCenter.setVisibility(View.GONE);
    }

    /**
     * 显示或隐藏输入法,centerType="searchView"模式下有效
     */
    public void showSoftInputKeyboard(boolean show) {
        if (centerSearchEditable && show) {
            etSearchHint.setFocusable(true);
            etSearchHint.setFocusableInTouchMode(true);
            etSearchHint.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etSearchHint, InputMethodManager.SHOW_FORCED);
            }
        } else {
            if (etSearchHint != null) {
                IBinder binder = etSearchHint.getWindowToken();
                if (binder != null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
            }
        }
    }

    /**
     * 设置搜索框右边图标
     *
     * @param res
     */
    public void setSearchRightImageResource(int res) {
        if (ivVoice != null) {
            ivVoice.setImageResource(res);
        }
    }

    /**
     * 获取SearchView输入结果
     */
    public String getSearchKey() {
        if (etSearchHint != null) {
            return etSearchHint.getText().toString();
        }
        return "";
    }

    /**
     * 设置点击事件监听
     *
     * @param listener
     */

    public void setListener(OnTitleBarListener listener) {
        this.listener = listener;
    }

    /**
     * 设置双击监听
     */
    public void setDoubleClickListener(OnTitleBarDoubleClickListener doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }


    public static final int ACTION_LEFT_TEXT = 1;        // 左边TextView被点击
    public static final int ACTION_LEFT_BUTTON = 2;      // 左边ImageBtn被点击
    public static final int ACTION_RIGHT_TEXT = 3;       // 右边TextView被点击
    public static final int ACTION_RIGHT_BUTTON = 4;     // 右边ImageBtn被点击
    public static final int ACTION_SEARCH = 5;           // 搜索框被点击,搜索框不可输入的状态下会被触发
    public static final int ACTION_SEARCH_SUBMIT = 6;    // 搜索框输入状态下,键盘提交触发
    public static final int ACTION_SEARCH_VOICE = 7;     // 语音按钮被点击
    public static final int ACTION_SEARCH_DELETE = 8;    // 搜索删除按钮被点击
    public static final int ACTION_CENTER_TEXT = 9;     // 中间文字点击

    /**
     * 点击事件
     */
    public interface OnTitleBarListener {
        /**
         * @param view
         * @param action 对应ACTION_XXX, 如ACTION_LEFT_TEXT
         * @param extra  中间为搜索框时,如果可输入,点击键盘的搜索按钮,会返回输入关键词
         */
        void onClicked(View view, int action, String extra);
    }

    /**
     * 标题栏双击事件监听
     */
    public interface OnTitleBarDoubleClickListener {
        void onClicked(View v);
    }

    /**
     * 计算View Id
     */
    @SuppressLint("ObsoleteSdkInt")
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId();
        } else {
            return UUID.randomUUID().hashCode();
        }
    }
}
