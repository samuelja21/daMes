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
    
     /**
     * Retorna true quan hi ha una fitxa del jugador a la casella del paràmetre.
     *
     * @param casella Casella que volem comprovar.
     * @return cert si a la casella hi ha una fitxa del nostre jugador.
     */
    public boolean fitxaJugador(CellType casella){
        boolean fitxaPlayer = false;
        if (jugador == PlayerType.PLAYER1){
            fitxaPlayer = casella == CellType.P1 || casella == CellType.P1Q;
        }
        else {
            fitxaPlayer = casella == CellType.P2 || casella == CellType.P2Q;
        }
        return fitxaPlayer;
    }
    
     /**
     * Retorna true quan hi ha una reina a la casella del paràmetre.
     *
     * @param casella Casella que volem comprovar.
     * @return cert si a la casella hi ha una reina.
     */
    public boolean esReina (CellType casella){
        return (casella == CellType.P2Q || casella == CellType.P1Q);
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
        
            for (int i = 0; i < tauler.getSize(); ++i){
                for (int j = 0; j < tauler.getSize(); ++j){
                    CellType casella_actual = tauler.getPos(i,j);
                    if (casella_actual != CellType.EMPTY){
                    //Si la casella que estem visitant té una fitxa
                        PlayerType player = casella_actual.getPlayer();
                        MoveNode moviments = tauler.getMoves(new Point(i,j), player);
                        if (player == jugador){
                            if (casella_actual.isQueen()) h += 4;
                            else h += 1;
                            if (moviments.isJump()) h += 2;
                        }
                        else {
                            if (casella_actual.isQueen()) h -= 4;
                            else h -= 1;
                            if (moviments.isJump()) h -= 2;
                        }
                        
                        
                    /*
                        PlayerType player = fitxaCasella(casella_actual);
                        MoveNode moviments = tauler.getMoves(new Point(i,j), player);
                        if (!moviments.getChildren().isEmpty()) {
                            if (player == jugador){
                                h += moviments.getChildren().size();
                            }
                            else {
                                h -= moviments.getChildren().size();
                            }
                            
                        }
                        */        
                        
                    }
                    
                }
            }
        }

        return h;
    }
    
}
