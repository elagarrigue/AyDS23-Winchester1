package ayds.winchester.songinfo.moredetails.fulllogic;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikipediaAPI {

  @GET("api.php?action=query&list=search&utf8=&format=json&srlimit=1")
   Call<String> getArtistInfo(@Query("srsearch") String artist);

}
