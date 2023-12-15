package meta_inicio;
/**
 * @author Jc & Oscar
 * 
 */
public class MAPE extends FunctionError{
    
    private int a;
    private double b;
    private double c;

    public MAPE() {}

    @Override
    public double compute(Double[] real, Double[] estimacion) {
        int N = real.length;
        double score;
        double sum = 0.0;
        double num = 0.0;
        
        for(int i=0; i<N; i++){
            if (real[i] != 0){
                sum = sum + Math.abs((real[i] - estimacion[i]) / real[i]);
                num = num+1;
            }
        }
        score = sum / num ;
        return score;
    }
    
}