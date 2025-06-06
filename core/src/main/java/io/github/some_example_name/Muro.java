package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

public class Muro {
    private Rectangle rectangle;
    private Texture texture = new Texture("imagenes/mapa.png");
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
    public void pintarmuro(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x, y, width, height);
        batch.end();
    }
    public Rectangle getRectangle() {
        return rectangle;
    }
    public Texture getTexture() {
        return texture;
    }
}
