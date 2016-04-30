package paulsenoi.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayAdapter<String> mForecastAdapter;

    @Override
    /*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ArrayList<String> list = new ArrayList<String>();
        list.add("Coucou c'est moi");
        list.add("B");
        list.add("C");
        list.add("D");

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new ArrayAdapter<String>(
                        this, // The current context (this activity)
                        R.layout.list_item_movies, // The name of the layout ID.
                        R.id.list_item_movies_textview, // The ID of the textview to populate.
                        list);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) this.findViewById(R.id.listview_movies);
        listView.setAdapter(mForecastAdapter);

       // setHasOptionsMenu(true);
    }
*/
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        FetchMoviesTask f = new FetchMoviesTask() ;
        f.execute() ;

        // "http://api.themoviedb.org/3/movie/popular?api_key=a84497c51a82ea19b9e709139688765f" :



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


    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         * <p/>
         * private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
         * throws JSONException {
         * <p/>
         * // These are the names of the JSON objects that need to be extracted.
         * final String OWM_LIST = "list";
         * final String OWM_WEATHER = "weather";
         * final String OWM_TEMPERATURE = "temp";
         * final String OWM_MAX = "max";
         * final String OWM_MIN = "min";
         * final String OWM_DESCRIPTION = "main";
         * <p/>
         * JSONObject forecastJson = new JSONObject(forecastJsonStr);
         * JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
         * <p/>
         * // OWM returns daily forecasts based upon the local time of the city that is being
         * // asked for, which means that we need to know the GMT offset to translate this data
         * // properly.
         * <p/>
         * // Since this data is also sent in-order and the first day is always the
         * // current day, we're going to take advantage of that to get a nice
         * // normalized UTC date for all of our weather.
         * <p/>
         * Time dayTime = new Time();
         * dayTime.setToNow();
         * <p/>
         * // we start at the day returned by local time. Otherwise this is a mess.
         * int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
         * <p/>
         * // now we work exclusively in UTC
         * dayTime = new Time();
         * <p/>
         * String[] resultStrs = new String[numDays];
         * <p/>
         * // Data is fetched in Celsius by default.
         * // If user prefers to see in Fahrenheit, convert the values here.
         * // We do this rather than fetching in Fahrenheit so that the user can
         * // change this option without us having to re-fetch the data once
         * // we start storing the values in a database.
         * SharedPreferences sharedPrefs =
         * PreferenceManager.getDefaultSharedPreferences(getActivity());
         * String unitType = sharedPrefs.getString(
         * getString(R.string.pref_units_key),
         * getString(R.string.pref_units_metric));
         * <p/>
         * for(int i = 0; i < weatherArray.length(); i++) {
         * // For now, using the format "Day, description, hi/low"
         * String day;
         * String description;
         * String highAndLow;
         * <p/>
         * // Get the JSON object representing the day
         * JSONObject dayForecast = weatherArray.getJSONObject(i);
         * <p/>
         * // The date/time is returned as a long.  We need to convert that
         * // into something human-readable, since most people won't read "1400356800" as
         * // "this saturday".
         * long dateTime;
         * // Cheating to convert this to UTC time, which is what we want anyhow
         * dateTime = dayTime.setJulianDay(julianStartDay+i);
         * day = getReadableDateString(dateTime);
         * <p/>
         * // description is in a child array called "weather", which is 1 element long.
         * JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
         * description = weatherObject.getString(OWM_DESCRIPTION);
         * <p/>
         * // Temperatures are in a child object called "temp".  Try not to name variables
         * // "temp" when working with temperature.  It confuses everybody.
         * JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
         * double high = temperatureObject.getDouble(OWM_MAX);
         * double low = temperatureObject.getDouble(OWM_MIN);
         * <p/>
         * highAndLow = formatHighLows(high, low, unitType);
         * resultStrs[i] = day + " - " + description + " - " + highAndLow;
         * }
         * return resultStrs;
         * <p/>
         * }
         */
        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;


            try {

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String MODE_PARAM = "popular";
                final String api_key = "api_key";


                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(MODE_PARAM)
                        .appendQueryParameter(api_key, "a84497c51a82ea19b9e709139688765f")
                        .build();


                Log.i("MON TAG", builtUri.toString());

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

            Log.i(LOG_TAG, forecastJsonStr);
            return new String[] {forecastJsonStr};
        }

    }

}
