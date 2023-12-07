package org.onlinetictactoe.multiplayer;
import org.onlinetictactoe.multiplayer.messages.*;
import org.onlinetictactoe.player.Player;
import org.onlinetictactoe.state.Lobby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

public class Server {
    ArrayList<ServerPlayer> clients;

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
        for (ServerPlayer player: lobbies.get(quitMessage.lobbyId).players) {
            if (player.player.id == quitMessage.player.id) {
                continue;
            }
            try {
                player.outputStream.writeObject(new QuitMessage(quitMessage.lobbyId, player.player));
                player.outputStream.flush();
            } catch (Exception ignored) {}
        }
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
            lobbies.get(createLobby.lobbyId).players.add(client);
            client.outputStream.writeObject(null);
            client.outputStream.flush();
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
        server.createLobby(UUID.randomUUID(), "old", 2);
        server.createLobby(UUID.randomUUID(), "old1", 2);
        server.createLobby(UUID.randomUUID(), "old2", 2);
        server.createLobby(UUID.randomUUID(), "old3", 2);
        server.createLobby(UUID.randomUUID(), "old4", 2);
        server.start();
    }
}
