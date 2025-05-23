package game;

import java.io.*;

public class Room {
    private int rows;
    private int cols;
    private String[][] grid;
    private String fileName;

    public Room(String fileName) {
        this.fileName = fileName;
        loadRoom(fileName);
    }

    private void loadRoom(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String[] size = reader.readLine().split(",");
            rows = Integer.parseInt(size[0].trim());
            cols = Integer.parseInt(size[1].trim());

            grid = new String[rows][cols];

            for (int i = 0; i < rows; i++) {
                String[] line = reader.readLine().split(",");
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = line[j].trim().isEmpty() ? " " : line[j].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load room: " + e.getMessage());
        }
    }

    public void printRoom(int heroRow, int heroCol) {
        System.out.println("+---".repeat(cols) + "+");

        for (int i = 0; i < rows; i++) {
            System.out.print("|");
            for (int j = 0; j < cols; j++) {
                if (i == heroRow && j == heroCol) {
                    System.out.print(" @ |");
                } else {
                    String cell = grid[i][j];
                    if (cell.length() == 1) {
                        System.out.print(" " + cell + " |");
                    } else {
                        System.out.print(cell.charAt(0) + " |"); // G:3 → G만 보이게
                    }
                }
            }
            System.out.println("\n+---".repeat(cols) + "+");
        }
    }

    public String getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, String value) {
        grid[row][col] = value;
    }

    public int getRowCount() {
        return rows;
    }

    public int getColCount() {
        return cols;
    }

    public void saveRoom(String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(rows + "," + cols + "\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(grid[i][j]);
                    if (j < cols - 1) writer.write(",");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save room: " + e.getMessage());
        }
    }
}
