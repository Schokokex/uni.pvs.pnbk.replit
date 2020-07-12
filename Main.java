import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class Main {
  public static void main(String[] args) {
    runTests();
  }
  public static void runTests() {
    System.out.println("\nRunning tests..");
    Result result = JUnitCore.runClasses(de.uulm.sp.pvs.pnbk.main.PNBKTests.class);
    for (Failure failure : result.getFailures()) {
      System.out.println("\t"+failure.toString());
    }
    if (result.wasSuccessful()) {
      System.out.println("\n✔️ Tests Passed!");
    } else {
      System.out.println("\nTests Failed.");
    }
  }
}