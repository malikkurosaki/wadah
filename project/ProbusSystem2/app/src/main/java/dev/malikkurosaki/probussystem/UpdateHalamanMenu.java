package dev.malikkurosaki.probussystem;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

public class UpdateHalamanMenu extends Fragment {

    private Spinner pilihLayout;
    private EditText isian;
    private Button simpanIsian;
    private View view;
    private DatabaseReference db;
    private String namalayout;

    private MarkdownView mdlihat;
    private boolean terlihat = false;
    private ProgressBar loading;

    public static UpdateHalamanMenu newInstance(){
        return new UpdateHalamanMenu();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_update_halaman_menu,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        pilihLayout = view.findViewById(R.id.pilihLayout);
        isian = view.findViewById(R.id.isian);
        simpanIsian = view.findViewById(R.id.simpanIsian);
        loading = view.findViewById(R.id.loading);
        mdlihat = view.findViewById(R.id.mdlihat);

        // firebase

        db = FirebaseDatabase.getInstance().getReference();

        // loading
        loading.setVisibility(View.GONE);


        InternalStyleSheet css = new Github();
        css.addMedia("screen and (min-width: 1281px)");
        css.addRule("h1", "color: orange");
        css.endMedia();
        css.addRule("body", "width:100%","padding:0px");
        css.addRule("img", "width:100%","padding:0px");
        mdlihat.addStyleSheet(css);

        final ArrayList<String> daftarLayout = new ArrayList<>();
        daftarLayout.add("pilih layout yang aka diedit");
        daftarLayout.add("visi dan misi");
        daftarLayout.add("product");
        daftarLayout.add("services");
        daftarLayout.add("probus team");
        daftarLayout.add("price list");
        daftarLayout.add("video tutorial");
        daftarLayout.add("live support");
        daftarLayout.add("downloads");
        daftarLayout.add("galery");
        daftarLayout.add("our client");

        ArrayAdapter<String> listMenu = new ArrayAdapter<>(getContext(), R.layout.spinner_layout, daftarLayout);
        listMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilihLayout.setAdapter(listMenu);

        pilihLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == 0)){
                    namalayout = daftarLayout.get(position);
                    db.child("halamanmenu").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(namalayout)){
                                for (DataSnapshot ds : dataSnapshot.child(namalayout).getChildren()){
                                    isian.setText((CharSequence) ds.getValue());
                                }
                            }else {
                                isian.setText("data yang anda tunjuk belum ada");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        simpanIsian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namalayout == null){
                    Toast.makeText(getContext(),"pilih halaman dulu ges",Toast.LENGTH_LONG).show();
                    return;
                }
                loading.setVisibility(View.VISIBLE);
                final String datanya = isian.getText().toString().trim();

                db.child("halamanmenulock").child(namalayout).child("data").setValue(datanya).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            db.child("halamanmenu").child(namalayout).child("data").setValue(datanya).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        loading.setVisibility(View.GONE);
                                        Toast.makeText(getContext(),"ya berhasil",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });


        isian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (namalayout == null){
                    Toast.makeText(getContext(),"pilih halaman yang ingin diedit",Toast.LENGTH_LONG).show();
                }
                mdlihat.loadMarkdown(isian.getText().toString());
            }
        });
    }
}
