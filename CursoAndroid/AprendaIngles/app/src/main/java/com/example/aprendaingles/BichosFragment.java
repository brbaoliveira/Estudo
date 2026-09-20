package com.example.aprendaingles;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BichosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BichosFragment extends Fragment implements View.OnClickListener{
    private ImageButton buttonCao, buttonGato, buttonLeao, buttonMacaco, buttonOvelha, buttonVaca;
    private MediaPlayer mediaPlayer;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BichosFragment() {
        // Required empty public constructor
    }
    public static BichosFragment newInstance(String param1, String param2) {
        BichosFragment fragment = new BichosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bichos, container, false);

        buttonCao = view.findViewById(R.id.buttonCao);
        buttonGato = view.findViewById(R.id.buttonGato);
        buttonLeao = view.findViewById(R.id.buttonLeao);
        buttonMacaco = view.findViewById(R.id.buttonMacaco);
        buttonOvelha = view.findViewById(R.id.buttonOvelha);
        buttonVaca = view.findViewById(R.id.buttonVaca);

        buttonCao.setOnClickListener(this);
        buttonGato.setOnClickListener(this);
        buttonLeao.setOnClickListener(this);
        buttonMacaco.setOnClickListener(this);
        buttonOvelha.setOnClickListener(this);
        buttonVaca.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonCao){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.dog);
            tocarSom();
        }if (id == R.id.buttonGato){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cat);
            tocarSom();
        }if (id == R.id.buttonLeao){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.lion);
            tocarSom();
        }if (id == R.id.buttonMacaco){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.monkey);
            tocarSom();
        }if (id == R.id.buttonOvelha){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.sheep);
            tocarSom();
        }if (id == R.id.buttonVaca){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cow);
            tocarSom();
        }

        //switch nao aceita mais id nessa versao
        /*switch (view.getId()){
            case R.id.buttonCao :
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.dog);
                break;
        }*/
    }
    public void tocarSom(){
        if(mediaPlayer != null){
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}