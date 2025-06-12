package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private SpriteBatch s = new SpriteBatch();
    private BitmapFont font = new BitmapFont();
    private final float ancho;
    private final float alto;

    public int vida = 3, llaves;

    public Personaje(float x, float y, float ancho, float alto, float velocidad, Texture texturaInicial) {
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = velocidad;
        this.playerTexture = texturaInicial;
        this.area_jugador = new Circle();
        this.area_jugador.setPosition(x + ancho / 2, y + alto / 2);
        this.area_jugador.setRadius(ancho * 2);
        rect = new Rectangle(x, y, ancho, alto);
        tiempo = 0f;
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

        animacion = new Animation<>(0.25f, frames);
    }

    public void pintarAtributos() {
        playerTexture = new Texture("imagenes/player.png");
        Texture l = new Texture("imagenes/llave.png");
        s.begin();
        font.draw(s, "Vidas " + this.vida + "/ 3", 30, Gdx.graphics.getHeight() - 10, 50, 50, true);
        font.draw(s, "Llaves " + this.llaves + " / 4", Gdx.graphics.getWidth() - 60, Gdx.graphics.getHeight() - 10, 50, 50, true);
        s.draw(playerTexture, 10, Gdx.graphics.getHeight() - 30, 20, 20);
        s.draw(l, Gdx.graphics.getWidth() - 90, Gdx.graphics.getHeight() - 30, 30, 20);
        s.end();
    }

    public void actualizarMovimiento(float delta, Iterable<Muro> muros, float mapaAncho, float mapaAlto, boolean muevete,Laverinto l,boolean salir) {
        float oldX = rect.x;
        float oldY = rect.y;

        if (moviendoArriba && !muevete) {
            rect.y += velocidad * delta;
            playerTexture = new Texture("imagenes/mov-detras.png");
        }
        if (moviendoAbajo && !muevete) {
            rect.y -= velocidad * delta;
            playerTexture = new Texture("imagenes/mov-delante.png");
        }
        if (moviendoIzquierda && !muevete) {
            rect.x -= velocidad * delta;
            playerTexture = new Texture("imagenes/mov-perfil-izq.png");
        }
        if (moviendoDerecha && !muevete) {

            rect.x += velocidad * delta;
            playerTexture = new Texture("imagenes/mov-perfil-der.png");

        }
        crearAnimacion();
        /*for (Muro m : muros) {
            if (rect.overlaps(m.getRectangle())) {
                rect.x = oldX;
                rect.y = oldY;
                break;
            }
        }*/
        if(rect.overlaps(l.getRectsalida()) && !salir){
            rect.x = oldX;
            rect.y = oldY;
        }

        // Limitar a mapa
        rect.x = MathUtils.clamp(rect.x, 0, mapaAncho - rect.width);
        rect.y = MathUtils.clamp(rect.y, 0, mapaAlto - rect.height);
        area_jugador.setPosition(rect.x + ancho / 2, rect.y + alto / 2);

    }

    public void dibujar(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, float delta) {
        if (hayMovimiento()) {
            tiempo += delta;
            jugadorframe = animacion.getKeyFrame(tiempo, true);
            batch.draw(jugadorframe, rect.x, rect.y, rect.width, rect.height);
        } else {
            playerTexture = new Texture("imagenes/player.png");
            batch.draw(playerTexture, rect.x, rect.y, rect.width, rect.height);
        }
    }

    public boolean hayMovimiento() {
        return moviendoArriba || moviendoAbajo || moviendoIzquierda || moviendoDerecha;
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

    public void recogerLlave(Llave l, SpriteBatch b) {
        BitmapFont font = new BitmapFont();
        if (l.getArea_para_recoger().overlaps(area_jugador)) {
            font.draw(b, "Recoger Objeto con 'F'", rect.x - 60, rect.y - 20);
        }
    }

    @Override
    public void dispose() {
        playerTexture.dispose();
        s.dispose();
        font.dispose();
        playerTexture.dispose();
    }
}

