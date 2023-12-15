package meta_inicio;

/**
 * @author Jc & Oscar
 * Intervalor xi -> [0, 2Pi]
 * OptimoGlobal = -9.66015
 */
public class AlgMichalewicz_Clase04_Grupo01 extends Function{
    private double m;
    private Double xmax = 2*Math.PI;
    private Double xmin = 0.0;
    private double optimo = -9.66015;
    
    
    public AlgMichalewicz_Clase04_Grupo01() {
        m = 10;
    }
 
    
    @Override
    public double f(Double[] x) {     
        double sum = 0;

        for(int i=0; i<d; i++){
            sum+= Math.sin(x[i]) * Math.pow(Math.sin(( i * Math.pow(x[i], 2) / Math.PI )), 2*m);
        }
        return -sum;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
    
}
