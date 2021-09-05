package com.kt.myproject.ui.fragment.repeat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kt.myproject.base.BaseViewModel
import com.kt.myproject.repository.model.RepeatData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: no name
 * @Created: KOP 2021/06/11
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RepeatVM : BaseViewModel() {

    init {
        repeatLog()
    }

    val listLiveData = MutableLiveData<List<RepeatData>>()

    private val list = arrayListOf<RepeatData>()

    private fun repeatLog() {
        viewModelScope.launch(Dispatchers.IO) {
            val random = Random().nextInt(listRandomRepeat.size)
            list.add(listRandomRepeat[random])
            listLiveData.postValue(list)
            delay(2000)
            repeatLog()
        }
    }

    private val listRandomRepeat = listOf(
        RepeatData(
            "Songoku",
            "https://i.pinimg.com/originals/d9/97/24/d99724907cd57cfcaf6a1833a8bd5b12.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Luffy",
            "https://yt3.ggpht.com/ytc/AAUvwnjyHPfxxlEjIsje7BF_6-Ns844XBXm7tTou9bdQ=s900-c-k-c0x00ffffff-no-rj",
            "11/06/2021"
        ),
        RepeatData(
            "Zoro",
            "https://gamek.mediacdn.vn/133514250583805952/2020/9/11/acz1-1599818885430180233680.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Ace",
            "https://cuongtruyen.com/wp-content/uploads/2019/01/portgas-d-ace-one-piece-1024x576.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Sabo",
            "https://cuongtruyen.com/wp-content/uploads/2019/09/hoa-long-sabo-quan-cach-mang-anh-luffy.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Doflamingo",
            "https://hoso.wiki/wp-content/uploads/2020/08/one-piece-donquixote-doflamingo.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Roger",
            "https://genk.mediacdn.vn/2018/7/2/maxresdefault-15305054713361589093044.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Garp",
            "https://static.wikia.nocookie.net/onepiece/images/e/e6/Monkey_D._Garp_Portrait.png/revision/latest?cb=20200112090224",
            "11/06/2021"
        ),
        RepeatData(
            "Naruto",
            "https://static.wikia.nocookie.net/naruto-viet-nam/images/4/49/Naruto_Shipp%C5%ABden_Logo-1.png/revision/latest?cb=20170427074448&path-prefix=vi",
            "11/06/2021"
        ),
        RepeatData(
            "Kakashi",
            "https://static.wikia.nocookie.net/naruto-viet-nam/images/1/12/1-2.png/revision/latest/top-crop/width/360/height/450?cb=20170501032359&path-prefix=vi",
            "11/06/2021"
        ),
        RepeatData(
            "Itachi",
            "https://genk.mediacdn.vn/2017/uchihaitachifull1628688-1510047496427.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Shikamaru",
            "https://upload.wikimedia.org/wikipedia/vi/8/87/Nara_Shikamaru.jpg",
            "11/06/2021"
        ),
        RepeatData(
            "Jinbei",
            "https://static.wikia.nocookie.net/onepiece/images/8/81/Jinbe_Anime_Infobox.png/revision/latest?cb=20170418090836&path-prefix=vi",
            "11/06/2021"
        ),
        RepeatData(
            "Kid",
            "https://static.wikia.nocookie.net/onepiece/images/4/47/Eustass_Kid_Anime_Post_Timeskip_Infobox.png/revision/latest?cb=20170420052010&path-prefix=vi",
            "11/06/2021"
        )
    )

}