package communication;

public class SemBoundedBuffer extends BoundedBuffer {
    Semaphore emptySlots, fullSlots;
    static boolean broadcastLock = false;
    static String waitingForMessage;

    public SemBoundedBuffer (int maxSize) {
        super(maxSize);
        emptySlots = new Semaphore(maxSize);
        fullSlots  = new Semaphore(0);
    }
    
    static public boolean getBroadcastLock(){
    	return broadcastLock;
    }
    
    static public void setBroadcastLock(boolean count){
    	broadcastLock = count;
    }
    
    static public void setWaitingMessage(String message){
    	waitingForMessage = message;
    }
    
    static public String getWaitingMessage(){
    	return waitingForMessage;
    }

    public Object get() throws InterruptedException {
        Object value;
            // Suspend until a full slot is available
        	fullSlots.acquire();
        	value = super.get();
        	System.out.println("OBJECT "+(String)value+" TAKEN FROM THE BUFFER");
            emptySlots.release();
            // Release an empty slot

        return value;
    }


    public void put(Object value) throws InterruptedException {
    		// Suspend until an empty slot is available
    		emptySlots.acquire();
    		System.out.println("OBJECT "+(String)value+" PUT IN THE BUFFER");
            super.put(value);
        	fullSlots.release();
        	// Release an empty slot

    }
}