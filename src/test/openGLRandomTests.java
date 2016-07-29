package test;

import main.RenderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;

import java.awt.*;
import java.awt.Font;

/**
 * Created by RaJU on 6/18/2015.
 */
public class openGLRandomTests {

    public openGLRandomTests(){
        DisplayManager.createDisplay();
        mainLoop();
    }

    public void mainLoop(){

        TrueTypeFont font;
        Font awtFont = new Font("Times New Roman", Font.BOLD, 24); //name, style (PLAIN, BOLD, or ITALIC), size
        font = new TrueTypeFont(awtFont, false); //base Font, anti-aliasing true/false

        while(!Display.isCloseRequested()){
            font.drawString(10,10, "SUCCESS" , Color.white);
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) break;
            DisplayManager.updateDisplay();
        }
    }



    public static void main(String[] args){

        new openGLRandomTests();
        System.exit(1);
    }
}
