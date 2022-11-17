package forms;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.alee.extended.label.WebHotkeyLabel;

import net.miginfocom.swing.MigLayout;

public class _infoMachinePanel extends JPanel {

	private static final long serialVersionUID = 1L;	
	
	private JLabel jobLabel;
	private JLabel tacheLabel;
	private JLabel dureeLabel;
	
	private WebHotkeyLabel jobValeurLabel;
	private WebHotkeyLabel tacheValeurLabel; 
	private WebHotkeyLabel dureeValeurLabel;

	public _infoMachinePanel() {

		setLayout(new MigLayout("","[]10",""));		

		jobLabel = new JLabel("Nom Job : ");
		tacheLabel = new JLabel("Tache : ");
		dureeLabel = new JLabel("Durée : ");

		jobValeurLabel = new WebHotkeyLabel();
		tacheValeurLabel = new WebHotkeyLabel();
		dureeValeurLabel = new WebHotkeyLabel();

		add(jobLabel);
		add(jobValeurLabel, "wrap");


		add(tacheLabel);
		add(tacheValeurLabel,"wrap");
		
		add(dureeLabel);
		add(dureeValeurLabel,"wrap");

		setBackground(Color.WHITE);
		Component[] comps = getComponents();
		for(int i = 0; i < comps.length; i++) {
			comps[i].setForeground(Color.BLACK);
		}
	}

	public void setJob(final String job){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jobValeurLabel.setText(job); 
			}
		});
	}
	

	
	public void setTache(final String op) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tacheValeurLabel.setText(op);
			}
		});
	}
	
	public void setDuree(final int duree) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dureeValeurLabel.setText(duree+"");
			}
		});
	}
	

	/**
	 * Runs on EDT
	 */
	public void reset() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jobValeurLabel.setText("");
				tacheValeurLabel.setText("");
				dureeValeurLabel.setText("");
			}
		});
	}

}
