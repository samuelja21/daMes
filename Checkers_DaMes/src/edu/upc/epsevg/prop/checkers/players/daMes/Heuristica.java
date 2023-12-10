/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.checkers.players.daMes;

import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author c3895969
 */
public class Heuristica {
    
    private GameStatus tauler;
    private PlayerType jugador;
    
    public Heuristica(GameStatus s, PlayerType p) {
        this.tauler = s;
        this.jugador = p;
    }
    
    public int fitxesVeines(int x, int y, PlayerType player){
        int f = 0;
        if (x > 1){
            if (tauler.getPos(x-2, y).getPlayer() == player) ++f;
            if (y < (tauler.getSize() - 2) && tauler.getPos(x-2, y+2).getPlayer() == player) ++f;
            if (y > 1 && tauler.getPos(x-2, y-2).getPlayer() == player) ++f;
        }
        if (x < tauler.getSize()-2){
            if (tauler.getPos(x+2, y).getPlayer() == player) ++f;
            if (y < (tauler.getSize() - 2) && tauler.getPos(x+2, y+2).getPlayer() == player) ++f;
            if (y > 1 && tauler.getPos(x+2, y-2).getPlayer() == player) ++f;
        }
        
        return f;
    }
    
    public int valor_heuristica(){
        int h = 0;
        if (tauler.checkGameOver()){
        //algu guanya
            if (tauler.GetWinner() == jugador) return Integer.MAX_VALUE-1;
            else return Integer.MIN_VALUE+1;
        }
        else if (tauler.isGameOver()){
        //empat
            return 0;
        }
        else {
        //no ha acabat la partida
        
            for (int j = 0; j < tauler.getSize(); ++j){
                for (int i = 0; i < tauler.getSize(); ++i){
                    CellType casella_actual = tauler.getPos(i,j);
                    if (casella_actual != CellType.EMPTY){
                    //Si la casella que estem visitant té una fitxa
                        PlayerType player = casella_actual.getPlayer();
                        MoveNode moviments = tauler.getMoves(new Point(i,j), player);
                        
                        if (player == jugador){
                            if (casella_actual.isQueen()) h += 5;
                            else{ 
                                h += 1; //Suma 1 fitxa
                                if (i > 0 && i < (tauler.getSize() - 1)) h+=2; //Bonificació per posicions intermitges
                                if (tauler.getYDirection(player) == 1 && j > tauler.getSize()/4) h += 2; //Bonificació per aproparse al final
                                if (tauler.getYDirection(player) == -1 && j < tauler.getSize()*3/4) h += 2; //Bonificació per aproparse al final
                            }
                            if (!moviments.getChildren().isEmpty() && moviments.getChildren().get(0).isJump()) h += 5; //Suma 5 per cada fitxa que pugui matar
                            h += moviments.getChildren().size(); //Suma els moviments possibles
                        }
                        else {
                            if (casella_actual.isQueen()) h -= 6;
                            else{ 
                                h -= 1; 
                                if (i > 0 && i < (tauler.getSize() - 1)) h-=2;
                                if (tauler.getYDirection(player) == 1 && j > tauler.getSize()/4) h -= 2; 
                                if (tauler.getYDirection(player) == -1 && j < tauler.getSize()*3/4) h -= 2;
                            }
                            if (!moviments.getChildren().isEmpty() && moviments.getChildren().get(0).isJump()) h -= 6;
                            h -= moviments.getChildren().size();
                        }
                    }
                }
            }
        }

        return h;
    }
    
}
