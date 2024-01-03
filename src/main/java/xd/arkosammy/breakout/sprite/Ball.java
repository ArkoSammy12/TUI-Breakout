package xd.arkosammy.breakout.sprite;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.ArrayList;
import java.util.List;

public class Ball extends AbstractSprite {

    private Velocity velocity;
    public Ball(double[] coordinate, int[] dimensions) {
        super(coordinate, dimensions);
        this.velocity = Velocity.UP_LEFT_HALF;
    }

    public void move(){

        double[] newCoordinate = this.velocity.addToPosition(this.coordinate);
        this.setCoordinate(newCoordinate);

    }

    public void setVelocity(Velocity velocity){
        this.velocity = velocity;
    }

    @Override
    public List<ScreenElement> getScreenElements() {
        List<ScreenElement> screenElements = new ArrayList<>();
        for(int i = (int) Math.round(this.coordinate[0]); i < Math.round(this.coordinate[0]) + dimensions[0]; i++){
            for(int j = (int) Math.round(this.coordinate[1]); j < Math.round(this.coordinate[1]) + dimensions[1]; j++){
                screenElements.add(new ScreenElement(i, j, ScreenElement.ElementType.BALL));
            }
        }
        return screenElements;
    }

    @Override
    public void tick(BreakoutGame game) {

        this.checkCollision(game);
        this.move();

    }

    private void checkCollision(BreakoutGame game){

        int x = (int) Math.round(this.coordinate[0]);
        int y = (int) Math.round(this.coordinate[1]);

        boolean sideEdgeCollision = false;
        boolean topOrBottomEdgeCollision = false;
        boolean[] collidedCorners = new boolean[4];

        int[][] coordinateChecks = {
                {x - 1, y + 1}, // 0: top left corner
                {x, y + 1}, // 1: top edge
                {x + 1, y + 1}, // 2: top right corner
                {x - 1, y}, // 3: side edge
                {x + 1, y}, // 4: side edge
                {x - 1, y - 1}, // 5: down left corner
                {x, y - 1}, // 6: bottom edge
                {x + 1, y - 1} // 7: down right corner
        };

        for(int i = 0; i < coordinateChecks.length; i++){

            int[] coordinateCheck = coordinateChecks[i];

            int xCheck = coordinateCheck[0];
            int yCheck = coordinateCheck[1];

            if(xCheck < 0 || xCheck >= game.getGameField().getMapWidth() || yCheck < 0 || yCheck >= game.getGameField().getMapHeight()){
                continue;
            }

            ScreenElement element = game.getElementAt(xCheck, yCheck);

            if(element != null && element.elementType() != ScreenElement.ElementType.BACKGROUND && element.elementType() != ScreenElement.ElementType.BALL){

                switch(i){

                    case 0 -> collidedCorners[0] = true;
                    case 2 -> collidedCorners[1] = true;
                    case 5 -> collidedCorners[2] = true;
                    case 7 -> collidedCorners[3] = true;
                    case 1, 6 -> topOrBottomEdgeCollision = true;
                    case 3, 4 -> sideEdgeCollision = true;

                }

            }

        }

        Velocity newVelocity = null;

        if(sideEdgeCollision || topOrBottomEdgeCollision){

            if(sideEdgeCollision && topOrBottomEdgeCollision){
                newVelocity = this.velocity.getFlipped();
            } else if (sideEdgeCollision){
                newVelocity = this.velocity.getXFlipped();
            } else {
                newVelocity = this.velocity.getYFlipped();
            }

        } else {

            int cornerCollisions = 0;

            for(boolean cornerCollision : collidedCorners){
                if(cornerCollision){
                    cornerCollisions++;
                }
            }

            if(cornerCollisions >= 3){
                newVelocity = this.velocity.getFlipped();
            } else if (cornerCollisions == 2){

                if((collidedCorners[0] && collidedCorners[2]) || (collidedCorners[1] && collidedCorners[3])){
                    newVelocity = this.velocity.getXFlipped();
                } else if ((collidedCorners[0] && collidedCorners[1]) || (collidedCorners[2] && collidedCorners[3])){
                    newVelocity = this.velocity.getYFlipped();
                }

            } else if (cornerCollisions == 1) {
                newVelocity = this.velocity.getFlipped();
            }

        }

        if(newVelocity != null){
            this.velocity = newVelocity;
        }

    }


    public enum Velocity {

        UP(new double[]{0, 1}),
        UP_RIGHT_TWO(new double[]{0.5, 1}),
        DIAGONAL_UP_RIGHT(new double[]{1, 1}),
        UP_RIGHT_HALF(new double[]{1, 0.5}),
        UP_LEFT_TWO(new double[]{-0.5, 1}),
        DIAGONAL_UP_LEFT(new double[]{-1, 1}),
        UP_LEFT_HALF(new double[]{-1, 0.5}),
        DOWN(new double[]{0, -1}),
        DOWN_RIGHT_TWO(new double[]{0.5, -1}),
        DIAGONAL_DOWN_RIGHT(new double[]{1, -1}),
        DOWN_RIGHT_HALF(new double[]{1, -0.5}),
        DOWN_LEFT_TWO(new double[]{-0.5, -1}),
        DIAGONAL_DOWN_LEFT(new double[]{-1, -1}),
        DOWN_LEFT_HALF(new double[]{-1, -0.5});

        private final double[] vector;

        Velocity(double[] vector){
            this.vector = vector;
        }

        public double[] addToPosition(double[] subPixel){
            return new double[]{subPixel[0] + this.vector[0], subPixel[1] + this.vector[1]};
        }

        Velocity getYFlipped(){

            return switch(this){
                case UP -> DOWN;
                case UP_RIGHT_TWO -> DOWN_RIGHT_TWO;
                case DIAGONAL_UP_RIGHT -> DIAGONAL_DOWN_RIGHT;
                case UP_RIGHT_HALF -> DOWN_RIGHT_HALF;
                case UP_LEFT_TWO -> DOWN_LEFT_TWO;
                case DIAGONAL_UP_LEFT -> DIAGONAL_DOWN_LEFT;
                case UP_LEFT_HALF -> DOWN_LEFT_HALF;
                case DOWN -> UP;
                case DOWN_RIGHT_TWO -> UP_RIGHT_TWO;
                case DIAGONAL_DOWN_RIGHT -> DIAGONAL_UP_RIGHT;
                case DOWN_RIGHT_HALF -> UP_RIGHT_HALF;
                case DOWN_LEFT_TWO -> UP_LEFT_TWO;
                case DIAGONAL_DOWN_LEFT -> DIAGONAL_UP_LEFT;
                case DOWN_LEFT_HALF -> UP_LEFT_HALF;
            };

        }

        Velocity getXFlipped(){
            
            return switch(this){
                case UP -> UP;
                case UP_RIGHT_TWO -> UP_LEFT_TWO;
                case DIAGONAL_UP_RIGHT -> DIAGONAL_UP_LEFT;
                case UP_RIGHT_HALF -> UP_LEFT_HALF;
                case UP_LEFT_TWO -> UP_RIGHT_TWO;
                case DIAGONAL_UP_LEFT -> DIAGONAL_UP_RIGHT;
                case UP_LEFT_HALF -> UP_RIGHT_HALF;
                case DOWN -> DOWN;
                case DOWN_RIGHT_TWO -> DOWN_LEFT_TWO;
                case DIAGONAL_DOWN_RIGHT -> DIAGONAL_DOWN_LEFT;
                case DOWN_RIGHT_HALF -> DOWN_LEFT_HALF;
                case DOWN_LEFT_TWO -> DOWN_RIGHT_TWO;
                case DIAGONAL_DOWN_LEFT -> DIAGONAL_DOWN_RIGHT;
                case DOWN_LEFT_HALF -> DOWN_RIGHT_HALF;
            };
            
        }

        public Velocity getFlipped() {
            return switch (this) {
                case UP -> DOWN;
                case UP_RIGHT_TWO -> DOWN_LEFT_TWO;
                case DIAGONAL_UP_RIGHT -> DIAGONAL_DOWN_LEFT;
                case UP_RIGHT_HALF -> DOWN_LEFT_HALF;
                case UP_LEFT_TWO -> UP_RIGHT_TWO;
                case DIAGONAL_UP_LEFT -> DIAGONAL_UP_RIGHT;
                case UP_LEFT_HALF -> UP_RIGHT_HALF;
                case DOWN -> UP;
                case DOWN_RIGHT_TWO -> UP_LEFT_TWO;
                case DIAGONAL_DOWN_RIGHT -> DIAGONAL_UP_LEFT;
                case DOWN_RIGHT_HALF -> UP_LEFT_HALF;
                case DOWN_LEFT_TWO -> DOWN_RIGHT_TWO;
                case DIAGONAL_DOWN_LEFT -> DIAGONAL_DOWN_RIGHT;
                case DOWN_LEFT_HALF -> DOWN_RIGHT_HALF;
            };
        }

    }

}
