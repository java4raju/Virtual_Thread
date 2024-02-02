package com.java4raju.vt.sc;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.java4raju.vt.sc.LongRunningTask.TaskResponse;

/**
 * Simulates a Task which can be used for testing. 
 * It responds to following events
 * to interrupts 
 * cleanly terminates on interruption or failure. * 
 * it is can given a name to the task, 
 * specify how much time it should take to run and 
 * also specify if it should fail eventually.
 *
 */
public class LongRunningTask implements Callable<TaskResponse> {

    private final String name;
    private final int time;
    private final String output;
    private final boolean fail;
    
    // Record : Represents successful response of the Task
    public record TaskResponse(String name, String response, long timeTaken) {
        
    }

    /*
     * Create a Task with a name, time taken to complete and whether
     * it should eventually fail.
     */
    public LongRunningTask(String name, int time, String output, boolean fail) {
        this.name = name;
        this.time = time;
        this.output = output;
        this.fail = fail;
    }

    /*
     * Body of the task which will be run on a separate Thread (mostly
     * Virtual Thread)
     */
    @Override
    public TaskResponse call() throws InterruptedException {

        display("Started");
        long start = System.currentTimeMillis();

        int numSecs = 0;
        while (numSecs++ < time) {
            
            if (Thread.interrupted()) {
                throwInterruptedException();
            }
            
            display("Working .." + numSecs);
            
             // process data (Code not shown) which uses CPU for 0.2 secs 
            
            try {
                Thread.sleep(Duration.ofSeconds(1));
                // new HttpCaller(name).makeCall(1);
            }
            catch (InterruptedException intExp) {
                throwInterruptedException();
            }

            // process data (Code not shown) which uses CPU for 0.2 secs 
        }
        
        /* simulate failure of task */
        if (fail) {
            throwExceptionOnFailure();
        }
        
        display("Completed");
        long end = System.currentTimeMillis();
        return new TaskResponse(this.name, this.output, end-start);

    }

    private void throwExceptionOnFailure() {
        display("Failed");
        throw new RuntimeException(name +  " : Failed");
    }

    private void throwInterruptedException() throws InterruptedException {
        
        display("Interrupted");
        throw new InterruptedException(name +  " : Interrupted");
    }

    private void display(String message) {
        System.out.printf("> %s : %s\n", name, message);
    }
    
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        System.out.println("Main Thread : Started");
        LongRunningTask task = new LongRunningTask("LongTask1", 10, "json-response1", false);
        
        try (ExecutorService service = Executors.newFixedThreadPool(2)) {
            Future<TaskResponse> taskFtr = service.submit(task);
            
            Thread.sleep(Duration.ofSeconds(9));
            taskFtr.cancel(true);
        }
        
        System.out.println("Main Thread : Completed");
    }
    
}
