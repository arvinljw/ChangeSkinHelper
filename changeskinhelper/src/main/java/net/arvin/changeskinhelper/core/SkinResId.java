package net.arvin.changeskinhelper.core;

import java.util.List;

/**
 * Created by arvinljw on 2019-08-02 16:39
 * Function：
 * Desc：
 */
public class SkinResId {
    private int background;
    private int src;
    private int textColor;
    private int typeface;

    private int customResId;
    //不止一个时可以使用customResIds来存储
    private List<Integer> customResIds;

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTypeface() {
        return typeface;
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
    }

    public int getCustomResId() {
        return customResId;
    }

    public void setCustomResId(int customResId) {
        this.customResId = customResId;
    }

    public List<Integer> getCustomResIds() {
        return customResIds;
    }

    public void setCustomResIds(List<Integer> customResIds) {
        this.customResIds = customResIds;
    }

    @Override
    public String toString() {
        return "SkinResId{" +
                "background=" + background +
                ", src=" + src +
                ", textColor=" + textColor +
                ", typeface=" + typeface +
                ", customResId=" + customResId +
                ", customResIds=" + customResIds +
                '}';
    }

}
