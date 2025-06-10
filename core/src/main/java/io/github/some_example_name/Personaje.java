package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class Personaje implements Disposable {
    public Rectangle rect;
    private float velocidad;
    private Texture playerTexture;
    private Animation<TextureRegion> animacion;
    private TextureRegion jugadorframe;
    private float tiempo;
    private boolean moviendoArriba, moviendoAbajo, moviendoIzquierda, moviendoDerecha;
    public Circle area_jugador;

    private final float ancho;
    private final float alto;

    public Personaje(float x, float y, float ancho, float alto, float velocidad, Texture texturaInicial) {
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = velocidad;
        this.playerTexture = texturaInicial;
        this.area_jugador = new Circle();
        this.area_jugador.setPosition(x+ancho/2, y+alto/2);
        this.area_jugador.setRadius(ancho*2);
        rect = new Rectangle(x, y, ancho, alto);
        tiempo = 0f;
        crearAnimacion();

        moviendoArriba = moviendoAbajo = moviendoIzquierda = moviendoDerecha = false;

    }

    private void crearAnimacion() {
        int columnas = 3;
        int filas = 1;

        TextureRegion[][] regiones = TextureRegion.split(
            playerTexture,
            playerTexture.getWidth() / columnas,
            playerTexture.getHeight() / filas
        );

        if (regiones.length < 1 || regiones[0].length < columnas) {
            throw new IllegalArgumentException("ups¡");
        }

        TextureRegion[] frames = new TextureRegion[columnas];
        for (int i = 0; i < columnas; i++) {
            frames[i] = regiones[0][i];
        }

        animacion = new Animation<>(0.5f, frames);
    }


    public void actualizarMovimiento(float delta, Iterable<Muro> muros, float mapaAncho, float mapaAlto) {
        float oldX = rect.x;
        float oldY = rect.y;

        if (moviendoArriba) rect.y += velocidad * delta;
        if (moviendoAbajo) rect.y -= velocidad * delta;
        if (moviendoIzquierda) rect.x -= velocidad * delta;
        if (moviendoDerecha) rect.x += velocidad * delta;

        for (Muro m : muros) {
            if (rect.overlaps(m.getRectangle())) {
                rect.x = oldX;
                rect.y = oldY;
                System.out.println("tocaste un muro");
                break;
            }
        }

        // Limitar a mapa
        rect.x = MathUtils.clamp(rect.x, 0, mapaAncho - rect.width);
        rect.y = MathUtils.clamp(rect.y, 0, mapaAlto - rect.height);
        area_jugador.setPosition(rect.x+ancho/2, rect.y+alto/2);

    }

    public void dibujar(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, float delta) {
        if (hayMovimiento()) {
            tiempo += delta;
            jugadorframe = animacion.getKeyFrame(tiempo, true);
            batch.draw(jugadorframe, rect.x, rect.y, rect.width, rect.height);
        } else {
            batch.draw(playerTexture, rect.x, rect.y, rect.width, rect.height);
        }
    }

    public boolean hayMovimiento() {
        return moviendoArriba || moviendoAbajo || moviendoIzquierda || moviendoDerecha;
    }

    public void setMovimiento(boolean arriba, boolean abajo, boolean izquierda, boolean derecha) {
        this.moviendoArriba = arriba;
        this.moviendoAbajo = abajo;
        this.moviendoIzquierda = izquierda;
        this.moviendoDerecha = derecha;
    }

    public Rectangle getRect() {
        return rect;
    }
    public void setMoviendoArriba(boolean estado) {
        this.moviendoArriba = estado;
    }
    public void setMoviendoAbajo(boolean estado) {
        this.moviendoAbajo = estado;
    }
    public void setMoviendoIzquierda(boolean estado) {
        this.moviendoIzquierda = estado;
    }
    public void setMoviendoDerecha(boolean estado) {
        this.moviendoDerecha = estado;
    }


    @Override
    public void dispose() {
        playerTexture.dispose();
    }
}

