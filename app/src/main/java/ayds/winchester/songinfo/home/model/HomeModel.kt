package ayds.winchester.songinfo.home.model

import ayds.winchester.songinfo.home.model.entities.Song
import ayds.winchester.songinfo.home.model.repository.SongRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface  HomeModel {

    val songObservable: Observable<Song>

    fun searchSong(term: String)

    fun getSongById(id: String): Song
}

internal class HomeModelImpl(private val repository: SongRepository) : HomeModel {

    override val songObservable = Subject<Song>()

    override fun searchSong(term: String) {
        repository.getSongByTerm(term).let {
            songObservable.notify(it)
        }
    }

    override fun getSongById(id: String): Song = repository.getSongById(id)
}