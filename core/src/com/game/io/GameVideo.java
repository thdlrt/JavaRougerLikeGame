package com.game.io;

import com.badlogic.gdx.utils.Timer;
import com.game.map.Map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class GameVideo {
    Map map;
    List<List<List<Integer>>> video;
    Timer.Task CaptureTask;
    public GameVideo(Map map){
        this.map=map;
        video = new ArrayList<>();
        CaptureTask = new Timer.Task() {
            @Override
            public void run() {
                video.add(map.simpleCapture());
            }
        };
    }
    public void startCapture(){
        Timer.schedule(CaptureTask, 0, 0.2f);
    }
    public void stopCapture(){
        CaptureTask.cancel();
        Path path = Paths.get("video/video.txt");
        try {
            // 确保文件存在，如果不存在则创建
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING).close();
            // 遍历video列表
            for (List<List<Integer>> frame : video) {
                for (List<Integer> row : frame) {
                    String line = row.stream()
                            .map(Object::toString)
                            .reduce((a, b) -> a + " " + b)
                            .orElse("");

                    Files.write(path, (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                }
                Files.write(path, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
