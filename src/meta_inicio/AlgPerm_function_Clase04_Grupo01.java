package meta_inicio;

/**
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-100 100]
 * OptimoGlobal=0
 */
public class AlgPerm_function_Clase04_Grupo01 extends Function{

    
    private Double xmax = 100.0;
    private Double xmin = -100.0;
    private double optimo = 0;
    private int b;

    public AlgPerm_function_Clase04_Grupo01() {
        b = 10;
    }
  
    @Override
    public double f(Double[] x) {
        
        double sum1 = 0;
        double sum2 = 0;
        
        for(int i=0; i<d; i++){
            
           sum2 = 0;
           
           for(int j=0; j<d; j++){
                sum2 += (j + b) * ( Math.pow(x[j], i) - (1.0/Math.pow(j+1, i)) );
            }
           sum1 += Math.pow(sum2, 2);
        }
        
        return sum1;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
    
}
