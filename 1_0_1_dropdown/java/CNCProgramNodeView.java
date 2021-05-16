package com.seal.student.thenewapp.impl;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class CNCProgramNodeView implements SwingProgramNodeView<CNCProgramNodeContribution>{

	private final ViewAPIProvider apiProvider;
	private JComboBox<String> programComboBox = new JComboBox<String>();
	private JLabel installationStatus = new JLabel();
	
	public CNCProgramNodeView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}
	
	@Override
	public void buildUI(JPanel panel, ContributionProvider<CNCProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createInstallationStatusIndicator(installationStatus));
		panel.add(Style.createVerticalSpace(Style.PROGRAM_DEFAULT_VERTICAL_SPACE));
		panel.add(createDescription("Velg Program:"));
		panel.add(createProgramComboBox(programComboBox, provider));
		panel.add(Style.createVerticalSpace(Style.PROGRAM_DEFAULT_VERTICAL_SPACE));
		
	}
	
	public void setCheckBoxEnabled(String status) {
		installationStatus.setText("(" + status + ")");
	}
	
	public void setProgramComboBoxItems(String[] items) {
		programComboBox.removeAllItems();
		programComboBox.setModel(new DefaultComboBoxModel<String>(items));
	}
	
	public void setProgramComboBoxSelection(String item) {
		programComboBox.setSelectedItem(item);
	}

	private Box createDescription(String desc) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel label = new JLabel(desc);
		box.add(label);
		return box;
	}
	
	private Box createInstallationStatusIndicator(JLabel label) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.add(label);
		return box;
	}
	
	private Box createProgramComboBox(final JComboBox<String> comboBox,
			final ContributionProvider<CNCProgramNodeContribution> provider) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		comboBox.setPreferredSize(Style.PROGRAM_COMBOBOX_SIZE);
		comboBox.setMaximumSize(comboBox.getPreferredSize());
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					provider.get().onFunctionSelection((String) e.getItem());
				}
			}
		});
		box.add(comboBox);
		return box;
	}
}
