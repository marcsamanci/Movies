package paulsenoi.movies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import paulsenoi.movies.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<String> mThumbUris;


    public ImageAdapter(Context c) {

        mContext = c;


        mThumbUris = new ArrayList<String>() ;
        mThumbUris.add("/5N20rQURev5CNDcMjHVUZhpoCNC.jpg") ;
    }

    public int getCount() {
        return mThumbUris.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true) ;
            imageView.setLayoutParams(new GridView.LayoutParams(205, 205));
            // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // imageView.setImageResource(mThumbIds[position]);

        Picasso.with(mContext).
                load("http://image.tmdb.org/t/p/w185/" + mThumbUris.get(position)).into(imageView);

        return imageView;
    }

    // references to our images


    public ArrayList<String> getUriList(){
        return mThumbUris;
    }





}