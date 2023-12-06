package org.onlinetictactoe.multiplayer;

import org.onlinetictactoe.multiplayer.messages.*;
import org.onlinetictactoe.player.Player;
import org.onlinetictactoe.state.Lobby;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class Client {
    private Socket serverSocket;
    private Player player;

    Client(String serverIp, int serverPort, Player player) {
        try {
            this.serverSocket = new Socket(serverIp, serverPort);
            this.player = player;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object sendRevMsg(Object message) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            objectOutputStream.writeObject(message);

            ObjectInputStream objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
            return objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Lobby joinLobby(UUID lobbyId) {
        JoinLobbyRequest joinLobbyRequest = new JoinLobbyRequest(lobbyId, player);
        return (Lobby) sendRevMsg(joinLobbyRequest);
    }

    public ArrayList<LobbyInfo> listLobbies() {
        ListLobbiesRequest listLobbiesRequest = new ListLobbiesRequest();
        ListLobbiesResponse response = (ListLobbiesResponse) sendRevMsg(listLobbiesRequest);
        return response.lobbyInfo;
    }

    public Lobby createLobby(String lobbyId) {
        CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest(lobbyId, UUID.randomUUID(), 2, player);
        return (Lobby) sendRevMsg(createLobbyRequest);
    }

    public static void main(String[] args) {
        Player player = new Player("Old Man", UUID.randomUUID());
        Client client = new Client("localhost", 6666, player);
        ArrayList<LobbyInfo> lobbies =  client.listLobbies();
        for (LobbyInfo lobby : lobbies) {
            if (lobby == null) {
                continue;
            }
            System.out.println(lobby.lobbyId);
        }
    }
}