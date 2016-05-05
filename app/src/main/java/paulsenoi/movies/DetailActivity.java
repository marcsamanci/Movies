package paulsenoi.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView im = (ImageView) findViewById(R.id.imageView_movie);
        TextView textView_description = (TextView) findViewById(R.id.textView_movie);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getIntent();
        if (intent != null) {
           HashMap movie = (HashMap) intent.getSerializableExtra("movie");

            Picasso.with(getApplicationContext()).
                    load("http://image.tmdb.org/t/p/w780/" +movie.get("path")).into(im);

            textView_description.setText((String) movie.get("overview"));


        }
    }
}
