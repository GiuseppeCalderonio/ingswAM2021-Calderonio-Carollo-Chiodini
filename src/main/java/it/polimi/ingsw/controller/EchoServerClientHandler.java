package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EchoServerClientHandler implements Runnable {
    private final Socket socket;
    private static Game game;
    private static List<String> nicknames = new ArrayList<>();
    private static boolean start = false;
    private static int numberOfPlayers = 4;

    public EchoServerClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            synchronized (this){
                if (numberOfPlayers == 4) {
                    numberOfPlayers = 5;
                    out.println("Welcome, you are the first player, decide the number of players[1,4]:  ");
                    out.flush();
                    String line = in.nextLine();
                    int a = 0;
                    while (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")){
                        out.println("ERROR, decide the number of players[1;4]: ");
                        out.flush();
                        line = in.nextLine();
                    }
                    switch (line){
                        case "1":
                            a = 1;
                            break;
                        case "2":
                            a = 2;
                            break;
                        case "3":
                            a = 3;
                            break;
                        case "4":
                            a = 4;
                            break;
                    }
                    numberOfPlayers = a;
                }
            }

            out.println("Welcome to the waiting room, type your nickname: ");
            out.flush();

            String line = in.nextLine();

            //va sincrionizzato meglio
            while(line.isEmpty() || nicknames.contains(line)){
                out.println("Invalid nickname, try again: ");
                out.flush();
                line = in.nextLine();
            }



            out.println("You are waiting for other players to join! ");
            out.flush();
            synchronized (this){
                nicknames.add(line);
            }

            String nickname = line;

            while(nicknames.size() < numberOfPlayers)out.flush();

            out.println(nicknames);
            out.flush();

            synchronized (this){
                if (!start){
                    game = new Game(nicknames);
                    start = true;
                }
            }
            out.println("Welcome to the game! ");
            out.flush();
            synchronized (this){
                nicknames = game.getPlayers().stream().map(RealPlayer::getNickname).collect(Collectors.toList());
                out.println(nicknames);
                out.flush();
            }

            /*
            inizializza
            chiedi collectionresources
            chiedi 2 interi
            */

            out.println("Write the first leader card that you want to discard[1;4]: ");
            out.flush();

            line = in.nextLine();
            while (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")){
                out.println("ERROR, Write the first leader card that you want to discard[1;4]: ");
                out.flush();
                line = in.nextLine();
            }
            String first = "";
            int a = 0;
            switch (line){
                case "1":
                    a = 1;
                    first = "1";
                    break;
                case "2":
                    a = 2;
                    first = "2";
                    break;
                case "3":
                    a = 3;
                    first = "3";
                    break;
                case "4":
                    a = 4;
                    first = "4";
                    break;
            }


            out.println("Write the second leader card that you want to discard[1;4]: ");
            out.flush();

            line = in.nextLine();
            while (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4") || line.equals(first)){
                out.println("ERROR, Write the first leader card that you want to discard[1;4]: ");
                out.flush();
                line = in.nextLine();
            }
            int b = 0;
            switch (line){
                case "1":
                    b = 1;
                    break;
                case "2":
                    b = 2;
                    break;
                case "3":
                    b = 3;
                    break;
                case "4":
                    b = 4;
                    break;
            }
            CollectionResources resources;
            do {
                resources = new CollectionResources();
                out.println("Write the first resource that you want to get[coin,stone,shield,servant]: ");
                out.flush();

                line = in.nextLine();
                while (!line.equals("coin") && !line.equals("stone") && !line.equals("shield") && !line.equals("servant") && !line.equals("")){
                    out.println("ERROR, Write the first resource that you want to get[coin,stone,shield,servant]: ");
                    out.flush();
                    line = in.nextLine();
                }

                switch (line){
                    case "coin":
                        resources.add(new Coin());
                        break;
                    case "stone":
                        resources.add(new Stone());
                        break;
                    case "shield":
                        resources.add(new Shield());
                        break;
                    case "servant":
                        resources.add(new Servant());
                        break;
                }


                out.println("Write the second resource that you want to get[coin,stone,shield,servant]: ");
                out.flush();

                line = in.nextLine();
                while (!line.equals("coin") && !line.equals("stone") && !line.equals("shield") && !line.equals("servant") && !line.equals("")){
                    out.println("ERROR, Write the first resource that you want to get[coin,stone,shield,servant]: ");
                    out.flush();
                    line = in.nextLine();
                }
                switch (line){
                    case "coin":
                        resources.add(new Coin());
                        break;
                    case "stone":
                        resources.add(new Stone());
                        break;
                    case "shield":
                        resources.add(new Shield());
                        break;
                    case "servant":
                        resources.add(new Servant());
                        break;
                }
                out.println(game.checkInitialising(nickname, resources));
                out.flush();
            }while (!game.checkInitialising(nickname, resources));

            game.initialiseGame(nickname, resources, a, b);

            out.println("Initialisation completed, wait for others...");
            out.flush();

            while (game.getTurnManager() < 0);

            out.println("Game initialised, now let's start ");
            out.flush();

            /*
            end
             */


            boolean quit = false;
            while (!quit){

                out.println("Inserire azione[spostare risorse, sceliere biglie, comprare carta, attivare produzione, azione leader]:");
                out.flush();

                line = in.nextLine();

                if(!game.getPlayers().get(game.getTurnManager()).getNickname().equals(nickname)){
                    out.println("Is not your turn!");
                    out.flush();
                }
                else{
                    out.println("Is your turn!");
                    out.flush();
                    switch (line){
                        case "spostare risorse":
                            out.println("Scrivere source[1,3/4/5]: ");
                            out.flush();

                            line = in.nextLine();
                            while (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4") && !line.equals("5")){
                                out.println("ERROR, Scrivere source[1,3/4/5]: ");
                                out.flush();
                                line = in.nextLine();
                            }
                            int source = 0;
                            switch (line){
                                case "1":
                                    source = 1;
                                    break;
                                case "2":
                                    source = 2;
                                    break;
                                case "3":
                                    source = 3;
                                    break;
                                case "4":
                                    source = 4;
                                    break;
                                case "5":
                                    source = 5;
                                    break;
                            }

                            out.println("Scrivere destination: ");
                            out.flush();

                            line = in.nextLine();
                            while (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4") && !line.equals("5")){
                                out.println("ERROR, Scrivere destination[1,3/4/5]: ");
                                out.flush();
                                line = in.nextLine();
                            }
                            int destination = 0;
                            switch (line){
                                case "1":
                                    destination = 1;
                                    break;
                                case "2":
                                    destination = 2;
                                    break;
                                case "3":
                                    destination = 3;
                                    break;
                                case "4":
                                    destination = 4;
                                    break;
                                case "5":
                                    destination = 5;
                                    break;
                            }
                            if ( !(game.checkShelfSelected(source) || !game.checkShelfSelected(destination))){
                                out.println("ERROR!!");
                                out.flush();
                                break;
                            }

                            game.shiftResources(source, destination);

                            break;
                        case "scegliere biglie":

                            List<Marble> marbles;

                            out.println("Selezionare se comprare da riga o da colonna[riga,colonna]: ");
                            out.flush();
                            line = in.nextLine();
                            while (!line.equals("riga") && !line.equals("colonna")) {
                                out.println("ERROR, Selezionare se comprare da riga o da colonna[riga,colonna]:  ");
                                out.flush();
                                line = in.nextLine();
                            }
                            String comando = line;
                            if (comando.equals("riga")){
                                out.println("Selezionare indice di riga[1,3]: ");
                                out.flush();
                                line = in.nextLine();
                                while (!line.equals("1") && !line.equals("2") && !line.equals("3")) {
                                    out.println("ERROR, Selezionare indice di riga[1,2,3]:  ");
                                    out.flush();
                                    line = in.nextLine();
                                }
                                int riga = 0;
                                switch (line){
                                    case "1":
                                        riga = 1;
                                        break;
                                    case "2":
                                        riga = 2;
                                        break;
                                    case "3":
                                        riga = 3;
                                        break;
                                }
                                marbles= game.selectRow(riga);
                            }
                            else {
                                out.println("Selezionare indice di colonna[1,4]: ");
                                out.flush();
                                line = in.nextLine();
                                while (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4")) {
                                    out.println("ERROR, Selezionare indice di colonna[1,4]:  ");
                                    out.flush();
                                    line = in.nextLine();
                                }
                                int colonna = 0;
                                switch (line){
                                    case "1":
                                        colonna = 1;
                                        break;
                                    case "2":
                                        colonna = 2;
                                        break;
                                    case "3":
                                        colonna = 3;
                                        break;
                                    case "4":
                                        colonna = 3;
                                        break;
                                }
                                marbles = game.selectColumn(colonna);
                            }

                            if (game.getActualPlayer().getLeaderWhiteMarbles().size() == 1){
                                while (!game.changeWhiteMarble(marbles, 1));
                            }
                            if (game.getActualPlayer().getLeaderWhiteMarbles().size() == 2){
                                int whiteMarbles = (int)marbles.stream().filter(marble -> marble.equals(new WhiteMarble())).count();
                                out.println("Hai ottenuto "+whiteMarbles+" biglie bianche");
                                out.flush();
                                for (int i = 0; i < whiteMarbles; i++){
                                    out.println("Selezionare leader card da usare per la "+(i+1)+"° biglia bianca[1,2]: ");
                                    out.flush();
                                    line = in.nextLine();
                                    while (!line.equals("1") && !line.equals("2")) {
                                        out.println("ERROR,Selezionare leader card da usare per la "+(i+1)+"° biglia bianca[1,2]: ");
                                        out.flush();
                                        line = in.nextLine();
                                    }
                                    int usare = 0;
                                    switch (line){
                                        case "1":
                                            usare = 1;
                                            break;
                                        case "2":
                                            usare = 2;
                                            break;
                                    }
                                    game.changeWhiteMarble(marbles, usare);
                                }
                            }

                            CollectionResources marbleConverted = game.convert(marbles);

                            List<Resource> typesOfResource = new ArrayList<>(
                                    new HashSet<>(marbleConverted.asList()));
                            for (Resource resource : typesOfResource){
                                out.println("Selezionare indice del magazzino in cui mettere ["+resource.getType().getName()+"] [1,3/4/5]: ");
                                out.flush();
                                line = in.nextLine();
                                while (!line.equals("1") && !line.equals("2") && !line.equals("3") && !line.equals("4") && !line.equals("5")) {
                                    out.println("ERROR,Selezionare indice del magazzino in cui mettere ["+resource.getType().getName()+"] [1,3/4/5]: ");
                                    out.flush();
                                    line = in.nextLine();
                                }
                                int indice = 0;
                                switch (line){
                                    case "1":
                                        indice = 1;
                                        break;
                                    case "2":
                                        indice = 2;
                                        break;
                                    case "3":
                                        indice = 3;
                                        break;
                                    case "4":
                                        indice = 4;
                                        break;
                                    case "5":
                                        indice = 5;
                                        break;
                                }

                                if (!game.checkShelfSelected(indice)){
                                    out.println("ERROR,Selezionato shelf sbagliato");
                                    out.flush();
                                    break;
                                }
                                out.println("Manca pocou");
                                out.flush();
                                game.insertInWarehouse(indice, resource, marbleConverted);
                                out.println("Andare avanti");
                                out.flush();
                            }


                            break;
                        case "comprare carta":

                            break;
                        case "attivare produzione":

                            break;
                        case "azione leader":

                            break;
                        case "quit":
                            quit = true;
                            break;
                        default:
                            out.println("Comando invalido!");
                            out.flush();
                            break;
                    }

                }
                out.println("Stato del warehouse!");
                out.flush();
                Warehouse warehouse = game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse();
                out.println("1 scaffale "+warehouse.getShelf(1).getResources().asList().stream().map(Resource::getType).collect(Collectors.toList()));
                out.flush();
                out.println("2 scaffale "+warehouse.getShelf(2).getResources().asList().stream().map(Resource::getType).collect(Collectors.toList()));
                out.flush();
                out.println("3 scaffale "+warehouse.getShelf(3).getResources().asList().stream().map(Resource::getType).collect(Collectors.toList()));
                out.flush();
            }

            // Chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}