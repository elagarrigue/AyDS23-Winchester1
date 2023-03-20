package ayds.winchester.songinfo.utils.view

import android.widget.ImageView
import com.squareup.picasso.Picasso

interface ImageLoader {

    fun loadImageIntoView(url: String, imageView: ImageView)
}

internal class ImageLoaderImpl(
    private val picasso: Picasso
) : ImageLoader {

    override fun loadImageIntoView(url: String, imageView: ImageView) {
        picasso.load(url).into(imageView)
    }

}