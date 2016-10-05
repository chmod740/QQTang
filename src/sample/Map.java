package sample;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

/**
 * 地图类
 */
public class Map {
    private int data[][] = new int[16][16];
    private void readMap(int n){
        FileInputStream fileInputStream;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(n+".txt"));
            for (int i = 0 ; i < 13 ; i ++){
                String s = bufferedReader.readLine();
                int length = s.split(" ").length;
                for (int j = 0 ; j < length ; j ++){
                    data[i][j] = Integer.parseInt(s.split(" ")[j]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.print(1);
    }

    public Map(int n){
        readMap(n);
    }
}
