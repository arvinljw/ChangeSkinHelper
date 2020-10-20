## 自定义View换肤

自定义View的换肤，其实自定义View中支持的属性是可以换的，例如背景颜色，文字颜色，src，字体等，这里主要是解决自定义属性的换肤。

自定义View换肤需要：

* 实现`ChangeCustomSkinListener`接口，实现`setCustomTag`和`changeCustomSkin`方法
* `setCustomTag`方法就是将需要换肤的自定义属性资源放入到tag中，例如只有一个自定义属性需要设置，可以使用`ChangeSkinHelper.setCustomResId`方法，如果有多个自定义属性则使用`ChangeSkinHelper.setCustomResIds`方法，传入的就是自定义属性资源id即可。
* `changeCustomSkin`方法就是换肤时的回调监听，这时候需要从设置在的tag中自定义属性资源获取出来，然后调用`ChangeSkinHelper`中获取对应皮肤资源方法，并设置给view即可。

获取对应皮肤资源的方法包括：

* public static int getColor(int resourceId) 获取color颜色资源

* public static ColorStateList getColorStateList(int resourceId) 获取ColorStateList颜色资源

* public static Drawable getDrawableOrMipMap(int resourceId) 获取drawable或者是mipmap资源，可以是xml写的drawable或者是图片资源

* public static String getString(int resourceId) 获取文字资源（主要用于字体）

* public static Object getBackgroundOrSrc(int resourceId) 获取背景资源，有可能是drawable有可能是color

* public static Typeface getTypeface(int resourceId) 获取字体资源

这样就基本实现了自定义View的自定义属性换肤，当然具体的内容还是查看demo中的CustomView实现即可。