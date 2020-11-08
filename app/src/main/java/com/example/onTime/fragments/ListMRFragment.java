package com.example.onTime.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onTime.R;
import com.example.onTime.modele.MRA;
import com.example.onTime.modele.MRManager;
import com.example.onTime.modele.MorningRoutine;
import com.example.onTime.modele.Toolbox;
import com.example.onTime.morning_routine.MorningRoutineActivity;
import com.example.onTime.mra.ItemTouchHelperMRA;
import com.example.onTime.mra.MorningRoutineAdressAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class ListMRFragment extends Fragment {

    private MRManager mrManager;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MorningRoutineAdressAdapter morningRoutineAdressAdapter;
    private SharedPreferences sharedPreferences;

    public ListMRFragment() {
        // Required empty public constructor
    }

    public static ListMRFragment newInstance() {
        ListMRFragment fragment = new ListMRFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_m_r, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = this.getActivity().getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences("onTimePreferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = this.sharedPreferences.getString("MRManager", "");
        this.mrManager = gson.fromJson(json, MRManager.class);


        TextView heureReveil = view.findViewById(R.id.heureReveil);
        if(heureReveil != null){
            heureReveil.setText(Toolbox.formaterHeure(Toolbox.getHourFromSecondes(this.mrManager.getHeureArrivee()), Toolbox.getMinutesFromSecondes(this.mrManager.getHeureArrivee())));
        }

        this.recyclerView = view.findViewById(R.id.morning_routine_adress_recycler_view);

        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);

        this.morningRoutineAdressAdapter = new MorningRoutineAdressAdapter(mrManager.getListMRA(), this);
        this.recyclerView.setAdapter(this.morningRoutineAdressAdapter);


        // We use a String here, but any type that can be put in a Bundle is supported
        SavedStateHandle handle = NavHostFragment.findNavController(this).getCurrentBackStackEntry().getSavedStateHandle();
        MutableLiveData<MorningRoutine> liveMorningRoutine = handle.getLiveData("morning_routine");
        final MutableLiveData<Integer> livePosition = handle.getLiveData("position");

        liveMorningRoutine.observe(getViewLifecycleOwner(), new Observer<MorningRoutine>() {
            @Override
            public void onChanged(MorningRoutine mr) {
                if(livePosition.getValue() != null){
                    ListMRFragment.this.editMR(mr, livePosition.getValue());
                }
            }
        });

        // drag and drop + swipe
        ItemTouchHelperMRA itemTouchHelperTache = new ItemTouchHelperMRA(getActivity(), this.morningRoutineAdressAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperTache);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MorningRoutineActivity.class);
                ListMRFragment.this.startActivityForResult(intent, 1);
            }
        });
    }

    public void editMR(MorningRoutine mr, int position) {
        if (position == -1 ){
            this.mrManager.ajouterMorningRoutine(mr);
            morningRoutineAdressAdapter.notifyItemInserted(mrManager.getListMRA().size());
        }else{
            MRA mra = this.mrManager.getListMRA().get(position);
            mra.setMorningRoutine(mr);
            morningRoutineAdressAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onStart() {
        Context context1 = getActivity().getApplicationContext();
        this.sharedPreferences = context1.getSharedPreferences("onTimePreferences", Context.MODE_PRIVATE);

        MorningRoutine morningRoutine;
        int position = this.sharedPreferences.getInt("position", -2);

        Gson gson = new Gson();
        String json = this.sharedPreferences.getString("morning_routine", "");
        if (json != "") {
            morningRoutine = gson.fromJson(json, MorningRoutine.class);
            if (position == -1) {
                this.mrManager.ajouterMorningRoutine(morningRoutine);
                morningRoutineAdressAdapter.notifyItemInserted(mrManager.getListMRA().size());
            } else {
                if (position >= 0) {
                    MRA mra = this.mrManager.getListMRA().get(position);
                    mra.setMorningRoutine(morningRoutine);
                    morningRoutineAdressAdapter.notifyItemChanged(position);
                }
            }
        }
        this.sharedPreferences.edit()
                .remove("morning_routine")
                .remove("position")
                .apply();

        super.onStart();
    }

    @Override
    public void onStop() {
        Context context = this.getActivity().getApplicationContext();
        this.sharedPreferences = context.getSharedPreferences("onTimePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.mrManager);
        editor.putString("MRManager", json);
        editor.apply();

        super.onStop();
    }
}