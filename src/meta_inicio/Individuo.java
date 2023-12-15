package meta_inicio;

import java.util.ArrayList;

/**
 *
 * @author Jc & Oscar
 */
public class Individuo {
    private double valor;                   //
    private Double[] solucion;              //Array de valores xi
    private int generacion;                 //
    private boolean elite;                  //
    Algoritmos algo;
    AlgoritmosError algerror;
    
    
    private ConfiguradorDeConjuntoDeDatos configD;
    
    private boolean practico;

    public Individuo() {
    }
    
    
    public Individuo(Double[] solucion, int generacion, Algoritmos algo_,ConfiguradorDeConjuntoDeDatos cond,AlgoritmosError algerror) {
        this.solucion = solucion;
        this.generacion = generacion;
        algo = algo_;
        elite = false;
        configD = cond;
        practico = false;
        this.algerror = algerror;
        Objetivo(algo_);
    }

    public int getGeneracion() {
        return generacion;
    }
    
    public void setSolucion(Double[] sol){
        solucion = sol;
        Objetivo(algo);
    }

    public Double[] getSolucion() {
        return solucion;
    }

    public double getValor() {
        return valor;
    }

    public void Objetivo(Algoritmos algo){
        Function fun;
            
        switch (algo) {
            case Ackley:
                fun=new AlgAckley_Clase04_Grupo01();
                break;
            case Dixon_Price:
                fun=new AlgDixon_Price_Clase04_Grupo01();
                break;
            case Griewank:
                fun=new AlgGriewank_Clase04_Grupo01();
                break;
            case Michalewicz:
                fun=new AlgMichalewicz_Clase04_Grupo01();
                break;
            case Perm_function:
                fun= new AlgPerm_function_Clase04_Grupo01();
                break;
            case Rastringin:
                fun= new AlgRastringin_Clase04_Grupo01();
                break;
            case Rosenbrock:
                fun = new AlgRosenbrock_Clase04_Grupo01();
                break;
            case Rotated_Hype_Ellipsoid:
                fun = new AlgRotated_Hype_Ellipsoid_Clase04_Grupo01();
                break;
            case Schewefel:
                fun= new AlgSchewefel_Clase04_Grupo01();
                break;
            case Trid:
                fun= new AlgTrid_Clase04_Grupo01();
                break;
            case Potencia:
                practico = true;
                fun = new Potencia();
                break;
            default:
                throw new AssertionError();
        }
        
        if(practico){//Se ejecuta en caso de ser la función Potencia
            Double[] solucionesReales = new Double[configD.getDni().size()];
            for(int i=0; i<configD.getDni().size(); i++){
                Double[] datosEx = new Double[solucion.length+configD.getVariables()];
                for(int j=0; j<solucion.length; j++)
                    datosEx[j] = solucion[j];
                datosEx[5] =  configD.getDni().get(i);
                datosEx[6] =  configD.getT().get(i);
                datosEx[7] =  configD.getW().get(i);
                datosEx[8] =  configD.getSMR().get(i);
                solucionesReales[i] = fun.f(datosEx);
            }
            
            Double[] solEsperadas = new Double[configD.getDni().size()];
            for(int i=0; i<configD.getDni().size(); i++)
                solEsperadas[i] = configD.getP().get(i);
            
            FunctionError score = new MAPE();
            switch(algerror){
                case MAPE:
                    score = new MAPE();
                    break;
                case RMSE:
                    score = new RMSE();
                    break;
            }
            valor = score.compute(solEsperadas, solucionesReales);
        }else
            valor = fun.f(solucion);
    }

    public void setElite(boolean elite) {
        this.elite = elite;
    }
    
    public boolean getElite(){
        return elite;
    }
    
    
    
    
    
}
