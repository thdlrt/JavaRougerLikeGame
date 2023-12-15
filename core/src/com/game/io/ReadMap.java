package com.game.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReadMap {
    static public List<List<Integer>> readMap(Path path)
    throws IOException {
        List<List<Integer>>res = new ArrayList<>();
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
    static public List<List<Integer>> resumeMap(Path path)
    throws IOException{
        List<List<Integer>> map = new ArrayList<>();
        List<String> allLines = Files.readAllLines(path);
        for (String line : allLines) {
            // 分割每行并转换为整数列表
            List<Integer> row = Arrays.stream(line.split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            map.add(row);
        }
        return map;
    }
}
