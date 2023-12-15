package meta_inicio;
/**
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-1000 1000]
 * OptimoGlobal = 0
 */
public class AlgGriewank_Clase04_Grupo01 extends Function{

    private Double xmax = 1000.0;
    private Double xmin = -1000.0;
    private double optimo = 0;
    
    @Override
    public double f(Double[] x) {
        double sum = 0;
        double prod = 1;
        
        for(int i=0; i<d; i++){
            sum += (Math.pow(x[i],2)/4000);
            double dato=i;
            prod *= Math.cos(x[i]/Math.sqrt(dato+1));
        }        
        
        return sum - prod +1;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
    
}
