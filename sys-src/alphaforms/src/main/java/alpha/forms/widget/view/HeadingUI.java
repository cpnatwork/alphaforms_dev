package alpha.forms.widget.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import alpha.forms.widget.model.Heading;

public class HeadingUI extends FormWidgetUI {

	private static final long serialVersionUID = 1L;
	
	private Heading model;
	
	private JLabel label;
	
	public HeadingUI(Heading model) {
		super(model);
		this.model = model;
		compose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#compose()
	 */
	@Override
	protected void compose() {
		label = new JLabel(model.getText());
		label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		Font font = label.getFont().deriveFont(Font.BOLD, 16);
		label.setFont(font);
		this.add(label);
		doLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.widget.view.FormWidgetUI#doLayout()
	 */
	@Override
	public void doLayout() {
		super.setBounds(model.getX(), model.getY(), model.getWidth(),
				model.getHeight());
		super.doLayout();

		
		
		label.setSize(model.getSize());
		label.setLocation(0, 0);
		label.setText(model.getText());
	}	

}
