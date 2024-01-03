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
        this.coordinate = coordinate;
        this.dimensions = dimensions;
    }

    public boolean shouldExist(){
        return this.shouldExist;
    }

    public void markForRemoval(){
        this.shouldExist = false;
    }

    public void setCoordinate(int x, int y){
        this.coordinate[0] = x;
        this.coordinate[1] = y;
    }

    public void setCoordinate(double[] coordinate){
        this.coordinate = coordinate;
    }

    public double[] getCoordinate(){
        return this.coordinate;
    }


    @Override
    abstract public List<ScreenElement> getScreenElements();

    public static int[] subPixelToPixelCoordinate(int[] subPixel){
        return new int[]{subPixel[0] * 2, subPixel[1] * 2};
    }

    abstract public void tick(BreakoutGame game);

}
