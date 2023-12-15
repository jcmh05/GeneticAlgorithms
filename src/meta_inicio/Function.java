package meta_inicio;

/**
 * @author Jc & Oscar
 */
public abstract class Function {
        int d = 10;

	public abstract double f(Double[] x);
        
        public abstract Double getXMin();
        public abstract Double getXMax();
        public abstract double getOptimo();
}


enum Algoritmos{Ackley, Dixon_Price, Griewank, Michalewicz, Perm_function,Rastringin,Rosenbrock,Rotated_Hype_Ellipsoid,Schewefel,Trid,Potencia};

