package subscribers;

import events.AbstractEvent;
import pubSubServer.SubscriptionManager;
import states.subscriber.*;


/**
 * @author kkontog, ktsiouni, mgrigori
 * an example concrete subscriber
 */
class ConcreteSubscriberA extends AbstractSubscriber {

	
	protected ConcreteSubscriberA() {
		state = StateFactory.createState(StateName.defaultState);
	}
	
	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#setState(states.subscriber.StateName)
	 */
	public void setState(StateName stateName) {
		state = StateFactory.createState(stateName);
		if (state instanceof AState) {
			// Subscriber with astate
			System.out.println("Subscriber " + this.hashCode() + " is on state " + StateName.values()[0]);
		}
		else if (state instanceof BState){
			// Subscriber with bstate
			System.out.println("Subscriber " + this.hashCode() + " is on state " + StateName.values()[2]);
		}
		else if (state instanceof CState){
			// Subscriber with cstate
			System.out.println("Subscriber " + this.hashCode() + " is on state " + StateName.values()[3]);
		}
		else {
			// Subscriber with defaultState
			System.out.println("Subscriber " + this.hashCode() + " is on state " + StateName.values()[1]);
		}
	}
	
	private String getState() {
		return state.toString();
	}
	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#alert(events.AbstractEvent, java.lang.String)
	 */
	@Override
	public void alert(AbstractEvent event, String channelName) {
		System.out.println("Subscriber " + this.hashCode() + " receives event " + event.hashCode() + " and handles it on state " + state.toString());
		state.handleEvent(event, channelName);
	}

	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#subscribe(java.lang.String)
	 */
	@Override
	public void subscribe(String channelName) {
		SubscriptionManager.getInstance().subscribe(channelName, this);		
	}

	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String channelName) {
		SubscriptionManager.getInstance().subscribe(channelName, this);
		
	}
	
	
}
