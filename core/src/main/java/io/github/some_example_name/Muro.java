package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;


public class Muro {
    private Rectangle rectangle;
    float x;
    float y;
    float width;
    float height;

    public Muro(float x, float y, float width, float height) {
        rectangle = new Rectangle(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public Rectangle getRectangle() {
        return rectangle;
    }
}
