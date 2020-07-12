package de.uulm.sp.pvs.pnbk.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;
import de.uulm.sp.Zoo.animals.*;
import de.uulm.sp.Zoo.containers.*;

public class PNBKTests {

	/***************************
	 * combineFileContents
	 **********************************/

	private static void writeToFile(String file, String s) throws IOException {
		var path = Paths.get(file);

		Files.write(path, s.getBytes());
	}

	private static String readFromFile(String file) throws IOException {
		var path = Paths.get(file);

		return Files.readString(path);
	}

	private static void deleteFile(String file) throws IOException {
		var path = Paths.get(file);

		Files.deleteIfExists(path);
	}

	@Test
	public void testCombineFileContentsa() {
		try {
			deleteFile("resources/tests/input1.txt");
			deleteFile("resources/tests/input2.txt");
			writeToFile("resources/tests/input1.txt", "abc");
			writeToFile("resources/tests/input2.txt", "abc");
			Solutions.combineFileContents("resources/tests/output1.txt",
					"resources/tests/input1.txt", "resources/tests/input2.txt");
			assertEquals("", "aabbcc",
					readFromFile("resources/tests/output1.txt"));

			deleteFile("resources/tests/input1.txt");
			deleteFile("resources/tests/input2.txt");
			deleteFile("resources/tests/output1.txt");
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/***************************
	 * LightenUp
	 *******************************************/
	@Test
	public void testLighteningBasic() {

		try {
			assertEquals("", "lllll\nlllll\nllsll\nlllll\nlllll", Solutions
					.lightenUp("eeeee\neeeee\neesee\neeeee\neeeee").trim());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/***************************
	 * SocialDistancing
	 *******************************************/
	private static String removeWhiteSpace(String s) {
		return s.replaceAll(" ", "");
	}

	private static String removeWhiteSpaceAndLB(String s) {
		return s.replaceAll(" ", "").replaceAll("\n", "");
	}

	@Test
	public void testFlighta() {
		try {
			var solution = removeWhiteSpace(Solutions.socialDistancing(
					"Frankfurt", "Berlin", "Warschau", "Stuttgart", 5));
			assertTrue("", solution.contains(
					"(Frankfurt->München->Warschau,Berlin->Stuttgart)"));
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/***************************
	 * Overlaps
	 *******************************************/
	@Test
	public void testIntervalOverlapsEvenNumbers() {
		try {
			assertEquals("", removeWhiteSpace(
					"[2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94, 96, 98, 100]"),
					removeWhiteSpace(
							Solutions.overlaps("(1,100,1);(0,100,2)")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************
	 * Json1
	 *******************************************/
	@Test
	public void testPenguinJson() {
		var pengu = new Penguin();
		pengu.die();
		pengu.fly();

		try (var oOut = new ObjectOutputStream(new FileOutputStream("in.data"));
				var oIn = new ObjectInputStream(new FileInputStream("in.data"));
				var dIn = new PipedInputStream();
				var dOut = new PipedOutputStream(dIn)) {
			oOut.writeObject(pengu);
			oOut.flush();
			oOut.close();

			Solutions.jsonifyAnimal(oIn, dOut);

			dOut.flush();
			dOut.close();
			var solution = new String(dIn.readAllBytes());

			assertEquals("",
					"{\"inTheAir\":true,\"numberOfLegs\":2,\"isAlive\":false}",
					removeWhiteSpaceAndLB(solution));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	/***************************
	 * Json2
	 *******************************************/
	@Test
	public void testPenguinFromJson() {

		try (var oOut = new ObjectOutputStream(new FileOutputStream("in.data"));
				var oIn = new ObjectInputStream(new FileInputStream("in.data"));
				var dIn = new PipedInputStream();
				var dOut = new PipedOutputStream(dIn)) {

			dOut.write(
					"{\"inTheAir\":true,\"numberOfLegs\":2,\"isAlive\":false}"
							.getBytes());
			dOut.flush();
			dOut.close();

			Solutions.deJsonifyPenguin(dIn, oOut);

			oOut.flush();
			oOut.close();
			var pengu = (Penguin) oIn.readObject();

			var testPengu = new Penguin();
			testPengu.die();
			testPengu.fly();

			assertEquals("", testPengu, pengu);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/***************************
	 * Json3
	 *******************************************/
	@Test
	public void testZooJson() {
		var zoo = new Zoo();
		var enc = new Enclosure<Animal>(3);
		var tiger = new Tiger();
		tiger.die();
		var tiger2 = new Tiger();
		enc.add(tiger);
		enc.add(tiger2);
		zoo.addEnclosure(enc);
		var enc2 = new Enclosure<Animal>(12333);
		var pengu = new Penguin();
		pengu.fly();
		var pengu2 = new Penguin();
		pengu2.die();
		enc2.add(pengu);
		enc2.add(pengu2);
		zoo.addEnclosure(enc2);
		zoo.addVisitor(p -> true);
		zoo.addVisitor(p -> false);
		zoo.removeVisitor();

		try (var oOut = new ObjectOutputStream(new FileOutputStream("in.data"));
				var oIn = new ObjectInputStream(new FileInputStream("in.data"));
				var dIn = new PipedInputStream();
				var dOut = new PipedOutputStream(dIn)) {
			oOut.writeObject(zoo);
			oOut.flush();
			oOut.close();

			Solutions.jsonifyZoo(oIn, dOut);

			dOut.flush();
			dOut.close();
			var solution = new String(dIn.readAllBytes());

			assertEquals("",
					"{\"animalEnclosures\":[{\"maxSize\":3,\"contained\":[{\"numberOfLegs\":4,\"isAlive\":false},{\"numberOfLegs\":4,\"isAlive\":true}]},{\"maxSize\":12333,\"contained\":[{\"inTheAir\":true,\"numberOfLegs\":2,\"isAlive\":true},{\"inTheAir\":false,\"numberOfLegs\":2,\"isAlive\":false}]}],\"allTimeVisitors\":2,\"currentVisitors\":1,\"rating\":0.5}",
					removeWhiteSpaceAndLB(solution));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	/***************************
	 * Json4
	 *******************************************/
	@Test
	public void testZooFromJson() {
		var zoo = new Zoo();
		var enc = new Enclosure<Animal>(3);
		var tiger = new Tiger();
		tiger.die();
		var tiger2 = new Tiger();
		enc.add(tiger);
		enc.add(tiger2);
		zoo.addEnclosure(enc);
		var enc2 = new Enclosure<Animal>(12333);
		var pengu = new Penguin();
		pengu.fly();
		var pengu2 = new Penguin();
		pengu2.die();
		enc2.add(pengu);
		enc2.add(pengu2);
		zoo.addEnclosure(enc2);
		zoo.addVisitor(p -> true);
		zoo.addVisitor(p -> false);
		zoo.removeVisitor();

		try (var oOut = new ObjectOutputStream(new FileOutputStream("in.data"));
				var oIn = new ObjectInputStream(new FileInputStream("in.data"));
				var dIn = new PipedInputStream();
				var dOut = new PipedOutputStream(dIn)) {

			dOut.write(
					"{\"animalEnclosures\":[{\"maxSize\":3,\"contained\":[{\"numberOfLegs\":4,\"isAlive\":false},{\"numberOfLegs\":4,\"isAlive\":true}]},{\"maxSize\":12333,\"contained\":[{\"inTheAir\":true,\"numberOfLegs\":2,\"isAlive\":true},{\"inTheAir\":false,\"numberOfLegs\":2,\"isAlive\":false}]}],\"allTimeVisitors\":2,\"currentVisitors\":1,\"rating\":0.5}"
							.getBytes());
			dOut.flush();
			dOut.close();

			Solutions.deJsonifyZoo(dIn, oOut);

			oOut.flush();
			oOut.close();
			var resultZoo = (Zoo) oIn.readObject();

			assertEquals("", zoo, resultZoo);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
