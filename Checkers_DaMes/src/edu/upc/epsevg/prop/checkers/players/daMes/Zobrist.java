/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.checkers.players.daMes;

import java.util.Random;

/**
 *
 * @author Samuel i Oriol (DA+)
 */
public class Zobrist {
    
    int hpp[][][] = new int[8][8][4];
    int p2move; //diferenciador

    public Zobrist() {
        init_Zobrist();
    }
    
    public void init_Zobrist(){
        for (int i = 0; i < 8; ++i){
            for (int j = 0; j < 8; ++j){
                for (int k = 0; k < 4; ++k){
                    Random rand = new Random();
                    hpp[i][j][k] = rand.nextInt();
                }
                
            }
        }
        Random rand = new Random();
        p2move = rand.nextInt();
    }
    
    
}
