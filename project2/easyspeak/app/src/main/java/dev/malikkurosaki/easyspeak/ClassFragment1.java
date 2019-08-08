package dev.malikkurosaki.easyspeak;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import java.util.ArrayList;
import java.util.List;

public class ClassFragment1 extends Fragment {

    private View view;
    private Context context;
    private Activity activity;
    private DroidSpeech mendengarkan;
    private RecyclerView soalContainer;

    private boolean bolehMendengarkan = false;

    ClassFragment1 newInstance(){
        return new ClassFragment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_view_fragment1,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;
        soalContainer = view.findViewById(R.id.soalContainer);

        mendengarkan = new DroidSpeech(context,null);


        mendengarkan.setOnDroidSpeechListener(new OnDSListener() {
            @Override
            public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
                mendengarkan.setPreferredLanguage("en-US");
            }

            @Override
            public void onDroidSpeechRmsChanged(float rmsChangedValue) {
                if (!bolehMendengarkan){
                   terlarang();
                }
            }

            @Override
            public void onDroidSpeechLiveResult(String liveSpeechResult) {

            }

            @Override
            public void onDroidSpeechFinalResult(String finalSpeechResult) {

            }

            @Override
            public void onDroidSpeechClosedByUser() {

            }

            @Override
            public void onDroidSpeechError(String errorMsg) {

            }
        });

        final List<String> listSoal = new ArrayList<>();
        listSoal.add("how are you");
        listSoal.add("iam fine");
        listSoal.add("thank");
        listSoal.add("and yout how are you");

        final ListSoalAdapter listSoalAdapter = new ListSoalAdapter(context,listSoal);
        soalContainer.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        soalContainer.setAdapter(listSoalAdapter);

        listSoalAdapter.setKetikaSoalDiklik(new ListSoalAdapter.KetikaSoalDiklik() {
            @Override
            public void maka(View view, int position) {
                listSoal.remove(position);
                listSoalAdapter.notifyItemRemoved(position);
                listSoalAdapter.notifyItemRangeChanged(position, listSoal.size());
            }
        });

    }

    void terlarang(){
        mendengarkan.closeDroidSpeechOperations();
        bolehMendengarkan = false;

    }
}
