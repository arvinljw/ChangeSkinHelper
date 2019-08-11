package net.arvin.changeskinhelper.sample;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.arvin.changeskinhelper.ChangeSkinHelper;
import net.arvin.changeskinhelper.core.ChangeSkinActivity;
import net.arvin.changeskinhelper.core.ChangeSkinPreferenceUtil;
import net.arvin.permissionhelper.PermissionUtil;

import java.io.File;

public class SecondActivity extends ChangeSkinActivity {

    private int currSkinIndex;
    private PermissionUtil permissionUtil;

    @Override
    protected boolean isChangeSkin() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setBarsColor();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_main, new MainFragment()).commit();
        String suffix = ChangeSkinPreferenceUtil.getString(getApplicationContext(), ChangeSkinHelper.KEY_SKIN_SUFFIX);
        currSkinIndex = MainActivity.getSkinIndexBySuffix(suffix);
    }


    public void changeSkin(View view) {
        currSkinIndex = (currSkinIndex + 1) % 4;
        String suffix = MainActivity.getSuffixBySkinIndex(currSkinIndex);
        if (currSkinIndex == 3) {
            final String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "blue.skin";
            if (permissionUtil == null) {
                permissionUtil = new PermissionUtil.Builder().with(this).build();
            }
            final String tempSuffix = suffix;
            permissionUtil.request("需要读取文件权限", Manifest.permission.READ_EXTERNAL_STORAGE,
                    new PermissionUtil.RequestPermissionListener() {
                        @Override
                        public void callback(boolean granted, boolean isAlwaysDenied) {
                            dynamicSkin(skinPath, tempSuffix);
                        }
                    });
        } else {
            dynamicSkin(suffix);
        }
    }

    public void defaultSkin(View view) {
        defaultSkin();
        currSkinIndex = 0;
    }

    @Override
    public void changeSkin() {
        super.changeSkin();
        setBarsColor();
    }

    private void setBarsColor() {
        StatusBarUtil.setColorNoTranslucent(this, ChangeSkinHelper.getColor(R.color.colorPrimary));
        ChangeSkinHelper.setNavigation(this, R.color.colorPrimary);
        ChangeSkinHelper.setActionBar(this, R.color.colorPrimary);
    }
}
