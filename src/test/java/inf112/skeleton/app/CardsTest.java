package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.assetManager.Assets;
import inf112.skeleton.app.cards.CardType;
import inf112.skeleton.app.cards.MovementCard;
import inf112.skeleton.app.cards.RotationCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.map.Board;
import inf112.skeleton.app.objects.Actors.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains mostly the same tests as robot, but instead calls directly on the cards action() method.
 */

@RunWith(GdxTestRunner.class)
public class CardsTest {
    
    // Make maps
    private TiledMap map;

    private TextureRegion[][] textures;
    private TiledMapTileLayer tilePlayer;
    private Board board;

    @Before
    public void initalise() {
        textures = new TextureRegion(new Texture("Images/player.png")).split(300, 300);  // Splits player texture into the 3 parts. Live/Dead/Win
        map = new TmxMapLoader().load("Maps/Chess.tmx");       // Get map file
        tilePlayer = (TiledMapTileLayer) map.getLayers().get("Player");
        Assets.load();
        Assets.manager.finishLoading();

        this.board = new Board(map);
    }

    @Test
    public void moveOneForward() {
        Player player = new Player(new Vector2(2,2), textures, board);
        MovementCard move1 = new MovementCard("Move1",1, Assets.manager.get(Assets.Move1Card), 1 );
        move1.action(tilePlayer, player);
        assertEquals(new Vector2(2,3), player.getPosition());
    }

    @Test
    public void moveOneBack() {
        Player player = new Player(new Vector2(2,2), textures, board);
        MovementCard move1 = new MovementCard("Moveback1",1, Assets.manager.get(Assets.Move1Card), -1 );
        move1.action(tilePlayer, player);
        assertEquals(new Vector2(2,1), player.getPosition());
    }

    @Test
    public void rotateWithClock() {
        Player player = new Player(new Vector2(2,2), textures, board);
        RotationCard rotate1 = new RotationCard("Rotate with clock 1",1, Assets.manager.get(Assets.RotateRightCard), 1 );
        rotate1.action(tilePlayer, player);
        assertEquals(Direction.EAST, player.getLookDirection());
    }

}

