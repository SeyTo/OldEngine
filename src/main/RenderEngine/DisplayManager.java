package main.RenderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Created by rj on 5/27/2015.
 */
public class DisplayManager {

    public static final int WIDTH = 800;
    public static final int HEIGHT = WIDTH-150;
    public static final int FPS = 120;
    public static boolean fullScreenMode = true;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay(){
        ContextAttribs contextAttrib = new ContextAttribs(3,2).
                withProfileCompatibility(true).
                withProfileCore(true);
        try {

            Display.create(new PixelFormat(), contextAttrib);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        if (fullScreenMode){
            try {
                Display.setFullscreen(true);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
                GL11.glViewport(0, 0, WIDTH, HEIGHT);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }

        }

        Display.setVSyncEnabled(true);

        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay(){
        Display.sync(FPS);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }

    public static void fullScreenMode(boolean value){
        fullScreenMode = value;
    }

    public static float getFrameTimeSeconds(){
        return delta;
    }

    private static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }

    public static void destroy(){
        Display.destroy();
    }
}
