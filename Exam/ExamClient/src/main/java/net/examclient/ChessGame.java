package net.examclient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ChessGame {

    enum Turn {
        WHITE,
        BLACK
    }

    private final static String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final Timestamp timestamp;
    private final List<Pair<String,String>> moves;
    private String fenString;
    private String id;
    private int moveCount;

    private Turn turn;

    public ChessGame(String id, String fen, Timestamp timestamp, String moves){
        this.timestamp = timestamp;
        this.fenString = fen;
        this.id = id;
        this.moves = Arrays.stream(moves.split(",")).map(s -> new Pair<String,String>(s,null)).collect(Collectors.toList());
    }

    public void play(String move){
        if(getTurn() == Turn.WHITE){
            moves.add(new Pair<>(move,null));
        }else{
            moves.get(moveCount).setValue(move);
            moveCount++;
        }

        updateFen();
    }

    private void updateFen() {
        fenString = START_FEN;
    }

    public Turn getTurn(){
        return turn;
    }

    public String getFenString() {
        return fenString;
    }

    public int getMoveCount(){
        return moveCount;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "timestamp=" + timestamp +
                ", FEN=" + fenString +
                '}';
    }
}
