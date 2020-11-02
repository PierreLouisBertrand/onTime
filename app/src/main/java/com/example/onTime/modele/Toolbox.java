package com.example.onTime.modele;


import com.example.onTime.services.GoogleMapsAPI;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Classe qui sert de boite à outil
 */
 public final class Toolbox {

    private Toolbox() { }

    static public long getSecondesFromHeureMinute(int heures, int minute) {
        return (heures * 3600 + minute * 60);
    }

    /**
     * Retourne le nombre d'heures depuis les secondes
     * @param secondes le nombre de secondes a convertir en heure
     * @return le nomre de secodnes en heures
     */
    static public int getHourFromSecondes(long secondes){
        return (int)(secondes / 3600);
    }

    /**
     * Méthode qui retourne une heure sous le format HH:MM
     * @param heure est l'heure de 0 à 23
     * @param minutes sont les minutes de 0 à 59
     * @return un String sous le format HH:MM
     */
    static public String formaterHeure(int heure, int minutes){
        String total = "";
        if (heure < 10) {
            total += "0";
        }
        total += heure + ":";
        if (minutes < 10){
            total += "0";
        }
        total += minutes;
        return total;
    }

    /**
     * Méthode qui retourne les minutes dans à partir d'un nombre de secondes (modulo 60 minutes)
     * @param secondes le nombre de seconde a convertir en minutes
     * @return le nombre de secondes en minutes % 60
     */
    static public int getMinutesFromSecondes(long secondes){
        return (int)((secondes % 3600) / 60);
    }

    /**
     * Méthode qui retourne les minutes (arrondi à l'entier supérieur) à partir d'un nombre de secondes
     * @param secondes le nombre de secondes à convertir en minutes
     * @return le nombre de secondes en minutes (arrondi à l'entier supérieur)
     */
    static public int getMinutesRoundedUpFromSecondes(long secondes) {
        return (int)(secondes + 60 - 1) / 60;
    }

    /**
     * Méthode qui retourne le nombre de secondes de la date passée en paramétre depuis Epoch
     * @param date est la date souhaitée
     * @return les secondes entre le 01/01/1970 et date
     */
    static public long getSecondesFromEpoch(Date date) {
        return (date.getTime() / 1000);
    }

    /**
     * Méthode qui prend une heure en secondes et qui retourne la date en epoch en secondes de l'heure d'arivee. Il faut ajouter la timezone à l'heure retournée
     * Ex: S'il est 8h et que j'apelle cette fonction avec 39600 (11h en secondes) alors ça retourne la date d'aujourd'hui à 9h en GMT soit 11h dans la bonne timezone
     * @param arrivee est l'heure d'arivee dans cette timezone
     * @return la date en epoch en secondes de l'heure d'arivee. Il faut ajouter la timezone à l'heure retournée.
     */
    static public long getDateFromHeureArrivee(long arrivee) {
        Calendar rightNow = Calendar.getInstance();
        Calendar heureArriveeAjd = new GregorianCalendar(rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DAY_OF_MONTH), Toolbox.getHourFromSecondes(arrivee), Toolbox.getMinutesFromSecondes(arrivee));
        long secondesFromEpochArivee = Toolbox.getSecondesFromEpoch(heureArriveeAjd.getTime());

        if (rightNow.after(heureArriveeAjd)) { // si on est après l'heure indiquée aujourd'hui alors on passe au lendemain
            return secondesFromEpochArivee + 86400; // date de l'heure d'arrivée du lendemain (+24h en secondes) sans le timezone
        } else {
            return secondesFromEpochArivee; // date de l'heure d'arrivée sans la timeZone
        }
    }

    static public long getTimeOfTravelWithTraffic(long arrivalTime, String adresseDepart, String adresseArrivee) throws ExecutionException, InterruptedException {
        GoogleMapsAPI googleMapsAPI = new GoogleMapsAPI(arrivalTime, adresseDepart, adresseArrivee);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(googleMapsAPI);
        return future.get();
    }


}
