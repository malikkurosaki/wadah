package probus.malikkurosaki.probussystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_Hiburan extends AppCompatActivity {

    @BindView(R.id.gameView)
    WebView gameView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__hiburan);
        ButterKnife.bind(this);

        gameView.loadUrl("file:///android_asset/index.html");
        gameView.getSettings().setJavaScriptEnabled (true);

    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        gameView.loadUrl("file:///android_asset/index.html");
        finish();
        super.onBackPressed();

    }
}
