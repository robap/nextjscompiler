/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rob
 */
public class Concatenator
{
    public void concatenate(ArrayList<JsFile> jsFiles, File outputFile) throws IOException
    {
        if (! outputFile.exists()) {
            outputFile.createNewFile();
        }

        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        
        ArrayList<JsFile> concatenated = new ArrayList<JsFile>();
        for (JsFile jsFile : jsFiles) {
            if (concatenated.contains(jsFile)) {
                continue;
            }
            
            bw.write(jsFile.getContents() + "\n");
            concatenated.add(jsFile);
        }
        
        bw.close();
    }
}
