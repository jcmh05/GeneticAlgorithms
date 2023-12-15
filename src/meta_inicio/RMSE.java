package meta_inicio;
/**
 * @author Jc & Oscar
 * 
 */
public class RMSE extends FunctionError{
    
    private int a;
    private double b;
    private double c;

    public RMSE() {}

    @Override
    public double compute(Double[] real, Double[] estimacion) {
        int N = real.length;
        double score;
        double sum = 0.0;
        
        for(int i=0; i < N; i++){
            sum += Math.pow( real[i] - estimacion[i], 2);
        }
        
        score = Math.sqrt( 1.0 / N * sum);
        return score;
    }
    
}