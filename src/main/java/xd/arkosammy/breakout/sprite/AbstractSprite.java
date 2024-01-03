package xd.arkosammy.breakout.sprite;

import xd.arkosammy.breakout.BreakoutGame;
import xd.arkosammy.breakout.screen.ScreenDrawable;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.List;

public abstract class AbstractSprite implements ScreenDrawable {

    protected double[] coordinate;
    protected int[] dimensions;
    protected boolean shouldExist = true;

    protected AbstractSprite(double[] coordinate, int[] dimensions){

        if(this instanceof Ball){
            if(dimensions[0] != 1 || dimensions[1] != 1){
                throw new IllegalArgumentException("Ball cannot have dimensions other than 1 * 1!");
            }
        } else if (this instanceof Paddle){
            if(dimensions[0] < 5 || dimensions[0] > 10){
                throw new IllegalArgumentException("Invalid width for paddle!");
            }
        }

        this.coordinate = coordinate;
        this.dimensions = dimensions;
    }

    public boolean shouldExist(){
        return this.shouldExist;
    }

    public void markForRemoval(){
        this.shouldExist = false;
    }

    public void setCoordinate(double[] coordinate){
        this.coordinate = coordinate;
    }

    public double[] getCoordinate(){
        return this.coordinate;
    }

    public int[] getDimensions(){
        return this.dimensions;
    }

    @Override
    abstract public List<ScreenElement> getScreenElements();

    abstract public void tick(BreakoutGame game);

}
