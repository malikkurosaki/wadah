package com.malikkurosaki.bestwahunter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;

public class Main2Activity extends AppCompatActivity {

    private MarkdownView infomd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();

        infomd.addStyleSheet(new Github());
        infomd.loadMarkdownFromAsset("README.md");

    }

    public void init(){

        infomd = findViewById(R.id.infomd);
    }
}
