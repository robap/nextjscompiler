package com.freezerfrog.extjs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rob
 */
public class JsFileTest {
    
    public JsFileTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getClassname method, of class JsFile.
     */
    @Test
    public void testGetClassname() throws Exception {
        String path = getClass().getResource("/fixtures/foo.js").getFile();
        JsFile instance = new JsFile(new File(path));
        String expResult = "Sample.Foo";
        String result = instance.getClassname();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPath method, of class JsFile.
     */
    @Test
    public void testGetPath() {
        String path = getClass().getResource("/fixtures/foo.js").getFile();
        JsFile instance = new JsFile(new File(path));
        String expResult = path;
        String result = instance.getPath();
        assertEquals(expResult, result);
    }
    
    /**
     * Get deps defined by any of these means:
     * config.requires
     * config.uses
     * config.extend
     * Ext.require
     * 
     * Don't include the classname as a dep and don't include deps more than
     * once
     */
    @Test
    public void testGetDependencies() throws IOException {
        String path = getClass().getResource("/fixtures/foo.js").getFile();
        JsFile instance = new JsFile(new File(path));
        ArrayList<String> expResult = new ArrayList();
        expResult.add("OtherLib.SetByExtRequire");
        expResult.add("Sample.Bar");
        expResult.add("OtherLib.SetByUses");
        expResult.add("Ext.view.View");
        ArrayList<String> result = instance.getDependencies();
        assertEquals(expResult, result);
    }
}