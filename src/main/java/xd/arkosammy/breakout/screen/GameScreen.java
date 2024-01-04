package xd.arkosammy.breakout.screen;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import xd.arkosammy.breakout.BreakoutGame;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GameScreen {

    private final Screen terminalScreen;
    private final List<ScreenElement> screenElements = new ArrayList<>();

    public GameScreen() throws IOException {

        Terminal terminal = new DefaultTerminalFactory(System.out, System.in, Charset.defaultCharset())
                .setInitialTerminalSize(new TerminalSize(104, 100))
                .setTerminalEmulatorFrameAutoCloseTrigger(TerminalEmulatorAutoCloseTrigger.CloseOnEscape)
                .setTerminalEmulatorTitle("Breakout")
                .setTerminalEmulatorFontConfiguration(AWTTerminalFontConfiguration.newInstance(new Font("Monospaced", Font.BOLD, 1)))
                .setPreferTerminalEmulator(true)
                .createTerminal();

        terminal.setForegroundColor(TextColor.ANSI.WHITE);
        terminal.setBackgroundColor(TextColor.ANSI.BLACK);
        terminal.setCursorVisible(false);
        terminalScreen = new TerminalScreen(terminal);
        this.terminalScreen.startScreen();
    }

    public Screen getTerminalScreen(){
        return this.terminalScreen;
    }

    public void clearScreenElements(){
        this.screenElements.clear();
    }

    public void submitScreenElement(ScreenDrawable drawable){
        this.screenElements.addAll(drawable.getScreenElements());
    }

    public void refreshDisplay(BreakoutGame game) throws IOException {

        int xOffset = game.getGameField().getXCoordinate();
        int yOffset = game.getGameField().getYCoordinate();
        this.terminalScreen.doResizeIfNecessary();
        for (int i = this.terminalScreen.getTerminalSize().getRows(); i >= 0; i--) {
            for (int j = 0; j < this.terminalScreen.getTerminalSize().getColumns(); j++) {
                this.terminalScreen.setCharacter(j + xOffset, i + 1 + yOffset, new TextCharacter(' '));
            }
        }
        TextGraphics scoreText = this.terminalScreen.newTextGraphics();
        scoreText.putString(0, 0, String.format("Score = %d. a=Move left, q=Move left (slow), d=Move right, e=Move right (slow), esc=Close window", game.getScore()));
        for(ScreenElement e : this.screenElements){
            TextCharacter character = new TextCharacter(e.elementType().getGraphic());
            this.terminalScreen.setCharacter(e.xCoordinate() + xOffset, e.yCoordinate() + yOffset + 1, character);
        }
        this.terminalScreen.refresh();
        this.clearScreenElements();

    }

    public void displayEndingScreen(BreakoutGame game, boolean won) throws IOException {
        int xOffset = game.getGameField().getXCoordinate();
        int yOffset = game.getGameField().getYCoordinate();
        this.terminalScreen.doResizeIfNecessary();
        for (int i = this.terminalScreen.getTerminalSize().getRows(); i >= 0; i--) {
            for (int j = 0; j < this.terminalScreen.getTerminalSize().getColumns(); j++) {
                this.terminalScreen.setCharacter(j + xOffset, i + 1 + yOffset, new TextCharacter(' '));
            }
        }
        TextGraphics scoreText = this.terminalScreen.newTextGraphics();
        scoreText.putString(0, 0, String.format("You %s! Score = %d", won ? "won" : "lost", game.getScore()));
        for(ScreenElement e : this.screenElements){
            TextCharacter character = new TextCharacter(e.elementType().getGraphic());
            this.terminalScreen.setCharacter(e.xCoordinate() + xOffset, e.yCoordinate() + yOffset + 1, character);
        }
        this.terminalScreen.refresh();
        this.clearScreenElements();
    }

}
