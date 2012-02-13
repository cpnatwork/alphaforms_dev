package alpha.forms.widget.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alpha.forms.form.event.EventMemento;
import alpha.forms.memento.model.DynamicAttributeMemento;
import alpha.forms.memento.model.MementoOriginator;
import alpha.forms.memento.model.ValueMemento;
import alpha.forms.memento.model.WidgetMemento;
import alpha.forms.propertyEditor.model.annotation.WidgetProperty;
import alpha.forms.widget.view.HeadingUI;

public class Heading extends FormWidget implements MementoOriginator {

	@WidgetProperty(description = "This text will show as headline.")
	protected String text;
	
	public Heading(String name) {
		super(name);
		width = 130;
		height = 30;
		text = name;
		ui = new HeadingUI(this);	}


	
	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	@Override
	public WidgetMemento createWidgetMemento() {
		WidgetMemento m = new WidgetMemento();
		m.setName(this.name);
		m.setType(this.getClass());
		m.setValue("");
		m.addAttribute("text", text);
		m.addAttribute("x", x);
		m.addAttribute("y", y);
		m.addAttribute("width", width);
		m.addAttribute("height", height);
		m.addAttribute("visible", visible);
		m.addAttribute("ui", ui.getClass().getName());
		m.setValidators(validators.createMemento());
		List<EventMemento> events = new ArrayList<EventMemento>();
		m.setEvents(events);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setWidgetMemento(alpha.forms
	 * .memento.model.WidgetMemento)
	 */
	@Override
	public void setWidgetMemento(WidgetMemento m) {
		if (m != null) {
			name = m.getName();
			Map<String, Object> attributes = m.getAttributes();
			text = attributes.get("text").toString();
			x = Integer.parseInt(attributes.get("x").toString());
			y = Integer.parseInt(attributes.get("y").toString());
			width = Integer.parseInt(attributes.get("width").toString());
			height = Integer.parseInt(attributes.get("height").toString());
			visible = Boolean
					.parseBoolean(attributes.get("visible").toString());
			setSize(width, height);
			setX(x);
			setY(y);
			validators.setMemento(m.getValidators());
			for (EventMemento em : m.getEvents()) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#createDynamicAttributeMemento
	 * (alpha.forms.memento.model.WidgetMemento)
	 */
	@Override
	public DynamicAttributeMemento createDynamicAttributeMemento(
			WidgetMemento ref) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setDynamicMemento(alpha.forms
	 * .memento.model.DynamicAttributeMemento)
	 */
	@Override
	public void setDynamicMemento(DynamicAttributeMemento m) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see alpha.forms.memento.model.MementoOriginator#createValueMemento()
	 */
	@Override
	public ValueMemento createValueMemento() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * alpha.forms.memento.model.MementoOriginator#setValueMemento(alpha.forms
	 * .memento.model.ValueMemento)
	 */
	@Override
	public void setValueMemento(ValueMemento m) {
	}	
	
}
