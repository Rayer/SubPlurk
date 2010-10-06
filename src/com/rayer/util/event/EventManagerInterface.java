package com.rayer.util.event;

public interface EventManagerInterface {

	/**
	 * Register a handler to an event.
	 * @param event
	 * @param eph
	 * @return
	 */
	public boolean registerHandler(Class<? extends EventBase> event,
			EventProcessHandler eph);

	//要不要增加一個通知EventProcessHandler「你被fire啦挖哈哈哈哈哈哈」的訊息呢(思考)
	/**
	 * Remove a notification handler in specified event. In most case you should use it to safely remove handler.
	 * @param event
	 * @param eph
	 * @return
	 */
	public boolean removeHandler(EventBase event, EventProcessHandler eph);

	/**
	 * Strip a whole event out of notification list. WARNING : Use it carefully.
	 * @param event
	 * @return
	 */
	public boolean removeHandler(EventBase event);

	/**
	 * Main message routine.
	 * @param base
	 * @return
	 */
	public boolean sendMessage(EventBase base);

	/**
	 * 	Reset this event handler.
	 * @param notifyHandler true if you want to send HandlerRemoveEvent, false for otherwise.
	 */
	public void reset(boolean notifyHandler);

}