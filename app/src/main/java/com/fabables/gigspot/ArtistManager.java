package com.fabables.gigspot;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistManager {

    public interface View {
        void reset();

        void addData(List<Artist> items);
    }

    public interface ActionListener {

        void init(String token);

        void loadMoreResults();

        void resume();

        void pause();

        void destroy();

        void update(String timeRange);
    }
}
