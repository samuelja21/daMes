
package edu.upc.epsevg.prop.checkers.players.daMes;

import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.PlayerType;

import edu.upc.epsevg.prop.checkers.players.daMes.Zobrist;
import java.awt.Point;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Samuel i Oriol (DA+)
 */
public class ElMeuStatus extends GameStatus {

    private int hash;
    private Zobrist z;
    
    public ElMeuStatus(int [][] tauler){
        super(tauler);
    }
    
     public ElMeuStatus(GameStatus gs){
        super(gs);
    }
    
    public ElMeuStatus(ElMeuStatus gs){
        super(gs);
        hash = gs.hash;
        z = gs.z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ElMeuStatus other = (ElMeuStatus) obj;
        if (this.hash != other.hash) {
            return false;
        }
        return Objects.equals(this.z, other.z);
    }
    
    public void movePiece(List<Point> path){
        
        int x = path.get(0).x;
        int y = path.get(0).y;
        
        int c = numFitxa(this.getPos(x,y));
        
        hash = hash ^ z.hpp[x][y][c];
        
        for (int i = 1; i < path.size(); ++i){
            int x2 = path.get(i).x;
            int y2 = path.get(i).y; 
            if (i == path.size()-1) hash = hash ^ z.hpp[x2][y2][c];
            
            if (x2 == x + 2 || x2 == x - 2){
            //si es salt
                int xx = x+1;
                if (x2 < x) xx = x-1;
                
                int yy = y+1;
                if (y2 < y) yy = y-1;
                
                int cc = numFitxa(this.getPos(xx,yy));
                
                hash = hash ^ z.hpp[xx][yy][cc];
            }
        }
        
        hash = hash ^ z.p2move;
        
        super.movePiece(path);
        //getHashCode();
    }

    public void init_Zobrist(Zobrist z1){
        z = z1;
        getHashCode();
    }
    
    public void getHashCode(){
        PlayerType jugadorTurn = this.getCurrentPlayer();
        int h = 0;
        for (int i = 0; i < this.getSize(); ++i){
            for (int j = 0; j < this.getSize(); ++j){
                if (this.getPos(i,j) != CellType.EMPTY){
                    int c = numFitxa(this.getPos(i,j));
                    h = h ^ z.hpp[i][j][c];
                }
            }
        }
        if (jugadorTurn == PlayerType.PLAYER2) h = h ^ z.p2move;
        
        hash = h;
    }
    
    public int hashCode(){
        return hash;
    }
    
    public int numFitxa(CellType c){
        int v = 0;
        if (c == CellType.P1) v = 0;
        if (c == CellType.P1Q) v = 1;
        if (c == CellType.P2) v = 2;
        if (c == CellType.P2Q) v = 3;
        return v;
    }
     
}
