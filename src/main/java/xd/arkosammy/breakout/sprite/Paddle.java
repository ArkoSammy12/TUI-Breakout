package xd.arkosammy.breakout.sprite;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.ArrayList;
import java.util.List;

public class Paddle extends AbstractSprite {
    public Paddle(double[] coordinate, int[] dimensions) {
        super(coordinate, dimensions);
    }

    public void move(Direction direction, BreakoutGame breakoutGame){

        double[] newCoordinate;
        if(direction == Direction.LEFT){
            newCoordinate = new double[]{this.coordinate[0] - 1, this.coordinate[1]};
        } else {
            newCoordinate = new double[]{this.coordinate[0] + 1, this.coordinate[1]};
        }

        for(int i = (int) newCoordinate[0]; i < newCoordinate[0] + dimensions[0]; i++){
            for(int j = (int) newCoordinate[1]; j < newCoordinate[1] + dimensions[1]; j++){
                ScreenElement element = breakoutGame.getElementAt(i, j);
                if(element.elementType() != ScreenElement.ElementType.BACKGROUND && element.elementType() != ScreenElement.ElementType.PADDLE){
                    return;
                }
            }
        }
        this.setCoordinate(newCoordinate);

    }

    @Override
    public List<ScreenElement> getScreenElements() {
        List<ScreenElement> screenElements = new ArrayList<>();
        for(int i = (int) this.coordinate[0]; i < this.coordinate[0] + dimensions[0]; i++){
            for(int j = (int) this.coordinate[1]; j < this.coordinate[1] + dimensions[1]; j++){
                screenElements.add(new ScreenElement(i, j, ScreenElement.ElementType.PADDLE));
            }
        }
        return screenElements;
    }

    @Override
    public void tick(BreakoutGame game) {

    }

    public enum Direction {
        LEFT,
        RIGHT;
    }

    public enum PaddleSection {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE;
    }

}
