package dev.malikkurosaki.probussystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;
import io.paperdb.Paper;

class LayoutPenjelasanChatBot extends BottomSheetDialog {
    private MarkdownView markdownView;
    private View view;
    private LayoutInflater inflater;

    LayoutPenjelasanChatBot(@NonNull Context context){
        super(context);
        this.inflater = LayoutInflater.from(context);
        this.view = inflater.inflate(R.layout.layout_penjelasan_chatbot,null);
        this.setContentView(view);
        Paper.init(context);

        markdownView  = findViewById(R.id.mkPenjelasan);
        InternalStyleSheet css = new Github();
        css.addRule("body", "padding:0px", "margin:0px");
        css.addRule("h1", "padding:0px", "margin:0px");
        css.addRule("p", "padding:0px", "margin:0px","color:#000033");
        css.addRule("div", "padding:0px", "margin:0px");
        markdownView.addStyleSheet(css);
        markdownView.loadMarkdown(context.getResources().getString(R.string.penjelasan));

    }
}
