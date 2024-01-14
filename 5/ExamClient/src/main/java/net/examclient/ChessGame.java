package net.examclient;
import kotlin.internal.HidesMembers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ChessGame {

    private final static String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final Timestamp timestamp;
    private final List<Pair<String,String>> moves;
    private final String playerWhite;
    private final String playerBlack;
    private boolean isActive;
    private String fenString;
    private String id;
    private int moveCount;

    private boolean isWhitesTurn;

    public ChessGame(String id, String fen, Timestamp timestamp, String moves){
        this.timestamp = timestamp;
        this.fenString = fen;
        this.id = id;
        this.moves = Arrays.stream(moves.split(",")).map(s -> new Pair<String,String>(s,null)).collect(Collectors.toList());
        this.playerBlack = "";
        this.playerWhite = "";
    }

    public ChessGame(String id, String playerWhite, String playerBlack, String fen, String isActive, String time) {
        this.id = id;
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.fenString = fen;
        this.isActive = Boolean.getBoolean(isActive);
        this.timestamp = Timestamp.valueOf(time);
        this.moves = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "timestamp=" + timestamp +
                ", moves=" + moves +
                ", playerWhite='" + playerWhite + '\'' +
                ", playerBlack='" + playerBlack + '\'' +
                ", isActive=" + isActive +
                ", fenString='" + fenString + '\'' +
                ", id='" + id + '\'' +
                ", moveCount=" + moveCount +
                ", isWhitesTurn=" + isWhitesTurn +
                '}';
    }

    public String getId() {
        return id;
    }
}
