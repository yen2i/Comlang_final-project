package game;

import java.io.*;

/*
 * Utility class for reading and writing room data in CSV format.
 */
public class CSVUtil {
    public static char[][] readRoomGrid(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String[] size = br.readLine().split(",");
        int rows = Integer.parseInt(size[0]);
        int cols = Integer.parseInt(size[1]);

        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String[] line = br.readLine().split(",");
            for (int j = 0; j < cols; j++) {
                grid[i][j] = line[j].charAt(0);
            }
        }

        return grid;
    }

    public static void writeRoomGrid(char[][] grid, String path) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        // Write grid size to the first line
        bw.write(grid.length + "," + grid[0].length + "\n");

        // Write each row of the grid
        for (char[] row : grid) {
            for (int j = 0; j < row.length; j++) {
                bw.write(row[j]);
                if (j < row.length - 1) bw.write(",");
            }
            bw.write("\n");
        }

        bw.close();
    }
}
