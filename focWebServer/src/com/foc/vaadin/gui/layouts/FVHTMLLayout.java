package com.foc.vaadin.gui.layouts;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;

import org.xml.sax.Attributes;

import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.foc.Globals;
import com.foc.dataDictionary.FocDataDictionary;
import com.foc.list.FocList;
import com.foc.shared.dataStore.IFocData;
import com.foc.vaadin.FocCentralPanel;
import com.foc.vaadin.gui.FocXMLGuiComponentDelegate;
import com.foc.vaadin.gui.FocXMLGuiComponentStatic;
import com.foc.vaadin.gui.XMLBuilder;
import com.foc.vaadin.gui.xmlForm.FXML;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FVHTMLLayout extends CustomComponent implements FVLayout {

	private VerticalLayout rootVerticalLayout = null;
	private CustomLayout customLayout = null;
	private String name;
	private String type;
	private Attributes attributes = null;
	private FocXMLGuiComponentDelegate delegate = null;
	private FocXMLLayout focXMLLayout = null;
	
	public FVHTMLLayout(Attributes attributes) {
		setAttributes(attributes);
		init();
	}

	public void init() {
		rootVerticalLayout = new VerticalLayout();
		setCompositionRoot(rootVerticalLayout);
		delegate = new FocXMLGuiComponentDelegate(this);

		addStyleName(FVLayout.DEFAULT_STYLE);
	}

	@Override
	public void dispose() {
		name = null;
		type = null;
		attributes = null;
		delegate = null;
		focXMLLayout = null;
		customLayout = null;
		rootVerticalLayout = null;
	}
	
	private String resolveExpression(String value){
		if(getFocXMLLayout() != null){
			FocDataDictionary localDataDictionary = getFocXMLLayout().getFocDataDictionary(false);
			if(localDataDictionary != null){
				value = localDataDictionary.resolveExpression(getFocXMLLayout().getFocData(), value, false);
			}
			value = FocDataDictionary.getInstance().resolveExpression(getFocXMLLayout().getFocData(), value, true);
		}
		return value;
	}

	public void parseAndBuildHtml(String html) {
		BufferedReader bufferedReader = new BufferedReader(new StringReader(html));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (line != null && !line.isEmpty()) {
					if (line.contains("$")) {
						String value = resolveExpression(line);
						line = line.replace(line, value);
					}
					stringBuilder.append(line);
				}
			}
		} catch (IOException e1) {
			Globals.logException(e1);
		}

		InputStream inputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes());
		try {
			customLayout = new CustomLayout(inputStream);
		} catch (IOException e) {
			Globals.logException(e);
		}
		addComponent(customLayout);
		stringBuilder  = null;
		bufferedReader = null;
	}

	public void addStyleName(String style) {
		super.addStyleName(style);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void addComponent(Component comp, Attributes attributes) {
		String idx = attributes != null ? attributes.getValue(FXML.ATT_IDX) : null;

		int idxInt = -1;
		try {
			idxInt = idx != null ? Integer.parseInt(idx) + 1 : -1;
		} catch (Exception e) {
		}

		if (idxInt >= getComponentCount())
			idxInt = -1;

		if (idxInt < 0) {
			addComponent(comp);
		}
	}

	@Override
	public void setDragDrop(boolean state) {
	}

	@Override
	public boolean isDragDrop() {
		return false;
	}

	@Override
	public ComponentPosition getPosition(Component comp) {
		return null;
	}

	@Override
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
		FocXMLGuiComponentStatic.applyAttributes(this, attributes);
		if (attributes != null) {

			if (attributes != null && attributes.getValue(FXML.ATT_IMAGE_DIR) != null) {
				FocXMLGuiComponentStatic.applyLayoutBackgroundImageAttributes(this, attributes);
			}
		}
	}

	@Override
	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public String getXMLType() {
		return FXML.TAG_HTML_LAYOUT;
	}

	@Override
	public boolean isXMLLeaf() {
		return false;
	}

	@Override
	public void fillXMLNodeContent(XMLBuilder builder) {
	}

	@Override
	public IFocData getFocData() {
		return null;
	}

	@Override
	public Field getFormField() {
		return null;
	}

	@Override
	public boolean copyGuiToMemory() {
		return false;
	}

	@Override
	public void copyMemoryToGui() {
	}

	@Override
	public void setDelegate(FocXMLGuiComponentDelegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public FocXMLGuiComponentDelegate getDelegate() {
		return delegate;
	}

	@Override
	public void setFocData(IFocData focData) {
	}

	@Override
	public String getValueString() {
		return null;
	}

	@Override
	public void setValueString(String value) {
	}

	@Override
	public FocCentralPanel getWindow() {
		return findAncestor(FocCentralPanel.class);
	}

	@Override
	public void refreshEditable() {
	}

	@Override
	public void addComponent(Component comp) {
		Component component = getCompositionRoot();
		if (component instanceof VerticalLayout) {
			VerticalLayout verticalLayout = (VerticalLayout) component;
			verticalLayout.addComponent(comp);
		}
	}

	@Override
	public Iterator<Component> getComponentIterator() {
		VerticalLayout verticalLayout = null;
		Component component = getCompositionRoot();
		if (component instanceof VerticalLayout) {
			verticalLayout = (VerticalLayout) component;
		}
		return verticalLayout != null ? verticalLayout.iterator() : null;
	}

	public FocXMLLayout getFocXMLLayout() {
		return focXMLLayout;
	}

	public void setFocXMLLayout(FocXMLLayout focXMLLayout) {
		this.focXMLLayout = focXMLLayout;
	}
}
