package xd.arkosammy.breakout;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import xd.arkosammy.breakout.playfield.GameField;
import xd.arkosammy.breakout.screen.GameScreen;
import xd.arkosammy.breakout.screen.ScreenElement;
import xd.arkosammy.breakout.sprite.AbstractSprite;
import xd.arkosammy.breakout.sprite.Ball;
import xd.arkosammy.breakout.sprite.Brick;
import xd.arkosammy.breakout.sprite.Paddle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BreakoutGame {

    private static final int FRAME_DELAY = 20;
    private static BreakoutGame instance;
    private final List<AbstractSprite> sprites = new ArrayList<>();
    private final GameScreen gameScreen;
    private GameField gameField;
    private int score;
    private boolean won = false;

    private BreakoutGame() throws IOException {
        this.gameScreen = new GameScreen();
        this.createMap();
        this.initializeSprites();
    }

    public static BreakoutGame getInstance() throws IOException {
        if(instance == null){
            instance = new BreakoutGame();
        }
        return instance;
    }

    public GameScreen getGameScreen(){
        return this.gameScreen;
    }

    public GameField getGameField(){
        return this.gameField;
    }

    public int getScore(){
        return this.score;
    }

    public Paddle getPaddle(){
        for(AbstractSprite sprite : this.sprites){
            if(sprite instanceof Paddle paddle){
                return paddle;
            }
        }
        return null;
    }

    public void startLoop() throws IOException, InterruptedException {

        while(shouldKeepRunning()){

            this.gameScreen.submitScreenElement(this.gameField);
            this.sprites.forEach(sprite -> sprite.tick(this));
            this.sprites.removeIf(sprite -> !sprite.shouldExist());
            this.sprites.forEach(this.gameScreen::submitScreenElement);
            this.gameScreen.refreshDisplay(this);
            Thread.sleep(FRAME_DELAY);

        }

        this.onGameFinished();

    }

    private boolean shouldKeepRunning(){
        int ballY = 0;
        int paddleY = 0;
        boolean noBricks = true;
        for(AbstractSprite sprite : this.sprites){
            switch(sprite){
                case Ball ball -> ballY = (int) Math.round(ball.getCoordinate()[1]);
                case Paddle paddle -> paddleY = (int) Math.round(paddle.getCoordinate()[1]);
                case Brick ignored -> noBricks = false;
                default -> {}
            }
        }
        if(ballY >= paddleY){
            return false;
        }
        if(noBricks){
            won = true;
            return false;
        }
        return true;
    }

    private void onGameFinished() throws IOException {
        this.sprites.removeIf(sprite -> sprite instanceof Ball);
        this.gameScreen.displayEndingScreen(this, this.won);
    }

    public static void checkInput(GameScreen gameScreen) throws IOException {

        Screen screen = gameScreen.getTerminalScreen();
        KeyStroke keyStroke = screen.pollInput();

        if(keyStroke != null){

            char character = keyStroke.getCharacter();

            Paddle.Direction moveDirection = switch(character){

                case 'a' -> Paddle.Direction.LEFT;
                case 'q' -> Paddle.Direction.LEFT_SLOW;
                case 'd' -> Paddle.Direction.RIGHT;
                case 'e' -> Paddle.Direction.RIGHT_SLOW;
                default -> null;

            };

            if(moveDirection != null){
                for(AbstractSprite sprite : BreakoutGame.getInstance().sprites){
                    if(sprite instanceof Paddle paddle){
                        paddle.move(moveDirection, BreakoutGame.getInstance());
                        return;
                    }
                }

            }

        }

    }

    public ScreenElement getElementAt(int x, int y){
        for(AbstractSprite sprite : this.sprites){
            for(ScreenElement element : sprite.getScreenElements()){
                if(element.xCoordinate() == x && element.yCoordinate() == y){
                    return element;
                }
            }
        }
        return this.gameField.getMapElementAt(x, y);
    }

    private void initializeSprites(){

        sprites.add(new Paddle(new double[]{47, 49}, new int[]{5, 1}));

        int currentBrickX = 2;
        int currentBrickY = 2;

        // Place bricks on game field
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 10; j++) {
                AbstractSprite brick = new Brick(new double[]{currentBrickX, currentBrickY}, new int[]{Brick.BRICK_WIDTH, Brick.BRICK_HEIGHT});
                this.sprites.add(brick);
                currentBrickX += brick.getDimensions()[0] + 1;
            }
            currentBrickY += Brick.BRICK_HEIGHT + 1;
            currentBrickX = 2;
        }
        sprites.add(new Ball(new double[]{49, 40}, new int[]{1, 1}));
    }

    public void removeBrickAt(int x, int y){
        for (AbstractSprite abstractSprite : this.sprites) {
            if (abstractSprite instanceof Brick brick) {
                for (ScreenElement element : brick.getScreenElements()) {
                    if (element.xCoordinate() == x && element.yCoordinate() == y && brick.shouldExist()) {
                        brick.markForRemoval();
                        score++;
                        return;
                    }
                }
            }
        }
    }

    public boolean isPositionOutOfBounds(int x, int y){
        return x < 0 || x >= this.getGameField().getMapWidth() || y < 0 || y >= this.getGameField().getMapHeight();
    }

    private void createMap(){

        gameField = new GameField.FieldBuilder(0, 0, 103)
                .withFieldRow("#######################################################################################################")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#                                                                                                     #")
                .withFieldRow("#######################################################################################################")
                .create();

    }

}
