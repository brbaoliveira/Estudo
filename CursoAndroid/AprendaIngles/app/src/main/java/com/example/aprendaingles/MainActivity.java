package com.example.aprendaingles;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smartTabLayout = findViewById(R.id.smartTabLayout);
        viewPager = findViewById(R.id.viewPager);
        getSupportActionBar().setElevation(0);
        //Configurar adapter para abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.bichos, BichosFragment.class)
                .add(R.string.numeros, NumerosFragment.class)
                .add(R.string.vogais, VogaisFragment.class)
                .create());
        /*ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);*/
        viewPager.setAdapter(adapter);
        /*SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.smartTabLayout);*/
        smartTabLayout.setViewPager(viewPager);
    }
}