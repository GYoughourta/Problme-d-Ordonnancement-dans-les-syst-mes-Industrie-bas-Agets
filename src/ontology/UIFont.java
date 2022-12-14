package ontology;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.alee.laf.WebLookAndFeel;

public class UIFont {
	public static Font font;
	public static Font defaultFont;
	public static Font headings;

	public static void setUIFont (FontUIResource f){
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put (key, f);
		}
	} 

	public static void loadFont() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				//					File font_file = new File("resources/Inconsolata.otf");
				//					Font iconSolataFont = Font.createFont(Font.TRUETYPE_FONT, font_file);
				//					ge.registerFont(iconSolataFont);
				defaultFont = new Font("Sans Serif", Font.PLAIN, 12);				
				font = defaultFont.deriveFont(Font.PLAIN, 14f);
				headings = defaultFont.deriveFont(Font.BOLD, 16f);
				
				WebLookAndFeel.install ();
				UIFont.setUIFont (new FontUIResource(font)); 
				//					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				//						if ("Nimbus".equals(info.getName())) {
				//							UIManager.setLookAndFeel(info.getClassName());
				//							break;
				//						}
				//					}
			}
		} );
	}

	public static boolean checkIfExists(JComponent parent,JComponent child) {

		if(child.getParent() == null)
			return false;

		return child.getParent().equals(parent);
	}

	public static void setColumnWidths(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();

		for (int col = 0; col < table.getColumnCount(); col++) {
			int maxWidth = 370/table.getColumnCount();

			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer rend = table.getCellRenderer(row, col);
				Object value = table.getValueAt(row, col);
				Component comp = rend.getTableCellRendererComponent(table, value, false, false, row, col);
				maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
			}
			TableColumn column = columnModel.getColumn(col);
			TableCellRenderer headerRenderer = column.getHeaderRenderer();
			if (headerRenderer == null) {
				headerRenderer = table.getTableHeader().getDefaultRenderer();
			}
			Object headerValue = column.getHeaderValue();
			Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, col);
			maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width);
			// note some extra padding
			//IntercellSpacing * 2 + 2 * 2 pixel instead of taking this value from Borders
			column.setPreferredWidth(maxWidth + 6);
		}

		table.setPreferredScrollableViewportSize(table.getPreferredSize());
	}
}
