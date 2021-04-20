/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swissapp.swissapp.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import swissapp.swissapp.ui.GraphicUI;

/**
 *
 * @author lassisav
 */
public class Main {
    public static void main(String[] args) throws IOException {
        File temp = new File("");
        String pathName = temp.getAbsolutePath();
        pathName = pathName.concat("/tourneyfiles");
        Path path = Paths.get(pathName);
        if (!Files.exists(path)) {
            File f1 = new File(pathName.concat("/db"));
            f1.mkdirs();
        }
        File f2 = new File(pathName.concat("/db/idList.txt"));
        f2.createNewFile();
        temp.delete();
        GraphicUI.main(args);
    }
}
