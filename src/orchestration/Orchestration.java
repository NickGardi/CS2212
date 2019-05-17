package orchestration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import events.AbstractEvent;
import events.EventFactory;
import events.EventMessage;
import events.EventType;
import pubSubServer.AbstractChannel;
import pubSubServer.ChannelAccessControl;
import pubSubServer.ChannelDiscovery;
import pubSubServer.SubscriptionManager;
import publishers.AbstractPublisher;
import publishers.PublisherFactory;
import publishers.PublisherType;
import states.subscriber.StateName;
import strategies.publisher.StrategyName;
import subscribers.AbstractSubscriber;
import subscribers.SubscriberFactory;
import subscribers.SubscriberType;

public class Orchestration {

	public static void main(String[] args) {
		// Creates lists of publishers, subscribers, channels, and commands
		List<AbstractPublisher> listOfPublishers = new ArrayList<>();
		List<AbstractSubscriber> listOfSubscribers = new ArrayList<>();
		List<String> listOfCommands = new ArrayList<>();
		List<String> channelList = new ArrayList<String>();
		Orchestration testHarness = new Orchestration();
		try {
			listOfPublishers = testHarness.createPublishers();
			listOfSubscribers = testHarness.createSubscribers();
			listOfCommands = testHarness.createCommands();
			//Get all channels and store in an ArrayList
			List<AbstractChannel> channels = ChannelDiscovery.getInstance().listChannels();
			try {
			BufferedReader initialChannels = new BufferedReader(new FileReader(new File("Channels.chl")));
			String line = "";
			while((line = initialChannels.readLine()) != null ){
				channelList.add(line);
			}
			initialChannels.close();
			}catch(IOException ioe) {
				System.out.println("Loading Channels from file failed proceeding with random selection");
				for(AbstractSubscriber subscriber : listOfSubscribers) {
					int index = (int) Math.round((Math.random()*10))/3;
					SubscriptionManager.getInstance().subscribe(channels.get(index).getChannelTopic(), subscriber);
				}
			}
			//Read command file and execute each command 
			for(String command : listOfCommands) {
				String[] commandCharacteristics = command.split(" ");
				//Publish Events
				if (commandCharacteristics [0].equals("PUB")){
					int pubId = Integer.parseInt(commandCharacteristics[1]);
					int index = findPub(listOfPublishers,pubId);
					if(index == -1) {
						System.out.println("Publisher " + commandCharacteristics[1] + " does not exist");
					}else {
						//Publish if no event is given
						if (commandCharacteristics.length == 2){
							listOfPublishers.get(index).publish();
						}
						//Publish with given event
						else{
							//Create event
							EventMessage newEventMessage = new EventMessage(commandCharacteristics[3],commandCharacteristics[4]);
							EventType type = null;
							if (commandCharacteristics[2].equals("TypeA")){
								type = EventType.values()[1];
							}
							else if (commandCharacteristics[2].equals("TypeB")){
								type = EventType.values()[0];
							}
							else if (commandCharacteristics[2].equals("TypeC")){
								type = EventType.values()[2];
							}
							AbstractEvent newEvent = EventFactory.createEvent(type, pubId, newEventMessage);
							listOfPublishers.get(index).publish(newEvent);
						}
					}
				}
				else if (commandCharacteristics [0].equals("SUB")){
					//Subscribe to channel
					int subId = Integer.parseInt(commandCharacteristics[1]);
					int index = findSub(listOfSubscribers,subId); 
					if(index == -1) {
						System.out.println("Subscriber " + commandCharacteristics[1] + " does not exist");
					}else {
						int size = channelList.size();
						for (int i=0; i< size; i++){
							
							if (channels.get(i).getChannelTopic().equals(commandCharacteristics[2])){
								SubscriptionManager.getInstance().subscribe(channels.get(i).getChannelTopic(), listOfSubscribers.get(index));
							}
						}
					}
				}
				else {
					int subId = Integer.parseInt(commandCharacteristics[1]);
					int index = findSub(listOfSubscribers,subId);
					if(index == -1) {
						System.out.println("Subscriber "+ commandCharacteristics[1] + " cannot be found in the system and therefore cannot be " + commandCharacteristics[0].toLowerCase() + "ed from " + commandCharacteristics[2]);
					}else {
						//Unblock subscriber
						if (ChannelAccessControl.getInstance().checkIfBlocked(listOfSubscribers.get(index),commandCharacteristics[2])){
							if (commandCharacteristics [0].equals("UNBLOCK")){
								ChannelAccessControl.getInstance().unBlockSubscriber(listOfSubscribers.get(index),commandCharacteristics[2]);
							}else {
								System.out.println("Command \"" + command + "\" cannot be performed");
							}
						}
						else{
							//Block Subscriber 
							if (commandCharacteristics [0].equals("BLOCK")){
								ChannelAccessControl.getInstance().blockSubcriber(listOfSubscribers.get(index),commandCharacteristics[2]);
							}else {
								System.out.println("Command \"" + command + "\" cannot be performed");
							}
						}
					}
				}
			}
			
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			System.out.println("Will now terminate");
			return;
		}
		
		
	}

	
	private List<AbstractPublisher> createPublishers() throws IOException{
		//Creates an ArrayList of AbstractPublishers
		List<AbstractPublisher> listOfPublishers = new ArrayList<>();
		AbstractPublisher newPub;
		//Read in file
		BufferedReader StrategyBufferedReader = new BufferedReader(new FileReader(new File("Strategies.str")));
		while(StrategyBufferedReader.ready()) {
			String PublisherConfigLine = StrategyBufferedReader.readLine();
			String[] PublisherConfigArray = PublisherConfigLine.split("\t");
			int[] PublisherConfigIntArray = new int[2];
			for(int i = 0; i < PublisherConfigArray.length; i++)
				PublisherConfigIntArray[i] = Integer.parseInt(PublisherConfigArray[i]);
			//Make publishers=
			newPub = PublisherFactory.createPublisher(
					PublisherType.values()[PublisherConfigIntArray[0]],
					StrategyName.values()[PublisherConfigIntArray[1]]);
			listOfPublishers.add(newPub);
		}
		StrategyBufferedReader.close();
		return listOfPublishers;
	}
	
	private List<AbstractSubscriber> createSubscribers() throws IOException{
		//Creates an ArrayList of AbstractPublishers
		List<AbstractSubscriber> listOfSubscribers = new ArrayList<>();
		AbstractSubscriber newSub;
		//Read in a file
		BufferedReader StateBufferedReader = new BufferedReader(new FileReader(new File("States.sts")));
		while(StateBufferedReader.ready()) {
			String StateConfigLine = StateBufferedReader.readLine();
			String[] StateConfigArray = StateConfigLine.split("\t");
			int[] StateConfigIntArray = new int[2];
			for(int i = 0; i < StateConfigArray.length; i++)
				StateConfigIntArray[i] = Integer.parseInt(StateConfigArray[i]);
			//Make subscriber 
			newSub = SubscriberFactory.createSubscriber(
					SubscriberType.values()[StateConfigIntArray[0]], 
					StateName.values()[StateConfigIntArray[1]]);
			listOfSubscribers.add(newSub);
		}
		StateBufferedReader.close();
		return listOfSubscribers;
	}
	
	private List<String> createCommands() throws IOException{
		//Create an List of strings where a string is a line in command.txt, ie a string is a command 
		List<String> commandList = new ArrayList<>();
		BufferedReader StateBufferedReader = new BufferedReader(new FileReader(new File("Commands.txt")));
		while(StateBufferedReader.ready()){
			String newCommandLine = StateBufferedReader.readLine();
			commandList.add(newCommandLine);
		}
		StateBufferedReader.close();
		return commandList;
	}
	
	private static int findPub (List<AbstractPublisher> listOfPublishers, int id) {
		//find a given publisher
		int index=0;
		boolean found = false;
		for (int i=0; i<listOfPublishers.size(); i++) {
			if (listOfPublishers.get(i).hashCode() == id) {
				found = true;
				index = i;
			}
		}
		if (found == true)
			return index;
		else 
			return -1;
	}
	private static int findSub (List<AbstractSubscriber> listOfSubscribers, int id) {
		//find a given subscriber 
		int index=0;
		boolean changed = false;
		for (int i=0; i<listOfSubscribers.size(); i++) {
			if (listOfSubscribers.get(i).hashCode() == id) {
				changed = true;
				index = i;
			}
		}
		if (changed == true)
			return index;
		else
			return -1;
	}
	
}