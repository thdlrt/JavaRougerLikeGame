
import com.game.RougerLike;
import com.game.actor.*;
import com.game.map.Map;
import com.game.screen.GameScreen;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTest {
    private Map map;
    private int width = 10;
    private int height = 10;

    @Before
    public void setUp() {
        map = new Map(width, height);
    }

    @Test
    public void testMapInitialization() {
        for (int i = 0; i <map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                assertNotNull("Cell should not be null", map.getCell(i, j));
                assertTrue("Cell should be empty", map.getCell(i, j).isEmpty());
            }
        }
    }
    @Test
    public void testCheckCell() {
        assertTrue("Cell should be empty", map.checkCell(0, 0));
        Being player = new Being(null, 0, 0, 10, new GameScreen("",false,false,new RougerLike()));
        map.setCell(player);
        assertFalse("Cell should not be empty", map.checkCell(0, 0));
    }
    @Test
    public void testSetAndDelCell() {
        GameScreen gameScreen = new GameScreen("",false,false,new RougerLike());
        Being player = new Being(null, 0, 0, 10, gameScreen);
        map.setCell(player);
        assertEquals("Cell should contain the player", player, map.getCell(0, 0).getBeing());

        map.delCell(player);
        assertTrue("Cell should be empty after deletion", map.getCell(0, 0).isEmpty());
        map.simpleCapture();
    }

    // ... 其他测试用例
}
