package com.example.aprendaingles;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumerosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumerosFragment extends Fragment implements View.OnClickListener{
    private ImageButton buttonUm, buttonDois, buttonTres, buttonQuatro, buttonCinco, buttonSeis;
    private MediaPlayer mediaPlayer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NumerosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NumerosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NumerosFragment newInstance(String param1, String param2) {
        NumerosFragment fragment = new NumerosFragment();
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
        View view = inflater.inflate(R.layout.fragment_numeros, container, false);

        buttonUm = view.findViewById(R.id.buttonUm);
        buttonDois = view.findViewById(R.id.buttonDois);
        buttonTres = view.findViewById(R.id.buttonTres);
        buttonQuatro = view.findViewById(R.id.buttonQuatro);
        buttonCinco = view.findViewById(R.id.buttonCinco);
        buttonSeis = view.findViewById(R.id.buttonSeis);

        buttonUm.setOnClickListener(this);
        buttonDois.setOnClickListener(this);
        buttonTres.setOnClickListener(this);
        buttonQuatro.setOnClickListener(this);
        buttonCinco.setOnClickListener(this);
        buttonSeis.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonUm){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.one);
            tocarSom();
        }if (id == R.id.buttonDois){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.two);
            tocarSom();
        }if (id == R.id.buttonTres){
                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.three);
            tocarSom();
        }if (id == R.id.buttonQuatro){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.four);
            tocarSom();
        }if (id == R.id.buttonCinco){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.five);
            tocarSom();
        }if (id == R.id.buttonSeis){
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.six);
            tocarSom();
        }
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