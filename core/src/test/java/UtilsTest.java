import com.game.alogrithm.Move;
import com.game.util.Utils;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void testGetBulletDirection() {
        assertEquals(Move.UPRight, Utils.getBulletDirection(45));
        assertEquals(Move.UP, Utils.getBulletDirection(90));
        assertEquals(Move.UPLeft, Utils.getBulletDirection(135));
    }

    @Test
    public void testGenerateMove() {
        assertEquals(Move.UP, Utils.generateMove(0, 1));
        assertEquals(Move.DOWN, Utils.generateMove(0, -1));
        assertEquals(Move.RIGHT, Utils.generateMove(1, 0));
        assertEquals(Move.LEFT, Utils.generateMove(-1, 0));
    }

}
