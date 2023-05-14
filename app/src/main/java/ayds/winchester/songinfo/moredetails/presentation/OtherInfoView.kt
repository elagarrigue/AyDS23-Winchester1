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
import ayds.winchester.songinfo.moredetails.injector.MoreDetailsInjector
import com.squareup.picasso.Picasso

interface OtherInfoView {

    fun setPresenter(presenter: MoreDetailsPresenter)

}

class OtherInfoViewActivity(): AppCompatActivity(), OtherInfoView{
    private lateinit var presenter: MoreDetailsPresenter
    private lateinit var urlButton : Button
    private lateinit var artistInfoTextView : TextView
    private lateinit var imageView : ImageView

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
        initInjector()
        initObservers()
        val artistName = getArtistNameFromIntent()
        presenter.generateArtistInfo(artistName)
    }

    private fun initProperties(){
        urlButton = findViewById(R.id.openUrlButton)
        artistInfoTextView = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
    }

    private fun initInjector() {
        MoreDetailsInjector.init(this)
    }

    private fun getArtistNameFromIntent() = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()

    override fun setPresenter(presenter: MoreDetailsPresenter) {
        this.presenter = presenter
    }

    private fun displayArtistInfo(artist: OtherInfoUiState) {
        loadImage(artist.wikipediaLogo)
        setText(artist.artistInfo)
        setListener(artist.wikipediaArticleUrl)
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

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}