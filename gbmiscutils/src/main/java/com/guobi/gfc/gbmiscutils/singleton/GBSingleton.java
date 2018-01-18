package com.guobi.gfc.gbmiscutils.singleton;

public abstract class GBSingleton {

	public abstract void trash();
	
	protected GBSingleton()
	{
		GBSingletonManager.register(this);
	}

}
