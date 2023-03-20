package ayds.winchester.songinfo.home.controller

import ayds.winchester.songinfo.home.model.HomeModelInjector
import ayds.winchester.songinfo.home.view.HomeView

object HomeControllerInjector {

    fun onViewStarted(homeView: HomeView) {
        HomeControllerImpl(HomeModelInjector.getHomeModel()).apply {
            setHomeView(homeView)
        }
    }
}