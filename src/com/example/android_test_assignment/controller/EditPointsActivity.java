//  1:48   10.03.2013 
package com.example.android_test_assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android_test_assignment.R;
import com.example.android_test_assignment.model.PointData;
import com.example.android_test_assignment.model.db.PointsDAO;

import java.math.BigInteger;

public class EditPointsActivity extends Activity {

	private TextView textViewPoint;
	private TextView textViewNotification;
	private EditText editTextX;
	private EditText editTextY;
	private Button buttonDelete;

	private PointsDAO pointsDAO;
	private Bundle extras;

	private int diagramWidth;
	private int diagramHeight;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_points);

		pointsDAO = new PointsDAO(this);
		pointsDAO.open();

		prepareViews();

		extras = getIntent().getExtras();
		initializeDiagramSize();
		if (editingExistingPoint()) {
			initializeViews();
		} else {
			creatingNewPointSoHideDeleteButton();
		}

		if (savedInstanceState != null) {
			restoreScreenInfo(savedInstanceState);
		}
	}

	private void prepareViews() {
		textViewPoint = (TextView) findViewById(R.id.point_label);
		textViewNotification = (TextView) findViewById(R.id.notification_text);
		editTextX = (EditText) findViewById(R.id.x_edit_text);
		editTextY = (EditText) findViewById(R.id.y_edit_text);
		buttonDelete = (Button) findViewById(R.id.delete_button);
		buttonDelete.setVisibility(View.VISIBLE);
		textViewNotification.setVisibility(View.INVISIBLE);
	}

	private void initializeDiagramSize() {
		if (extras.containsKey(getString(R.string.extras_key_diagram_width))) {
			diagramWidth = extras.getInt(getString(R.string.extras_key_diagram_width));
		}
		if (extras.containsKey(getString(R.string.extras_key_diagram_height))) {
			diagramHeight = extras.getInt(getString(R.string.extras_key_diagram_height));
		}
	}

	private boolean editingExistingPoint() {
		boolean haveExtrasAndPointData = (extras != null)
				&& extras.containsKey(getString(R.string.extras_key_row))
				&& extras.containsKey(getString(R.string.extras_key_point));
		return haveExtrasAndPointData;
	}

	private void initializeViews() {
		int row = extras.getInt(getString(R.string.extras_key_row));
		textViewPoint.append(String.valueOf(row));
		PointData point = (PointData) extras.get(getString(R.string.extras_key_point));
		editTextX.setText(String.valueOf(point.x));
		editTextY.setText(String.valueOf(point.y));
	}

	private void creatingNewPointSoHideDeleteButton() {
		buttonDelete.setVisibility(View.GONE);
	}

	private void restoreScreenInfo(Bundle savedInstanceState) {
		String bundleKeyPoint = getString(R.string.bundle_key_point);
		String bundleKeyX = getString(R.string.bundle_key_x);
		String bundleKeyY = getString(R.string.bundle_key_y);
		textViewPoint.setText(savedInstanceState.getString(bundleKeyPoint));
		editTextX.setText(savedInstanceState.getString(bundleKeyX));
		editTextY.setText(savedInstanceState.getString(bundleKeyY));
	}


	public void onClickSave(View view) {
		if (valuesAreEmpty() || valuesAreIncorrect()) {
			return;
		}
		int xValue = Integer.parseInt(editTextX.getText().toString());
		int yValue = Integer.parseInt(editTextY.getText().toString());
		if (editingExistingPoint()) {
			PointData point = (PointData) extras.get(getString(R.string.extras_key_point));
			pointsDAO.updatePoint(point, xValue, yValue);
		} else {
			pointsDAO.createPoint(xValue, yValue);
		}
		returnToPreviousActivity();
	}

	private boolean valuesAreEmpty() {
		if (editTextX.getText().toString().isEmpty() || editTextY.getText().toString().isEmpty()) {
			Toast.makeText(this, getString(R.string.toast_empty_values), Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}

	private boolean valuesAreIncorrect() {
		BigInteger xValue = new BigInteger(editTextX.getText().toString());
		BigInteger yValue = new BigInteger(editTextY.getText().toString());
		// Center point is at diagramWidth/2 and diagramHeight/2, so assign maxs and mins accordingly
		BigInteger xMax = new BigInteger(String.valueOf(diagramWidth/2));
		BigInteger xMin = new BigInteger(String.valueOf(diagramWidth/2 * -1));
		BigInteger yMax = new BigInteger(String.valueOf(diagramHeight/2));
		BigInteger yMin = new BigInteger(String.valueOf(diagramHeight/2 * -1));
		if (xValue.compareTo(xMax) > 0
				|| xValue.compareTo(xMin) < 0
				|| yValue.compareTo(yMax) > 0
				|| yValue.compareTo(yMin) < 0) {
			String text = String.format(getString(R.string.notification_incorrect_values),
					diagramWidth, diagramHeight,
					Integer.parseInt(xMin.toString()), Integer.parseInt(xMax.toString()),
					Integer.parseInt(yMin.toString()), Integer.parseInt(yMax.toString()));
			textViewNotification.setText(text);
			textViewNotification.setVisibility(View.VISIBLE);
			return true;
		}
		return false;
	}

	public void onClickDelete(View view) {
		PointData point = (PointData) extras.get(getString(R.string.extras_key_point));
		pointsDAO.deletePoint(point);
		returnToPreviousActivity();
	}

	private void returnToPreviousActivity() {
		Intent intent = new Intent();
		intent.setClass(this, ShowPointsActivity.class);
		startActivity(intent);
	}


	@Override
	public void onResume() {
		pointsDAO.open();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(getString(R.string.bundle_key_point), textViewPoint.getText().toString());
		outState.putString(getString(R.string.bundle_key_x), editTextX.getText().toString());
		outState.putString(getString(R.string.bundle_key_y), editTextY.getText().toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		pointsDAO.close();
		super.onPause();
	}


}
