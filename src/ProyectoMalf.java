import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ProyectoMalf {
    public static int contador = 0;
    public static ArrayList<Integer> K_AFND = new ArrayList<>();
    public static ArrayList<String> Conexiones = new ArrayList<>();


    private static int contador() {
        return contador += 1;
    }

    private static Nodo crearEstrella_Basica(char caracter) {
        Nodo nodo = new Nodo(contador(), 0);
        nodo.anadirTransicion(new Nodo(contador(), 1), '_');
        nodo.getNodo(0).anadirTransicion(new Nodo(contador(), 1), caracter);
        nodo.getNodo(0).getNodo(0).anadirTransicion(new Nodo(contador(), 2), '_');
        nodo.getNodo(0).getNodo(0).anadirTransicion(nodo.getNodo(0), '_');
        nodo.anadirTransicion(nodo.getNodo(0).getNodo(0).getNodo(0), '_');
        nodo.fin = nodo.getNodo(0).getNodo(0).getNodo(0);
        return nodo;
    }

    private static Nodo crearBarra_Basica(char c1, char c2) {
        Nodo nodo = new Nodo(contador(), 0);
        Nodo fin = new Nodo(contador(), 2);

        nodo.anadirTransicion(new Nodo(contador(), 1), '_');
        nodo.anadirTransicion(new Nodo(contador(), 1), '_');

        nodo.getNodo(0).anadirTransicion(new Nodo(contador(), 1), c1);
        nodo.getNodo(1).anadirTransicion(new Nodo(contador(), 1), c2);

        nodo.getNodo(0).getNodo(0).anadirTransicion(fin, '_');
        nodo.getNodo(1).getNodo(0).anadirTransicion(fin, '_');

        nodo.fin = fin;

        return nodo;
    }

    private static Nodo crearPunto_basico(char caracter1, char caracter2) {
        Nodo nodo = new Nodo(contador(), 0);
        nodo.anadirTransicion(new Nodo(contador(), 1), caracter1);
        nodo.getNodo(0).anadirTransicion(new Nodo(contador(), 2), caracter2);
        nodo.fin = nodo.getNodo(0).getNodo(0);
        return nodo;
    }

    private static Nodo estrella(Object entrada) {
        if (entrada instanceof Nodo) {
            Nodo nodo = new Nodo(contador(), 0);
            Nodo fin = new Nodo(contador(), 2);

            nodo.anadirTransicion(((Nodo) (entrada)), '_');
            ((Nodo) (entrada)).fin.anadirTransicion(fin, '_');

            ((Nodo) entrada).fin.anadirTransicion((Nodo) (entrada), '_');
            ((Nodo) entrada).setEstado(1);
            ((Nodo) entrada).fin.setEstado(1);

            nodo.anadirTransicion(fin, '_');
            nodo.fin = fin;
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

            Nodo nodo = new Nodo(contador(), 0);
            Nodo fin = new Nodo(contador(), 2);
            //caracter
            nodo.anadirTransicion(new Nodo(contador(), 1), '_');
            nodo.getNodo(0).anadirTransicion(new Nodo(contador(), 1), (Character) entrada1);
            nodo.getNodo(0).getNodo(0).anadirTransicion(fin, '_');

            //nodo
            nodo.anadirTransicion(((Nodo) entrada2), '_');
            ((Nodo) entrada2).setEstado(1);
            ((Nodo) entrada2).fin.setEstado(1);
            ((Nodo) entrada2).fin.anadirTransicion(fin, '_');

            nodo.fin = fin;

            return nodo;
        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Character)) {
            Nodo nodo = new Nodo(contador(), 0);
            Nodo fin = new Nodo(contador(), 2);
            //caracter
            nodo.anadirTransicion(new Nodo(contador(), 1), '_');
            nodo.getNodo(0).anadirTransicion(new Nodo(contador(), 1), (Character) entrada2);
            nodo.getNodo(0).getNodo(0).anadirTransicion(fin, '_');

            //nodo
            nodo.anadirTransicion(((Nodo) entrada1), '_');
            ((Nodo) entrada1).setEstado(1);
            ((Nodo) entrada1).fin.setEstado(1);
            ((Nodo) entrada1).fin.anadirTransicion(fin, '_');

            nodo.fin = fin;

            return nodo;
        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Nodo)) {
            Nodo nodo = new Nodo(contador(), 0);
            Nodo fin = new Nodo(contador(), 2);

            nodo.anadirTransicion(((Nodo) entrada1), '_');
            nodo.anadirTransicion(((Nodo) entrada2), '_');

            ((Nodo) entrada1).setEstado(1);
            ((Nodo) entrada2).setEstado(1);

            ((Nodo) entrada1).fin.setEstado(1);
            ((Nodo) entrada2).fin.setEstado(1);

            ((Nodo) entrada1).fin.anadirTransicion(fin, '_');
            ((Nodo) entrada2).fin.anadirTransicion(fin, '_');

            nodo.fin = fin;

            return nodo;
        }
        return null;
    }


    //esta lista esta wea
    private static Nodo Punto(Object entrada1, Object entrada2) {
        if ((entrada1 instanceof Character) && (entrada2 instanceof Character)) {
            Nodo nodo = crearPunto_basico((Character) entrada1, (Character) entrada2);
            return nodo;
        }
        if ((entrada1 instanceof Character) && (entrada2 instanceof Nodo)) {
            Nodo nodo = new Nodo(contador(), 0);
            nodo.anadirTransicion((Nodo) (entrada2), (Character) (entrada1));
            ((Nodo) (entrada2)).setEstado(1);
            nodo.fin = ((Nodo) (entrada2)).fin;
            return nodo;
        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Character)) {
            Nodo nodo = ((Nodo) (entrada1));
            Nodo auxfin = new Nodo(contador(), 2);
            nodo.fin.setEstado(1);
            nodo.fin.anadirTransicion(auxfin, ((Character) (entrada2)));
            nodo.fin = auxfin;
            return nodo;
        }
        if ((entrada1 instanceof Nodo) && (entrada2 instanceof Nodo)) {
            Nodo nodo = ((Nodo) (entrada1));
            Nodo auxfin = ((Nodo) (entrada2));
            nodo.fin.anadirTransicion(auxfin, '_');
            nodo.fin.setEstado(1);
            nodo.fin = auxfin.fin;
            auxfin.setEstado(1);

            return nodo;
        }

        return null;
    }

    private static Nodo casoBase(Character a) {
        Nodo nodo = new Nodo(contador(), 0);
        nodo.anadirTransicion(new Nodo(contador(), 2), a);
        nodo.fin = nodo.getNodo(0);
        return nodo;
    }

    private static Nodo recursion(ArrayList<Object> expresion) {

        Nodo aux;
        //falta el parentesis
        int i = 0;
        if (expresion.size() == 1) {
            aux = casoBase((Character) expresion.get(0));
            expresion.remove(0);
            expresion.add(aux);

        }
        while (i < expresion.size()) {
            if (expresion.get(i) instanceof Character) {
                if ((Character) expresion.get(i) == '(') {
                    ArrayList<Object> auxiliar = new ArrayList<>();
                    auxiliar = parentesis(expresion, i);
                    int indiceMayor = indexFinalParentesis(expresion, i);

                    for (int k = 0; k < indiceMayor - i; ++k) {
                        expresion.remove(i);
                    }

                    expresion.set(i, recursion(auxiliar));

                }
            }
            i++;
        }
        i = 0;

        while (i < expresion.size()) {
            if (expresion.get(i) instanceof Character) {
                if ((Character) expresion.get(i) == '*') {

                    aux = estrella(expresion.get(i - 1));

                    expresion.remove(i);
                    expresion.remove(i - 1);
                    expresion.add(i - 1, aux);
                    i--;
                }
            }
            i++;
        }
        i = 0;

        while (i < expresion.size()) {
            if (expresion.get(i) instanceof Character) {
                if ((Character) expresion.get(i) == '.') {

                    aux = Punto(expresion.get(i - 1), expresion.get(i + 1));

                    expresion.remove(i + 1);
                    expresion.remove(i);
                    expresion.remove(i - 1);
                    expresion.add(i - 1, aux);
                    i--;
                }
            }
            i++;
        }
        i = 0;

        while (i < expresion.size()) {
            if (expresion.get(i) instanceof Character) {
                if ((Character) expresion.get(i) == '|') {

                    aux = (barra(expresion.get(i - 1), expresion.get(i + 1)));

                    expresion.remove(i + 1);
                    expresion.remove(i);
                    expresion.remove(i - 1);
                    expresion.add(i - 1, aux);
                    i--;
                }
            }
            i++;
        }
        return (Nodo) expresion.get(0);
    }

    private static ArrayList parentesis(ArrayList lista, int index) {
        ArrayList<Object> aux = new ArrayList<>();
        int count = 0;
        if ((Character) lista.get(index) == '(') {
            for (int i = index + 1; i < lista.size(); i++) {
                if ((Character) lista.get(i) == '(') {
                    count += 1;
                }
                if ((Character) lista.get(i) == ')') {
                    if (count > 0) {
                        count -= 1;
                    } else {
                        return aux;
                    }
                }
                aux.add(lista.get(i));
            }
        }
        return null;
    }

    private static int indexFinalParentesis(ArrayList lista, int index) {
        int count = 0;
        if ((Character) lista.get(index) == '(') {
            for (int i = index + 1; i < lista.size(); i++) {
                if ((Character) lista.get(i) == '(') {
                    count += 1;
                }
                if ((Character) lista.get(i) == ')') {
                    if (count > 0) {
                        count -= 1;
                    } else {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    private static void imprimirNodo(Nodo nodo) {
        System.out.println("AFND:");
        System.out.print("K={");
        int i = K_AFND.get(K_AFND.size() - 1);
        K_AFND.remove(K_AFND.size() - 1);
        for (Integer k : K_AFND) {
            System.out.print("q" + k + ",");
        }
        System.out.println("q" + i + "}");
        for (String s : Conexiones) {
            System.out.println(s);
        }
        System.out.println("s=q" + nodo.numero);
        System.out.println("F={q" + nodo.fin.numero + "}");
        return;
    }

    public static void main(String[] args) {
        ArrayList<Object> parseo = new ArrayList<>();
        Conexiones.add("Delta:");
        Nodo grafo;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String entrada = "";
        try {
            entrada = br.readLine();
        } catch (IOException e) {
            System.out.println("Can't read from input");
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println(entrada.length());
        int i = 0;
        while (i < entrada.length()) {
            parseo.add((entrada.charAt(i)));
            i++;
        }
        grafo = recursion(parseo);
        imprimirNodo(grafo);
    }

    public static class Parse {
        private Nodo nodo;
        private char caracter;
        private boolean isNodo = false;
        Nodo fin;

        public Parse(Nodo node) {
            this.nodo = nodo;
            isNodo = true;
        }

        public Parse(char caracter) {
            this.caracter = caracter;
        }

        public boolean isNodo() {
            return this.isNodo;
        }

    }

    public static class Nodo {
        int numero;
        int estado;//puede ser 0 de transicion, 1 de inicio, 2 de final
        boolean check = false;
        ArrayList<Character> transiciones;
        ArrayList<Nodo> conexion;
        Nodo fin;

        public Nodo(int numero, int estado) {
            K_AFND.add(numero);
            this.numero = numero;
            this.estado = estado;
            this.conexion = new ArrayList<>();
            this.transiciones = new ArrayList<>();
        }

        public static void changeComienzo(Nodo nodo1, Nodo nodo2, char transicion) {
            nodo1.setEstado(0);
            nodo2.setEstado(1);
            nodo1.anadirTransicion(nodo2, transicion);
        }

        public void anadirTransicion(Nodo nodo, char transicion) {
            Conexiones.add("(q" + this.numero + "," + transicion + ",q" + nodo.numero + ")");
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
