package com.game.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadMap {
    static public List<List<Integer>> readMap(String map)
    throws IOException {
        map="map/"+map+".txt";
        List<List<Integer>>res = new ArrayList<>();
        Path path = Paths.get(map);
        List<String> lines = Files.readAllLines(path);
        for(int i=0;i<lines.size();i++) {
            String[] line = lines.get(i).split(" ");
            for(int j=0;j<line.length;j++) {
                if(line[j].equals("1")) {
                    res.add(new ArrayList<Integer>());
                    res.get(res.size()-1).add(i);
                    res.get(res.size()-1).add(j);
                }
            }
        }
        return res;
    }
}
