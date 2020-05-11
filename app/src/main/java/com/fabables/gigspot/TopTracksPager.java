package com.fabables.gigspot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;

public class TopTracksPager {

    private final SpotifyService mSpotifyApi;
    private int mCurrentOffset;
    private int mPageSize;
    private String mTimeRange;

    public interface CompleteListener {
        void onComplete(List<Track> items);
        void onError(Throwable error);
    }

    public TopTracksPager(SpotifyService spotifyApi) {
        mSpotifyApi = spotifyApi;
    }

    public void getFirstPage(String timeRange, int pageSize, CompleteListener listener) {
        mCurrentOffset = 0;
        mPageSize = pageSize;
        mTimeRange = timeRange;
        getData(timeRange, 0, pageSize, listener);
    }

    public void getNextPage(CompleteListener listener) {
        mCurrentOffset += mPageSize;
        getData(mTimeRange, mCurrentOffset, mPageSize, listener);
    }

    private void getData(String timeRange, int offset, final int limit, final CompleteListener listener) {

        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);
        options.put(SpotifyService.TIME_RANGE, timeRange);

        mSpotifyApi.getTopTracks(options, new SpotifyCallback<Pager<Track>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }

            @Override
            public void success(Pager<Track> trackPager, Response response) {
                listener.onComplete(trackPager.items);
            }
        });
    }
}
