package com.seal.student.thenewapp.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class CNCInstallationNodeService implements SwingInstallationNodeService<CNCInstallationNodeContribution, CNCInstallationNodeView>{

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle(Locale locale) {
		return "CNC";
	}

	@Override
	public CNCInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new CNCInstallationNodeView();
	}

	@Override
	public CNCInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider,
			CNCInstallationNodeView view, DataModel model, CreationContext context) {
		return new CNCInstallationNodeContribution(apiProvider,model,view);
	}

}
