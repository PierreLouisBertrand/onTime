package com.example.onTime.modele;


import com.example.onTime.services.GoogleMapsAPI;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

/**
 * Classe qui sert de boite à outil
 */
public final class Toolbox {

    private Toolbox() {
    }

    public static long getSecondesFromHeureMinute(int heures, int minute) {
        return (heures * 3600 + minute * 60);
    }

    /**
     * Retourne le nombre d'heures depuis les secondes
     *
     * @param secondes le nombre de secondes a convertir en heure
     * @return le nomre de secodnes en heures
     */
    public static int getHourFromSecondes(long secondes) {
        return (int) (secondes / 3600);
    }

    /**
     * Méthode qui retourne une heure sous le format HH:MM
     *
     * @param heure   est l'heure de 0 à 23
     * @param minutes sont les minutes de 0 à 59
     * @return un String sous le format HH:MM
     */
    public static String formaterHeure(int heure, int minutes) {
        String total = "";
        if (heure < 10) {
            total += "0";
        }
        total += heure + ":";
        if (minutes < 10) {
            total += "0";
        }
        total += minutes;
        return total;
    }

    /**
     * Méthode qui retourne les minutes dans à partir d'un nombre de secondes (modulo 60 minutes)
     *
     * @param secondes le nombre de seconde a convertir en minutes
     * @return le nombre de secondes en minutes % 60
     */
    public static int getMinutesFromSecondes(long secondes) {
        return (int) ((secondes % 3600) / 60);
    }

    /**
     * Méthode qui retourne les minutes (arrondi à l'entier supérieur) à partir d'un nombre de secondes
     *
     * @param secondes le nombre de secondes à convertir en minutes
     * @return le nombre de secondes en minutes (arrondi à l'entier supérieur)
     */
    public static int getMinutesRoundedUpFromSecondes(long secondes) {
        return (int) (secondes + 60 - 1) / 60;
    }

    /**
     * Méthode qui retourne le nombre de secondes de la date passée en paramétre depuis Epoch
     *
     * @param date est la date souhaitée
     * @return les secondes entre le 01/01/1970 et date
     */
    public static long getSecondesFromEpoch(Date date) {
        return (date.getTime() / 1000);
    }

    /**
     * Méthode qui prend une heure en secondes et qui retourne la date en epoch en secondes de l'heure d'arivee. Il faut ajouter la timezone à l'heure retournée
     * Ex: S'il est 8h et que j'apelle cette fonction avec 39600 (11h en secondes) alors ça retourne la date d'aujourd'hui à 11h
     *
     * @param arrivee est l'heure d'arivee dans cette timezone
     * @return la date en epoch en secondes de l'heure d'arivee. avec la timezone d'ajoutée
     */
    public static long getDateFromHeureArrivee(long arrivee) {
        Calendar rightNow = Calendar.getInstance();
        Calendar heureArriveeAjd = new GregorianCalendar(rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DAY_OF_MONTH), Toolbox.getHourFromSecondes(arrivee), Toolbox.getMinutesFromSecondes(arrivee));
        long secondesFromEpochArivee = Toolbox.getSecondesFromEpoch(heureArriveeAjd.getTime());
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        secondesFromEpochArivee += (offsetInMillis / 1000);

        if (rightNow.after(heureArriveeAjd)) { // si on est après l'heure indiquée aujourd'hui alors on passe au lendemain
            return secondesFromEpochArivee + 86400; // date de l'heure d'arrivée du lendemain (+24h en secondes) sans le timezone
        } else {
            return secondesFromEpochArivee; // date de l'heure d'arrivée sans la timeZone
        }
    }

    /**
     * Méthode qui renvoie l'heure en secondes depuis minuit à partir d'une date en secondes depuis le 1er janvier 70 minuit
     *
     * @param epoch une date/heure en format epoch
     * @return cette même date/heure sans la partie date, juste les secondes depuis minuit
     */
    public static long getHeureFromEpoch(long epoch) {
        return epoch % 86400;
    }

    /**
     * Méthode qui calcule le temps de trajet (en voiture OU vélo OU à pied) entre deux points, pour arriver à une heure donnée
     *
     * @param arrivalTime  l'heure d'arrivée voulue au format epoch (nb de secondes entre 1/1/1970 à minuit et l'heure voulue)
     * @param trajet       le trajet
     * @param ridingMethod un entier qui représente le moyen de transport (0 = voiture, 1 = vélo, 2 = à pied)
     * @return le temps de trajet en minutes (arrondi à l'entier supérieur)
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static long getTimeOfTravelWithTraffic(long arrivalTime, Trajet trajet, int ridingMethod) throws ExecutionException, InterruptedException {
        GoogleMapsAPI googleMapsAPI = new GoogleMapsAPI(arrivalTime, trajet, ridingMethod);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(googleMapsAPI);
        return future.get();
    }

    public static void showToast(Context context, String message, int displayTime) {
        Toast.makeText(context, message, displayTime).show();
    }

    /**
     * Converti les secondes au format HhMminSs
     *
     * @param time est le temps en seconde
     * @return un string formatté
     */
    public static String secondesToMinSecString(long time) {
        long heures = time / 3600;
        long minutes = (time % 3600) / 60;
        long secondes = ((time % 3600) % 60);

        StringBuilder sb = new StringBuilder();
        if (heures > 0) {
            sb.append(heures).append("h");
        }

        if (minutes > 0) {
            sb.append(minutes).append("min");
        }

        if (secondes >= 0) {
            sb.append(secondes).append("s");
        }

        return sb.toString();
    }

    /**
     * Converti les secondes au format HhMmin
     *
     * @param time est le temps en seconde
     * @return un string formatté
     */
    public static String secodesToMin(long time) {
        long heures = time / 3600;
        long minutes = (time % 3600) / 60;
        long secondes = ((time % 3600) % 60);

        StringBuilder sb = new StringBuilder();
        if (heures > 0) {
            sb.append(heures).append("h");
        }
        if (minutes >= 0) {
            sb.append(minutes).append("min");
        }
        return sb.toString();
    }


    /**
     * Méthode qui permet de cacher le clavier
     *
     * @param v est la vue dans laquelle la méthode est appellée
     */
    public static void hideSoftKeyboard(View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
