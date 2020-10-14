package com.example.onTime;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.onTime.modele.Adresse;
import com.example.onTime.modele.MRA;
import com.example.onTime.modele.MorningRoutine;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);

        List<MRA> mras = getListData();
        this.gridView = findViewById(R.id.gridView);
        this.gridView.setAdapter(new CustomGridAdapter(this, mras));
    }


    public void nextSetDestination(View view) {
        Intent intent = new Intent(this, Destination.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);
    }

    private  List<MRA> getListData() {
        MorningRoutine mr1 = new MorningRoutine("mr1");
        MorningRoutine mr2 = new MorningRoutine("mr2");
        MorningRoutine mr3 = new MorningRoutine("m3");
        Adresse a1 = new Adresse("adresse1", "d1","a1");
        Adresse a2 = new Adresse("adresse2", "d2","a2");
        Adresse a3 = new Adresse("adresse3", "d3","a3");

        MRA mra1 = new MRA(mr1, null);
        MRA mra2 = new MRA(mr2, a2);
        MRA mra3 = new MRA(mr3, a3);

        List<MRA> mras = new ArrayList<>();

        mras.add(mra1);
        mras.add(mra2);
        mras.add(mra3);

        return mras;
    }

}