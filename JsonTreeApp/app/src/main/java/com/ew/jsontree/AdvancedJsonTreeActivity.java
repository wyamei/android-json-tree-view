package com.ew.jsontree;

import android.app.Activity;
import android.os.Bundle;
import android.widget.HorizontalScrollView;

import com.ew.jsontree.utils.JsonFormatUtils;
import com.ew.jsontree.view.AdvancedJsonTreeView;
import com.ew.jsontree.view.JsonTreeView;

import java.util.LinkedHashMap;

public class AdvancedJsonTreeActivity extends Activity{

    private HorizontalScrollView llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_tree);
        llRoot = (HorizontalScrollView) this.findViewById(R.id.ll_root_container);

        String json = "{\n" +
                "  \"bstatus\": {\n" +
                "    \"code\": 0,\n" +
                "    \"des\": \"查询成功!\"\n" +
                "  },\n" +
                "  \"data\": {\n" +
                "    \"allFilters\": [\n" +
                "      {\n" +
                "        \"details\": [\n" +
                "          {\n" +
                "            \"detailId\": \"timeArea\",\n" +
                "            \"detailItems\": [\n" +
                "              {\n" +
                "                \"clearOthers\": false,\n" +
                "                \"detailItemId\": \"depTime\",\n" +
                "                \"detailItemTitle\": \"\",\n" +
                "                \"isDefaultItem\": false,\n" +
                "                \"locked\": false,\n" +
                "                \"selected\": false,\n" +
                "                \"timeArea\": true,\n" +
                "                \"value\": \"00:00;24:00\"\n" +
                "              }\n" +
                "            ],\n" +
                "            \"detailTitle\": \"\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"filterId\": \"time\",\n" +
                "        \"filterTitle\": \"起飞时段\"\n" +
                "      }\n" +
                "     \n" +
                "    ]\n" +
                "  }\n" +
                "}";

        LinkedHashMap<String, Object> map = JsonFormatUtils.jsonToMapKeeped(json);

        AdvancedJsonTreeView view = new AdvancedJsonTreeView(this, map);
        llRoot.addView(view);
    }

}
