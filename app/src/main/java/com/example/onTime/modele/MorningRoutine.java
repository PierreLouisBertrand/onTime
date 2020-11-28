package com.example.onTime.modele;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui représente une morning routine composée d'un nom et d'une liste de taches.
 */
public class MorningRoutine implements Parcelable {
    private String nom;
    private List<Tache> listeTaches;

    /**
     * Constrcteur avec le nom et la liste des taches qui pourra être utilisér lors d'un partage de routine
     *
     * @param nom         est le nom de la routine
     * @param listeTaches est la liste des taches
     */
    public MorningRoutine(String nom, List<Tache> listeTaches) {
        this.nom = nom;
        this.listeTaches = listeTaches;
    }

    /**
     * Constructeur pour créer une nouvelle mornging routine
     *
     * @param nom est le nom de la morning routine
     */
    public MorningRoutine(String nom) {
        this.nom = nom;
        this.listeTaches = new ArrayList<>();
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MorningRoutine> CREATOR = new Parcelable.Creator<MorningRoutine>() {
        @Override
        public MorningRoutine createFromParcel(Parcel in) {
            return new MorningRoutine(in);
        }

        @Override
        public MorningRoutine[] newArray(int size) {
            return new MorningRoutine[size];
        }
    };

    protected MorningRoutine(Parcel in) {
        nom = in.readString();
        if (in.readByte() == 0x01) {
            listeTaches = new ArrayList<>();
            in.readList(listeTaches, Tache.class.getClassLoader());
        } else {
            listeTaches = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        if (listeTaches == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(listeTaches);
        }
    }

    /**
     * Ajoute une tache a la morning routine
     *
     * @param tache est la tache à ajouter
     * @return un bool pour savoir si l'ajout s'est effectué
     */
    public boolean ajouterTache(Tache tache) {
        return this.listeTaches.add(tache);
    }

    /**
     * Méthode qui supprime une tache passée en paramètres
     *
     * @param tache est la tache a supprimmer
     * @return true si la tache a été supprimmée, false sinon
     */
    public boolean supprimmerTache(Tache tache) {
        return this.listeTaches.remove(tache);
    }

    /**
     * Méthode qui supprime la tache par rapport à un index
     *
     * @param index est l'index de la tache a supprimmer
     * @return la tache supprimée ou null
     */
    public Tache supprimmerTache(int index) {
        return this.listeTaches.remove(index);
    }

    public String getNom() {
        return this.nom;
    }

    public List<Tache> getListeTaches() {
        return this.listeTaches;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
