package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Enemigo {
    Circle zonaseguimiento;
    Texture enemitexture;
    float x,y,alto,ancho;
    Random random = new Random();
    float objetivo_x = random.nextInt(0, 2000),objetivo_y=random.nextInt(0, 2000);
    Rectangle enemigoRect;
    int bajarvida=0;


    Enemigo(float x,float y) {
        this.x = x;
        this.y = y;
        this.zonaseguimiento = new Circle();
        this.zonaseguimiento.setPosition(x+ancho/2, y+alto/2);
        this.zonaseguimiento.setRadius(140);
        this.enemigoRect = new Rectangle();
        this.alto = 20;
        this.ancho = 20;
        this.enemitexture = new Texture("imagenes/enemigo.png");
        enemigoRect.setPosition(x+4, y);
        enemigoRect.setSize(ancho-4, alto);
    }
     void cambiarObjetivo(float x,float y) {
        this.objetivo_x = x;
        this.objetivo_y = y;
    }
     void pintarEnemigo(SpriteBatch batch) {
        batch.draw(enemitexture, x, y, ancho, alto);
    }
     void mover(){
        float velocidad = 1f;
        if ((int)x != (int)objetivo_x) {
            if (x < objetivo_x) {
                x+= velocidad;
            }

            if ((int)x > (int)objetivo_x) {
                x-= velocidad;
            }
        }
        if ((int)y != (int)objetivo_y) {
            if (y < objetivo_y) {
                y+= velocidad;
            }

            if ((int)y > (int)objetivo_y) {
                y-= velocidad;
            }
        }
        this.zonaseguimiento.setPosition(x+ancho/2, y+alto/2);
         enemigoRect.setPosition(x, y);

    }
     boolean reconocerArea(float mapaAncho,float mapaAlto,Personaje jugador){
        float oldx = this.x;
        float oldy = this.y;
        boolean encontrado = zonaseguimiento.overlaps(jugador.area_jugador);
        if(!encontrado) {
            enemitexture = new Texture("imagenes/enemigo.png");
            if ((int)x == (int)objetivo_x && (int)y == (int)objetivo_y) {
                cambiarObjetivo(random.nextInt(0, (int) mapaAncho), random.nextInt(0, (int) mapaAlto));
            }
        } else{
            enemitexture = new Texture("imagenes/enemigo_2.png");
             cambiarObjetivo(jugador.rect.x, jugador.rect.y);
            if (enemigoRect.overlaps(jugador.rect )) {
                try {
                    oldy = enemigoRect.y;
                    oldx = enemigoRect.x;
                    jugador.vida--;
                    if (jugador.vida - 1 <= 0) {
                        Thread.sleep(500);
                    }
                    return true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
         }
        mover();
        return false;
    }
    public void dispose(){
        enemitexture.dispose();
    }
}
