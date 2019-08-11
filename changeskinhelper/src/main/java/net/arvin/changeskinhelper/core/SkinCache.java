package net.arvin.changeskinhelper.core;

import android.content.res.Resources;

/**
 * Created by arvinljw on 2019-07-17 16:22
 * Function：
 * Desc：
 */
public class SkinCache {
    private Resources skinResource;
    private String skinPackageName;
    private String skinSuffix;

    public SkinCache(Resources skinResource, String skinPackageName, String skinSuffix) {
        this.skinResource = skinResource;
        this.skinPackageName = skinPackageName;
        this.skinSuffix = skinSuffix;
    }

    public Resources getSkinResource() {
        return skinResource;
    }

    public String getSkinPackageName() {
        return skinPackageName;
    }

    public String getSkinSuffix() {
        return skinSuffix;
    }
}
