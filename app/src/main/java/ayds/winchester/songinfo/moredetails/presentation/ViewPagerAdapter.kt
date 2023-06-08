package ayds.winchester.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.moredetails.domain.entities.Source
import com.squareup.picasso.Picasso

interface ViewPagerAdapter{
}

class ViewPagerAdapterImpl(private val activity: AppCompatActivity) : ViewPagerAdapter, RecyclerView.Adapter<ViewPagerAdapterImpl.Pager2ViewHolder>() {

    private val urlButtonList: MutableList<String> = mutableListOf()
    private val artistInfoList: MutableList<String> = mutableListOf()
    private val sourceLogoList: MutableList<String> = mutableListOf()
    private val sourceNameList: MutableList<String> = mutableListOf()

    inner class Pager2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val urlButton : Button = itemView.findViewById(R.id.openUrlButton)
        val artistInfoTextView : TextView = itemView.findViewById(R.id.artistInfoPanel)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val sourceLabel: TextView = itemView.findViewById(R.id.sourceLabelTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_other_info, parent, false))
    }

    override fun getItemCount(): Int {
        return artistInfoList.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        setListener(holder.urlButton, urlButtonList[position])
        holder.artistInfoTextView.text = Html.fromHtml(artistInfoList[position])
        loadImage(holder.imageView, sourceLogoList[position])
        holder.sourceLabel.text = sourceNameList[position]
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        Picasso.get().load(imageUrl).into(imageView)
    }

    private fun setListener(urlButton: Button, urlString: String){
        urlButton.setOnClickListener {
            openUrlInExternalApp(urlString)
        }
    }

    private fun openUrlInExternalApp(urlString: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlString)
        activity.startActivity(intent)
    }

    fun addUrlButton(url: String) {
        urlButtonList.add(url)
    }
    fun addArtistInfo(artistInfo: String){
        artistInfoList.add(artistInfo)
    }
    fun addSourceLogo(sourceLogo: String){
        sourceLogoList.add(sourceLogo)
    }

    fun addSourceName(sourceName: String){
        var source = ""
        when (sourceName){
            Source.Wikipedia.name -> source = "Wikipedia"
            Source.LastFM.name -> source = "Last FM"
            Source.NYT.name -> source = "New York Times"
        }
        sourceNameList.add(source)
    }

}