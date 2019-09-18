package com.qaselenium.framework.testingobjectmodel;


public class MyPageFactory {

	public static <T> T getPage(Class<T> name, String domain) {
		T page = null;
		String className = name.getName();

		Class clazz;

		try {
			try {
				clazz = Class.forName(className + domain.toUpperCase());
			} catch (ClassNotFoundException e) {
				System.out
						.println("No specialized PageObject found, default one will be used instehad:"
								+ className);
				clazz = Class.forName(className);
			}

			if (clazz == null) {
				page = name.newInstance();
			} else {
				page = (T) clazz.newInstance();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Could not create class " + className, e);
		}

		return page;
	}
}
