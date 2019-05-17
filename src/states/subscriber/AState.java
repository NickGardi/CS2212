package states.subscriber;

import events.AbstractEvent;

public class AState extends AbstractState{

    // Handle event based on logic of A State

    @Override
    public void handleEvent(AbstractEvent event, String channelName) {
        
    }
    public String toString() {
    	return "a";
    }
}
