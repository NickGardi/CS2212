package states.subscriber;

import events.AbstractEvent;

public class DefaultState extends AbstractState{

    // Handle event based on logic of Default State
    @Override
    public void handleEvent(AbstractEvent event, String channelName) {
        
    }
    public String toString() {
    	return "default";
    }
}
