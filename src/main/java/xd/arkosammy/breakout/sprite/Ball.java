package xd.arkosammy.breakout.sprite;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ball extends AbstractSprite {

    private Velocity velocity;
    public Ball(double[] coordinate, int[] dimensions) {
        super(coordinate, dimensions);
        this.velocity = Velocity.DOWN_LEFT_LOW;
    }

    public void move(){

        double[] newCoordinate = this.velocity.addToPosition(this.coordinate);
        this.setCoordinate(newCoordinate);

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

        this.checkPaddleCollision(game);
        this.checkBrickCollision(game);
        this.move();

    }

    private void checkPaddleCollision(BreakoutGame game){

        int x = (int) Math.round(this.coordinate[0]);
        int y = (int) Math.round(this.coordinate[1]);
        int coordinateCheckIndex = -1;

        boolean hitPaddle = false;

        int[][] coordinateChecks = {
                {x - 1, y + 1},
                {x, y + 1},
                {x + 1, y + 1}
        };

        for(int i = 0; i < coordinateChecks.length; i++){

            int[] coordinateCheck = coordinateChecks[i];
            int xCheck = coordinateCheck[0];
            int yCheck = coordinateCheck[1];

            if(xCheck < 0 || xCheck >= game.getGameField().getMapWidth() || yCheck < 0 || yCheck >= game.getGameField().getMapHeight()){
                continue;
            }
            ScreenElement element = game.getElementAt(xCheck, yCheck);
            if(element.elementType() == ScreenElement.ElementType.PADDLE){
                hitPaddle = true;
                coordinateCheckIndex = i;
                break;
            }

        }

        if(!hitPaddle){
            return;
        }
        Paddle paddle = game.getPaddle();
        if(paddle == null){
            return;
        }

        Paddle.PaddleSection section = paddle.getCollidingPaddleSection(coordinateChecks[coordinateCheckIndex][0]);

        if(section != null) {
            switch (section) {

                case ONE -> {
                    if(this.velocity == Velocity.DOWN){
                        this.velocity = Velocity.UP_LEFT_LOW;
                    } else {
                        this.velocity = this.velocity.getFlipped();
                    }
                }
                case TWO -> {
                    if(this.velocity == Velocity.DOWN){
                        this.velocity = Velocity.UP_LEFT_HIGH;
                    } else {
                        this.velocity = this.velocity.getYFlipped();
                    }
                }
                case THREE -> this.velocity = Velocity.UP;
                case FOUR -> {
                    if(this.velocity == Velocity.DOWN){
                        this.velocity = Velocity.UP_RIGHT_HIGH;
                    } else {
                        this.velocity = this.velocity.getYFlipped();
                    }
                }
                case FIVE -> {
                    if(this.velocity == Velocity.DOWN){
                        this.velocity = Velocity.UP_RIGHT_LOW;
                    } else {
                        this.velocity = this.velocity.getFlipped();
                    }
                }

            }
        }

    }

    private void checkBrickCollision(BreakoutGame game){

        int x = (int) Math.round(this.coordinate[0]);
        int y = (int) Math.round(this.coordinate[1]);

        boolean sideEdgeCollision = false;
        boolean topOrBottomEdgeCollision = false;
        boolean[] collidedCorners = new boolean[4];

        int[][] coordinateChecks = {
                {x - 1, y + 1}, // 0: top left corner
                {x, y + 1},     // 1: top edge
                {x + 1, y + 1}, // 2: top right corner
                {x - 1, y},     // 3: side edge
                {x + 1, y},     // 4: side edge
                {x - 1, y - 1}, // 5: down left corner
                {x, y - 1},     // 6: bottom edge
                {x + 1, y - 1}  // 7: down right corner
        };

        for(int i = 0; i < coordinateChecks.length; i++){

            int[] coordinateCheck = coordinateChecks[i];
            int xCheck = coordinateCheck[0];
            int yCheck = coordinateCheck[1];
            if(xCheck < 0 || xCheck >= game.getGameField().getMapWidth() || yCheck < 0 || yCheck >= game.getGameField().getMapHeight()){
                continue;
            }
            ScreenElement element = game.getElementAt(xCheck, yCheck);
            if(element != null && (element.elementType() == ScreenElement.ElementType.BRICK || element.elementType() == ScreenElement.ElementType.WALL)){
                if(element.elementType() == ScreenElement.ElementType.BRICK) {
                    game.removeBrickAt(xCheck, yCheck);
                }
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

        Velocity newVelocity = getNewVelocityForBrickCollision(sideEdgeCollision, topOrBottomEdgeCollision, collidedCorners);
        if(newVelocity != null){
            this.velocity = newVelocity;
        }

    }

    private Velocity getNewVelocityForBrickCollision(boolean sideEdgeCollision, boolean topOrBottomEdgeCollision, boolean[] collidedCorners) {
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
        return newVelocity;
    }

    public enum Velocity {

        UP(new double[]{0, -1}),
        UP_RIGHT_HIGH(new double[]{0.5, -1}),
        UP_RIGHT_STRAIGHT(new double[]{1, -1}),
        UP_RIGHT_LOW(new double[]{1, -0.5}),
        UP_LEFT_HIGH(new double[]{-0.5, -1}),
        UP_LEFT_STRAIGHT(new double[]{-1, -1}),
        UP_LEFT_LOW(new double[]{-1, -0.5}),
        DOWN(new double[]{0, 1}),
        DOWN_RIGHT_HIGH(new double[]{0.5, 1}),
        DOWN_RIGHT_STRAIGHT(new double[]{1, 1}),
        DOWN_RIGHT_LOW(new double[]{1, 0.5}),
        DOWN_LEFT_HIGH(new double[]{-0.5, 1}),
        DOWN_LEFT_STRAIGHT(new double[]{-1, 1}),
        DOWN_LEFT_LOW(new double[]{-1, 0.5});

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
                case UP_RIGHT_HIGH -> DOWN_RIGHT_HIGH;
                case UP_RIGHT_STRAIGHT -> DOWN_RIGHT_STRAIGHT;
                case UP_RIGHT_LOW -> DOWN_RIGHT_LOW;
                case UP_LEFT_HIGH -> DOWN_LEFT_HIGH;
                case UP_LEFT_STRAIGHT -> DOWN_LEFT_STRAIGHT;
                case UP_LEFT_LOW -> DOWN_LEFT_LOW;
                case DOWN -> UP;
                case DOWN_RIGHT_HIGH -> UP_RIGHT_HIGH;
                case DOWN_RIGHT_STRAIGHT -> UP_RIGHT_STRAIGHT;
                case DOWN_RIGHT_LOW -> UP_RIGHT_LOW;
                case DOWN_LEFT_HIGH -> UP_LEFT_HIGH;
                case DOWN_LEFT_STRAIGHT -> UP_LEFT_STRAIGHT;
                case DOWN_LEFT_LOW -> UP_LEFT_LOW;
            };
        }

        Velocity getXFlipped(){
            return switch(this){
                case UP -> UP;
                case UP_RIGHT_HIGH -> UP_LEFT_HIGH;
                case UP_RIGHT_STRAIGHT -> UP_LEFT_STRAIGHT;
                case UP_RIGHT_LOW -> UP_LEFT_LOW;
                case UP_LEFT_HIGH -> UP_RIGHT_HIGH;
                case UP_LEFT_STRAIGHT -> UP_RIGHT_STRAIGHT;
                case UP_LEFT_LOW -> UP_RIGHT_LOW;
                case DOWN -> DOWN;
                case DOWN_RIGHT_HIGH -> DOWN_LEFT_HIGH;
                case DOWN_RIGHT_STRAIGHT -> DOWN_LEFT_STRAIGHT;
                case DOWN_RIGHT_LOW -> DOWN_LEFT_LOW;
                case DOWN_LEFT_HIGH -> DOWN_RIGHT_HIGH;
                case DOWN_LEFT_STRAIGHT -> DOWN_RIGHT_STRAIGHT;
                case DOWN_LEFT_LOW -> DOWN_RIGHT_LOW;
            };
        }

        public Velocity getFlipped() {
            return switch (this) {
                case UP -> DOWN;
                case UP_RIGHT_HIGH -> DOWN_LEFT_HIGH;
                case UP_RIGHT_STRAIGHT -> DOWN_LEFT_STRAIGHT;
                case UP_RIGHT_LOW -> DOWN_LEFT_LOW;
                case UP_LEFT_HIGH -> UP_RIGHT_HIGH;
                case UP_LEFT_STRAIGHT -> UP_RIGHT_STRAIGHT;
                case UP_LEFT_LOW -> UP_RIGHT_LOW;
                case DOWN -> UP;
                case DOWN_RIGHT_HIGH -> UP_LEFT_HIGH;
                case DOWN_RIGHT_STRAIGHT -> UP_LEFT_STRAIGHT;
                case DOWN_RIGHT_LOW -> UP_LEFT_LOW;
                case DOWN_LEFT_HIGH -> DOWN_RIGHT_HIGH;
                case DOWN_LEFT_STRAIGHT -> DOWN_RIGHT_STRAIGHT;
                case DOWN_LEFT_LOW -> DOWN_RIGHT_LOW;
            };
        }

    }

}
