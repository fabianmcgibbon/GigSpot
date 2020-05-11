package com.fabables.gigspot;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.client.Response;

public class TopArtistsPager_old {

    private final SpotifyService mSpotifyApi;
    private int mCurrentOffset;
    private int mPageSize;
    private String mTimeRange;

    public interface CompleteListener {
        void onComplete(Pager<Artist> items);
        void onError(Throwable error);
    }

    public TopArtistsPager_old(SpotifyService spotifyApi) {
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
        options.put(SpotifyService.LIMIT, limit);
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.TIME_RANGE, timeRange);

        mSpotifyApi.getTopArtists(options, new SpotifyCallback<Pager<Artist>>() {
            @Override
            public void success(Pager<Artist> artistsPager, Response response) {
                listener.onComplete(artistsPager);
            }

            @Override
            public void failure(SpotifyError error) {
                listener.onError(error);
            }
        });
    }
}
