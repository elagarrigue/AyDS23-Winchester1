package ayds.winchester.songinfo.moredetails.fulllogic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ayds.winchester.songinfo.R;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;


import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;



public class OtherInfoWindow extends AppCompatActivity {

  public final static String ARTIST_NAME_EXTRA = "artistName";

  private TextView textPane2;
  //private JPanel imagePanel;
 // private JLabel posterImageLabel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_other_info);

    textPane2 = findViewById(R.id.textPane2);


    open(getIntent().getStringExtra("artistName"));
  }

  public void getARtistInfo(String artistName) {

    // create
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/w/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    WikipediaAPI wikipediaAPI = retrofit.create(WikipediaAPI.class);

    Log.e("TAG","artistName " + artistName);

        new Thread(new Runnable() {
          @Override
          public void run() {

            String text = DataBase.getInfo(dataBase, artistName);


            if (text != null) { // exists in db

              text = "[*]" + text;
            } else { // get from service
              Response<String> callResponse;
              try {
                callResponse = wikipediaAPI.getArtistInfo(artistName).execute();

                System.out.println("JSON " + callResponse.body());

                Gson gson = new Gson();
                JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);
                JsonObject query = jobj.get("query").getAsJsonObject();
                JsonElement snippet = query.get("search").getAsJsonArray().get(0).getAsJsonObject().get("snippet");
                JsonElement pageid = query.get("search").getAsJsonArray().get(0).getAsJsonObject().get("pageid");



                if (snippet == null) {
                  text = "No Results";
                } else {
                  text = snippet.getAsString().replace("\\n", "\n");

                  text = textToHtml(text, artistName);


                  // save to DB  <o/

                  DataBase.saveArtist(dataBase, artistName, text);
                }


                final String urlString = "https://en.wikipedia.org/?curid=" + pageid;
                findViewById(R.id.openUrlButton).setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlString));
                    startActivity(intent);
                  }
                });

              } catch (IOException e1) {
                Log.e("TAG", "Error " + e1);
                e1.printStackTrace();
              }
            }


            String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png";

            Log.e("TAG","Get Image from " + imageUrl);



            final String finalText = text;

            runOnUiThread( () -> {
              Picasso.get().load(imageUrl).into((ImageView) findViewById(R.id.imageView));


              textPane2.setText(Html.fromHtml( finalText));


            });



          }
        }).start();

  }

  private DataBase dataBase = null;

  private void open(String artist) {


    dataBase = new DataBase(this);

    DataBase.saveArtist(dataBase, "test", "sarasa");


    Log.e("TAG", ""+ DataBase.getInfo(dataBase,"test"));
    Log.e("TAG",""+ DataBase.getInfo(dataBase,"nada"));

    getARtistInfo(artist);
  }

  public static String textToHtml(String text, String term) {

    StringBuilder builder = new StringBuilder();

    builder.append("<html><div width=400>");
    builder.append("<font face=\"arial\">");

    String textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replaceAll("(?i)" + term, "<b>" + term.toUpperCase() + "</b>");

    builder.append(textWithBold);

    builder.append("</font></div></html>");

    return builder.toString();
  }

}
