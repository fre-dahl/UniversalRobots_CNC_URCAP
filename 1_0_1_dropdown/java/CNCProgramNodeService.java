package com.seal.student.thenewapp.impl;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

public class CNCProgramNodeService implements SwingProgramNodeService<CNCProgramNodeContribution, CNCProgramNodeView>{

	@Override
	public String getId() {
		return "CNCSwingProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
		
	}

	@Override
	public String getTitle(Locale locale) {
		return "CNC";
	}

	@Override
	public CNCProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new CNCProgramNodeView(apiProvider);
	}

	@Override
	public CNCProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider, 
			CNCProgramNodeView view, 
			DataModel model, 
			CreationContext context) {
		return new CNCProgramNodeContribution(apiProvider, view, model);
	}

}
