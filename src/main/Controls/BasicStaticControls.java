package main.Controls;

import main.Entities.Camera;
import main.Entities.Helpers.InvisiblePlayerFollower;
import main.Game;
import org.lwjgl.input.Keyboard;

/**
 * Created by RaJU on 6/14/2015.
 */
public class BasicStaticControls {

    //This method need to moved to another class.
    /**
     * Contains the primary controls such as the Game Exit, Free Cam, Player Cam.
     *  Escape      - exits game
     *  P           - Toggle player cam or Free cam
     *  LCtrl + =   - Console prints the current cam location
     */
    public static void controls(){
        //exit the game
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) Game.exit = true;
        //toggle between Player cam and Free cam
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) InvisiblePlayerFollower.playerCam = (InvisiblePlayerFollower.playerCam? false : true);
        //prints the location of the camera
        if (Keyboard.isKeyDown(Keyboard.KEY_EQUALS) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            System.out.println("Camera\nX: " + Camera.getPosition().getX() + "\nY: " + Camera.getPosition().getY() + "\nZ: " + Camera.getPosition().getZ() +
                            "\nYaw: " + Camera.getYaw() + " Pitch: " + Camera.getPitch());
        }

    }
}
