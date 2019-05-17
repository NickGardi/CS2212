package strategies.publisher;


import java.util.ArrayList;
import java.util.List;

import events.AbstractEvent;
import events.EventFactory;
import events.EventType;
import pubSubServer.AbstractChannel;
import pubSubServer.ChannelDiscovery;
import pubSubServer.ChannelEventDispatcher;
import subscribers.AbstractSubscriber;
import events.EventMessage;

public class AStrategy extends AbstractStrategy{

    @Override
    // This method generates both the appropriate event and determines which channels to post it to, based on AStrategy logic
    // This logic consists of events only being posted to channels that begin with letters that come before "i" (a-h).
    public void doPublish(int publisherId) {
    	EventMessage message = new EventMessage("", "");
    	AbstractEvent a = EventFactory.createEvent(EventType.TypeA, publisherId, message);
    	
    	List<String> ChannelListA = new ArrayList<String>();

    	for (AbstractChannel channel : ChannelDiscovery.getInstance().listChannels()) {
    		if(channel.getChannelTopic().charAt(0) < "i".charAt(0)) {
    			ChannelListA.add(channel.getChannelTopic());
    		}
    	}
    	
    	System.out.println("Publisher "  + publisherId + " publishes event " + a.hashCode());
    	ChannelEventDispatcher.getInstance().postEvent(a, ChannelListA, publisherId);
    }

    @Override
    // This method publishes an event that comes from the publisher and determines which channels to post it to, based on AStrategy logic.
    // This logic consists of events only being posted to channels that begin with letters that come before "i" (a-h).
    public void doPublish(AbstractEvent event, int publisherId) {
    	
    	List<String> ChannelListA = new ArrayList<String>();
    	
    	for (AbstractChannel channel : ChannelDiscovery.getInstance().listChannels()) {
    		if(channel.getChannelTopic().charAt(0) < "i".charAt(0)) {
    			ChannelListA.add(channel.getChannelTopic());
    		}
    	}
    	System.out.println("Publisher "  + publisherId + " publishes event " + event.hashCode());
    	ChannelEventDispatcher.getInstance().postEvent(event, ChannelListA, publisherId);

    }
}
