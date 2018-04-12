package com.immersionslabs.lcatalog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.adapters.ProjectPartImageSliderAdapter;
import com.immersionslabs.lcatalog.adapters.ProjectpartDetailsAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectPartDetailsActivity extends AppCompatActivity implements ApiCommunication {

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getProjectDetails/";
    private static String PROJECT_PART_ARTICLE_URL = null;

    private static final String TAG = "ProjectPartDetailsActivity";

    TextView part_name, part_Desc;
    ImageView part_image;

    String image1, image2, image3, image4, image5;
    String project_id;
    RecyclerView recyclerView;
    ProjectpartDetailsAdapter adapter;
    GridLayoutManager layoutManager;

    private ArrayList<String> part_articles_id;
    private ArrayList<String> part_article_images;
    private ArrayList<String> part_article_name;
    private ArrayList<String> project_ids;

    ArrayList<String> slider_images = new ArrayList<>();
    private LinearLayout slider_dots;
    String project_part_images;
    private ViewPager viewPager;
    ProjectPartImageSliderAdapter imageSliderAdapter;
    TextView[] dots;
    int page_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_part_details);

        Toolbar toolbar = findViewById(R.id.toolbar_project_part);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.project_part_item_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        part_name = findViewById(R.id.prject_part_title_text);
        part_Desc = findViewById(R.id.project_part_description_text);
        part_image = findViewById(R.id.project_part_image_view);

        part_articles_id = new ArrayList<>();
        part_article_name = new ArrayList<>();
        part_article_images = new ArrayList<>();
        project_ids = new ArrayList<>();

        final Bundle b = getIntent().getExtras();

        project_id = (String) b.getCharSequence("_id");
        Log.e(TAG, "project_id ---- " + project_id);


        project_part_images = (String) b.getCharSequence("partimages");
        Log.e(TAG, "onCreate: projectpartimage" + project_part_images);

        try {
            JSONArray image_json = new JSONArray(project_part_images);
            for (int i = 0; i < image_json.length(); i++) {
                image1 = image_json.getString(0);
                image2 = image_json.getString(1);
                image3 = image_json.getString(2);
                image4 = image_json.getString(3);
                image5 = image_json.getString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "ProjectpartImage 1----" + image1);
        Log.e(TAG, "ProjectpartImage 2----" + image2);
        Log.e(TAG, "ProjectpartImage 3----" + image3);
        Log.e(TAG, "ProjectpartImage 4----" + image4);
        Log.e(TAG, "ProjectpartImage 5----" + image5);

        final String[] Images = {image1, image2, image3, image4, image5};

        Collections.addAll(slider_images, Images);

        viewPager = findViewById(R.id.project_part_view_pager);
        imageSliderAdapter = new ProjectPartImageSliderAdapter(ProjectPartDetailsActivity.this, slider_images, project_id);
        viewPager.setAdapter(imageSliderAdapter);

        slider_dots = findViewById(R.id.project_part_slide_dots);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        PROJECT_PART_ARTICLE_URL = REGISTER_URL + project_id;
        Log.e(TAG, "PROJECT_PART_URL------" + PROJECT_PART_ARTICLE_URL);

        part_name.setText(b.getCharSequence("partName"));
        Log.e(TAG, "onCreate:part_name " + part_name);
        part_Desc.setText(b.getCharSequence("partDesc"));

        try {
            getpartData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_images.size()];

        slider_dots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.WHITE);
            slider_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#004D40"));
    }

    final Runnable update = new Runnable() {
        @Override
        public void run() {
            if (page_position == slider_images.size()) {
                page_position = 0;
            } else {
                page_position = page_position + 1;
            }
            viewPager.setCurrentItem(page_position, true);
        }
    };

    private void getpartData() throws JSONException {
        ApiService.getInstance(this).getData(this, false, "PROJECT_PART_DATA", PROJECT_PART_ARTICLE_URL, "PART_ARTICLE");
    }

    @Override
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("PART_ARTICLE")) {
            try {
                JSONObject resp = response.getJSONObject("data");
                project_ids.add(resp.getString("_id"));
                Log.e(TAG, "responseproject " + response);

                JSONArray parts = resp.getJSONArray("parts");
                Log.e(TAG, "partsjson: " + parts);
                getData(parts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getData(JSONArray parts) {
        for (int i = 0; i < parts.length(); i++) {
            JSONObject object = null;
            try {
                object = parts.getJSONObject(i);
                JSONArray array = object.getJSONArray("articlesData");
                Log.e(TAG, "getData: articlesdata" + array);
                for (int j = 0; j < object.length(); j++) {

                    JSONObject object1 = array.getJSONObject(j);
                    part_articles_id.add(object1.getString("_id"));
                    part_article_name.add(object1.getString("name"));
                    part_article_images.add(object1.getString("img"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "getData: Article Id" + part_articles_id);
        Log.e(TAG, "getData: Article Name" + part_article_name);
        Log.e(TAG, "getData: Article Images" + part_article_images);

        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProjectpartDetailsAdapter(this, part_articles_id, part_article_name, part_article_images);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onErrorCallback(VolleyError error, String flag) {
        Toast.makeText(this, "Internal Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();

        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra("activity", "SplashScreen");
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}