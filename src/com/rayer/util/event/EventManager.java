package com.rayer.util.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.os.Message;

/**
 * Route system event and dispatch it.
 * @author rayer
 *
 */
public class EventManager {
	HashMap<Class<? extends EventBase>, ArrayList<EventProcessHandler> > mEventHandlerMap = new HashMap<Class<? extends EventBase>, ArrayList<EventProcessHandler> >();
	
	/**
	 * Constructor, only SystemManager allow to create it.
	 * Put it here is due to Java Language restriction.
	 */
	protected EventManager() {
		
	}
	
	/**
	 * Register a handler to an event.
	 * @param event
	 * @param eph
	 * @return
	 */
	public boolean registerHandler(Class<? extends EventBase> event, EventProcessHandler eph) {
		//int id = event.hashCode();
		ArrayList<EventProcessHandler> handlerList = null;
		
		if(mEventHandlerMap.containsKey(event) == true) {
			handlerList = mEventHandlerMap.get(event);
		}
		else {
			handlerList = new ArrayList<EventProcessHandler>();
			mEventHandlerMap.put(event, handlerList);

		}
		
		if(handlerList.contains(eph))
			return false;
		
		handlerList.add(eph);
		
		

		return true;
	}
	
	//要不要增加一個通知EventProcessHandler「你被fire啦挖哈哈哈哈哈哈」的訊息呢(思考)
	/**
	 * Remove a notification handler in specified event. In most case you should use it to safely remove handler.
	 * @param event
	 * @param eph
	 * @return
	 */
	boolean removeHandler(EventBase event, EventProcessHandler eph) {
		ArrayList<EventProcessHandler> handlerList = mEventHandlerMap.get(event.getID());
		if(handlerList == null)
			return false;
		
		boolean ret = handlerList.remove(eph);
		if(ret == true)
			this.sendMessageToSpecifiedHandler(eph, new OnHandlerDetachedEvent());
		
		return ret;
	}
	
	/**
	 * Send a message to a specified handler, internal use only!
	 * @param eph Target handler
	 * @param EventBase The event which need to be passed to the handler
	 */
	private void sendMessageToSpecifiedHandler(EventProcessHandler eph,
			EventBase event) {
		EventParamBase ep = event.createParameters();
		Message msg = eph.obtainMessage(ep.getWhat());
		if(msg == null)
			msg = eph.obtainMessage();
		
		if(msg == null)
			msg = new Message();
		
		ep.generateMessage(msg);
		eph.sendMessage(msg);
	}

	/**
	 * Strip a whole event out of notification list. WARNING : Use it carefully.
	 * @param event
	 * @return
	 */
	boolean removeHandler(EventBase event) {
		return mEventHandlerMap.remove(event.getID()) != null;
	}
	
	/**
	 * Strip a event handler out of whole notification list, in all events.
	 * Its overhead seems so costly....
	 * @param eph target EventProcessHandler
	 * @return true if any EventProcessHandler is removed, false for otherwise.
	 */
	boolean removeHandler(EventProcessHandler eph) {
		Set<Entry<Class<? extends EventBase>, ArrayList<EventProcessHandler> > > set = mEventHandlerMap.entrySet();
		boolean ret = false;
		for(Entry<Class<? extends EventBase>, ArrayList<EventProcessHandler>> ent : set) 
			ret = ((ret == true) || ent.getValue().remove(eph)) ? true : false;
		
		return ret;
	}
	
	public boolean sendMessage(EventBase base) {
		
		ArrayList<EventProcessHandler> handlerList = mEventHandlerMap.get(base.getClass());
		if(handlerList == null) {
			return false;
		}
		
		EventParamBase ep = base.createParameters();
		
		for(EventProcessHandler eph : handlerList) {
			Message msg = eph.obtainMessage(ep.getWhat());
			if(msg == null)
				msg = eph.obtainMessage();
			
			if(msg == null)
				msg = new Message();
			
			ep.generateMessage(msg);
			eph.sendMessage(msg);
		}
		
		return true;
	}
	
	/**
	 * 	Reset this event handler.
	 * @param notifyHandler true if you want to send HandlerRemoveEvent, false for otherwise.
	 */
	public void reset(boolean notifyHandler) {
		mEventHandlerMap = new HashMap<Class<? extends EventBase>, ArrayList<EventProcessHandler> >();;
	}

}
