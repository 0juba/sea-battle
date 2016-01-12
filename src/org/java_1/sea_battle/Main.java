package org.java_1.sea_battle;

import org.java_1.sea_battle.game_play.GamePlay;

/**
 * Created by Guba Andrei on 19.12.15.
 */
public class Main {
    public static void main(String[] args) {
        // Да начнется бой!
        GamePlay.getInstance().start();
    }
}
