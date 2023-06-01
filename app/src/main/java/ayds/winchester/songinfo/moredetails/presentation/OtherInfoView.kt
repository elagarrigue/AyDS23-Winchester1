package ayds.winchester.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ayds.observer.Observer
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.moredetails.injector.MoreDetailsInjector
import com.squareup.picasso.Picasso
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3

interface OtherInfoView {
    fun setPresenter(presenter: MoreDetailsPresenter)
}

class OtherInfoViewActivity(): AppCompatActivity(), OtherInfoView{
    private lateinit var presenter: MoreDetailsPresenter
    private lateinit var urlButton : Button
    private lateinit var artistInfoTextView : TextView
    private lateinit var imageView : ImageView
    private lateinit var sourceLabel: TextView
    private val viewPagerAdapter = ViewPagerAdapterImpl()

    private val observer: Observer<List<OtherInfoUiState>> =
        Observer{ value -> for(element in value){
            displayArtistInfo(element)
        }

        }
//        Observer { value -> displayArtistInfo(value)
//        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_pager)
        //setContentView(R.layout.activity_other_info)
        //initProperties()


        val viewPager = findViewById<ViewPager2>(R.id.view_pager2)
        viewPager.adapter = viewPagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewPager)
        initInjector()
        initObservers()
        generateArtistInfo()
    }

    private fun initProperties(){
        urlButton = findViewById(R.id.openUrlButton)
        artistInfoTextView = findViewById(R.id.artistInfoPanel)
        imageView = findViewById(R.id.imageView)
        sourceLabel = findViewById(R.id.sourceLabelTextView)
    }

    private fun initInjector() {
        MoreDetailsInjector.init(this)
    }

    private fun initObservers() {
        presenter.uiStateObservable.subscribe(observer)
    }

    private fun generateArtistInfo() {
        val artistName = getArtistNameFromIntent()
        presenter.generateArtistInfo(artistName)
    }

    private fun getArtistNameFromIntent() = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()

    override fun setPresenter(presenter: MoreDetailsPresenter) {
        this.presenter = presenter
    }

    private fun displayArtistInfo(artist: OtherInfoUiState) {
        viewPagerAdapter.addSourceLogo(artist.sourceLogo)
        viewPagerAdapter.addUrlButton(artist.sourceArticleUrl)
        viewPagerAdapter.addArtistInfo(artist.description)
        viewPagerAdapter.addSourceName(artist.sourceName)
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

    private fun setSourceLabel(source: String){
        runOnUiThread {
            sourceLabel.text = source
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}