package xd.arkosammy.breakout.screen;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public record ScreenElement(int xCoordinate, int yCoordinate, ElementType elementType) {

    public enum ElementType {

        BRICK(new TextCharacter('#', TextColor.ANSI.RED, TextColor.ANSI.BLACK)),
        BALL(new TextCharacter('#', TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.BLACK)),
        PADDLE(new TextCharacter('W', TextColor.ANSI.BLUE_BRIGHT  , TextColor.ANSI.BLACK)),
        WALL(new TextCharacter('#', TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK)),
        BACKGROUND(new TextCharacter(' '));

        private final TextCharacter graphic;

        ElementType(TextCharacter graphic){
            this.graphic = graphic;
        }

        public TextCharacter getGraphic(){
            return this.graphic;
        }

    }

}
