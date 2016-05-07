package com.tifaniwarnita.metsky.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tifaniwarnita.metsky.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tifani on 4/7/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<List<String>> _listDataHeader; // header titles and icons
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<List<String>> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(0).get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this._listDataChild.get(this._listDataHeader.get(0).get(groupPosition)) != null)
            return this._listDataChild.get(this._listDataHeader.get(0).get(groupPosition))
                .size();
        else return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(0).get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.get(0).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        String headerIcon = this._listDataHeader.get(1).get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        ImageView iconListHeader = (ImageView) convertView
                .findViewById(R.id.iconListHeader);
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        ImageView iconDown = (ImageView) convertView
                .findViewById(R.id.arrow_bottom);

        Context context = iconListHeader.getContext();
        int id = context.getResources().getIdentifier(headerIcon,
                "drawable", context.getPackageName());
        iconListHeader.setImageResource(id);

        lblListHeader.setText(headerTitle);

        if (getChildrenCount(groupPosition) > 0)
            iconDown.setVisibility(View.VISIBLE);
        else
            iconDown.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
