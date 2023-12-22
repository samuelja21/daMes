/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.checkers.players.daMes;

/**
 *
 * @author Samuel i Oriol (DA+)
 */
public class GameInfo {
    
    private int indexMillorMov; 
    private int nivellsPerSota; //maxprof - prof actual

    public GameInfo(int indexMillorMov, int nivellsPerSota) {
        this.indexMillorMov = indexMillorMov;
        this.nivellsPerSota = nivellsPerSota;
    }
    
    public int getMillorMoviment(){
        return indexMillorMov;
    }
    
    public int getNivellsPerSota(){
        return nivellsPerSota;
    }
}
