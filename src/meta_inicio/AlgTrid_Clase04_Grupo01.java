package meta_inicio;

/**
 *
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-1000 1000]
 * OptimoGlobal = -200
 */
public class AlgTrid_Clase04_Grupo01 extends Function{
    
    private Double xmax = 1000.0;
    private Double xmin = -1000.0;
    private double optimo = 0;
    
    @Override
    public double f(Double[] x) {
        double sum1 = 0;
        double sum2 = 0;
        
        for(int i=0; i<d; i++){
            sum1 += Math.pow(( x[i] - 1 ), 2);
            
            sum2 += i>0 ? x[i]*x[i-1] : 0;
        }

        return sum1 - sum2;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
}
