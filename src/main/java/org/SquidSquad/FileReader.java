package org.SquidSquad;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
    private final String scriptDir;
    public FileReader(){
        scriptDir = "./DYN_Scripts"; // RIP windows users (I hope)
    }
    public FileReader(String startDir){
        this.scriptDir = startDir;
    }
    public String readFile(String name){
        if (name.contains(".dyn")) {
            name = name.replaceAll(".dyn",""); // ensures clean name
        }
        String out = "";
        try (var inputStream = new FileInputStream(scriptDir+"/"+name+".dyn")){
            int i;
            while ((i = inputStream.read()) != -1){
              out += (char) i;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }
    public String[] readLines(String name){
        String fileContents = readFile(name);
        return fileContents.split("\r\n|\n|\r");
    }
}
