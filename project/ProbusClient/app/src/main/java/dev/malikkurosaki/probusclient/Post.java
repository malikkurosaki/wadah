package dev.malikkurosaki.probusclient;

import com.google.gson.annotations.SerializedName;

class Post {

    @SerializedName("nm_cus")
    private String text;

    String getText() {
        return text;
    }
}
