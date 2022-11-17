package machines;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.rootpane.WebFrame;

import forms._infoMachinePanel;
import jade.gui.GuiEvent;
import manager.AM;
import ontology.MaintMsgPanel;
import ontology.UIFont;


public class MachineGUI extends WebFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	//private MA ma;

	private static Dimension windowSize;
	private int screenWidth;
	private int screenHeight;

	private JLayeredPane layeredPane;
	private MaintMsgPanel maintMsgPanel;
	private JSplitPane splitPane;
	private JScrollPane scrollerPane;
	
	private JPanel controleMachinePanel;
	private JPanel affichageMachinePanel;
	private BufferedImage machineIcon;
	
	private JPanel controleButtonsPanel;
	public _infoMachinePanel currentStatutPanel;
	
	private JButton panneMachineButton;
	private JButton repaireMachineButton;	
	private BufferedImage repaireIcon;
		
	private JLabel machineIconLabel;
	private JLabel machineStatusLabel;

	private AR machineAgent;

	public static Color disponibleColor = Color.BLACK;
	public static Color processusColor =  new Color(46, 139, 87);
	public static Color panneColor = Color.RED;
	public static Color maintenanceColor = Color.yellow;

	
	/*************************************************************************************************
	 * Launch the application.
	 *************************************************************************************************/
     public static void main(String[] args) {
    	 UIFont.loadFont();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MachineGUI();	
			}
		});
	}

    
 	// Constructeur: créer l'interface graphique.
 	/*public MachineGUI(AR ar) {
 		this.ma = ar;		
 		
 		// Ajouter les widgets au JFrame
		initComponents();
		showGui();	
 	}*/
	
  	public MachineGUI(AR machineAgent) {
 		this.machineAgent = machineAgent;		
 		
 		// Ajouter les widgets au JFrame
		initComponents();
		showGui();	
 	}
	
	public MachineGUI() {
		initComponents();
		showGui();	
	}
	
	private void initComponents() {
		
		layeredPane = new JLayeredPane();
		maintMsgPanel = new MaintMsgPanel();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		windowSize = new Dimension(screenWidth/4,screenHeight-200);

		this.controleMachinePanel = new JPanel(new VerticalFlowLayout());
		this.affichageMachinePanel = new JPanel(new GridBagLayout());		
		this.currentStatutPanel = new _infoMachinePanel();
		this.machineStatusLabel = new JLabel();
		
		controleButtonsPanel = new JPanel(new GridBagLayout()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				GradientPaint paint = new GradientPaint(0.0f, 0.0f, new Color(0xF2F2F2), 0.0f, getHeight(), new Color(0xD7D7D7));
				g2.setPaint(paint);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.drawLine(0, getHeight(), getWidth(), getHeight());
			}
		};
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;		
		
		constraints.gridx = 0;
		constraints.gridy = 0;		
		panneMachineButton = new JButton("En Panne");
		controleButtonsPanel.add(panneMachineButton,constraints);
		panneMachineButton.addActionListener(this);
		
		repaireMachineButton = new JButton("Répairer");
		repaireMachineButton.setIcon(new ImageIcon("resources/repair_64.png"));
		repaireMachineButton.setOpaque(false);
		repaireMachineButton.setFocusPainted(false);
		repaireMachineButton.setContentAreaFilled(false);
		repaireMachineButton.setBorderPainted(false);
		constraints.gridx = 1;
		controleButtonsPanel.add(repaireMachineButton,constraints);
		repaireMachineButton.setEnabled(false);		
		repaireMachineButton.addActionListener(this);
		
		constraints.gridy = 1;
		constraints.gridx = 0;
		controleButtonsPanel.add(currentStatutPanel,constraints);
		
		//constraints.gridx = 0;
		//constraints.gridx = 1;
		
		//machineIcon = ImageIO.read(new File("resources/machineBigIcon.png"));
		machineIconLabel = new JLabel(new ImageIcon("resources/machineBigIcon.png"));
		machineIconLabel.setVerticalAlignment(SwingConstants.CENTER);
		machineIconLabel.setHorizontalAlignment(SwingConstants.LEFT);

		machineStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		machineStatusLabel.setFont(UIFont.headings);
		
		GridBagConstraints statusConstraints = new GridBagConstraints();
		statusConstraints.fill = GridBagConstraints.HORIZONTAL;
		statusConstraints.gridx = 0;
		statusConstraints.gridy = 0;
		
		GridBagConstraints iconConstraints = new GridBagConstraints();
		iconConstraints.fill = GridBagConstraints.HORIZONTAL;
		iconConstraints.gridx = 0;
		iconConstraints.gridy = 1;
		
		affichageMachinePanel.add(machineStatusLabel, statusConstraints);
		affichageMachinePanel.add(machineIconLabel, iconConstraints);
		controleMachinePanel.setBorder((new EmptyBorder(5,5,5,5) ));

		controleMachinePanel.add(controleButtonsPanel);
		controleMachinePanel.add(Box.createRigidArea(new Dimension(controleMachinePanel.getWidth(),50)) );

		controleMachinePanel.add(new JScrollPane(currentStatutPanel));
		controleMachinePanel.add(Box.createRigidArea(new Dimension(controleMachinePanel.getWidth(),50)) );

		controleMachinePanel.add(affichageMachinePanel);
		controleMachinePanel.add(Box.createRigidArea(new Dimension(controleMachinePanel.getWidth(),50)) );
		
		//this.scrollerPane = new JScrollPane();//queuePanel);

		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(controleMachinePanel), scrollerPane);
		this.splitPane.setDividerLocation(0.30);
		//machineIdle();

		splitPane.setBounds(0, 0,(int) windowSize.getWidth(),(int) windowSize.getHeight()-30);
		layeredPane.add(splitPane, new Integer(0), 0);

		maintMsgPanel.setOpaque(false);
		maintMsgPanel.setVisible(false);
		maintMsgPanel.setBounds(0, 0,(int) windowSize.getWidth()-25,(int) windowSize.getHeight()-50);
		layeredPane.add(maintMsgPanel, new Integer(1), 1);

		add(layeredPane);
	}
	
	
	

	
	private void showGui() {
		setTitle(" Machine GUI : ");				
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\machine-48.png"));		

		setPreferredSize(windowSize);		
		this.setResizable(false);
		pack();
		//setLocation(0,0);
		this.setLocationRelativeTo(null);
		super.setVisible(true);
	}
	
	/**
	 * Runs on EDT
	 */
	public void machineDisponible() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				machineStatusLabel.setText("Disponible");
				machineStatusLabel.setForeground(disponibleColor);
			}
		});
	}
	

	/**
	 * Runs on EDT
	 */
	public void machineProcessing() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				machineStatusLabel.setText("Exécution");
				machineStatusLabel.setForeground(processusColor);
			}
		});
	}
	
	/**
	 * Runs on EDT
	 */
	public void machineFailed() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				machineStatusLabel.setText("en Panne");
				machineStatusLabel.setForeground(panneColor);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.getSource().equals(panneMachineButton)) {
			/*if(machineSimulator != null) {
			 log.info("unload : " + machineSimulator.isUnloadFlag());
			 // if the machine isn't loaded with some job, it can't be marked as failed
			  * if(machineSimulator.getCurrentJob() == null) {
					JOptionPane.showMessageDialog(MachineGUI.this, "Machine cannot fail while idle", "Dialog", JOptionPane.ERROR_MESSAGE);
				} else {*/
					machineFailed();
					repaireMachineButton.setEnabled(true);
					panneMachineButton.setEnabled(false);
					new Thread(new Runnable() {
						@Override
						public void run() {
							//machineSimulator.setStatus(MachineStatus.FAILED);
						}
					}).start();
		}
		
		if(arg0.getSource().equals(repaireMachineButton)) {
			/*if(machineSimulator != null) {
			 log.info("unload : " + machineSimulator.isUnloadFlag());
			 // if the machine isn't loaded with some job, it can't be marked as failed
			  * if(machineSimulator.getCurrentJob() == null) {
					JOptionPane.showMessageDialog(MachineGUI.this, "Machine cannot fail while idle", "Dialog", JOptionPane.ERROR_MESSAGE);
				} else {*/
					machineDisponible();
					repaireMachineButton.setEnabled(false);
					panneMachineButton.setEnabled(true);
					new Thread(new Runnable() {
						@Override
						public void run() {
							//machineSimulator.setStatus(MachineStatus.FAILED);
						}
					}).start();
		}

	} 


}

