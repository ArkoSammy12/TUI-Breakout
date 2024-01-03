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
import java.util.Iterator;
import java.util.List;

public class BreakoutGame {

    private static final int FRAME_DELAY = 40;
    private static BreakoutGame instance;
    private List<AbstractSprite> sprites = new ArrayList<>();
    private final GameScreen gameScreen;
    private GameField gameField;
    private int score;
    private boolean running = true;


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

    public void startLoop() throws IOException, InterruptedException {

        while(running){

            this.gameScreen.submitScreenElement(this.gameField);
            this.sprites.forEach(sprite -> sprite.tick(this));
            this.sprites.removeIf(sprite -> !sprite.shouldExist());
            this.sprites.forEach(this.gameScreen::submitScreenElement);
            this.gameScreen.refreshDisplay(this);
            Thread.sleep(FRAME_DELAY);

        }

    }

    public static void checkInput(GameScreen gameScreen) throws IOException {

        Screen screen = gameScreen.getTerminalScreen();
        KeyStroke keyStroke = screen.pollInput();

        if(keyStroke != null){

            char character = keyStroke.getCharacter();

            Paddle.Direction moveDirection = switch(character){

                case 'a' -> Paddle.Direction.LEFT;
                case 'd' -> Paddle.Direction.RIGHT;
                default -> null;

            };

            if(moveDirection != null){
                for(AbstractSprite sprite : BreakoutGame.getInstance().sprites){
                    if(sprite instanceof Paddle paddle){
                        paddle.move(moveDirection, BreakoutGame.getInstance());
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

        sprites.add(new Paddle(new double[]{50, 49}, new int[]{5, 1}));

        int currentX = 2;
        int currentY = 2;

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 3; j++) {
                AbstractSprite brick = new Brick(new double[]{currentY, currentX}, new int[]{9, 3});
                this.sprites.add(brick);
                currentX += 4;
            }
            currentY += 10;
            currentX = 2;
        }
        sprites.add(new Ball(new double[]{50, 40}, new int[]{1, 1}));
    }

    public void deleteBrickAt(int x, int y){
        for (AbstractSprite abstractSprite : this.sprites) {
            if (abstractSprite instanceof Brick brick) {
                for (ScreenElement element : brick.getScreenElements()) {
                    if (element.xCoordinate() == x && element.yCoordinate() == y) {
                        abstractSprite.markForRemoval();
                        return;
                    }
                }
            }
        }
    }

    private void createMap(){

        gameField = new GameField.FieldBuilder(30, 0, 103)
                .withMapRow("#######################################################################################################")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#                                                                                                     #")
                .withMapRow("#######################################################################################################")
                .create();

    }

}
