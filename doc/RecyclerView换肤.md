## RecyclerView换肤

在readme中提到过RecyclerView换肤，因为RecyclerView的item的创建多半也是使用inflate创建的，所以其实它的item都是包含了可换肤的tag的，但是因为item复用原理，导致在执行换肤的时候只能给当前屏幕的itemView换肤，滑动之后上下的几个item因为从RecyclerView的复用池里获取的就没有换肤，还是之前的，就有点问题。

知道了问题，就可以解决了，方法也比较简单：

* 1、在`onCreateViewHolder`，item创建之后获取到可换肤view之后使用，使用`ChangeSkinHelper.setSkin`；
* 2、在`onBindViewHolder`拿到需要换肤的view，也设置一下`ChangeSkinHelper.setSkin`；
* 3、最后在使用`ChangeSkinHelper.addListener`监听一下换肤回调，使用RecyclerView的adapter的`adapter.notifyDataSetChanged()`方法就解决了RecyclerView的换肤。

其实ListView等包含复用item的view，都是可以使用这种方式解决换肤的。具体的使用可以参考demo中的RecyclerView的使用（在app下MainFragment中）。