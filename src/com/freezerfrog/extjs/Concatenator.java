/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rob
 */
public class Concatenator
{
    public void concatenate(ArrayList<JsFile> jsFiles, FileWriter fw) throws IOException
    {
        ArrayList<JsFile> concatenated = new ArrayList<JsFile>();
        for (JsFile jsFile : jsFiles) {
            if (concatenated.contains(jsFile)) {
                continue;
            }
            
            fw.write(jsFile.getContents() + "\n");
            concatenated.add(jsFile);
        }
    }
}
