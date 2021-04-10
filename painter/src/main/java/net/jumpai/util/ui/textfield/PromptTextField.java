package net.jumpai.util.ui.textfield;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import net.jumpai.util.ui.NonGrabbingTextField;

import static net.jumpai.util.ReflectionUtil.call;
import static net.jumpai.util.ReflectionUtil.get;
import static net.jumpai.util.ReflectionUtil.set;
import static net.jumpai.util.Validation.ensurePositive;

/**
 * {@link NonGrabbingTextField} that has a prompt, separate text that is
 * showed with the text of the textfield but not part of what the user types or
 * can erase.
 * <p>
 * Created on 2019-01-17.
 *
 * @author Alexander Winter
 */
public class PromptTextField extends NonGrabbingTextField
{
	private String prompt = "> ";

	private TextFieldListener listener; // duplication since parent has private access

	public PromptTextField(String text, Skin skin)
	{
		super("", skin);
		setText(text);
	}

	public PromptTextField(String text, Skin skin, String styleName)
	{
		super("", skin, styleName);
		setText(text);
	}

	public PromptTextField(String text, TextFieldStyle style)
	{
		super("", style);
		setText(text);
	}

	@Override
	protected InputListener createInputListener()
	{
		return new PromptTextFieldListener();
	}


	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		final Stage stage = getStage();
		final boolean focused = stage != null && stage.getKeyboardFocus() == this;

		final Task blinkTask = get(this, "blinkTask");
		final Task keyRepeatTask = get(this, "keyRepeatTask");
		final float blinkTime = get(this, "blinkTime");

		if(focused != get(this, "focused", Boolean.class))
		{
			set(this, "focused", focused);
			blinkTask.cancel();
			set(this, "cursorOn", focused);
			if (focused)
				Timer.schedule(blinkTask, blinkTime, blinkTime);
			else
				keyRepeatTask.cancel();
		}
		else if (!focused)
			set(this, "cursorOn", false);

		final TextFieldStyle style = getStyle();

		final BitmapFont font = style.font;
		final Color fontColor = (isDisabled() && style.disabledFontColor != null) ? style.disabledFontColor
				: ((focused && style.focusedFontColor != null) ? style.focusedFontColor : style.fontColor);
		final Drawable selection = style.selection;
		final Drawable cursorPatch = style.cursor;
		final Drawable background = call(this, "getBackgroundDrawable");

		final Color color = getColor();
		final float x = getX();
		final float y = getY();
		final float width = getWidth();
		final float height = getHeight();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		float bgLeftWidth = 0, bgRightWidth = 0;

		if(background != null)
		{
			background.draw(batch, x, y, width, height);
			bgLeftWidth = background.getLeftWidth();
			bgRightWidth = background.getRightWidth();
		}

		float textY = getTextY(font, background);
		calculateOffsets();

		if(focused && hasSelection && selection != null)
			drawSelection(selection, batch, font, x + bgLeftWidth, y + textY);

		final String messageText = getMessageText() == null ? null : prompt + getMessageText();

		final float yOffset = font.isFlipped() ? -textHeight : 0;

		if(displayText.length() == prompt.length() && !focused && messageText != null)
		{
			if(style.messageFontColor != null)
				font.setColor(style.messageFontColor.r,
						style.messageFontColor.g,
						style.messageFontColor.b,
						style.messageFontColor.a * color.a * parentAlpha);
			else
				font.setColor(0.7f, 0.7f, 0.7f, color.a * parentAlpha);

			BitmapFont messageFont = style.messageFont != null
					? style.messageFont
					: font;

			messageFont.draw(batch,
					messageText, x + bgLeftWidth,
					y + textY + yOffset,
					0,
					messageText.length(),
					width - bgLeftWidth - bgRightWidth,
					get(this, "textHAlign", Integer.class),
					false,
					"...");
		}
		else if(displayText.length() > 0)
		{
			font.setColor(fontColor.r,
					fontColor.g,
					fontColor.b,
					fontColor.a * color.a * parentAlpha);
			drawText(batch, font, x + bgLeftWidth, y + textY + yOffset);
		}

		if(!isDisabled() && get(this, "cursorOn", Boolean.class) && cursorPatch != null)
			drawCursor(cursorPatch, batch, font, x + bgLeftWidth, y + textY);
	}

	@Override
	public String getText()
	{
		return super.getText().substring(prompt.length());
	}

	@Override
	public void setText(String str)
	{
		super.setText(prompt + str);
		if(prompt != null) // null only when calling super() in constructor
			cursor = prompt.length();
	}

	@Override
	public void setCursorPosition(int cursorPosition)
	{
		ensurePositive(cursorPosition, "cursorPosition");
		super.setCursorPosition(cursorPosition + prompt.length());
	}

	@Override
	public int getCursorPosition()
	{
		return super.getCursorPosition() - prompt.length();
	}

	@Override
	public void setSelection(int selectionStart, int selectionEnd)
	{
		super.setSelection(selectionStart + prompt.length(), selectionEnd + prompt.length());
	}

	@Override
	public void selectAll()
	{
		super.setSelection(prompt.length(), text.length());
	}

	@Override
	protected int letterUnderCursor(float x)
	{
		x -= textOffset + fontOffset - getStyle().font.getData().cursorX - glyphPositions.get(get(this, "visibleTextStart"));
		Drawable background = call(this, "getBackgroundDrawable");
		if (background != null) x -= getStyle().background.getLeftWidth();
		int n = this.glyphPositions.size;
		float[] glyphPositions = this.glyphPositions.items;

		for(int i = 1 + prompt.length(); i < n; i++)
			if(glyphPositions[i] > x)
				return glyphPositions[i] - x <= x - glyphPositions[i - 1] ? i : i - 1;

		return n - 1;
	}

	protected void moveCursor(boolean forward, boolean jump)
	{
		int limit = forward ? text.length() : prompt.length();
		int charOffset = forward ? 0 : -1;
		while((forward ? ++cursor < limit : --cursor > limit)
				&& jump
				&& continueCursor(cursor, charOffset));

		cursor = MathUtils.clamp(cursor, prompt.length(), text.length());
	}

	@Override
	public void setTextFieldListener(TextFieldListener listener)
	{
		super.setTextFieldListener(listener);
		this.listener = listener;
	}

	protected int[] wordUnderCursor(int at)
	{
		String prev = text;
		this.text = getText();
		int[] res = super.wordUnderCursor(at);
		this.text = prev;
		return res;
	}

	public String getPrompt()
	{
		return prompt;
	}

	public void setPrompt(String prompt)
	{
		String text = getText();
		this.prompt = prompt;
		setText(text);
	}

	public class PromptTextFieldListener extends NonGrabbingTextFieldListener
	{
		@Override
		public boolean keyDown(InputEvent event, int keycode)
		{
			if(isDisabled())
				return false;

			Stage stage = getStage();
			if(stage == null || stage.getKeyboardFocus() != PromptTextField.this)
				return false;

			if(UIUtils.ctrl() && keycode == Keys.Z)
			{
				String oldText = text;
				PromptTextField.super.setText(get(PromptTextField.this, "undoText"));
				cursor = prompt.length();
				set(PromptTextField.this, "undoText", oldText);
				return true;
			}

			return super.keyDown(event, keycode);
		}

		@Override
		protected void goHome(boolean jump)
		{
			cursor = prompt.length();
		}

		public boolean keyTyped(InputEvent event, char character)
		{
			if(isDisabled())
				return false;

			if(character == 8 && cursor <= prompt.length()) // BACKSPACE = 8; (private access)
			{
				if(listener != null)
					listener.keyTyped(PromptTextField.this, character);
				return true;
			}
			return super.keyTyped(event, character);
		}
	}
}
