package com.ew.jsontree.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ew.jsontree.R;
import com.ew.jsontree.utils.Constants;
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
        try {
        rootContainer = new LinearLayout(getContext());
        rootContainer.setLayoutParams(new LayoutParams(-1,-1));
        rootContainer.setOrientation(VERTICAL);
        rootContainer.setBackgroundColor(Color.WHITE);
        addView(rootContainer);
            createTreeViewOfKeeepedOfOptimize(rootContainer, (LinkedHashMap<String, Object>) jsonMap);
        }catch (Throwable e){  //如何捕获StackOverflowError
            //参考： http://www.jb51.net/article/81352.htm
            Log.e("error","栈溢出");
            Toast.makeText(getContext(),"层级过多不支持",Toast.LENGTH_SHORT).show();
        }
    }

    public JsonTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void createTreeViewOfKeeepedOfOptimize(LinearLayout rootView, LinkedHashMap<String, Object> jsonMap, Boolean... isVirtualNode){
        level+=1;
        for(LinkedHashMap.Entry<String, Object> entry:jsonMap.entrySet()){
            TreeItemView view = new TreeItemView(getContext(), Constants.TREE_LAYOUT_TYPE_OPTIMIZE);
            LinearLayout.LayoutParams lp = new LayoutParams(-2,-2);
            lp.setMargins(50,0,0,0);
            rootView.addView(view,lp);
            String size = null,value = null,btnText = null;
            boolean isExpend = level<=4?true:false;
            String key = entry.getKey();
            Object obj = entry.getValue();
            if(obj instanceof JSONObjectKeeped){
                LinkedHashMap<String,Object> map = ((JSONObjectKeeped)obj).getMap();
                size = "{" + String.valueOf(map.size()) + "}";
                if(map.size()==0){
                    btnText = "";
                    isExpend = false;
                }
                view.setData(key,size,value,btnText,isVirtualNode!=null && isVirtualNode.length==1 &&isVirtualNode[0],isExpend);
                //创建子节点的tree
                createTreeViewOfKeeepedOfOptimize(view, map);
            } else if(obj instanceof org.json.JSONArray){
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                size = "[" + String.valueOf(jsonArray.length()) + "]";
                if(jsonArray.length()==0){
                    btnText = "";
                    isExpend = false;
                }
                view.setData(key,size,value,btnText,isVirtualNode!=null && isVirtualNode.length==1 &&isVirtualNode[0],isExpend);
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
                        createTreeViewOfKeeepedOfOptimize(view, map, true);
                    }
                }
            } else{  //基础数据类型
                value = String.valueOf(obj);
                view.setData(key,size,value,btnText,isVirtualNode!=null && isVirtualNode.length==1 &&isVirtualNode[0],isExpend);
            }
        }
        level-=1;
    }


    /**
     * 创建的json tree view中节点顺序与输入的json string保持一致
     * @param rootView
     * @param jsonMap
     */
    public void createTreeViewOfKeeeped(LinearLayout rootView, LinkedHashMap<String, Object> jsonMap, Boolean... isVirtualNode){
        level+=1;
        for(LinkedHashMap.Entry<String, Object> entry:jsonMap.entrySet()){
            TreeItemView view = new TreeItemView(getContext(),Constants.TREE_LAYOUT_TYPE_DEFAULT);
            rootView.addView(view);
            String size = null,value = null,btnText = null;
            boolean isExpend = level<=4?true:false;
            String key = entry.getKey();
            Object obj = entry.getValue();
            if(obj instanceof JSONObjectKeeped){
                LinkedHashMap<String,Object> map = ((JSONObjectKeeped)obj).getMap();
                size = "{" + String.valueOf(map.size()) + "}";
                if(map.size()==0){
                    btnText = "";
                    isExpend = false;
                }
                view.setData(key,size,value,btnText,isVirtualNode!=null && isVirtualNode.length==1 &&isVirtualNode[0],isExpend);
                //创建子节点的tree
                createTreeViewOfKeeeped(view.getChildContainer(), map);
            } else if(obj instanceof org.json.JSONArray){
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                size = "[" + String.valueOf(jsonArray.length()) + "]";
                if(jsonArray.length()==0){
                    btnText = "";
                    isExpend = false;
                }
                view.setData(key,size,value,btnText,isVirtualNode!=null && isVirtualNode.length==1 &&isVirtualNode[0],isExpend);
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
                        createTreeViewOfKeeeped(view.getChildContainer(), map, true);
                    }
                }
            } else{  //基础数据类型
                value = String.valueOf(obj);
                view.setData(key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend);
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.json_tree_item, null);
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
                Map<String,Object> map =  (Map)JSON.toJSON(entry.getValue());
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
