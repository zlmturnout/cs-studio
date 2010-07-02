package org.csstudio.opibuilder.widgets.editparts;


import org.csstudio.opibuilder.model.AbstractWidgetModel;
import org.csstudio.opibuilder.widgets.figures.LabelFigure;
import org.csstudio.platform.ui.util.CustomMediaFactory;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.CellEditorActionHandler;


/**The manager help to managing the label direct editing.
 * @author Xihui Chen
 *
 */
public class LabelEditManager extends DirectEditManager
{

private IActionBars actionBars;
private CellEditorActionHandler actionHandler;
private IAction copy, cut, paste, undo, redo, find, selectAll, delete;
private double cachedZoom = -1.0;
private Font scaledFont;
private boolean multiLine = true;
private ZoomListener zoomListener = new ZoomListener() {
	public void zoomChanged(double newZoom) {
		updateScaledFont(newZoom);
	}
};
public LabelEditManager(GraphicalEditPart source, CellEditorLocator locator, boolean multiline) {
	super(source, null, locator);
	this.multiLine = multiline;
}

public LabelEditManager(GraphicalEditPart source, CellEditorLocator locator) {
	super(source, null, locator);
}

/**
 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
 */
protected void bringDown() {
	ZoomManager zoomMgr = (ZoomManager)getEditPart().getViewer()
			.getProperty(ZoomManager.class.toString());
	if (zoomMgr != null)
		zoomMgr.removeZoomListener(zoomListener);

	if (actionHandler != null) {
		actionHandler.dispose();
		actionHandler = null;
	}
	if (actionBars != null) {
		restoreSavedActions(actionBars);
		actionBars.updateActionBars();
		actionBars = null;
	}
	
	super.bringDown();
	// dispose any scaled fonts that might have been created
	disposeScaledFont();
}

protected CellEditor createCellEditorOn(Composite composite) {
	return new TextCellEditor(composite, (multiLine ? SWT.MULTI : SWT.SINGLE) | SWT.WRAP);
}

private void disposeScaledFont() {
	if (scaledFont != null) {
		scaledFont.dispose();
		scaledFont = null;
	}
}

protected void initCellEditor() {
	// update text
	LabelFigure label = (LabelFigure) getEditPart().getAdapter(LabelFigure.class);
	AbstractWidgetModel labelModel = (AbstractWidgetModel) getEditPart().getModel();
	getCellEditor().setValue(label.getText());
	if(label.isOpaque()){
		getCellEditor().getControl().setBackground(
			CustomMediaFactory.getInstance().getColor(
					labelModel.getBackgroundColor()));	
	}
	getCellEditor().getControl().setForeground(
		CustomMediaFactory.getInstance().getColor(labelModel.getForegroundColor()));
	// update font
	ZoomManager zoomMgr = (ZoomManager)getEditPart().getViewer()
			.getProperty(ZoomManager.class.toString());
	if (zoomMgr != null) {
		// this will force the font to be set
		cachedZoom = -1.0;
		updateScaledFont(zoomMgr.getZoom());
		zoomMgr.addZoomListener(zoomListener);
	} else
		getCellEditor().getControl().setFont(label.getFont());

	// Hook the cell editor's copy/paste actions to the actionBars so that they can
	// be invoked via keyboard shortcuts.
	actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			.getActiveEditor().getEditorSite().getActionBars();
	saveCurrentActions(actionBars);
	actionHandler = new CellEditorActionHandler(actionBars);
	actionHandler.addCellEditor(getCellEditor());
	actionBars.updateActionBars();
}

private void restoreSavedActions(IActionBars actionBars){
	actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
	actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), paste);
	actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), delete);
	actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAll);
	actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cut);
	actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), find);
	actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undo);
	actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
}

private void saveCurrentActions(IActionBars actionBars) {
	copy = actionBars.getGlobalActionHandler(ActionFactory.COPY.getId());
	paste = actionBars.getGlobalActionHandler(ActionFactory.PASTE.getId());
	delete = actionBars.getGlobalActionHandler(ActionFactory.DELETE.getId());
	selectAll = actionBars.getGlobalActionHandler(ActionFactory.SELECT_ALL.getId());
	cut = actionBars.getGlobalActionHandler(ActionFactory.CUT.getId());
	find = actionBars.getGlobalActionHandler(ActionFactory.FIND.getId());
	undo = actionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
	redo = actionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
}

private void updateScaledFont(double zoom) {
	if (cachedZoom == zoom)
		return;
	
	Text text = (Text)getCellEditor().getControl();
	Font font = getEditPart().getFigure().getFont();
	
	disposeScaledFont();
	cachedZoom = zoom;
	if (zoom == 1.0)
		text.setFont(font);
	else {
		FontData fd = font.getFontData()[0];
		fd.setHeight((int)(fd.getHeight() * zoom));
		text.setFont(scaledFont = new Font(null, fd));
	}
}

}