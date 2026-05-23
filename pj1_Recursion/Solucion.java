import java.util.LinkedList;
import java.io.*;

public class Solucion{

    public int actual_pos, movements, north, south, east, west;
    public String iz = "[", coma = ", ", de  = "]";
    public LinkedList<String> lista = new LinkedList<String>();
    public String[] solves = new String[lista.size()];
    public StringBuilder sb;

    public Solucion(Maze maze){

        this.actual_pos = 0;
        this.movements = 0;
        this.north = maze.moveNorth(actual_pos);
        this.south = maze.moveSouth(actual_pos);
        this.east = maze.moveEast(actual_pos);
        this.west = maze.moveWest(actual_pos);

    }

    public void ValoresNeso(Maze maze, int movements,int north, int south, int east, int west, int actual_pos, String camino){

        if(movements!=maze.getMaxMoves()){
            Laberint(maze, movements, maze.moveNorth(actual_pos), actual_pos, camino);
            Laberint(maze, movements, maze.moveSouth(actual_pos), actual_pos, camino);
            Laberint(maze, movements, maze.moveEast(actual_pos), actual_pos, camino);
            Laberint(maze, movements, maze.moveWest(actual_pos), actual_pos, camino);
        }
    }
    public void Laberint(Maze maze, int movements, int neso, int actual_pos, String camino){

        if(neso == maze.getExitSpace() && neso != actual_pos){
            sb = new StringBuilder(camino);
            sb.append(Integer.toString(neso)).append(de);
            lista.add(sb.toString());
        }

        if(neso != maze.getExitSpace() && neso != actual_pos){
            sb = new StringBuilder(camino);
            sb.append(Integer.toString(neso)).append(coma);
            movements++;
            ValoresNeso(maze, movements, north, south, east, west, neso, sb.toString());
        }
    }

    public String[] respuesta(Maze maze)throws Exception{

        this.sb = new StringBuilder(iz);
        this.sb.append(maze.getStartSpace()).append(coma);
        ValoresNeso(maze, movements, north, south, east, west, maze.getStartSpace(), sb.toString());
        this.solves = lista.toArray(solves);
        return solves;
    }
}