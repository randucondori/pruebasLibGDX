package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    //texturas y utilidades
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture playerTexture;
    // Jugador
    private Rectangle jugador;
    private float velocidad = 200f;
    private final float ANCHO_PERSONAJE = 25f;
    private final float ALTO_PERSONAJE = 25f;

    // Mapa
    private Laverinto laverinto;
    private ArrayList<Muro> muros;
    private float mapaAncho = 2000;
    private float mapaAlto = 2000;
    private Vector3 objetivoCamara; // Usamos// Vector3 que es compatible con camera.position
    // Control de movimiento
    private boolean moviendoArriba = false;
    private boolean moviendoAbajo = false;
    private boolean moviendoIzquierda = false;
    private boolean moviendoDerecha = false;
    //animacion
    private Animation<TextureRegion> animacion;
    private TextureRegion jugadorframe;
    private float tiempo = 0f;
    //Animaciones de Menu y pantalla de inicio
    private Texture pantalladDeInicio;
    private Sprite menusprite;
    private float transparencia = 0;
    //Menu de Inicio
    private Texture botonInicio;
    private Texture botonFin;
    private Sprite boton1sprite;
    private Sprite boton2sprite;


    @Override
    public void create() {
        // Inicializar vector objetivo (Vector3 para compatibilidad con cámara)
        objetivoCamara = new Vector3();

        // Configurar cámara
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Cargar texturas
        playerTexture = new Texture(Gdx.files.internal("imagenes/player.png"));
        pantalladDeInicio = new Texture("mapa/maps.png");
        botonInicio = new Texture("imagenes/puerta.png");
        botonFin = new Texture("imagenes/puerta.png");
        boton1sprite = new Sprite(botonInicio);
        boton2sprite = new Sprite(botonFin);

        // Inicializar jugador
        jugador = new Rectangle();
        jugador.width = ANCHO_PERSONAJE;
        jugador.height = ALTO_PERSONAJE;
        jugador.x = (mapaAncho - jugador.width) / 2;
        jugador.y = (mapaAlto - jugador.height) / 2;

        //iniciar laverinto
        laverinto = new Laverinto();
        muros = laverinto.Muros();


        //Iniciar pintador y controles
        batch = new SpriteBatch();
        configurarControles();

        // Inicializar posición de la cámara
        actualizarCamara();
    }
    private void crearAnimacion(){
        TextureRegion[][] tmp = TextureRegion.split(playerTexture, playerTexture.getWidth()/3, playerTexture.getHeight());
        TextureRegion[] arrayregion = new TextureRegion[3];
        for (int i = 0; i < 3; i++) arrayregion[i] = tmp[0][i];
        animacion = new Animation(0.5f, arrayregion);
    }

    private boolean cambioDeAnimacion(){
        boolean cambio;
        if(moviendoArriba){
            playerTexture=new Texture(Gdx.files.internal("imagenes/puerta.png"));
            cambio = true;
        } else if (moviendoAbajo) {
            playerTexture = new Texture(Gdx.files.internal("imagenes/player.png"));
            cambio = true;
        }else if (moviendoIzquierda) {
            playerTexture=new Texture(Gdx.files.internal("imagenes/puerta.png"));
            cambio = true;
        } else if (moviendoDerecha) {
            playerTexture=new Texture(Gdx.files.internal("imagenes/player.png"));
            cambio = true;
        }else{
            playerTexture=new Texture(Gdx.files.internal("imagenes/puerta.png"));
            cambio = false;
        }
        if (cambio){
            crearAnimacion();
        }
        return cambio;
    }


    @Override
    public void render() {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Animacion de Inico
        if (pantallaInicio()) {
            //Despliegue de menu

            // Dibujar
            batch.setProjectionMatrix(camera.combined);
            laverinto.pintarFondo(batch, mapaAncho, mapaAlto);
            batch.begin();
            // Actualizar
            actualizarMovimiento(Gdx.graphics.getDeltaTime() * 2f);
            actualizarCamara();
            if (cambioDeAnimacion()) {
                tiempo += Gdx.graphics.getDeltaTime();
                jugadorframe = animacion.getKeyFrame(tiempo, true);
                batch.draw(jugadorframe, jugador.x, jugador.y, jugador.width, jugador.height);
            } else batch.draw(playerTexture, jugador.x, jugador.y, jugador.width, jugador.height);
            batch.end();
            for (Muro muro : muros) {
                muro.pintarmuro(batch);
            }
        }
    }

    private boolean pantallaInicio () {
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

    private boolean menu () {
        boolean respuesta = false;
        if (Gdx.input.isTouched()) {
            //if (Gdx.input.getX()<
        }
        return respuesta;
    }

    private void configurarControles () {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:
                        moviendoArriba = true;
                        break;
                    case Input.Keys.DOWN:
                        moviendoAbajo = true;
                        break;
                    case Input.Keys.LEFT:
                        moviendoIzquierda = true;
                        break;
                    case Input.Keys.RIGHT:
                        moviendoDerecha = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.UP:
                        moviendoArriba = false;
                        break;
                    case Input.Keys.DOWN:
                        moviendoAbajo = false;
                        break;
                    case Input.Keys.LEFT:
                        moviendoIzquierda = false;
                        break;
                    case Input.Keys.RIGHT:
                        moviendoDerecha = false;
                        break;
                }
                return true;
            }
        });
    }

    private void actualizarMovimiento ( float delta){
        float oldx = jugador.x;
        float oldy = jugador.y;

        if (moviendoArriba) {
            jugador.y += velocidad * delta;
        }
        else if (moviendoAbajo) {
            jugador.y -= velocidad * delta;
        }
        else if (moviendoIzquierda) jugador.x -= velocidad * delta;
        else if (moviendoDerecha) jugador.x += velocidad * delta;
        for (Muro m : muros) {
            if (jugador.overlaps(m.getRectangle())) {
                jugador.x = oldx;
                jugador.y = oldy;
                System.out.println("tocaste un muro");
            }
        }

        // Limitar al mapa
        jugador.x = MathUtils.clamp(jugador.x, 0, mapaAncho - jugador.width);
        jugador.y = MathUtils.clamp(jugador.y, 0, mapaAlto - jugador.height);
    }

    private void actualizarCamara () {
        // Configurar objetivo (centrado en jugador)
        objetivoCamara.set(
            jugador.x + jugador.width / 2,
            jugador.y + jugador.height / 2,
            0 // El componente Z siempre es 0 en cámaras 2D
        );

        // Interpolación suave (5f = factor de suavizado)
        camera.position.lerp(objetivoCamara, 5f * Gdx.graphics.getDeltaTime());

        // Limitar cámara a bordes del mapa
        float halfViewportWidth = camera.viewportWidth / 2;
        float halfViewportHeight = camera.viewportHeight / 2;

        camera.position.x = MathUtils.clamp(
            camera.position.x,
            halfViewportWidth,
            mapaAncho - halfViewportWidth
        );

        camera.position.y = MathUtils.clamp(
            camera.position.y,
            halfViewportHeight,
            mapaAlto - halfViewportHeight
        );

        camera.update();
    }

    @Override
    public void dispose () {
        batch.dispose();
        playerTexture.dispose();
        laverinto.dispoce();
    }

    @Override
    public void resize ( int width, int height){
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

}
