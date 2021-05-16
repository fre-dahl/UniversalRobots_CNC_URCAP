package com.seal.student.thenewapp.impl;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

public class CNCInstallationNodeView implements SwingInstallationNodeView<CNCInstallationNodeContribution>{
	
	
	private JCheckBox checkBox;

	@Override
	public void buildUI(JPanel panel, CNCInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createInfo());
		panel.add(Style.createVerticalSpace(20));
		panel.add(createCheckBox(contribution));
		
	}
	
	public void setEnableCheckBox(boolean checked) {
		checkBox.setSelected(checked);
	}
	
	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JTextPane pane = new JTextPane();
		pane.setBorder(BorderFactory.createEmptyBorder());
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attributeSet, 0.5f);
		StyleConstants.setLeftIndent(attributeSet, 0f);
		pane.setParagraphAttributes(attributeSet, false);
		pane.setText(getDescription());
		pane.setEditable(false);
		pane.setMaximumSize(pane.getPreferredSize());
		pane.setBackground(infoBox.getBackground());
		infoBox.add(pane);
		return infoBox;
	}
	
	private Box createCheckBox(CNCInstallationNodeContribution contribution) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		checkBox = new JCheckBox("Enable");
		checkBox.addItemListener(contribution.getListenerForCheckBox());
		box.add(checkBox);
		return box;
	}
	
	private String getDescription() {
		return "Denne Installation noden inneholder CNC funksjonalitet som kan benyttes under Robot programmering.\n"
			+ "RSC polling blir kjørt som en bakgrunnsprosess, og man får tilgang til CNC relaterte funksjoner.";
	}

}
