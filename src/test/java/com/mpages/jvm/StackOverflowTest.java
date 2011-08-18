package com.mpages.jvm;

import org.junit.Test;

public class StackOverflowTest {
	int start = 616;
    int recoveryCatch = start;
    int recoveryFinally = start;
    boolean funcIsDeadly = true;
    
    @Test
    public void test() {        
        try {
            deadlyFunc();
            
        } catch (StackOverflowError ex) {
            System.out.println("Caught");
            recovery(recoveryCatch);    /*625 max*/
            System.out.println("Handled");
            
        } finally {
        	recovery(recoveryFinally);   /*625 max*/
            System.out.println("Finally");
        }
    }

    @Test 
    public void testBarrier() {
        int increment = 1;
        while (true) {
            test();
            System.out.println("Was ok for "+recoveryCatch);
            recoveryCatch+=increment;
            recoveryFinally+=increment;
        }
    }
    
    private void recovery(int i) {
        if (i == 0) {
            System.out.println("Recovery normally executed.");
        } else {
            recovery(i - 1);
        }
    }

    private void deadlyFunc() {
    	//try {
	        if (funcIsDeadly) {        	
	            deadlyFunc();
	        }
    	/*} catch (StackOverflowError ex) {
    		System.out.println("Died");
    		recovery(36);
    		System.out.println("Restored");
    	}*/
    }
}