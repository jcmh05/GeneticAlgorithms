package meta_inicio;


public abstract class FunctionError {
    
    public abstract double compute(Double[] real, Double[] estimacion);
    
}

enum AlgoritmosError{MAPE,RMSE};