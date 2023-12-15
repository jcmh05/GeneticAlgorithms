package meta_inicio;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jc & Oscar
 */
public class AlgEvolutivoDiferencial  implements Runnable{
    
    private int maxIteraciones;
    private int d;
    private Random random;
    private Double xMin;
    private Double xMax;
    private Algoritmos algo;
    private double OptimoGlobal;
    private StringBuilder log;
    CountDownLatch cdl;           
    //Parámetros del algoritmo evolutivo
    private int tamPoblacion;      //PARAMETRIZAR
    private int k;                  //Padres que entran en el torneo //PARAMETRIZAR
    private int evaluaciones;
    ConfiguradorDeConjuntoDeDatos archivoDeDatos;
    private AlgoritmosError algoer;
    boolean primeraVez = true;

    

    
    public AlgEvolutivoDiferencial(Double xMin_, Double xMax_, Long seed, int d_,Algoritmos algo_, double Optimo,CountDownLatch c_,int _maxIteraciones, int tamPoblacion, int k,ConfiguradorDeConjuntoDeDatos cdatos,AlgoritmosError algoer){
        maxIteraciones = _maxIteraciones;
        cdl = c_;
        log = new StringBuilder();
        d = d_;
        random = new Random(seed);
        xMax=xMax_;
        xMin=xMin_;
        algo=algo_;
        OptimoGlobal=Optimo;
        evaluaciones = 0;
        
        this.tamPoblacion = tamPoblacion;
        this.k = k;
        
        this.archivoDeDatos = cdatos;
        this.algoer = algoer;
        log.append("\n-----------------------------------------------\n");
        log.append("Semilla: " + seed);
        log.append("\nFuncion: " + algo + "\nOptimoGlobal: " + Optimo);
        if( algo == Algoritmos.Potencia){ log.append("\nEVALUACIÓN: " + algoer); }
        log.append("\nIntervalo: ["+xMin+" , "+xMax+"]");
        log.append("\n-----------------------------------------------\n\n");
        
    }

    @Override
    public void run() {
        double time_start = System.currentTimeMillis();
        int contador = 0;
        
        //ESTABLECEMOS PRIMERA GENERACIÓN
        log.append("Generamos población inicial...\n");
        ArrayList<Individuo> PoblacionActual = PoblacionInicial();
        visualiza(PoblacionActual);
        
        Individuo mejorSolucion = PoblacionActual.get(0);
        do{
            contador++;
            log.append("\n\n--------------Iteración nº" + contador +"----------------\n");

            //GENERAMOS NUEVA POBLACIÓN
            ArrayList<Individuo> generacionNueva = generarNuevaGeneracion(PoblacionActual,contador);
            
            //REEMPLAZAMIENTO
            if(primeraVez){
                log.append("realizamos el remplazo de la nueva generacion por la antigua-----------------------\n");
            }
            for(int i=0; i<PoblacionActual.size(); i++){
                if(evalua(generacionNueva.get(i).getValor(),PoblacionActual.get(i).getValor())){
                    PoblacionActual.set(i, generacionNueva.get(i));
                    if(primeraVez){
                        log.append("\n\n--------------Se sustituye al Padre Por el hijo en"+i+"----------------\n");
                    }
                }
            }
                
            primeraVez = false;
            mejorSolucion = visualiza(PoblacionActual); //Visualiza población y actualiza mejor individuo
            
        }while( evaluaciones < maxIteraciones && mejorSolucion.getValor() != OptimoGlobal);
        
        if( mejorSolucion.getValor() == OptimoGlobal)log.append("\n\n --------------------------------------- \n| ÓPTIMO ENCONTRADO EN LA GENERACIÓN " + contador + "|\n --------------------------------------");
        
        
        log.append("\nf(x)= " + mejorSolucion.getValor()); 
        double time_end = System.currentTimeMillis();
        double time = time_end - time_start;
        log.append("\nTiempo: " + (double) (time/1000)%60 );
        cdl.countDown();
    }
    
    private Individuo visualiza(ArrayList<Individuo> PoblacionActual){
        Individuo mejorSolucion = PoblacionActual.get(0);
        for(int i=0; i<tamPoblacion; i++){
            if( evalua(PoblacionActual.get(i).getValor(),mejorSolucion.getValor()) ){
                mejorSolucion = PoblacionActual.get(i);
            }
            log.append("\n->" + PoblacionActual.get(i).getValor());
        }
        log.append("\nLa mejor solucion ahora es " + mejorSolucion.getValor());
        return mejorSolucion;
    }

    
    private ArrayList<Individuo> generarNuevaGeneracion( ArrayList<Individuo> PoblacionActual, int contador){
        ArrayList<Individuo> nuevaGeneracion = new ArrayList<>();
        if(primeraVez){
            log.append("Se crea una nueva generacion, la generacion "+contador+"\n");
            log.append("----------------------------------------------------------------------------------------------------------------\n\n");
        }
         for(int i=0; i<PoblacionActual.size(); i++){
            if(primeraVez){
                log.append("-generamos 1 individuo para la nueva generacion \n");
                log.append("----------------------------------------------------------------------------------------------------------------\n");
            }
             boolean seRepitenLosPadres = true;
            ArrayList<Integer> posPadres = new ArrayList<>();
            posPadres.add(i);
            
            //Generamos el padre
            Individuo IndividuoPadre = PoblacionActual.get(i);
                        
            //Generamos el individuo objetivo
            Individuo IndividuoObjetivo = new Individuo();
            while(seRepitenLosPadres){
                IndividuoObjetivo= generarPadre(PoblacionActual);
                 ArrayList<Individuo> contrincantes = new ArrayList<>();
                if(IndividuoPadre != IndividuoObjetivo)
                    seRepitenLosPadres = false;  
            }
            
            for(int j=0; j<PoblacionActual.size(); j++)
                if(PoblacionActual.get(j)==IndividuoObjetivo)
                    posPadres.add(j);
            
            //Generamos los dos aleatorios
            Individuo IndividioAleatorio1 = new Individuo();
            Individuo IndividioAleatorio2 = new Individuo();
            seRepitenLosPadres = true;
            while(seRepitenLosPadres){
                IndividioAleatorio1 = PoblacionActual.get(random.nextInt((tamPoblacion-1)));
                IndividioAleatorio2 = PoblacionActual.get(random.nextInt((tamPoblacion-1)));
                if(IndividioAleatorio1 != IndividuoPadre && IndividioAleatorio1 != IndividuoObjetivo && IndividioAleatorio1 != IndividioAleatorio2)
                    if(IndividioAleatorio2 != IndividuoPadre && IndividioAleatorio2 != IndividuoObjetivo)
                        seRepitenLosPadres = false;
            }
            
             for(int j=0; j<PoblacionActual.size(); j++)
                if(PoblacionActual.get(j)==IndividuoObjetivo || PoblacionActual.get(j)==IndividioAleatorio1 || PoblacionActual.get(j)==IndividioAleatorio2)
                    posPadres.add(j);
            
            
            //Generamos individuo nuevo
            Individuo nuevo = generarIndividuoDeLaNuevaGeneracion(IndividuoPadre,IndividuoObjetivo,IndividioAleatorio1,IndividioAleatorio2, contador);
            nuevaGeneracion.add(nuevo);
            
            evaluaciones++;
            
            if(primeraVez){
            log.append(" El hijo "+nuevo.getValor()+ "el cual es el "+ i+" hijo nacido de su generacion ha nacido de los Padres "+posPadres.get(0)+" "+posPadres.get(1)+" "+posPadres.get(2)+" "+posPadres.get(3)+"\n");
            log.append("                el nuevo usuario tiene:\n");
            log.append("                    fitness: "+nuevo.getValor()+"\n");
            log.append("                    genotipo: ");
            for(int u=0; u<nuevo.getSolucion().length; u++)
                log.append(" "+nuevo.getSolucion()[u]+" ");
            log.append("\n");
            log.append("----------------------------------------------------------------------------------------------------------------\n");
            }
         }
        if(primeraVez){
            log.append("----------------------------------------------------------------------------------------------------------------\n\n");
        }
        return nuevaGeneracion;
     }
    
    
    
    
    private Individuo generarPadre(ArrayList<Individuo> PoblacionActual){
        ArrayList<Individuo> contrincantes = new ArrayList<>();
        for(int j=0; j<k; j++){
            contrincantes.add(PoblacionActual.get(random.nextInt((tamPoblacion-1))));
        }
        Individuo Mejor1 = contrincantes.get(0);
        for(int j=1; j<k; j++){
            if(evalua(contrincantes.get(j).getValor(),Mejor1.getValor())){
                Mejor1 = contrincantes.get(j);    
            }  
        }
        return Mejor1;
    }
      
    private Individuo generarIndividuoDeLaNuevaGeneracion(Individuo Padre,Individuo IndividuoObjetivo,Individuo IndividuoAleatorio1,Individuo IndividuoAleatorio2, int contador){
        Double[] solucion = new Double[d];
        Double[] padre = Padre.getSolucion();
        Double[] objetivo = IndividuoObjetivo.getSolucion();
        Double[] aleatorio1 = IndividuoAleatorio1.getSolucion();
        Double[] aleatorio2 = IndividuoAleatorio2.getSolucion();
        
        //creamos la solucion
        for(int i=0; i<d; i++){
            if((random.nextDouble()%1)<0.5){
                solucion[i] = padre[i]+((random.nextDouble()%1)*(aleatorio1[i]-aleatorio2[i]));
                if (solucion[i] > xMax) solucion[i] = xMax;
                if(primeraVez){
                    log.append("para el alelo "+i+" se ha recurido a la operacion ternaria \n");
                }
            }else{
                solucion[i] = objetivo[i];
                if(primeraVez){
                    log.append("para el alelo "+i+" se ha recurido al alelo del padre \n");
                }
            }
        }
            
        Individuo nuevo = new Individuo(solucion,contador,algo,archivoDeDatos,algoer);
        return nuevo;
    }
    
    //Devuelve true solo si el vecino 1 es mejor que el vecino2
    private Boolean evalua(double valor1, double valor2){
            return Math.abs(OptimoGlobal-valor1) < Math.abs(OptimoGlobal-valor2) ? true : false;
    }
    
    private ArrayList<Individuo> PoblacionInicial(){
        ArrayList<Individuo> PoblacionActual = new ArrayList<>();
        
        for(int i = 0; i<tamPoblacion; i++){
            Double[] solActual = solucionInicial();
            Individuo ind= new Individuo(solActual,1,algo, archivoDeDatos,algoer);
            PoblacionActual.add(ind);
        }
        
        return PoblacionActual;
    }
    
    private Double[] solucionInicial(){
        Double[] vector = new Double[d]; 
        for(int i=0; i<d; i++)
           vector[i] = random.nextDouble(xMax) + xMin;
        return vector;
    }

     
    public String getLog(){
        return log.toString();
    }
}

