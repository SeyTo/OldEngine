package main.Entities;

import org.lwjgl.util.vector.Vector2f;

import java.util.*;

/**
 * Created by RaJU on 6/13/2015.
 */
public class Text {

    private static Map<Character, Vector2f> lettersMap = new HashMap<Character, Vector2f>();
    private List<Vector2f> word = new ArrayList<Vector2f>();
    private Vector2f position;
    private Vector2f scale; //scale change in constructor and TextGUIVertexShader.txt

    public Text(Vector2f position, Vector2f scale){
        this.position = position;
        this.scale = scale;

        setText("aBABAB");
    }

    public void setText(String text){
        clearWord();
        char[] letter = text.toCharArray();
        for (int i=0; i< text.length(); i++){
            if (lettersMap.containsKey(letter[i])){
                word.add(lettersMap.get(letter[i]));
            } else {
                word.add(lettersMap.get(' '));
            }
        }
    }

    /**
     * Initiate the mapping to a static HashMap. Should be done only once.
     */
    public static void initiateMapping(){
        lettersMap.put(' ',new Vector2f(0,2));
        lettersMap.put('!',new Vector2f(1,2));
        lettersMap.put('"',new Vector2f(2,2));
        lettersMap.put('#',new Vector2f(3,2));
        lettersMap.put('$',new Vector2f(4,2));
        lettersMap.put('%',new Vector2f(5,2));
        lettersMap.put('&',new Vector2f(6,2));
        lettersMap.put('\'',new Vector2f(7,2));
        lettersMap.put('(',new Vector2f(8,2));
        lettersMap.put(')',new Vector2f(9,2));
        lettersMap.put('*',new Vector2f(10,2));
        lettersMap.put('+',new Vector2f(11,2));
        lettersMap.put(',',new Vector2f(12,2));
        lettersMap.put('-',new Vector2f(13,2));
        lettersMap.put('.',new Vector2f(14,2));
        lettersMap.put('/',new Vector2f(15,2));

        lettersMap.put('0',new Vector2f(0,3));
        lettersMap.put('1',new Vector2f(1,3));
        lettersMap.put('2',new Vector2f(2,3));
        lettersMap.put('3',new Vector2f(3,3));
        lettersMap.put('3',new Vector2f(4,3));
        lettersMap.put('5',new Vector2f(5,3));
        lettersMap.put('6',new Vector2f(6,3));
        lettersMap.put('7',new Vector2f(7,3));
        lettersMap.put('8',new Vector2f(8,3));
        lettersMap.put('9',new Vector2f(9,3));
        lettersMap.put(':',new Vector2f(10,3));
        lettersMap.put(';',new Vector2f(11,3));
        lettersMap.put('<',new Vector2f(12,3));
        lettersMap.put('=',new Vector2f(13,3));
        lettersMap.put('>',new Vector2f(14,3));
        lettersMap.put('?',new Vector2f(15,3));

        lettersMap.put('@',new Vector2f(0,4));
        lettersMap.put('A',new Vector2f(1,4));
        lettersMap.put('B',new Vector2f(2,4));
        lettersMap.put('C',new Vector2f(3,4));
        lettersMap.put('D',new Vector2f(4,4));
        lettersMap.put('E',new Vector2f(5,4));
        lettersMap.put('F',new Vector2f(6,4));
        lettersMap.put('G',new Vector2f(7,4));
        lettersMap.put('H',new Vector2f(8,4));
        lettersMap.put('I',new Vector2f(9,4));
        lettersMap.put('J',new Vector2f(10,4));
        lettersMap.put('K',new Vector2f(11,4));
        lettersMap.put('L',new Vector2f(12,4));
        lettersMap.put('M',new Vector2f(13,4));
        lettersMap.put('N',new Vector2f(14,4));
        lettersMap.put('O',new Vector2f(15,4));

        lettersMap.put('P',new Vector2f(0,5));
        lettersMap.put('Q',new Vector2f(1,5));
        lettersMap.put('R',new Vector2f(2,5));
        lettersMap.put('S',new Vector2f(3,5));
        lettersMap.put('T',new Vector2f(4,5));
        lettersMap.put('U',new Vector2f(5,5));
        lettersMap.put('V',new Vector2f(6,5));
        lettersMap.put('W',new Vector2f(7,5));
        lettersMap.put('X',new Vector2f(8,5));
        lettersMap.put('Y',new Vector2f(9,5));
        lettersMap.put('Z',new Vector2f(10,5));
        lettersMap.put('[',new Vector2f(11,5));
        lettersMap.put('\\',new Vector2f(12,5));
        lettersMap.put(']',new Vector2f(13,5));
        lettersMap.put('^',new Vector2f(14,5));
        lettersMap.put('_',new Vector2f(15,5));

        lettersMap.put('`',new Vector2f(0,6));
        lettersMap.put('a',new Vector2f(1,6));
        lettersMap.put('b',new Vector2f(2,6));
        lettersMap.put('c',new Vector2f(3,6));
        lettersMap.put('d',new Vector2f(4,6));
        lettersMap.put('e',new Vector2f(5,6));
        lettersMap.put('f',new Vector2f(6,6));
        lettersMap.put('g',new Vector2f(7,6));
        lettersMap.put('h',new Vector2f(8,6));
        lettersMap.put('i',new Vector2f(9,6));
        lettersMap.put('j',new Vector2f(10,6));
        lettersMap.put('k',new Vector2f(11,6));
        lettersMap.put('l',new Vector2f(12,6));
        lettersMap.put('m',new Vector2f(13,6));
        lettersMap.put('n',new Vector2f(14,6));
        lettersMap.put('o',new Vector2f(15,6));

        lettersMap.put('p',new Vector2f(0,7));
        lettersMap.put('q',new Vector2f(1,7));
        lettersMap.put('r',new Vector2f(2,7));
        lettersMap.put('s',new Vector2f(3,7));
        lettersMap.put('t',new Vector2f(4,7));
        lettersMap.put('u',new Vector2f(5,7));
        lettersMap.put('v',new Vector2f(6,7));
        lettersMap.put('w',new Vector2f(7,7));
        lettersMap.put('x',new Vector2f(8,7));
        lettersMap.put('y',new Vector2f(9,7));
        lettersMap.put('z',new Vector2f(10,7));
        lettersMap.put('{',new Vector2f(11,7));
        lettersMap.put('|',new Vector2f(12,7));
        lettersMap.put('}',new Vector2f(13,7));

        lettersMap.put('~',new Vector2f(14,7));
    }

    public void clearWord(){
        word.clear();
    }

    /**
     * Cleans the letter map. Should be done only once.
     */
    public static void cleanLettersMap(){
        lettersMap.clear();
    }

    //BEANS

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public List<Vector2f> getWord(){
        return word;
    }
}
