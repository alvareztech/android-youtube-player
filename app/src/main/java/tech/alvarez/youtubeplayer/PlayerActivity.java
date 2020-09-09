package tech.alvarez.youtubeplayer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        YouTubePlayerView playerView = findViewById(R.id.playerView);

        if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this) == YouTubeInitializationResult.SUCCESS) {
            playerView.initialize("YOUR_API_KEY", this);
        } else {
            WebView webView = findViewById(R.id.webView);

            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAppCacheEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webView.getSettings().setMediaPlaybackRequiresUserGesture(true); // autoplay
            }
            webView.getSettings().setDomStorageEnabled(true);

            String videoId = getIntent().getStringExtra("videoId");
            webView.loadData(getYouTubeHtml(videoId, this), "text/html", "UTF-8");
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        String videoId = getIntent().getStringExtra("videoId");
        youTubePlayer.loadVideo(videoId);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
        if (error.isUserRecoverableError()) {
            error.getErrorDialog(this, 1).show();
            return;
        }
        Toast.makeText(this, "Error: " + error.name(), Toast.LENGTH_SHORT).show();
    }

    public static String getYouTubeHtml(String videoId, Context context) {
        String html = null;
        try {
            InputStream inputStream = context.getAssets().open("youtube.html");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            html = new String(buffer);
            html = html.replace("videoCode", videoId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    public static void openYouTubeWeb(String videoId, Context context) {
        try {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
            context.startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}