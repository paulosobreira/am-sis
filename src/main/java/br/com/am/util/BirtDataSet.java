package br.com.am.util;

import java.util.Map;

public class BirtDataSet {

	public void close() {
	}

	public void open(Object appContext, Map<String, Object> map) {

	}

	// this method is a mandatory method. It must be implemented. This method
	// is used by the BIRT Reporting engine.
	public Object next() {
		return null;
	}

}
