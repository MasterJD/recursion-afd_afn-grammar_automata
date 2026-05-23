/*
    Esta es su clase principal. El unico metodo que debe implementar es
    public String[] solve(Maze maze)
    pero es libre de crear otros metodos y clases en este u otro archivo que desee.
*/
public class Solver{

    public Solver(){
        //Sientase libre de implementar el contructor de la forma que usted lo desee
    }

    public String[] solve(Maze maze)throws Exception{
        //Implemente su metodo aqui. Sientase libre de implementar métodos adicionales
        Solucion solution = new Solucion(maze);
        return solution.respuesta(maze);
    }

}