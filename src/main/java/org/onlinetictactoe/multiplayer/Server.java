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

        ServerPlayer(Socket socket, Player player) {
            this.socket = socket;
            this.player = player;
        }
    }

    ServerSocket serverSocket;
    ConcurrentHashMap<UUID, ServerLobby> lobbies;

    public Server(int port) {
        lobbies = new ConcurrentHashMap<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLobby(UUID id, String lobbyName, int maxPlayers) {
        lobbies.put(id, new ServerLobby(new ArrayList<>(), lobbyName, maxPlayers));
    }

    private boolean joinLobby(Socket client, JoinLobbyRequest joinLobbyRequest) {
        if (!lobbies.containsKey(joinLobbyRequest.lobbyId)) {
            return false;
        }
        if (lobbies.get(joinLobbyRequest.lobbyId).isFull()) {
            return false;
        }

        ServerLobby serverLobby = lobbies.get(joinLobbyRequest.lobbyId);
        serverLobby.players.add(new ServerPlayer(client, joinLobbyRequest.player));
        return true;
    }

    private void handleRequest(Object request, Socket client) throws IOException {
        if (request instanceof JoinLobbyRequest joinLobby) {
            boolean successfulJoin = joinLobby(client, joinLobby);
            if (!successfulJoin) {
                ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(null);
                return;
            }
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.writeObject(joinLobby.lobbyId);
        } else if (request instanceof CreateLobbyRequest createLobby) {
            createLobby(createLobby.lobbyId, createLobby.lobbyName, createLobby.maxPlayers);
            lobbies.get(createLobby.lobbyId).players.add(new ServerPlayer(client, createLobby.player));
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.writeObject(null);
        } else if (request instanceof ListLobbiesRequest) {
            ArrayList<Lobby> listOfLobbies = new ArrayList<>();
            for (Map.Entry<UUID, ServerLobby> pair : lobbies.entrySet()) {
                UUID id = pair.getKey();
                ServerLobby serverLobby = pair.getValue();
                Lobby lobby = new Lobby(serverLobby.name, id, serverLobby.players.size(), serverLobby.maxPlayers);
                listOfLobbies.add(lobby);
            }
            ListLobbiesResponse response = new ListLobbiesResponse(listOfLobbies);
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
            outputStream.writeObject(response);
        } else if (request instanceof ReadyMessage readyMessage) {
            ServerLobby lobby = lobbies.get(readyMessage.lobbyId);
            lobby.readyPlayers += 1;
            if (lobby.readyPlayers == lobby.maxPlayers) {
                Random rand = new Random();
                boolean firstPlayerX = rand.nextBoolean();
                for (int i = 0; i < lobby.players.size(); i++) {
                    ServerPlayer serverPlayer = lobby.players.get(i);
                    char playerSymbol = firstPlayerX ? 'X' : 'O';
                    ObjectOutputStream outputStream = new ObjectOutputStream(serverPlayer.socket.getOutputStream());
                    outputStream.writeObject(new StartGameMessage(playerSymbol, serverPlayer.player.name));
                }
            }
        } else if (request instanceof Move move) {
            UUID lobbyId = move.lobbyId;
            // confirm move is by right player
            if (move.player.id != lobbies.get(lobbyId).currentMove.player.id) {
                ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(null);
                return;
            }
            // send sender back nothing adn forward move to other player
            for (ServerPlayer player: lobbies.get(lobbyId).players) {
                if (player.socket == client) {
                    ObjectOutputStream outputStream = new ObjectOutputStream(player.socket.getOutputStream());
                    outputStream.writeObject(null);
                    continue;
                }
                lobbies.get(lobbyId).currentMove = player;
                ObjectOutputStream outputStream = new ObjectOutputStream(player.socket.getOutputStream());
                outputStream.writeObject(move);
            }
        } else if (request instanceof QuitMessage quitMessage) {
            UUID lobbyId = quitMessage.lobbyId;
            for (ServerPlayer player: lobbies.get(lobbyId).players) {
                ObjectOutputStream outputStream = new ObjectOutputStream(player.socket.getOutputStream());
                outputStream.writeObject(quitMessage);
            }
        }
    }

    public void start() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        Object object = new ObjectInputStream(socket.getInputStream()).readObject();
                        handleRequest(object, socket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(6666);
        server.createLobby(UUID.randomUUID(), "old", 2);
        server.createLobby(UUID.randomUUID(), "old1", 2);
        server.createLobby(UUID.randomUUID(), "old2", 2);
        server.createLobby(UUID.randomUUID(), "old3", 2);
        server.createLobby(UUID.randomUUID(), "old4", 2);
        server.start();
    }
}
