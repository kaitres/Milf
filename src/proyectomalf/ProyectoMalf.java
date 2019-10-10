/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectomalf;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author ThePina
 */

public class ProyectoMalf {
    public static int contador = 0;
    public static ArrayList<Object> parseo = new ArrayList();

    /**
     * @param args the command line arguments
     */


    //* >>>> . >>>>> | >>>>
        

   
    
    
    
    /*private static ArrayList buscarComando(String exprecion, int tipo){
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
*/
    private static int contador() {
        return contador += 1;
    }

    private static Nodo crearEstrella_Basica(char cararcater) {
        Nodo nodo = new Nodo(contador(), 0);
        nodo.aniadirTransicion(new Nodo(contador(), 1), 'E');
        nodo.getNodo(0).aniadirTransicion(new Nodo(contador(), 1), cararcater);
        nodo.getNodo(0).getNodo(0).aniadirTransicion(new Nodo(contador(), 2), 'E');
        nodo.getNodo(0).getNodo(0).aniadirTransicion(nodo.getNodo(0), 'E');
        nodo.aniadirTransicion(nodo.getNodo(0).getNodo(0).getNodo(0), 'E');
        nodo.fin = nodo.getNodo(0).getNodo(0).getNodo(0);
        return nodo;
    }

    private static Nodo crearBarra_Basica(char c1, char c2) {
        Nodo nodo = new Nodo(contador(), 0);
        Nodo fin = new Nodo(contador(), 2);

        nodo.aniadirTransicion(new Nodo(contador(), 1), 'E');
        nodo.aniadirTransicion(new Nodo(contador(), 1), 'E');

        nodo.getNodo(0).aniadirTransicion(new Nodo(contador(), 1), c1);
        nodo.getNodo(1).aniadirTransicion(new Nodo(contador(), 1), c2);

        nodo.getNodo(0).getNodo(0).aniadirTransicion(fin, 'E');
        nodo.getNodo(1).getNodo(0).aniadirTransicion(fin, 'E');

        nodo.fin = fin;


        return nodo;


    }

    private static Nodo crearPunto_basico(char caracter1, char caracter2) {
        Nodo nodo = new Nodo(contador(), 0);
        nodo.aniadirTransicion(new Nodo(contador(), 1), caracter1);
        nodo.getNodo(0).aniadirTransicion(new Nodo(contador(), 2), caracter2);
        nodo.fin = nodo.getNodo(0).getNodo(0);
        return nodo;

    }

    private static Nodo estrella(Object entrada) {
        if (entrada instanceof Nodo) {
            //rellenar
        }
        if (entrada instanceof Character) {
            Nodo nodo = crearEstrella_Basica((Character) entrada);
            return nodo;
        }
        return null;
    }

    private static Nodo barra(Object entrada1, Object entrada2) {
        if ((entrada1 instanceof Character) && (entrada2 instanceof Character)) {
            Nodo nodo = crearBarra_Basica((Character) entrada1, (Character) entrada2);
            return nodo;
        }
        if ((entrada1 instanceof Character) && (entrada2 instanceof Nodo)) {

        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Character)) {

        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Nodo)) {

        }

        return null;
    }

    private static Nodo Punto(Object entrada1, Object entrada2) {
        System.out.println("estoy en ");
        if ((entrada1 instanceof Character) && (entrada2 instanceof Character)) {
            System.out.println("hoaskdokasodkasodka");
            Nodo nodo = crearPunto_basico((Character) entrada1, (Character) entrada2);
            return nodo;
        }
        if ((entrada1 instanceof Character) && (entrada2 instanceof Nodo)) {

        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Character)) {

        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Nodo)) {
            Nodo nodo = (Nodo) entrada1;
            nodo.fin.aniadirTransicion(nodo, 'E');

            return nodo;

        }

        return null;
    }

    private static Nodo recucion(ArrayList<Object> exprecion) {
        System.out.println("dentro recurcion");
        Nodo aux;
        //falta el parentecis
        int i = 0;
        while (i < parseo.size()) {
            if (parseo.get(i) instanceof Character) {
                if ((Character) parseo.get(i) == '*') {
                    aux = estrella(parseo.get(i - 1));
                    parseo.remove(i);
                    parseo.remove(i - 1);
                    parseo.add(i - 1, aux);

                }
            }
            i++;
        }
        i = 0;
        while (i < parseo.size()) {
            if (parseo.get(i) instanceof Character) {
                if ((Character) parseo.get(i) == '.') {

                    aux = Punto(parseo.get(i - 1), parseo.get(i + 1));
                    System.out.println(parseo);
                    parseo.remove(i + 1);
                    System.out.println(parseo);
                    parseo.remove(i);
                    System.out.println(parseo);
                    parseo.remove(i - 1);
                    System.out.println(parseo);
                    parseo.add(i - 1, aux);
                    System.out.println("resultado");
                    System.out.println(parseo);
                }
            }
            i++;
        }
        i = 0;
        while (i < parseo.size()) {
            if (parseo.get(i) instanceof Character) {
                if ((Character) parseo.get(i) == '|') {
                    aux = (barra(parseo.get(i - 1), parseo.get(i + 1)));
                    parseo.remove(i + 1);
                    parseo.remove(i);
                    parseo.remove(i - 1);
                    parseo.add(i - 1, aux);

                }
            }
            i++;
        }
        return (Nodo) parseo.get(0);
    }

    private static String parentecis(String exprecion, int index) {
        int count = 0;
        if (exprecion.charAt(index) == '(') {


            for (int i = index + 1; i < exprecion.length(); i++) {
                if (exprecion.charAt(i) == '(') {
                    count += 1;
                }
                if (exprecion.charAt(i) == ')') {
                    if (count > 0) {
                        count -= 1;
                    } else {
                        return exprecion.substring((index + 1), i);
                    }
                }
            }
        }
        if (exprecion.charAt(index) == ')') {
            for (int i = index - 1; i >= 0; i--) {
                if (exprecion.charAt(i) == ')') {
                    count += 1;
                }
                if (exprecion.charAt(i) == '(') {
                    if (count > 0) {
                        count -= 1;
                    } else {
                        return exprecion.substring(i, index - 1);
                    }
                }
            }
        }
        return "0";
    }

    private static void imprimirNodo(Nodo n, Nodo fin) {
        if (n.numero == fin.numero) {
            return;
        }

        for (Nodo i : n.conexion) {
            if (n.numero > i.numero) return;
            System.out.println(n.numero + " " + n.transiciones.get(n.conexion.indexOf(i)) + " " + i.numero);

            imprimirNodo(i, fin);

        }
        return;
    }

    public static void main(String[] args) {


        Scanner scan = new Scanner(System.in);
        String entrada = scan.next();
        int i = 0;
        while (i < entrada.length()) {
            parseo.add((entrada.charAt(i)));
            i++;
        }
        System.out.println(parseo);
        System.out.println(recucion(parseo));
        imprimirNodo(((Nodo) parseo.get(0)), ((Nodo) parseo.get(0)).fin);


    }

    public static class Parse {
        Nodo nodo;
        char caracter;
        boolean isNodo = false;
        Nodo fin;

        public Parse(Nodo node) {
            this.nodo = nodo;
            isNodo = true;
        }

        public Parse(char caracter) {
            this.caracter = caracter;
        }

        public boolean isIsNodo() {
            return isNodo;
        }

    }

    public static class Nodo {
        int numero;
        int estado;//puede ser 0 de transicion, 1 de inicio, 2 de final
        ArrayList<Character> transiciones;
        ArrayList<Nodo> conexion;
        Nodo fin;

        public Nodo(int numero, int estado) {
            this.numero = numero;
            this.estado = estado;
            this.conexion = new ArrayList<>();
            this.transiciones = new ArrayList<>();
        }

        public static void changeComienzo(Nodo nodo1, Nodo nodo2, char transicion) {
            nodo1.setEstado(0);
            nodo2.setEstado(1);
            nodo1.aniadirTransicion(nodo2, transicion);
        }

        public void aniadirTransicion(Nodo nodo, char transicion) {
            transiciones.add(transicion);
            conexion.add(nodo);
        }

        public char getTransicion(int i) {
            return transiciones.get(i);
        }

        public ArrayList<Character> getTransiciones() {
            return transiciones;
        }

        public Nodo getNodo(int i) {
            return conexion.get(i);
        }

        public ArrayList<Nodo> getNodos() {
            return conexion;
        }

        public int getNumero() {
            return numero;
        }

        public int getEstado() {
            return estado;
        }

        public void setEstado(int estado) {
            this.estado = estado;
        }

        public boolean existeEnTransicion(String search) {
            return transiciones.contains(search);
        }

    }

}
