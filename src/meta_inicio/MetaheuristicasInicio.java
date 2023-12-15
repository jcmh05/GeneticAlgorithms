package meta_inicio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jc & Oscar
 */
public class MetaheuristicasInicio {

    
    public static void main(String[] args) {
        //Configurador para archivo de datos
        ArrayList<Configurador> config=new ArrayList<>();
        ConfiguradorDeConjuntoDeDatos cdatos = new ConfiguradorDeConjuntoDeDatos(args[0]);

        //Configutador para archivo config
        for(int i=1; i<args.length; i++){
            config.add(new Configurador(args[i]));
        }

        
        System.out.println("Archivosconfig: " + config.size());
        ExecutorService ejecutor = Executors.newCachedThreadPool();
        
        for(int j=0; j<config.size(); j++){
            System.out.println("Cargamos archivo: " + config.get(j).getArchivos());
            //RECORREMOS ALGORITMOS
            for (int i = 0; i < config.get(j).getAlgoritmos().size() ; i++){
                System.out.println("    └-Cargamos algoritmo: " + config.get(j).getAlgoritmos().get(i));
                for(int q=0; q< config.get(j).getFuncion().size(); q++){
                    Algoritmos algo = Algoritmos.valueOf(config.get(j).getFuncion().get(q));
                    System.out.print("        └-Cargamos función: "+ algo + "\n");
                    
                    Function func = ObtenerFuncion(algo);
                    Double xMin = func.getXMin();
                    Double xMax = func.getXMax();
                    double optimo = func.getOptimo();
                    
                    int funcionesDeError = 1;
                    //Si la función es una potencia también tendremos que recorrer las distintas funciones de error
                    if( algo == Algoritmos.Potencia){ funcionesDeError = config.get(j).getFuncionesError().size(); }
                    try {
                        for(int e=0; e<funcionesDeError; e++){
                            //Recorremos cada cruce
                            for(int b=0; b<config.get(j).getBLK_ALFA().size(); b++){
                                CountDownLatch cdl = new CountDownLatch(config.get(j).getSemillas().size());
                                switch(config.get(j).getAlgoritmos().get(i)){
                                    case("AlgoritmoEvolutivo"):
                                        ArrayList<AlgEvolutivo> m = new ArrayList();

                                            //Recorremos cada semilla
                                            for(int k=0; k< config.get(j).getSemillas().size() ; k++){
                                                AlgoritmosError algError = AlgoritmosError.valueOf(config.get(j).getFuncionesError().get(e));
                                                AlgEvolutivo meta = new AlgEvolutivo(xMin,xMax,config.get(j).getSemillas().get(k),10,algo, optimo,config.get(j).getBLK_ALFA().get(b),cdl, config.get(j).getMaxiteraciones(),config.get(j).getTamPoblacion(),config.get(j).getK(),config.get(j).getkElite(),config.get(j).getAlfa(),cdatos,algError);
                                                m.add(meta);
                                                ejecutor.execute(meta);
                                            }

                                            cdl.await();
                                            for( int k=0; k<m.size();k++){
                                               if( config.get(j).getBLK_ALFA().get(b) ){
                                                   if( algo == Algoritmos.Potencia){ guardararchivo("log/"+ "AlgoritmoEvBlxALfa" + config.get(j).getSemillas().get(k)+ config.get(j).getFuncion().get(q) + config.get(j).getFuncionesError().get(e) +".txt", m.get(k).getLog()); }
                                                   else{ guardararchivo("log/" + "AlgoritmoEvBlxAlfa" + config.get(j).getSemillas().get(k)+ config.get(j).getFuncion().get(q) +".txt", m.get(k).getLog());}
                                               }else{
                                                   if( algo == Algoritmos.Potencia){ guardararchivo("log/"+ "AlgoritmoEvBlxMedia" + config.get(j).getSemillas().get(k)+ config.get(j).getFuncion().get(q) + config.get(j).getFuncionesError().get(e) +".txt", m.get(k).getLog()); }
                                                   else{ guardararchivo("log/" + "AlgoritmoEvBlxMedia" + config.get(j).getSemillas().get(k)+ config.get(j).getFuncion().get(q) +".txt", m.get(k).getLog());}
                                               }
                                            }
                                            break;
                                    case("AlgoritmoDiferencial"):
                                        ArrayList<AlgEvolutivoDiferencial> mon = new ArrayList();
                                        for(int k=0; k< config.get(j).getSemillas().size() ; k++){
                                            AlgoritmosError algError = AlgoritmosError.valueOf(config.get(j).getFuncionesError().get(e));
                                            AlgEvolutivoDiferencial met = new AlgEvolutivoDiferencial(xMin,xMax,config.get(j).getSemillas().get(k),10,algo, optimo,cdl, config.get(j).getMaxiteraciones(),config.get(j).getTamPoblacion(),config.get(j).getK(),cdatos,algError);
                                            mon.add(met);
                                            ejecutor.execute(met);

                                        }
                                        cdl.await();
                                        for( int k=0; k<mon.size();k++){
                                            if( algo == Algoritmos.Potencia){ 
                                                guardararchivo("log/" + config.get(j).getAlgoritmos().get(i) + config.get(j).getSemillas().get(k)+ config.get(j).getFuncion().get(q) + config.get(j).getFuncionesError().get(e) +".txt", mon.get(k).getLog());
                                            }
                                            else{
                                                guardararchivo("log/" + config.get(j).getAlgoritmos().get(i) + config.get(j).getSemillas().get(k)+ config.get(j).getFuncion().get(q) +".txt", mon.get(k).getLog());
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MetaheuristicasInicio.class.getName()).log(Level.SEVERE, null, ex);
                    }
            
                }
            }
        }
    }
    
    
    public static Function ObtenerFuncion(Algoritmos algo){
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
                fun = new Potencia();
                break;
            default:
                throw new AssertionError();
        }
        return fun;
    }
    
    public static void guardararchivo(String ruta, String texto){
        FileWriter fichero = null;
        PrintWriter pw = null;
        
        try{
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);
            
            pw.print(texto);
        }catch (IOException e) {
        } finally {
            try {
                if( null!= fichero) {
                    fichero.close();
                }
            }catch(IOException e2){    
            }
        }
    }
}
