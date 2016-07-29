package com.ew.jsontree.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ew.jsontree.R;
import com.ew.jsontree.utils.JSONObjectKeeped;


import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by WYM on 2016/7/23.
 */
public class JsonTreeView extends LinearLayout {
    private LinearLayout rootContainer;
    private int level=0;
    private boolean isExpend=true;

    public JsonTreeView(Context context) {
        super(context);
    }

    public JsonTreeView(Context context,Map<String,Object> jsonMap) {
        super(context);
        rootContainer = new LinearLayout(getContext());
        rootContainer.setLayoutParams(new LayoutParams(-1,-1));
        rootContainer.setOrientation(VERTICAL);
        rootContainer.setBackgroundColor(Color.WHITE);
        addView(rootContainer);
//        createTreeView(rootContainer, jsonMap);
//        createTreeViewOfKeepedOptimize(rootContainer, (LinkedHashMap<String, Object>) jsonMap);
    }

    public JsonTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 创建的json tree view中节点顺序与输入的json string保持一致
     * @param rootView
     * @param jsonMap
     */
    public void createTreeViewOfKeeeped(LinearLayout rootView,LinkedHashMap<String,Object> jsonMap,Boolean ...isVirtualNode){
        level+=1;
        for(LinkedHashMap.Entry<String, Object> entry:jsonMap.entrySet()){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.atom_flight_cfg_json_tree_item, null);
            rootView.addView(view);
            TextView tvNodeKey = (TextView)view.findViewById(R.id.atom_flight_tv_node_key);
            TextView tvNodeSize = (TextView)view.findViewById(R.id.atom_flight_tv_node_size);
            TextView tvColon = (TextView)view.findViewById(R.id.atom_flight_tv_colon);
            TextView tvNodeValue = (TextView)view.findViewById(R.id.atom_flight_tv_node_value);
            final LinearLayout llChildContainer = (LinearLayout)view.findViewById(R.id.atom_flight_ll_child_node_container);
            if(level>4){
                llChildContainer.setVisibility(GONE);
            }
            final Button btnExpend = (Button)view.findViewById(R.id.atom_flight_btn_expend);
            btnExpend.setText(llChildContainer.getVisibility()==VISIBLE ? "-" : "+");
            btnExpend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnExpend.setText(llChildContainer.getVisibility() == VISIBLE ? "+" : "-");
                    llChildContainer.setVisibility(llChildContainer.getVisibility()==VISIBLE ? GONE : VISIBLE);
                }
            });
            tvNodeKey.setText(entry.getKey());
            if(isVirtualNode!=null && isVirtualNode.length==1 && isVirtualNode[0]){  //若是虚拟节点则设置灰色
                tvNodeKey.setTextColor(0xff7F8081);
            }
            Object obj = entry.getValue();
            if(obj instanceof JSONObjectKeeped){
                LinkedHashMap<String,Object> map = ((JSONObjectKeeped)obj).getMap();
                tvNodeSize.setText("{" + String.valueOf(map.size()) + "}");
                tvNodeSize.setVisibility(VISIBLE);
                if(map.size()==0){
                    btnExpend.setEnabled(false);
                    btnExpend.setText("");
                    llChildContainer.setVisibility(GONE);
                }
                //创建子节点的tree
                createTreeViewOfKeeeped(llChildContainer, map);
            } else if(obj instanceof org.json.JSONArray){
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                tvNodeSize.setText("[" + String.valueOf(jsonArray.length()) + "]");
                tvNodeSize.setVisibility(VISIBLE);
                if(jsonArray.length()==0){
                    btnExpend.setEnabled(false);
                    btnExpend.setText("");
                    llChildContainer.setVisibility(GONE);
                }
                for(int i=0;i<(jsonArray).length();i++){
                    Object each = null;
                    try {
                        each = jsonArray.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(each!=null){
                        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
                        map.put(String.valueOf(i),each);
                        //创建子节点的tree
                        createTreeViewOfKeeeped(llChildContainer,map,true);
                    }
                }
            } else{  //基础数据类型
                btnExpend.setVisibility(GONE);
                tvColon.setVisibility(VISIBLE);
                tvNodeValue.setText(String.valueOf(obj));
                tvNodeValue.setVisibility(VISIBLE);
                //根据数据类型来设置value的颜色
                if(obj instanceof String){
                    tvNodeValue.setTextColor(0xff008000);
                }else if(obj instanceof Integer || obj instanceof Double || obj instanceof Long){
                    tvNodeValue.setTextColor(0xffff8c30);
                }else if(obj instanceof Boolean){
                    tvNodeValue.setTextColor(0xff3883FA);
                }
            }
        }
        level-=1;
    }

    /**
     * 创建json tree view 其中节点顺序不保持
     * 适用于fastJson
     * @param rootView
     * @param jsonMap 使用的是fastJson提供的方法Map<String,Object> map = (Map<String,Object>) JSON.parse(json);
     */
    public void createTreeView(LinearLayout rootView,Map<String,Object> jsonMap){
        level+=1;
        for(Map.Entry<String, Object> entry:jsonMap.entrySet()){
            View view = LayoutInflater.from(getContext()).inflate(R.layout.atom_flight_cfg_json_tree_item, null);
            rootView.addView(view);
            TextView tvNodeKey = (TextView)view.findViewById(R.id.atom_flight_tv_node_key);
            TextView tvNodeSize = (TextView)view.findViewById(R.id.atom_flight_tv_node_size);
            TextView tvNodeValue = (TextView)view.findViewById(R.id.atom_flight_tv_node_value);
            final LinearLayout llChildContainer = (LinearLayout)view.findViewById(R.id.atom_flight_ll_child_node_container);
            if(level>4){
                llChildContainer.setVisibility(GONE);
            }
            final Button btnExpend = (Button)view.findViewById(R.id.atom_flight_btn_expend);
            btnExpend.setText(llChildContainer.getVisibility()==VISIBLE ? "-" : "+");
            btnExpend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnExpend.setText(llChildContainer.getVisibility() == VISIBLE ? "+" : "-");
                    llChildContainer.setVisibility(llChildContainer.getVisibility()==VISIBLE ? GONE : VISIBLE);
                }
            });
            Object obj = entry.getValue();
            if(obj.getClass().toString().equals("class com.alibaba.fastjson.JSONObject")){//array怎么处理
                Map<String,Object> map = JsonUtils.fromBean(entry.getValue());
                tvNodeKey.setText(entry.getKey());
                tvNodeSize.setText("{" + String.valueOf(map.size()) + "}");
                tvNodeSize.setVisibility(VISIBLE);
                if(map.size()==0){
                    btnExpend.setEnabled(false);
                    btnExpend.setText("");
                    llChildContainer.setVisibility(GONE);
                }
                //创建子节点的tree
                createTreeView(llChildContainer,map);
            }
            if(obj instanceof JSONArray){
                JSONArray jsonArray = (JSONArray) obj;
                tvNodeKey.setText(entry.getKey());
                tvNodeSize.setText("[" + String.valueOf(jsonArray.size()) + "]");
                tvNodeSize.setVisibility(VISIBLE);
                if(jsonArray.size()==0){
                    btnExpend.setEnabled(false);
                    btnExpend.setText("");
                    llChildContainer.setVisibility(GONE);
                }
                for(int i=0;i<(jsonArray).size();i++){
                    Object each = jsonArray.get(i);
                    if(each instanceof JSONObject){
                        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
                        map.put(String.valueOf(i),each);
                        //创建子节点的tree
                        createTreeView(llChildContainer,map);
                    }
                }

            }
            if(obj instanceof String || obj instanceof Integer || obj instanceof Boolean){
                btnExpend.setVisibility(GONE);
                tvNodeKey.setText(entry.getKey());
                tvNodeValue.setText(":  " + String.valueOf(obj));
                tvNodeValue.setVisibility(VISIBLE);
            }
        }
    }
}
