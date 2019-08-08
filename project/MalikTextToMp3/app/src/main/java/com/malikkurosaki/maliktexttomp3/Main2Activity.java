package com.malikkurosaki.maliktexttomp3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;

public class Main2Activity extends AppCompatActivity {

    private MarkdownView markdownView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        markdownView = findViewById(R.id.markdownview);

        markdownView.addStyleSheet(new Github());
        markdownView.loadMarkdownFromAsset("README.md");
    }
}
