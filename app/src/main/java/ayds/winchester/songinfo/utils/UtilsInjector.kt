package ayds.winchester.songinfo.utils

import ayds.winchester.songinfo.utils.navigation.NavigationUtils
import ayds.winchester.songinfo.utils.navigation.NavigationUtilsImpl
import ayds.winchester.songinfo.utils.view.ImageLoader
import ayds.winchester.songinfo.utils.view.ImageLoaderImpl
import com.squareup.picasso.Picasso

object UtilsInjector {

    val imageLoader: ImageLoader = ImageLoaderImpl(Picasso.get())

    val navigationUtils: NavigationUtils = NavigationUtilsImpl()
}