package dev.malikkurosaki.probussystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

public class TanggapanDariMenu extends Fragment {

    private View view;
    private MarkdownView mdcontainer;
    private DatabaseReference db;

    public static TanggapanDariMenu newInstance(){
        return new TanggapanDariMenu();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_tanggapan_dari_menu,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        mdcontainer = view.findViewById(R.id.mdContainer);
        db = FirebaseDatabase.getInstance().getReference();


        InternalStyleSheet css = new Github();
        css.addMedia("screen and (min-width: 1281px)");
        css.addRule("h1", "color: orange");
        css.endMedia();
        css.addRule("body", "width:100%","padding:0px");
        css.addRule("img", "width:100%","padding:0px");
        mdcontainer.addStyleSheet(css);


        if (getArguments() != null){
            final String menuNya = getArguments().getString("dari");
            if (menuNya!=null){
                db.child("halamanmenu").child(menuNya).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String dataNya = dataSnapshot.getValue(String.class);
                        mdcontainer.loadMarkdown(dataNya);
                        mdcontainer.setBackgroundResource(R.color.colorBg1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else {
                Toast.makeText(getContext(),"data yang anda minta belum tersedia",Toast.LENGTH_LONG).show();
            }


        }

    }
}
