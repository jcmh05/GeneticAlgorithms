package meta_inicio;
/**
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-51.12 51.12]
 * OptimoGlobal=0
 */
public class AlgRastringin_Clase04_Grupo01 extends Function{

    private Double xmax = 51.12;
    private Double xmin = -51.12;
    private double optimo = 0;
    
    @Override
    public double f(Double[] x) {
        
        double sum= 0;
        
        for(int i=0; i<d; i++){
            sum += Math.pow(x[i],2) - ( 10 * Math.cos(2* Math.PI * x[i]));
        }
        
        return (10*d) + sum;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
    
}
