/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swissapp.swissapp.applogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import swissapp.swissapp.filehandling.FileHandler;

/**
 *
 * @author lassisav
 */
public class AppLogic {
    public static String makeInitialMatchesFile(int id, String tourneyName, ArrayList<String> players, int rounds) throws IOException {
        String[] playerArray = players.toArray(new String[players.size()]);
        String[] toCarry = new String[1 + playerArray.length + (playerArray.length / 2)];
        //First Line
        toCarry[0] = Integer.toString(id) + " " + tourneyName + " " + Integer.toString(playerArray.length) + " " + rounds + " 1";
        //Match Lines
        for (int i = 0; i < playerArray.length / 2; i++) {
            toCarry[i + 1] = Integer.toString(i + 1) + " " + Integer.toString(i + 1 + (playerArray.length / 2)) + " " + Integer.toString(i + 1);
        }
        //Standings Lines
        int linesPassed = 1 + playerArray.length / 2;
        String whiteBlack = "-1";
        String[] info;
        for (int i = 0; i < playerArray.length; i++) {
            if (i >= playerArray.length / 2) {
                whiteBlack = "1";
            }
            info = playerArray[i].split(" ");
            toCarry[i + linesPassed] = Integer.toString(i + 1) + " " + info[0] + " " + info[1] + " " + 0 + " " + whiteBlack + " " + whiteBlack + " 0 0";
        }
        if (playerArray.length % 2 == 1) {
            int x = playerArray.length - 1;
            info = playerArray[x].split(" ");
            toCarry[toCarry.length - 1] = Integer.toString(x + 1) + " " + info[0] + " " + info[1] + " 1 0 0 1 0 BYE"; 
        }
        return FileHandler.writeMatchesFile(toCarry);
    }

    public static String[][] getMatches(String fileNameAbsolute) throws FileNotFoundException {
        String[] rawData = FileHandler.readMatchesFile(fileNameAbsolute);
        String[] firstLine = rawData[0].split(" ");
        int totalPlayers = Integer.parseInt(firstLine[2]);
        int totalMatches = rawData.length - 1 - totalPlayers;
        String[][] toCarry = new String[totalMatches + 1][5];
        toCarry[0][0] = rawData[1];
        toCarry[0][1] = rawData[4];
        toCarry[0][2] = rawData[3];
        for (int i = 1; i < totalMatches + 1; i++) {
            String[] matchData = rawData[i].split(" ");
            String matchID = matchData[0];
            int whiteRow = Integer.parseInt(matchData[1]) + totalMatches;
            int blackRow = Integer.parseInt(matchData[2]) + totalMatches;
            String[] whiteData = rawData[whiteRow].split(" ");
            String[] blackData = rawData[blackRow].split(" ");
            toCarry[i][0] = matchID;
            toCarry[i][1] = whiteData[1];
            toCarry[i][2] = whiteData[3];
            toCarry[i][3] = blackData[1];
            toCarry[i][4] = blackData[3];
        }
        
        return toCarry;
    }
}
