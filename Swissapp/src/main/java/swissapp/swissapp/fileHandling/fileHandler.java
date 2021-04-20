/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swissapp.swissapp.fileHandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author lassisav
 */
public class fileHandler {
    static Scanner scan;
    public static int setupDir(String name) throws FileNotFoundException, IOException {
        File temp = new File("");
        String fileName = temp.getAbsolutePath();
        fileName = fileName.concat("/tourneyfiles/" + name);
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            return -1;
        }
        File f = new File(fileName);
        f.mkdir();
        String tourneyListFileName = temp.getAbsolutePath();
        tourneyListFileName = tourneyListFileName.concat("/tourneyfiles/db/idList.txt");
        File tourneyList = new File(tourneyListFileName);
        scan = new Scanner(tourneyList);
        String lastLine = "0 noTourneys";
        while(scan.hasNext()) {
            lastLine = scan.nextLine();
        }
        String[] lastLineParse = lastLine.split(" ");
        int ID = Integer.parseInt(lastLineParse[0]) + 1;
        FileWriter writer = new FileWriter(tourneyList, true);
        writer.write(ID + " " + name + "\n");
        writer.flush();
        writer.close();
        temp.delete();
        return ID;
    }
    public static String[] readStandingsFile(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        scan = new Scanner(f);
        String firstLine = scan.nextLine();
        String[] standings = new String[getPlayerCount(firstLine)+1];
        standings[0] = firstLine;
        int i = 1;
        while(scan.hasNext()) {
            standings[i] = scan.nextLine();
            i++;
        }
        scan.close();
        return standings;
    }
    public static String[] readMatchesFile(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        scan = new Scanner(f);
        String firstLine = scan.nextLine();
        int playerCount = getPlayerCount(firstLine);
        String[] matchesAndStandings = new String[playerCount + playerCount/2 + 1];
        matchesAndStandings[0] = firstLine;
        int i = 1;
        while(scan.hasNext()) {
            matchesAndStandings[i] = scan.nextLine();
            i++;
        }
        scan.close();
        return matchesAndStandings;
    }
    public static String writeStandingsFile(String[] stringArray) throws IOException {
        File temp = new File("");
        String fileName = temp.getAbsolutePath();
        fileName = fileName.concat("/tourneyfiles/");
        String[] firstLine = stringArray[1].split(" ");
        fileName = fileName.concat(firstLine[1] + "/standings" + firstLine[4] + ".txt");
        writeFile(stringArray, fileName);
        temp.delete();
        return fileName;
    }
    public static String writeMatchesFile(String[] stringArray) throws IOException {
        File temp = new File("");
        String fileName = temp.getAbsolutePath();
        fileName = fileName.concat("/tourneyfiles/");
        String[] firstLine = stringArray[0].split(" ");
        fileName = fileName.concat(firstLine[1] + "/matches" + firstLine[4] + ".txt");
        writeFile(stringArray, fileName);
        temp.delete();
        return fileName;
    }
    public static void writeFile(String[] input, String fileName) throws IOException {
        BufferedWriter writer = null;
        File f = new File(fileName);
        writer = new BufferedWriter(new FileWriter(f));
        for (int i = 0; i < input.length; i++) {
            writer.write(input[i]);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
    public static int getPlayerCount(String firstLine){
        String[] arr = firstLine.split(" ");
        return Integer.parseInt(arr[2]);
    }
}
