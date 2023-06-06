package ayds.winchester.songinfo.moredetails.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import ayds.observer.Observer
import ayds.winchester.songinfo.R
import ayds.winchester.songinfo.moredetails.injector.MoreDetailsInjector
import me.relex.circleindicator.CircleIndicator3

interface OtherInfoView {
    fun setPresenter(presenter: MoreDetailsPresenter)
}

class OtherInfoViewActivity(): AppCompatActivity(), OtherInfoView{
    private lateinit var presenter: MoreDetailsPresenter
    private val viewPagerAdapter = ViewPagerAdapterImpl(this)

    private val observer: Observer<List<OtherInfoUiState>> =
        Observer{ value -> for(element in value){
            displayArtistInfo(element)
        }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_pager)

        initInjector()
        initObservers()

        val viewPager = findViewById<ViewPager2>(R.id.view_pager2)
        viewPager.adapter = viewPagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewPager)

        generateCards()
    }

    private fun initInjector() {
        MoreDetailsInjector.init(this)
    }

    private fun initObservers() {
        presenter.uiStateObservable.subscribe(observer)
    }

    private fun generateCards() {
        val artistName = getArtistNameFromIntent()
        presenter.generateCards(artistName)
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
        this.runOnUiThread {
            viewPagerAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}