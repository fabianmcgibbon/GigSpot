package com.fabables.gigspot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final String EXTRA_TOKEN = "EXTRA_TOKEN";

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final String token = intent.getStringExtra(EXTRA_TOKEN);

        // Find button views
        Button profileButton = (Button) findViewById(R.id.profile_button);
        final Button playlistButton = (Button) findViewById(R.id.create_playlist_button);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("token", token);
                startActivity(profileIntent);
            }
        });

        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playlistIntent = new Intent(MainActivity.this, playlistActivity.class);
                startActivity(playlistIntent);
            }
        });
    }
}
