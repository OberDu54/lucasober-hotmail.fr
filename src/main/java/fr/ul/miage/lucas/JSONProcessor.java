package fr.ul.miage.lucas;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.ul.miage.meteo.json.Result;

public class JSONProcessor {
	/**
	 * écrire un objet Student
	 * 
	 * @param student
	 * @param filename
	 * @throws IOException
	 */
	public static void serialize(Result result, String filename) throws IOException {
		Writer out = new FileWriter(filename);
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		out.write(gson.toJson(result));
		out.close();
	}

	
	/**
	 * Lit un objet meteo
	 * 
	 * @param json une chaine de caractères au format json
	 * @return Un objet Result exploitable
	 */
	public static Result simpleDeserialize(String json) {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Result res = gson.fromJson(json, Result.class);
		return res;
	}
}
