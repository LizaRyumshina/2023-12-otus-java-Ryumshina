import TestFramework.ProcessingAnnotation;
import TestFramework.Statistic;

public class RunTests {
    public static void main(String[] args) {
        System.out.println("--------------------------------");
        System.out.println("Processing started");
        System.out.println("--------------------------------");
        Statistic statistic = new Statistic();
        ProcessingAnnotation.Run("BaseObjects.TF_CardManager", statistic);
        ProcessingAnnotation.Run("BaseObjects.TF_Customer", statistic);
        ProcessingAnnotation.Run("BaseObjects.TF_Card", statistic);
        System.out.println("--------------------------------");
        statistic.PrintStatistics();
        System.out.println("--------------------------------");
        System.out.println("Processing finished");
        System.out.println("--------------------------------");
    }
}
