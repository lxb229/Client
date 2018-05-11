package org.treediagram.nina.memcache.enhance;

import java.util.HashMap;
import java.util.Map;

public class EnhancedModel {
	public Map<Long, Long> endmap = new HashMap<Long, Long>();
	private static EnhancedModel _EnhancedModel = new EnhancedModel();

	public static EnhancedModel in() {
		return _EnhancedModel;
	}
	
	
}
