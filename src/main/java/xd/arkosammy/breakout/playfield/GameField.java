package xd.arkosammy.breakout.playfield;

import xd.arkosammy.breakout.screen.ScreenDrawable;
import xd.arkosammy.breakout.screen.ScreenElement;

import java.util.ArrayList;
import java.util.List;

public class GameField implements ScreenDrawable {

    private final int xCoordinate;
    private final int yCoordinate;

    private final int width;

    private final int height;
    private final List<String> mapStrings;

    private GameField(int x, int y, List<String> mapStrings, int width){
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.mapStrings = mapStrings;
        this.width = width;
        this.height = mapStrings.size();
    }

    public int getXCoordinate(){
        return this.xCoordinate;
    }

    public int getYCoordinate(){
        return this.yCoordinate;
    }

    public int getMapWidth(){
        return this.width;
    }

    public int getMapHeight(){
        return this.height;
    }

    @Override
    public List<ScreenElement> getScreenElements() {
        List<ScreenElement> screenElements = new ArrayList<>();
        for(int i = 0; i < this.mapStrings.size(); i++){
            for(int j = 0; j < this.mapStrings.get(i).length(); j++){
                screenElements.add(this.getMapElementAt(j, i));
            }
        }
        return screenElements;
    }

    public ScreenElement getMapElementAt(int x, int y){

        char mapElement = this.mapStrings.get(y).charAt(x);

        if(mapElement == '#'){
            return new ScreenElement(x, y, ScreenElement.ElementType.WALL);
        } else {
            return new ScreenElement(x, y, ScreenElement.ElementType.BACKGROUND);
        }

    }

    public static class FieldBuilder {

        private int xCoordinate;
        private int yCoordinate;
        private int maxWidth;

        private List<String> mapStrings = new ArrayList<>();

        public FieldBuilder(int xCoordinate, int yCoordinate, int maxWidth){
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
            this.maxWidth = maxWidth;
        }

        public FieldBuilder withMapRow(String mapString){
            if(mapString.length() != this.maxWidth){
                throw new IllegalArgumentException();
            }
            this.mapStrings.add(mapString);
            return this;
        }

        public GameField create(){
            return new GameField(this.xCoordinate, this.yCoordinate, this.mapStrings, maxWidth);
        }


    }

}
