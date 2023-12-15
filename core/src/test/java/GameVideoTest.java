import com.game.io.GameVideo;
import com.game.map.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameVideoTest {

    private GameVideo gameVideo;
    private Map map;

    @Before
    public void setUp() {
        map = mock(Map.class);
        when(map.simpleCapture()).thenReturn(new ArrayList<>()); // 模拟简单捕获方法的返回值

        gameVideo = new GameVideo(map,true);
    }

    @Test
    public void testStartCapture() {
        gameVideo.startCapture();

    }

    @Test
    public void testStopCaptureAndFileWriting() throws IOException {
        //获取当前路径
        Path path = Paths.get("../assets/video/video.txt");
        gameVideo.stopCapture();
        assertTrue(Files.exists(path));
        List<String> lines = Files.readAllLines(path);
        assertFalse(lines.isEmpty());
    }

}
