package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Llave {
    private Circle area_para_recoger;
    float x, y;
    float alto = 20, ancho = 20;
    Texture textura = new Texture("imagenes/player.png");

    public Llave(float x, float y) {
        this.x = x;
        this.y = y;
        area_para_recoger = new Circle(x + ancho / 2, y + alto / 2, 30);
    }

    public void pintarLlave(SpriteBatch batch) {
        batch.draw(textura, x, y, ancho, alto);
    }

    public boolean recogerLlave(Personaje jugador) {
        if (area_para_recoger.overlaps(jugador.area_jugador) && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            jugador.llaves++;
            return true;
        }
        return false;
    }
}
