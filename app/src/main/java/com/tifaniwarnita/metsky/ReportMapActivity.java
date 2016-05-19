package com.tifaniwarnita.metsky;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;

public class ReportMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setMetSkyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_map);

        WebView myBrowser = (WebView) findViewById(R.id.mybrowser);
        myBrowser.loadUrl("http://weather.meteo.itb.ac.id/wcplfsys/wrmap/wrmap.html");
        myBrowser.getSettings().setJavaScriptEnabled(true);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.report_map_progress_bar);
        myBrowser.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(progress);
                if(progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setMetSkyTheme() {
        int emotion = MetSkyPreferences.getEmotion(getApplicationContext());
        switch (emotion) {
            case EmotionFragment.EMOTION_TWINK:
                setTheme(R.style.AppTheme_Blue);
                break;
            case EmotionFragment.EMOTION_SURPRISED:
                setTheme(R.style.AppTheme_LightBlue);
                break;
            case EmotionFragment.EMOTION_HAPPY:
                setTheme(R.style.AppTheme_Orange);
                break;
            case EmotionFragment.EMOTION_FLAT:
                setTheme(R.style.AppTheme_Green);
                break;
            case EmotionFragment.EMOTION_ANGRY:
                setTheme(R.style.AppTheme_Red);
                break;
            default:
                break;
        }
    }

}
