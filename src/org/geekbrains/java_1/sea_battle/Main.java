package org.geekbrains.java_1.sea_battle;

import org.geekbrains.java_1.sea_battle.game_play.GamePlay;

/**
 * Created by Guba Andrei on 19.12.15.
 */
public class Main {
    public static void main(String[] args) {
        GamePlay game = new GamePlay();

        // Да начнется бой
        game.start();
    }
}
