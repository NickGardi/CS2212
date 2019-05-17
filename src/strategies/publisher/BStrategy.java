package strategies.publisher;


import java.util.ArrayList;
import java.util.List;

import events.AbstractEvent;
import events.EventFactory;
import events.EventMessage;
import events.EventType;
import pubSubServer.AbstractChannel;
import pubSubServer.ChannelDiscovery;
import pubSubServer.ChannelEventDispatcher;

public class BStrategy extends AbstractStrategy {

    @Override
    // This method generates both the appropriate event and determines which channels to post it to, based on BStrategy logic
    // This logic consists of events only being posted to channels that begin with letters i-q . 
    public void doPublish(int publisherId) {
    	
    	EventMessage message = new EventMessage("", "");
    	AbstractEvent a = EventFactory.createEvent(EventType.TypeB, publisherId, message);
    	
    	List<String> ChannelListB = new ArrayList<String>();

    	
    	
    	for (AbstractChannel channel : ChannelDiscovery.getInstance().listChannels()) {
    		if(channel.getChannelTopic().charAt(0) >= "i".charAt(0) & channel.getChannelTopic().charAt(0) < "r".charAt(0)) {
    			ChannelListB.add(channel.getChannelTopic());
    		}
    	}
    	
    	System.out.println("Publisher "  + publisherId + " publishes event " + a.hashCode());
    	ChannelEventDispatcher.getInstance().postEvent(a, ChannelListB, publisherId);

    }

    @Override
    // This method publishes an event that comes from the publisher and determines which channels to post it to, based on BStrategy logic.
    // This logic consists of events only being posted to channels that begin with letters i-q .
    public void doPublish(AbstractEvent event, int publisherId) {
    	
    	List<String> ChannelListB = new ArrayList<String>();

    	for (AbstractChannel channel : ChannelDiscovery.getInstance().listChannels()) {
    		if(channel.getChannelTopic().charAt(0) >= "i".charAt(0) & channel.getChannelTopic().charAt(0) < "r".charAt(0)) {
    			ChannelListB.add(channel.getChannelTopic());
    		}
    	}
    	
    	System.out.println("Publisher "  + publisherId + " publishes event " + event.hashCode());
    	ChannelEventDispatcher.getInstance().postEvent(event, ChannelListB, publisherId);

    	
    }
}
