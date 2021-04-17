package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import algoritmoGenetico.AlgoritmoGenetico;
import controller.Controller;

public class ControlPanel extends JPanel {//implements ItemListener{
	
	private static final long serialVersionUID = 1L;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.black, 2);	

	private Controller _ctrl;
	private JButton run;
	private JComboBox<String> Seleccion, Cruce,Mutacion;
	//private JCheckBox Elitismo;
	private JSpinner poblacion, maxGeneracion, mutacion,elitismo,precision,probCruce;//NGenotipos;
	
	private static final Double[] Precision={ 0.1, 0.01, 0.001, 0.0001, 0.00001};
	private String[] seleccion= { "Ruleta","Estocastico","Torneo Probabilistico", "Torneo Deterministico", "Truncamiento", "Restos","Ranking"};
	private String[] cruce= {"PMX","OX","OXPP","CX","ERX","CO"};
	private String[] mutac= {"Inversion","Intercambio","Insercion","Heuristica"};
	
	private Map<String, String> datos;
	
	public ControlPanel(Controller c) {
		_ctrl=c;

		InitGui();
		createControl();
	}
		
	private void InitGui() {

		poblacion = new JSpinner(new SpinnerNumberModel(100,15,500,1));
		poblacion.setPreferredSize(new Dimension(75,30));
		maxGeneracion = new JSpinner(new SpinnerNumberModel(100,10,1000,10));
		maxGeneracion.setPreferredSize(new Dimension(75,30));
		mutacion = new JSpinner(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.01));
		mutacion.setPreferredSize(new Dimension(75,30));
		elitismo = new JSpinner(new SpinnerNumberModel(0.03, 0.0, 0.5, 0.01));
		elitismo.setPreferredSize(new Dimension(75,30));
		//torneo = new JSpinner(new SpinnerNumberModel(5, 2, 10, 1));
		//torneo.setPreferredSize(new Dimension(65,25));
		probCruce= new JSpinner(new SpinnerNumberModel(0.6, 0.0, 1.0, 0.05));
		probCruce.setPreferredSize(new Dimension(75,30));
		precision = new JSpinner(new SpinnerListModel(Precision));
		precision.setValue(0.001);
		precision.setPreferredSize(new Dimension(75,30));
		Seleccion = ComboBox(seleccion);
		Cruce= ComboBox(cruce);
		Mutacion=ComboBox(mutac);
		
		//Individuo= DComboBox(loadData()); //CAMBIADO
		//loadData();
		AlgoritmoGenetico.loadDataFile(loadData());
		//AlgoritmoGenetico.loadDataFile(datos.get(Individuo.getSelectedItem().toString()));
		
		//Elitismo = new JCheckBox();
		//Elitismo.setSelected(_ctrl.getElitism());
		run= new JButton("Run");
		run.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
		    	carga();
				_ctrl.reset();
				_ctrl.run();
				run.setEnabled(false);
				run_sim((int) maxGeneracion.getValue());
		    }
			}); 
		run.setPreferredSize(new Dimension(75,30));		
	}
	
	private void run_sim(int n) {
		if (n > 0 ){
			try {
				Thread.sleep(50);
				_ctrl.run_sim(); 
			} catch (Exception e) {
				System.out.print(e);
				return;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		}
		else
			run.setEnabled(true);
	}
	
	private void createControl() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder(_defaultBorder, "Controles", TitledBorder.LEFT, TitledBorder.TOP));
		//add(estructura("Individuo", Individuo));
		
		add(estructura("Poblacion", poblacion));
		add(estructura("Generaciones", maxGeneracion));
		//add(estructura("Elitismo", Elitismo));
		add(estructura("Elitismo", elitismo));
		add(estructura("Seleccion", Seleccion));
		add(estructura("Cruce", Cruce));
		add(estructura("Mutacion", Mutacion));
		add(estructura("Prob. Cruce", probCruce));
		add(estructura("Prob. Mutacion", mutacion));
		add(estructura("Valor de Error", precision));	
		//add(estructura2(estructura1("Poblacion", poblacion),estructura1("Generaciones:", maxGeneracion)));
		//add(estructura2(estructura1("Elitismo", Elitismo), estructura1("Rango", elitismo)));
		//add(estructura2(estructura1("Selection", Seleccion), estructura1("Tamaño Torneo", torneo)));
		//add(estructura1("Selection", Seleccion));
		//add(estructura2(estructura1("Cruce", Cruce), estructura1("Probabilidad", probCruce)));
		//add(estructura2(estructura1("Mutacion", mutacion), estructura1("Valor de Error", precision)));	
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));			
		buttons.add(run);
		add(buttons);
		this.setPreferredSize(new Dimension(280, 450));
		setVisible(true);
	}
	
	private JComboBox<String> ComboBox(String[] a) {
		JComboBox<String> s = new JComboBox<String>();
		for (int i = 0; i < a.length;i++) s.addItem(a[i].toString());
		s.setPreferredSize(new Dimension(125,30));
		return s;
	}
	
	/*private JComboBox<String> CComboBox() {
		JComboBox<String> c = new JComboBox<String>();
		for (int i = 0; i < cruce.length;i++) c.addItem(cruce[i].toString());
		c.setPreferredSize(new Dimension(100,25));
		return c;
	}*/
	
	/*private JComboBox<String> IComboBox() {
		JComboBox<String> c = new JComboBox<String>();
		for (int i = 0; i < indis.length;i++) c.addItem(indis[i].toString());
		c.setPreferredSize(new Dimension(100,25));
		return c;
	}*/
	
	/*private JComboBox<String> DComboBox(Object[] options) {
		JComboBox<String> c = new JComboBox<String>();
		for (int i = 0; i < options.length;i++) c.addItem(options[i].toString());
		c.setPreferredSize(new Dimension(100,25));
		return c;
	}*/
	
	private JPanel estructura(String a, JComponent c) { 
		JPanel p = new JPanel(new GridLayout(1,2));
		JPanel up = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel l= new JLabel(a);
		l.setFont(new Font("Comic Sans MS", Font.PLAIN, 17));
		up.add(l);
		JPanel down = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		down.add(c);
		p.add(up);
		p.add(down);
		return p;
	}
	
	/*private JPanel estructura1(String a, JComponent c) { //REVISAR PARA PODER HACERLO DE OTRA FORMA
		JPanel p = new JPanel(new GridLayout(2,1));
		JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER));
		up.add(new JLabel(a));
		JPanel down = new JPanel(new FlowLayout(FlowLayout.CENTER));
		down.add(c);
		p.add(up);
		p.add(down);
		return p;
	}
	
	private JPanel estructura2(JPanel a, JPanel b) { 
		JPanel r = new JPanel(new GridLayout(1,2));
		r.add(a);
		r.add(b);
		return r;
	}*/
	
	private void carga() {
		//_ctrl.setIndi(Individuo.getSelectedIndex());
		_ctrl.setPob((int) poblacion.getValue());
		_ctrl.setMaxGen((int) maxGeneracion.getValue());		
		//_ctrl.setElitism(Elitismo.isSelected());
		_ctrl.setElitismRango((double) elitismo.getValue());
		_ctrl.setSelection(Seleccion.getSelectedIndex());//getSelectedItem().toString());
		//_ctrl.setTamTorneo((int) torneo.getValue());
		_ctrl.setCruce(Cruce.getSelectedIndex()); //Devolver String o entero, comprobar	
		_ctrl.setMutac(Mutacion.getSelectedIndex());
		_ctrl.setProbCruce((double) probCruce.getValue());
		_ctrl.setProbMut((double) mutacion.getValue());
		//_ctrl.setPrecision((double) precision.getValue());
	}
	
	private Object[] loadData() {
		File f = new File("resources/ngrams/");
		datos = new HashMap<String, String>();
		for (File fil : f.listFiles()) {
			if (fil.getName().substring(fil.getName().lastIndexOf('.'), fil.getName().length()).equalsIgnoreCase(".txt")) {
				datos.put(fil.getName(), fil.getAbsolutePath());
			}
		}
		Object[] list = datos.values().toArray();//keySet().toArray();
		Arrays.sort(list);
		return list;
	}

	/*@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == Individuo) 
			AlgoritmoGenetico.loadDataFile(datos.get(Individuo.getSelectedItem().toString()));
	}*/
}
