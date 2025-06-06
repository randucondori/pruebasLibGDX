package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Laverinto {
    private ArrayList<Muro> muros = new ArrayList<>();
    private Texture texture = new Texture("imagenes/mapa.png");
    private Texture salida=new Texture(Gdx.files.internal("imagenes/puerta.png"));
    private Rectangle rectsalida= new Rectangle();

    private Muro muro;
    private Muro muro1;
    private Muro muro2;
    private Muro muro3;
    private Muro muro4;
    private Muro muro5;
    private Muro muro6;
    private Muro muro7;
    private Muro muro8;
    private Muro muro9;
    private Muro muro10;
    private Muro muro11;
    private Muro muro12;
    private Muro muro13;
    private Muro muro14;
    private Muro muro15;
    private Muro muro16;
    private Muro muro17;
    private Texture muroTextura;

    public ArrayList<Muro> Muros(){
        //muros
        muroTextura = new Texture("mapa/maps.png");
        //vertical
        muro2 = new Muro(110, 115, 5, 1750);
        muro3 = new Muro(1875, 115, 5, 1750);
        muro6 = new Muro(170, 170, 5, 1650);
        muro9 = new Muro(1825, 170, 5, 1650);
        muro11 = new Muro(1120, 175, 5, 60);
        muro12 = new Muro(1000, 120, 5, 60);

        //Horizontal
        muro = new Muro(1080, 115, 800, 5);
        muro1 = new Muro(110, 115, 880, 5);
        muro4 = new Muro(110, 1880, 1790, 5);
        muro5 = new Muro(240, 170, 800, 5);
        muro7 = new Muro(170, 1825, 300, 5);
        muro8 = new Muro(540, 1825, 1275, 5);
        muro10 = new Muro(1120, 175, 700, 5);





        muro13 = new Muro(170, 120, 900, 10);
        muro14 = new Muro(240, 120, 900, 10);
        muro15 = new Muro(170, 120, 900, 10);
        muro16 = new Muro(240, 120, 900, 10);
        muros.add(muro);
        muros.add(muro1);
        muros.add(muro2);
        muros.add(muro3);
        muros.add(muro4);
        muros.add(muro5);
        muros.add(muro6);
        muros.add(muro7);
        muros.add(muro8);
        muros.add(muro9);
        muros.add(muro10);
        muros.add(muro11);
        muros.add(muro12);

        return muros;
    }

    public Laverinto() {
    }
    public void pintarFondo(SpriteBatch batch, float alto, float ancho) {
        batch.begin();
        batch.draw(texture, 0,0,ancho,alto);
        batch.draw(salida, 970, 100, 100, 40);
        rectsalida.set(970, 100, 80, 20);
        batch.end();
    }

    public Rectangle getRectsalida() {
        return rectsalida;
    }

    public Texture getSalida() {
        return salida;
    }

    public void dispoce(){
        texture.dispose();
        salida.dispose();
    }
}
