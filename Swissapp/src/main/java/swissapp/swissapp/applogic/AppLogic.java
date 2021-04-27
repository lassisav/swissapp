/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swissapp.swissapp.applogic;

import java.io.File;
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
    
    public static String makeStandingsFileFromMatchResults(String fileNameAbsolute, int[] matchResults) throws FileNotFoundException, IOException {
        String[] oldMatchesData = FileHandler.readMatchesFile(fileNameAbsolute);
        System.out.println("oldMatchesData:");
        for (int i = 0; i < oldMatchesData.length; i++) {
            System.out.println(oldMatchesData[i]);
        }
        String[] firstLine = oldMatchesData[0].split(" ");
        String[][] newStandingsData = new String[Integer.parseInt(firstLine[2]) + 1][9];
        for (int i = 0; i < 4; i++) {
            newStandingsData[0][i] = firstLine[i];
        }
        int currentRound = Integer.parseInt(firstLine[4]);
        newStandingsData[0][4] = Integer.toString(currentRound + 1);
        int[][] thisMatchDataByPlayer = new int[Integer.parseInt(firstLine[2]) + 1][3];
        for (int i = 1; i < matchResults.length; i++) {
            String[] singleMatchData = oldMatchesData[i].split(" ");
            int matchID = Integer.parseInt(singleMatchData[0]);
            int whitePlayerID = Integer.parseInt(singleMatchData[1]);
            int blackPlayerID = Integer.parseInt(singleMatchData[2]);
            thisMatchDataByPlayer[whitePlayerID][0] = blackPlayerID;
            thisMatchDataByPlayer[whitePlayerID][1] = 1;
            thisMatchDataByPlayer[whitePlayerID][2] = (matchResults[matchID] - 4) * -1;
            thisMatchDataByPlayer[blackPlayerID][0] = whitePlayerID;
            thisMatchDataByPlayer[blackPlayerID][1] = 2;
            thisMatchDataByPlayer[blackPlayerID][2] = matchResults[matchID];
        }
        String[][] oldStandingsData = new String[Integer.parseInt(firstLine[2]) + 1][9];
        for (int i = 1; i < Integer.parseInt(firstLine[2]) + 1; i++) {
            String[] oldStuff = oldMatchesData[i + matchResults.length - 1].split(" ");
            for (int j = 0; j < oldStuff.length; j++) {
                oldStandingsData[i][j] = oldStuff[j];
            }
        }
        for (int i = 1; i < newStandingsData.length; i++) {
            newStandingsData[i][0] = oldStandingsData[i][0];
            newStandingsData[i][1] = oldStandingsData[i][1];
            newStandingsData[i][2] = oldStandingsData[i][2];
                newStandingsData[i][4] = oldStandingsData[i][4];
                newStandingsData[i][5] = oldStandingsData[i][5];
            if (thisMatchDataByPlayer[i][0] == 0) {
                newStandingsData[i][3] = oldStandingsData[i][3];
                newStandingsData[i][6] = "1";
                newStandingsData[i][7] = oldStandingsData[i][7];
                newStandingsData[i][8] = oldStandingsData[i][8];
            } else {
                int newScore = Integer.parseInt(oldStandingsData[i][3]);
                newScore += thisMatchDataByPlayer[i][2];
                newScore--;
                newStandingsData[i][3] = Integer.toString(newScore);
                newStandingsData[i][6] = oldStandingsData[i][6];
                newStandingsData[i][7] = oldStandingsData[i][7];
                String matchResultString;
                if (oldStandingsData[i][8] == null) {
                    matchResultString = "";
                } else {
                    matchResultString = oldStandingsData[i][8].concat("-");
                }
                matchResultString = matchResultString.concat(Integer.toString(thisMatchDataByPlayer[i][0]));
                int matchResultSwitchValue = thisMatchDataByPlayer[i][2];
                boolean whiteGame = thisMatchDataByPlayer[i][1] == 1;
                if (!whiteGame) {
                    matchResultSwitchValue *= -1;
                }
                String lastAdd = "";
                switch (matchResultSwitchValue) {
                    case -3:
                        lastAdd = "w";
                        break;
                    case -2:
                        lastAdd = "d";
                        break;
                    case -1:
                        lastAdd = "l";
                        break;
                    case 1:
                        lastAdd = "L";
                        break;
                    case 2:
                        lastAdd = "D";
                        break;
                    case 3:
                        lastAdd = "W";
                        break;
                    default:
                        break;
                }
                newStandingsData[i][8] = matchResultString.concat(lastAdd);
            }
        }
        String[] inputArray = new String[Integer.parseInt(firstLine[2]) + 1];
        System.out.println("inputArray:");
        inputArray[0] = newStandingsData[0][0];
        for (int i = 1; i < 5; i++) {
            inputArray[0] = inputArray[0].concat(" ");
            inputArray[0] = inputArray[0].concat(newStandingsData[0][i]);
        }
        System.out.println("inputArray.length: " + inputArray.length);
        System.out.println("0: " + inputArray[0]);
        for (int i = 1; i < inputArray.length; i++) {
            inputArray[i] = newStandingsData[i][0];
            for (int j = 1; j < 9; j++) {
                inputArray[i] = inputArray[i].concat(" " + newStandingsData[i][j]);
            }
            System.out.print(i + ": ");
            System.out.println(inputArray[i]);
        }
        return FileHandler.writeStandingsFile(inputArray);
    }
}
