package tech.alvarez.youtubeplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.openPlayerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerScreen();
            }
        });
    }

    private void openPlayerScreen() {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("videoId", "Zc1D84MeeyU");
        startActivity(intent);
    }
}