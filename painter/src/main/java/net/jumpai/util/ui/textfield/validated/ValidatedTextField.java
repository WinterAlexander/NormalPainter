package net.jumpai.util.ui.textfield.validated;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import net.jumpai.util.ui.drawable.NullDrawable;
import net.jumpai.util.ui.listener.ChangeAdapter;
import net.jumpai.util.ui.textfield.TextFieldIcon;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.hide;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.show;
import static net.jumpai.util.ObjectUtil.coalesce;
import static net.jumpai.util.Validation.ensureNotNull;

/**
 * {@link TextField} that validates its input by displaying an icon and a popup
 * error message upon entering invalid input
 * <p>
 * Created on 2019-05-29.
 *
 * @author Alexander Winter
 */
public class ValidatedTextField extends TextFieldIcon
{
	private final ValidatedTextFieldStyle style;
	private final TextFieldValidator validator;

	private String prevTextFieldContent = "";

	private final Table errorPopup = new Table();

	public ValidatedTextField(TextFieldValidator validator,
	                          Skin skin,
	                          String styleName)
	{
		this(validator, skin.get(styleName, ValidatedTextFieldStyle.class));
	}

	public ValidatedTextField(TextFieldValidator validator,
	                          ValidatedTextFieldStyle style)
	{
		super(new NullDrawable(), style);

		ensureNotNull(validator, "validator");

		this.validator = validator;
		this.style = style;

		getTextField().addListener(new ChangeAdapter(this::validateInput));

		errorPopup.setBackground(style.popupBackground);
		errorPopup.setVisible(false);
		errorPopup.getColor().a = 0f;
		addActor(errorPopup);
	}

	public void validateInput()
	{
		if(getTextField().getText().length() == 0)
		{
			prevTextFieldContent = "";

			getIcon().setDrawable(new NullDrawable());
			setBackground(style.background);
			errorPopup.clearActions();
			errorPopup.addAction(sequence(fadeOut(0.15f), hide()));
			return;
		}

		if(getTextField().getText().equals(prevTextFieldContent))
			return;

		validator.getErrors(getTextField().getText(), this::displayErrors);
		prevTextFieldContent = getTextField().getText();

		getIcon().setDrawable(style.loading);
		setBackground(style.background);
		errorPopup.clearActions();
		errorPopup.addAction(sequence(fadeOut(0.15f), hide()));
	}

	public void displayErrors(Array<String> errors)
	{
		if(errors.size == 0)
		{
			getIcon().setDrawable(style.valid);
			setBackground(coalesce(style.validBackground, style.background));
			errorPopup.clearActions();
			errorPopup.addAction(sequence(fadeOut(0.15f), hide()));
			return;
		}

		getIcon().setDrawable(style.invalid);
		setBackground(coalesce(style.invalidBackground, style.background));

		errorPopup.clearChildren();

		for(String error : errors)
		{
			Label label = new Label("  - " + error,
					new LabelStyle(style.popupFont, style.popupFontColor));
			label.setWrap(true);

			float width = style.popupWidth - errorPopup.getPadX();
			label.setWidth(width);

			errorPopup.add(label).width(width).row();
		}

		errorPopup.clearActions();
		errorPopup.addAction(sequence(show(), fadeIn(0.15f)));
		errorPopup.setWidth(style.popupWidth);
		errorPopup.setHeight(errorPopup.getPrefHeight());

		errorPopup.setPosition(getWidth() + style.popupOffsetX,
				getHeight() / 2f - errorPopup.getHeight() + style.popupOffsetY);
	}
}
