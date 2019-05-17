package states.subscriber;

import events.AbstractEvent;

public class BState extends AbstractState{

    // Handle event based on logic of B State
    @Override
    public void handleEvent(AbstractEvent event, String channelName) {
       
    }
    public String toString() {
    	return "b";
    }
}
