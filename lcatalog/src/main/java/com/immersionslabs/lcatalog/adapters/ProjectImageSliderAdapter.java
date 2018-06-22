package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.ProjectDetailActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.EnvConstants;

import java.util.ArrayList;

public class ProjectImageSliderAdapter extends PagerAdapter {

    private ArrayList<String> Images;
    private LayoutInflater inflater;
    private Activity activity;
    private AppCompatImageView images;
    private String project_id;
    private static final String TAG = "ProjectImageSliderAdapter";
    private Context context;

    public ProjectImageSliderAdapter(Context context,
                                     ArrayList<String> slider_images,
                                     String project_id) {
        this.context = context;
        this.Images =slider_images;
        this.project_id =project_id;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_project_design, container, false);
        Log.e(TAG, "projectimage  " + project_id);

        images = v.findViewById(R.id.project_image_view);
        String urls = Images.get(position);
        Log.e(TAG, "instantiateItem:urls" + urls);


        Glide.with(context)
                .load(EnvConstants.APP_BASE_URL + "/upload/projectimages/" + project_id + urls)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(images);

        container.addView(v);
        return v;
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}
