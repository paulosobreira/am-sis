package br.com.am.util;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

import javax.servlet.ServletContext;
import java.util.logging.Level;


public class BirtEngine {

	private static IReportEngine birtEngine = null;

	public static synchronized IReportEngine getBirtEngine(ServletContext sc) {
		if (birtEngine == null) {
			EngineConfig config = new EngineConfig();
			String logLevel = br.com.am.recursos.Recursos.getProperties().getProperty("birtLog");
			Level level = Level.OFF;
			if ("SEVERE".equalsIgnoreCase(logLevel)) {
				level = Level.SEVERE;
			} else if ("WARNING".equalsIgnoreCase(logLevel)) {
				level = Level.WARNING;
			} else if ("INFO".equalsIgnoreCase(logLevel)) {
				level = Level.INFO;
			} else if ("CONFIG".equalsIgnoreCase(logLevel)) {
				level = Level.CONFIG;
			} else if ("FINE".equalsIgnoreCase(logLevel)) {
				level = Level.FINE;
			} else if ("FINER".equalsIgnoreCase(logLevel)) {
				level = Level.FINER;
			} else if ("FINEST".equalsIgnoreCase(logLevel)) {
				level = Level.FINEST;
			} else if ("OFF".equalsIgnoreCase(logLevel)) {
				level = Level.OFF;
			}
			String birtLogDir = br.com.am.recursos.Recursos.getProperties()
					.getProperty("birtLogDir");
			if (!br.com.am.util.Util.isNullOrEmpty(birtLogDir)) {
				config.setLogConfig(birtLogDir, level);
			}
			config.setEngineHome("");
			config.getAppContext().put(DataEngine.MEMORY_USAGE,
					DataEngine.MEMORY_USAGE_CONSERVATIVE);
			config.getAppContext().put(DataEngine.MEMORY_BUFFER_SIZE, 10);
			config.getAppContext().put(DataEngine.MEMORY_DATA_SET_CACHE,
					new Integer(0));
			config.getAppContext().put(
					EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
					BirtEngine.class.getClassLoader());

			IPlatformContext context = new PlatformServletContext(sc);
			config.setPlatformContext(context);
			// Create the report engine
			// birtEngine = new ReportEngine( config );
			// ReportEngine engine = new ReportEngine( null );

			try {
				Platform.startup(config);
			} catch (BirtException e) {
				e.printStackTrace();
			}

			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(
							IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			birtEngine = factory.createReportEngine(config);

		}
		return birtEngine;
	}

	public static synchronized void destroyBirtEngine() {
		if (birtEngine == null) {
			return;
		}
		birtEngine.destroy();
		Platform.shutdown();
		birtEngine = null;
		System.gc();
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
