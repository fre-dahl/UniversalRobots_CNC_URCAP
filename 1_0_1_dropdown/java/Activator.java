package com.seal.student.thenewapp.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(SwingInstallationNodeService.class, new CNCInstallationNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new CNCProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

