import com.badlogic.gdx.graphics.Texture;
import com.game.RougerLike;
import com.game.actor.*;
import com.game.alogrithm.Move;
import com.game.io.NetWork;
import com.game.screen.GameScreen;
import com.game.screen.GuideScreen;
import com.game.screen.MainMenuScreen;
import com.game.screen.VideoSCreen;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BeingTest {

    private Being being;
    private Bullet bullet;
    private Creature creature;
    private Wall wall;
    private Player player;
    private Enemy enemy;
    private Base base;

    private GameScreen gameScreen;
    private GuideScreen guideScreen;
    private MainMenuScreen mainMenuScreen;
    private VideoSCreen videoSCreen;

    @Before
    public void setUp() {
        base = new Base(null, 1, 1, new GameScreen("",false,false,new RougerLike()));
        bullet = new Bullet(null, 1, 1, 1, null, new GameScreen("",false,false,new RougerLike()), null);
        creature = new Creature(null, 1, 1, 100, 0, new GameScreen("",false,false,new RougerLike()));
        wall = new Wall(null, 1, 1, new GameScreen("",false,false,new RougerLike()));
        player = new Player(null, 1, 1, false, new GameScreen("",false,false,new RougerLike()));
        enemy = new Enemy(null, 1, 1,  new GameScreen("",false,false,new RougerLike()));
        gameScreen = new GameScreen("",false,false,new RougerLike());
        guideScreen = new GuideScreen(0,new NetWork(),new RougerLike());
        mainMenuScreen = new MainMenuScreen(new RougerLike());
        videoSCreen = new VideoSCreen(new RougerLike());
        being = new Being(null, 1, 1, 10, gameScreen);
    }

    @Test
    public void testSetPlace() {
        being.setPlace(2, 3);
        assertEquals(2, being.x);
        assertEquals(3, being.y);
        assertNull(gameScreen.getPlayer());
        assertNull(mainMenuScreen.getStage());
        assertNull(mainMenuScreen.getSkin());
        assertNotNull(mainMenuScreen.getGame());
    }
    @Test
    public void testBeingChildreb() {
        bullet.move(Move.UP);
        assertEquals(1, bullet.x);
        assertEquals(2, bullet.y);
        creature.move(Move.DOWN);
        assertEquals(1, creature.x);
        assertEquals(0, creature.y);
        creature.underAttack(10);
        assertFalse(creature.isDead());

    }
}
