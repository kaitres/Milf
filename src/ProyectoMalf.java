import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public class ProyectoMalf {

    public static int counter = 0;
    public static ArrayList<Integer> K_AFND = new ArrayList<>();
    public static ArrayList<String> Connections_AFND = new ArrayList<>();
    public static HashSet<Character> Sigma_AFND = new HashSet<>();

    private static Node crearEstrella_Basica(char caracter) {
        Node node = new Node(NodeStatus.START);
        node.addTransition(new Node(NodeStatus.TRANSITION), '_');
        node.getConnectedNode(0).addTransition(new Node(NodeStatus.TRANSITION), caracter);
        node.getConnectedNode(0).getConnectedNode(0).addTransition(new Node(NodeStatus.FINAL), '_');
        node.getConnectedNode(0).getConnectedNode(0).addTransition(node.getConnectedNode(0), '_');
        node.addTransition(node.getConnectedNode(0).getConnectedNode(0).getConnectedNode(0), '_');
        node.finalNode = node.getConnectedNode(0).getConnectedNode(0).getConnectedNode(0);
        return node;
    }

    private static Node crearBarra_Basica(char c1, char c2) {
        Node node = new Node(NodeStatus.START);
        Node fin = new Node(NodeStatus.FINAL);

        node.addTransition(new Node(NodeStatus.TRANSITION), '_');
        node.addTransition(new Node(NodeStatus.TRANSITION), '_');

        node.getConnectedNode(0).addTransition(new Node(NodeStatus.TRANSITION), c1);
        node.getConnectedNode(1).addTransition(new Node(NodeStatus.TRANSITION), c2);

        node.getConnectedNode(0).getConnectedNode(0).addTransition(fin, '_');
        node.getConnectedNode(1).getConnectedNode(0).addTransition(fin, '_');

        node.finalNode = fin;

        return node;
    }

    private static Node crearPunto_basico(char caracter1, char caracter2) {
        Node node = new Node(NodeStatus.START);
        node.addTransition(new Node(NodeStatus.TRANSITION), caracter1);
        node.getConnectedNode(0).addTransition(new Node(NodeStatus.FINAL), caracter2);
        node.finalNode = node.getConnectedNode(0).getConnectedNode(0);
        return node;
    }

    private static Node estrella(Object entrada) {
        if (entrada instanceof Node) {
            Node node = new Node(NodeStatus.START);
            Node fin = new Node(NodeStatus.FINAL);

            node.addTransition(((Node) (entrada)), '_');
            ((Node) (entrada)).finalNode.addTransition(fin, '_');

            ((Node) entrada).finalNode.addTransition((Node) (entrada), '_');
            ((Node) entrada).status = NodeStatus.TRANSITION;
            ((Node) entrada).finalNode.status = NodeStatus.TRANSITION;

            node.addTransition(fin, '_');
            node.finalNode = fin;
        }
        if (entrada instanceof Character) {
            Node node = crearEstrella_Basica((Character) entrada);
            return node;
        }
        return null;
    }

    private static Node barra(Object entrada1, Object entrada2) {
        if ((entrada1 instanceof Character) && (entrada2 instanceof Character)) {
            Node node = crearBarra_Basica((Character) entrada1, (Character) entrada2);
            return node;
        }
        if ((entrada1 instanceof Character) && (entrada2 instanceof Node)) {

            Node node = new Node(NodeStatus.START);
            Node fin = new Node(NodeStatus.FINAL);
            //caracter
            node.addTransition(new Node(NodeStatus.TRANSITION), '_');
            node.getConnectedNode(0).addTransition(new Node(NodeStatus.TRANSITION), (Character) entrada1);
            node.getConnectedNode(0).getConnectedNode(0).addTransition(fin, '_');

            //nodo
            node.addTransition(((Node) entrada2), '_');
            ((Node) entrada2).status = NodeStatus.TRANSITION;
            ((Node) entrada2).finalNode.status = NodeStatus.TRANSITION;
            ((Node) entrada2).finalNode.addTransition(fin, '_');

            node.finalNode = fin;

            return node;
        }
        if ((entrada1 instanceof Node) && (entrada2 instanceof Character)) {
            Node node = new Node(NodeStatus.START);
            Node fin = new Node(NodeStatus.FINAL);
            //caracter
            node.addTransition(new Node(NodeStatus.TRANSITION), '_');
            node.getConnectedNode(0).addTransition(new Node(NodeStatus.TRANSITION), (Character) entrada2);
            node.getConnectedNode(0).getConnectedNode(0).addTransition(fin, '_');

            //nodo
            node.addTransition(((Node) entrada1), '_');
            ((Node) entrada1).status = NodeStatus.TRANSITION;
            ((Node) entrada1).finalNode.status = NodeStatus.TRANSITION;
            ((Node) entrada1).finalNode.addTransition(fin, '_');

            node.finalNode = fin;

            return node;
        }
        if ((entrada1 instanceof Node) && (entrada2 instanceof Node)) {
            Node node = new Node(NodeStatus.START);
            Node fin = new Node(NodeStatus.FINAL);

            node.addTransition(((Node) entrada1), '_');
            node.addTransition(((Node) entrada2), '_');

            ((Node) entrada1).status = NodeStatus.TRANSITION;
            ((Node) entrada2).status = NodeStatus.TRANSITION;

            ((Node) entrada1).finalNode.status = NodeStatus.TRANSITION;
            ((Node) entrada2).finalNode.status = NodeStatus.TRANSITION;

            ((Node) entrada1).finalNode.addTransition(fin, '_');
            ((Node) entrada2).finalNode.addTransition(fin, '_');

            node.finalNode = fin;

            return node;
        }
        return null;
    }


    private static Node Punto(Object entrada1, Object entrada2) {
        if ((entrada1 instanceof Character) && (entrada2 instanceof Character)) {
            Node node = crearPunto_basico((Character) entrada1, (Character) entrada2);
            return node;
        }
        if ((entrada1 instanceof Character) && (entrada2 instanceof Node)) {
            Node node = new Node(NodeStatus.START);
            node.addTransition((Node) (entrada2), (Character) (entrada1));
            ((Node) (entrada2)).status = NodeStatus.TRANSITION;
            node.finalNode = ((Node) (entrada2)).finalNode;
            return node;
        }
        if ((entrada1 instanceof Node) && (entrada2 instanceof Character)) {
            Node node = ((Node) (entrada1));
            Node auxfin = new Node(NodeStatus.FINAL);
            node.finalNode.status = NodeStatus.TRANSITION;
            node.finalNode.addTransition(auxfin, ((Character) (entrada2)));
            node.finalNode = auxfin;
            return node;
        }
        if ((entrada1 instanceof Node) && (entrada2 instanceof Node)) {
            Node node = ((Node) (entrada1));
            Node auxfin = ((Node) (entrada2));
            node.finalNode.addTransition(auxfin, '_');
            node.finalNode.status = NodeStatus.TRANSITION;
            node.finalNode = auxfin.finalNode;
            auxfin.status = NodeStatus.TRANSITION;

            return node;
        }

        return null;
    }

    /**
     * Create the part of an AFND corresponding to a simple character
     *
     * @param c character of the RegEx
     * @return start node of the AFND
     */
    private static Node casoBase(Character c) {
        Node node = new Node(NodeStatus.START);
        node.addTransition(new Node(NodeStatus.FINAL), c);
        node.finalNode = node.getConnectedNode(0);
        return node;
    }

    private static Node recursion(ArrayList<Object> expression) {
        Node aux;
        //falta el parentesis
        if (expression.size() == 1) {
            aux = casoBase((Character) expression.get(0));
            expression.remove(0);
            expression.add(aux);
        }

        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i) instanceof Character) {
                if ((Character) expression.get(i) == '(') {
                    ArrayList<Object> auxiliar = parentesis(expression, i);
                    int indiceMayor = indexFinalParenthesis(expression, i);

                    for (int k = 0; k < indiceMayor - i; k++)
                        expression.remove(i);

                    expression.set(i, recursion(auxiliar));
                }
            }
        }

        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i) instanceof Character) {
                if ((Character) expression.get(i) == '*') {
                    aux = estrella(expression.get(i - 1));

                    expression.remove(i);
                    expression.remove(i - 1);
                    expression.add(i - 1, aux);
                    i--;
                }
            }
        }

        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i) instanceof Character) {
                if ((Character) expression.get(i) == '.') {
                    aux = Punto(expression.get(i - 1), expression.get(i + 1));

                    expression.remove(i + 1);
                    expression.remove(i);
                    expression.remove(i - 1);
                    expression.add(i - 1, aux);
                    i--;
                }
            }
        }

        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i) instanceof Character) {
                if ((Character) expression.get(i) == '|') {
                    aux = barra(expression.get(i - 1), expression.get(i + 1));

                    expression.remove(i + 1);
                    expression.remove(i);
                    expression.remove(i - 1);
                    expression.add(i - 1, aux);
                    i--;
                }
            }
        }
        return (Node) expression.get(0);
    }

    private static ArrayList parentesis(ArrayList lista, int index) {
        ArrayList<Object> aux = new ArrayList<>();
        int count = 0;
        if ((Character) lista.get(index) == '(') {
            for (int i = index + 1; i < lista.size(); i++) {
                if ((Character) lista.get(i) == '(')
                    count++;
                else if ((Character) lista.get(i) == ')') {
                    if (count > 0)
                        count--;
                    else
                        return aux;
                }
                aux.add(lista.get(i));
            }
        }
        return null;
    }

    private static int indexFinalParenthesis(ArrayList lista, int index) {
        int count = 0;
        if ((Character) lista.get(index) == '(') {
            for (int i = index + 1; i < lista.size(); i++) {
                if ((Character) lista.get(i) == '(')
                    count++;
                else if ((Character) lista.get(i) == ')') {
                    if (count > 0)
                        count--;
                    else
                        return i;
                }
            }
        }
        return 0;
    }

    private static void printAFND(Node startNode) {
        System.out.println("AFND:");

        String printableK = "K={";
        for (Integer i : K_AFND)
            printableK += "q" + i + ",";
        System.out.println(printableK.substring(0, printableK.length() - 1) + "}");

        String printableSigma = "Sigma={";
        for (Character c : Sigma_AFND)
            printableSigma += (c + ",");
        System.out.println(printableSigma.substring(0, printableSigma.length() - 1) + "}");

        System.out.println("Delta:");
        for (String s : Connections_AFND) {
            System.out.println(s);
        }

        System.out.println("s=q" + startNode.id);
        System.out.println("F={q" + startNode.finalNode.id + "}");
    }

    public static void main(String[] args) {
        ArrayList<Object> parsing = new ArrayList<>();
        Node graph;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String regex = "";
        try {
            regex = br.readLine();
        } catch (IOException e) {
            System.out.println("Can't read from input");
            e.printStackTrace();
            System.exit(-1);
        }

        for (int i = 0; i < regex.length(); i++) {
            parsing.add(regex.charAt(i));
            if (regex.charAt(i) != '.' && regex.charAt(i) != '|' && regex.charAt(i) != '*' && regex.charAt(i) != '_' && regex.charAt(i) != '0' && regex.charAt(i) != '-' && regex.charAt(i) != '(' && regex.charAt(i) != ')')
                Sigma_AFND.add(regex.charAt(i));
        }

        graph = recursion(parsing);
        printAFND(graph);
    }

    enum NodeStatus {
        START,
        TRANSITION,
        FINAL
    }

    public static class Node {
        int id;
        NodeStatus status;
        boolean check = false;
        ArrayList<Character> transitions;
        ArrayList<Node> connections;
        Node finalNode;

        public Node(NodeStatus status) {
            K_AFND.add(counter);
            this.id = counter;
            counter++;
            this.status = status;
            this.connections = new ArrayList<>();
            this.transitions = new ArrayList<>();
        }

        public static void changeStart(Node node1, Node node2, char transition) {
            node1.status = NodeStatus.START;
            node2.status = NodeStatus.TRANSITION;
            node1.addTransition(node2, transition);
        }

        public void addTransition(Node node, char transition) {
            Connections_AFND.add("(q" + this.id + "," + transition + ",q" + node.id + ")");
            this.transitions.add(transition);
            this.connections.add(node);
        }

        public char getTransition(int i) {
            return transitions.get(i);
        }

        public ArrayList<Character> getTransitions() {
            return transitions;
        }

        public Node getConnectedNode(int i) {
            return connections.get(i);
        }

        public boolean containsTransition(String transition) {
            return transitions.contains(transition);
        }

    }

    public static class Parse {
        Node fin;
        private Node node;
        private char c;
        private boolean isNode = false;

        public Parse(Node node) {
            this.node = this.node;
            isNode = true;
        }

        public Parse(char c) {
            this.c = c;
        }

        public boolean getNode() {
            return this.isNode;
        }

    }

}
