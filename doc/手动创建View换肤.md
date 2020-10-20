## 手动创建View换肤

这里的手动创建的View是指不通过inflate布局文件创建，例如直接new一个View，然后加到某个view里显示。

这时候就需要手动给该View设置一个换肤的tag，执行换肤时知道该View也是需要换肤的，例如：

```
TextView textView = new TextView(this);
textView.setText("我是手动创建的view");
textView.setTextColor(ChangeSkinHelper.getColor(R.color.colorAccent));
textView.setTypeface(ChangeSkinHelper.getTypeface(R.string.custom_typeface));
ChangeSkinHelper.setViewTag(textView, -1, -1, R.color.colorAccent, R.string.custom_typeface);
```

这里需要注意的是，可以换肤的包括textColor和字体，所以在设置的时候需要调用`ChangeSkinHelper.getColor`和`ChangeSkinHelper.getTypeface`方法传入文字颜色和字体资源，返回对应的文字颜色和字体，因为可能这时候使用的不是默认皮肤，所以需要动态获取，而不能直接使用`getResources().getColor`之类的来获取，因为之前设置的皮肤有可能是插件包里的皮肤。

这里目前支持的获取皮肤资源的方法包含：


* public static int getColor(int resourceId) 获取color颜色资源

* public static ColorStateList getColorStateList(int resourceId) 获取ColorStateList颜色资源

* public static Drawable getDrawableOrMipMap(int resourceId) 获取drawable或者是mipmap资源，可以是xml写的drawable或者是图片资源

* public static String getString(int resourceId) 获取文字资源（主要用于字体）

* public static Object getBackgroundOrSrc(int resourceId) 获取背景资源，有可能是drawable有可能是color

* public static Typeface getTypeface(int resourceId) 获取字体资源

具体如何可参考demo中的例子（在app下的MainActivity的onCreate方法中）。
