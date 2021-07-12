package com.kt.myproject.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.kt.myproject.R
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.databinding.SlideViewBinding
import com.kt.myproject.utils.load
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderViewAdapter

class SlideView(context: Context, attrs: AttributeSet) :
    AppBindCustomView<SlideViewBinding>(context, attrs, SlideViewBinding::inflate) {

    private var adapterSlider: SlideAdapter? = null

    override fun onViewInit(context: Context, types: TypedArray) {
        adapterSlider = SlideAdapter()
        bd.slideView.setSliderAdapter(adapterSlider!!)
        bd.slideView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        bd.slideView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION)
        bd.slideView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        bd.slideView.indicatorSelectedColor = Color.WHITE
        bd.slideView.indicatorUnselectedColor = Color.GRAY
        bd.slideView.scrollTimeInSec = 5
        bd.slideView.isAutoCycle = true
        bd.slideView.startAutoCycle()
    }

    fun initSlider(list: List<Any>) {
        adapterSlider?.setSlide(list)
    }

    fun destroySlide() {
        adapterSlider = null
        adapterSlider?.removeList()
    }

    private inner class SlideAdapter : SliderViewAdapter<SlideAdapter.SlideAdapterVH>() {

        private var listAdv = listOf<Any>()

        fun setSlide(list: List<Any>) {
            listAdv = list
            notifyDataSetChanged()
        }

        fun removeList() {
            listAdv = listOf()
        }

        override fun onCreateViewHolder(parent: ViewGroup): SlideAdapterVH {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide, null)
            return SlideAdapterVH(view)
        }

        override fun onBindViewHolder(viewHolder: SlideAdapterVH?, position: Int) {
            val item = listAdv.get(position)
            when (item) {
                is String -> {
                    viewHolder?.image?.load(item)
                }
                is Int -> {
                    viewHolder?.image?.load(item)
                }
            }
        }

        override fun getCount(): Int = listAdv.size

        inner class SlideAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
            val image: ImageView = itemView.findViewById(R.id.itemSlideImage)
        }

    }

}