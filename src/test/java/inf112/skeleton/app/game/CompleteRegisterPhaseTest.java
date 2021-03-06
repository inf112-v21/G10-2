package inf112.skeleton.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.GdxTestRunner;
import inf112.skeleton.app.assetManager.Assets;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Actors.Robot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CompleteRegisterPhaseTest {
    //Get map
    private TiledMap map = new TmxMapLoader().load("Maps/MapForJunitTests.tmx");
    //Splits player texture into the 3 parts. Live/Dead/Win
    private TextureRegion[][] textures = new TextureRegion(new Texture("Images/Robot/robot.png")).split(300, 300);
    private MainGame game;

    @Before
    public void initialise() {
        Assets.load();
        Assets.manager.finishLoading();

        game = new MainGame();
        game.setup(map); //Make game board

    }

    ////////////////////////////////////////
    /// Test Conveyors without collision ///
    ////////////////////////////////////////

    @Test
    public void normalConveyorShouldPushRobotOneTileEast() {
        Robot robot = new Robot(new Vector2(2,1), textures, "1");
        game.addPlayer(robot);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.moveConveyor(false);

        Assert.assertEquals(new Vector2(3,1), robot.getPosition());
    }


    @Test
    //Begin at (3,1) --> (3,0)
    public void expressConveyorShouldPushRobotOneTileSouth() {
        Robot robot = new Robot(new Vector2(3,1), textures, "1");
        game.addPlayer(robot);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.moveConveyor(true);

        Assert.assertEquals(new Vector2(3,0), robot.getPosition());
    }



    /**
     *  Robot is placed on a conveyor that goes in a circle.
     *  * -> *
     *  |     \
     *  * <- *
     */
    public void conveyorsWorkInCircle() {
        Robot robot = new Robot(new Vector2(3,1), textures, "1");
        game.addPlayer(robot);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.moveConveyor(true);
        phase.moveConveyor(false);
        phase.moveConveyor(true);
        phase.moveConveyor(false);

        Assert.assertEquals(new Vector2(3,1), robot.getPosition()); //Position should not be different.
    }

    ////////////////////////////////////////
    ///   Test Conveyors with collision  ///
    ////////////////////////////////////////

    @Test
    public void playerCollisionConveyorShouldNotPlayers() {
        Robot robot = new Robot(new Vector2(4,1), textures, "1");
        Robot robot2 = new Robot(new Vector2(4,0), textures, "1");
        game.addPlayer(robot);
        game.addPlayer(robot2);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.moveConveyor(false); //Only move normal conveyors. Not express

        //Robots should not move. Robot2 is facing a wall. robot is on a conveyor, heading towards robot2, due to the wall blocking robot2, they should stand still.
        Assert.assertEquals(new Vector2(4,1), robot.getPosition()); //Position should not be different.
        Assert.assertEquals(new Vector2(4,0), robot2.getPosition()); //Position should not be different.
    }

    @Test
    public void conveyorCannotPushPlayersThroughWalls() {
        Robot robot = new Robot(new Vector2(2,3), textures, "1");
        game.addPlayer(robot);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.moveConveyor(false); //Only move normal conveyors. Not express

        //Robots should not move
        Assert.assertEquals(new Vector2(2,3), robot.getPosition()); //Position should not be different.
    }


    //////////////////////////////////////
    /// Test Pushers without collision ///
    //////////////////////////////////////

    @Test
    //Begin at (0,4) --> (0,3)
    public void pusherShouldPushInPushDirection() {
        Robot robot = new Robot(new Vector2(1,4), textures, "1");
        game.addPlayer(robot);
        Direction lookDirection = robot.getLookDirection();

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.movePusher();

        Assert.assertEquals(lookDirection, robot.getLookDirection());     //Look direction should not change.
        Assert.assertEquals(new Vector2(1,3), robot.getPosition()); //Position should not be different.
    }

    @Test
    //Begin at (1,4) --> (1,3)
    public void pusherShouldPushInPushDirection2() {
        Robot robot = new Robot(new Vector2(1,4), textures, "1");
        game.addPlayer(robot);
        Direction lookDirection = robot.getLookDirection();

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.movePusher();

        Assert.assertEquals(lookDirection, robot.getLookDirection());     //Look direction should not change.
        Assert.assertEquals(new Vector2(1,3), robot.getPosition()); //Position should not be different.
    }


    //////////////////////////////////////
    ///   Test Pushers with collision  ///
    //////////////////////////////////////

    @Test
    public void pushersCannotPushPlayersThroughWalls() {
        Robot robot = new Robot(new Vector2(0,4), textures, "1");
        game.addPlayer(robot);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.movePusher(); //Only move normal conveyors. Not express

        //Robots should not move
        Assert.assertEquals(new Vector2(0,4), robot.getPosition()); //Position should not be different.
    }


    /////////////////////////////////////////
    /// Test Robot move without collision ///
    /////////////////////////////////////////

    @Test
    public void playerMovedToCorrectPosition() {
        Robot robot = new Robot(new Vector2(1,1), textures, "1");
        robot.setLookDirection(Direction.SOUTH);

        robot.setPosition(Direction.goDirection(robot.getPosition(), robot.getLookDirection()));

        Assert.assertEquals(new Vector2(1,0), robot.getPosition());
    }


    /////////////////////////////////////////
    ///             Test Lasers           ///
    /////////////////////////////////////////
    @Test
    public void singleLaserShouldDamagePlayer() {
        Robot robot = new Robot(new Vector2(3,3), textures, "1");
        game.addPlayer(robot);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.lasersFire();

        int damage = robot.getProgramSheet().getDamage();
        Assert.assertEquals(1, damage); //Takes 1 damage
    }

    @Test
    public void doubleLaserShouldDamagePlayer() {
        Robot robot = new Robot(new Vector2(4,4), textures, "1");
        game.addPlayer(robot);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.lasersFire();

        int damage = robot.getProgramSheet().getDamage();
        Assert.assertEquals(2, damage); //Takes 1 damage
    }

    @Test
    public void laserShouldOnlyDamageFirstPlayerInALine() {
        Robot robot1 = new Robot(new Vector2(3,3), textures, "1");
        Robot robot2 = new Robot(new Vector2(3,2), textures, "1");
        robot2.setLookDirection(Direction.EAST); //Prevents robot2 shooting robot1 in the back
        game.addPlayer(robot1);
        game.addPlayer(robot2);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.lasersFire();

        Assert.assertEquals(1, robot1.getProgramSheet().getDamage()); //Takes 1 damage
        Assert.assertEquals(0, robot2.getProgramSheet().getDamage()); //Takes 0 damage
    }

    @Test
    public void robotShouldDamageOtherRobotWhenShooting() {
        Robot robot1 = new Robot(new Vector2(0,0), textures, "1");
        Robot robot2 = new Robot(new Vector2(0,1), textures, "1");
        game.addPlayer(robot1);
        game.addPlayer(robot2);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.lasersFire();

        Assert.assertEquals(0, robot1.getProgramSheet().getDamage());
        Assert.assertEquals(1, robot2.getProgramSheet().getDamage());
    }



    /////////////////////////////////////////
    ///    Test Gear Rotating players     ///
    /////////////////////////////////////////

    @Test
    public void robotShouldBeRotatedWithClock() {
        Robot robot1 = new Robot(new Vector2(4,2), textures, "1");
        game.addPlayer(robot1);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.rotatePlayer();

        Assert.assertEquals(Direction.EAST, robot1.getLookDirection());
    }

    @Test
    public void robotShouldBeRotatedAgainstClock() {
        Robot robot1 = new Robot(new Vector2(3,2), textures, "1");
        game.addPlayer(robot1);

        CompleteRegisterPhase phase = new CompleteRegisterPhase();
        phase.rotatePlayer();

        Assert.assertEquals(Direction.WEST, robot1.getLookDirection());
    }
}
