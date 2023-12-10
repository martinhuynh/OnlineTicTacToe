package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class ScoreBoardResponse implements Serializable {
    public TreeMap<Integer, String> scores;

    public ScoreBoardResponse(TreeMap<Integer, String> scores) {
        this.scores = scores;
    }
}
