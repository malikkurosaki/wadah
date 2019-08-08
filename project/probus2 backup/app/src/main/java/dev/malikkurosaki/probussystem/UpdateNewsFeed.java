package dev.malikkurosaki.probussystem;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class UpdateNewsFeed extends Fragment {

    private View view;
    private Context context;
    private Activity activity;
    private String TAG = "-->";
    private ImageButton takeImage;
    private TextView updateFeed;
    private ImageView gambarSetelah;
    private FirebaseStorage storage;
    private ProgressBar loadingUpload;
    private Button simpanNews;
    private EditText textFeed;
    private EditText textJudul;

    private String namaPokok,namaGambar;
    private DatabaseReference db;

    UpdateNewsFeed newInstance(){
        return new UpdateNewsFeed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_update_news_feed,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;

        takeImage = view.findViewById(R.id.takeImage);
        updateFeed = view.findViewById(R.id.update);
        gambarSetelah = view.findViewById(R.id.gambarSetelah);
        loadingUpload = view.findViewById(R.id.loadingUpload);
        simpanNews = view.findViewById(R.id.simpanNews);
        loadingUpload.setVisibility(View.GONE);
        textFeed = view.findViewById(R.id.textFeed);
        textJudul = view.findViewById(R.id.textJudul);

        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),567);
            }
        });


        simpanNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaGambar == null && TextUtils.isEmpty(namaGambar)){
                    Toast.makeText(getContext(),"please upload some image",Toast.LENGTH_LONG).show();
                    return;
                }

                if (loadingUpload.getVisibility() == View.VISIBLE){
                    Toast.makeText(getContext(),"tunggu upload selesai ",Toast.LENGTH_LONG).show();
                    return;
                }

                boolean more = false;
                String feedNya = textFeed.getText().toString().trim();
                String judulNya = textJudul.getText().toString().trim();
                String contentMore = "";
                if (TextUtils.isEmpty(feedNya)){
                    Toast.makeText(getContext(),"type some text please ",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(judulNya)){
                    Toast.makeText(getContext(),"title please",Toast.LENGTH_LONG).show();
                    return;
                }

                StringBuilder moreBuilder = new StringBuilder();
                String[] hitungText = feedNya.split(" ");
                String[] hitungText2 = feedNya.split("\n");
                if (hitungText.length > 3 || hitungText2.length > 2){
                    more = true;
                    for (int a =0 ; a<3;a++){
                        moreBuilder.append(hitungText[a]).append(" ");
                    }
                    contentMore = String.valueOf(moreBuilder);
                }else {
                    more = false;
                }


                Map<String,Object> kirimFeed = new HashMap<>();
                kirimFeed.put("pokok",namaPokok);
                kirimFeed.put("gambar",namaGambar);
                kirimFeed.put("content",feedNya);
                kirimFeed.put("suka",0);
                kirimFeed.put("judul",judulNya);
                kirimFeed.put("contentmore",contentMore);


                String key = db.child("menubawah").child("newsfeed").child("kontent").push().getKey();
                kirimFeed.put("key",key);
                db.child("menubawah").child("newsfeed").child("kontent").child(key).setValue(kirimFeed).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            YoYo.with(Techniques.ZoomOutUp).duration(500).onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    gambarSetelah.setImageURI(null);
                                    textFeed.setText("");
                                    textJudul.setText("");
                                    textJudul.setHint("any title here");
                                    textFeed.setHint("do you want to update something more?");
                                    Toast.makeText(getContext(),"data was updated",Toast.LENGTH_LONG).show();
                                }
                            }).playOn(gambarSetelah);

                        }
                    }
                });



            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 567){
            if (resultCode == RESULT_OK){
                if (data.getData() != null){
                    loadingUpload.setVisibility(View.VISIBLE);

                    Uri mUri = data.getData();

                    Picasso.get().load(mUri).resize(300,300).centerCrop().into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            gambarSetelah.setImageBitmap(bitmap);

                            ByteArrayOutputStream baos=new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                            byte [] b =baos.toByteArray();

                            YoYo.with(Techniques.ZoomInRight).duration(500).playOn(gambarSetelah);

                            namaPokok = "newsfeed".toLowerCase();
                            File gambar = new File(String.valueOf(data.getData()));
                            namaGambar = gambar.getName();

                            StorageReference reference = FirebaseStorage.getInstance().getReference().child(namaPokok).child(namaGambar);
                            reference.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    loadingUpload.setVisibility(View.GONE);
                                }
                            });

                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });


                }

            }
        }


    }
}
