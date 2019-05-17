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

public class DefaultStrategy extends AbstractStrategy{

    @Override
    // This method generates both the appropriate event and determines which channels to post it to, based on DefaultStrategy logic
    // This logic consists of events being posted to all channels in the PubSubServer.
    public void doPublish(int publisherId) {

    	EventMessage message = new EventMessage("", "");
    	AbstractEvent a = EventFactory.createEvent(EventType.TypeA, publisherId, message);
    	List<String> ChannelListDefault = new ArrayList<String>();
    	
       	for (AbstractChannel channel : ChannelDiscovery.getInstance().listChannels()) {
    		ChannelListDefault.add(channel.getChannelTopic());

    	}
    	
       	System.out.println("Publisher "  + publisherId + " publishes event " + a.hashCode());
    	ChannelEventDispatcher.getInstance().postEvent(a, ChannelListDefault, publisherId);
    	
    }

    @Override
    // This method publishes an event that comes from the publisher and determines which channels to post it to, based on Default Strategy logic.
    // This logic consists of events being posted to all channels in the PubSubServer.
    public void doPublish(AbstractEvent event, int publisherId) {
    	
    	List<String> ChannelListDefault = new ArrayList<String>();
    	
       	for (AbstractChannel channel : ChannelDiscovery.getInstance().listChannels()) {
    		ChannelListDefault.add(channel.getChannelTopic());

    	}
    	
       	System.out.println("Publisher "  + publisherId + " publishes event " + event.hashCode());
    	ChannelEventDispatcher.getInstance().postEvent(event, ChannelListDefault, publisherId);
    	

    }
}
