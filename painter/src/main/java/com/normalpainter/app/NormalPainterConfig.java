package com.normalpainter.app;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Configuration for {@link NormalPainterApp}
 * <p>
 * Created on 2020-12-21.
 *
 * @author Alexander Winter
 */
public class NormalPainterConfig
{
	private Conf conf;

	public void load(File file)
	{
		try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file))))
		{
			conf = (Conf)ois.readObject();
		}
		catch(IOException | ClassCastException | ClassNotFoundException ex)
		{
			System.out.println("Failed to read config");
			ex.printStackTrace();
			conf = new Conf();
		}
	}

	public void save(File file)
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file))))
		{
			oos.writeObject(conf);
		}
		catch(IOException ex)
		{
			System.out.println("Failed to write config");
			ex.printStackTrace();
		}
	}

	public void apply(NormalPainterScreen screen)
	{
		if(conf == null)
			throw new IllegalStateException("Config not loaded");

		screen.painter.brushOpacity = conf.brushOpacity;
		screen.painter.brushSize = conf.brushSize;
		screen.painter.enablePressure = conf.enablePressure;
		screen.painter.maxPressure = conf.maxPressure;
		screen.painter.hardness = conf.hardness;
		screen.painter.drawMode = conf.drawMode;
		screen.painter.equalizeWeight = conf.equalizeWeight;
		screen.painter.maskMultiply = conf.multiplyRef;

		screen.colorPicker.disableStickOrPen = conf.disableStickOrPen;
		screen.colorPicker.normalRotateSpeed = conf.normalRotateSpeed;
		screen.colorPicker.maxTilt = conf.maxTilt;
		screen.colorPicker.minRadius = conf.minRadius;
		screen.colorPicker.maxRadius = conf.maxRadius;
		screen.colorPicker.radiusStep = conf.radiusStep;
		screen.colorPicker.invertPinning = conf.inside;
		screen.colorPicker.normalRelToPath = conf.normalRelToPath;
		screen.colorPicker.angle = conf.angle;
		screen.colorPicker.numpadIntensity = conf.numpadIntensity;
		screen.colorPicker.invert = conf.invert;

		screen.colorPicker.joystickSwapXY = conf.joystickSwapXY;
		screen.colorPicker.joystickInvertX = conf.joystickInvertX;
		screen.colorPicker.joystickInvertY = conf.joystickInvertY;
		screen.colorPicker.joystickRadius = conf.joystickRadius;

		screen.renderFlat = conf.renderFlat;
		screen.maskOpacity = conf.maskOpacity;
		screen.flatOpacity = conf.flatOpacity;
		screen.maskOnTop = conf.maskOnTop;
		screen.flatOnTop = conf.flatOnTop;
		screen.viewNormals = conf.viewNormals;
		screen.normalSpacing = conf.normalSpacing;
		screen.livePreview = conf.livePreview;
		screen.drawLinesOnDrag = conf.drawLinesOnDrag;

		screen.fillBlue = conf.fillBlue;

		screen.stage.flatFile.setText(conf.flatFile);
		screen.stage.maskFile.setText(conf.refFile);
		screen.stage.inputFile.setText(conf.inputFile);
		screen.stage.outputFile.setText(conf.outputFile);

		screen.stage.update();
	}

	public void setFrom(NormalPainterScreen screen)
	{
		if(conf == null)
			conf = new Conf();

		conf.brushOpacity = screen.painter.brushOpacity;
		conf.brushSize = screen.painter.brushSize;
		conf.enablePressure = screen.painter.enablePressure;
		conf.maxPressure = screen.painter.maxPressure;
		conf.hardness = screen.painter.hardness;
		conf.drawMode = screen.painter.drawMode;
		conf.equalizeWeight = screen.painter.equalizeWeight;
		conf.multiplyRef = screen.painter.maskMultiply;

		conf.disableStickOrPen = screen.colorPicker.disableStickOrPen;
		conf.normalRotateSpeed = screen.colorPicker.normalRotateSpeed;
		conf.maxTilt = screen.colorPicker.maxTilt;
		conf.minRadius = screen.colorPicker.minRadius;
		conf.maxRadius = screen.colorPicker.maxRadius;
		conf.radiusStep = screen.colorPicker.radiusStep;
		conf.inside = screen.colorPicker.invertPinning;
		conf.normalRelToPath = screen.colorPicker.normalRelToPath;
		conf.angle = screen.colorPicker.angle;
		conf.numpadIntensity = screen.colorPicker.numpadIntensity;
		conf.invert = screen.colorPicker.invert;

		conf.joystickSwapXY = screen.colorPicker.joystickSwapXY;
		conf.joystickInvertX = screen.colorPicker.joystickInvertX;
		conf.joystickInvertY = screen.colorPicker.joystickInvertY;
		conf.joystickRadius = screen.colorPicker.joystickRadius;

		conf.renderFlat = screen.renderFlat;
		conf.maskOpacity = screen.maskOpacity;
		conf.flatOpacity = screen.flatOpacity;
		conf.maskOnTop = screen.maskOnTop;
		conf.flatOnTop = screen.flatOnTop;
		conf.viewNormals = screen.viewNormals;
		conf.normalSpacing = screen.normalSpacing;
		conf.livePreview = screen.livePreview;
		conf.drawLinesOnDrag = screen.drawLinesOnDrag;

		conf.fillBlue = screen.fillBlue;

		conf.flatFile = screen.stage.flatFile.getText();
		conf.refFile = screen.stage.maskFile.getText();
		conf.inputFile = screen.stage.inputFile.getText();
		conf.outputFile = screen.stage.outputFile.getText();
	}

	private static class Conf implements Serializable
	{
		public float brushOpacity = 1f;
		public int brushSize = 5;
		public boolean enablePressure = false;
		public float maxPressure = 0.85f;
		public float hardness = 0.75f;
		public DrawMode drawMode = DrawMode.Normal;
		public float equalizeWeight = 0.5f;
		public boolean multiplyRef = true;

		public boolean disableStickOrPen = false;
		public float normalRotateSpeed = 1f;
		public float maxTilt = 20f;
		public float minRadius = 0f, maxRadius = 1f;
		public float radiusStep = 0f;
		public boolean inside = false;
		public boolean normalRelToPath = false;
		public float angle = 90f;
		public float numpadIntensity = 0.75f;
		public boolean invert = false;

		public boolean joystickSwapXY = false;
		public boolean joystickInvertX = false;
		public boolean joystickInvertY = false;
		public float joystickRadius = 1f;

		public boolean renderFlat = false;
		public float maskOpacity = 0.75f, flatOpacity = 0.75f;
		public boolean maskOnTop = false, flatOnTop = false;
		public boolean viewNormals = false;
		public int normalSpacing = 5;
		public boolean livePreview = true;
		public boolean drawLinesOnDrag = true;

		public boolean fillBlue = true;

		public String flatFile = "", refFile = "", inputFile = "", outputFile = "output.png";
	}
}
