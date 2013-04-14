/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

/**
 *
 * @author rob
 */
public class Compiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        CompileCommand cmd = new CompileCommand();
        System.exit(cmd.run(args));
    }
}
