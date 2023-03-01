package setup.gui.control;

import java.util.Map;

import setup.gui.model.SetupEditorModel;
import setup.gui.model.SetupEditorModel.EntitySeparator;

public class SetupEditorController {
	SetupEditorModel model;
	public SetupEditorController() {
		model = SetupEditorModel.emptyModel();
	}
	public Map<Class<?>, EntitySeparator> getSeparators() {
		return model.getSeparators();
	}
}
