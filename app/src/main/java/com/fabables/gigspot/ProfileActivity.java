package com.fabables.gigspot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kaaes.spotify.webapi.android.models.Track;

public class ProfileActivity extends AppCompatActivity implements TrackManager.View {

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    // Top Tracks
    private TrackManager.ActionListener mTrackActionListener;
    private LinearLayoutManager mTrackLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private ScrollListener mTrackScrollListener = new ScrollListener(mTrackLayoutManager);
    private SearchResultsAdapter mTrackAdapter;

    // Top Artists
    private ArtistManager.ActionListener mArtistActionListener;
    private LinearLayoutManager mArtistLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private ScrollListener mArtistScrollListener = new ScrollListener(mTrackLayoutManager);
    private SearchResultsAdapter mArtistAdapter;

    // Spinner
    private Spinner mTimeRangeSpinner;
    private boolean mTimeRangeHasChanged = false;
    private String mTimeRange = "short_term";

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mTimeRangeHasChanged = true;
            return false;
        }
    };


    private class ScrollListener extends ResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mTrackActionListener.loadMoreResults();
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String token = extras.getString("token");

            mTrackActionListener = new SearchPresenter(this, this);
            mTrackActionListener.init(token);
        }

        // Setup search results list
        mTrackAdapter = new SearchResultsAdapter(this, new SearchResultsAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, Track item) {
                mTrackActionListener.selectTrack(item);
            }
        });

        // Top tracks recycler view
        RecyclerView topTracksList = (RecyclerView) findViewById(R.id.top_tracks);
        topTracksList.setHasFixedSize(true);
        topTracksList.setLayoutManager(mTrackLayoutManager);
        topTracksList.setAdapter(mTrackAdapter);
        topTracksList.addOnScrollListener(mTrackScrollListener);

        // Top artists recycler view
        RecyclerView topArtistsList = (RecyclerView) findViewById(R.id.top_artists);
        topArtistsList.setHasFixedSize(true);
        topArtistsList.setLayoutManager(mArtistLayoutManager);
        topArtistsList.setAdapter(mArtistAdapter);
        topArtistsList.addOnScrollListener(mArtistScrollListener);

        // Get Spinner
        mTimeRangeSpinner = (Spinner) findViewById(R.id.time_range_spinner);
        mTimeRangeSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();

        mTrackActionListener.update(mTimeRange);
    }

    private void setupSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_range_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mTimeRangeSpinner.setAdapter(adapter);

        // Set the integer mSelected to the constant values
        mTimeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.time_range_short))) {
                        mTimeRange = "short_term"; // Weeks
                    } else if (selection.equals(getString(R.string.time_range_medium))) {
                        mTimeRange = "medium_term"; // Months
                    } else {
                        mTimeRange = "long_term"; // Years
                    }

                    mTrackActionListener.update(mTimeRange);
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTimeRange = "short_term"; // Weeks
            }
        });
    }

    @Override
    public void reset() {
        mTrackScrollListener.reset();
        mTrackAdapter.clearData();
    }

    @Override
    public void addData(List<Track> items) {
        mTrackAdapter.addData(items);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTrackActionListener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTrackActionListener.resume();
    }

    @Override
    protected void onDestroy() {
        mTrackActionListener.destroy();
        super.onDestroy();
    }

}