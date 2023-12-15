package meta_inicio;
/**
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-5 10]
 * OptimoGlobal=0
 */
public class AlgRosenbrock_Clase04_Grupo01 extends Function{
    
    private Double xmax = 10.0;
    private Double xmin = -5.0;
    private double optimo = 0;
    
     @Override
    public double f(Double[] x) {
        
        double sum=0;
        
        for(int i=0; i<(d-1); i++){
            sum+=  (100*Math.pow((x[i+1] - Math.pow(x[i], 2)), 2)) + (Math.pow(x[i] -1, 2));
            
        }
        return sum;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
}
