package org.onlinetictactoe.multiplayer;

import org.onlinetictactoe.multiplayer.messages.*;
import org.onlinetictactoe.player.Player;
import org.onlinetictactoe.state.MultiplayerState;
import org.onlinetictactoe.state.PlayState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import static org.onlinetictactoe.state.LobbyState.startCountDown;
import static org.onlinetictactoe.state.LobbyState.updateOpponentName;

public class Client {
    private Socket serverSocket;

    private ObjectOutputStream outputStream;

    private ObjectInputStream inputStream;

    private Player player;
    private PlayState playState;

    public Client(String serverIp, int serverPort, Player player) {
        try {
            this.serverSocket = new Socket(serverIp, serverPort);
            this.outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(serverSocket.getInputStream());
            this.player = player;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Object message) {
        try {
            outputStream.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public void joinLobby(UUID lobbyId) {
        JoinLobbyRequest joinLobbyRequest = new JoinLobbyRequest(lobbyId, player);
        sendMsg(joinLobbyRequest);
    }

    public void listLobbies() {
        ListLobbiesRequest listLobbiesRequest = new ListLobbiesRequest(player);
        sendMsg(listLobbiesRequest);
    }

    public void createLobby(UUID lobbyId, String lobbyName) {
        CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest(lobbyName, lobbyId, 2, player);
        sendMsg(createLobbyRequest);
    }

    public void move(UUID lobbyId, int x, int y) {
        Move moveRequest = new Move(lobbyId, x, y, player);
        sendMsg(moveRequest);
    }

    public void sendChatMessage(UUID lobbyId, String message) {
        ChatMessage chatMessage = new ChatMessage(lobbyId, message, player);
        sendMsg(chatMessage);
    }

    public void quit(UUID lobbyId) {
        sendMsg(new QuitMessage(lobbyId, player));
    }

    public void handleRequest(Object object) {
        if (object instanceof JoinLobbyResponse joinLobbyResponse) {
        } else if (object instanceof ListLobbiesResponse listLobbiesResponse) {
            MultiplayerState.lobbies = listLobbiesResponse.lobby;
            MultiplayerState.refreshLobbies();
        } else if (object instanceof StartGameMessage startGameMessage) {
            playState.setMark(startGameMessage.playerMark);
            updateOpponentName(startGameMessage.opponentName + ": " + ((startGameMessage.playerMark == 'X') ? 'O' : 'X'));
            startCountDown();
        } else if (object instanceof Move move) {
            playState.makeMove(move.x, move.y);
        } else if (object instanceof ChatMessage chatMessage) {
            playState.addChatMSG(chatMessage.player.name, chatMessage.message);
        }
    }

    public void start() {
        try {
            new Thread(() -> {
                while (serverSocket.isConnected()) {
                    Object object = null;
                    try {
                        object = inputStream.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (object != null) handleRequest(object);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}