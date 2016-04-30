package paulsenoi.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private ImageAdapter imag_adap;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);

        imag_adap = new ImageAdapter(this) ;

        gridview.setAdapter(imag_adap);

       // FetchMoviesTask f = new FetchMoviesTask();
        //f.execute();

        /// salut
        /*
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(HelloGridView.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                FetchMoviesTask f = new FetchMoviesTask() ;
                f.execute() ;
                Log.e("MONTAG", "salut");
                return true ;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<HashMap>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<HashMap> doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
           // if (params.length == 0) {
          //      return null;
          //  }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;


            try {

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String MODE_PARAM = "top_rated";
                final String api_key = "api_key";


                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(MODE_PARAM)
                        .appendQueryParameter(api_key, "a84497c51a82ea19b9e709139688765f")
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try{
                return getMoviesDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
                return null ;
        }


        private ArrayList<HashMap> getMoviesDataFromJson(String json) throws JSONException{

            JSONObject forecastJson = new JSONObject(json);
            JSONArray weatherArray = forecastJson.getJSONArray("results");

            ArrayList<HashMap> resultat = new ArrayList<>();
            for(int i = 0; i < weatherArray.length(); i++) {

                JSONObject obj = weatherArray.getJSONObject(i) ;
                HashMap movie = new HashMap<String, String>() ;
                movie.put("path", obj.getString("poster_path")) ;
                movie.put("id", obj.getString("id")) ;

                resultat.add(movie) ;
            }

            return resultat ;
        }


        @Override
        protected void onPostExecute(ArrayList<HashMap> result) {
            if (result != null) {

                ArrayList<String> mThumbUris = imag_adap.getUriList() ;
                mThumbUris.clear() ;

                for(HashMap dayForecastStr : result) {
                    mThumbUris.add((String) dayForecastStr.get("path")) ;
                }

                imag_adap.notifyDataSetChanged();

                // New data is back from the server.  Hooray!
            }
        }

    }

}
