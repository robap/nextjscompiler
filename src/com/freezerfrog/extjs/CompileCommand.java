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
import java.io.FileReader;
import java.io.FileWriter;
import org.apache.commons.io.IOUtils;

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
        
        Option sourceOpt = new Option("s", true, "source directory or directories");
        sourceOpt.setRequired(true);
        sourceOpt.setLongOpt("src");
        options.addOption(sourceOpt);
        
        Option appOpt = new Option("a", true, "the name of the FILE used to " +
                "start assembling dependencies");
        appOpt.setLongOpt("app");
        appOpt.setRequired(true);
        options.addOption(appOpt);
        
        Option outputOpt = new Option("o", true, "output js file");
        outputOpt.setRequired(true);
        outputOpt.setLongOpt("output");
        options.addOption(outputOpt);
        
        Option prepend = new Option("p", true, "prepend js file(s) before any others");
        prepend.setRequired(false);
        prepend.setLongOpt("prepend");
        options.addOption(prepend);
        
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
                    new JsFile(new File(cmd.getOptionValue('a'))));
            
            System.out.println("concatenating " + deps.size() + " files");
            Concatenator concatenator = new Concatenator();
            File concatFile = File.createTempFile("nextjscompile-", ".txt");
            try (FileWriter tmpFw = new FileWriter(concatFile.getAbsolutePath())) {
                concatenator.concatenate(deps, tmpFw);
            }

            System.out.println("compiling");
            Compiler.setLoggingLevel(Level.SEVERE);
            Compiler compiler = new Compiler();

            CompilerOptions compOpts = new CompilerOptions();
            CompilationLevel.SIMPLE_OPTIMIZATIONS
                    .setOptionsForCompilationLevel(compOpts);

            SourceFile inputJs = SourceFile.fromFile(concatFile);
            SourceFile extern = SourceFile.fromCode("none", "");
            compiler.compile(extern, inputJs, compOpts);
            concatFile.delete();
            
            File outputFile = new File(cmd.getOptionValue('o'));
            if (! outputFile.exists()) {
                outputFile.createNewFile();
            }
            
            try (FileWriter fw = new FileWriter(outputFile.getAbsoluteFile())) {
                if (! outputFile.exists()) {
                    outputFile.createNewFile();
                }
                if (cmd.hasOption('p')) {
                    for (String prependFileName : cmd.getOptionValues('p')) {
                        fw.write(IOUtils.toString(new FileReader(prependFileName)));
                    }
                }
                
                fw.write("\n" + compiler.toSource() + "\n");
                fw.close();
            }
            
            System.out.println("wrote to " + outputFile.getAbsoluteFile());
            return 0;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp(options);
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
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
