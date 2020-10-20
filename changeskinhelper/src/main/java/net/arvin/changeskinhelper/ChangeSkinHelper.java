package net.arvin.changeskinhelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.arvin.changeskinhelper.core.ChangeCustomSkinListener;
import net.arvin.changeskinhelper.core.ChangeSkinListener;
import net.arvin.changeskinhelper.core.SkinResId;
import net.arvin.changeskinhelper.core.SkinResourceProcessor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arvinljw on 2019-08-02 16:18
 * Function：
 * Desc：
 * 2、自定义view的自定义属性
 */
public class ChangeSkinHelper {

    public static String KEY_SKIN_PATH = "skin_path";
    public static String KEY_SKIN_SUFFIX = "skin_suffix";
    private static List<ChangeSkinListener> changeSkinListeners = new ArrayList<>();

    private static int[] attrIds = {
            android.R.attr.background,
            android.R.attr.src,
            R.attr.csh_textColor,
            R.attr.csh_typeface,
            android.R.attr.windowBackground,
    };

    //todo 反射：这里使用反射创建View
    private static final Map<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();
    private static final Object[] mConstructorArgs = new Object[2];
    private static final Class<?>[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    public static void init(Application application) {
        SkinResourceProcessor.init(application);
    }

    public static void addListener(ChangeSkinListener listener) {
        changeSkinListeners.add(listener);
    }

    public static void notifyListener(final String skinPath, final String skinSuffix) {
        long startTime = System.currentTimeMillis();
        loadSkinResources(skinPath, skinSuffix);
        for (ChangeSkinListener listener : changeSkinListeners) {
            listener.changeSkin();
        }
        Log.e("ChangeSkin", "需要时间" + (System.currentTimeMillis() - startTime));
    }

    public static void removeListener(ChangeSkinListener listener) {
        changeSkinListeners.remove(listener);
    }

    public static View onCreateView(AppCompatActivity activity, LayoutInflater inflater, View parent, String name, Context context, AttributeSet attrs) {
        View view = activity.getDelegate().createView(parent, name, context, attrs);
        if (view == null) {
            view = createViewFromTag(context, name, attrs);
        }
        if (view != null) {
            setSkinTag(context, attrs, view);
            setSkin(view);
        }

        return view;
    }

    //不能使用反射调用inflater的onCreateView方法，有的Layout会创建失败
    private static View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }
        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;
            if (-1 == name.indexOf('.')) {
                return createView(context, name, "android.widget.");
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            return null;
        } finally {
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private static View createView(Context context, String name, String prefix) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        try {
            if (constructor == null) {
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);
                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("ResourceType")
    private static void setSkinTag(Context context, AttributeSet attrs, View view) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, attrIds);
        SkinResId skin = new SkinResId();
        int defValue = -1;
        int background = typedArray.getResourceId(0, defValue);
        int windowBackground = typedArray.getResourceId(4, defValue);
        if (background == -1) {
            background = windowBackground;
        }
        skin.setBackground(background);
        skin.setSrc(typedArray.getResourceId(1, defValue));
        int textColorResId = typedArray.getResourceId(2, defValue);
        if (textColorResId == -1 && view instanceof TextView) {
            textColorResId = ChangeSkinHelper.getTextColorResId(attrs);
        }
        skin.setTextColor(textColorResId);
        skin.setTypeface(typedArray.getResourceId(3, defValue));
        typedArray.recycle();
        view.setTag(R.id.change_skin_tag, skin);

        if (view instanceof ChangeCustomSkinListener) {
            ChangeCustomSkinListener changeCustomSkinListener = (ChangeCustomSkinListener) view;
            changeCustomSkinListener.setCustomTag();
        }
    }

    public static void loadSkinResources(String skinPath, String skinSuffix) {
        SkinResourceProcessor.getInstance().loadSkinResources(skinPath, skinSuffix);
    }

    public static void applyViews(View view) {
        setSkin(view);

        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                applyViews(parent.getChildAt(i));
            }
        }
    }

    public static void setSkin(View view) {
        if (view == null) {
            return;
        }
        Object tag = view.getTag(R.id.change_skin_tag);
        if (tag != null) {
            SkinResId skin = (SkinResId) tag;
            int background = skin.getBackground();
            if (background != -1) {
                ChangeSkinHelper.setBackground(view, background);
            }

            int textColor = skin.getTextColor();
            if (textColor != -1) {
                if (view instanceof TextView) {
                    ChangeSkinHelper.setTextColor((TextView) view, textColor);
                }
            }
            int src = skin.getSrc();
            if (src != -1) {
                if (view instanceof ImageView) {
                    ChangeSkinHelper.setSrc((ImageView) view, src);
                }
            }
            int typeface = skin.getTypeface();
            if (typeface != -1) {
                if (view instanceof TextView) {
                    ChangeSkinHelper.setTypeface((TextView) view, typeface);
                }
            }
            if (view instanceof ChangeCustomSkinListener) {
                ChangeCustomSkinListener changeCustomSkinListener = (ChangeCustomSkinListener) view;
                changeCustomSkinListener.changeCustomSkin();
            }
        }
    }

    /**
     * @param resId 长度为4，依次为background，src，textColor，typeface
     *              例如要设置textColor，background和src不设置则可使用0占位
     */
    public static void setViewTag(View view, int... resId) {
        if (view == null || resId == null || resId.length == 0) {
            return;
        }
        Object temp = view.getTag(R.id.change_skin_tag);
        SkinResId tag;
        if (temp instanceof SkinResId) {
            tag = (SkinResId) temp;
        } else {
            tag = new SkinResId();
        }
        tag.setBackground(resId[0]);
        if (resId.length > 1) {
            tag.setSrc(resId[1]);
        }
        if (resId.length > 2) {
            tag.setTextColor(resId[2]);
        }
        if (resId.length > 3) {
            tag.setTypeface(resId[3]);
        }
        view.setTag(R.id.change_skin_tag, tag);
    }

    public static void setViewBackground(View view, int background) {
        if (view == null) {
            return;
        }
        Object tag = view.getTag(R.id.change_skin_tag);
        if (tag instanceof SkinResId) {
            SkinResId skinResId = (SkinResId) tag;
            skinResId.setBackground(background);
            view.setTag(skinResId);
        }
    }

    public static void setViewSrc(ImageView imageView, int src) {
        if (imageView == null) {
            return;
        }
        Object tag = imageView.getTag(R.id.change_skin_tag);
        if (tag instanceof SkinResId) {
            SkinResId skinResId = (SkinResId) tag;
            skinResId.setSrc(src);
            imageView.setTag(skinResId);
        }
    }

    public static void setViewTextColor(TextView textView, int textColor) {
        if (textView == null) {
            return;
        }
        Object tag = textView.getTag(R.id.change_skin_tag);
        if (tag instanceof SkinResId) {
            SkinResId skinResId = (SkinResId) tag;
            skinResId.setTextColor(textColor);
            textView.setTag(skinResId);
        }
    }

    /**
     * @param typeface typeface的资源id，是一个string资源，内容是字体在assets中的路径
     */
    public static void setViewTypeface(TextView textView, int typeface) {
        if (textView == null) {
            return;
        }
        Object tag = textView.getTag(R.id.change_skin_tag);
        if (tag instanceof SkinResId) {
            SkinResId skinResId = (SkinResId) tag;
            skinResId.setTypeface(typeface);
            textView.setTag(skinResId);
        }
    }

    public static void setCustomResId(View view, int customResId) {
        Object temp = view.getTag(R.id.change_skin_tag);
        SkinResId tag;
        if (temp instanceof SkinResId) {
            tag = (SkinResId) temp;
        } else {
            tag = new SkinResId();
        }
        tag.setCustomResId(customResId);
        view.setTag(R.id.change_skin_tag, tag);
    }

    public static void setCustomResIds(View view, int... customResIds) {
        Object temp = view.getTag(R.id.change_skin_tag);
        SkinResId tag;
        if (temp instanceof SkinResId) {
            tag = (SkinResId) temp;
        } else {
            tag = new SkinResId();
        }
        List<Integer> resIds = new ArrayList<>();
        for (int customResId : customResIds) {
            resIds.add(customResId);
        }
        tag.setCustomResIds(resIds);
        view.setTag(R.id.change_skin_tag, tag);
    }

    public static int getSkinCustomResId(View view) {
        if (view == null) {
            return 0;
        }
        Object temp = view.getTag(R.id.change_skin_tag);
        if (temp instanceof SkinResId) {
            SkinResId tag = (SkinResId) temp;
            return tag.getCustomResId();
        }
        return 0;
    }

    public static List<Integer> getSkinCustomResIds(View view) {
        if (view == null) {
            return null;
        }
        Object temp = view.getTag(R.id.change_skin_tag);
        if (temp instanceof SkinResId) {
            SkinResId tag = (SkinResId) temp;
            return tag.getCustomResIds();
        }
        return null;
    }

    public static void setBackground(View view, int backgroundResourceId) {
        if (backgroundResourceId > 0) {
            // 是否默认皮肤
            SkinResourceProcessor skinResProcessor = SkinResourceProcessor.getInstance();
            if (skinResProcessor.usingDefaultSkin() && skinResProcessor.usingInnerAppSkin()) {
                // 兼容包转换
                Drawable drawable = ContextCompat.getDrawable(view.getContext(), backgroundResourceId);
                // 控件自带api，这里不用setBackgroundColor()因为在9.0测试不通过
                // setBackgroundDrawable本来过时了，但是兼容包重写了方法
                view.setBackgroundDrawable(drawable);
            } else {
                // 获取皮肤包资源
                Object skinResourceId = skinResProcessor.getBackgroundOrSrc(backgroundResourceId);
                // 兼容包转换
                if (skinResourceId instanceof Integer) {
                    int color = (int) skinResourceId;
                    view.setBackgroundColor(color);
//                     setBackgroundResource(color); // 未做兼容测试
                } else {
                    Drawable drawable = (Drawable) skinResourceId;
                    view.setBackgroundDrawable(drawable);
                }
            }
        }
    }

    public static void setSrc(ImageView view, int srcResourceId) {
        if (srcResourceId > 0) {
            // 是否默认皮肤
            SkinResourceProcessor skinResProcessor = SkinResourceProcessor.getInstance();
            if (skinResProcessor.usingDefaultSkin() && skinResProcessor.usingInnerAppSkin()) {
                // 兼容包转换
                view.setImageResource(srcResourceId);
                Drawable drawable = ContextCompat.getDrawable(view.getContext(), srcResourceId);
                view.setImageDrawable(drawable);
            } else {
                // 获取皮肤包资源
                Object skinResourceId = skinResProcessor.getBackgroundOrSrc(srcResourceId);
                // 兼容包转换
                if (skinResourceId instanceof Integer) {
                    int color = (int) skinResourceId;
                    view.setImageResource(color);
                    // setImageBitmap(); // Bitmap未添加
                } else {
                    Drawable drawable = (Drawable) skinResourceId;
                    view.setImageDrawable(drawable);
                }
            }
        }
    }

    public static void setTextColor(TextView view, int textColorResourceId) {
        if (textColorResourceId > 0) {
            SkinResourceProcessor skinResProcessor = SkinResourceProcessor.getInstance();
            if (skinResProcessor.usingDefaultSkin() && skinResProcessor.usingInnerAppSkin()) {
                ColorStateList color = ContextCompat.getColorStateList(view.getContext(), textColorResourceId);
                view.setTextColor(color);
            } else {
                ColorStateList color = skinResProcessor.getColorStateList(textColorResourceId);
                view.setTextColor(color);
            }
        }
    }

    public static void setTypeface(TextView view, int textTypefaceResourceId) {
        if (textTypefaceResourceId > 0) {
            SkinResourceProcessor skinResProcessor = SkinResourceProcessor.getInstance();
            if (skinResProcessor.usingDefaultSkin() && skinResProcessor.usingInnerAppSkin()) {
                view.setTypeface(Typeface.DEFAULT);
            } else {
                view.setTypeface(skinResProcessor.getTypeface(textTypefaceResourceId));
            }
        }
    }

    public static int getTextColorResId(AttributeSet attrs) {
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (!attrName.equals("textColor")) {
                continue;
            }
            if (attrValue.startsWith("@")) {
                return Integer.parseInt(attrValue.substring(1));
            }
        }
        return -1;
    }

    public static int getColor(int resourceId) {
        return SkinResourceProcessor.getInstance().getColor(resourceId);
    }

    public static ColorStateList getColorStateList(int resourceId) {
        return SkinResourceProcessor.getInstance().getColorStateList(resourceId);
    }

    public static Drawable getDrawableOrMipMap(int resourceId) {
        return SkinResourceProcessor.getInstance().getDrawableOrMipMap(resourceId);
    }

    public static String getString(int resourceId) {
        return SkinResourceProcessor.getInstance().getString(resourceId);
    }

    // 返回值特殊情况：可能是color / drawable / mipmap
    public static Object getBackgroundOrSrc(int resourceId) {
        return SkinResourceProcessor.getInstance().getBackgroundOrSrc(resourceId);
    }

    // 获得字体
    public static Typeface getTypeface(int resourceId) {
        return SkinResourceProcessor.getInstance().getTypeface(resourceId);
    }

    public static void setNavigation(Activity activity, @ColorRes int navigationBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(getColor(navigationBarColor));
        }
    }

    public static void setActionBar(AppCompatActivity activity, @ColorRes int actionBarColor) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(actionBarColor)));
        }
    }
}
