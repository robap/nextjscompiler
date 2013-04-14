/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import java.io.FileWriter;

/**
 *
 * @author rob
 */
public class CompileCommand
{
    
    public int run(String[] args)
    {
        Options options = new Options();
        
        options.addOption("h", "help", false, "print help and exit");
        
        Option sourceOpt = new Option("s", true, "source directory");
        sourceOpt.setRequired(true);
        sourceOpt.setLongOpt("src");
        options.addOption(sourceOpt);
        
        Option appOpt = new Option("a", true, "the name of the class used to " +
                "start assembling dependencies");
        appOpt.setLongOpt("app");
        appOpt.setRequired(true);
        options.addOption(appOpt);
        
        Option outputOpt = new Option("o", true, "output js file");
        outputOpt.setRequired(true);
        outputOpt.setLongOpt("output");
        options.addOption(outputOpt);
        
        GnuParser parser = new GnuParser();
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            if (cmd.hasOption('h') || cmd.getOptions().length < 1) {
                printHelp(options);
                return 0;
            }
            
            MapBuilder mapBuilder = new MapBuilder();
            JsFileMap jsFiles = mapBuilder.buildMap(cmd.getOptionValues('s'));
            
            DepsBuilder depsBuilder = new DepsBuilder();
            ArrayList<JsFile> deps = depsBuilder.buildDeps(jsFiles, 
                    cmd.getOptionValue('a'));
            
            System.out.println("concatenating " + deps.size() + " files");
            File outputFile = new File(cmd.getOptionValue('o'));
            Concatenator concatenator = new Concatenator();
            concatenator.concatenate(deps, outputFile);
            
            System.out.println("compiling");
            Compiler.setLoggingLevel(Level.SEVERE);
            Compiler compiler = new Compiler();
            
            CompilerOptions compOpts = new CompilerOptions();
            CompilationLevel.SIMPLE_OPTIMIZATIONS
                    .setOptionsForCompilationLevel(compOpts);
            
            SourceFile inputJs = SourceFile.fromFile(outputFile);
            SourceFile extern = SourceFile.fromCode("none", "");
            compiler.compile(extern, inputJs, compOpts);
            
            if (! outputFile.exists()) {
                outputFile.createNewFile();
            }
            try (FileWriter fw = new FileWriter(outputFile.getAbsoluteFile())) {
                fw.write(compiler.toSource() + "\n");
            }
            System.out.println("wrote to " + outputFile.getAbsoluteFile());
            return 0;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp(options);
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
        
        return 1;
    }
    
    /**
     * @param options 
     */
    private void printHelp(Options options)
    {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("help", options);
    }
}
