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
    private float transparencia_final = 0;
    private Texture pantallaFinal;
    private Sprite endsprite;

    private boolean muevete;
    private boolean salir;

    private Texture botonInicio;
    private Texture botonFin;
    private Sprite boton1sprite;
    private Sprite boton2sprite;
    private boolean inicio;

    private Texture logo;
    private Sprite logo_sprite;

    private Enemigo[] enemigos = new Enemigo[4];

    @Override
    public void create() {
        objetivoCamara = new Vector3();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        Texture texturaJugador = new Texture(Gdx.files.internal("imagenes/player.png"));
        pantallaFinal = new Texture("imagenes/mapa.png");
        pantalladDeInicio = new Texture("imagenes/Pantalla de Inicio.png");
        menusprite = new Sprite(this.pantalladDeInicio);
        botonInicio = new Texture("imagenes/boton_inicio.png");
        botonFin = new Texture("imagenes/boton_salir.png");
        boton1sprite = new Sprite(botonInicio);
        boton2sprite = new Sprite(botonFin);
        logo = new Texture("imagenes/logo.png");
        logo_sprite = new Sprite(logo);
        salir = false;

        enemigos[0] = new Enemigo(0, 0);
        enemigos[1] = new Enemigo(0, mapaAlto);
        enemigos[2] = new Enemigo(mapaAncho, 0);
        enemigos[3] = new Enemigo(mapaAncho, mapaAlto);
        muevete = false;
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
            if (Gdx.input.getX() >= this.boton1sprite.getX() && Gdx.input.getX() <= this.boton1sprite.getX() + this.boton1sprite.getWidth() && Gdx.input.getY() <= Gdx.graphics.getHeight() - boton1sprite.getY() && Gdx.input.getY() > (float) Gdx.graphics.getHeight() - this.boton1sprite.getY() - this.boton1sprite.getHeight()) {
                this.inicio = true;
            }

            if (Gdx.input.getX() >= this.boton2sprite.getX() && Gdx.input.getX() <= this.boton2sprite.getX() + this.boton2sprite.getWidth() && Gdx.input.getY() <= Gdx.graphics.getHeight() - boton2sprite.getY() && Gdx.input.getY() > (float) Gdx.graphics.getHeight() - this.boton2sprite.getY() - this.boton2sprite.getHeight()) {
                this.dispose();
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture oscuridadTexture = new Texture("imagenes/pantalla negra.jpg");
        Sprite oscuridad = new Sprite(oscuridadTexture);
        oscuridad.setAlpha(0.7555F);
        oscuridad.setSize((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        SpriteBatch oscuridad_s = new SpriteBatch();
        if (pantallaInicio() && jugador.vida != 0) {
            this.menu();
            if (this.inicio) {
                batch.setProjectionMatrix(camera.combined);
                batch.begin();
                laverinto.pintarFondo(batch, mapaAncho, mapaAlto);
                for (Enemigo enemigo : enemigos) {
                    if (enemigo.reconocerArea(mapaAncho, mapaAlto, jugador)) {
                        jugador.rect.x = (mapaAncho - ANCHO_PERSONAJE) / 2;
                        jugador.rect.y = (mapaAlto - ALTO_PERSONAJE) / 2;
                        enemigos[0] = new Enemigo(0, 0);
                        enemigos[1] = new Enemigo(0, mapaAlto);
                        enemigos[2] = new Enemigo(mapaAncho, 0);
                        enemigos[3] = new Enemigo(mapaAncho, mapaAlto);
                    }
                    enemigo.bajarvida = 0;
                    enemigo.pintarEnemigo(batch);
                    enemigo.reconocerArea(mapaAncho, mapaAlto, jugador);
                }
                jugador.actualizarMovimiento(Gdx.graphics.getDeltaTime() * 2f, muros, mapaAncho, mapaAlto,muevete,laverinto,salir);
                jugador.dibujar(batch, Gdx.graphics.getDeltaTime());

                Llave eliminarLlave = null;
                for (Llave l : llaves) {
                    l.pintarLlave(batch);
                    if (l.recogerLlave(jugador)) {
                        eliminarLlave = l;
                    }
                    jugador.recogerLlave(l, batch);
                }
                if (eliminarLlave != null) {
                    laverinto.delLlave(eliminarLlave);
                }
                batch.end();
                oscuridad_s.begin();
                oscuridad.draw(oscuridad_s);
                oscuridad_s.end();
                jugador.pintarAtributos();
                actualizarCamara();
            }
        }
        Win(laverinto);
        if (jugador.vida <= 0 && pantallaFinal(pantallaFinal)) {
            inicio = false;
            menu();
            if (inicio) {
                jugador.vida = 3;
                jugador.llaves = 0;
                transparencia_final = 0;
                llaves.clear();
                llaves = laverinto.llaves();
            }
        }

    }

    private boolean pantallaInicio() {
        SpriteBatch pantallasInicio_Fin = new SpriteBatch();
        this.menusprite.setSize((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        logo_sprite = new Sprite(logo);
        this.logo_sprite.setSize(0.5859375F * Gdx.graphics.getWidth(), 0.3645833333333333F * Gdx.graphics.getHeight());
        this.logo_sprite.translate((float) Gdx.graphics.getWidth() / 2.0F - this.logo_sprite.getWidth() / 2.0F, Gdx.graphics.getHeight() - logo_sprite.getHeight() - logo_sprite.getHeight() / 10);
        this.boton1sprite = new Sprite(this.botonInicio);
        this.boton1sprite.setSize(0.3125F * Gdx.graphics.getWidth(), 0.2083333333333333F * Gdx.graphics.getHeight());
        this.boton1sprite.translate((float) Gdx.graphics.getWidth() / 2.0F - this.boton1sprite.getWidth() / 2.0F, Gdx.graphics.getHeight() / 2.0F - this.boton1sprite.getHeight());
        this.boton2sprite = new Sprite(this.botonFin);
        this.boton2sprite.setSize(0.3125F * Gdx.graphics.getWidth(), 0.2083333333333333F * Gdx.graphics.getHeight());
        this.boton2sprite.translate((float) Gdx.graphics.getWidth() / 2.0F - this.boton2sprite.getWidth() / 2.0F, Gdx.graphics.getHeight() / 2.0F - this.boton2sprite.getHeight() - boton1sprite.getHeight() - boton1sprite.getHeight() / 4);
        boolean respuesta = false;
        pantallasInicio_Fin.begin();
        this.transparencia += 0.01F;
        if (this.transparencia > 1.0F) {
            this.transparencia = 1.0F;
            respuesta = true;
        }
        this.menusprite.setAlpha(this.transparencia);
        this.logo_sprite.setAlpha(this.transparencia);
        this.boton1sprite.setAlpha(this.transparencia);
        this.boton2sprite.setAlpha(this.transparencia);
        this.menusprite.draw(pantallasInicio_Fin);
        this.logo_sprite.draw(pantallasInicio_Fin);
        this.boton1sprite.draw(pantallasInicio_Fin);
        this.boton2sprite.draw(pantallasInicio_Fin);
        pantallasInicio_Fin.end();
        pantallasInicio_Fin.dispose();
        return respuesta;
    }

    private boolean pantallaFinal(Texture textura_Final) {
        SpriteBatch pantallasInicio_Fin = new SpriteBatch();
        Texture pantallaNegra = new Texture("imagenes/pantalla negra.jpg");
        menusprite = new Sprite(pantallaNegra);
        menusprite.setSize((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        pantallasInicio_Fin.begin();
        menusprite.draw(pantallasInicio_Fin);
        pantallasInicio_Fin.end();
        boolean respuesta = false;
        endsprite = new Sprite(textura_Final);
        endsprite.setSize((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        Texture boton_inicio_2 = new Texture("imagenes/boton_inicio_2.png");
        Texture boton_salir_2 = new Texture("imagenes/boton_salir_2.png");
        this.boton1sprite = new Sprite(boton_inicio_2);
        this.boton1sprite.setSize(0.3125F * Gdx.graphics.getWidth(), 0.2083333333333333F * Gdx.graphics.getHeight());
        this.boton1sprite.translate((float) Gdx.graphics.getWidth() / 2.0F - this.boton1sprite.getWidth() / 2.0F, Gdx.graphics.getHeight() / 2.0F - this.boton1sprite.getHeight());
        this.boton2sprite = new Sprite(boton_salir_2);
        this.boton2sprite.setSize(0.3125F * Gdx.graphics.getWidth(), 0.2083333333333333F * Gdx.graphics.getHeight());
        this.boton2sprite.translate((float) Gdx.graphics.getWidth() / 2.0F - this.boton2sprite.getWidth() / 2.0F, Gdx.graphics.getHeight() / 2.0F - this.boton2sprite.getHeight() - boton1sprite.getHeight() - boton1sprite.getHeight() / 4);
        transparencia_final += 0.01F;
        if (transparencia_final > 1.0F) {
            transparencia_final = 1.0F;
            respuesta = true;
        }
        boton1sprite.setAlpha(transparencia_final);
        boton2sprite.setAlpha(transparencia_final);
        endsprite.setAlpha(transparencia_final);
        pantallasInicio_Fin.begin();
        endsprite.draw(pantallasInicio_Fin);
        this.boton1sprite.draw(pantallasInicio_Fin);
        this.boton2sprite.draw(pantallasInicio_Fin);
        pantallasInicio_Fin.end();
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

    private void Win(Laverinto l) {
        if (jugador.llaves == 4) {
            salir = true;
            if (jugador.rect.overlaps(l.getRectsalida())) {
                //Texture texture = new Texture();
                inicio=false;
                muevete = true;
                pantallaFinal(pantallaFinal);
                menu();
                if (inicio) {
                    jugador.vida = 3;
                    jugador.llaves = 0;
                    transparencia_final = 0;
                    jugador.rect.x = (mapaAncho - ANCHO_PERSONAJE) / 2;
                    jugador.rect.y = (mapaAlto - ALTO_PERSONAJE) / 2;
                    enemigos[0] = new Enemigo(0, 0);
                    enemigos[1] = new Enemigo(0, mapaAlto);
                    enemigos[2] = new Enemigo(mapaAncho, 0);
                    enemigos[3] = new Enemigo(mapaAncho, mapaAlto);
                    llaves=laverinto.llaves();
                    muevete=false;
                    salir=false;
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        jugador.dispose();
        laverinto.dispoce();
        pantalladDeInicio.dispose();
        botonInicio.dispose();
        botonFin.dispose();
        jugador.dispose();
        for (Enemigo enemigo : enemigos) {
            enemigo.dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

}

