package ui;


import chess.ChessGame;
import chess.ChessPiece;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        client.init();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Client.startMenu();

    }

}