package xd.arkosammy.breakout.sprite;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.ArrayList;
import java.util.List;

public class Brick extends AbstractSprite {
    public Brick(double[] coordinate, int[] dimensions) {
        super(coordinate, dimensions);
    }

    @Override
    public List<ScreenElement> getScreenElements() {
        List<ScreenElement> screenElements = new ArrayList<>();
        for(int i = (int) this.coordinate[0]; i < this.coordinate[0] + dimensions[0]; i++){
            for(int j = (int) this.coordinate[1]; j < this.coordinate[1] + dimensions[1]; j++){
                screenElements.add(new ScreenElement(i, j, ScreenElement.ElementType.BRICK));
            }
        }
        return screenElements;
    }

    @Override
    public void tick(BreakoutGame game) {

    }
}
