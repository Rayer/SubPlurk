package com.rayer.util.event;


public class OnHandlerDetachedEvent extends EventBase {

	@Override
	public EventParamBase createParameters() {
		return new EventParamBase(OnHandlerDetachedEvent.class){

			@Override
			public int getArg1() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getArg2() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getObj() {
				// TODO Auto-generated method stub
				return null;
			}};
	}

}
