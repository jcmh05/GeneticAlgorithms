package meta_inicio;

/**
 * @author Jc & Oscar
 * Busqueda en intervalo xi -> [-5000 5000]
 * OptimoGlobal=0
 */
public class AlgSchewefel_Clase04_Grupo01 extends Function{
    
    private double num;
    private Double xmax = 5000.0;
    private Double xmin = -5000.0;
    private double optimo = 0;

    public AlgSchewefel_Clase04_Grupo01() {
        num = 418.9829;
    }
    
    
    
    @Override
    public double f(Double[] x) {
        
        double sum= 0;
        
        for(int i=0; i<d; i++){
            sum += x[i] * Math.sin(Math.sqrt(Math.abs(x[i])));
        }
        
        return (num * d) - sum;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
}
