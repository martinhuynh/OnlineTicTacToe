package org.onlinetictactoe.multiplayer;
import org.onlinetictactoe.multiplayer.messages.*;
import org.onlinetictactoe.player.Player;
import org.onlinetictactoe.state.Lobby;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    ArrayList<ServerPlayer> clients;

    ConcurrentHashMap<String, Integer> scoreBoard;

    public class ServerLobby {
        public ServerPlayer currentMove;

        public String name;

        public ArrayList<ServerPlayer> players;

        public int maxPlayers;

        public int readyPlayers;

        ServerLobby(ArrayList<ServerPlayer> players, String name, int maxPlayers) {
            this.players = players;
            this.name = name;
            this.maxPlayers = maxPlayers;
        }

        public boolean isFull() {
            return players.size() == maxPlayers;
        }
    }

    public class ServerPlayer {
        public Socket socket;
        public Player player;
        public ObjectOutputStream outputStream;

        ServerPlayer(Socket socket, ObjectOutputStream outputStream) {
            this.socket = socket;
            this.outputStream = outputStream;
        }
    }

    ServerSocket serverSocket;
    ConcurrentHashMap<UUID, ServerLobby> lobbies;

    public Server(int port) {
        lobbies = new ConcurrentHashMap<>();
        clients = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fis = new FileInputStream("scores.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.scoreBoard = (ConcurrentHashMap<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            this.scoreBoard = new ConcurrentHashMap<>();
        }

    }

    private void createLobby(UUID id, String lobbyName, int maxPlayers) {
        lobbies.put(id, new ServerLobby(new ArrayList<>(), lobbyName, maxPlayers));
    }

    private boolean joinLobby(ServerPlayer client, JoinLobbyRequest joinLobbyRequest) {
        if (!lobbies.containsKey(joinLobbyRequest.lobbyId)) {
            return false;
        }
        if (lobbies.get(joinLobbyRequest.lobbyId).isFull()) {
            return false;
        }

        ServerLobby serverLobby = lobbies.get(joinLobbyRequest.lobbyId);
        serverLobby.players.add(client);
        return true;
    }

    private void removeLobby(QuitMessage quitMessage) {
        lobbies.remove(quitMessage.lobbyId);
    }

    private void startGame(ServerLobby lobby) throws IOException {
        if (lobby.players.size() == lobby.maxPlayers) {
            System.out.println("Starting Lobby");
            Random rand = new Random();
            boolean firstPlayerX = rand.nextBoolean();
            ServerPlayer player1 = lobby.players.get(0);
            ServerPlayer player2 = lobby.players.get(1);

            for (int i = 0; i < lobby.players.size(); i++) {
                ServerPlayer serverPlayer = lobby.players.get(i);
                char playerSymbol = firstPlayerX ? 'X' : 'O';
                firstPlayerX = !firstPlayerX;
                if (i == 0) {
                    serverPlayer.outputStream.writeObject(new StartGameMessage(playerSymbol, player2.player.name));
                } else {
                    serverPlayer.outputStream.writeObject(new StartGameMessage(playerSymbol, player1.player.name));
                }
                if (playerSymbol == 'X') {
                    lobby.currentMove = serverPlayer;
                }
                serverPlayer.outputStream.flush();
            }
        }
    }

    private void handleRequest(Object request, ServerPlayer client) throws IOException {
        if (request instanceof JoinLobbyRequest joinLobby) {
            System.out.println("Received Join Request on Lobby: " + joinLobby.lobbyId);
            boolean successfulJoin = joinLobby(client, joinLobby);
            if (!successfulJoin) {
                client.outputStream.writeObject(new JoinLobbyResponse(false));
                client.outputStream.flush();
                return;
            }
            client.player = joinLobby.player;
            startGame(lobbies.get(joinLobby.lobbyId));
            client.outputStream.writeObject(new JoinLobbyResponse(true));
            client.outputStream.flush();
        } else if (request instanceof CreateLobbyRequest createLobby) {
            System.out.println("Received Create Request");
            createLobby(createLobby.lobbyId, createLobby.lobbyName, createLobby.maxPlayers);
        } else if (request instanceof ListLobbiesRequest) {
            System.out.println("Received List Lobbies Request");
            ArrayList<Lobby> listOfLobbies = new ArrayList<>();
            for (Map.Entry<UUID, ServerLobby> pair : lobbies.entrySet()) {
                UUID id = pair.getKey();
                ServerLobby serverLobby = pair.getValue();
                Lobby lobby = new Lobby(serverLobby.name, id, serverLobby.players.size(), serverLobby.maxPlayers);
                listOfLobbies.add(lobby);
            }
            ListLobbiesResponse response = new ListLobbiesResponse(listOfLobbies);
            client.outputStream.writeObject(response);
            client.outputStream.flush();
        } else if (request instanceof ReadyMessage readyMessage) {
            System.out.println("Received Ready Message for lobby: " + readyMessage.lobbyId);
            ServerLobby lobby = lobbies.get(readyMessage.lobbyId);
            lobby.readyPlayers += 1;
            startGame(lobby);
        } else if (request instanceof Move move) {
            System.out.println("Received Move Message for lobby: " + move.lobbyId);
            UUID lobbyId = move.lobbyId;
            // confirm move is by right player
            if (move.player.id != lobbies.get(lobbyId).currentMove.player.id) {
                return;
            }
            // send sender back nothing adn forward move to other player
            for (ServerPlayer player: lobbies.get(lobbyId).players) {
                if (player == client) {
                    continue;
                }
                lobbies.get(lobbyId).currentMove = player;
                player.outputStream.writeObject(move);
                player.outputStream.flush();
            }
        } else if (request instanceof QuitMessage quitMessage) {
            System.out.println("Received Quit Message for lobby: " + quitMessage.lobbyId);
            removeLobby(quitMessage);
        } else if (request instanceof ChatMessage chatMessage) {
            for (ServerPlayer player: lobbies.get(chatMessage.lobbyId).players) {
                if (player.player.id == chatMessage.player.id) {
                    continue;
                }
                player.outputStream.writeObject(chatMessage);
                player.outputStream.flush();
            }
        } else if (request instanceof WinMessage winMessage) {
            String name = winMessage.player.name;
            if (scoreBoard.containsKey(name)) {
                scoreBoard.put(name, scoreBoard.get(name) + 1);
                return;
            }
            scoreBoard.put(name, 1);
            try (FileOutputStream fos = new FileOutputStream("scores.ser");
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(scoreBoard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (request instanceof ScoreBoardRequest) {
            TreeMap<Integer, ArrayList<String>> treeMap = new TreeMap<>(Comparator.reverseOrder());
            for (Map.Entry<String, Integer> entry: scoreBoard.entrySet()) {
                ArrayList<String> playersWithScore = treeMap.getOrDefault(entry.getValue(), new ArrayList<>());
                playersWithScore.add(entry.getKey());
                treeMap.put(entry.getValue(), playersWithScore);
            }
            client.outputStream.writeObject(new ScoreBoardResponse(treeMap));
            client.outputStream.flush();
        }
    }

    public void start() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    ServerPlayer serverPlayer;
                    ObjectInputStream inputStream = null;
                    ObjectOutputStream outputStream = null;
                    try {
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        outputStream = new ObjectOutputStream(socket.getOutputStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    serverPlayer = new ServerPlayer(socket, outputStream);
                    clients.add(serverPlayer);
                    try {
                        while (socket.isConnected()) {
                            Object object = inputStream.readObject();
                            if (object != null) handleRequest(object, serverPlayer);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    UUID lobbyToRemove = null;
                    for (Map.Entry<UUID, ServerLobby> lobbyEntry : lobbies.entrySet()) {
                        for (ServerPlayer player : lobbyEntry.getValue().players) {
                            if (player.socket == socket) {
                                lobbyToRemove = lobbyEntry.getKey();
                                break;
                            }
                        }
                        if (lobbyToRemove != null) {
                            break;
                        }
                    }

                    if (lobbyToRemove != null) {
                        removeLobby(new QuitMessage(lobbyToRemove, serverPlayer.player));
                    }

                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(4001);
        server.createLobby(UUID.randomUUID(), "Lobby 1", 2);
        server.createLobby(UUID.randomUUID(), "Lobby 2", 2);
        server.createLobby(UUID.randomUUID(), "Lobby 3", 2);
        server.createLobby(UUID.randomUUID(), "Lobby 4", 2);
        server.createLobby(UUID.randomUUID(), "Lobby 5", 2);
        server.start();
    }
}
