package com.fabables.gigspot;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class TrackManager {

    public interface View {
        void reset();

        void addData(List<Track> items);
    }

    public interface ActionListener {

        void init(String token);

        void loadMoreResults();

        void selectTrack(Track item);

        void resume();

        void pause();

        void destroy();

        void update(String timeRange);
    }
}
