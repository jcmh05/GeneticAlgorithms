package meta_inicio;

/**
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-100 100]
 * OptimoGlobal = 0
 */
public class AlgDixon_Price_Clase04_Grupo01 extends Function{

    private Double xmax = 100.0;
    private Double xmin = -100.0;
    private double optimo = 0;
    
    @Override
    public double f(Double[] x) {
        
        double sum1 = 0;
        
        for(int i=1; i<d; i++){
            sum1 += i * ( (2*Math.pow(x[i], 2)) - x[i-1] );
        }
        
        return Math.pow(x[0] - 1, 2) + sum1;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
}
