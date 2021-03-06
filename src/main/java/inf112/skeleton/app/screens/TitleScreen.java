package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import inf112.skeleton.app.RoboRally;
import inf112.skeleton.app.game.MainGame;

import static com.badlogic.gdx.Gdx.gl;

/**
 * Creates a title screen to act as a menu.
 * * The display the player encounters
 */
public class TitleScreen implements Screen {

    final RoboRally switcher;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private final Texture title = new Texture("Images/title.png");

    FitViewport viewPort;

    float width;
    float height;

    MainGame mainGame;

    int alignToAxisX = Gdx.graphics.getWidth()/2;
    /**
     * Constructor method

     */
    public TitleScreen(final RoboRally switcher, Stage stage, FitViewport viewPort) {
        this.switcher = switcher;
        this.stage = stage;
        this.viewPort = viewPort;
    }

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        batch = new SpriteBatch();
        stage = new Stage(new StretchViewport(width, height));
        skin = new Skin(Gdx.files.internal("uiskin.json"));


        final Button singleplayer = new TextButton("Offline", skin);
        singleplayer.setWidth(width*0.40f);
        singleplayer.setHeight(height*0.10f);
        singleplayer.setX(alignToAxisX - singleplayer.getWidth()/2);
        singleplayer.setY(height - singleplayer.getHeight()-height*0.45f);


        final Button multiplayer = new TextButton("Online", skin);
        multiplayer.setWidth(width*0.40f);
        multiplayer.setHeight(height*0.10f);
        multiplayer.setX(alignToAxisX - multiplayer.getWidth()/2);
        multiplayer.setY(height - multiplayer.getHeight()-height*0.55f);


        final Button exitGame = new TextButton("Exit", skin);
        exitGame.setWidth(width*0.40f);
        exitGame.setHeight(height*0.10f);
        exitGame.setX(alignToAxisX - exitGame.getWidth()/2);
        exitGame.setY(height - exitGame.getHeight()-height*0.70f);
        Gdx.input.setInputProcessor(stage);

        singleplayer.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y){
                switcher.setMapScreen(mainGame);
            }
        });

        multiplayer.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                switcher.setScreen(new MultiplayerScreen(switcher));
            }
        });

        exitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(singleplayer);
        stage.addActor(multiplayer);
        stage.addActor(exitGame);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void render(float delta) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        switcher.batch.begin();
        switcher.batch.draw(title, alignToAxisX - title.getWidth()/3, height*0.7f,width*0.7f, height*0.3f);
        switcher.batch.end();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
        stage.getCamera().viewportHeight = height;
        stage.getCamera().viewportWidth = width;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }



    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        title.dispose();
    }

}
