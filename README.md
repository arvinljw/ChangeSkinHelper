## ChangeSkinHelper

这是一个Android换肤的库，提供一下功能：

* **无需重启，一键换肤效率高**
* **支持App内多套皮肤换肤**
* **支持插件式动态换肤**
* **支持Activity，Fragment，以及使用LayoutInflater创建的View换肤**
* **支持手动创建的View换肤**
* **支持RecyclerView换肤**
* **支持自定义View换肤**

### 使用

#### 依赖

**1、在根目录的build.gradle中加入如下配置**

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**2、在要是用的module中增加如下引用**

```
dependencies {
    ...
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.github.arvinljw:ChangeSkinHelper:v1.0.2'
}
```

#### 资源定义

换肤，就像字面意思换的大多无非就是颜色，颜色里又可以包含文字**背景颜色和不同颜色图片资源**，当然除此之外还可以换字体等。本库支持的资源类型包含：

* color
* drawable和mipmap
* string

*其中字体资源目前只能放在assets目录中，路径需要在string中定义*下文中详细介绍。

其实**app内和插件皮肤包**中的**资源定义**方式都是一致的，只是位置不一样，插件皮肤包需要先加载，app内的可以直接使用而已。

本库，定义不同皮肤的标准是通过**后缀**，例如默认颜色资源`colorAccent`，皮肤颜色资源就需要使用原资源名字加后缀，以后缀`_light`为例，light皮肤就是`colorAccent_light`，也就是资源名字对应为：

* color资源

	默认：`colorAccent` 
	
	light皮肤：`colorAccent_light`
	
* drawable资源

	默认：`img_avatar`
	
	light皮肤：`img_avatar_light`

* string资源

	默认：`custom_typeface`
	
	light皮肤：`custom_typeface_light`

其中字体的设置比较特殊，建议在app的style中加入`csh_typeface`属性进行全局设置：

```
<item name="csh_typeface">@string/custom_typeface</item>
```

这里的`custom_typeface`资源就是指字体在assets目录中的路径，没有填写目录时表示使用默认字体。

**皮肤资源的定义**：在默认资源后加后缀定义某类皮肤，例如各类xml写的drawable也是一样，当然xml里边写的资源就需要自己手动换了。

*需要注意的是，如果皮肤里没有找到对应资源就会使用默认资源*

#### 换肤

资源的定义都完成了，那么换肤的工作就基本完成了百分之99。剩下的功能就是简单的三步：

* 在需要换肤的Activity继承`ChangeSkinActivity`，
* 重写`isChangeSkin`方法并返回true即可，默认是不开启换肤的
* 需要换肤时，调用父类的`dynamicSkin`方法

`dynamicSkin`该方法有两个重载方法：

* `protected void dynamicSkin(String skinSuffix)` 该方法是换app内的皮肤的，传入皮肤后缀就可以了。
* `protected void dynamicSkin(String skinPath, String skinSuffix)` 该方法是插件式动态换肤，第一个参数就是**皮肤包的具体路径**，第二个就是**皮肤后缀**。

当然还原成默认皮肤可以直接调用`defaultSkin`方法就能实现默认皮换替换。

**换肤：**`dynamicSkin`

**还原：**`defaultSkin `

##### 补充说明：

View只要是通过`LayoutInflater.from(context).inflate()`来创建的，其中context是继承自`ChangeSkinActivity`的activity，当然这些view的文字背景或者图片资源是通过引用资源的方式设置的，那么就可以实现换肤。

这样的情况包括Fragment，或者通过inflate引用布局文件创建View。

其实RecyclerView这类的也可以，但是滑动一下就会发现不对劲，这是因为它存在item复用，所以需要优化处理，后文中会介绍如何处理。

##### 状态栏、导航栏、ActionBar颜色设置

这部分就比较简单，状态栏的颜色设置主要是通过[laobie/StatusBarUtil](https://github.com/laobie/StatusBarUtil)设置的，主要是颜色获取通过`ChangeSkinHelper.getColor`，导航栏只支持5.0以后，actionBar主要就是获取到actionBar设置颜色即可，不使用actionBar不管也行，具体代码：

```
private void setBarsColor() {
    StatusBarUtil.setColorNoTranslucent(this, ChangeSkinHelper.getColor(R.color.colorPrimary));
    ChangeSkinHelper.setNavigation(this, R.color.colorPrimary);
    ChangeSkinHelper.setActionBar(this, R.color.colorPrimary);
}
```

这个方法需要在activity的onCreate方法中调用和在换肤的回调changeSkin方法中调用即可，使用方法见app下的MainActivity。

##### 插件皮肤包

插件皮肤包其实就是一个Android apk，只是里边可以只包含资源文件即可。

皮肤包只需要创建好项目，加入对应的资源，然后build成apk，存放到手机目录中即可。

*可以注意的点就是皮肤包最好不加任何第三方依赖包括google的support包这样会让皮肤包小很多。*

#### [手动创建View换肤](https://github.com/arvinljw/ChangeSkinHelper/blob/master/doc/手动创建View换肤.md)

#### [RecyclerView换肤](https://github.com/arvinljw/ChangeSkinHelper/blob/master/doc/RecyclerView换肤.md)

#### [自定义View换肤](https://github.com/arvinljw/ChangeSkinHelper/blob/master/doc/自定义View换肤.md)

### 感谢

 换肤功能最开始是通过《网易云课程-安卓高级开发工程师微专业》学习，后来又看到了[hongyangAndroid/ChangeSkin](https://github.com/hongyangAndroid/ChangeSkin)库，吸取了一些技巧，对我帮助都很大，在此表示特别感谢～
 
如果有任何对本库需要改进和优化的建议都可以通过issues提交给我，我会定期维护优化，感谢支持。