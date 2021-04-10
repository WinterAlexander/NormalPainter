package net.jumpai.util.ui.colorpicker;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Pools;
import net.jumpai.util.StringUtil;
import net.jumpai.util.ui.drawable.MultiDrawable;
import net.jumpai.util.ui.drawable.TintedDrawable;
import net.jumpai.util.ui.listener.ChangeAdapter;

import java.util.Locale;

import static com.badlogic.gdx.graphics.Color.rgb888;
import static java.lang.Integer.toHexString;
import static net.jumpai.util.gdx.Scene2dUtil.isKeyEvent;

/**
 * Widget to select any RGB888 color
 * <p>
 * Created on 2019-06-12.
 *
 * @author Alexander Winter
 */
public class ColorPicker extends Table
{
	private final TextField codeField;

	private final SquareSlider sbSelector;
	private final Slider hueSelector;

	private final Color color = new Color();
	private final Color tmpColor = new Color();
	private final Color prevColor = new Color();
	private final Color hueOnly = new Color();

	private final float[] tmpHsv = new float[3];

	private boolean updatingSelectors = false;

	public ColorPicker(Skin skin, String styleName)
	{
		this(skin.get(styleName, ColorPickerStyle.class));
	}

	public ColorPicker(ColorPickerStyle style)
	{
		SquareSliderStyle sbSelectorStyle = new SquareSliderStyle();

		sbSelectorStyle.background = new MultiDrawable(
				new TintedDrawable(style.satBriGradientBackground, hueOnly),
				style.satBriGradient);
		sbSelectorStyle.knob = new MultiDrawable(
				new TintedDrawable(style.satBriKnobBackground, color),
				style.satBriKnob);

		sbSelector = new SquareSlider(Vector2.Zero,
				new Vector2(1f, 1f),
				sbSelectorStyle);

		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = style.hueGradient;
		sliderStyle.knob = new MultiDrawable(
				new TintedDrawable(style.hueKnobBackground, hueOnly),
				style.hueKnob);

		hueSelector = new Slider(0f, 360f, 1f, true, sliderStyle);

		add(sbSelector).pad(5f).size(200f);
		add(hueSelector).padLeft(0f).pad(5f).height(200f);
		row();

		Table table = new Table();

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.fontColor = Color.WHITE;
		labelStyle.font = style.font;

		table.add(new Label("#", labelStyle));

		TextFieldStyle fieldStyle = new TextFieldStyle();
		fieldStyle.font = style.font;
		fieldStyle.fontColor = Color.WHITE;
		fieldStyle.cursor = style.fieldCursor;
		fieldStyle.selection = style.fieldSelection;

		codeField = new TextField("", fieldStyle) {
			@Override
			protected InputListener createInputListener() {
				return new TextFieldClickListener() {
					@Override
					public boolean handle(Event e) {
						boolean handle = super.handle(e);

						if(e instanceof InputEvent
								&& isKeyEvent((InputEvent)e)
								&& (((InputEvent)e).getKeyCode() == Keys.CONTROL_LEFT
									|| ((InputEvent)e).getKeyCode() == Keys.CONTROL_RIGHT))
							return false;

						return handle;
					}
				};
			}
		};

		table.add(codeField).width(100f);

		add(table).colspan(2).expandX().right().row();

		sbSelector.addListener(new ChangeAdapter(this::update));
		hueSelector.addListener(new ChangeAdapter(this::update));
		codeField.addListener(new ChangeAdapter(() -> {
			try
			{
				setColorValue(Color.valueOf(codeField.getText()));
			}
			catch(NumberFormatException | StringIndexOutOfBoundsException ignored) {}
		}));

		setTouchable(Touchable.childrenOnly);
		setColorValue(Color.WHITE);
	}

	public void update()
	{
		if(updatingSelectors)
			return;

		Vector2 val = sbSelector.getValue();
		setColorValue(tmpColor.fromHsv(360f - hueSelector.getValue(), val.x, val.y));
	}

	public Color getColorValue()
	{
		return tmpColor.set(color);
	}

	public void setColorValue(Color color)
	{
		if(this.color.equals(color))
			return;

		prevColor.set(this.color);

		setColorInternal(color);

		ChangeEvent event = Pools.obtain(ChangeEvent.class);

		if(fire(event))
			setColorInternal(prevColor);

		Pools.free(event);
	}

	private void setColorInternal(Color color)
	{
		this.color.set(color);
		this.color.a = 1f;

		String hexStr = toHexString(rgb888(color)).toUpperCase(Locale.US);
		codeField.setText(StringUtil.padLeft(hexStr, '0', 6 - hexStr.length()));

		Vector2 val = sbSelector.getValue();
		tmpColor.fromHsv(360f - hueSelector.getValue(), val.x, val.y);

		if(!tmpColor.equals(color))
		{
			color.toHsv(tmpHsv);
			updatingSelectors = true;
			sbSelector.setValue(tmpHsv[1], tmpHsv[2]);
			hueSelector.setValue(360f - tmpHsv[0]);
			updatingSelectors = false;
		}

		hueOnly.fromHsv(360f - hueSelector.getValue(), 1f, 1f);
		hueOnly.a = 1f;
	}
}
