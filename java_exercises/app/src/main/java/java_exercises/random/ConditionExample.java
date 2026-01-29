package java_exercises.random;


public class ConditionExample {
    static final Object lockObject = new Object();
    private boolean isReady = false;

    public void waitForCondition() throws InterruptedException {
        synchronized(lockObject) {
            // Warten, bis die Bedingung erfüllt ist
            while (!isReady) {
                System.out.println("Waiting for condition");
                lockObject.wait();
            }
            // Bedingung ist erfüllt, fahre fort
            System.out.println("Condition met, proceeding with execution.");
        }
    }

    public void setConditionMet() throws InterruptedException {
        
        synchronized(lockObject) {
            // Bedingung wird erfüllt
            isReady = true;
            // Benachrichtige alle wartenden Threads
            lockObject.notify();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionExample example = new ConditionExample();

        // Thread, der auf die Bedingung wartet
        Thread waitingThread = new Thread(() -> {
            try {
                example.waitForCondition();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Thread, der die Bedingung erfüllt
        Thread signalingThread = new Thread(() -> {
            try {
                example.setConditionMet();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        waitingThread.start();
        Thread.sleep(3000);
        signalingThread.start();
    }
}