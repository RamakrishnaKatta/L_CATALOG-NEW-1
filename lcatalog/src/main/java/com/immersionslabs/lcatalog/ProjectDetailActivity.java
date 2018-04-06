package com.immersionslabs.lcatalog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.immersionslabs.lcatalog.Utils.EnvConstants;
import com.immersionslabs.lcatalog.adapters.CampaignAdapter;
import com.immersionslabs.lcatalog.adapters.ImageSliderAdapter;
import com.immersionslabs.lcatalog.adapters.ProjectPartAdapter;
import com.immersionslabs.lcatalog.network.ApiCommunication;
import com.immersionslabs.lcatalog.network.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectDetailActivity extends AppCompatActivity implements ApiCommunication {

    private static final String REGISTER_URL = EnvConstants.APP_BASE_URL + "/getProjectDetails/";
    private static String PROJECT_PART_URL = null;

    private ViewPager viewpager;
    private LinearLayout slider_dots;
    ImageSliderAdapter imageSliderAdapter;
    ArrayList<String> slider_images = new ArrayList<>();
    TextView[] dots;
    int page_position = 0;
    TextView prject_name, project_description, project_Sdescription;
    ImageView project_image;
    String image1, image2, image3, image4, image5;

    String project_id;

    RecyclerView recyclerView;
    ProjectPartAdapter adapter;
    GridLayoutManager ProjectpartManager;


    private ArrayList<String> project_part;
    private ArrayList<String> project_partName;
    private ArrayList<String> project_partDesc;
    private ArrayList<String> project_partimages;
    private ArrayList<String> project_part_articlesIds;
    private ArrayList<String> project_part_articlesData;

    private static final String TAG = "ProjectDetailActivty";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page);


        Toolbar toolbar = findViewById(R.id.toolbar_projects);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        prject_name = findViewById(R.id.prject_title_text);
        project_description = findViewById(R.id.project_description_text);
        project_Sdescription = findViewById(R.id.project_subdescription_text);
        project_image = findViewById(R.id.project_image_view);


        recyclerView = findViewById(R.id.project_part_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        final Bundle b = getIntent().getExtras();

        project_id = (String) b.getCharSequence("_id");
        Log.e(TAG, "project_id ---- " + project_id);


        PROJECT_PART_URL = REGISTER_URL + project_id;
        Log.e(TAG, "PROJECT_PART_URL------" + PROJECT_PART_URL);


        prject_name.setText(b.getCharSequence("projectName"));
        Log.e(TAG, "qweqeqwqw  " + prject_name);
        project_description.setText(b.getCharSequence("projectDescription"));
        project_Sdescription.setText(b.getCharSequence("projectSubDescription"));


        project_part = new ArrayList<>();
        project_partName = new ArrayList<>();
        project_partDesc = new ArrayList<>();
        project_partimages = new ArrayList<>();
        project_part_articlesIds = new ArrayList<>();
        project_part_articlesData = new ArrayList<>();


        try {
            getProjectData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String project_images = (String) b.getCharSequence("images");

//        try {
//            JSONArray image_json = new JSONArray(project_images);
//            for (int i = 0; i < image_json.length(); i++) {
//                image1 = image_json.getString(0);
//                image2 = image_json.getString(1);
//                image3 = image_json.getString(2);
//                image4 = image_json.getString(3);
//                image5 = image_json.getString(4);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        Log.e(TAG, "Project Image 1----" + image1);
        Log.e(TAG, "Project Image 2----" + image2);
        Log.e(TAG, "Project Image 3----" + image3);
        Log.e(TAG, "Project Image 4----" + image4);
        Log.e(TAG, "Project Image 5----" + image5);

//        final String[] Images = {image1, image2, image3, image4, image5};
//        Collections.addAll(slider_images, Images);

//        viewpager = findViewById(R.id.article_view_pager);
//        imageSliderAdapter = new ImageSliderAdapter(ProjectDetailActivity.this, slider_images);
//        viewpager.setAdapter(imageSliderAdapter);

//        slider_dots = findViewById(R.id.project_slide_dots);
//        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                addBottomDots(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }

    private void getProjectData() throws JSONException {

        ApiService.getInstance(this).getData(this, false, "PROJECT_PART_DATA", PROJECT_PART_URL, "PROJECT_PART");

    }

//    private void addBottomDots(int currentPage) {
//        dots = new TextView[slider_images.size()];
//
//        slider_dots.removeAllViews();
//
//        for (int i = 0; i < dots.length; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(Color.WHITE);
//            slider_dots.addView(dots[i]);
//        }
//
//        if (dots.length > 0)
//            dots[currentPage].setTextColor(Color.parseColor("#004D40"));
//
//    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();

        Intent intent = new Intent(this, CampaignActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    public void onResponseCallback(JSONObject response, String flag) {
        if (flag.equals("PROJECT_PART")) {
            try {
                JSONObject resp = response.getJSONObject("data");
                Log.e(TAG, "responseproject: " + response);
                JSONArray parts = resp.getJSONArray("parts");
                Log.e(TAG, "partsjson: " + parts);
                getdata(parts);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void getdata(JSONArray parts) {
        for (int i = 0; i < parts.length(); i++) {
            JSONObject object = null;

            try {
                object = parts.getJSONObject(i);
                project_part.add(object.getString("part"));
                project_partName.add(object.getString("partName"));
                project_partDesc.add(object.getString("partDesc"));
                project_partimages.add(object.getString("partimages"));
                project_part_articlesIds.add(object.getString("articlesId"));
                project_part_articlesData.add(object.getString("articlesData"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "part" + project_part);
        Log.e(TAG, "partName" + project_partName);
        Log.e(TAG, "partDesc" + project_partDesc);
        Log.e(TAG, "partimages" + project_partimages);
        Log.e(TAG, "articlesId" + project_part_articlesIds);
        Log.e(TAG, "articlesData" + project_part_articlesData);

        ProjectpartManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(ProjectpartManager);

        adapter = new ProjectPartAdapter(this, project_part, project_partName, project_partDesc, project_partimages, project_part_articlesIds, project_part_articlesData);

    }


    @Override
    public void onErrorCallback(VolleyError error, String flag) {

    }
}