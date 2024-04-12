import chess.*;
import dataAccess.MemoryUserAccess;
import server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        getInitialUserInput();
    }

    private static void getInitialUserInput(){
        Server server;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type 1 to start server\n");
        String input = scanner.nextLine();
        int response = Integer.parseInt(input);
        if (response == 1){
            server = new Server();
            server.run(8080);
            System.out.println("Server is running...");
            getRunningUserInput(server);
        }
        else{
            getInitialUserInput();
        }
    }

    private static void getRunningUserInput(Server server){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type 2 to stop server or 3 to restart it\n");
        String input = scanner.nextLine();
        int response = Integer.parseInt(input);
        if (response == 2){
            server.stop();
        } else if (response == 3) {
            server.stop();
            server = new Server();
            server.run(8080);
            System.out.println("Server is running...");
            getRunningUserInput(server);
        } else{
            getRunningUserInput(server);
        }
    }
}

