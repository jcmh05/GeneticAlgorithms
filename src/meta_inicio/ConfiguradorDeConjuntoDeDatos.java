
package meta_inicio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Jc & Oscar
 */
public class ConfiguradorDeConjuntoDeDatos {
    ArrayList<Double> dni;
    ArrayList<Double> T;
    ArrayList<Double> W;
    ArrayList<Double> SMR;
    ArrayList<Double> P;
    ArrayList<Double> AP;
    ArrayList<Double> G;
    int variables = 5;
    
    public ConfiguradorDeConjuntoDeDatos(){
        
    }

    public ConfiguradorDeConjuntoDeDatos(String ruta) {
        dni = new ArrayList<>();
        T = new ArrayList<>();
        W = new ArrayList<>();
        SMR = new ArrayList<>();
        P = new ArrayList<>();
        AP = new ArrayList<>();
        
        String linea;
        FileReader f=null;
        try{
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while((linea = b.readLine()) != null){
                if( linea.charAt(0) != 'D'){
                    String[] split= linea.split(",");
                    dni.add(Double.parseDouble(split[0]));
                    AP.add(Double.parseDouble(split[1]));
                    T.add(Double.parseDouble(split[2]));
                    W.add(Double.parseDouble(split[3]));
                    SMR.add(Double.parseDouble(split[4]));
                    P.add(Double.parseDouble(split[5]));      
                }
            }
        }catch(IOException e){
           System.out.println(e);
        }
        int tt=0;
    }

    public ArrayList<Double> getDni() {
        return dni;
    }

    public ArrayList<Double> getP() {
        return P;
    }

    public ArrayList<Double> getSMR() {
        return SMR;
    }

    public ArrayList<Double> getT() {
        return T;
    }

    public ArrayList<Double> getW() {
        return W;
    }

    public int getVariables() {
        return variables;
    }
    
    
    
    
    
}
