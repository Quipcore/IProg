package net;

import com.jamesmurty.utils.XMLBuilder2;

import java.sql.Timestamp;

public class ProtocolBuilder {

    private XMLBuilder2 xmlBuilder;

    private static final String XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE message>";

    public ProtocolBuilder(String name){
        xmlBuilder = XMLBuilder2.create(name);
    }

    public ProtocolBuilder addHeader(String username, String email,String command){
        xmlBuilder = xmlBuilder.element("header")
                .element("protocol")
                    .element("type").text("").up()
                    .element("version").text("1.0").up()
                    .element("command").text(command).up()
                .up()
                .element("id")
                    .element("username").text(username).up()
                    .element("email").text(email).up()
                .up()
                .up();
        return this;
    }

    public ProtocolBuilder addBody(){
        xmlBuilder = xmlBuilder.e("body"); //Needs assignment for some reason! Probably a bug in the lib
        return this;
    }

    public ProtocolBuilder closeTag(){
        xmlBuilder = xmlBuilder.up();
        return this;
    }

    public ProtocolBuilder addGame(String id, String fen, String time){
        xmlBuilder = xmlBuilder.e("game")
                    .e("id").t(id).up()
                    .e("fen").t(fen).up()
                    .e("time").t(time).up()
                    .e("moves").t("1.e4").up()
                .up();
        return this;
    }
    @Override
    public String toString() {
        return XML_HEADER + xmlBuilder.asString();
    }

    public ProtocolBuilder addGame(int gameId, String playerWhite, String playerBlack, String fenString, String result, boolean isActive, Timestamp gameDate) {
        xmlBuilder = xmlBuilder.e("game")
                        .e("id").t(String.valueOf(gameId)).up()
                        .e("player_white").text(playerWhite).up()
                        .e("player_black").text(playerBlack).up()
                        .e("fen").t(fenString).up()
                        .e("result").text(String.valueOf(result)).up()
                        .e("is_active").text(String.valueOf(isActive)).up()
                        .e("time").t(String.valueOf(gameDate)).up()
                    .up();
        return this;
    }

    public ProtocolBuilder addMove(int gameId, int turn, String whiteMove, String blackMove) {
        xmlBuilder = xmlBuilder.e("move")
                    .e("id").t(String.valueOf(gameId)).up()
                    .e("turn").text(String.valueOf(turn)).up()
                    .e("white").text(whiteMove).up()
                    .e("black").text(blackMove).up()
                .up();
        return this;
    }
}
