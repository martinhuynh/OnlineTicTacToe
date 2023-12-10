package org.onlinetictactoe.multiplayer.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class ScoreBoardResponse implements Serializable {
    public TreeMap<Integer, ArrayList<String>> scores;

    public ScoreBoardResponse(TreeMap<Integer, ArrayList<String>> scores) {
        this.scores = scores;
    }
}
