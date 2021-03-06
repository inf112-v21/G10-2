package inf112.skeleton.app.objects.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.GdxTestRunner;
import inf112.skeleton.app.assetManager.Assets;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.game.MainGame;
import inf112.skeleton.app.objects.Actors.Robot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class SimpleRobotTest {

    // Make maps
    private TiledMap map;

    private TextureRegion[][] textures;
    private static MainGame game;


    @Before
    public void initialise() {
        textures = new TextureRegion(new Texture("Images/Robot/robot.png")).split(300, 300);  // Splits player texture into the 3 parts. Live/Dead/Win
        map = new TmxMapLoader().load("Maps/MapForJunitTests.tmx");       // Get map file
        Assets.load();
        Assets.manager.finishLoading();
        game = new MainGame();
        MainGame.setup(map);
    }

    ///////////////////////////////////////////////////
    /// Test Player Collision with multiple players ///
    ///////////////////////////////////////////////////

    @Test
    public void player1ShouldPushPlayer2OneTile() {
        Robot robot1 = new Robot(new Vector2(0,3),textures, "1");
        robot1.setLookDirection(Direction.SOUTH); //Robot1 is facing robot2
        Robot robot2 = new Robot(new Vector2(0,2),textures, "1");

        game.addPlayer(robot1);
        game.addPlayer(robot2);

        robot1.moveRobot(1);

        Assert.assertEquals(new Vector2(0,2), robot1.getPosition());
        Assert.assertEquals(new Vector2(0,1), robot2.getPosition());
    }

    @Test
    public void player1ShouldPushPlayer2TwoTiles() {
        Robot robot1 = new Robot(new Vector2(1,0),textures, "1");
        robot1.setLookDirection(Direction.NORTH); //Robot1 is facing robot2
        Robot robot2 = new Robot(new Vector2(1,1),textures, "1");

        game.addPlayer(robot1);
        game.addPlayer(robot2);

        robot1.moveRobot(2);

        Assert.assertEquals(new Vector2(1,2), robot1.getPosition());
        Assert.assertEquals(new Vector2(1,3), robot2.getPosition());
    }

    @Test
    public void player1ShouldPushPlayer2AndPlayer3OneTile() {
        Robot robot1 = new Robot(new Vector2(1,0),textures, "1");
        robot1.setLookDirection(Direction.NORTH); //Robot1 is facing robot2
        Robot robot2 = new Robot(new Vector2(1,1),textures, "1");
        Robot robot3 = new Robot(new Vector2(1,2),textures, "1");

        game.addPlayer(robot1);
        game.addPlayer(robot2);
        game.addPlayer(robot3);

        robot1.moveRobot(1);

        Assert.assertEquals(new Vector2(1,1), robot1.getPosition());
        Assert.assertEquals(new Vector2(1,2), robot2.getPosition());
        Assert.assertEquals(new Vector2(1,3), robot3.getPosition());
    }

    @Test
    public void player1ShouldPushPlayer2AndPlayer3TwoTiles() {
        Robot robot1 = new Robot(new Vector2(1,0),textures, "1");
        robot1.setLookDirection(Direction.NORTH); //Robot1 is facing robot2
        Robot robot2 = new Robot(new Vector2(1,1),textures, "1");
        Robot robot3 = new Robot(new Vector2(1,2),textures, "1");

        game.addPlayer(robot1);
        game.addPlayer(robot2);
        game.addPlayer(robot3);

        robot1.moveRobot(2);

        Assert.assertEquals(new Vector2(1,2), robot1.getPosition());
        Assert.assertEquals(new Vector2(1,3), robot2.getPosition());
        Assert.assertEquals(new Vector2(1,4), robot3.getPosition());
    }

    /////////////////////////////////////////////
    /// Test Player Collision with wall types ///
    /////////////////////////////////////////////

    @Test
    public void player1ShouldNotPushPlayer2DueToWall() {
        Robot robot1 = new Robot(new Vector2(0,3),textures, "1");
        Robot robot2 = new Robot(new Vector2(0,2),textures, "1");
        robot2.setLookDirection(Direction.NORTH); //Robot2 is facing robot1. North of robot1 there is a wall.

        game.addPlayer(robot1);
        game.addPlayer(robot2);

        robot1.moveRobot(1);

        //Due to wall, robots should not move.
        Assert.assertEquals(new Vector2(0,3), robot1.getPosition());
        Assert.assertEquals(new Vector2(0,2), robot2.getPosition());
    }

    @Test
    public void player1ShouldNotPushPlayer2DueToPusher() {
        Robot robot1 = new Robot(new Vector2(1,3),textures, "1");
        Robot robot2 = new Robot(new Vector2(1,4),textures, "1");
        robot2.setLookDirection(Direction.NORTH); //Robot2 is facing robot1. North of robot1 there is a wall.

        game.addPlayer(robot1);
        game.addPlayer(robot2);

        robot1.moveRobot(1);

        //Due to wall(Pusher), robots should not move.
        Assert.assertEquals(new Vector2(1,3), robot1.getPosition());
        Assert.assertEquals(new Vector2(1,4), robot2.getPosition());
    }

    @Test
    public void player1ShouldPushPlayer2AndPlayer3TwoTilesAndBeStoppedByAWall() {
        Robot robot1 = new Robot(new Vector2(1,0),textures, "1");
        robot1.setLookDirection(Direction.NORTH); //Robot1 is facing robot2
        Robot robot2 = new Robot(new Vector2(1,1),textures, "1");
        Robot robot3 = new Robot(new Vector2(1,2),textures, "1");

        game.addPlayer(robot1);
        game.addPlayer(robot2);
        game.addPlayer(robot3);

        robot1.moveRobot(4);

        Assert.assertEquals(new Vector2(1,2), robot1.getPosition());
        Assert.assertEquals(new Vector2(1,3), robot2.getPosition());
        Assert.assertEquals(new Vector2(1,4), robot3.getPosition());
    }







}
