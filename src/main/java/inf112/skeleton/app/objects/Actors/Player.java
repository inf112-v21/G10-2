package inf112.skeleton.app.objects.Actors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.IActor;

public class Player implements IActor {

    private Vector2 position;
    private Direction lookDirection;

    private final TiledMapTileLayer.Cell playerCell, playerCellDead, playerCellWon;

    public Player(int startRow, int startCol, TextureRegion [][] texture) {
        this.position = new Vector2(startRow, startCol);
        this.playerCell =  new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][0]));
        this.playerCellDead = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][1]));
        this.playerCellWon  = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(texture[0][2]));

    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Direction getLookDirection() {
        return this.lookDirection;
    }

    @Override
    public void setLookDirection(Direction direction) {
        this.lookDirection = direction;
    }

    public void movePlayer(TiledMapTileLayer playerTile, int keycode) {
        System.out.println("keycode" + keycode);
        playerTile.setCell((int) position.x,(int) position.y, new TiledMapTileLayer.Cell());
        switch(keycode) {
            case Input.Keys.W:
                position.y += 1;
                break;
            case Input.Keys.S:
                position.y -= 1;
                break;
            case Input.Keys.A:
                position.x -= 1;
                break;
            case Input.Keys.D:
                position.x += 1;
                break;
        }
    }

    @Override
    public void setPosition(Vector2 position){
        this.position = position;
    }

    public TiledMapTileLayer.Cell getPlayerCell() {
        return playerCell;
    }
    public TiledMapTileLayer.Cell getPlayerCellDead() {
        return playerCellDead;
    }

    public TiledMapTileLayer.Cell getPlayerCellWon() {
        return playerCellWon;
    }


}