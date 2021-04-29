package com.fcodex.videoplayer.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fcodex.videoplayer.Modal.Modal;
import com.fcodex.videoplayer.R;
import com.fcodex.videoplayer.RecyclerViewAdapter.VideoRecyclerViewAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private InterstitialAd interstitialAd;
    private final ArrayList<Modal> videosList = new ArrayList<>();
    private VideoRecyclerViewAdapter adapterVideoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        id();
        setRecyclerView();
        permissions();
        interstitialAdMethod();
        bannerAdMethod();

        return view;
    }

    private void bannerAdMethod() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getActivity(), initializationStatus -> {
        });
        AdView bannerAdView = view.findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);
    }

    private void permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
            } else {
                showVideos();
            }
        } else {
            showVideos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showVideos();
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showVideos() {
        String[] projection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION};
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

        @SuppressLint("Recycle")
        Cursor cursor = getActivity().getApplication().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);
        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);
                int duration = cursor.getInt(durationColumn);

                Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                String duration_formatted;
                int sec = (duration / 1000) % 60;
                int min = (duration / (1000 * 60)) % 60;
                int hrs = duration / (1000 * 60 * 60);

                if (hrs == 0) {
                    duration_formatted = String.valueOf(min).concat(":".concat(String.format(Locale.UK, "%02d", sec)));
                } else {
                    duration_formatted = String.valueOf(hrs).concat(":".concat(String.format(Locale.UK, "%02d", min)
                            .concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
                }

                videosList.add(new Modal(id, data, title, duration_formatted));
                getActivity().runOnUiThread(() -> adapterVideoList.notifyItemInserted(videosList.size() - 1));
            }
        }
    }

    private void id() {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //3 = column count
        adapterVideoList = new VideoRecyclerViewAdapter(getActivity(), videosList);
        recyclerView.setAdapter(adapterVideoList);
    }

    // Interstitial ad start
    private void interstitialAdMethod() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getActivity(), initializationStatus -> {
        });
        interstitialAd = new InterstitialAd(getActivity());
        // set the ad unit ID
        interstitialAd.setAdUnitId(getString(R.string.interstitial_test_id));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
    // Interstitial ad end


}