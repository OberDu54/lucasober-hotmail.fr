package fr.ul.miage.lucas;

public class MyUtil {
	
	public static long toMiliseconds(int hour, int min, int sec) {
		return (hour*3600+min*60+sec)*1000;
	}
	
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
