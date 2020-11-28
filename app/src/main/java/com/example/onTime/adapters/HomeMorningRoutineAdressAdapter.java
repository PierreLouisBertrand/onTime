package com.example.onTime.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onTime.R;
import com.example.onTime.fragments.HomeFragment;
import com.example.onTime.modele.MRT;

import java.util.List;

/**
 * Adapter pour le fragment HomeFragmnent
 */
public class HomeMorningRoutineAdressAdapter extends ArrayAdapter<MRT> {

    private List<MRT> listMRT;
    private HomeFragment homeFragment;
    private Context context;

    public HomeMorningRoutineAdressAdapter(@NonNull Context context, List<MRT> listMRT, HomeFragment homeFragment) {
        super(context, 0, listMRT);
        this.context = context;
        this.listMRT = listMRT;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MRT mrt = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_mr_item_layout, parent, false);
        }

        TextView mr = convertView.findViewById(R.id.home_morning_routine);
        mr.setTag(position);

        if (mrt != null) {
            if (mrt.getMorningRoutine() != null){
                mr.setText(mrt.getMorningRoutine().getNom());
            }else{
                mr.setText(R.string.aucune_mr_definie);
            }
        }

        return convertView;
    }


    public MRT getMrt(int which) {
        return this.listMRT.get(which);
    }

    /**
     * Selection d'une morning routine qui change le current_id_MRA des shared pref
     * @param position est la position de la mrt
     */
    public void choisirMRT(int position) {
        MRT mrt = getItem(position);
        HomeMorningRoutineAdressAdapter.this.homeFragment.changerCurrentMr(mrt);
        int idCurrentMRA = mrt.getId();

        SharedPreferences sharedPreferences = context.getSharedPreferences("onTimePreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("current_id_MRA", idCurrentMRA)
                .apply();
    }
}

