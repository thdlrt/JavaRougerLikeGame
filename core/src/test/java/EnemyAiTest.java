import com.game.RougerLike;
import com.game.actor.Being;
import com.game.alogrithm.EnemyAi;
import com.game.map.Cell;
import com.game.map.Map;
import com.game.actor.Player;
import com.game.screen.GameScreen;
import com.game.util.Utils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnemyAiTest {

    private Map map;
    private EnemyAi enemyAi;
    private Player player;
    @Before
    public void setUp() {
        map = mock(Map.class);
        when(map.getWidth()).thenReturn(10);
        when(map.getHeight()).thenReturn(10);

        player = new Player(null, 0, 0, false, new GameScreen("",false,false,new RougerLike()));
        player.x = 5;
        player.y = 5;
        Cell cell = mock(Cell.class);
        when(cell.getBeing()).thenReturn(player);
        Cell cell2 = mock(Cell.class);
        when(cell2.getBeing()).thenReturn(null);
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if(i==5&&j==5)
                    continue;
                when(map.getCell(i,j)).thenReturn(cell2);
            }
        }
        when(map.getCell(5, 5)).thenReturn(cell);
        enemyAi = new EnemyAi(map);
        enemyAi.target=player;
    }

    @Test
    public void testGetNextMove() {
        assertNull(enemyAi.getNextMove(0, 0, 1));
    }

    @Test
    public void testGetAttack() {
        assertNotNull(enemyAi.getAttack(4, 4, 3));
    }

}
