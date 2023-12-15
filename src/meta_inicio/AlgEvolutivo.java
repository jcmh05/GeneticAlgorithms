package meta_inicio;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author Jc & Oscar
 */
public class AlgEvolutivo  implements Runnable{
    
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
    int tamPoblacion;       //PARAMETRIZAR
    int k;                  //Padres que entran en el torneo //PARAMETRIZAR
    int kElite;             //valores que lucharan para meter al elite en el podio de la nueva generacion
    boolean blx_alfa;       //FALSE para MEDIA - TRUE para BLX_ALFA
    double alfa;            //Valor de alfa para blx_alfa
    int evaluaciones;       //Contador para el número de evaluaciones
    ConfiguradorDeConjuntoDeDatos archivoDeDatos;
    private AlgoritmosError algoer;
    boolean primeraVez = true;

    
    public AlgEvolutivo(double xMin_, double xMax_, Long seed, int d_,Algoritmos algo_, double Optimo,boolean blx_,CountDownLatch c_,int _maxIteraciones, int tamPoblacion, int k,int kElite, double alfa, ConfiguradorDeConjuntoDeDatos cdatos,AlgoritmosError algoer){
        maxIteraciones = _maxIteraciones;
        cdl = c_;
        log = new StringBuilder();
        d = d_;
        random = new Random(seed);
        xMax=xMax_;
        xMin=xMin_;
        algo=algo_;
        OptimoGlobal=Optimo;
        blx_alfa = blx_;
        evaluaciones = 0;
        this.tamPoblacion = tamPoblacion;
        this.k =k;
        this.kElite = kElite;
        this.alfa = alfa;
        this.archivoDeDatos = cdatos;
        this.algoer = algoer;
        
        log.append("\n-----------------------------------------------\n");
        log.append("Semilla: " + seed);
        log.append("\nFuncion: " + algo + "\nOptimoGlobal: " + Optimo);
        log.append("\nIntervalo: ["+xMin+" , "+xMax+"]");
        if(blx_) log.append("\nAlgoritmo: Evolutivo con cruce BLX-ALFA");
        else     log.append("\nAlgoritmo: Evolutivo con cruce BLX-Media"); 
        log.append("\n-----------------------------------------------\n\n");
        
    }


    @Override
    public void run() {
        double time_start = System.currentTimeMillis();
        int contador = 0;
        
        //ESTABLECEMOS PRIMERA GENERACIÓN
        log.append("Generamos población inicial...\n");
        ArrayList<Individuo> PoblacionActual = PoblacionInicial();     
        buscarElite(PoblacionActual);
        Individuo mejorSolucion = PoblacionActual.get(0);
        do{
            contador++;
            log.append("\n\n--------------Iteración nº" + contador + "Evaluaciones: "+ evaluaciones +"----------------\n");

            //Generar Nueva Poblacion
            ArrayList<Individuo> generacionNueva =  generarNuevaGeneracion(PoblacionActual,contador);
            buscarElite(generacionNueva);
             
            //remplazo
            PoblacionActual = remplazarGeneracion(PoblacionActual, generacionNueva);
        
                
        mejorSolucion = visualiza(PoblacionActual); //Visualiza población y actualiza mejor individuo
        primeraVez = false;
                
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
                log.append("\n<" + PoblacionActual.get(i).getValor());
                if( PoblacionActual.get(i).getElite() ){
                    log.append("  <---  ELITE (generacion " + PoblacionActual.get(i).getGeneracion() + ")");
                    mejorSolucion = PoblacionActual.get(i);
                }
            }
        
        return mejorSolucion;
    }
        
    private ArrayList<Individuo> generarNuevaGeneracion( ArrayList<Individuo> PoblacionActual, int contador){
        if(primeraVez){
        log.append("Se crea una nueva generacion, la generacion "+contador+"\n");
        log.append("----------------------------------------------------------------------------------------------------------------\n\n");
        }
        ArrayList<Individuo> nuevaGeneracion = new ArrayList<>();
        //Lanzamos el torneo
        for(int i=0; i<PoblacionActual.size(); i++){
            Boolean evaluado = false;
            //Generamos los padres
            ArrayList<Integer> padres = generarPadres(PoblacionActual);
           if(primeraVez){
            log.append("-generamos 1 individuo para la nueva generacion \n");
            log.append("----------------------------------------------------------------------------------------------------------------\n");
           }
            //Generamos individuo
            Individuo nuevo = new Individuo();
            if( (random.nextDouble()%1) <= 0.7){
                nuevo = cruce(PoblacionActual.get(padres.get(0)),PoblacionActual.get(padres.get(1)), contador);
                evaluado = true;
                if(primeraVez){
                    log.append("    El nuevo individuo ha nacido por Cruce \n");
                }
            }else{ 
                nuevo = PoblacionActual.get(0);
                if(primeraVez){
                    log.append("    El nuevo individuo no ha nacido por Cruce \n");
                }
            }
            if(primeraVez){
                log.append("        El hijo "+nuevo.getValor()+ "el cual es el "+ i+" hijo nacido de su generacion ha nacido de los Padres  "+padres.get(0)+" y "+padres.get(1)+"\n");
            }
            //Mutación
            for(int j=0; j<d; j++){//Recorremos cada gen
               if( (random.nextDouble()%1) <= 0.01){//Mutamos el gen
                   Double[] sol = nuevo.getSolucion();
                   sol[j] = random.nextDouble(xMax) + xMin;
                   nuevo.setSolucion(sol);
                   evaluado = true;
                   if(primeraVez){
                        log.append("         El hijo presenta 1 mutacion nueva"+" en el alelo "+j+"\n");
                   }
               }       
            }
            if(primeraVez){
                log.append("                el nuevo usuario tiene:\n");
                log.append("                    fitness: "+nuevo.getValor()+"\n");
                log.append("                    genotipo: ");
                for(int u=0; u<nuevo.getSolucion().length; u++)
                    log.append(" "+nuevo.getSolucion()[u]+" ");
                log.append("\n");
                log.append("----------------------------------------------------------------------------------------------------------------\n");
            }
            nuevaGeneracion.add(nuevo);  
            if(evaluado) evaluaciones++;
        }
        if(primeraVez){
            log.append("----------------------------------------------------------------------------------------------------------------\n\n");
        }
        return nuevaGeneracion;
     }
    
    private ArrayList<Integer> generarPadres(ArrayList<Individuo> PoblacionActual){
        ArrayList<Integer> padres = new ArrayList<>();
        //Generamos primer padre
        ArrayList<Integer> candidatos = new ArrayList<>();
        for(int j=0; j<k; j++){
            candidatos.add(random.nextInt((tamPoblacion-1)));
        }
        int mejor = candidatos.get(0);
        for(int j=1; j<k; j++){
            if( evalua(PoblacionActual.get(candidatos.get(j)).getValor(),PoblacionActual.get(mejor).getValor())){
                mejor = candidatos.get(j);
            }
        }
        padres.add(mejor);
        //Generamos segundo padre
        do{
            candidatos.clear();
            for(int j=0; j<k; j++){
                candidatos.add(random.nextInt((tamPoblacion-1)));
            }
            mejor = candidatos.get(0);
            for(int j=1; j<k; j++){
                if( evalua(PoblacionActual.get(candidatos.get(j)).getValor(),PoblacionActual.get(mejor).getValor())){
                    mejor = candidatos.get(j);
                }
            }
        }while( padres.get(0) == mejor);
        padres.add(mejor);
        return padres;
    }
    
    private Individuo cruce(Individuo individuo1,Individuo individuo2, int contador){
        Double[] solucion = new Double[d];
        Double[] padre1 = individuo1.getSolucion();
        Double[] padre2 = individuo2.getSolucion();
            
        if( !blx_alfa){//BLX-MEDIA
            for(int a =0; a<d; a++)
                solucion[a] = (padre1[a]+padre2[a])/2;
        }else{//BLX-ALFA
            for(int a=0; a<d; a++){
                Double max = 0.0,min = 0.0;
                if( padre1[a] > padre2[a]){
                    max = padre1[a];
                    min = padre2[a]; 
                }else{
                    max = padre2[a];
                    min = padre1[a];
                }
                Double Int = max - min;
                Double MinIntervalo = min - (Int*alfa);
                Double MaxIntervalo = max + (Int*alfa);
                solucion[a] = (random.nextDouble() * (MaxIntervalo-MinIntervalo)) + MinIntervalo;
            }
        }
        Individuo nuevo = new Individuo(solucion,contador,algo,archivoDeDatos,algoer);
        return nuevo;
    }
    
    private void buscarElite(ArrayList<Individuo> poblacion){
        Individuo mejor = poblacion.get(0);
        int pos = 0;
        for(int i=1; i<poblacion.size(); i++){
            if(evalua(poblacion.get(i).getValor(),mejor.getValor())){
                mejor = poblacion.get(i);
                pos = i;
            }
        }
        poblacion.get(pos).setElite(true);
    }
    
    
    //Devuelve true solo si el indivuo1 tiene mejor fitness que el individuo2
    private Boolean evalua(double valor1, double valor2){
            return Math.abs(OptimoGlobal-valor1) < Math.abs(OptimoGlobal-valor2) ? true : false;
    }
    
    private ArrayList<Individuo> PoblacionInicial(){
        ArrayList<Individuo> PoblacionActual = new ArrayList<>();
        
        for(int i = 0; i<tamPoblacion; i++){
            Double[] solActual = solucionInicial();
            Individuo ind= new Individuo(solActual,1,algo,archivoDeDatos,algoer);
            PoblacionActual.add(ind);
        }
        
        return PoblacionActual;
    }
    
    private Double[] solucionInicial(){
        Double[] vector = new Double[d]; 
        for(int i=0; i<d; i++){
            vector[i] = random.nextDouble(xMax) + xMin;
        }
        return vector;
    }
     
    public String getLog(){
        return log.toString();
    }
    
    
    
    public ArrayList<Individuo> remplazarGeneracion(ArrayList<Individuo> PoblacionActual,ArrayList<Individuo> generacionNueva){
        //remplazo buscando el elite
        if(primeraVez){
            log.append("realizamos el remplazo de la nueva generacion por la antigua-----------------------\n");
        }
            Individuo antiguaElite = remplazo(PoblacionActual, generacionNueva);
        
            //Reemplazamos generación
            ArrayList<Individuo> PoblacionActualProvisional = generacionNueva;
        
            //Buscamos el peor para sustituirlo por la elite
            busquedaYSustitucionPorElPeor(PoblacionActualProvisional, antiguaElite);
            return PoblacionActualProvisional;
    }
    
    
    
    public Individuo remplazo(ArrayList<Individuo> PoblacionActual,ArrayList<Individuo> generacionNueva){
        Individuo antiguaElite = buscarElitePoblacionActual(PoblacionActual);
        //Buscamos y guardamos el elite de nuestra nueva generación
        buscarEliteNuevaGeneracion(generacionNueva, antiguaElite); 
        return antiguaElite;
    }
    
    
    
    
    public Individuo buscarElitePoblacionActual(ArrayList<Individuo> PoblacionActual){
        Individuo antiguaElite = PoblacionActual.get(0);
            for(int i=0; i<tamPoblacion; i++){
                if( PoblacionActual.get(i).getElite())
                    antiguaElite = PoblacionActual.get(i);
            }
        return antiguaElite;
    }
    public void buscarEliteNuevaGeneracion(ArrayList<Individuo> generacionNueva,Individuo antiguaElite){
         //Buscamos y guardamos el elite de nuestra nueva generación
            Individuo nuevoElite = generacionNueva.get(0);
            for(int i=0; i<tamPoblacion; i++){
                if( generacionNueva.get(i).getElite()){
                    nuevoElite = generacionNueva.get(i);
                    if(evalua(antiguaElite.getValor(),nuevoElite.getValor())){
                        log.append("\nConvervamos la elite de la generación anterior\n");
                        generacionNueva.get(i).setElite(false);   
                    }
                    else{
                        antiguaElite.setElite(false);   
                    }
                }
            }
    }
    
    public void busquedaYSustitucionPorElPeor(ArrayList<Individuo> PoblacionActual,Individuo antiguaElite){
                   //Buscamos el peor para sustituirlo por la elite
            Double peor = antiguaElite.getValor();
            int pos = 0;
            boolean encontrado = false;
            for(int i=0; i<tamPoblacion; i++){
                if( evalua(peor, PoblacionActual.get(i).getValor())){
                    pos = i;
                    peor = PoblacionActual.get(i).getValor();
                    encontrado = true;
                }
            }
            
            
            if( encontrado ){//Si la mejor solucion ha sobrevivido
               PoblacionActual.remove(pos);
               PoblacionActual.add(antiguaElite);
            }
            else//Si la mejor solucion no ha sobrevivido (no se ha podido meter elite)
            {
                log.append("\nLa mejor solucion no ha sobrevivido -> La metemos\n");
                //Lanzamos torneo, seleccionamos al peor y lo cambiamos por el elite
                ArrayList<Individuo> torneo = new ArrayList<>();
                ArrayList<Integer> posiciones = new ArrayList<>();
                for(int i=0; i<kElite; i++){
                    int valor = random.nextInt(tamPoblacion);
                    torneo.add( PoblacionActual.get(valor));
                    posiciones.add(valor);
                }
                peor = torneo.get(0).getValor();
                for(int i=1; i<kElite; i++)
                    if( evalua(peor,torneo.get(i).getValor()) ){
                        peor = torneo.get(i).getValor();
                        pos = i;
                    }
               PoblacionActual.remove(posiciones.get(pos));
               antiguaElite.setElite(false);
               PoblacionActual.add(antiguaElite);
            }
    }
}
