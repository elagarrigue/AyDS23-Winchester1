package ayds.winchester.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ayds.winchester.songinfo.R
import com.squareup.picasso.Picasso

interface ViewPagerAdapter{
}

class ViewPagerAdapterImp(
    private var urlButton: List<String>,
    private var artistInfo: List<String>,
    private var sourceLogo: List<String>,
    private var sourceName: List<String>
): ViewPagerAdapter, RecyclerView.Adapter<ViewPagerAdapterImp.Pager2ViewHolder>() {

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
        return artistInfo.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        setListener(holder.urlButton, urlButton[position])
        holder.artistInfoTextView.text = artistInfo[position]
        loadImage(holder.imageView, sourceLogo[position])
        holder.sourceLabel.text = sourceName[position]
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
        startActivity(intent)
    }

}