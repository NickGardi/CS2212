package states.subscriber;

import events.AbstractEvent;

public class AbstractState implements IState {
	/**
	 * Parent class for concrete classes AState, BState, DefaultState
	 */


	/**
	 *
	 * @param event Current Event
	 * @param channelName Channel Name
	 */

	@Override
	public void handleEvent(AbstractEvent event, String channelName) {}

}
