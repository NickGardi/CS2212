package subscribers;

import states.subscriber.StateName;


/**
 * @author kkontog, ktsiouni, mgrigori
 *  
 */
/**
 * @author kkontog, ktsiouni, mgrigori
 * creates new {@link AbstractSubscriber} objects
 * contributes to the State design pattern
 * implements the FactoryMethod design pattern   
 */
public class SubscriberFactory {

	
	/**
	 * creates a new {@link AbstractSubscriber} using an entry from the {@link SubscriberType} enumeration
	 * @param subscriberType a value from the {@link SubscriberType} enumeration specifying the type of Subscriber to be created 
	 * @return the newly created {@link AbstractSubscriber} instance 
	 */
	public static AbstractSubscriber createSubscriber(SubscriberType subscriberType, StateName stateName) {
		
		switch (subscriberType) {
			case alpha : 
				ConcreteSubscriberA a = new ConcreteSubscriberA();
				System.out.println("Subscriber " + a.hashCode() + " created");
				a.setState(stateName);
				
				return a;
			case beta :
				ConcreteSubscriberA b = new ConcreteSubscriberA();
				System.out.println("Subscriber " + b.hashCode() + " created");
				b.setState(stateName);
				return b;
				
			case gamma :
				ConcreteSubscriberA c = new ConcreteSubscriberA();
				System.out.println("Subscriber " + c.hashCode() + " created");
				c.setState(stateName);
				return c;
			default:
				ConcreteSubscriberA d = new ConcreteSubscriberA();
				System.out.println("Subscriber " + d.hashCode() + " created");
				d.setState(stateName);
				return d;
		}
	}
	
}
