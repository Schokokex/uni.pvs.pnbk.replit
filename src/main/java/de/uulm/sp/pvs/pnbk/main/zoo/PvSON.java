package de.uulm.sp.pvs.pnbk.main.zoo;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.uulm.sp.Zoo.animals.Animal;
import de.uulm.sp.Zoo.animals.CanFly;
import de.uulm.sp.Zoo.animals.Penguin;
import de.uulm.sp.Zoo.containers.Enclosure;
import de.uulm.sp.Zoo.containers.Zoo;

public class PvSON {
	

	public PvSON(String reason) {
		printPvSONDisclaimer(reason);
	}

	public static void printPvSONDisclaimer(String obj) {
		System.err.println(Thread.currentThread().getStackTrace()[3]);
		System.out.printf(
				"%s wird als PvS-ObjectNotation-String zurückgegeben. PvSON-Strings können als JSON-String verwendet werden.\nUnterschied: Bei JSON-Strings sind Name/Wert-Paare eines Objekts ungeordnet (https://www.json.org/json-de.html).\n",
				obj);
	}

	public String animalListToPvSON(List<Animal> list) {
		if (null == list) {
			return "null";
		}
		return list.stream().map(animal -> animalToPvSON(animal)).collect(Collectors.joining(",", "[", "]"));
	}

	public String animalToPvSON(Animal animal) {
		if (animal instanceof CanFly) {
			final var bird = (Penguin) animal;
			return "{\"inTheAir\":%b,\"numberOfLegs\":%d,\"isAlive\":%b}".formatted(bird.inTheAir(),
					bird.getNumberOfLegs(), bird.isAlive());
		} else {
			return "{\"numberOfLegs\":%d,\"isAlive\":%b}".formatted(animal.getNumberOfLegs(), animal.isAlive());
		}
	}

	public String enclosureListToPvSON(List<Enclosure<Animal>> list) {
		if (null == list) {
			return "null";
		}
		return list.stream().map(enclosure -> enclosureToPvSON(enclosure)).collect(Collectors.joining(",", "[", "]"));
	}

	public String enclosureToPvSON(Enclosure<Animal> enclosure) {
		return "{\"maxSize\":%d,\"contained\":%s}".formatted(enclosure.getMaxSize(),
				animalListToPvSON(enclosure.findAll(a -> true)));
	}

	public String zooToPvSON(Zoo zoo) {
		final var enclosureList = new LinkedList<Enclosure<Animal>>();
		{
			var i = 0;
			for (var enclosure = zoo.getAnimalEnclosure(0); null != enclosure;) {
				enclosureList.add(enclosure);
				try {
					enclosure = zoo.getAnimalEnclosure(++i);
				} catch (IndexOutOfBoundsException e) {
					enclosure = null;
				}
			}
		}
		Double rating = zoo.getRating();
		return "{\"animalEnclosures\":%s,\"allTimeVisitors\":%d,\"currentVisitors\":%d,\"rating\":%s}".formatted(
				enclosureListToPvSON(enclosureList), zoo.getAllTimeVisitors(), zoo.getCurrentVisitors(),
				rating.toString());
	}

}
