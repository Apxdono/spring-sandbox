package org.apx.utils;

import java.util.UUID;

/**
 * Created by oleg on 3/13/14.
 */
public abstract class Generator {

	public static synchronized String UUID(){
		return UUID.randomUUID().toString();
	}

}
