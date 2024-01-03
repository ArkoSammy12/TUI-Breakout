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
            newCoordinate = new double[]{this.coordinate[0] - 2, this.coordinate[1]};
        } else {
            newCoordinate = new double[]{this.coordinate[0] + 2, this.coordinate[1]};
        }

        for(int i = (int) Math.round(newCoordinate[0]); i < Math.round(newCoordinate[0]) + dimensions[0]; i++){
            for(int j = (int) Math.round(newCoordinate[1]); j < Math.round(newCoordinate[1]) + dimensions[1]; j++){
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
        for(int i = (int) Math.round(this.coordinate[0]); i < Math.round(this.coordinate[0]) + dimensions[0]; i++){
            for(int j = (int) Math.round(this.coordinate[1]); j < Math.round(this.coordinate[1]) + dimensions[1]; j++){
                screenElements.add(new ScreenElement(i, j, ScreenElement.ElementType.PADDLE));
            }
        }
        return screenElements;
    }

    @Override
    public void tick(BreakoutGame game) {

    }

    public PaddleSection getCollidingPaddleSection(int xCoordinate){

        int sectionLength = this.dimensions[0] / 5;
        int paddleX = (int) Math.round(this.coordinate[0]);

        for(int section = 0; section < 4; section++){
            for(int xPos = paddleX + (sectionLength * section); xPos < (paddleX + (sectionLength * section)) + sectionLength; xPos++){
                if(xPos == xCoordinate){
                    return PaddleSection.fromNumber(section + 1);
                }
            }
        }

        return null;

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

        public static PaddleSection fromNumber(int n){
            return switch(n){

                case 1 -> ONE;
                case 2 -> TWO;
                case 3 -> THREE;
                case 4 -> FOUR;
                case 5 -> FIVE;
                default -> throw new IllegalArgumentException();

            };
        }

    }

}
