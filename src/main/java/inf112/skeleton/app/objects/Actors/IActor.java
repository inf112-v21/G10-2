package inf112.skeleton.app.objects.Actors;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.IObject;

public interface IActor extends IObject {

    /**
     * @return Directions actor is looking.
     */
    Direction getLookDirection();

    /**
     * Change direction actor is looking.
     * @param direction
     */
    void setLookDirection(Direction direction);

}