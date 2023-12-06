package org.onlinetictactoe.player;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    public String name;
    public UUID id;

    public Player(String name, UUID id) {
        this.name = name;
        this.id = id;
    }
}
