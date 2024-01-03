package xd.arkosammy.breakout.sprite;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.ArrayList;
import java.util.List;

public class Brick extends AbstractSprite {

    public static final int BRICK_WIDTH = 9;
    public static final int BRICK_HEIGHT = 3;

    public Brick(double[] coordinate, int[] dimensions) {
        super(coordinate, dimensions);
    }

    @Override
    public List<ScreenElement> getScreenElements() {
        List<ScreenElement> screenElements = new ArrayList<>();
        for(int i = (int) Math.round(this.coordinate[0]); i < Math.round(this.coordinate[0]) + dimensions[0]; i++){
            for(int j = (int) Math.round(this.coordinate[1]); j < Math.round(this.coordinate[1]) + dimensions[1]; j++){
                screenElements.add(new ScreenElement(i, j, ScreenElement.ElementType.BRICK));
            }
        }
        return screenElements;
    }

    @Override
    public void tick(BreakoutGame game) {

    }
}
