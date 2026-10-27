package org.example.demo;

import org.example.demo.classes.Alignment;
import org.example.demo.classes.MagicalCreature;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils {

    public static List<MagicalCreature> readFromFile(String filename){
        List<MagicalCreature> magicalCreatures = new ArrayList<>();
        try {
            Scanner input = new Scanner(new File(filename));
            while (input.hasNextLine()) {
                String[] line = input.nextLine().split(":");
                String name = line[0];
                String[] finalLine = line[1].split(";");
                Alignment alignment = Alignment.valueOf(finalLine[0].trim());
                LocalDate dateFound = LocalDate.parse(finalLine[2].trim());
                boolean isEndangered = finalLine[5].equals("taip");
                MagicalCreature magicalCreature = new MagicalCreature(name, alignment, finalLine[1], dateFound, finalLine[3], finalLine[4], isEndangered);
                magicalCreatures.add(magicalCreature);
            }
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return magicalCreatures;
    }

    public static void writeToBinaryFile(List<MagicalCreature> creatures, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(creatures);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<MagicalCreature> readFromBinaryFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object data = in.readObject();
            if (!(data instanceof List<?> storedCreatures)) {
                throw new IOException("File does not contain a list of magical creatures.");
            }

            List<MagicalCreature> creatures = new ArrayList<>();
            for (Object creature : storedCreatures) {
                if (!(creature instanceof MagicalCreature magicalCreature)) {
                    throw new IOException("File contains an invalid magical creature entry.");
                }
                creatures.add(magicalCreature);
            }
            return creatures;
        }
    }
}
