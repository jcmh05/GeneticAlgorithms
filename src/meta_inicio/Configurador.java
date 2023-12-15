package meta_inicio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Jc & Oscar
 */
public class Configurador {
    ArrayList<String> archivos;
    ArrayList<String> algoritmos;
    ArrayList<Long> semillas;
    ArrayList<String> funcion;
    ArrayList<Boolean> BLK_ALFA;
    int maxEvaluaciones;
    
    int tamPoblacion;      
    int k;                  
    int kElite;             
    double alfa;
    
    ArrayList<String> funcionesError;
    
    public Configurador(String ruta){
        archivos = new ArrayList<>();
        algoritmos = new ArrayList<>();
        semillas = new ArrayList<>();
        funcion = new ArrayList<>();
        funcionesError = new ArrayList<>();
        BLK_ALFA = new ArrayList<>();
        
        String linea;
        FileReader f=null;
        try{
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while((linea = b.readLine()) != null){
                String[] split = linea.split("=");
                switch(split[0]){
                    //archivos.add(split[i]);
                    case "Archivos":
                        String[] v= split[1].split(" ");
                        for (int i=0; i < v.length; i++){
                            archivos.add(v[i]);
                        }
                        break;
                    case "Semillas":
                        String[] vsemillas= split[1].split(" ");
                        for (int i=0; i < vsemillas.length; i++){
                            semillas.add(Long.parseLong(vsemillas[i]));
                        }
                        break;
                    case "Algoritmos":                         
                        String[] valgoritmos = split[1].split(" ");
                        for (int i = 0; i < valgoritmos.length; i++) {
                            algoritmos.add(valgoritmos[i]);
                        }
                        break;
                    case "BLK_ALFA":
                        String[] vblx = split[1].split(" ");
                        for(int i=0; i< vblx.length; i++){
                            BLK_ALFA.add(Boolean.parseBoolean(vblx[i]));
                        }
                        break;
                    case "Funcion":
                        String[] vfun= split[1].split(" ");
                        for (int i=0; i < vfun.length; i++){
                            funcion.add(vfun[i]);
                        }
                        break;
                    case "maxEvaluaciones":
                        maxEvaluaciones = Integer.parseInt(split[1]);
                        break;
                    case "Poblacion":
                        tamPoblacion = Integer.parseInt(split[1]);
                        break;
                    case "TorneoSeleccion":
                        k = Integer.parseInt(split[1]);
                        break;
                    case "TorneoElite":
                        kElite = Integer.parseInt(split[1]);
                        break;
                    case "alfa":
                        alfa = Double.parseDouble(split[1]);
                        break;
                    case "FuncionError":
                        String[] vfuncionesError= split[1].split(" ");
                        for (int i=0; i < vfuncionesError.length; i++){
                            funcionesError.add(vfuncionesError[i]);
                        }
                }
            }
        }catch(IOException e){
                    System.out.println(e);
        }
    }

    public ArrayList<String> getArchivos() {
        return archivos;
    }

    public ArrayList<String> getAlgoritmos() {
        return algoritmos;
    }

    public ArrayList<Long> getSemillas() {
        return semillas;
    }
    
    public ArrayList<String> getFuncion() {
        return funcion;
    }
    
    public ArrayList<Boolean> getBLK_ALFA() {
        return BLK_ALFA;
    }
    
    public Integer getMaxiteraciones(){
        return maxEvaluaciones;
    }

    public double getAlfa() {
        return alfa;
    }

    public int getK() {
        return k;
    }

    public int getMaxEvaluaciones() {
        return maxEvaluaciones;
    }

    public int getTamPoblacion() {
        return tamPoblacion;
    }

    public int getkElite() {
        return kElite;
    }

    public ArrayList<String> getFuncionesError() {
        return funcionesError;
    }  
}



