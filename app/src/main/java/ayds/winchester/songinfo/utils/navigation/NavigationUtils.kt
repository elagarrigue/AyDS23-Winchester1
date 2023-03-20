package ayds.winchester.songinfo.utils.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

interface NavigationUtils {
    fun openExternalUrl(activity: Activity, url: String)
}

internal class NavigationUtilsImpl: NavigationUtils {
    override fun openExternalUrl(activity: Activity, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        activity.startActivity(intent)
    }
}