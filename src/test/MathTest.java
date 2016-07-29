package test;

/**
 * Created by RaJU on 6/13/2015.
 */
public class MathTest {

    public MathTest(){
        test1();
    }

    private void test1(){

        int[] x = {0,1,1,0}; //texture Coord (unconverted i.e from 1 --> factor
        float X1;
        for (int f = 0; f <= 2; f++) {
            for (float ax : x) {
                X1 = ax * 0.20f + f * 0.20f;
                System.out.println(X1);
            }
            System.out.println();
        }

    }

    public static void main(String[] args){
        new MathTest();
    }
}
