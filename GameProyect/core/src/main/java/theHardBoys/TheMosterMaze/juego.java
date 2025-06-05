package theHardBoys.TheMosterMaze;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class juego extends Game {
    private SpriteBatch batch;
    private Texture image;
    private Sprite imagenM;
    private TiledMap map;
    private OrthogonalTiledMapRenderer ender;
    private OrthographicCamera camera;
    private Pintor pinsel;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("logo/libgdx.png");
        pinsel = new Pintor(0,0,1,1,image);
        imagenM = new Sprite(image);
        camera = new OrthographicCamera();

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        //batch.draw(imagenM, 140, 210);
        batch.end();
        pinsel.pintarDibujo(batch);
    }
    public void input(){
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            imagenM.translate(imagenM.getX(), imagenM.getY()+delta*2);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            imagenM.translate(imagenM.getX(), imagenM.getY()-delta*2);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            imagenM.translate(imagenM.getX()+delta*2, imagenM.getY());
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            imagenM.translate(imagenM.getX()-delta*2, imagenM.getY());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            pause();
        }
    }
    public void dibujarFondo(){
    }


    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
