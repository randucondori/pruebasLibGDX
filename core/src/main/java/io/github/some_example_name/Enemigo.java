package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

public class Enemigo {
    Circle zonaseguimiento;
    Texture enemitexture;
    float x,y,alto,ancho;
    float objetivo_x,objetivo_y = 0;
    Random random = new Random();


    Enemigo(float x,float y) {
        this.x = x;
        this.y = y;
        this.zonaseguimiento = new Circle();
        this.zonaseguimiento.setPosition(x+ancho/2, y+alto/2);
        this.zonaseguimiento.setRadius(120);
        this.alto = 20;
        this.ancho = 20;
        this.enemitexture = new Texture("imagenes/enemigo.png");
    }
     void cambiarObjetivo(float x,float y) {
        this.objetivo_x = x;
        this.objetivo_y = y;
    }
     void pintarEnemigo(SpriteBatch batch) {
        batch.draw(enemitexture, x, y, ancho, alto);
    }
     void mover(){
        if (x != objetivo_x) {
            if (x < objetivo_x) {
                x=x+0.5F;
            }

            if (x > objetivo_x) {
                x=x-0.5F;
            }
        }
        if (y != objetivo_y) {
            if (y < objetivo_y) {
                y=y+0.5F;
            }

            if (y > objetivo_y) {
                y=y-0.5F;
            }
        }
        this.zonaseguimiento.setPosition(x+ancho/2, y+alto/2);
    }
     void reconocerArea(float mapaAncho,float mapaAlto,Personaje jugador) {
        boolean encontrado = zonaseguimiento.overlaps(jugador.area_jugador);
        if(!encontrado) {
            if (x == (int)objetivo_x && y == (int)objetivo_y) {
                cambiarObjetivo(random.nextInt(0, (int) mapaAncho), random.nextInt(0, (int) mapaAlto));
            }
        } else{
             cambiarObjetivo(jugador.rect.x, jugador.rect.y);
         }
        mover();
    }
}
