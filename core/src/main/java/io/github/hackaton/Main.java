package io.github.hackaton;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.ApplicationListener;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements ApplicationListener {
    private Texture mazeTexture;
    private SpriteBatch batch;
    private Texture imageTexture;
    int playerX = 0, playerY = 0;
    int tileSize = 32;
    int[][] mazeMap = {
        {0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
        {1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0},
        {1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1},
        {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1},
        {0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0},
        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0},
        {1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0},
        {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0},
        {0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1}
    };

    @Override
    public void create() {
        batch = new SpriteBatch();
        mazeTexture = new Texture("imagenes/mapa.png");
        imageTexture = new Texture("imagenes/player.png");
    }

    @Override
    public void render() {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        // Dibujar el laberinto
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                if (mazeMap[y][x] == 1) {
                    // Pared = negra
                    batch.setColor(Color.BLACK);
                } else {
                    // Camino = blanco
                    batch.setColor(Color.WHITE);
                }
                batch.draw(mazeTexture, x * tileSize, (19 - y) * tileSize, tileSize, tileSize);
            }
        }

        // Dibujar jugador (puedes usar otro sprite si prefieres)
        batch.setColor(Color.RED);
        batch.draw(imageTexture, playerX * tileSize, (19 - playerY) * tileSize, tileSize, tileSize);
        batch.setColor(Color.WHITE); // reset color
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (playerX > 0 && mazeMap[playerY][playerX - 1] == 0)
                playerX--;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            if (playerX < 19 && mazeMap[playerY][playerX + 1] == 0)
                playerX++;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (playerY > 0 && mazeMap[playerY - 1][playerX] == 0)
                playerY--;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (playerY < 19 && mazeMap[playerY + 1][playerX] == 0)
                playerY++;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        imageTexture.dispose();
        mazeTexture.dispose();
    }
}
