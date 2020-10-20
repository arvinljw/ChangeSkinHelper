package net.arvin.changeskinhelper.core;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import net.arvin.changeskinhelper.ChangeSkinHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arvinljw on 2019-08-09 14:16
 * Function：
 * Desc：
 */
public class SkinResourceProcessor {
    private static final String TAG = SkinResourceProcessor.class.getSimpleName();
    //todo 反射：这里用的反射加载外部资源
    private static final String METHOD_ADD_ASSETS_PATH = "addAssetPath";
    private static SkinResourceProcessor instance;
    private static Method addAssetsPath;
    private Application application;
    private Resources appResources;
    private Resources skinResources;
    private String skinPackageName;
    private String skinPath;
    /*是否使用默认资源或app内资源，根据是否包含skinSuffix确定是否跟换资源*/
    private boolean usingDefaultSkin = true;
    private String skinSuffix;
    private Map<String, SkinCache> skinCacheMap;

    private SkinResourceProcessor(Application application) {
        this.application = application;
        appResources = application.getResources();
        skinCacheMap = new HashMap<>();
    }

    /**
     * 单例方法，目的是初始化app内置资源（越早越好，用户的操作可能是：换肤后的第2次冷启动）
     */
    public static void init(Application application) {
        if (instance == null) {
            synchronized (ChangeSkinHelper.class) {
                if (instance == null) {
                    instance = new SkinResourceProcessor(application);
                }
            }
        }
    }

    public static SkinResourceProcessor getInstance() {
        return instance;
    }

    public boolean usingDefaultSkin() {
        return usingDefaultSkin;
    }

    public boolean usingInnerAppSkin() {
        return TextUtils.isEmpty(skinSuffix);
    }

    public void loadSkinResources(String skinPath, String skinSuffix) {
        ChangeSkinPreferenceUtil.putString(application, ChangeSkinHelper.KEY_SKIN_PATH, skinPath);
        ChangeSkinPreferenceUtil.putString(application, ChangeSkinHelper.KEY_SKIN_SUFFIX, skinSuffix);
        this.skinSuffix = skinSuffix;
        this.skinPath = skinPath;
        if (TextUtils.isEmpty(skinPath)) {
            usingDefaultSkin = true;
            return;
        }
        if (skinCacheMap.containsKey(skinPath)) {
            usingDefaultSkin = false;
            SkinCache skinCache = skinCacheMap.get(skinPath);
            if (skinCache == null) {
                usingDefaultSkin = true;
                return;
            }
            skinResources = skinCache.getSkinResource();
            skinPackageName = skinCache.getSkinPackageName();
            return;
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            if (addAssetsPath == null) {
                addAssetsPath = assetManager.getClass().getDeclaredMethod(METHOD_ADD_ASSETS_PATH, String.class);
                addAssetsPath.setAccessible(true);
            }
            addAssetsPath.invoke(assetManager, skinPath);

            skinResources = new Resources(assetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());
            PackageInfo packageInfo = application.getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
            skinPackageName = packageInfo.packageName;
            usingDefaultSkin = TextUtils.isEmpty(skinPackageName);

            if (!usingDefaultSkin) {
                skinCacheMap.put(skinPath, new SkinCache(skinResources, skinPackageName, skinSuffix));
            }
        } catch (Exception e) {
            e.printStackTrace();
            usingDefaultSkin = true;
        }
    }

    public int getSkinResourceId(int resourceId) {
        String resourceName = appResources.getResourceEntryName(resourceId);
        String resourceType = appResources.getResourceTypeName(resourceId);
        String skinResourceName = resourceName + skinSuffix;
        if (TextUtils.isEmpty(skinPath)) {
            if (!TextUtils.isEmpty(skinSuffix)) {
                int skinResourceId = appResources.getIdentifier(skinResourceName, resourceType, application.getPackageName());
                return skinResourceId == 0 ? resourceId : skinResourceId;
            }
            return resourceId;
        }

        int skinResourceId = skinResources.getIdentifier(skinResourceName, resourceType, skinPackageName);
        usingDefaultSkin = skinResourceId == 0;
        if (usingDefaultSkin) {
            Log.i(TAG, "皮肤包中资源：" + skinResourceName + " 未不到，将使用默认资源：" + resourceName);
        }
        return usingDefaultSkin ? resourceId : skinResourceId;
    }

    public int getColor(int resourceId) {
        int resId = getSkinResourceId(resourceId);
        return usingDefaultSkin ? appResources.getColor(resId) : skinResources.getColor(resId);
    }

    public ColorStateList getColorStateList(int resourceId) {
        int resId = getSkinResourceId(resourceId);
        return usingDefaultSkin ? appResources.getColorStateList(resId) : skinResources.getColorStateList(resId);
    }

    public Drawable getDrawableOrMipMap(int resourceId) {
        int resId = getSkinResourceId(resourceId);
        return usingDefaultSkin ? appResources.getDrawable(resId) : skinResources.getDrawable(resId);
    }

    public String getString(int resourceId) {
        int resId = getSkinResourceId(resourceId);
        return usingDefaultSkin ? appResources.getString(resId) : skinResources.getString(resId);
    }

    // 返回值特殊情况：可能是color / drawable / mipmap
    public Object getBackgroundOrSrc(int resourceId) {
        // 需要获取当前属性的类型名Resources.getResourceTypeName(resourceId)再判断
        String resourceType = appResources.getResourceTypeName(resourceId);
        switch (resourceType) {
            case "color":
                return getColor(resourceId);
            case "mipmap": // drawable / mipmap
            case "drawable":
                return getDrawableOrMipMap(resourceId);
        }
        return null;
    }

    // 获得字体
    public Typeface getTypeface(int resourceId) {
        // 通过资源ID获取资源path，参考：resources.arsc资源映射表
        String skinTypefacePath = getString(resourceId);
        // 路径为空，使用系统默认字体
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        return usingDefaultSkin ? Typeface.createFromAsset(appResources.getAssets(), skinTypefacePath)
                : Typeface.createFromAsset(skinResources.getAssets(), skinTypefacePath);
    }
}
