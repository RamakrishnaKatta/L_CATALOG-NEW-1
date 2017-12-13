package com.immersionslabs.lcatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.immersionslabs.lcatalog.ProductPageActivity;
import com.immersionslabs.lcatalog.R;
import com.immersionslabs.lcatalog.Utils.DownloadImageTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ListViewVerticalAdapter extends RecyclerView.Adapter<ListViewVerticalAdapter.ViewHolder> {
    private static final String TAG = "ListViewVerticalAdapter";

    private Activity activity;

    private ArrayList<String> item_ids;
    private ArrayList<String> item_names;
    private ArrayList<String> item_descriptions;
    private ArrayList<String> item_prices;
    private ArrayList<String> item_discounts;
    private ArrayList<String> item_vendors;
    private ArrayList<String> item_images;
    private ArrayList<String> item_dimensions;
    private ArrayList<String> item_3ds;

    public ListViewVerticalAdapter(Activity activity,
                                   ArrayList<String> item_ids,
                                   ArrayList<String> item_names,
                                   ArrayList<String> item_descriptions,
                                   ArrayList<String> item_prices,
                                   ArrayList<String> item_discounts,
                                   ArrayList<String> item_vendors,
                                   ArrayList<String> item_images,
                                   ArrayList<String> item_dimensions,
                                   ArrayList<String> item_3ds) {

        this.item_ids = item_ids;
        this.item_names = item_names;
        this.item_descriptions = item_descriptions;
        this.item_prices = item_prices;
        this.item_discounts = item_discounts;
        this.item_vendors = item_vendors;
        this.item_images = item_images;
        this.item_dimensions = item_dimensions;
        this.item_3ds = item_3ds;

        Log.e(TAG, "ids----" + item_ids);
        Log.e(TAG, "names----" + item_names);
        Log.e(TAG, "descriptions----" + item_descriptions);
        Log.e(TAG, "prices----" + item_prices);
        Log.e(TAG, "discounts----" + item_discounts);
        Log.e(TAG, "vendors----" + item_vendors);
        Log.e(TAG, "Images----" + item_images);
        Log.e(TAG, "Dimensions----" + item_dimensions);
        Log.e(TAG, "3ds" + item_3ds);

        this.activity = activity;
    }

    /**
     * View holder to display each RecylerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_name, item_description, item_price, item_discount, item_price_new;
        private ImageView item_image;
        private RelativeLayout v_container;

        ViewHolder(View view) {
            super(view);
            v_container = view.findViewById(R.id.v_container);
            item_image = view.findViewById(R.id.v_item_image);
            item_name = view.findViewById(R.id.v_item_name);
            item_description = view.findViewById(R.id.v_item_description);
            item_price = view.findViewById(R.id.v_item_price);
            item_discount = view.findViewById(R.id.v_item_discount_value);
            item_price_new = view.findViewById(R.id.v_item_price_new);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_vertical_list, parent, false);
        //    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewVerticalAdapter.ViewHolder viewHolder, final int position) {

        final Context[] context = new Context[1];

        viewHolder.item_image.setImageResource(R.drawable.dummy_icon);
        String im1 = null;
        String get_image = item_images.get(position);
        try {

            JSONArray images_json = new JSONArray(get_image);
            for (int i = 0; i < images_json.length(); i++) {
                im1 = images_json.getString(0);
                Log.e(TAG, "image1 >>>>" + im1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide
                .with(activity)
                .load("http://35.154.150.204:4000/upload/images" + im1)
                .placeholder(R.drawable.dummy_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.item_image);
        new DownloadImageTask(viewHolder.item_image).execute(im1);

        Integer x = Integer.parseInt(item_prices.get(position));
        Integer y = Integer.parseInt(item_discounts.get(position));
        Integer z = (x * (100 - y)) / 100;
        String itemNewPrice = Integer.toString(z);

        viewHolder.item_name.setText(item_names.get(position));
        viewHolder.item_description.setText(item_descriptions.get(position));
        viewHolder.item_price.setText((Html.fromHtml("<strike>" + item_prices.get(position) + "</strike>")));
        viewHolder.item_price.setPaintFlags(viewHolder.item_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.item_discount.setText(item_discounts.get(position));
        viewHolder.item_price_new.setText(itemNewPrice);

        viewHolder.v_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context[0] = v.getContext();

                Intent intent = new Intent(context[0], ProductPageActivity.class);
                Bundle b = new Bundle();

                b.putString("article_id", item_ids.get(position));
                b.putString("article_title", item_names.get(position));
                b.putString("article_description", item_descriptions.get(position));
                b.putString("article_price", item_prices.get(position));
                b.putString("article_discount", item_discounts.get(position));
                b.putString("article_vendor", item_vendors.get(position));
                b.putString("article_dimensions", item_dimensions.get(position));
                b.putString("article_3ds", item_3ds.get(position));
                b.putString("article_images", item_images.get(position));
                b.putString("article_position", String.valueOf(position));

                intent.putExtras(b);

                context[0].startActivity(intent);
//                Toast.makeText(activity, "Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }


}
