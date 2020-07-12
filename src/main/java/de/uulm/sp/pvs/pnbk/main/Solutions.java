package de.uulm.sp.pvs.pnbk.main.PNBKTests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uulm.sp.Zoo.animals.*;
import de.uulm.sp.Zoo.containers.*;
import de.uulm.sp.pvs.pnbk.main.airports.Graph;
import de.uulm.sp.pvs.pnbk.main.airports.Route;
import de.uulm.sp.pvs.pnbk.main.candles.CandleBoard;
import de.uulm.sp.pvs.pnbk.main.intervals.Interval;
import de.uulm.sp.pvs.pnbk.main.zoo.PvSON;

public class Solutions {
	// @formatter:off

	/**
	 * 3p Writes the contents from all files at the passed paths to the output file
	 * (create if not existent) by bytewise alternating between the files
	 * 
	 * sample: f1: abc; f2: abc -> outputFile: aabbcc f1: abcd; f2: e; f3: f ->
	 * outputfile: aefbcd
	 * 
	 * @param outputPath path of file to write to
	 * @param paths      paths of files to read from
	 * @throws Exception
	 */
	public static void combineFileContents(String outputPath, String... paths) throws Exception {
		final var outFile = new FileOutputStream(outputPath);
		final var amntInpFiles = paths.length;
		final var inFileStreams = new FileInputStream[amntInpFiles];
		for (int i = amntInpFiles - 1; i >= 0; i--) {
			inFileStreams[i] = new FileInputStream(paths[i]);
		}

		for (boolean allDone = true;; allDone = true) {
			for (int i = 0; i < amntInpFiles; i++) {
				final var a = inFileStreams[i].readNBytes(1);
				outFile.write(a);
				if (a.length > 0) {
					allDone = false;
				} else {
					inFileStreams[i].close();
				}
			}
			if (allDone) {
				break;
			}
		}
		outFile.close();
	}

	/**
	 * find overlaps (in sorted order) of n intervals with variable stepWidth input
	 * format: (start,end,stepWidth);...
	 * 
	 * output format: [overlap1, ...] whitespace is ignored in the output!!
	 * 
	 * sample: "(1,100,1);(0,100,2)" -> [all even numbers]
	 * "(44,44,1);(40,50,2);(1,1,1)" -> "[44]"
	 * 
	 * @param s in input format
	 * @return String in output format
	 * @throws Exception
	 */
	public static String overlaps(String s) throws Exception {
		/*
		 * create Interval Objects that are iterable. Iterate through Intervals. Use a
		 * duplicate list and alreadyUsed list, save duplicates and return as String.
		 * 
		 */
		final var intervalStrings = s.split(";");
		final var alreadyUsed = new TreeSet<Integer>();
		final SortedSet<Integer> duplicates = new TreeSet<Integer>();
		for (var i = 0; i < intervalStrings.length; i++) {
			final var interval = new Interval(intervalStrings[i]);
			for (final Integer step : interval) {
				if (alreadyUsed.contains(step)) {
					duplicates.add(step);
				} else {
					alreadyUsed.add(step);
				}
			}
		}
		return duplicates.toString();
	}

	/**
	 * highlight all tiles that are reached by at least one light source input
	 * format: eeee eswe eeee e = empty tile s = light source w = wall l = lit tile
	 * 
	 * light source can reach a total of two tiles away in each direction (as a
	 * square): eeeeeee ellllle ellllle ellslle ellllle ellllle eeeeeee
	 * 
	 * walls block tiles behind them eewsll
	 * 
	 * eelll ewlll llsll lllwe lllee
	 * 
	 * sample: "eesee" -> "llsll"
	 * 
	 * "eeeee\newewe\neesee\newewe\neeeee" -> "eelee\newlwe\nllsll\newlwe\neelee"
	 * 
	 * @param s in input format
	 * @return String in output format
	 * @throws Exception
	 */
	public static String lightenUp(String s) throws Exception {
		final var board = new CandleBoard(s);
		return board.toString();
	}

	/**
	 * find all possible pairs of flight paths for two passengers that do not cross
	 * at all with a maximum of n hops between start and end a hop is defined as A-B
	 * so: A-B + B-C are two hops
	 * 
	 * output format: [([(Start : Between),(Between : End)],[(Start : End)]),...]
	 * whitespace is ignored in the output!!
	 * 
	 * sample: "Frankfurt,Berlin,Warschau,Stuttgart,5" ->
	 * "(Frankfurt->München->Warschau,Berlin->Stuttgart)"
	 * "Frankfurt,Berlin,Brüssel,Stuttgart,2" ->
	 * "(Frankfurt->Wien->Brüssel,Berlin->Stuttgart)
	 * (Frankfurt->Paris->Brüssel,Berlin->Stuttgart)
	 * (Frankfurt->Wien->Brüssel,Berlin->München->Stuttgart)
	 * (Frankfurt->Paris->Brüssel,Berlin->München->Stuttgart)"
	 * 
	 * @param p1Start start airport for passenger 1
	 * @param p2Start start airport for passenger 2
	 * @param p1End   goal airport for passenger 1
	 * @param p2End   goal airport for passenger 2
	 * @param maxHops max number of flights
	 * @return String in output format
	 * @throws Exception
	 */
	public static String socialDistancing(String p1Start, String p2Start, String p1End, String p2End, int maxHops)
			throws Exception {
		final var graphAgain = new Graph<String>("Rom");
		
		graphAgain.addDirections("Rom",new String[] {"Wien"});
		graphAgain.addDirections("Wien",new String[] {"Brüssel","Frankfurt"});
		graphAgain.addDirections("Brüssel",new String[] {"Frankfurt","Wien"});
		graphAgain.addDirections("Paris",new String[] {"Brüssel"});
		graphAgain.addDirections("Frankfurt",new String[] {"Paris", "Wien", "Stuttgart", "München", "Berlin", "London"});
		graphAgain.addDirections("Berlin",new String[] {"Frankfurt", "Stuttgart", "München", "Warschau"});
		graphAgain.addDirections("Warschau",new String[] {"Berlin", "Moskau"});
		graphAgain.addDirections("Moskau",new String[] {"München"});
		graphAgain.addDirections("München",new String[] {"Warschau", "Berlin", "Frankfurt", "Stuttgart"});
		graphAgain.addDirections("Stuttgart",new String[] {"München", "Berlin", "Frankfurt", "Rom"});

		final var routes1 = graphAgain.getAllRoutes(p1Start, p1End, maxHops);
		final var routes2 = graphAgain.getAllRoutes(p2Start, p2End, maxHops);
		
		var out = "";
		
		for (Route<String> route1 : routes1) {
			for (Route<String> route2 : routes2) {
				
				if (!route1.intersects(route2)) {
					out += "(";
					out += String.join("->", route1);
					out += ", ";
					out += String.join("->", route2);
					out +=")";
				}
				
			}
		}
		return out;
	}

	/**
	 * takes an Animal object from the input stream, converts it into json format
	 * and outputs the string using the outputStream whitespace in the output is
	 * ignored!
	 * 
	 * @param in  Stream transferring *exactly* one animal object
	 * @param out Stream to write Jsonified animal to
	 * @throws Exception
	 */
	public static void jsonifyAnimal(ObjectInputStream in, OutputStream out) throws Exception {
		final var inObject = in.readObject();
		if (!(inObject instanceof Animal)) {
			throw new IllegalArgumentException();
		} else {
			final var animal = (Animal) inObject;
			out.write(new PvSON("Animal").animalToPvSON(animal).getBytes());
		}
	}

	/**
	 * reads String from input stream and converts it into a penguin object you can
	 * assume that all byte transfered by the input stream together form the string
	 * to use the input will not contain whitespace or line breaks!!
	 * 
	 * @param in  stream transferring the input json String
	 * @param out stream to write Penguin object to
	 * @throws Exception
	 */
	public static void deJsonifyPenguin(InputStream in, ObjectOutputStream out) throws Exception {
		final var inString = new String(in.readAllBytes());
		final var penguin = new Gson().fromJson(inString, Penguin.class);
		out.writeObject(penguin);
	}

	/**
	 * takes a zoo object from the input stream, converts it into json format and
	 * outputs the string using the outputStream whitespace in the output is
	 * ignored!
	 * 
	 * @param in  Stream transferring *exactly* one zoo object
	 * @param out Stream to write Jsonified zoo to
	 * @throws Exception
	 */
	public static void jsonifyZoo(ObjectInputStream in, OutputStream out) throws Exception {
		final var inObject = in.readObject();
		if (!(inObject instanceof Zoo)) {
			throw new IllegalArgumentException();
		} else {
			final var zoo = (Zoo) inObject;
			out.write(new PvSON("Zoo").zooToPvSON(zoo).getBytes());
		}
	}

	/**
	 * reads String from input stream and converts it into a zoo object the input
	 * will not contain whitespace or line breaks!! you can assume that all byte
	 * transfered by the input stream together form the string to use
	 * 
	 * @param in  stream transferring the input json String
	 * @param out stream to write Zoo object to
	 * @throws Exception
	 */
	public static void deJsonifyZoo(InputStream in, ObjectOutputStream out) throws Exception {
		final var gson = new GsonBuilder().registerTypeAdapter(Animal.class, new AnimalDeserializer()).create();
		final var inString = new String(in.readAllBytes());
		final var zoo = gson.fromJson(inString, Zoo.class);
		out.writeObject(zoo);
	}

}
