package fr.ul.miage.lucas;

/**
 * Classe contenant des méthodes utiles dans d'autres classes de l'application
 * 
 * @author lucas
 *
 */
public class MyUtil {
	
	/**
	 * Convertit une durée en milisecondes
	 * @param hour Heures
	 * @param min Minutes
	 * @param sec Secondes
	 * @return La même durée en milisecondes 
	 */
	public static long toMiliseconds(int hour, int min, int sec) {
		return (hour*3600+min*60+sec)*1000;
	}
	
	/**
	 * Convertit un temps en secondes en un temps en heures, minutes, secondes 
	 * @param x
	 * @return
	 */
	public static int[] convertSeconds(int x) {
		int[] tab = {0,0,0};
		tab[0] = x/3600;
		x = x%3600;
		tab[1] = x/60;
		x = x%60;
		tab[2] = x;
		return tab;
	}
}
