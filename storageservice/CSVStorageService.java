package storageservice;

import domain.object.Territory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVStorageService {

    public List<String[]> readFile(String path, String delimiter){
        String csvFile = path;
        String line = "";
        String cvsSplitBy = delimiter;
        List<String[]> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                line = line.strip();
                if (line.isEmpty()) { continue; }
                String[] values = line.split(cvsSplitBy);
                lines.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void writeFile(String entry, String filePath){
        if (!entry.equals("")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(entry);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }
        }
    }

    public void resetFile(String filePath){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print("");
            writer.flush();
            System.out.println("File content cleared: " + filePath);
        }
        catch (IOException e) {
            System.out.println("An error occurred while clearing the file content.");
            e.printStackTrace();
        }
    }
}