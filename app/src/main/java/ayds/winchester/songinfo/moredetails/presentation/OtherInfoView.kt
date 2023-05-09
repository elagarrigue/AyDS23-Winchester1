package ayds.winchester.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.observer.Observer
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.moredetails.fulllogic.DataBase
import com.squareup.picasso.Picasso

private const val WIKIPEDIA_LOGO = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"
interface OtherInfoView {

    fun onCreate(savedInstanceState: Bundle?)
    fun displayArtistInfo(artist: OtherInfoUiState)
}

class OtherInfoViewActivity(
    val presenter: MoreDetailsPresenter
): AppCompatActivity(), OtherInfoView{
    private lateinit var urlButton : Button
    private lateinit var artistInfoTextView : TextView
    private lateinit var imageView : ImageView
    //private lateinit var dataBase: DataBase // VER DONDE PONER ESTO

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> displayArtistInfo(value)
        }

    private fun initObservers() {
        presenter.uiStateObservable.subscribe(observer)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initProperties()
        initObservers()
        presenter.generateArtistInfo()
    }

    override fun displayArtistInfo(artist: OtherInfoUiState) {
        loadImage(WIKIPEDIA_LOGO)
        setText(artist.artistInfo)
        setListener(artist.wikipediaArticleUrl)
    }

    private fun initProperties(){
        urlButton = findViewById(R.id.openUrlButton)
        artistInfoTextView = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        //dataBase = DataBase(this) //VER DONDE PONER ESTO
    }

    private fun loadImage(imageUrl: String) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(imageView)
        }
    }

    private fun setText(finalText: String?) {
        runOnUiThread {
            artistInfoTextView.text = Html.fromHtml(finalText)
        }
    }

    private fun setListener(urlString: String){
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