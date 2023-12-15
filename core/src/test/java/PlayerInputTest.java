import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.game.actor.Player;
import com.game.alogrithm.Move;
import com.game.alogrithm.PlayerInput;
import com.game.screen.GameScreen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class PlayerInputTest {

    private GameScreen mockGameScreen;
    private Player mockPlayer=null;
    private PlayerInput playerInput;
    private InputEvent mockEvent;

    @Before
    public void setUp() {
        mockGameScreen = mock(GameScreen.class);
        mockEvent = mock(InputEvent.class);
        playerInput = new PlayerInput(mockGameScreen);
        when(mockGameScreen.getPlayer()).thenReturn(mockPlayer);
    }

    @Test
    public void testKeyDown() {
        playerInput.keyDown(mockEvent, Input.Keys.W);
        verify(mockGameScreen).move(mockPlayer, Move.UP);
        playerInput.keyDown(mockEvent, Input.Keys.A);
        verify(mockGameScreen).move(mockPlayer, Move.LEFT);
        playerInput.keyDown(mockEvent, Input.Keys.S);
        verify(mockGameScreen).move(mockPlayer, Move.DOWN);
        playerInput.keyDown(mockEvent, Input.Keys.D);
        verify(mockGameScreen).move(mockPlayer, Move.RIGHT);
    }

}
