/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectomalf;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ThePina
 */

public class ProyectoMalf {
    public static class Nodo{
        int numero;
        int estado;//puede ser 0 de transicion, 1 de inicio, 2 de final
        ArrayList<String> transiciones ;
        ArrayList<Nodo> conexion;
        
        public Nodo(int numero, int estado){
            this.numero = numero;
            this.estado = estado;
            this.conexion = new ArrayList<>();
            this.transiciones = new ArrayList<>();
        }
        public void aniadirTransicion(Nodo nodo, String transicion){
            transiciones.add(transicion);
            conexion.add(nodo);
        }
        public String getTransicion(int i){
            return transiciones.get(i);
        }
        public ArrayList<String> getTransiciones(){
            return transiciones;
        }
        public Nodo getNodo(int i){
            return conexion.get(i);
        }
        public ArrayList<Nodo> getNodos(){
            return conexion;
        }
        public int getNumero(){
            return numero;
        }
        public int getEstado(){
            return estado;
        }
        public boolean existeEnTransicion(String search){
            return transiciones.contains(search);
        }
        public void setEstado(int estado){
            this.estado=estado;
        }
        public static void changeComienzo(Nodo nodo1, Nodo nodo2, String transicion){
            nodo1.setEstado(0);
            nodo2.setEstado(1);
            nodo1.aniadirTransicion(nodo2, transicion);
        }

    }
    /**
     * @param args the command line arguments
     */
  
    
    
            //* >>>> . >>>>> | >>>>
        

   
    
    
    
    private static ArrayList buscarComando(String exprecion, int tipo){
        String aux= exprecion;
        boolean afueraParentecis;
        int contParentecis =0;
        ArrayList<Integer> retu= new ArrayList<>();
        if(tipo==0){//*
            for (int i = 0; i < exprecion.length(); i++) {
                if (exprecion.charAt(i)=='*') {
                    contParentecis =0;
                    for (int j = i; j >= 0; j--) {
                        if(exprecion.charAt(j)=='('){
                            contParentecis+=1;
                        }
                        if(exprecion.charAt(j)==')'){
                            contParentecis-=1;
                        }
                        
                    }
                    if(contParentecis==0){
                        retu.add(i);
                    }
                }
            }
        }
        if(tipo==1){//.
             for (int i = 0; i < exprecion.length(); i++) {
                if (exprecion.charAt(i)=='.') {
                    contParentecis =0;
                    for (int j = i; j >= 0; j--) {
                        if(exprecion.charAt(j)=='('){
                            contParentecis+=1;
                        }
                        if(exprecion.charAt(j)==')'){
                            contParentecis-=1;
                        }
                        
                    }
                    if(contParentecis==0){
                        retu.add(i);
                    }
                }
            }
            
        }
        if(tipo==2){//|
             for (int i = 0; i < exprecion.length(); i++) {
                if (exprecion.charAt(i)=='|') {
                    contParentecis =0;
                    for (int j = i; j >= 0; j--) {
                        if(exprecion.charAt(j)=='('){
                            contParentecis+=1;
                        }
                        if(exprecion.charAt(j)==')'){
                            contParentecis-=1;
                        }
                        
                    }
                    if(contParentecis==0){
                        retu.add(i);
                    }
                }
            }
            
        }
        return retu;
    }
    private static String parentecis(String exprecion , int index){
            int count=0;
            
            for (int i = index + 1; i < exprecion.length(); i++) {
                if(exprecion.charAt(i)=='('){
                    count+=1;
                }
                if(exprecion.charAt(i)==')'){
                    if(count>0){
                        count-=1;
                    }
                    else{
                        return  exprecion.substring((index + 1), i);
                    }
                }
            }
            return "0";
        }
    
        public static void main(String[] args) {
        
        
        
        Scanner scan= new Scanner(System.in);
        String entrada= scan.next();
        System.out.println(buscarComando(entrada, 2));

        
        
        
        
        }
    
}
