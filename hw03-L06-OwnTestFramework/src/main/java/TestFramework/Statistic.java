package TestFramework;

public class Statistic {
    private int total;
    private int passed;
    private int failed;
    public Statistic(){
        total =0;
        passed=0;
        failed=0;
    }
    public int getTotal(){
        return total;
    }
    public int getPassed(){
        return total;
    }
    public void passed(){
        passed++;
        total++;
    }
    public void failed(){
        failed++;
        total++;
    }
    public void printStatistics(){
        System.out.println("totalTests:  "+  total);
        System.out.println("passedTests: "+  passed+" - "+Math.round((double)passed/total*100)+"%");
        System.out.println("failedTests: "+  failed+" - "+Math.round((double)failed/total*100)+"%");
    }
}
