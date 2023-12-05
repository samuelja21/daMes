package edu.upc.epsevg.prop.checkers.players.daMes;


import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;
import edu.upc.epsevg.prop.checkers.players.daMes.Heuristica;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Jugador MiniMax
 * @author Samuel i Oriol (DA+)
 */
public class PlayerMiniMax implements IPlayer, IAuto {

    private String name;
    private GameStatus s;
    private int maxprof;
    private PlayerType jugador;
    List<Point> millor_moviment = new ArrayList<>();

    public PlayerMiniMax(String name, int profunditat) {
        this.name = name;
        this.maxprof = profunditat;
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
        
    }

    /**
     * Donat un node fulla, retorna la llista de punts que contenen el propi
     * node, el pare, el pare del seu pare... fins arribar a la arrel.
     *
     * @param mov node que conté la casella on acaba el moviment.
     * @return llista de punts que conformen el moviment.
     */
    public List<Point> puntsMoviment(MoveNode mov){
        List<Point> punts = new ArrayList<>();
        //afegim el punt de la fulla.
        punts.add(0, mov.getPoint());
        MoveNode mn = mov.getParent();
        //anem visitant el pare fins arribar al node arrel.
        while (mn != null){
            punts.add(0, mn.getPoint());
            //afegim el punt del pare de mn al principi de la llista i anem pujant en el arbre.
            mn = mn.getParent();
        }
        return punts;
    }
    
     /**
     * Recorre l'arbre de punts, quan arriba a una fulla obté la llista
     * de punts que conformen el moviment i l'afegeix a la llista de llistes.
     *
     * @param mov node de l'arbre que inicialment és la posició on està situada en el tauler
     * i en una fulla és la posició on acaba el moviment.
     * @param moviments llista de els possibles moviments trobats.
     * @return llista amb tots els possibles moviments d'una peça.
     */
    public List<List<Point>> llistaPunts(MoveNode mov, List<List<Point>> moviments){
        List<MoveNode> fills = mov.getChildren();
        
        if (fills.isEmpty()){
            moviments.add(puntsMoviment(mov));
        } 
        else {
                for (MoveNode fill : fills){
                    moviments = llistaPunts(fill, moviments);
                }
        }
        
        return moviments;
    }
    
    /**
     * Crea arbre segons els possibles moviments, actualitza el millor moviment i 
     * retorna la heurística escollida.
     *
     * @param s Tauler i estat actual de joc.
     * @param prof profunditat a la que ens trobem.
     * @param alpha valor alpha del minimax.
     * @param beta valor beta del minimax.
     * @return valor de la heuristica escollida.
     */
    public int minimax(GameStatus s, int prof, int alpha, int beta){
        //comprovar si es fulla
        int h = 0;
        if (prof == maxprof || s.isGameOver()){
            //retorna heuristica
            h = s.getScore(PlayerType.PLAYER1) - s.getScore(PlayerType.PLAYER2);
        }
        else {
            //Recorrer possibles fills i cridar recursivament a minimax
            List<MoveNode> movs = s.getMoves();
            for (MoveNode mov : movs){
                List<List<Point>> moviments = new ArrayList<>();
                moviments = llistaPunts(mov, moviments);
                for (List<Point> moviment : moviments){
                    if (beta > alpha){
                        GameStatus s2 = new GameStatus(s);
                        s2.movePiece(moviment);
                        h = minimax(s2, prof+1, alpha, beta);
                        if (prof % 2 == 0 && h > alpha) {
                            alpha = h;
                            if (prof == 0) millor_moviment = moviment;
                        }
                        else if (prof % 2 != 0 && h < beta) beta = h;
                    }
                }
            }
            if (prof % 2 == 0) h = alpha;
            else h = beta;
        }
        return h;
    }
    
    /**
     * Decideix el moviment del jugador donat un tauler i un color de peça que
     * ha de posar.
     *
     * @param s Tauler i estat actual de joc.
     * @return el moviment que fa el jugador.
     */
    @Override
    public PlayerMove move(GameStatus s) {
        jugador = s.getCurrentPlayer();
        minimax(s, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new PlayerMove( millor_moviment, 0L, 0, SearchType.MINIMAX);         
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return "MinMax(" + name + ")";
    }

}
