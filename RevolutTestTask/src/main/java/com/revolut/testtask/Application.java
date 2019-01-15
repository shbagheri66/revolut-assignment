package com.revolut.testtask;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.revolut.testtask.controller.AccountRestController;
/**
 * Main Class (Starting point) 
 */
public class Application {

	private static Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		logger.info("Starting server");
		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",AccountRestController.class.getCanonicalName());
		try {
			server.start();
			logger.info("Server started on port 8080");
			server.join();
		} finally {
			server.destroy();
		}
	}
}
