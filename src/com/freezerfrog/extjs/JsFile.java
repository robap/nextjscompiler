/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.IOUtils;
import sun.org.mozilla.javascript.Parser;
import sun.org.mozilla.javascript.ast.AstNode;
import sun.org.mozilla.javascript.ast.FunctionCall;
import sun.org.mozilla.javascript.ast.Name;
import sun.org.mozilla.javascript.ast.NodeVisitor;
import sun.org.mozilla.javascript.ast.PropertyGet;
import sun.org.mozilla.javascript.ast.StringLiteral;

/**
 *
 * @author rob
 */
public class JsFile
{
    private String classname;
    
    private File file;
    
    private boolean parsed;
    
    /**
     * 
     * @param file file java script class is contained in
     */
    JsFile(File file)
    {
        this.classname = "";
        this.file = file;
        this.parsed = false;
    }

    public String getClassname() throws IOException
    {
        this.parse();
        
        return this.classname;
    }
    
    public String getContents() throws IOException
    {
        return IOUtils.toString(new FileReader(this.file));
    }
    
    public String getPath()
    {
        return this.file.getAbsolutePath();
    }

    @Override
    public String toString()
    {
        return this.getPath();
    }
    
    /**
     * Extract and set source class name.
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void parse() throws FileNotFoundException, IOException
    {
        if (parsed) {
            return;
        }
        try (FileReader reader = new FileReader(file)) {
            AstNode rootNode = new Parser().parse(reader, file.getCanonicalPath(), 1);
            rootNode.visit(new sourceFileParser());
            reader.close();
        }
        
        parsed = true;
    }
    
    class sourceFileParser implements NodeVisitor
    {
        @Override
        public boolean visit(AstNode node) {
            setClassname(node);

            return true;
        }
        
        private void setClassname(AstNode node)
        {
            //all extjs classnames are declared like this:
            //Ext.define('classname', {})
            //The first argument passed to Ext.define is the class name.
            
            if (!(node instanceof FunctionCall)) {
                return;
            }
            
            FunctionCall funcCall = (FunctionCall) node;
            AstNode funcTarget = funcCall.getTarget();
            
            if (!(funcTarget instanceof PropertyGet)) {
                return;
            }
            
            PropertyGet propGet = (PropertyGet) funcTarget;
            AstNode propTarget = propGet.getTarget();
            
            if (!(propTarget instanceof Name)) {
                return;
            }
            
            Name propTargetName = (Name) propTarget;
            Name pgName = (Name) propGet.getProperty();
            
            if ("Ext".equals(propTargetName.getIdentifier()) && 
                    "define".equals(pgName.getIdentifier())) {
                List<AstNode> args = funcCall.getArguments();
                if (args.size() > 1) {
                    AstNode defArg = args.get(0);
                    if (defArg instanceof StringLiteral) {
                        StringLiteral defArgStr = (StringLiteral) defArg;
                        classname = defArgStr.getValue();
                    }
                }
            }
        }

    }
}
