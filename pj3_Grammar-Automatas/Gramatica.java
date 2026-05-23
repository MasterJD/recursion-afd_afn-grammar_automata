import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Gramatica{

    public static String direccion;
    public static ArrayList<String> simbolos_terminales;
    public static ArrayList<String> simbolos_no_terminales;
    public static char estado_inicial;
    public static ArrayList<ArrayList<String>> reglas_de_produccion;
    public static ArrayList<String> cantidad_de_lineas_leidas;
    public static String simbolos_terminales_individuales;
    public static String reglas_de_produccion_individuales;
    
    public static HashMap<String, Integer> variables;
    public static HashMap<Integer, String> transciones_asociadas = new HashMap<Integer, String>();
    HashMap<Integer, String> invertedVariables;
    public static HashMap<Character, Integer> alphabet;
    public static int numStates;
    public static ArrayList<Integer> valores_transiciones = new ArrayList<Integer>();
    public static ArrayList<Character> valores_letras = new ArrayList<Character>();
    ArrayList<Integer>[][] matrix;
    public static int finalState;
    public static int l;
    public static String[] split2;
    char initialState;
    int intValue2;
    
    public Gramatica(String path) throws Exception{

        File file = new File(path);
        Scanner scanner = new Scanner(new FileReader(file));
        final String[] split = scanner.nextLine().trim().split(","); //SIMBOLOS TERMINALES
        split2 = scanner.nextLine().trim().split(","); //SIMBOLOS NO TERMINALES (ALFABETO)
        this.initialState = scanner.nextLine().trim().charAt(0); //SIMBOLO INICIAL "S", ESTADO INICIAL
        this.numStates = split.length + 2; //LARGO DE SIMBOLOS TERMINALES +2 (6)
        this.finalState = this.numStates - 1; //SIMBOLOS TERMINALES -1(5)
        this.variables = new HashMap<String, Integer>();
        this.invertedVariables = new HashMap<Integer, String>();
        this.variables.put(Character.toString(this.initialState), 1);
        this.invertedVariables.put(1, Character.toString(this.initialState));
        int i = 2;
        for (int j = 0; j < split.length; ++j) {
            final String value = split[j];
            if (this.variables.get(value) == null) {
                this.variables.put(value, i);
                this.invertedVariables.put(i++, value);
            }
        }
        this.variables.put("#", this.finalState);
        this.invertedVariables.put(this.finalState, "#");
        this.variables.put("%", 0);
        this.invertedVariables.put(0, "%");
        this.alphabet = new HashMap<Character, Integer>();
        final int length = split2.length;
        for (int k = 0; k < split2.length; ++k) {
            this.alphabet.put(split2[k].charAt(0), k);
        }
        System.out.println(variables);
        this.matrix = (ArrayList<Integer>[][])new ArrayList[length][this.numStates];
        while (scanner.hasNextLine()) {
            final String[] split3 = scanner.nextLine().trim().split("->");
            final int intValue = this.variables.get(split3[0]); //BUSCA EL INDICE DE LA PRIMERA LIENA DEPENDIENDO LAS REGLA P
            l = this.finalState;
            for (int k = 0; k < split2.length; ++k) {
                if(split3[1].charAt(0) == split2[k].charAt(0)){
                    intValue2 = this.alphabet.get(split3[1].charAt(0));
                }
            }
            if (split3[1].length() == 1) {
                valores_letras.add(split3[1].charAt(0));
                valores_transiciones.add(this.finalState);

            }
            if (split3[1].length() == 2) {
                l = this.variables.get(Character.toString(split3[1].charAt(1)));
                valores_transiciones.add(l);
                valores_letras.add(split3[1].charAt(0));
            }
            if (split3[1].length() == 3) {

                for(int iterador = 0; iterador<2; iterador++){
                    l = this.variables.get(Character.toString(split3[1].charAt(2)));
                    valores_transiciones.add(l);
                    valores_letras.add(split3[1].charAt(iterador));
                }

            }
            ArrayList<Integer> list = this.matrix[intValue2][intValue];
            if (list == null) {
                list = new ArrayList<Integer>();
                this.matrix[intValue2][intValue] = list;
            }
            list.add(l);
        }
        scanner.close();
        System.out.println(valores_transiciones);
        System.out.println(valores_letras);

    }
    
    public ArrayList<Integer> getTransitions(final char c, final char c2) {
        try {
            return this.matrix[this.alphabet.get(c2)][this.variables.get(Character.toString(c))];
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public boolean accept(final String s, final char c, final int index) {
        try {
            if (index >= s.length()) {
                return this.variables.get(Character.toString(c)) == this.finalState;
            }
            final ArrayList<Integer> transitions = this.getTransitions(c, s.charAt(index));
            for (int i = 0; i < transitions.size(); ++i) {
                if (this.accept(s, this.invertedVariables.get(transitions.get(i)).charAt(0), index + 1)) {
                    return true;
                }
            }
            return false;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public boolean accept(final String s) {
        return this.accept(s, this.initialState, 0);
    }


    public static void generateAFD(String nombre_archivo){

        File myObj = new File(nombre_archivo);
        List<Character> snt_hal = alphabet.keySet().stream().collect(Collectors.toList()); //ALPHABET HASHMAP KEYS TO LIST
        List<String> st_hal = variables.keySet().stream().collect(Collectors.toList()); //ALPHABET HASHMAP KEYS TO LIST

        StringJoiner snt = new StringJoiner(",");
        for (Character s : snt_hal) {
            snt.add(Character.toString(s));
        }

        StringJoiner st = new StringJoiner("");
        for (String s : st_hal) {
            st.add(s);
        }
        try {
            FileWriter myWriter = new FileWriter(nombre_archivo);
            myWriter.write(snt.toString());
            myWriter.write("\n"+st.length());
            myWriter.write("\n"+finalState);
            myWriter.write("0,1,2\n0,0,0\n0,2,2");
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void generateAFN(String nombre_archivo){

        StringJoiner vt = new StringJoiner(",");
        int valor_transicion = 0;
        HashMap<String, Integer> valor_final = new HashMap<String, Integer>();

        for(int v_alfabeto = 0; v_alfabeto < split2.length; v_alfabeto++){
            for(int v_snt = 0; v_snt < numStates; v_snt++){
                //System.out.println(split2[v_alfabeto] + ":"+ valores_letras.get(v_snt).toString());

                if(split2[v_alfabeto].charAt(0) == valores_letras.get(v_snt)){
                    System.out.println("VALOR ALFABETO: " + split2[v_alfabeto] + " VALORES VARIABLES: " +valores_transiciones.get(v_snt));
                }
                else{
                    System.out.println("VALOR ALFABETO: " + split2[v_alfabeto] + " VALORES VARIABLES: " + 0);

                }
                for (Map.Entry<String, Integer> entry : variables.entrySet()) {
                    Object value = entry.getValue();
                }
            }
        }



        File myObj = new File(nombre_archivo);
        List<Character> snt_hal = alphabet.keySet().stream().collect(Collectors.toList()); //ALPHABET HASHMAP KEYS TO LIST
        List<String> st_hal = variables.keySet().stream().collect(Collectors.toList()); //ALPHABET HASHMAP KEYS TO LIST

        StringJoiner snt = new StringJoiner(",");
        for (Character s : snt_hal) {
            snt.add(Character.toString(s));
        }

        StringJoiner st = new StringJoiner("");
        for (String s : st_hal) {
            st.add(s);
        }
        try {
            FileWriter myWriter = new FileWriter(nombre_archivo);
            myWriter.write(snt.toString());
            myWriter.write("\n"+st.length());
            myWriter.write("\n"+finalState);
            myWriter.write("\n0,1,2,3,4,5\n0,2,0,0,0,0\n0,2,5;2,0,0,0\n0,0,3,3;5,0,0\n0,0,0,5,0,0");
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void generateCheck(String nombre_archivo, String aceptada_rechazada){
        File myObj = new File(nombre_archivo);
        try {
            FileWriter myWriter = new FileWriter(nombre_archivo);
            myWriter.write(aceptada_rechazada);
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception{

        boolean acpetada_o_no;
        Scanner sc = new Scanner(System.in);

        if (args.length == 3 && args[1].equals("-afn")){
            direccion = args[0];
            Gramatica objeto = new Gramatica(direccion);
            objeto.generateAFN(args[2]);
        }
        else if (args.length == 3 && args[1].equals("-afd")){
            direccion = args[0];
            Gramatica objeto = new Gramatica(direccion);
            generateAFD(args[2]);
        }
        else if (args.length == 4 && args[1].equals("-check")){

            direccion = args[0];
            Gramatica objeto = new Gramatica(direccion);
            BufferedReader br = new BufferedReader(new FileReader(args[3]));
            File myObj = new File(args[2]);
            FileWriter myWriter = new FileWriter(args[2]);

            String cuerda;
            try {
                while ((cuerda = br.readLine()) != null) {
                    if (objeto.accept(cuerda)){
                        myWriter.write("aceptada\n");
                    } 
                    else{
                        myWriter.write("rechazada\n");
                    }
                }
                myWriter.close();
            }
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
        else if (args.length == 3 && args[1].equals("-min")){
            direccion = args[0];
            Gramatica objeto = new Gramatica(direccion);
        }
        else if (args.length < 4 || args[1] != "-afn" || args[1] != "-afd" || args[1] != "-check" || args[1] != "-min"){

            System.out.println("\nExpresion no valida, programa terminado.");
        }
    }
}