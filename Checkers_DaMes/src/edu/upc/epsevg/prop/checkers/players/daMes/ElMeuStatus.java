
package edu.upc.epsevg.prop.checkers.players.daMes;

import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.PlayerType;
import java.util.Random;

/**
 *
 * @author @author Samuel i Oriol (DA+)
 */
public class ElMeuStatus extends GameStatus {
    
    int hpp[][][] = new int[8][8][4];
    int p2move; //diferenciador
    int hash;
    
    public ElMeuStatus(int [][] tauler){
        super(tauler);
    }
    
     public ElMeuStatus(GameStatus gs){
        super(gs);
        init_Zobrist();
    }
    
    public void init_Zobrist(){
        for (int i = 0; i < this.getSize(); ++i){
            for (int j = 0; j < this.getSize(); ++j){
                for (int k = 0; k < 4; ++k){
                    Random rand = new Random();
                    hpp[i][j][k] = rand.nextInt();
                }
                
            }
        }
        Random rand = new Random();
        p2move = rand.nextInt();
    }
    
    public void getHashCode(){
        PlayerType jugadorTurn = this.getCurrentPlayer();
        int h = 0;
        for (int i = 0; i < this.getSize(); ++i){
            for (int j = 0; j < this.getSize(); ++j){
                if (this.getPos(i,j) != CellType.EMPTY){
                    int c = numFitxa(this.getPos(i,j));
                    h = h ^ hpp[i][j][c];
                }
            }
        }
        if (jugadorTurn == PlayerType.PLAYER2) h = h ^ p2move;
        
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
