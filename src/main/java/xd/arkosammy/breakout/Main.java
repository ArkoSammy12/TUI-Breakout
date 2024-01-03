package xd.arkosammy.breakout;

import xd.arkosammy.breakout.input.PlayerInputHandler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        BreakoutGame game = BreakoutGame.getInstance();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new PlayerInputHandler(game.getGameScreen()));
        game.startLoop();
        executorService.shutdown();

    }

}
