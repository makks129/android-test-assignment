//  15:44   11.03.2013 
package com.example.android_test_assignment.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class DiagramView extends View {

	private float[] pointsSource;

	public DiagramView(Context context) {
		super(context);
	}

	public DiagramView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DiagramView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public float[] getPointsSource() {
		return pointsSource;
	}

	public void setPointsSource(float[] pointsSource) {
		this.pointsSource = pointsSource;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// Make a copy of points because points will be converted to actual pixels
		float[] points = Arrays.copyOf(pointsSource, pointsSource.length);

		// Reversing Y
		for (int i = 1; i < points.length; i += 2) {
			points[i] *= -1;
		}
		// Shifting all points from upper-left corner to center
		for (int i = 0; i < points.length - 1; i += 2) {
			points[i] += getWidth() / 2;
			points[i + 1] += getHeight() / 2;
		}

		// Drawing text
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		for (int i = 0; i < points.length - 3; i += 2) {
			// Using pointsSource because we need actual values for texts
			String firstPointText = "(" + (int)pointsSource[i] + ", " + (int)pointsSource[i + 1] + ")";
			String secondPointText = "(" + (int)pointsSource[i + 2] + ", " + (int)pointsSource[i + 3] + ")";
			canvas.drawText(firstPointText, points[i], points[i + 1], paint);
			canvas.drawText(secondPointText, points[i + 2], points[i + 3], paint);
		}

		// Drawing lines
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(2.0f);
		for (int i = 0; i < points.length - 3; i += 2) {
			canvas.drawLine(points[i], points[i + 1], points[i + 2], points[i + 3], paint);
		}

		// Drawing points
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(5.0f);
		canvas.drawPoints(points, paint);

		// Drawing coordinate axis
		paint.setStrokeWidth(1.0f);
		canvas.drawText("Y", getWidth() / 2 + 5, 15, paint);
		canvas.drawText("X", getWidth() - 15, getHeight() / 2 - 5, paint);
		canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
		canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
	}
}
