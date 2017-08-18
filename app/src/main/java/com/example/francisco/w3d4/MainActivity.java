package com.example.francisco.w3d4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.francisco.w3d4.model.Flickr;
import com.example.francisco.w3d4.model.Item;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String BASE_URL = "http://api.flickr.com/services/feeds/photos_public.gne?tag=kitten&format=json&nojsoncallback=1";

    @BindView(R.id.rvFlickrList)
    RecyclerView rvFlickrList;


    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;
    ItemListAdapter randomsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        ButterKnife.bind(this);

        GetDataRetrofit();

        rvFlickrList.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    String resultResponse = "";

    public void GetDataRetrofit() {
        retrofit2.Call<Flickr> flickrDataCall = RetrofitHelper.getFlickerCall();
        flickrDataCall.enqueue(new retrofit2.Callback<Flickr>() {
            @Override
            public void onResponse(retrofit2.Call<Flickr> call, retrofit2.Response<Flickr> response) {
                Log.d(TAG, "onResponse: " + response.body().getItems().get(0).getTitle() + " " + Thread.currentThread());

                ArrayList<Item> randomsList = (ArrayList<Item>) response.body().getItems();
                Log.d(TAG, "onResponse2: " + randomsList.size());
                rvFlickrList = (RecyclerView) findViewById(R.id.rvFlickrList);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                itemAnimator = new DefaultItemAnimator();
                rvFlickrList.setLayoutManager(layoutManager);
                rvFlickrList.setItemAnimator(itemAnimator);

                //initialize the adapter
                randomsListAdapter = new ItemListAdapter(randomsList);
                rvFlickrList.setAdapter(randomsListAdapter);

                randomsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<Flickr> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });


    }
}
