package xd.arkosammy.breakout.sprite;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.ArrayList;
import java.util.List;

public class Paddle extends AbstractSprite {

    public Paddle(double[] coordinate, int[] dimensions) {
        super(coordinate, dimensions);
    }

    public void move(Direction direction, BreakoutGame game){

        double[] newCoordinate = switch (direction) {
            case LEFT -> new double[]{this.coordinate[0] - 2, this.coordinate[1]};
            case LEFT_SLOW -> new double[]{this.coordinate[0] - 1, this.coordinate[1]};
            case RIGHT -> new double[]{this.coordinate[0] + 2, this.coordinate[1]};
            case RIGHT_SLOW -> new double[]{this.coordinate[0] + 1, this.coordinate[1]};
        };

        for(int i = (int) Math.round(newCoordinate[0]); i < Math.round(newCoordinate[0]) + dimensions[0]; i++){
            for(int j = (int) Math.round(newCoordinate[1]); j < Math.round(newCoordinate[1]) + dimensions[1]; j++){
                if (game.isPositionOutOfBounds(i, j)) continue;
                ScreenElement element = game.getElementAt(i, j);
                if(element.elementType() == ScreenElement.ElementType.WALL){
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

        int sectionLength = (int) Math.ceil( (double) this.dimensions[0] / 5);
        int paddleX = (int) Math.round(this.coordinate[0]);

        for(int section = 0; section < 5; section++){
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
        LEFT_SLOW,
        RIGHT,
        RIGHT_SLOW
    }

    public enum PaddleSection {
        LEFT_EDGE,
        MIDDLE_LEFT,
        MIDDLE,
        MIDDLE_RIGHT,
        RIGHT_EDGE;

        public static PaddleSection fromNumber(int n){
            return switch(n){

                case 1 -> LEFT_EDGE;
                case 2 -> MIDDLE_LEFT;
                case 3 -> MIDDLE;
                case 4 -> MIDDLE_RIGHT;
                case 5 -> RIGHT_EDGE;
                default -> throw new IllegalArgumentException("Invalid id number for paddle section");

            };
        }

    }

}
