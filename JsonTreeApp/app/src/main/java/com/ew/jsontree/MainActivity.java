package com.ew.jsontree;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ew.jsontree.utils.JsonFormatUtils;
import com.ew.jsontree.view.JsonTreeView;

import java.util.LinkedHashMap;

public class MainActivity extends Activity {

    private HorizontalScrollView llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llRoot = (HorizontalScrollView) this.findViewById(R.id.ll_root_container);

        String json = "{\n" +
                "  \"tip\": {\n" +
                "    \"tip\": {\n" +
                "      \"tip\": {\n" +
                "        \"tip\": {\n" +
                "          \"tip\": {\n" +
                "            \"tip\": {\n" +
                "              \"tip\": {\n" +
                "                \"tip\": {\n" +
                "                  \"tip\": {\n" +
                "                    \"tip\": {\n" +
                "                      \"tip\": {\n" +
                "                        \"tip\": {\n" +
                "                          \"tip\": {\n" +
                "                            \"tip\": {\n" +
                "                              \"tip\": {\n" +
                "                                \"tip\": {\n" +
                "                                  \"tip\": {\n" +
                "                                    \"tip\": {\n" +
                "                                      \"tip\": {\n" +
                "                                        \"tip\": {\n" +
                "                                          \"tip\": {\n" +
                "                                            \"tip\": {\n" +
                "                                              \"tip\": {\n" +
                "                                                \"tip\": {\n" +
                "                                                  \"tip\": {\n" +
                "                                                        \"tip\": {\n" +
                "                                                           \"tip\": \"请填写json数据\"\n" +
                "                                                        }\n" +
                "                                                    }\n" +
                "                                                      \n" +
                "                                                    }\n" +
                "                                                  }\n" +
                "                                                }\n" +
                "                                              }\n" +
                "                                            }\n" +
                "                                          }\n" +
                "                                        }\n" +
                "                                      }\n" +
                "                                    }\n" +
                "                                  }\n" +
                "                                }\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        }\n" +
                "                      }\n" +
                "                    }\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  \n";

        LinkedHashMap<String, Object> map = JsonFormatUtils.jsonToMapKeeped(json);
        JsonTreeView view = new JsonTreeView(this, map);
        llRoot.addView(view);
    }
}
