package net.jumpai.tools.normalpainter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import jpen.PenDevice;
import net.jumpai.render.AssetSupplier;
import net.jumpai.tools.normalpainter.buffer.GdxPixmap;
import net.jumpai.util.math.MathUtil;
import net.jumpai.util.ui.BetterSlider;
import net.jumpai.util.ui.dynamic.DynamicLabel;
import net.jumpai.util.ui.dynamic.DynamicStage;
import net.jumpai.util.ui.dynamic.DynamicTable;
import net.jumpai.util.ui.dynamic.DynamicTextButton;
import net.jumpai.util.ui.dynamic.DynamicTextField;
import net.jumpai.util.ui.listener.ChangeAdapter;
import net.jumpai.util.ui.listener.ClickAdapter;
import net.jumpai.util.ui.listener.HoverAdapter;
import net.jumpai.util.ui.listener.TouchNothingListener;
import net.jumpai.util.ui.listener.TouchOutOfActorListener;
import net.jumpai.util.ui.scrollpane.BetterScrollPane;

import java.io.File;
import java.util.Locale;

import static net.jumpai.tools.normalpainter.NormalPainterScreen.STAGE_HEIGHT;
import static net.jumpai.tools.normalpainter.NormalPainterScreen.STAGE_WIDTH;

/**
 * Stage for {@link NormalPainterScreen}
 * <p>
 * Created on 2020-12-21.
 *
 * @author Alexander Winter
 */
public class NormalPainterStage extends DynamicStage
{
	private final NormalPainterScreen screen;

	BetterSlider brushS, hardn, eqW, opacityS;
	SelectBox<DrawMode> drawModeSelect;
	CheckBox multiply;
	CheckBox enablePressure;
	BetterSlider maxPressure;

	CheckBox disaStick, invertPinning, normalRelToPath, invert;
	BetterSlider maxTiltS, minRad, maxRad, rotSpeed, angleFromPath, numpadIntensity;

	CheckBox showFlat, maskOnTop, normV, livePrev, drawLines, flatOnTop;
	BetterSlider maskOpacity, viewSpacing, flatOpacity;

	CheckBox fillBlue;

	TextField flatFile, maskFile, inputFile, outputFile;

	DesktopFileSelector fileSelector = new DesktopFileSelector();

	private final Vector3 tmpVec3 = new Vector3();

	File baseDir = new File(System.getProperty("user.dir"));

	public NormalPainterStage(NormalPainterScreen screen)
	{
		super(new FitViewport(STAGE_WIDTH, STAGE_HEIGHT), screen.batch);
		this.screen = screen;

		AssetSupplier assets = screen.assets;

		DynamicTable container = new DynamicTable(assets.getSkin());

		container.setFillParent(true);
		container.top();
		container.left();

		DynamicTable table = new DynamicTable(assets.getSkin());
		table.add("NormalPainter Alpha v1.4.1").left().row();
		table.add("by Alexander Winter").left().row();

		makeBrushSection(assets, screen.painter, table);

		makeNormalSection(assets, screen.colorPicker, table);

		makeDisplaySection(assets, table);

		makeExportSection(assets, table);

		makeDeviceSection(assets, table);

		makeHelpSection(assets, table);

		table.add(new DynamicCallback(() -> {
			opacityS.setValue(screen.painter.brushOpacity);
			brushS.setValue(screen.painter.brushSize);
			enablePressure.setChecked(screen.painter.enablePressure);
			maxPressure.setValue(screen.painter.maxPressure);
			hardn.setValue(screen.painter.hardness);
			eqW.setValue(screen.painter.equalizeWeight);
			drawModeSelect.setSelected(screen.painter.drawMode);
			multiply.setChecked(screen.painter.maskMultiply);

			disaStick.setChecked(screen.colorPicker.disableStickOrPen);
			maxTiltS.setValue(screen.colorPicker.maxTilt);
			minRad.setValue(screen.colorPicker.minRadius);
			maxRad.setValue(screen.colorPicker.maxRadius);
			rotSpeed.setValue(screen.colorPicker.normalRotateSpeed);
			invertPinning.setChecked(screen.colorPicker.invertPinning);
			normalRelToPath.setChecked(screen.colorPicker.normalRelToPath);
			angleFromPath.setValue(screen.colorPicker.angle);
			numpadIntensity.setValue(screen.colorPicker.numpadIntensity);
			invert.setChecked(screen.colorPicker.invert);

			showFlat.setChecked(screen.renderFlat);
			maskOnTop.setChecked(screen.maskOnTop);
			flatOnTop.setChecked(screen.flatOnTop);
			normV.setChecked(screen.viewNormals);
			maskOpacity.setValue(screen.maskOpacity);
			flatOpacity.setValue(screen.flatOpacity);
			viewSpacing.setValue(screen.normalSpacing);
			livePrev.setChecked(screen.livePreview);
			drawLines.setChecked(screen.drawLinesOnDrag);

			fillBlue.setChecked(screen.fillBlue);
		}));
		table.setBackground("flat-roundrect-20-alpha-transparent");
		table.pad(10f);

		BetterScrollPane pane = new BetterScrollPane(table, assets.getSkin());
		pane.addListener(new TouchOutOfActorListener(pane, this::unfocusAll));
		pane.setCancelTouchFocus(false);
		pane.setScrollingDisabled(true, false);

		pane.addListener(new HoverAdapter(
				() -> setScrollFocus(pane),
				() -> setScrollFocus(null)));
		container.add(pane);
		addActor(container);

		DynamicTextField field = new DynamicTextField(() -> {
			String value = Integer.toHexString(((int)(255 * screen.painter.color.r) << 16) | ((int)(255 * screen.painter.color.g) << 8) | ((int)(255 * screen.painter.color.b)));
			while(value.length() < 6)
				value = "0" + value;
			return "#" + value.toUpperCase(Locale.ROOT);
		}, assets.getSkin(), "default-small");
		field.setBounds(STAGE_WIDTH - field.getPrefWidth() - 10f, STAGE_HEIGHT - 275f, field.getPrefWidth(), field.getPrefHeight());
		addActor(field);

		DynamicTable savingTable = new DynamicTable(assets.getSkin());

		DynamicTextButton flush = new DynamicTextButton(() -> "Flush mask", assets.getSkin(), "default-small") {
			@Override
			public void update()
			{
				super.update();
				setVisible(screen.painter.uncommittedMaskChanges);
			}
		};

		flush.addListener(new ClickAdapter(screen.painter::flushMask));

		savingTable.add(flush).colspan(3).center().row();

		savingTable.add(new DynamicLabel(() -> screen.app.unsavedChanges ? "Unsaved changes" : "", assets.getSkin())).colspan(3).center().row();

		Table buttonWrapper = new Table(assets.getSkin());

		TextButton newPixmap = new TextButton("New image", assets.getSkin(), "default-small");
		newPixmap.addListener(new ClickAdapter(() -> {

			Vector2 maskSize = screen.painter.mask != null ? new Vector2(screen.painter.mask.getWidth(),
					screen.painter.mask.getHeight()) : null;

			Vector2 flatSize = screen.flat != null ? new Vector2(screen.flat.getRegionWidth(),
					screen.flat.getRegionHeight()) : null;

			new NewPixmapDialog(assets, maskSize, flatSize, size -> newPixmap(Math.round(size.x), Math.round(size.y))).show(this);
		}));
		buttonWrapper.add(newPixmap).padRight(5f);

		TextButton loadTrio = new TextButton("Load Trio", assets.getSkin(), "default-small");
		loadTrio.addListener(new ClickAdapter(() -> {
			fileSelector.selectFile(file -> {
				Gdx.app.postRunnable(() -> {
					String flatAsset = null, altFlatAsset = null;
					String normalAsset = null;
					String maskAsset = null;

					if(file.getName().endsWith("_f.png"))
					{
						flatAsset = file.getName();
						altFlatAsset = file.getName().substring(0, file.getName().length() - 6) + ".png";
						normalAsset = file.getName().substring(0, file.getName().length() - 6) + "_n.png";
						maskAsset = file.getName().substring(0, file.getName().length() - 6) + "_m.png";
					}
					else if(file.getName().endsWith("_n.png"))
					{
						flatAsset = file.getName().substring(0, file.getName().length() - 6) + "_f.png";
						altFlatAsset = file.getName().substring(0, file.getName().length() - 6) + ".png";
						normalAsset = file.getName();
						maskAsset = file.getName().substring(0, file.getName().length() - 6) + "_m.png";
					}
					else if(file.getName().endsWith("_m.png"))
					{
						flatAsset = file.getName().substring(0, file.getName().length() - 6) + "_f.png";
						altFlatAsset = file.getName().substring(0, file.getName().length() - 6) + ".png";
						normalAsset = file.getName().substring(0, file.getName().length() - 6) + "_n.png";
						maskAsset = file.getName();
					}
					else if(file.getName().endsWith(".png"))
					{
						flatAsset = file.getName();
						altFlatAsset = file.getName().substring(0, file.getName().length() - 4) + "_f.png";
						normalAsset = file.getName().substring(0, file.getName().length() - 4) + "_n.png";
						maskAsset = file.getName().substring(0, file.getName().length() - 4) + "_m.png";
					}

					File flatAssetF = new File(file.getParentFile(), flatAsset);
					File altAssetF = new File(file.getParentFile(), altFlatAsset);
					File maskAssetF = new File(file.getParentFile(), maskAsset);
					File normalAssetF = new File(file.getParentFile(), normalAsset);

					if(flatAssetF.exists())
					{
						flatFile.setText(baseDir.toURI().relativize(flatAssetF.toURI()).getPath());
						loadFlat(false);
					}
					else if(altAssetF.exists())
					{
						flatFile.setText(baseDir.toURI().relativize(altAssetF.toURI()).getPath());
						loadFlat(false);
					}
					else
					{
						flatFile.setText("");
						loadFlat(true);
					}

					if(maskAssetF.exists())
					{
						maskFile.setText(baseDir.toURI().relativize(maskAssetF.toURI()).getPath());
						loadMask(false);
					}
					else
					{
						maskFile.setText("");
						loadMask(true);
					}

					if(normalAssetF.exists())
					{
						inputFile.setText(baseDir.toURI().relativize(normalAssetF.toURI()).getPath());
						load(false);
					}
					else
					{
						inputFile.setText("");
						load(true);
					}
				});
			});
		}));
		buttonWrapper.add(loadTrio);


		savingTable.add(buttonWrapper).colspan(3).row();

		savingTable.add("Flat", "default-small").colspan(3).left().row();

		TextButton flatOpen = new TextButton("O", assets.getSkin(), "default-tiny");
		savingTable.add(flatOpen).width(30f);

		flatFile = new TextField("", assets.getSkin(), "default-tiny");
		savingTable.add(flatFile).growX();
		TextButton loadFlatButton = new TextButton("Load", assets.getSkin(), "default-small");
		loadFlatButton.addListener(new ClickAdapter(() -> loadFlat(false)));
		savingTable.add(loadFlatButton).row();

		flatOpen.addListener(new ClickAdapter(() -> {
			fileSelector.selectFile(file -> {
				Gdx.app.postRunnable(() -> {
					flatFile.setText(baseDir.toURI().relativize(file.toURI()).getPath());
					loadFlat(false);
				});
			});
		}));

		savingTable.add("Mask", "default-small").colspan(3).left().row();

		TextButton refOpen = new TextButton("O", assets.getSkin(), "default-tiny");
		savingTable.add(refOpen).width(30f);

		maskFile = new TextField("", assets.getSkin(), "default-tiny");
		savingTable.add(maskFile).growX();
		TextButton loadRefButton = new TextButton("Load", assets.getSkin(), "default-small");
		loadRefButton.addListener(new ClickAdapter(() -> loadMask(false)));
		savingTable.add(loadRefButton).row();

		refOpen.addListener(new ClickAdapter(() -> {
			fileSelector.selectFile(file -> {
				Gdx.app.postRunnable(() -> {
					maskFile.setText(baseDir.toURI().relativize(file.toURI()).getPath());
					loadMask(false);
				});
			});
		}));

		savingTable.add("Input", "default-small").colspan(3).left().row();

		TextButton inOpen = new TextButton("O", assets.getSkin(), "default-tiny");
		savingTable.add(inOpen).width(30f);

		inputFile = new TextField("", assets.getSkin(), "default-tiny");
		savingTable.add(inputFile).growX();
		TextButton loadInputButton = new TextButton("Load", assets.getSkin(), "default-small");
		loadInputButton.addListener(new ClickAdapter(() -> load(false)));
		savingTable.add(loadInputButton).row();

		inOpen.addListener(new ClickAdapter(() -> {
			fileSelector.selectFile(file -> {
				Gdx.app.postRunnable(() -> {
					inputFile.setText(baseDir.toURI().relativize(file.toURI()).getPath());
					load(false);
				});
			});
		}));

		savingTable.add("Output", "default-small").colspan(3).left().row();

		TextButton outOpen = new TextButton("O", assets.getSkin(), "default-tiny");
		savingTable.add(outOpen).width(30f);

		outputFile = new TextField("", assets.getSkin(), "default-tiny");
		savingTable.add(outputFile).growX();
		TextButton saveOutputButton = new TextButton("Save", assets.getSkin(), "default-small");
		saveOutputButton.addListener(new ClickAdapter(this::save));
		savingTable.add(saveOutputButton).row();

		outOpen.addListener(new ClickAdapter(() -> {
			fileSelector.selectFile(file -> {
				Gdx.app.postRunnable(() -> {
					outputFile.setText(baseDir.toURI().relativize(file.toURI()).getPath());
					save();
				});
			});
		}));

		savingTable.setBounds(STAGE_WIDTH - savingTable.getPrefWidth() - 10f,
				10f,
				savingTable.getPrefWidth(),
				savingTable.getPrefHeight());

		addListener(new TouchNothingListener(savingTable, () -> {
			setKeyboardFocus(null);
		}));

		addActor(savingTable);
	}

	public void loadFlat(boolean silent)
	{
		if(flatFile.getText().length() <= 0)
		{
			screen.flat = null;
			return;
		}

		TextureParameter mipMapParams = new TextureParameter();
		mipMapParams.minFilter = TextureFilter.MipMapLinearLinear;
		mipMapParams.magFilter = TextureFilter.Nearest;
		mipMapParams.genMipMaps = true;

		try
		{
			screen.flat = new TextureRegion(new Texture(new FileHandle(new File(flatFile.getText()))));
		}
		catch(Exception ex)
		{
			screen.flat = null;
			if(!silent)
				new OkayDialog(screen.assets, "Failed to load flat file", flatFile.getText()).show(this);
		}
	}

	public void loadMask(boolean silent)
	{
		if(maskFile.getText().length() <= 0)
		{
			screen.painter.mask = null;
			return;
		}

		try
		{
			screen.painter.mask = new GdxPixmap(new FileHandle(new File(maskFile.getText())));
		}
		catch(Exception ex)
		{
			screen.painter.mask = null;
			if(!silent)
				new OkayDialog(screen.assets, "Failed to load mask file", maskFile.getText()).show(this);
		}
	}

	public void load(boolean silent)
	{
		if(inputFile.getText().length() <= 0)
		{
			if(silent)
			{
				int width = 123;
				int height = 123;

				if(screen.painter.mask != null)
				{
					width = screen.painter.mask.getWidth();
					height = screen.painter.mask.getHeight();
				}

				newPixmap(width, height);
			}
			return;
		}

		try
		{
			screen.painter.pixmap = new GdxPixmap(new FileHandle(new File(inputFile.getText())));
			screen.painter.init();
		}
		catch(Exception ex)
		{
			if(screen.painter.pixmap == null)
			{
				int width = 123;
				int height = 123;

				if(screen.painter.mask != null)
				{
					width = screen.painter.mask.getWidth();
					height = screen.painter.mask.getHeight();
				}

				newPixmap(width, height);
			}

			new OkayDialog(screen.assets, "Failed to load input file", inputFile.getText()).show(this);
		}
	}

	public void newPixmap(int width, int height)
	{
		screen.painter.pixmap = new GdxPixmap(width, height);
		screen.painter.init();
	}

	public void save()
	{
		if(outputFile.getText().length() <= 0)
		{
			new SaveDialog(screen.assets.getSkin(), "", fileName -> {
				outputFile.setText(fileName);
				save();
			}).show(this);
			return;
		}

		if(!outputFile.getText().contains("."))
			outputFile.setText(outputFile.getText() + ".png");

		try
		{
			if(screen.fillBlue)
			{
				screen.buffer.setColor(0.5f, 0.5f, 1f, 1f);
				screen.buffer.fillRectangle(0, 0, screen.buffer.getWidth(), screen.buffer.getHeight());
				screen.buffer.setBlending(Blending.SourceOver);
				screen.buffer.drawPixmap(screen.painter.preview, 0, 0);
				PixmapIO.writePNG(new FileHandle(new File(outputFile.getText())), screen.buffer);
			}
			else
				PixmapIO.writePNG(new FileHandle(new File(outputFile.getText())), screen.painter.preview);
			screen.app.unsavedChanges = false;
		}
		catch(Exception ex)
		{
			new OkayDialog(screen.assets, "Failed to save to output file").show(this);
			ex.printStackTrace();
		}
	}

	private void makeBrushSection(AssetSupplier assets, PaintComponent painter, Table table)
	{
		DynamicTable header = new DynamicTable(assets.getSkin());

		header.add("Brush", "default-small").expandX().left();

		DynamicTable content = new DynamicTable(assets.getSkin());

		DynamicTextButton opener = new DynamicTextButton(() -> content.isVisible() ? "-" : "+", assets.getSkin(), "default-small");
		opener.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				content.setVisible(!content.isVisible());
				table.getCell(content).height(content.isVisible() ? content.getPrefHeight() : 0f);
				return true;
			}
		});
		header.add(opener).width(40f);

		table.add(header).fillX().padTop(20f).left().row();

		content.padLeft(20f);

		content.add("Opacity: ", "default-small").left().row();
		opacityS = new BetterSlider(0f, 1f, 1f / 255f, false, assets.getSkin(), 150f);
		opacityS.setValue(painter.brushOpacity);
		opacityS.addListener(new ChangeAdapter(() -> painter.brushOpacity = opacityS.getValue()));

		DynamicTable brushOpaContainer = new DynamicTable();
		brushOpaContainer.add(opacityS);
		brushOpaContainer.add(new DynamicLabel(() -> MathUtil.round(painter.brushOpacity, 2) + "", assets.getSkin(), "default-small"));

		content.add(brushOpaContainer).left().row();

		content.add("Size: ", "default-small").left().row();
		brushS = new BetterSlider(0f, 100f, 1f, false, assets.getSkin(), 150f);
		brushS.setValue(painter.brushSize);
		brushS.addListener(new ChangeAdapter(() -> painter.brushSize = Math.round(brushS.getValue())));

		DynamicTable brushSizeContainer = new DynamicTable();
		brushSizeContainer.add(brushS);
		brushSizeContainer.add(new DynamicLabel(() -> MathUtil.round(painter.brushSize, 2) + "", assets.getSkin(), "default-small"));

		content.add(brushSizeContainer).left().row();

		enablePressure = new CheckBox(" Enable pressure", assets.getSkin(), "default-small");
		enablePressure.setChecked(painter.enablePressure);
		enablePressure.addListener(new ChangeAdapter(() -> painter.enablePressure = enablePressure.isChecked()));
		content.add(enablePressure).left().row();

		content.add("Max pressure: ", "default-small").left().row();
		maxPressure = new BetterSlider(0f, 1f, 0.0001f, false, assets.getSkin(), 150f);
		maxPressure.setValue(painter.maxPressure);
		maxPressure.addListener(new ChangeAdapter(() -> painter.maxPressure = maxPressure.getValue()));

		DynamicTable maxPresContainer = new DynamicTable();
		maxPresContainer.add(maxPressure);
		maxPresContainer.add(new DynamicLabel(() -> MathUtil.round(painter.maxPressure, 2) + "", assets.getSkin(), "default-small"));

		content.add(maxPresContainer).left().row();

		content.add("Hardness: ", "default-small").left().row();
		hardn = new BetterSlider(0f, 1f, 1f / 255f, false, assets.getSkin(), 150f);
		hardn.setValue(painter.hardness);
		hardn.addListener(new ChangeAdapter(() -> painter.hardness = hardn.getValue()));

		DynamicTable hardnContainer = new DynamicTable();
		hardnContainer.add(hardn);
		hardnContainer.add(new DynamicLabel(() -> MathUtil.round(painter.hardness, 2) + "", assets.getSkin(), "default-small"));

		content.add(hardnContainer).left().row();

		content.add(new DynamicLabel(() -> "Draw mode: ", assets.getSkin(), "default-small")).left().row();

		drawModeSelect = new SelectBox<DrawMode>(assets.getSkin()) {
			@Override
			protected void onShow(Actor selectBoxList, boolean below) {}

			@Override
			protected void onHide(Actor selectBoxList) {
				selectBoxList.remove();
			}
		};
		drawModeSelect.setItems(DrawMode.values());
		drawModeSelect.setSelected(painter.drawMode);
		drawModeSelect.addListener(new ChangeAdapter(() -> painter.drawMode = drawModeSelect.getSelected()));
		content.add(drawModeSelect).left().row();

		content.add("Blend weight: ", "default-small").left().row();
		eqW = new BetterSlider(0f, 1f, 1f / 255f, false, assets.getSkin(), 150f);
		eqW.setValue(painter.equalizeWeight);
		eqW.addListener(new ChangeAdapter(() -> painter.equalizeWeight = eqW.getValue()));

		DynamicTable eqContainer = new DynamicTable();
		eqContainer.add(eqW);
		eqContainer.add(new DynamicLabel(() -> MathUtil.round(painter.equalizeWeight, 2) + "", assets.getSkin(), "default-small"));

		content.add(eqContainer).left().row();

		multiply = new CheckBox(" Mask multiply (M)", assets.getSkin(), "default-small");
		multiply.setChecked(painter.maskMultiply);
		multiply.addListener(new ChangeAdapter(() -> painter.maskMultiply = multiply.isChecked()));
		content.add(multiply).left().row();

		table.add(content).expandX().left().row();
	}

	private void makeNormalSection(AssetSupplier assets, ColorComponent colorPicker, Table table)
	{
		DynamicTable header = new DynamicTable(assets.getSkin());

		header.add("Normal color", "default-small").expandX().left();

		DynamicTable content = new DynamicTable(assets.getSkin());

		DynamicTextButton opener = new DynamicTextButton(() -> content.isVisible() ? "-" : "+", assets.getSkin(), "default-small");
		opener.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				content.setVisible(!content.isVisible());
				table.getCell(content).height(content.isVisible() ? content.getPrefHeight() : 0f);
				return true;
			}
		});
		header.add(opener).width(40f);

		table.add(header).fillX().padTop(20f).left().row();

		content.padLeft(20f);

		disaStick = new CheckBox(" Disable joystick/pen", assets.getSkin(), "default-small");
		disaStick.setChecked(colorPicker.disableStickOrPen);
		disaStick.addListener(new ChangeAdapter(() -> colorPicker.disableStickOrPen = disaStick.isChecked()));
		content.add(disaStick).left().row();

		content.add("Max pen tilt (degrees): ", "default-small").left().row();
		maxTiltS = new BetterSlider(0f, 50f, 0.0001f, false, assets.getSkin(), 150f);
		maxTiltS.setValue(colorPicker.maxTilt);
		maxTiltS.addListener(new ChangeAdapter(() -> colorPicker.maxTilt = maxTiltS.getValue()));

		DynamicTable maxTiltContainer = new DynamicTable();
		maxTiltContainer.add(maxTiltS);
		maxTiltContainer.add(new DynamicLabel(() -> colorPicker.maxTilt + "", assets.getSkin(), "default-small"));

		content.add(maxTiltContainer).left().row();

		content.add("Min radius: ", "default-small").left().row();
		minRad = new BetterSlider(0f, 1f, 1f / 255f, false, assets.getSkin(), 150f);
		minRad.setValue(colorPicker.minRadius);
		minRad.addListener(new ChangeAdapter(() -> colorPicker.minRadius = minRad.getValue()));

		DynamicTable minRadContainer = new DynamicTable();
		minRadContainer.add(minRad);
		minRadContainer.add(new DynamicLabel(() -> MathUtil.round(colorPicker.minRadius, 2) + "", assets.getSkin(), "default-small"));

		content.add(minRadContainer).left().row();

		content.add("Max radius: ", "default-small").left().row();
		maxRad = new BetterSlider(0f, 1f, 1f / 255f, false, assets.getSkin(), 150f);
		maxRad.setValue(colorPicker.maxRadius);
		maxRad.addListener(new ChangeAdapter(() -> colorPicker.maxRadius = maxRad.getValue()));

		DynamicTable maxRadContainer = new DynamicTable();
		maxRadContainer.add(maxRad);
		maxRadContainer.add(new DynamicLabel(() -> MathUtil.round(colorPicker.maxRadius, 2) + "", assets.getSkin(), "default-small"));

		content.add(maxRadContainer).left().row();

		content.add(new DynamicLabel(() -> {

			screen.painter.tmpColor.set(Color.rgba8888(screen.painter.color));
			tmpVec3.set(screen.painter.tmpColor.r, screen.painter.tmpColor.g, screen.painter.tmpColor.b).scl(2f).sub(1f);
			float quality = tmpVec3.len();

			return "Normal error: " + MathUtil.round(Math.abs(1f - quality), 5);
		}, assets.getSkin(), "default-small")).left().row();

		content.add("Normal rotation speed: ", "default-small").left().row();
		rotSpeed = new BetterSlider(0f, 10f, 0.0001f, false, assets.getSkin(), 150f);
		rotSpeed.setValue(colorPicker.normalRotateSpeed);
		rotSpeed.addListener(new ChangeAdapter(() -> colorPicker.normalRotateSpeed = rotSpeed.getValue()));

		DynamicTable rotSpeedContainer = new DynamicTable();
		rotSpeedContainer.add(rotSpeed);
		rotSpeedContainer.add(new DynamicLabel(() -> MathUtil.round(colorPicker.normalRotateSpeed, 2) + "", assets.getSkin(), "default-small"));

		content.add(rotSpeedContainer).left().row();

		invertPinning = new CheckBox(" Invert normal pin direction (I)", assets.getSkin(), "default-small");
		invertPinning.setChecked(colorPicker.invertPinning);
		invertPinning.addListener(new ChangeAdapter(() -> colorPicker.invertPinning = invertPinning.isChecked()));
		content.add(invertPinning).left().row();

		normalRelToPath = new CheckBox(" Normals from path (WIP)", assets.getSkin(), "default-small");
		normalRelToPath.setChecked(colorPicker.normalRelToPath);
		normalRelToPath.addListener(new ChangeAdapter(() -> {
			colorPicker.normalRelToPath = normalRelToPath.isChecked();
			if(colorPicker.normalRelToPath)
				colorPicker.pinned = false;
		}));
		content.add(normalRelToPath).left().row();

		content.add("Angle from path (CCW): ", "default-small").left().row();
		angleFromPath = new BetterSlider(-180f, 180f, 0.1f, false, assets.getSkin(), 150f);
		angleFromPath.setValue(colorPicker.angle);
		angleFromPath.addListener(new ChangeAdapter(() -> colorPicker.angle = angleFromPath.getValue()));

		DynamicTable angleContainer = new DynamicTable();
		angleContainer.add(angleFromPath);
		angleContainer.add(new DynamicLabel(() -> MathUtil.round(colorPicker.angle, 2) + "", assets.getSkin(), "default-small"));

		content.add(angleContainer).left().row();

		content.add("Numpad color intensity: ", "default-small").left().row();
		numpadIntensity = new BetterSlider(0f, 1f, 0.0001f, false, assets.getSkin(), 150f);
		numpadIntensity.setValue(colorPicker.numpadIntensity);
		numpadIntensity.addListener(new ChangeAdapter(() -> colorPicker.numpadIntensity = numpadIntensity.getValue()));

		DynamicTable numContainer = new DynamicTable();
		numContainer.add(numpadIntensity);
		numContainer.add(new DynamicLabel(() -> colorPicker.numpadIntensity + "", assets.getSkin(), "default-small"));

		content.add(numContainer).left().row();

		invert = new CheckBox(" Invert color picker (Y)", assets.getSkin(), "default-small");
		invert.setChecked(colorPicker.invert);
		invert.addListener(new ChangeAdapter(() -> colorPicker.invert = invert.isChecked()));
		content.add(invert).left().row();

		table.add(content).expandX().left().row();
	}

	private void makeDisplaySection(AssetSupplier assets, Table table)
	{
		DynamicTable header = new DynamicTable(assets.getSkin());

		header.add("Display", "default-small").expandX().left();

		DynamicTable content = new DynamicTable(assets.getSkin());

		DynamicTextButton opener = new DynamicTextButton(() -> content.isVisible() ? "-" : "+", assets.getSkin(), "default-small");
		opener.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				content.setVisible(!content.isVisible());
				table.getCell(content).height(content.isVisible() ? content.getPrefHeight() : 0f);
				return true;
			}
		});
		header.add(opener).width(40f);

		table.add(header).fillX().padTop(20f).left().row();

		content.padLeft(20f);

		maskOnTop = new CheckBox(" Mask on top", assets.getSkin(), "default-small");
		maskOnTop.setChecked(screen.maskOnTop);
		maskOnTop.addListener(new ChangeAdapter(() -> screen.maskOnTop = maskOnTop.isChecked()));
		content.add(maskOnTop).left().row();

		content.add("Mask opacity: ", "default-small").padLeft(20f).left().row();
		maskOpacity = new BetterSlider(0f, 1f, 1f / 255f, false, assets.getSkin(), 150f);
		maskOpacity.setValue(screen.maskOpacity);
		maskOpacity.addListener(new ChangeAdapter(() -> screen.maskOpacity = maskOpacity.getValue()));
		content.add(maskOpacity).left().row();

		showFlat = new CheckBox(" Show flat texture (T)", assets.getSkin(), "default-small");
		showFlat.setChecked(screen.renderFlat);
		showFlat.addListener(new ClickAdapter(() -> screen.renderFlat = showFlat.isChecked()));
		content.add(showFlat).left().row();

		flatOnTop = new CheckBox(" Flat on top", assets.getSkin(), "default-small");
		flatOnTop.setChecked(screen.flatOnTop);
		flatOnTop.addListener(new ChangeAdapter(() -> screen.flatOnTop = flatOnTop.isChecked()));
		content.add(flatOnTop).left().row();

		content.add("Flat opacity: ", "default-small").padLeft(20f).left().row();
		flatOpacity = new BetterSlider(0f, 1f, 1f / 255f, false, assets.getSkin(), 150f);
		flatOpacity.setValue(screen.flatOpacity);
		flatOpacity.addListener(new ChangeAdapter(() -> screen.flatOpacity = flatOpacity.getValue()));
		content.add(flatOpacity).left().row();

		normV = new CheckBox(" View normals", assets.getSkin(), "default-small");
		normV.setChecked(screen.viewNormals);
		normV.addListener(new ChangeAdapter(() -> screen.viewNormals = normV.isChecked()));
		content.add(normV).left().row();

		content.add("Normal view spacing: ", "default-small").padLeft(20f).left().row();
		viewSpacing = new BetterSlider(1f, 50f, 1f, false, assets.getSkin(), 150f);
		viewSpacing.setValue(screen.normalSpacing);
		viewSpacing.addListener(new ChangeAdapter(() -> screen.normalSpacing = Math.round(viewSpacing.getValue())));
		content.add(viewSpacing).left().row();

		livePrev = new CheckBox(" Live preview", assets.getSkin(), "default-small");
		livePrev.setChecked(screen.livePreview);
		livePrev.addListener(new ChangeAdapter(() -> screen.livePreview = livePrev.isChecked()));
		content.add(livePrev).left().row();

		drawLines = new CheckBox(" Draw lines on drag", assets.getSkin(), "default-small");
		drawLines.setChecked(screen.drawLinesOnDrag);
		drawLines.addListener(new ChangeAdapter(() -> screen.drawLinesOnDrag = drawLines.isChecked()));
		content.add(drawLines).left().row();

		table.add(content).expandX().left().row();
	}

	private void makeExportSection(AssetSupplier assets, Table table)
	{
		DynamicTable header = new DynamicTable(assets.getSkin());

		header.add("Export", "default-small").expandX().left();

		DynamicTable content = new DynamicTable(assets.getSkin());

		DynamicTextButton opener = new DynamicTextButton(() -> content.isVisible() ? "-" : "+", assets.getSkin(), "default-small");
		opener.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				content.setVisible(!content.isVisible());
				table.getCell(content).height(content.isVisible() ? content.getPrefHeight() : 0f);
				return true;
			}
		});
		header.add(opener).width(40f);

		table.add(header).fillX().padTop(20f).left().row();

		content.padLeft(20f);

		fillBlue = new CheckBox(" Fill background on export", assets.getSkin(), "default-small");
		fillBlue.setChecked(screen.fillBlue);
		fillBlue.addListener(new ChangeAdapter(() -> screen.fillBlue = fillBlue.isChecked()));
		content.add(fillBlue).left().row();

		table.add(content).expandX().left().row();
	}

	private void makeDeviceSection(AssetSupplier assets, Table table)
	{
		DynamicTable header = new DynamicTable(assets.getSkin());

		header.add("Devices", "default-small").expandX().left();

		DynamicTable content = new DynamicTable(assets.getSkin());

		DynamicTextButton opener = new DynamicTextButton(() -> content.isVisible() ? "-" : "+", assets.getSkin(), "default-small");
		opener.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				content.setVisible(!content.isVisible());
				table.getCell(content).height(content.isVisible() ? content.getPrefHeight() : 0f);
				return true;
			}
		});
		header.add(opener).width(40f);

		table.add(header).fillX().padTop(20f).left().row();

		content.padLeft(20f);

		for(Controller controller : Controllers.getControllers())
			content.add(controller.getName(), "default-small").left().row();

		for(PenDevice pen : screen.colorPicker.jPenWrapper.getDevices())
			content.add(pen.getName(), "default-small").left().row();

		table.add(content).expandX().left().row();
	}

	private void makeHelpSection(AssetSupplier assets, Table table)
	{
		DynamicTable header = new DynamicTable(assets.getSkin());

		header.add("Shortcuts", "default-small").expandX().left();

		DynamicTable content = new DynamicTable(assets.getSkin());

		DynamicTextButton opener = new DynamicTextButton(() -> content.isVisible() ? "-" : "+", assets.getSkin(), "default-small");
		opener.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				content.setVisible(!content.isVisible());
				table.getCell(content).height(content.isVisible() ? content.getPrefHeight() : 0f);
				return true;
			}
		});
		header.add(opener).width(40f);

		table.add(header).fillX().padTop(20f).left().row();

		content.padLeft(20f);

		content.add("CTRL + S saves", "default-tiny").left().row();
		content.add("CTRL + Z undos/redos", "default-tiny").left().row();
		content.add("F5 tests lighting", "default-tiny").left().row();
		content.add("Scroll zooms", "default-tiny").left().row();
		content.add("Scroll + CTRL sizes brush", "default-tiny").left().row();
		content.add("Scroll + SHIFT rotates normal", "default-tiny").left().row();
		content.add("CTRL + Click makes a gradient pin", "default-tiny").left().row();
		content.add("SHIFT + Click copies hovered color", "default-tiny").left().row();
		content.add("Numpad sets basic normal", "default-tiny").left().row();
		content.add("L locks current radius", "default-tiny").left().row();
		content.add("CTRL + N normalizes all colors", "default-tiny").left().row();
		content.add("Delete/Backspace/E sets eraser", "default-tiny").left().row();
		content.add("B sets mode to behind", "default-tiny").left().row();
		content.add("R sets mode to normal", "default-tiny").left().row();
		content.add("F flips normal in X axis", "default-tiny").left().row();
		content.add("U flips normal in Y axis", "default-tiny").left().row();
		content.add("Y toggles invert pin normal", "default-tiny").left().row();
		content.add("M toggles mask multiply", "default-tiny").left().row();
		content.add("T toggles render flat texture", "default-tiny").left().row();

		table.add(content).expandX().left().row();
	}
}

