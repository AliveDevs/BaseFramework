package com.akashic.framework.widgets.load

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akashic.framework.R
import com.akashic.framework.ext.bind
import com.akashic.framework.ext.gone
import com.akashic.framework.ext.visible
import com.akashic.framework.widgets.MyRecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
/**
 *  author: akashic
 *
 *  date: 2021-03-14 1:48 PM
 *
 *  desc:
 */
class LoadListLayout : FrameLayout {

    companion object {

        var loadingImgRes: Int? = null

        var errorImgRes: Int? = null

        var emptyImgRes: Int? = null
    }

    var refreshLoadLayout: SmartRefreshLayout  //刷新视图

    val recyclerView: MyRecyclerView //列表视图

    val loadAdapter = MultiTypeAdapter() //列表适配器

    var items = mutableListOf<Any>() //列表数据

    private lateinit var tipView: TextView

    private lateinit var tipImg: ImageView


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        val rootView = inflate(context, R.layout.load_list_layout, null)

        addView(rootView)

        refreshLoadLayout = rootView.findViewById(R.id.load_refreshLayout)

        refreshLoadLayout.setEnableAutoLoadMore(true)

        recyclerView = rootView.findViewById(R.id.load_list)

        try {
            tipView = rootView.findViewById(R.id.load_layout_tip_tv)

            tipImg = rootView.findViewById(R.id.load_layout_tip_iv)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        recyclerView.adapter = loadAdapter

        loadAdapter.items = items

        showLoading()
    }


    /**
     * 刷新加载相关
     */

    fun setRefreshLoadMoreListener(listener: OnRefreshLoadMoreListener) {
        refreshLoadLayout.setOnRefreshLoadMoreListener(listener)
    }

    fun setRefreshListener(listener: OnRefreshListener){
        refreshLoadLayout.setOnRefreshListener(listener)
    }

    fun setLoadMoreListener(listener: OnLoadMoreListener){
        refreshLoadLayout.setOnLoadMoreListener(listener)
    }

    /**
     * 开始刷新 显示刷新动画
     */
    fun autoRefresh() {
        refreshLoadLayout.autoRefresh()
        showTip()
    }

    /**
     * 只显示刷新动画
     */
    fun autoRefreshAnimOnly() {
        refreshLoadLayout.autoRefreshAnimationOnly()
    }


    /**
     * 结束
     */
    fun finishLoad(noMoreData: Boolean) {
        refreshLoadLayout.finishRefresh()
        if (noMoreData) {
            refreshLoadLayout.finishLoadMoreWithNoMoreData()
            return
        }
        refreshLoadLayout.finishLoadMore()
    }


    /**
     * 获取数据成功
     */
    fun loadDataSuccess(
        dataList: MutableList<Any>,
        noMoreData: Boolean,
        clearOldData: Boolean = false,
        index: Int = -1
    ) {
        finishLoad(noMoreData)

        if(dataList.isEmpty()){
            showEmpty()
            return
        }
        showContent()
        if(clearOldData && items.isNotEmpty()){
            items.clear()
            items.addAll(dataList)
            loadAdapter.notifyItemRangeChanged(0,dataList.size)
        }else{
            items.addAll(if (index > -1) index else items.size, dataList)
            if(index> -1){
                loadAdapter.notifyItemRangeInserted(index,dataList.size)
            }else{
                notifyItemRangeInserted(dataList.size)
            }
        }
    }


    /**
     * 允许/禁止 下拉刷新
     */
    fun setEnableRefresh(enable: Boolean) {
        refreshLoadLayout.setEnableRefresh(enable)
    }


    /**
     * 允许/禁止 加载更多
     */
    fun setEnableLoadMore(enable: Boolean) {
        refreshLoadLayout.setEnableLoadMore(enable)
    }

    /**
     * 开启/关闭 纯净回弹
     */
    fun setEnablePureScrollMode(enable: Boolean) {
        refreshLoadLayout.setEnablePureScrollMode(enable)
    }


    fun setEnableLoadMoreWhenContentNotFull(enabled: Boolean) {
        refreshLoadLayout.setEnableLoadMoreWhenContentNotFull(enabled)
    }

    fun setEnableAutoLoadMore(enabled: Boolean) {
        refreshLoadLayout.setEnableAutoLoadMore(enabled)
    }


    /**
     * 嵌套滑动
     */
    fun setListViewNestedScrollingEnabled(enable: Boolean) {
        recyclerView.isNestedScrollingEnabled = enable
    }

    /**
     * 列表相关
     */

    /**
     * 注册 列表项类型
     */
    inline fun <reified T : Any> register(delegate: ItemViewDelegate<T, *>) {
        loadAdapter.register(delegate)
    }

    fun setLayoutManager(layout: RecyclerView.LayoutManager?) {
        recyclerView.layoutManager = layout
    }

//    fun getLayoutManager(): RecyclerView.LayoutManager? = recyclerView.layoutManager


    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
        recyclerView.adapter = adapter
    }

//    fun getAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>? = recyclerView.adapter


    /**
     * 添加间隔
     */
    fun addItemDecoration(decoration: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(decoration)
    }


    /**
     * 有新增数据 刷新
     */
    fun notifyItemRangeInserted(addSize: Int) {
        loadAdapter.notifyItemRangeInserted(items.size - addSize, addSize)
    }


    /**
     * 删除数据 刷新
     */
    fun notifyItemRemoved(position: Int) {
        items.removeAt(position)
        loadAdapter.notifyItemRemoved(position)
        if(items.isEmpty()){
            showEmpty()
        }
    }


    /**
     * 删除数据 刷新
     */
    fun notifyItemRangeRemoved(startPosition: Int,count:Int) {
        loadAdapter.notifyItemRangeRemoved(startPosition,count)
        if(items.isEmpty()){
            showEmpty()
        }
    }

    /**
     * 更新数据 刷新
     */
    fun notifyItemChanged(position: Int) {
        loadAdapter.notifyItemChanged(position)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataChanged() {
        loadAdapter.notifyDataSetChanged()
    }

    /**
     * 显示列表内容视图
     */
    fun showContent() {
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        tipView.gone()
        tipImg.gone()
        recyclerView.visible()
    }


    /**
     * 显示加载视图
     */
    fun showLoading(tip: String = "正在加载") {
        showTip(tip, loadingImgRes)
    }

    /**
     * 显示空白视图
     */
    fun showEmpty(tip: String = "暂无数据") {
        showTip(tip, emptyImgRes)
    }

    fun showError(tip: String = "貌似出了点问题") {
        showTip(tip, errorImgRes)
    }

    /**
     * 显示提示视图
     */
    private fun showTip(tip: String = "正在加载", imgRes: Int? = loadingImgRes) {
        refreshLoadLayout.finishRefresh()
        refreshLoadLayout.finishLoadMore()
        tipView.visible()
        tipView.text = tip
        tipImg.visible()
        tipImg.bind(imgRes)
        recyclerView.gone()
    }


}