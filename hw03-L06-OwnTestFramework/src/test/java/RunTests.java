import TestFramework.ProcessingAnnotation;
import TestFramework.Statistic;

public class RunTests {
    public static void main(String[] args) {
        System.out.println("--------------------------------");
        System.out.println("Processing started");
        System.out.println("--------------------------------");
        Statistic statistic = new Statistic();
        ProcessingAnnotation.run("BaseObjects.TF_CardManager", statistic);
        ProcessingAnnotation.run("BaseObjects.TF_Customer", statistic);
        ProcessingAnnotation.run("BaseObjects.TF_Card", statistic);
        System.out.println("--------------------------------");
        statistic.printStatistics();
        System.out.println("--------------------------------");
        System.out.println("Processing finished");
        System.out.println("--------------------------------");
    }
}
