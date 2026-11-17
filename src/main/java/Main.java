import com.github.curiousoddman.rgxgen.RgxGen;
import com.github.curiousoddman.rgxgen.iterators.StringIterator;
import com.github.curiousoddman.rgxgen.parsing.dflt.RgxGenParseException;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//issues: works well enough ig

public class Main {

    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter options:\n" +
                    "R: regex\n" +
                    "M: modify a file\n" +
                    "Q: quit\n");

            String option = input.next();

            if (option.equalsIgnoreCase("M")) {
                try {
                    modifyTextFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (option.equalsIgnoreCase("R")) {
                generateTextFile();
            } else if (option.equalsIgnoreCase("Q")) {
                break;
            } else {
                System.out.println("Invalid option");
            }
        }
    }

    static void modifyTextFile() throws IOException {
        System.out.println("Enter a filename to modify: ");
        String filename = input.next();

        System.out.println("Enter options:\n" +
                "R: reverse file\n" +
                "C: cut file\n");

        String option = input.next();

        List<String> list = new ArrayList<>();
        FileWriter fw = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename + ".txt"));


            String in;
            while ((in = br.readLine()) != null) {
                list.add(in);
            }
            fw = new FileWriter(filename + ".txt");

        } catch (IOException e) {
            e.printStackTrace();
        }


        if (option.equalsIgnoreCase("R")) {
            Collections.reverse(list);

            for (int i = 0; i < list.size(); i++) {
                fw.write(list.get(i) + "\n");
            }
        } else if (option.equalsIgnoreCase("C")) {
            System.out.println("Enter a fraction of the total line count from the top to keep (1/3): ");
            String fraction = input.next();

            String[] parts = fraction.split("/");
            Double numerator = Double.parseDouble(parts[0]);
            Double denominator = Double.parseDouble(parts[1]);
            if (numerator >= denominator ||  denominator <= 0) {
                System.out.println("Fraction must be between 0 and 1, not inclusive.");
                return;
            }

            int topLines = (int) Math.ceil(list.size() * numerator / denominator);
            for (int i = 0; i < topLines; i++) {
                fw.write(list.get(i) + "\n");
            }

        }  else {
            System.out.println("Invalid option");
        }

        fw.close();
        br.close();
    }

    static void generateTextFile() {
        try {
            System.out.print("Enter a regex string: ");
            RgxGen rgxGen = RgxGen.parse(input.next());
            StringIterator uniqueStrings = rgxGen.iterateUnique();

            System.out.println("Enter a filename: ");
            String filename = input.next();

            FileWriter fw = new FileWriter(filename + ".txt");

            while(uniqueStrings.hasNext()) {
                fw.write(uniqueStrings.next());
                fw.write("\n");
            }
            fw.close();
            System.out.println("File written\n");

        } catch (RgxGenParseException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}