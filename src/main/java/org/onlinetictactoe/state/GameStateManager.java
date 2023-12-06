package org.onlinetictactoe.state;

import org.onlinetictactoe.Main;
import org.onlinetictactoe.state.GameState;
import org.onlinetictactoe.state.MenuState;
import org.onlinetictactoe.state.PauseState;
import org.onlinetictactoe.state.PlayState;

public class GameStateManager {
    public int WIDTH, HEIGHT;
    public enum State {
        PAUSE(pauseState),
        PLAY(playState),
        MENU(menuState),
        LOBBY(lobbyState),
        CREATE_LOBBY(createLobby),
        MULTIPLAYER(multiplayerState);
        private GameState gameState;
        public GameState getState() {
            return gameState;
        }
        State(GameState gameState) {
            this.gameState = gameState;
        }
    }
    private static Main main;
    private static PlayState playState;
    private static PauseState pauseState;
    private static MenuState menuState;
    private static CreateLobby createLobby;
    private static LobbyState lobbyState;
    private static MultiplayerState multiplayerState;
    private GameState previousState;
    private GameState currentState;

    private boolean pause = false;

    public GameStateManager(Main main, int WIDTH, int HEIGHT) {
        this.main = main;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        playState = new PlayState(this);
        pauseState = new PauseState(this);
        menuState = new MenuState(this);
        createLobby = new CreateLobby(this);
        lobbyState = new LobbyState(this);
        multiplayerState = new MultiplayerState(this);

        currentState = menuState;
        previousState = pauseState;
        main.add(menuState);
    }

    public void setState(State state) {
        if (state == State.PLAY) playState = new PlayState(this);
        main.remove(currentState);
        currentState = state.getState();
        update();
        main.add(currentState);
        main.validate();
        currentState.validate();
        currentState.repaint();
        currentState.requestFocus();
    }

    public void update() {
        currentState.update();
    }

    public void escape() {
        currentState.escape();
    }
}
