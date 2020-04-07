package fr.ul.miage.lucas;

public class MyUtil {
	
	public static long toMiliseconds(int hour, int min, int sec) {
		return (hour*3600+min*60+sec)*1000;
	}
}
