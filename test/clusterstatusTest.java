import junit.framework.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.Timer;
/*
 * clusterstatusTest.java
 * JUnit based test
 *
 * Created on 24 de Julho de 2006, 09:39
 */

/**
 *
 * @author collier
 */
public class clusterstatusTest extends TestCase {
    
    public clusterstatusTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(clusterstatusTest.class);
        
        return suite;
    }

    /**
     * Test of NodeClicked method, of class clusterstatus.
     */
    public void testNodeClicked() {
        System.out.println("NodeClicked");
        
        clusterstatus instance = new clusterstatus();
        
        instance.NodeClicked();
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of itemStateChanged method, of class clusterstatus.
     */
    public void testItemStateChanged() {
        System.out.println("itemStateChanged");
        
        ItemEvent e = null;
        clusterstatus instance = new clusterstatus();
        
        instance.itemStateChanged(e);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of SetMouseEvent method, of class clusterstatus.
     */
    public void testSetMouseEvent() {
        System.out.println("SetMouseEvent");
        
        clusterstatus instance = new clusterstatus();
        
        instance.SetMouseEvent();
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of SetTimer method, of class clusterstatus.
     */
    public void testSetTimer() {
        System.out.println("SetTimer");
        
        clusterstatus instance = new clusterstatus();
        
        instance.SetTimer();
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class clusterstatus.
     */
    public void testMain() {
        System.out.println("main");
        
        String[] args = null;
        
        clusterstatus.main(args);
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
