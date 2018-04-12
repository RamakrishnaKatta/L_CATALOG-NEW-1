package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.immersionslabs.lcatalog.ProjectPartDetailsActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ProjectPartImageSliderAdapter extends PagerAdapter {

    private ArrayList<String> Images;
    private Activity activity;

    String project_id;
    String TAG = "ProjectPartImageSliderAdapter";

    public ProjectPartImageSliderAdapter(ProjectPartDetailsActivity activity,
                                         ArrayList<String> slider_images,
                                         String project_id) {
        this.activity = activity;
        this.Images = slider_images;
        this.project_id = project_id;
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_project_part_details, container, false);
        Log.e(TAG, "projectidpartimage  " + project_id);
        ImageView images = v.findViewById(R.id.project_part_image_view);
        Bitmap b = download_images(Images.get(position));
        images.setImageBitmap(b);
        container.addView(v);
        return v;
    }

    private Bitmap download_images(String urls) {
        String urldisplay = EnvConstants.APP_BASE_URL + "/upload/projectpartimages/partimages_" + project_id + "_" + urls;
        Log.e(TAG, "download_images:partimage  " + urldisplay);
        Bitmap mIcon = null;
        try {

            InputStream in = new URL(urldisplay).openStream();
            mIcon = BitmapFactory.decodeStream(in);

        } catch (Exception e) {

            Log.e(TAG, "Error" + e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}