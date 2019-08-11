package net.arvin.changeskinhelper.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.arvin.changeskinhelper.ChangeSkinHelper;
import net.arvin.changeskinhelper.core.ChangeSkinActivity;
import net.arvin.changeskinhelper.core.ChangeSkinPreferenceUtil;
import net.arvin.permissionhelper.PermissionUtil;

import java.io.File;

public class MainActivity extends ChangeSkinActivity {
    //默认是0，1是dark，2是light，3是blue，来自动态加载的皮肤包
    private int currSkinIndex = 0;

    private PermissionUtil permissionUtil;

    protected boolean isChangeSkin() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = findViewById(R.id.layout_main);
        frameLayout.setBackgroundColor(ChangeSkinHelper.getColor(R.color.colorPrimary));
        ChangeSkinHelper.setViewTag(frameLayout, R.color.colorPrimary);

        TextView textView = new TextView(this);
        textView.setText("我是手动创建的view");
        textView.setTextColor(ChangeSkinHelper.getColor(R.color.colorAccent));
        textView.setTypeface(ChangeSkinHelper.getTypeface(R.string.custom_typeface_string));
        ChangeSkinHelper.setViewTag(textView, -1, -1, R.color.colorAccent, R.string.custom_typeface_string);
        frameLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        String suffix = ChangeSkinPreferenceUtil.getString(getApplicationContext(), ChangeSkinHelper.KEY_SKIN_SUFFIX);
        currSkinIndex = getSkinIndexBySuffix(suffix);

    }

    public static int getSkinIndexBySuffix(String suffix) {
        int skinIndex = 0;
        switch (suffix) {
            case "":
                skinIndex = 0;
                break;
            case "_dark":
                skinIndex = 1;
                break;
            case "_light":
                skinIndex = 2;
                break;
            case "_blue":
                skinIndex = 3;
                break;
        }
        return skinIndex;
    }

    public static String getSuffixBySkinIndex(int skinIndex) {
        String suffix;
        switch (skinIndex) {
            case 0:
            default:
                suffix = "";
                break;
            case 1:
                suffix = "_dark";
                break;
            case 2:
                suffix = "_light";
                break;
            case 3:
                suffix = "_blue";
                break;
        }
        return suffix;
    }

    public void changeSkin(View view) {
        currSkinIndex = (currSkinIndex + 1) % 4;
        String suffix = getSuffixBySkinIndex(currSkinIndex);
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

    public void toSecond(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }


}
