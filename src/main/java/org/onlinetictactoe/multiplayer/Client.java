package org.onlinetictactoe.multiplayer;

import org.onlinetictactoe.multiplayer.messages.*;
import org.onlinetictactoe.player.Player;
import org.onlinetictactoe.state.MultiplayerState;
import org.onlinetictactoe.state.PlayState;
import org.onlinetictactoe.state.ScoreboardState;

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

    private PlayState playState;

    private ScoreboardState scoreboardState;

    public Client(String serverIp, int serverPort) {
        try {
            this.serverSocket = new Socket(serverIp, serverPort);
            this.outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(serverSocket.getInputStream());
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

    public void setScoreboardState(ScoreboardState scoreboardState) {
        this.scoreboardState = scoreboardState;
    }

    public void joinLobby(UUID lobbyId, Player player) {
        JoinLobbyRequest joinLobbyRequest = new JoinLobbyRequest(lobbyId, player);
        sendMsg(joinLobbyRequest);
    }

    public void listLobbies(Player player) {
        ListLobbiesRequest listLobbiesRequest = new ListLobbiesRequest(player);
        sendMsg(listLobbiesRequest);
    }

    public void createLobby(UUID lobbyId, String lobbyName) {
        CreateLobbyRequest createLobbyRequest = new CreateLobbyRequest(lobbyName, lobbyId, 2);
        sendMsg(createLobbyRequest);
    }

    public void move(UUID lobbyId, int x, int y, Player player) {
        Move moveRequest = new Move(lobbyId, x, y, player);
        sendMsg(moveRequest);
    }

    public void sendChatMessage(UUID lobbyId, String message, Player player) {
        ChatMessage chatMessage = new ChatMessage(lobbyId, message, player);
        sendMsg(chatMessage);
    }

    public void quit(UUID lobbyId, Player player) {
        sendMsg(new QuitMessage(lobbyId, player));
    }

    public void requestScoreboard() {
        ScoreBoardRequest scoreBoardRequest = new ScoreBoardRequest();
        sendMsg(scoreBoardRequest);
    }

    public void sendWin(UUID lobbyId, Player player) {
        sendMsg(new WinMessage(lobbyId, player));
    }

    public void handleRequest(Object object) {
        if (object instanceof JoinLobbyResponse joinLobbyResponse) {
        } else if (object instanceof ListLobbiesResponse listLobbiesResponse) {
            MultiplayerState.lobbies = listLobbiesResponse.lobby;
            MultiplayerState.refreshLobbies();
        } else if (object instanceof StartGameMessage startGameMessage) {
            playState.setMark(startGameMessage.playerMark);
            updateOpponentName(startGameMessage.opponentName);
            startCountDown();
        } else if (object instanceof Move move) {
            playState.makeMove(move.x, move.y);
        } else if (object instanceof ChatMessage chatMessage) {
            playState.addChatMSG(chatMessage.player.name, chatMessage.message);
        } else if (object instanceof ScoreBoardResponse scoreBoardResponse) {
            scoreboardState.refreshScores(scoreBoardResponse.scores);
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