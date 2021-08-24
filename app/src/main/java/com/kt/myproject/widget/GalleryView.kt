package com.kt.myproject.widget

import android.content.Context
import android.content.res.TypedArray
import android.provider.MediaStore
import android.util.AttributeSet
import androidx.viewbinding.ViewBinding
import com.kt.myproject.base.AppBindCustomView
import com.kt.myproject.base.BaseBindRecyclerView
import com.kt.myproject.base.ItemInflating
import com.kt.myproject.databinding.GalleryItemImageBinding
import com.kt.myproject.databinding.GalleryViewBinding
import com.kt.myproject.ex.load
import com.kt.myproject.repository.model.Media
import com.kt.myproject.ui.fragment.gallery.keyboard.InputAwareLayout
import com.kt.myproject.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryView(context: Context, attrs: AttributeSet?) :
    AppBindCustomView<GalleryViewBinding>(context, attrs, GalleryViewBinding::inflate),
    InputAwareLayout.InputView {

    private val adapter = AdapterGallery()

    override fun onViewInit(context: Context, types: TypedArray) {

    }

    fun bindAdapterView() {
        if (!adapter.currentList.isNullOrEmpty()) return
        CoroutineScope(Dispatchers.IO).launch {
            val list = Utils().getMediaInBucket(
                context, Media.ALL_MEDIA_BUCKET_ID,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true
            )
            launch(Dispatchers.Main) {
                adapter.set(list)
                adapter.bind(bd.galleryViewRecycler, 3)
            }
        }
    }

    override fun show(height: Int, immediate: Boolean) {
        val params = layoutParams
        params.height = height
        layoutParams = params
        visibility = VISIBLE
    }

    override fun hide(immediate: Boolean) {
        visibility = GONE
    }

    override fun isShowing(): Boolean {
        return visibility == VISIBLE
    }

    inner class AdapterGallery : BaseBindRecyclerView<Media>() {

        override fun itemInflating(item: Media, position: Int): ItemInflating {
            return GalleryItemImageBinding::inflate
        }

        override fun ViewBinding.onBindItem(item: Media, position: Int) {
            if (this is GalleryItemImageBinding) {
                com.kt.myproject.ex.post { galleryItemImage.load(item.uri!!) }
            }
        }

    }

}