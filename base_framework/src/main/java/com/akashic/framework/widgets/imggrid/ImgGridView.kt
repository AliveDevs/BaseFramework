package com.akashic.framework.widgets.imggrid

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akashic.framework.R
import com.akashic.framework.ext.dp
import com.akashic.framework.ext.toast
import com.akashic.framework.widgets.SimpleItemDecoration
import com.drakeet.multitype.MultiTypeAdapter

class ImgGridView : RecyclerView {


    private val imgAdapter = MultiTypeAdapter()

    private val imgListData = mutableListOf<ImgGridItemData>()

    private var maxSelectNum = 9 // 最大选择数

    private var enableAdd = true

    private var onAddClickCallback: ((count: Int) -> Unit)? = null
    private var clickCallback: ((list: List<String>, position: Int) -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int = 0) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        init(attributes)
    }

    private val addItemData = ImgGridItemData(0, isAddFlag = true)

    private fun init(attributes: AttributeSet?) {

        isVerticalFadingEdgeEnabled = false
        overScrollMode = OVER_SCROLL_NEVER

        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.ImgGridView)
        val rowNum = typedArray.getInteger(R.styleable.ImgGridView_igv_row_num, 3)
        maxSelectNum = typedArray.getInteger(R.styleable.ImgGridView_igv_max_select, maxSelectNum)
        val addIconRes = typedArray.getResourceId(R.styleable.ImgGridView_igv_add_icon, 0)
        val removeIconRes = typedArray.getResourceId(R.styleable.ImgGridView_igv_remove_icon, 0)
        val videoIconRes = typedArray.getResourceId(R.styleable.ImgGridView_igv_video_icon, 0)
        val radius = typedArray.getDimensionPixelSize(R.styleable.ImgGridView_igv_item_radius,0)
        addItemData.data = addIconRes
        typedArray.recycle()

        imgListData.add(addItemData)

        layoutManager = GridLayoutManager(context, rowNum)
        addItemDecoration(SimpleItemDecoration(3.dp, 3.dp))
        adapter = imgAdapter.apply {
            register(ImgGridVH(removeIconRes,videoIconRes,radius,onAddClickCallback = {
                if (!enableAdd) return@ImgGridVH
                if (imgListData.size > maxSelectNum) {
                    toast("已达到最大数量")
                    return@ImgGridVH
                }
                onAddClickCallback?.invoke(maxSelectNum - imgListData.size + 1)
            }, clickCallback = {
                val list = mutableListOf<String>()
                imgListData.forEach { imgGridItemData ->
                    if (imgGridItemData.data is String) {
                        list.add( imgGridItemData.data as String)
                    }
                }
                clickCallback?.invoke(list,it)
            }) {
                imgListData.removeAt(it)
                imgAdapter.notifyItemRemoved(it)
                imgAdapter.notifyItemRangeChanged(0, imgListData.size)
                if (imgListData.size == maxSelectNum - 1 && !imgListData.last().isAddFlag) {
                    imgListData.add(imgListData.size, addItemData)
                    imgAdapter.notifyItemInserted(imgListData.lastIndex)
                }
            })
            items = imgListData
        }
    }

    fun setAddIconRes(resId: Int) {
        addItemData.data = resId
        imgListData.forEachIndexed { index, imgGridItemData ->
            if (imgGridItemData.isAddFlag) {
                imgAdapter.notifyItemChanged(index)
            }
        }
    }

    fun show(imgList: List<String>) {
        imgListData.addAll(0, imgList.map { ImgGridItemData(it) })
        imgAdapter.notifyItemRangeInserted(0, imgList.size)
    }

    fun clear() {
        val size = imgListData.size
        for (index in 0 until imgListData.lastIndex) {
            imgListData.removeAt(index)
        }
        imgAdapter.notifyItemRangeRemoved(0, size - 1)
    }

    fun getImgList(): List<String> {
        return imgListData.filter { !it.isAddFlag }.map { it.data as String }
    }

    fun setMaxNum(count: Int) {
        this.maxSelectNum = count
    }

    fun add(data: ImgGridItemData) {
        addAll(listOf(data))
    }

    fun addAll(list: List<ImgGridItemData>) {
        val lastIndex = imgListData.lastIndex
        imgListData.addAll(lastIndex, list)
        imgAdapter.notifyItemInserted(lastIndex)

        if (imgListData.size > maxSelectNum) {
            imgListData.removeAt(maxSelectNum)//移除添加按钮
            imgAdapter.notifyItemRemoved(maxSelectNum)
        }
    }

    fun disableAddAction() {
        enableAdd = false
        imgListData.clear()
        imgAdapter.notifyItemRangeChanged(0, imgListData.size)
    }
}