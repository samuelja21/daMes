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
        int[] fitxes = new int[2];
        int[] reines = new int[2];
        int[] ultima_fila = new int[2];
        int[] extrems_centre = new int[2];
        int[] centre_tauler = new int[2];
        int[] salts = new int[2];
        int[] protegides = new int[2];
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
            List<Point> fitxesSaltables = new ArrayList<>();
            PlayerType rival = PlayerType.PLAYER2;
            if (jugador == PlayerType.PLAYER2) rival = PlayerType.PLAYER1;
            protegides[0] = tauler.getScore(jugador);
            protegides[1] = tauler.getScore(rival);
            h += tauler.getScore(jugador);
            h -= tauler.getScore(rival);
            for (int j = 0; j < tauler.getSize(); ++j){
                for (int i = 0; i < tauler.getSize(); ++i){
                    CellType casella_actual = tauler.getPos(i,j);
                    if (casella_actual != CellType.EMPTY){
                        PlayerType player = casella_actual.getPlayer();
                        MoveNode moviments = tauler.getMoves(new Point(i,j), player);
                        if (player == jugador){ 
                            if (casella_actual.isQueen()){ ++reines[0]; h += 8;}
                            else ++fitxes[0]; h+= 4;
                            if (j == 0 && tauler.getYDirection(player) == 1) ++ultima_fila[0]; h += 1;
                            if (j == tauler.getSize()-1 && tauler.getYDirection(player) == -1) ++ultima_fila[0]; h += 1;
                            if (j == 3 || j == 4) {
                                if (i >= 2 && i <= 5){ ++centre_tauler[0]; h+= 2;}
                                else ++extrems_centre[0]; h+= 1;
                            }
                            if (!moviments.getChildren().isEmpty() && moviments.getChildren().get(0).isJump()){
                                ++salts[0];
                                h += 3;
                                Point jumpedPoint = moviments.getChildren().get(0).getJumpedPoint();
                                if (!fitxesSaltables.contains(jumpedPoint)){
                                    fitxesSaltables.add(jumpedPoint);
                                    --protegides[1];
                                    h += 3;
                                }
                            }
                        }
                        else {
                            if (casella_actual.isQueen()){ ++reines[1]; h -= 8;}
                            else ++fitxes[1]; h -= 4;
                            if (j == 0 && tauler.getYDirection(player) == 1){ ++ultima_fila[1]; h -= 1;}
                            if (j == tauler.getSize()-1 && tauler.getYDirection(player) == -1) ++ultima_fila[1]; h-= 1;
                            if (j == 3 || j == 4) {
                                if (i >= 2 && i <= 5){ ++centre_tauler[1]; h-=2;}
                                else ++extrems_centre[1];h-=1;
                            }
                            if (!moviments.getChildren().isEmpty() && moviments.getChildren().get(0).isJump()){
                                ++salts[1];
                                h -= 3;
                                Point jumpedPoint = moviments.getChildren().get(0).getJumpedPoint();
                                if (!fitxesSaltables.contains(jumpedPoint)){
                                    fitxesSaltables.add(jumpedPoint);
                                    --protegides[0];
                                    h -= 3;
                                }
                            }
                        }
                    }
                }
            }
        }
        /*
        System.out.println("Fitxes jugador = " + fitxes[0] + " fitxes rival = " + fitxes[1]);
        System.out.println("Reines jugador = " + reines[0] + " reines rival = " + reines[1]);
        System.out.println("Fitxes jugador protegides = " + protegides[0] + " fitxes rival protegides = " + protegides[1]);
        System.out.println("Fitxes jugador ultima fila = " + ultima_fila[0] + " fitxes rival ultima fila = " + ultima_fila[1]);
        System.out.println("Fitxes jugador extrems centre = " + extrems_centre[0] + " fitxes rival extrems centre = " + extrems_centre[1]);
        System.out.println("Fitxes jugador centre tauler = " + centre_tauler[0] + " fitxes rival centre tauler = " + centre_tauler[1]);
        System.out.println("Fitxes jugador que poden matar = " + salts[0] + " fitxes rival que poden matar = " + salts[1]);
        */
        return h;
    }   
}
