/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import sun.org.mozilla.javascript.Parser;
import sun.org.mozilla.javascript.ast.ArrayLiteral;
import sun.org.mozilla.javascript.ast.AstNode;
import sun.org.mozilla.javascript.ast.FunctionCall;
import sun.org.mozilla.javascript.ast.Name;
import sun.org.mozilla.javascript.ast.NodeVisitor;
import sun.org.mozilla.javascript.ast.ObjectLiteral;
import sun.org.mozilla.javascript.ast.ObjectProperty;
import sun.org.mozilla.javascript.ast.PropertyGet;
import sun.org.mozilla.javascript.ast.StringLiteral;

/**
 *
 * @author rob
 */
public class JsFile
{
    private String classname;
    
    private ArrayList<String> deps;
    
    private File file;
    
    private boolean parsed;
    
    /**
     * 
     * @param file file java script class is contained in
     */
    JsFile(File file)
    {
        this.classname = "";
        this.deps = new ArrayList();
        this.file = file;
        this.parsed = false;
    }

    public String getClassname() throws IOException
    {
        this.parse();
        
        return this.classname;
    }
    
    /**
     * Retrieve a list of dependency class names.
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getDependencies() throws IOException
    {
        this.parse();
        
        return deps;
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
    
    private void addDep(String dep)
    {
        //a class cannot have itself as a dependency
        if (dep.equals(classname)) {
            return;
        }
        
        //don't duplicate deps
        if (deps.contains(dep)) {
            return;
        }
        
        deps.add(dep);
    }
    
    private void addDep(StringLiteral dep)
    {
        addDep(dep.getValue());
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
            //all extjs classnames are declared like this:
            //Ext.define('classname', {})
            //The first argument passed to Ext.define is the class name.
            
            if (!(node instanceof FunctionCall)) {
                return true;
            }
            
            FunctionCall funcCall = (FunctionCall) node;
            AstNode funcTarget = funcCall.getTarget();
            
            if (!(funcTarget instanceof PropertyGet)) {
                return true;
            }
            
            PropertyGet propGet = (PropertyGet) funcTarget;
            AstNode propTarget = propGet.getTarget();
            
            if (!(propTarget instanceof Name)) {
                return true;
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
                    
                    if (args.get(1) instanceof ObjectLiteral) {
                        setDepsByLiteral((ObjectLiteral) args.get(1));
                    } else {
                        System.out.println("WARN Ext.define does not conform to "
                                + "sencha api. Deps cannot be handled. " 
                                + getPath() + " " + args.get(1).getClass());
                    }
                }
            } else if ("Ext".equals(propTargetName.getIdentifier()) && 
                    "require".equals(pgName.getIdentifier())) {
                List<AstNode> args = funcCall.getArguments();
                if (args.size() > 0) {
                    AstNode reqArg = args.get(0);
                    if (reqArg instanceof StringLiteral) {
                        addDep((StringLiteral) reqArg);
                    } else if (reqArg instanceof ArrayLiteral) {
                        for (AstNode localDep : ((ArrayLiteral) reqArg).getElements()) {
                            addDep((StringLiteral) localDep);
                        }
                    }
                }
            }

            return true;
        }

        private void setDepsByLiteral(ObjectLiteral node)
        {
            for (ObjectProperty config : node.getElements()) {
                if (config.getLeft() instanceof Name) {
                    Name configName = (Name) config.getLeft();
                    if (("uses".equals(configName.getIdentifier()) || "requires".equals(configName.getIdentifier())) 
                            && config.getRight() instanceof ArrayLiteral) {
                        ArrayLiteral localDeps = (ArrayLiteral) config.getRight();
                        for (AstNode localDep : localDeps.getElements()) {
                            addDep((StringLiteral) localDep);
                        }
                    } else if ("extend".equals(configName.getIdentifier()) 
                            && config.getRight() instanceof StringLiteral) {
                        addDep((StringLiteral) config.getRight());
                    }
                }
            }
        }
    }
}
