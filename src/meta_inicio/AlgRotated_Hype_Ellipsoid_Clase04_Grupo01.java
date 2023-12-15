package meta_inicio;

/**
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-65536 65536]
 * OptimoGlobal = 0
 */
public class AlgRotated_Hype_Ellipsoid_Clase04_Grupo01 extends Function{

    private Double xmax = 65536.0;
    private Double xmin = -65536.0;
    private double optimo = 0;
    
    @Override
    public double f(Double[] x) {
        double sum1 = 0;
        double sum2;
        
        for(int i=0; i<d; i++){
            sum2=0;
            for(int j=0; j<i; j++){
               sum2 += Math.pow(x[j], 2);
            }
            sum1+= sum2;
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
