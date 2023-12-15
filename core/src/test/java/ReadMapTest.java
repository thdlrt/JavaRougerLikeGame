import com.game.io.ReadMap;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ReadMapTest {

    private Path testFilePath;

    @Before
    public void setUp() throws IOException {
        testFilePath = Files.createTempFile("testMap", ".txt");
        List<String> lines = Arrays.asList(
                "0 0 1",
                "0 1 0",
                "1 0 0"
        );
        Files.write(testFilePath, lines);
    }

    @Test
    public void testReadMap() throws IOException {
        List<List<Integer>> map = ReadMap.readMap(testFilePath);
        assertEquals(3, map.size());
        assertTrue(map.contains(Arrays.asList(0, 2)));
        assertTrue(map.contains(Arrays.asList(1, 1)));
        assertTrue(map.contains(Arrays.asList(2, 0)));
    }

    @Test
    public void testResumeMap() throws IOException {
        List<List<Integer>> map = ReadMap.resumeMap(testFilePath);
        assertEquals(3, map.size());
        assertArrayEquals(new Integer[]{0, 0, 1}, map.get(0).toArray(new Integer[0]));
        assertArrayEquals(new Integer[]{0, 1, 0}, map.get(1).toArray(new Integer[0]));
        assertArrayEquals(new Integer[]{1, 0, 0}, map.get(2).toArray(new Integer[0]));
    }
    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

}
