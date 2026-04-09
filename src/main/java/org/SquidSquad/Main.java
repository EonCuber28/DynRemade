package org.SquidSquad;

import java.util.Scanner;
import org.SquidSquad.FileReader;
import org.SquidSquad.Tokenizer.Token;
import org.SquidSquad.Tokenizer.Tokenizer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static FileReader fr;
    private static String ScriptContents;
    private static Tokenizer tk;
    public static void main(String[] args) {
        // scanner (just cus)
        Scanner scanner = new Scanner(System.in);
        // we now testing the file reader

        // startup the scanner
        //print("Path to scrips (leave empty for default): ");
        String response = "";//scanner.nextLine();
        if (response != null) {
            if (!response.isEmpty()) {
                fr = new FileReader(response);
            } else {
                fr = new FileReader();
            }
        } else {
            fr = new FileReader();
        }

        // select the file, and start parsing
        //print("script name (leave empty for default): ");
        response = "";//scanner.nextLine();
        if (response != null){
            if (!response.isEmpty()){
                ScriptContents = fr.readFile(response);
            } else {
                ScriptContents = fr.readFile("Test");
            }
        } else {
            ScriptContents = fr.readFile("Test");
        }
        //println("Read Script: ");
        //println(ScriptContents);
        // test tokenizer
        tk = new Tokenizer();
        Token[] tokens = tk.processScript(ScriptContents);
        for(Token tk : tokens) println(tk.toString());
        scanner.close();
    }
    private static void println(Object toBePrinted){
        System.out.println(toBePrinted);
    }
    private static void print(Object toBePrinted){
        System.out.print(toBePrinted);
    }
}