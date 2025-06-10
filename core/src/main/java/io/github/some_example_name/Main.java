package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Personaje jugador;
    private final float ANCHO_PERSONAJE = 25f;
    private final float ALTO_PERSONAJE = 25f;
    private final float velocidad = 100f;

    private Laverinto laverinto;
    private ArrayList<Muro> muros;
    private ArrayList<Llave> llaves = new ArrayList<>();
    private float mapaAncho = 2000;
    private float mapaAlto = 2000;
    private Vector3 objetivoCamara;

    private Texture pantalladDeInicio;
    private Sprite menusprite;
    private float transparencia = 0;

    private Texture botonInicio;
    private Texture botonFin;
    private Sprite boton1sprite;
    private Sprite boton2sprite;
    private boolean inicio;

    private Enemigo[] enemigos = new Enemigo[4];

    @Override
    public void create() {
        objetivoCamara = new Vector3();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        Texture texturaJugador = new Texture(Gdx.files.internal("imagenes/player.png"));
        pantalladDeInicio = new Texture("mapa/maps.png");
        botonInicio = new Texture("imagenes/puerta.png");
        botonFin = new Texture("imagenes/puerta.png");
        boton1sprite = new Sprite(botonInicio);
        boton2sprite = new Sprite(botonFin);

        enemigos[0] = new Enemigo(0, 0);
        enemigos[1] = new Enemigo(0, mapaAlto);
        enemigos[2] = new Enemigo(mapaAncho, 0);
        enemigos[3] = new Enemigo(mapaAncho, mapaAlto);

        jugador = new Personaje(
            (mapaAncho - ANCHO_PERSONAJE) / 2,
            (mapaAlto - ALTO_PERSONAJE) / 2,
            ANCHO_PERSONAJE,
            ALTO_PERSONAJE,
            velocidad,
            texturaJugador
        );

        laverinto = new Laverinto();
        muros = laverinto.Muros();
        llaves = laverinto.llaves();

        configurarControles();
        actualizarCamara();
    }

    private void menu() {
        if (Gdx.input.isTouched()) {
            if ((float) Gdx.input.getX() > this.boton1sprite.getX() && (float) Gdx.input.getX() < this.boton1sprite.getX() + this.boton1sprite.getWidth() && (float) Gdx.input.getY() < (float) Gdx.graphics.getHeight() - this.boton1sprite.getHeight() && (float) Gdx.input.getY() > (float) Gdx.graphics.getHeight() - this.boton1sprite.getY() - this.boton1sprite.getHeight()) {
                this.inicio = true;
            }

            if ((float) Gdx.input.getX() > this.boton2sprite.getX() && (float) Gdx.input.getX() < this.boton2sprite.getX() + this.boton2sprite.getWidth() && (float) Gdx.input.getY() < (float) Gdx.graphics.getHeight() - this.boton2sprite.getHeight() && (float) Gdx.input.getY() > (float) Gdx.graphics.getHeight() - this.boton2sprite.getY() - this.boton2sprite.getHeight()) {
                this.dispose();
            }
        }

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (pantallaInicio()) {
            this.menu();
            if (this.inicio && jugador.vida != 0) {
                batch.setProjectionMatrix(camera.combined);
                laverinto.pintarFondo(batch, mapaAncho, mapaAlto);
                batch.begin();
                for (Enemigo enemigo : enemigos) {
                    if (enemigo.reconocerArea(mapaAncho, mapaAlto, jugador)) {
                        jugador.rect.x = (mapaAncho - ANCHO_PERSONAJE) / 2;
                        jugador.rect.y = (mapaAlto - ALTO_PERSONAJE) / 2;
                        enemigos[0] = new Enemigo(0, 0);
                        enemigos[1] = new Enemigo(0, mapaAlto);
                        enemigos[2] = new Enemigo(mapaAncho, 0);
                        enemigos[3] = new Enemigo(mapaAncho, mapaAlto);
                        System.out.println(jugador.vida);
                    }
                    enemigo.pintarEnemigo(batch);
                    enemigo.reconocerArea(mapaAncho, mapaAlto, jugador);
                }
                jugador.actualizarMovimiento(Gdx.graphics.getDeltaTime() * 2f, muros, mapaAncho, mapaAlto);
                jugador.dibujar(batch, Gdx.graphics.getDeltaTime());

                Llave eliminarLlave=null;
                for (Llave l : llaves) {
                    l.pintarLlave(batch);
                    if (l.recogerLlave(jugador)){
                        eliminarLlave = l;
                        System.out.println(jugador.llaves);
                    }
                }
                if(eliminarLlave!=null){
                    laverinto.delLlave(eliminarLlave);
                }
                batch.end();
                actualizarCamara();
            } else if (jugador.vida == 0) {
                dispose();
            }
        }
    }

    private boolean pantallaInicio() {
        this.menusprite = new Sprite(this.pantalladDeInicio);
        this.menusprite.setSize((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        this.boton1sprite = new Sprite(this.botonInicio);
        this.boton1sprite.setSize(200.0F, 100.0F);
        this.boton1sprite.translate((float) Gdx.graphics.getWidth() / 2.0F - this.boton1sprite.getWidth() / 2.0F, 225.0F);
        this.boton2sprite = new Sprite(this.botonFin);
        this.boton2sprite.setSize(200.0F, 100.0F);
        this.boton2sprite.translate((float) Gdx.graphics.getWidth() / 2.0F - this.boton1sprite.getWidth() / 2.0F, 100.0F);
        boolean respuesta = false;
        this.batch.begin();
        this.transparencia += 0.01F;
        if (this.transparencia > 1.0F) {
            this.transparencia = 1.0F;
            respuesta = true;
        }

        this.menusprite.setAlpha(this.transparencia);
        this.boton1sprite.setAlpha(this.transparencia);
        this.boton2sprite.setAlpha(this.transparencia);
        this.menusprite.draw(this.batch);
        this.boton1sprite.draw(this.batch);
        this.boton2sprite.draw(this.batch);
        this.batch.end();
        return respuesta;
    }

    private void configurarControles() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:
                        jugador.setMoviendoArriba(true);
                        break;
                    case Input.Keys.DOWN:
                        jugador.setMoviendoAbajo(true);
                        break;
                    case Input.Keys.LEFT:
                        jugador.setMoviendoIzquierda(true);
                        break;
                    case Input.Keys.RIGHT:
                        jugador.setMoviendoDerecha(true);
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:
                        jugador.setMoviendoArriba(false);
                        break;
                    case Input.Keys.DOWN:
                        jugador.setMoviendoAbajo(false);
                        break;
                    case Input.Keys.LEFT:
                        jugador.setMoviendoIzquierda(false);
                        break;
                    case Input.Keys.RIGHT:
                        jugador.setMoviendoDerecha(false);
                        break;
                }

                return true;
            }
        });
    }

    private void actualizarCamara() {
        objetivoCamara.set(
            jugador.getRect().x + jugador.getRect().width / 2,
            jugador.getRect().y + jugador.getRect().height / 2,
            0
        );

        camera.position.lerp(objetivoCamara, 5f * Gdx.graphics.getDeltaTime());

        float halfViewportWidth = camera.viewportWidth / 2;
        float halfViewportHeight = camera.viewportHeight / 2;

        camera.position.x = MathUtils.clamp(camera.position.x, halfViewportWidth, mapaAncho - halfViewportWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, halfViewportHeight, mapaAlto - halfViewportHeight);

        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        jugador.dispose();
        laverinto.dispoce();
        pantalladDeInicio.dispose();
        botonInicio.dispose();
        botonFin.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

}

