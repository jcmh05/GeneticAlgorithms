package meta_inicio;
import java.util.Random;
/**
 * @author Oscar
 */
public class Potencia extends Function{
    private Random random;
    private Double xmax = 1.0;
    private Double xmin = -1.0;
    private double optimo = 0;

    public Potencia() {
    }
    
    // de 0 a 4 son los valores que calculamos nosotros, de 5 a 9 son los que posee cada individuo

    @Override
    public double f(Double[] x) {
        double potencia = x[5]*(x[0]+(x[1]*x[5])+(x[2]*x[6])+(x[3]*x[7])+(x[4]*x[8]));
        return potencia;
    }

    @Override
    public Double getXMin() { return xmin; }

    @Override
    public Double getXMax() { return xmax; }

    @Override
    public double getOptimo() { return optimo; }

}
