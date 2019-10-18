import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class ProyectoMalf {

    private static HashSet<Character> Sigma = new HashSet<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String regex;
        regex = sc.nextLine();

        ArrayList<Object> parsedRegex = new ArrayList<>();
        for (int i = 0; i < regex.length(); i++) {
            parsedRegex.add(regex.charAt(i));
            if (regex.charAt(i) != '.' && regex.charAt(i) != '|' && regex.charAt(i) != '*' && regex.charAt(i) != '_' && regex.charAt(i) != '0' && regex.charAt(i) != '-' && regex.charAt(i) != '(' && regex.charAt(i) != ')') {
                Sigma.add(regex.charAt(i));
            }
        }

        // AFND
        AFND afnd = new AFND(parsedRegex);
        System.out.println(afnd);

        // AFD
        //generateAFD(afnd);
        AFD afd = new AFD(afnd);
        System.out.println(afd);

        String text = "";
        while (sc.hasNextLine())
            text += sc.nextLine();

        System.out.println("Ocurrencias:");
        System.out.println(checkOcurrencies(afd, text));
    }

    private static String checkOcurrencies(AFD afd, String text) {
        String s = "", currentLine = "";
        int lineId = 1, positionInLine = 1;
        NodeAFD currentNode = AFD.s;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '-') {
                if (!currentLine.equals(""))
                    s += "linea " + lineId + ": " + currentLine + "\n";
                positionInLine = 1;
                lineId++;
                currentLine = "";
            } else if (!afd.containsInSigma(text.charAt(i))) {
                currentNode = AFD.s;
                positionInLine++;
            } else {
                currentNode = currentNode.nextNode(text.charAt(i));
                if (currentNode.status == NodeStatus.FINAL)
                    currentLine += positionInLine + " ";
                positionInLine++;
            }
        }
        if (!currentLine.equals(""))
            s += "linea " + lineId + ": " + currentLine + "\n";
        if (s.equals(""))
            s += "Ninguna\n";
        return s;
    }

    enum NodeStatus {
        START,
        TRANSITION,
        FINAL
    }

    public static class AFND {
        static int counter = 0;
        static ArrayList<Node> k = new ArrayList<>();
        static Object[] sigma;
        static ArrayList<Transition> delta = new ArrayList<>();
        static Node s;
        static Node f;

        AFND(ArrayList<Object> parsedRegex) {
            sigma = Sigma.toArray();
            s = generateAFND(parsedRegex);
            f = s.finalNode;
        }

        private static Node crearEstrella_Basica(char caracter) {
            Node startNode = new Node(NodeStatus.START);
            startNode.addTransition(new Node(NodeStatus.TRANSITION), '_');
            startNode.transitions.get(0).end.addTransition(new Node(NodeStatus.TRANSITION), caracter);
            startNode.transitions.get(0).end.transitions.get(0).end.addTransition(new Node(NodeStatus.FINAL), '_');
            startNode.transitions.get(0).end.transitions.get(0).end.addTransition(startNode.transitions.get(0).end, '_');
            startNode.addTransition(startNode.transitions.get(0).end.transitions.get(0).end.transitions.get(0).end, '_');
            startNode.finalNode = startNode.transitions.get(0).end.transitions.get(0).end.transitions.get(0).end;
            return startNode;
        }

        private static Node crearBarra_Basica(char c1, char c2) {
            Node startNode = new Node(NodeStatus.START);
            Node endNode = new Node(NodeStatus.FINAL);

            startNode.addTransition(new Node(NodeStatus.TRANSITION), '_');
            startNode.addTransition(new Node(NodeStatus.TRANSITION), '_');

            startNode.transitions.get(0).end.addTransition(new Node(NodeStatus.TRANSITION), c1);
            startNode.transitions.get(1).end.addTransition(new Node(NodeStatus.TRANSITION), c2);

            startNode.transitions.get(0).end.transitions.get(0).end.addTransition(endNode, '_');
            startNode.transitions.get(1).end.transitions.get(0).end.addTransition(endNode, '_');

            startNode.finalNode = endNode;

            return startNode;
        }

        private static Node crearPunto_basico(char caracter1, char caracter2) {
            Node node = new Node(NodeStatus.START);
            node.addTransition(new Node(NodeStatus.TRANSITION), caracter1);
            node.transitions.get(0).end.addTransition(new Node(NodeStatus.FINAL), caracter2);
            node.finalNode = node.transitions.get(0).end.transitions.get(0).end;
            return node;
        }

        private static Node estrella(Object entrada) {
            Node node;
            if (entrada instanceof Node) {
                node = new Node(NodeStatus.START);
                Node fin = new Node(NodeStatus.FINAL);

                node.addTransition(((Node) (entrada)), '_');
                ((Node) (entrada)).finalNode.addTransition(fin, '_');

                ((Node) entrada).finalNode.addTransition((Node) (entrada), '_');
                ((Node) entrada).status = NodeStatus.TRANSITION;
                ((Node) entrada).finalNode.status = NodeStatus.TRANSITION;

                node.addTransition(fin, '_');
                node.finalNode = fin;
            } else {
                node = crearEstrella_Basica((Character) entrada);
            }
            return node;
        }

        private static Node barra(Object entrada1, Object entrada2) {
            if ((entrada1 instanceof Character) && (entrada2 instanceof Character))
                return crearBarra_Basica((Character) entrada1, (Character) entrada2);

            if ((entrada1 instanceof Character) && (entrada2 instanceof Node)) {

                Node node = new Node(NodeStatus.START);
                Node fin = new Node(NodeStatus.FINAL);
                //caracter
                node.addTransition(new Node(NodeStatus.TRANSITION), '_');
                node.transitions.get(0).end.addTransition(new Node(NodeStatus.TRANSITION), (Character) entrada1);
                node.transitions.get(0).end.transitions.get(0).end.addTransition(fin, '_');

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
                node.transitions.get(0).end.addTransition(new Node(NodeStatus.TRANSITION), (Character) entrada2);
                node.transitions.get(0).end.transitions.get(0).end.addTransition(fin, '_');

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
            if ((entrada1 instanceof Character) && (entrada2 instanceof Character))
                return crearPunto_basico((Character) entrada1, (Character) entrada2);

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

        //UWUWUWUWUWUUWUWWUWUWUUWUWUWUWUWUWUWUUWUWUWUWUWUWUWUWUUWUUWUUWUUWUUWUUWUW
        ///AASDKAKDAKSDASGDUASDILAUSDIAYDSADILAOGRWQDUIHAWUDHAOS
        //HACKEADO
        // juguemo lolcito
        private static Node casoBase(Character c) {
            Node node = new Node(NodeStatus.START);
            node.addTransition(new Node(NodeStatus.FINAL), c);
            node.finalNode = node.transitions.get(0).end;
            return node;
        }

        private static Node generateAFND(ArrayList<Object> expression) {
            Node aux;
            if (expression.size() == 1) {
                aux = casoBase((Character) expression.get(0));
                expression.remove(0);
                expression.add(aux);
            }

            for (int i = 0; i < expression.size(); i++) {
                if (expression.get(i) instanceof Character && (Character) expression.get(i) == '(') {
                    ArrayList<Object> auxiliar = parenthesis(expression, i);
                    if (auxiliar.isEmpty()) {
                        System.out.println("Expresión incorrecta");
                        System.exit(0);
                    }
                    int indiceMayor = indexFinalParenthesis(expression, i);

                    for (int k = 0; k < indiceMayor - i; k++)
                        expression.remove(i);

                    expression.set(i, generateAFND(auxiliar));
                }
            }

            for (int i = 0; i < expression.size(); i++) {
                if (expression.get(i) instanceof Character && (Character) expression.get(i) == '*') {
                    aux = estrella(expression.get(i - 1));

                    expression.remove(i);
                    expression.remove(i - 1);
                    expression.add(i - 1, aux);
                    i--;
                }
            }

            for (int i = 0; i < expression.size(); i++) {
                if (expression.get(i) instanceof Character && (Character) expression.get(i) == '.') {
                    aux = Punto(expression.get(i - 1), expression.get(i + 1));

                    expression.remove(i + 1);
                    expression.remove(i);
                    expression.remove(i - 1);
                    expression.add(i - 1, aux);
                    i--;
                }
            }

            for (int i = 0; i < expression.size(); i++) {
                if (expression.get(i) instanceof Character && (Character) expression.get(i) == '|') {
                    aux = barra(expression.get(i - 1), expression.get(i + 1));

                    expression.remove(i + 1);
                    expression.remove(i);
                    expression.remove(i - 1);
                    expression.add(i - 1, aux);
                    i--;
                }
            }
            Node s = (Node) expression.get(0);
            for (Object c : sigma) {
                s.addTransition(s, (Character) c);
            }
            return (Node) expression.get(0);
        }

        private static ArrayList parenthesis(ArrayList list, int index) {
            ArrayList<Object> aux = new ArrayList<>();
            int count = 0;
            if ((Character) list.get(index) == '(') {
                for (int i = index + 1; i < list.size(); i++) {
                    if ((Character) list.get(i) == '(')
                        count++;
                    else if ((Character) list.get(i) == ')') {
                        if (count > 0)
                            count--;
                        else
                            return aux;
                    }
                    aux.add(list.get(i));
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

        public String toString() {
            String s = "AFND:\n";

            s += "K={";
            for (Node node : k)
                s += node + ",";
            s = s.substring(0, s.length() - 1) + "}\n";

            s += "Sigma={";
            for (Object c : sigma)
                s += (c + ",");
            s = s.substring(0, s.length() - 1) + "}\n";

            s += "Delta:\n";
            for (Transition t : delta)
                if (t.transitionCharacter != '0')
                    s += t.toString() + "\n";

            s += "s=" + AFND.s + "\n";
            s += "F={" + f + "}\n";

            return s;
        }

    }

    public static class Node {
        int id;
        NodeStatus status;
        ArrayList<Transition> transitions;
        Node finalNode;

        Node(NodeStatus status) {
            this.id = AFND.counter;
            AFND.counter++;
            this.status = status;
            this.transitions = new ArrayList<>();
            AFND.k.add(this);
        }

        void addTransition(Node node, char transition) {
            Transition t = new Transition(this, transition, node);
            this.transitions.add(t);
            AFND.delta.add(t);
        }

        public String toString() {
            return "q" + this.id;
        }

    }

    public static class Transition {
        Node start, end;
        char transitionCharacter;

        Transition(Node start, char transitionCharacter, Node end) {
            this.start = start;
            this.transitionCharacter = transitionCharacter;
            this.end = end;
        }

        public String toString() {
            return "(" + this.start + "," + this.transitionCharacter + "," + this.end + ")";
        }
    }

    public static class AFD {
        static int counter = 0;
        static ArrayList<NodeAFD> k = new ArrayList<>();
        static Object[] sigma;
        static ArrayList<TransitionAFD> delta = new ArrayList<>();
        static NodeAFD s;
        static ArrayList<NodeAFD> f = new ArrayList<>();

        AFD(AFND afnd) {
            sigma = Sigma.toArray();
            s = new NodeAFD(eClossure(AFND.s, '_'), NodeStatus.START);
            k.add(s);

            for (int i = 0; i < k.size(); i++) {
                for (Object c : sigma) {
                    HashSet<Node> hs = new HashSet<>();
                    for (Node node : k.get(i).nodesAFND) {
                        hs.addAll(eClossure(node, (Character) c));
                        for (Object nodeForEClossure : hs.toArray())
                            hs.addAll(eClossure((Node) nodeForEClossure, '_'));
                    }
                    NodeAFD previousCreated = wasAlreadyCreated(hs);
                    if (previousCreated == null) {
                        NodeAFD newNode = new NodeAFD(hs, NodeStatus.TRANSITION);
                        k.add(newNode);
                        TransitionAFD newTransition = new TransitionAFD(k.get(i), (Character) c, newNode);
                        k.get(i).transitions.add(newTransition);
                        delta.add(newTransition);
                    } else {
                        TransitionAFD newTransition = new TransitionAFD(k.get(i), (Character) c, previousCreated);
                        k.get(i).transitions.add(newTransition);
                        delta.add(newTransition);
                    }
                }
            }
            for (NodeAFD node : k) {
                if (isFinal(node)) {
                    node.status = NodeStatus.FINAL;
                    f.add(node);
                }
            }
        }

        HashSet<Node> eClossure(Node node, char c) {
            HashSet<Node> nodes = new HashSet<>();
            if (c == '_')
                nodes.add(node);
            for (Transition t : node.transitions) {
                if (t.transitionCharacter == c) {
                    nodes.add(t.end);
                    if (c == '_')
                        nodes.addAll(eClossure(t.end, c));
                }
            }
            return nodes;
        }

        NodeAFD wasAlreadyCreated(HashSet<Node> hs) {
            for (NodeAFD n : k) {
                if (n.nodesAFND.containsAll(hs) && hs.containsAll(n.nodesAFND))
                    return n;
            }
            return null;
        }

        boolean isFinal(NodeAFD node) {
            return node.nodesAFND.contains(AFND.f);
        }

        boolean containsInSigma(char c) {
            for (Object sigmaC : sigma)
                if ((char) sigmaC == c)
                    return true;
            return false;
        }

        public String toString() {
            String s = "AFD:\n";

            s += "K={";
            for (NodeAFD node : k)
                s += node + ",";
            s = s.substring(0, s.length() - 1) + "}\n";

            s += "Sigma={";
            for (Object c : sigma)
                s += (c + ",");
            s = s.substring(0, s.length() - 1) + "}\n";

            s += "Delta:\n";
            for (TransitionAFD t : delta)
                s += t.toString() + "\n";

            s += "s=" + AFD.s + "\n";
            s += "F={";
            for (NodeAFD node : f)
                s += (node + ",");
            s = s.substring(0, s.length() - 1) + "}\n";

            return s;
        }
    }

    public static class NodeAFD {
        int id;
        ArrayList<Node> nodesAFND = new ArrayList<>();
        NodeStatus status;
        ArrayList<TransitionAFD> transitions = new ArrayList<>();

        NodeAFD(HashSet<Node> nodesAFND, NodeStatus status) {
            this.id = AFD.counter;
            AFD.counter++;
            this.nodesAFND.addAll(nodesAFND);
            this.status = status;
        }

        NodeAFD nextNode(Character c) {
            NodeAFD next = null;
            for (TransitionAFD transition : transitions) {
                if (transition.transitionCharacter == c)
                    next = transition.end;
            }
            return next;
        }

        public String toString() {
            return "q" + this.id;
        }
    }

    public static class TransitionAFD {
        NodeAFD start, end;
        char transitionCharacter;

        TransitionAFD(NodeAFD start, char transitionCharacter, NodeAFD end) {
            this.start = start;
            this.transitionCharacter = transitionCharacter;
            this.end = end;
        }

        public String toString() {
            return "(" + this.start + "," + this.transitionCharacter + "," + this.end + ")";
        }
    }

}
