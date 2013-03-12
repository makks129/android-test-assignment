package com.example.android_test_assignment.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.android_test_assignment.R;
import com.example.android_test_assignment.model.PointData;
import com.example.android_test_assignment.model.db.PointsDAO;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import com.example.android_test_assignment.view.DiagramView;

import java.util.List;

public class ShowPointsActivity extends Activity {

	private ListView pointsList;
	private DiagramView diagramView;

	private PointsDAO pointsDAO;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_points);

		pointsDAO = new PointsDAO(this);
		pointsDAO.open();

		initializeTabs();
		prepareViews();
		initializeList();
		passPointsForDiagram();

		pointsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PointData point = (PointData) parent.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.setClass(ShowPointsActivity.this, EditPointsActivity.class);
				intent.putExtra(getString(R.string.extras_key_row), position);
				intent.putExtra(getString(R.string.extras_key_point), point);
				intent.putExtra(getString(R.string.extras_key_diagram_width), diagramView.getWidth());
				intent.putExtra(getString(R.string.extras_key_diagram_height), diagramView.getHeight());
				startActivity(intent);
			}
		});
	}

	private void initializeTabs() {
		TabHost tabs = (TabHost) findViewById(R.id.tabHost);
		tabs.setup();
		TabHost.TabSpec spec = tabs.newTabSpec(getString(R.string.tab_tag_points));
		spec.setContent(R.id.tab_points_list);
		spec.setIndicator(getString(R.string.tab_name_points));
		tabs.addTab(spec);
		spec = tabs.newTabSpec(getString(R.string.tab_tag_diagram));
		spec.setContent(R.id.tab_diagram);
		spec.setIndicator(getString(R.string.tab_name_diagram));
		tabs.addTab(spec);
		tabs.setCurrentTab(0);
	}

	private void prepareViews() {
		pointsList = (ListView) findViewById(R.id.points_list);
		diagramView = (DiagramView) findViewById(R.id.diagram_view);
		registerForContextMenu(pointsList);
	}

	private void initializeList() {
		List<PointData> points = pointsDAO.getAllPoints();
		ArrayAdapter<PointData> adapter = new ArrayAdapter<PointData>(this,
				android.R.layout.simple_list_item_1, points);
		pointsList.setAdapter(adapter);
	}

	private void passPointsForDiagram() {
		List<PointData> points = pointsDAO.getAllPoints();
		// Convert list to array that is required for drawing
		// List has x and y in each element but the array must be {x1, y1, x2, y2,...}
		float[] pointsToDraw = new float[points.size() * 2];
		for (int i = 0, j = 0; i < pointsToDraw.length; i++, j++) {
			pointsToDraw[i] = (float) points.get(j).x;
			pointsToDraw[++i] = (float) points.get(j).y;
		}
		diagramView.setPointsSource(pointsToDraw);
	}

	public void onClickAddPoint(View view) {
		Intent intent = new Intent();
		intent.setClass(this, EditPointsActivity.class);
		intent.putExtra(getString(R.string.extras_key_diagram_width), diagramView.getWidth());
		intent.putExtra(getString(R.string.extras_key_diagram_height), diagramView.getHeight());
		startActivity(intent);
	}

	public void onClickDeleteAllButton(View view) {
		pointsDAO.deleteAllPoints();
		initializeList();
		float[] emptyPointsSource = {};
		diagramView.setPointsSource(emptyPointsSource);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, Menu.NONE, Menu.NONE, getString(R.string.context_menu_item_delete_point));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.context_menu_item_delete_point))) {
			AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			PointData point = (PointData) pointsList.getAdapter().getItem(menuInfo.position);
			pointsDAO.deletePoint(point);
			initializeList();
			passPointsForDiagram();
		} else {
			return false;
		}
		return true;
	}

	@Override
	protected void onResume() {
		pointsDAO.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		pointsDAO.close();
		super.onPause();
	}


}
