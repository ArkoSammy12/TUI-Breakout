package xd.arkosammy.breakout.input;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.GameScreen;

import java.io.IOException;

public class PlayerInputHandler implements Runnable {

    private final GameScreen gameScreen;
    public PlayerInputHandler(GameScreen gameScreen){
        this.gameScreen = gameScreen;
    }

    @Override
    public void run() {
        while(true){
            try {
                BreakoutGame.checkInput(gameScreen);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
