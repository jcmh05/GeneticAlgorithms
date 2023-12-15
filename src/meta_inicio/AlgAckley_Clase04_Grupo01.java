package meta_inicio;
/**
 * @author Jc & Oscar
 * OptimoGlobal = 0
 * Busqueda en intervalo xi -> [-32768 32768]
 */
public class AlgAckley_Clase04_Grupo01 extends Function{
    
    private int a;
    private double b;
    private double c;
    private Double xmax = 32768.0;
    private Double xmin = -32768.0;
    private double optimo = 0;

    public AlgAckley_Clase04_Grupo01() {
        a = 20;
        b = 0.2;
        c = Math.PI*2;
    }

    @Override
    public double f(Double[] x) {
        double sum1 = 0;
        double sum2 = 0;
        
        for(int i=0; i<d; i++){
            sum1 += Math.pow(x[i],2);
            sum2 += Math.cos(c * x[i]);
        }
        
        
        double dat1=Math.exp(-b * Math.sqrt((1.0/d)*sum1));
        double dat2=Math.exp((1.0/d)*sum2);
        double sol = ((-a * dat1) - dat2 + Math.exp(1.0))+a;
        return sol;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }
    
    
    
}
