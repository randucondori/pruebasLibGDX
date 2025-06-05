package theHardBoys.TheMosterMaze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pintor {
    private int x,y,alto,ancho;
    private Texture texture;

    public Pintor(int x, int y, int alto, int ancho, Texture texture) {
        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;
        this.texture = texture;
    }

    public void pintarDibujo(SpriteBatch spriteBatch){
        spriteBatch.begin();
        spriteBatch.draw(texture,x,y);
        spriteBatch.end();
    }
}
