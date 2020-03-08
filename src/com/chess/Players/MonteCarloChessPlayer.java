package com.chess.Players;

import com.chess.Game.ChessBoard;
import com.chess.Game.Move;
import com.chess.Game.NamedColor;
import com.chess.Settings;

import java.util.*;

public class MonteCarloChessPlayer extends AIChessPlayer {

    @SuppressWarnings("FieldCanBeLocal")
    private final int MINIMUM_DELAY = 500;
    @SuppressWarnings("FieldCanBeLocal")
    private final int MAXIMUM_DELAY = 10000;

    private int nodeCount;

    public MonteCarloChessPlayer(NamedColor color) {
        super(color);
    }

    @Override
    public Move pickMove(ChessBoard board, List<Move> moves) {
        nodeCount = 0;
        int games = 0;
        if(moves.size()>0) {
            long startTime = System.currentTimeMillis();
            Node tree = new Node(board, getColor());
            while (System.currentTimeMillis() - startTime < MAXIMUM_DELAY) {
                games++;
                Node n = treePolicy(tree);
                Node child;
                if (n.isTerminal()) {
                    child = n;
                } else {
                    child = n.nextChild();
                    nodeCount++;
                }
                int reward = playout(child);
                child.propogateReward(reward);
            }
            System.out.println("Node Count: " + nodeCount);
            System.out.println("Games: " + games);
            return tree.bestChild().move;
        } else {
            return null;
        }
    }

    private int playout(Node tree) {
        NamedColor player = tree.player;
        ChessBoard state = tree.state.clone();
        int turns = 0;
        while (turns < 50) {
            List<Move> moves = state.getAllMoves(player, true);
            if (moves.size() == 0) {
                return player.equals(tree.player) ? 0 : 1;
            }
            List<Move> captures = new ArrayList<>();
            for (Move m: moves) {
                if (m.isCapture()) {
                    captures.add(m);
                }
            }
            List<Move> acceptableMoves = moves;
            if (captures.size() > 0) {
                acceptableMoves = captures;
            }
            Move m = acceptableMoves.get((int) (Math.random() * (acceptableMoves.size())));
            state.makeMove(m);
            turns++;
            player = player.equals(Settings.p1Color) ? Settings.p2Color : Settings.p1Color;
        }
        if (tree.player.equals(Settings.p1Color)) {
            if (state.score() > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (state.score() < 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private long time = 0;
    private void time(String s) {
        System.out.println(s + ": " + (System.nanoTime() - time));
        time = System.nanoTime();
    }

    private Node treePolicy(Node tree) {
        if (Math.random() > .6) {
            // Random node
            Random rng = new Random();
            Node n = tree;

            while (n.children.size() > 0) {
                if (n.children.size() < n.moves.size()) {
                    return n;
                }
                n = n.children.get(rng.nextInt(n.children.size()));
            }
            return n;
        } else {
            // Best node
            Node rv = tree;
            while (rv.children.size() > 0) {
                rv = rv.bestChild();
            }
            return rv;
        }
    }


    @Override
    public void invalidMove(Move m) {
        System.out.println("Made invalid move: " + m);
    }

    private class Node {
        NamedColor player;
        ChessBoard state;
        Move move;
        int wins;
        int games;
        Node parent;
        List<Node> children;
        List<Move> moves;

        public Node(ChessBoard state, NamedColor player) {
            this(state, player, null, null);
        }

        public Node(ChessBoard state, NamedColor player, Node parent, Move m) {
            this.state = state;
            this.player = player;
            this.parent = parent;
            this.move = m;
            this.wins = 0;
            this.games = 0;
            this.children = new ArrayList<>();
            this.moves = null;
        }

        public boolean isTerminal() {
            return state.isGameOver();
        }

        public Node nextChild() {
            if (moves == null) {
                moves = state.getAllMoves(player, false);
            }
            Move m = moves.get(children.size());
            ChessBoard clone = state.clone();
            clone.makeMove(m);
            Node child = new Node(clone, player.equals(Settings.p1Color) ? Settings.p2Color : Settings.p1Color, this, m);
            children.add(child);
            return child;
        }

        public void propogateReward(int reward) {
            this.wins += reward;
            this.games++;
            if (this.parent != null) {
                this.parent.propogateReward(reward);
            }
        }

        public Node bestChild() {
            if (children.size() == 0) {
                return this;
            }
            Node bestNode = children.get(0);
            for (Node c: children) {
                if (c.wins / c.games > bestNode.wins / bestNode.games) {
                    bestNode = c;
                }
            }
            return bestNode;
        }
    }
}
