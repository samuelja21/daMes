package edu.upc.epsevg.prop.checkers.players.daMes;


import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;
import edu.upc.epsevg.prop.checkers.players.daMes.Heuristica;
import edu.upc.epsevg.prop.checkers.players.daMes.ElMeuStatus;
import edu.upc.epsevg.prop.checkers.players.daMes.GameInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import edu.upc.epsevg.prop.checkers.players.daMes.Zobrist;
import java.util.List;

/**
 * Jugador MiniMax
 * @author Samuel i Oriol (DA+)
 */
public class PlayerMiniMaxIDS implements IPlayer, IAuto {

    private String name;
    private GameStatus s;
    private int maxprof;
    private int prof_maxima; 
    private int nodes_explorats;
    private PlayerType jugador;
    private boolean timeout;
    private List<Point> millor_moviment = new ArrayList<>();
    private HashMap<ElMeuStatus, GameInfo> taula = new HashMap<>();
    private Zobrist z;

    public PlayerMiniMaxIDS() {
        name = "DaMesIDS";
        z = new Zobrist();
    }

    @Override
    public void timeout() {
        timeout = true;
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
    public int minimax(ElMeuStatus s, int prof, int alpha, int beta){
        int h = 0;
        if (!timeout){
            prof_maxima = Math.max(prof_maxima, prof);
            int millor_h = 0;
            //Comprova si es fulla
            if (prof == maxprof || s.isGameOver()){
                //retorna heuristica
                Heuristica heur = new Heuristica(s, jugador);
                h = heur.valor_heuristica();
                ++nodes_explorats;
            }
            else {
                if (prof % 2 == 0){
                //Si MAX
                    millor_h = Integer.MIN_VALUE;
                } 
                else {
                //Si MIN
                    millor_h = Integer.MAX_VALUE;
                }
                List<MoveNode> movs = s.getMoves();
                int escollit = 0;
                int inicial = 0;
                //Si esta el tauler, iniciem amb el millor moviment
                if (taula.containsKey(s)) inicial = taula.get(s).getMillorMoviment();
                //System.out.println(inicial);
                for (int i = inicial; i < movs.size(); ++i){
                    MoveNode mov = movs.get(i);
                    mov = s.getMoves(mov.getPoint(), s.getCurrentPlayer());
                    List<List<Point>> moviments = new ArrayList<>();
                    moviments = llistaPunts(mov, moviments);
                    for (List<Point> moviment : moviments){
                        if (!timeout){
                            if (beta > alpha){
                                ElMeuStatus s2 = new ElMeuStatus(s);
                                s2.movePiece(moviment);
                                h = minimax(s2, prof+1, alpha, beta);
                                if (prof % 2 == 0) {
                                    if (prof == 0 && h > millor_h) millor_moviment = moviment; escollit = i;
                                    millor_h = Math.max(millor_h, h);
                                    alpha = Math.max(alpha, millor_h);
                                }
                                else {
                                    millor_h = Math.min(millor_h, h);
                                    beta = Math.min(beta, millor_h);
                                }
                            }
                        }
                    }
                }
                //si tenim mes nivells per sota, actualitza
                //si no esta a la taula afegeix
                int nivellsSota = maxprof - prof;
                if (!taula.containsKey(s) || taula.containsKey(s) && nivellsSota > taula.get(s).getNivellsPerSota()){
                    GameInfo ginfo = new GameInfo(escollit,nivellsSota);
                    taula.put(s, ginfo);
                }

                h = millor_h;
            }
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
        timeout = false;
        jugador = s.getCurrentPlayer();
        nodes_explorats = 0;
        prof_maxima = 0;
        maxprof = 0;
        List<Point> moviment_seleccionat = new ArrayList<>();
        int maxprof_acabada = 0;
        ElMeuStatus ms = new ElMeuStatus(s);
        ms.init_Zobrist(z);
        while (!timeout){
            ++maxprof;
            minimax(ms, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (!timeout) {
                moviment_seleccionat = millor_moviment;
                maxprof_acabada = prof_maxima;
            }
        }
        
        return new PlayerMove( moviment_seleccionat, nodes_explorats, maxprof_acabada, SearchType.MINIMAX);        
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return "MinMaxIDS(" + name + ")";
    }

}
