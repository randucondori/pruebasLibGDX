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

        configurarControles();
        actualizarCamara();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (pantallaInicio()) {
            batch.setProjectionMatrix(camera.combined);
            laverinto.pintarFondo(batch, mapaAncho, mapaAlto);

            batch.begin();

            jugador.actualizarMovimiento(Gdx.graphics.getDeltaTime() * 2f, muros, mapaAncho, mapaAlto);
            jugador.dibujar(batch, Gdx.graphics.getDeltaTime());

            batch.end();

            for (Muro muro : muros) {
                muro.pintarmuro(batch);
            }

            actualizarCamara();
        }
    }

    private boolean pantallaInicio() {
        menusprite = new Sprite(pantalladDeInicio);
        boolean respuesta = false;
        batch.begin();
        transparencia += 0.01F;
        if (transparencia > 1) {
            transparencia = 1;
            respuesta = true;
        }
        menusprite.setAlpha(transparencia);
        menusprite.draw(batch);
        batch.end();
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

