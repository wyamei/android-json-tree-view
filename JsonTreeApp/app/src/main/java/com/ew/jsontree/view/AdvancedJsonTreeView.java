package com.ew.jsontree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ew.jsontree.R;

import java.util.LinkedHashMap;

/**
 * json tree view的改进版本
 * 支持的json层级没有限制，理论上支持无限层级
 * Created by WYM on 2016/8/1.
 */
public class AdvancedJsonTreeView extends LinearLayout {
    public AdvancedJsonTreeView(Context context) {
        super(context);
    }

    public AdvancedJsonTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void createTreeViewOfKeepedOptimize(final LinearLayout rootView, LinkedHashMap<String, Object> jsonMap, Boolean... isVirtualNode) {
        level += 1;
        for (LinkedHashMap.Entry<String, Object> entry : jsonMap.entrySet()) {
            final View view = LayoutInflater.from(getContext()).inflate(R.layout.atom_flight_cfg_json_tree_item_optimize, null);
            final LinearLayout llContainer = (LinearLayout) view.findViewById(R.id.atom_flight_ll_parent_node_container);
            llContainer.setTag(level);
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.setMargins(BitmapHelper.dip2px((level - 1) * 40), BitmapHelper.dip2px(5), 0, 0);
            rootView.addView(llContainer, lp);
            TextView tvNodeKey = (TextView) view.findViewById(R.id.atom_flight_tv_node_key);
            TextView tvNodeSize = (TextView) view.findViewById(R.id.atom_flight_tv_node_size);
            TextView tvColon = (TextView) view.findViewById(R.id.atom_flight_tv_colon);
            TextView tvNodeValue = (TextView) view.findViewById(R.id.atom_flight_tv_node_value);
            final Button btnExpend = (Button) view.findViewById(R.id.atom_flight_btn_expend);
            if (level > 4) {
                btnExpend.setText("+");
            }
            btnExpend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isExpend = !isExpend;
                    btnExpend.setText(btnExpend.getText().equals("-") ? "+" : "-");

                    int currtLevelTag = Integer.valueOf(llContainer.getTag().toString());
                    boolean startSearchChildNodes = false;
                    //查找当前节点的子节点，然后设置其visibility
                    for (int j = 0; j < rootView.getChildCount(); j++) {
                        View childView = rootView.getChildAt(j);
                        if (startSearchChildNodes) {            //开始查找其子节点
                            int levelTag = Integer.valueOf(childView.getTag().toString());
                            if (levelTag > currtLevelTag) {       //子节点开始
                                if (btnExpend.getText().equals("-")) {
                                    LinearLayout currtParent = (LinearLayout) rootView.getChildAt(getParentNodeIndex(rootView, levelTag, j));//找到其父节点，根据其父节点btn判断是否展开
                                    Button btn = (Button) currtParent.getChildAt(0);
                                    childView.setVisibility(btn.getText().equals("-") ? VISIBLE : GONE);
                                } else {
                                    childView.setVisibility(GONE);
                                }
                            } else {                            //子节点结束
                                break;
                            }
                        } else {
                            if (childView.equals(llContainer)) {  //找到当前节点，下一个节点则为其子节点
                                startSearchChildNodes = true;
                            }
                        }
                    }
                }
            });
            tvNodeKey.setText(entry.getKey());
            if (isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0]) {  //若是虚拟节点则设置灰色
                tvNodeKey.setTextColor(0xff7F8081);
            }
            Object obj = entry.getValue();
            if (obj instanceof JSONObjectKeeped) {
                LinkedHashMap<String, Object> map = ((JSONObjectKeeped) obj).getMap();
                tvNodeSize.setText("{" + String.valueOf(map.size()) + "}");
                tvNodeSize.setVisibility(VISIBLE);
                if (map.size() == 0) {
                    btnExpend.setEnabled(false);
                    btnExpend.setText("");
                }
                //创建子节点的tree
                createTreeViewOfKeepedOptimize(rootContainer, map);
            } else if (obj instanceof org.json.JSONArray) {
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                tvNodeSize.setText("[" + String.valueOf(jsonArray.length()) + "]");
                tvNodeSize.setVisibility(VISIBLE);
                if (jsonArray.length() == 0) {
                    btnExpend.setEnabled(false);
                    btnExpend.setText("");
                }
                for (int i = 0; i < (jsonArray).length(); i++) {
                    Object each = null;
                    try {
                        each = jsonArray.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (each != null) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        map.put(String.valueOf(i), each);
                        //创建子节点的tree
                        createTreeViewOfKeepedOptimize(rootContainer, map, true);
                    }
                }
            } else {  //基础数据类型
                btnExpend.setVisibility(GONE);
                tvColon.setVisibility(VISIBLE);
                tvNodeValue.setText(String.valueOf(obj));
                tvNodeValue.setVisibility(VISIBLE);
                //根据数据类型来设置value的颜色
                if (obj instanceof String) {
                    tvNodeValue.setTextColor(0xff008000);
                } else if (obj instanceof Integer || obj instanceof Double || obj instanceof Long) {
                    tvNodeValue.setTextColor(0xffff8c30);
                } else if (obj instanceof Boolean) {
                    tvNodeValue.setTextColor(0xff3883FA);
                }
            }
        }
        level -= 1;
    }

    public int getParentNodeIndex(LinearLayout rootView,int currtLevel,int currtIndex){
        for(int i = currtIndex;i>=0;i--){
            if(Integer.valueOf(rootView.getChildAt(i).getTag().toString())<currtLevel){
                return i;
            }
        }
        return 0;
    }
}
