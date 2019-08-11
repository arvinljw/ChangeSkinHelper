package net.arvin.changeskinhelper.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import net.arvin.changeskinhelper.ChangeSkinHelper;


/**
 * Created by arvinljw on 2019-08-02 16:21
 * Function：
 * Desc：LICENSE
 */
public class ChangeSkinActivity extends AppCompatActivity implements ChangeSkinListener {
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isChangeSkin()) {
            ChangeSkinHelper.init(getApplication());
            inflater = LayoutInflater.from(this);
            LayoutInflaterCompat.setFactory2(inflater, this);
            ChangeSkinHelper.addListener(this);
            String skinPath = ChangeSkinPreferenceUtil.getString(getApplicationContext(), ChangeSkinHelper.KEY_SKIN_PATH);
            String suffix = ChangeSkinPreferenceUtil.getString(getApplicationContext(), ChangeSkinHelper.KEY_SKIN_SUFFIX);
            ChangeSkinHelper.loadSkinResources(skinPath, suffix);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (!isChangeSkin()) {
            return super.onCreateView(parent, name, context, attrs);
        }
        return ChangeSkinHelper.onCreateView(this, inflater, parent, name, context, attrs);
    }

    /**
     * @return 是否需要开启换肤，默认是false，true表示开启
     */
    protected boolean isChangeSkin() {
        return false;
    }

    protected void defaultSkin() {
        ChangeSkinHelper.notifyListener(null, "");
    }

    protected void dynamicSkin(String skinSuffix) {
        ChangeSkinHelper.notifyListener(null, skinSuffix);
    }

    protected void dynamicSkin(String skinPath, String skinSuffix) {
        ChangeSkinHelper.notifyListener(skinPath, skinSuffix);
    }

    @Override
    public void changeSkin() {
        ChangeSkinHelper.applyViews(getWindow().getDecorView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChangeSkinHelper.removeListener(this);
    }
}
