package com.example.onTime.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onTime.R;
import com.example.onTime.adapters.TachesRecurrentesAdapter;
import com.example.onTime.item_touch_helpers.ItemTouchHelperTacheRecurrentes;
import com.example.onTime.modele.Tache;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListTachesRecurrentesFragment extends Fragment {

    private List<Tache> listeTaches;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private TachesRecurrentesAdapter tachesRecurrentesAdapter;

    public ListTachesRecurrentesFragment() {
        // Required empty public constructor
    }

    public static ListTachesRecurrentesFragment newInstance() {
        ListTachesRecurrentesFragment fragment = new ListTachesRecurrentesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list_taches_recurrentes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.listeTaches = new ArrayList<>();
        Context context = this.getActivity().getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences("onTimePreferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = this.sharedPreferences.getString("listeTachesRec", "");
        if (json != "") {
            Type type = new TypeToken<List<Tache>>(){}.getType();
            this.listeTaches = gson.fromJson(json, type);
        }

        this.recyclerView = view.findViewById(R.id.taches_recurrentes_recyclerview);

        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.tachesRecurrentesAdapter = new TachesRecurrentesAdapter(this.listeTaches);
        this.recyclerView.setAdapter(this.tachesRecurrentesAdapter);

        ItemTouchHelperTacheRecurrentes itemTouchHelperTacheRecurrentes = new ItemTouchHelperTacheRecurrentes(getActivity(), this.tachesRecurrentesAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperTacheRecurrentes);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreerTacheRecurrente();
            }
        });

    }

    private void showCreerTacheRecurrente(){

        LayoutInflater factory = LayoutInflater.from(ListTachesRecurrentesFragment.this.getContext());
        final View textEntryView = factory.inflate(R.layout.ajout_tache, null);

        TextInputLayout nomTacheLayout = textEntryView.findViewById(R.id.nomtachecreate);
        final EditText nomTache = nomTacheLayout.getEditText();
        final NumberPicker duree = textEntryView.findViewById(R.id.duree);
        duree.setMinValue(0);
        duree.setMaxValue(60);

        final MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(ListTachesRecurrentesFragment.this.getContext());

        alert.setTitle("Créer une nouvelle tache :")
                .setView(textEntryView)
                .setPositiveButton("Sauvegarder",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Tache t = new Tache(nomTache.getText().toString(),duree.getValue()*60);
                                ListTachesRecurrentesFragment.this.listeTaches.add(t);
                                ListTachesRecurrentesFragment.this.tachesRecurrentesAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        });
        alert.show();
    }

    @Override
    public void onResume() {
        Context context1 = getActivity().getApplicationContext();
        this.sharedPreferences = context1.getSharedPreferences("onTimePreferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = this.sharedPreferences.getString("listeTachesRec", "");

        if (json != "") {
            Type type = new TypeToken<List<Tache>>(){}.getType();
            //this.listeTaches = gson.fromJson(json, type);
        }

        super.onResume();
    }

    @Override
    public void onStop() {
        Context context = this.getActivity().getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences("onTimePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.listeTaches);
        editor.putString("listeTachesRec", json);
        editor.apply();
        super.onStop();
    }
}