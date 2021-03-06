package inf112.skeleton.app.objects.Actors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.SimpleObject;

import static inf112.skeleton.app.enums.Direction.*;
import static inf112.skeleton.app.game.MainGame.gameBoard;
import static inf112.skeleton.app.game.MainGame.robots;

public  class Robot extends SimpleObject implements IActor {

    private Direction lookDirection;
    private ProgramSheet programSheet;
    private final TiledMapTileLayer.Cell playerCellDead;
    private final TiledMapTileLayer.Cell DirectionTextureNORTH, DirectionTextureSOUTH, DirectionTextureEAST, DirectionTextureWEST;
    private String playerName;

    public Robot(Vector2 startpos, TextureRegion[][] texture, String playerName) {
        super(startpos);
        this.playerName = playerName;
        this.lookDirection = Direction.NORTH;
        this.programSheet = new ProgramSheet();
        programSheet.setArchiveMarker(startpos);
        programSheet.setArchiveMarkerDirection(this.lookDirection);

        this.playerCellDead = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][4]));

        this.DirectionTextureNORTH = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][0]));
        this.DirectionTextureSOUTH = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][1]));
        this.DirectionTextureWEST = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][2]));
        this.DirectionTextureEAST = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][3]));
    }

    /**
     * Move robot forward based on input
     * Recursive method to move robot forward
     * Backwards movement is determined by the MovementCard class
     * @param steps : number of steps to be taken
     */
    public void moveRobot(int steps) {
        TiledMapTileLayer playerTile = (TiledMapTileLayer) gameBoard.getMap().getLayers().get("Player");
        if (steps == 0) return;

        Vector2 pos = getPosition();
        playerTile.setCell((int) pos.x, (int) pos.y, new TiledMapTileLayer.Cell()); // Set empty cell where robot once existed

        //Player & Wall Collision
        playerCollisionHandler(this, pos, lookDirection, false);

        moveRobot(steps - 1);
    }

    /**
     * Accounts for player collision and tries to move robot 1 step in pushDirection.
     * Checks that when player collision occurs, there is an empty space the robots can be pushed onto. If not, robots stand still.
     * @param pos : Location of current robot.
     * @param pushDir : Direction robot wants to move.
     * @param collisionOccurred : If robot is moved due to collision.
     */
    protected boolean playerCollisionHandler(Robot currentRobot, Vector2 pos, Direction pushDir, Boolean collisionOccurred) {
        //No wall fills the next position
        if (!gameBoard.canGoToTile(pos,pushDir)) return false;

        Vector2 nextPos = Direction.goDirection(pos, pushDir);

        if (occupied(nextPos)) { //Check for player collision
            for (Robot collidedRobot : robots) { //Find robot we have collided with.
                if (nextPos.equals(collidedRobot.getPosition())) {
                    if (!playerCollisionHandler(collidedRobot, nextPos, pushDir, true)) return false; //Check if collided robot is pushable.
                }
            }
        }
        //Update pos
        currentRobot.setPosition(nextPos);

        if (collisionOccurred) {
            checkPosition(currentRobot); //Check if pushed robot is still alive.
            return true;
        } else {
            return checkPosition(currentRobot);
        }
    }

    /**
     * Checks if position is occupied by an actor.
     * @param pos : Pos to check
     * @return true if position has an actor
     */
    public boolean occupied(Vector2 pos) {
        for (Robot robot : robots) {
            if (robot.getPosition().equals(pos)) return true;
        }
        return false;
    }

    public void robotLoseLife(Robot robot){
        if(robot.getProgramSheet().getLife()>1){
            robot.getProgramSheet().loseLife();
            robot.newPosition(robot);
        } else {
            robot.getProgramSheet().setDead();
            //Remove dead player from map
            TiledMapTileLayer playerTile = (TiledMapTileLayer) gameBoard.getMap().getLayers().get("Player");
            playerTile.setCell((int) robot.getPosition().x, (int) robot.getPosition().y, new TiledMapTileLayer.Cell()); // Set empty cell where robot once existed
        }
    }

    public void newWaitingPosition(Robot robot){
        Vector2 waitingPosition = new Vector2(getPosition().x -100, getPosition().y -100);
        robot.setPosition(waitingPosition);

    }

    public void newPosition(Robot robot){

        robot.setPosition(robot.getProgramSheet().getArchiveMarker());
        robot.setLookDirection(robot.getProgramSheet().getArchiveMarkerDirection());
    }

    /**
     * Check at after each step the robot takes, it is still alive.
     * @param robot : Robot to perform check on.
     * @return True if robot can still move.
     */
    public boolean checkPosition(Robot robot) {
        Vector2 playerPos = robot.getPosition();
        //If player is on Pit or outside map. Set player to dead.
        if (gameBoard.isOnBoard(playerPos) || gameBoard.isPosAPit(playerPos)) {
            newWaitingPosition(robot);
            //robotLoseLife(robot);

            return false;
        }
        return true;
    }

    public void moveRobotWASD(int keycode) {
        Vector2 pos = getPosition();
        TiledMapTileLayer playerTile = (TiledMapTileLayer) gameBoard.getMap().getLayers().get("Player");
        playerTile.setCell((int) pos.x, (int) pos.y, new TiledMapTileLayer.Cell()); // Clear previous robot image

        if (keycode == Input.Keys.W) {
            setLookDirection(NORTH);
            moveRobot(1);
        }
        if (keycode == Input.Keys.A) {
            setLookDirection(WEST);
            moveRobot(1);
        }
        if (keycode == Input.Keys.S) {
            setLookDirection(SOUTH);
            moveRobot(1);
        }
        if (keycode == Input.Keys.D) {
            setLookDirection(EAST);
            moveRobot(1);
        }
        checkPosition(this);
    }


    @Override
    public ProgramSheet getProgramSheet() {
        return this.programSheet;
    }

    public TiledMapTileLayer.Cell getPlayerCell() {
        if (this.lookDirection == Direction.NORTH) {
            return this.DirectionTextureNORTH;
        } else if (this.lookDirection == Direction.SOUTH) {
            return this.DirectionTextureSOUTH;
        } else if (this.lookDirection == Direction.EAST) {
            return this.DirectionTextureEAST;
        } else if (this.lookDirection == Direction.WEST) {
            return this.DirectionTextureWEST;
        }
         throw new IllegalArgumentException("LookDirection of " + this + " robot did not match any of the cardinal directions.");
    }

    public TiledMapTileLayer.Cell getPlayerCellDead() {
        return playerCellDead;
    }

    public TiledMapTileLayer.Cell getPlayerCellWon() {
        return getPlayerCell();
    }

    @Override
    public Direction getLookDirection() {
        return this.lookDirection;
    }

    @Override
    public void setLookDirection(Direction direction) {
        this.lookDirection = direction;
    }

    @Override
    public void rotate(int clockwiseTurns) {
        for (int i = 0; i < clockwiseTurns; i++) {
            setLookDirection(Direction.DirectionClockwise(getLookDirection()));
        }

    }

    public String getRobotName() {return this.playerName;}

    public void clearRobotSprite(int x, int y) {
        TiledMapTileLayer playerTile = (TiledMapTileLayer) gameBoard.getMap().getLayers().get("Player");
        playerTile.setCell( x, y, new TiledMapTileLayer.Cell());
    }
}
