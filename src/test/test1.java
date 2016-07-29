package test;

import org.lwjgl.Sys;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;

/**
 * Created by RaJU on 6/2/2015.
 */
public class test1 {

    public static void main(String[] args){
        try {
            String line = null;
            FileReader fr = new FileReader("res/raw/Tree.obj");
            FileWriter fw = new FileWriter("res/raw/Tree2.obj");
            BufferedReader reader = new BufferedReader(fr);
            BufferedWriter writer = new BufferedWriter(fw);
            line = reader.readLine();
            while(line != null){

                if (line.startsWith("v  ")) {
                    String replacement = line.replace("v  ", "v ");
                    writer.append(replacement + "\n");
                } else if (line.startsWith("vt  ")){
                    String replacement = line.replace("vt  ", "vt ");
                    writer.append(replacement + "\n");
                } else if (line.startsWith("vn  ")){
                    String replacement = line.replace("vn  ", "vn ");
                    writer.append(replacement + "\n");
                } else {
                    writer.append(line + "\n");
                }
                writer.flush();
                line = reader.readLine();
            }

            writer.close();
            reader.close();
            fr.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
