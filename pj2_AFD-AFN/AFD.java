import java.io.*; 
import java.util.*;
import java.util.ArrayList;

public class AFD{

	public static int estados;
   	public static Hashtable<Character, Integer> simbolos = new Hashtable<Character, Integer>();
	public static String st;
	public static List<String> estado_final;
	public static int[][] matriz_transiciones;
	public static String[] transiciones_individuales;
	public static String[] simbolos_individuales;

	public static String direccion;
	
	public AFD(String path)throws Exception{

		File file = new File(path); //"pj2/tests/afd/hex.afd"
		BufferedReader br = new BufferedReader(new FileReader(file));

//--------------------Simbolos----------------------------------

		st = br.readLine();
		StringTokenizer tokenizer1 = new StringTokenizer(st, ",");
		simbolos_individuales = st.split(",");
        for(int i = 0; i<simbolos_individuales.length; i++){
        	simbolos.put(simbolos_individuales[i].charAt(0), i);
        }
//---------------------Estados----------------------------
  		st = br.readLine();
  		estados = Integer.parseInt(st);

//---------------------ESTADOS FINALES----------------------
		st = br.readLine();
		StringTokenizer tokenizer2 = new StringTokenizer(st, ",");
		estado_final = new ArrayList<String>();

//--------------unico estado final--------------------------

		if(st.length()==1){
			estado_final.add(st);
		}
//--------------en caso hayan mas de un estado final---------
		else if(st.length()>1){
			while(tokenizer2.hasMoreTokens()){
				estado_final.add(tokenizer2.nextToken());
			}
		}

//------Resetear file del afd, para asi luego leer los elementos de las transiciones---
  	
  		BufferedReader br2 = new BufferedReader(new FileReader(file)); 
  		for(int i=0; i<3; i++){
    		st = br2.readLine();
  		}

//---------------------------Datos a la matriz-----------------------------------
  		matriz_transiciones = new int[simbolos_individuales.length][estados];
  		int iterador = 0;
        while ((st = br2.readLine()) != null) {

            transiciones_individuales =  st.split(",");
            for(int i = 0; i<transiciones_individuales.length; i++){
            	matriz_transiciones[iterador][i] = Integer.parseInt(transiciones_individuales[i]);
            }
            iterador++;

        }

	}

	public int getTransition(int currentState, char symbol) throws Exception{

		return this.matriz_transiciones[simbolos.get(symbol)][currentState];
	}

	public boolean accept(String string) throws Exception{
		int estado = 1;
		for (int indice = 0; indice < string.length();indice++) {
			estado = getTransition(estado,string.charAt(indice));
			if(estado == 0){
				return false;
			}
		}

		char ayu;
		for (int indice = 0; indice < estado_final.size();indice++) {
			ayu = (estado_final.get(indice)).charAt(0);
			if (Character.getNumericValue(ayu)== estado){
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) throws Exception{
		boolean acpetada_o_no;
		Scanner sc = new Scanner(System.in);

		if (args.length == 2 && args[1].equals("-i")){
			direccion = args[0];

			AFD objeto = new AFD(direccion);
			
			System.out.print("Ingrese una cuerda a evaluar: ");
			String cuerda =sc.nextLine();

			if(cuerda.isEmpty()){
				System.out.println("\nNo ingresó cuerda, programa terminado.");	
				System.exit(0);		
			}
			else{
	          	System.out.print("Evaluando cuerda: " + cuerda);
	            acpetada_o_no = objeto.accept(cuerda);
	            
	              if ( acpetada_o_no== true){
				 	System.out.print("\nCuerda Aceptada.");
					
				}
				else{
				 	System.out.print("\nCuerda No Aceptada.");
				}
            }

			while(cuerda != null){
				
				System.out.println("\n");

				System.out.print("\nIngrese una cuerda a evaluar: ");
	            cuerda =sc.nextLine();

	            if(cuerda.isEmpty()){
				    System.out.println("\nNo ingresó cuerda, programa terminado.");			
					System.exit(0);
            	}

	            System.out.println("Evaluando cuerda: " + cuerda);
	            acpetada_o_no = objeto.accept(cuerda);
	            
	            if ( acpetada_o_no== true){
				 	System.out.print("Cuerda Aceptada.");
					
				}
				else{
				 	System.out.print("Cuerda No Aceptada.");
				}		
			}
        }

        
        else if (args.length == 3 && args[1].equals("-f")){
        	direccion = args[0];
        	String archivo2 = args[2];
        	//System.out.println("Procesando Archivo...");
        	
        	File file = new File(archivo2); //"pj2/tests/afd/hex.afd"
			BufferedReader br = new BufferedReader(new FileReader(file));

        	AFD objeto = new AFD(direccion);
        	
        	while ((st = br.readLine()) != null) {
        		System.out.println("--------------------------------------------------");
				System.out.print("\nCuerda que se analizó: ");
				System.out.println(st);
				System.out.print("\nRESULTADO: ");
				acpetada_o_no = objeto.accept(st);
				if ( acpetada_o_no== true){
				 	System.out.print("\nCuerda Aceptada."+"\n");
					
				}else{
				 	System.out.print("\nCuerda No Aceptada."+"\n");
				 }
				System.out.println("--------------------------------------------------");
				System.out.println("");
		
  			}
        } 
        else if (args.length < 2 || args[1] != "-i" || args[1] != "-f"){

        	System.out.println("\nExpresión no válida, programa terminado.");
        }      	
	}
	
}