package com.ew.jsontree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ew.jsontree.R;
import com.ew.jsontree.utils.Constants;

/**
 * Created by WYM on 2016/7/30.
 */
public class TreeItemView extends LinearLayout {

    private LinearLayout rootLayout;
    private Button btnExpend;
    private TextView tvNodeKey;
    private TextView tvNodeSize;
    private TextView tvColon;
    private TextView tvNodeValue;
    private LinearLayout llChildContainer;

    public TreeItemView(Context context,int type) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_json_tree_item, this, true);
        this.setOrientation(VERTICAL);

        if(type== Constants.TREE_LAYOUT_TYPE_DEFAULT){
            initViewOfDefault();
        }else if(type==Constants.TREE_LAYOUT_TYPE_OPTIMIZE){
            initViewOfOptimize();
        }
    }

    public TreeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化布局
     */
    public void initViewOfDefault(){
        rootLayout = (LinearLayout)getRootView();
        btnExpend = (Button)this.findViewById(R.id.ew_btn_expend);
        tvNodeKey= (TextView)this.findViewById(R.id.ew_tv_node_key);
        tvNodeSize= (TextView)this.findViewById(R.id.ew_tv_node_size);
        tvColon= (TextView)this.findViewById(R.id.ew_tv_colon);
        tvNodeValue= (TextView)this.findViewById(R.id.ew_tv_node_value);
        llChildContainer= (LinearLayout)this.findViewById(R.id.ew_ll_child_node_container);

        btnExpend.setText(llChildContainer.getVisibility()==VISIBLE ? "-" : "+");
        btnExpend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnExpend.setText(llChildContainer.getVisibility() == VISIBLE ? "+" : "-");
                llChildContainer.setVisibility(llChildContainer.getVisibility()==VISIBLE ? GONE : VISIBLE);
            }
        });
    }

    /**
     * 对layout的层级进行了优化
     * 可以支持27层级的json结构
     */
    public void initViewOfOptimize(){
        rootLayout = (LinearLayout)getRootView();
        btnExpend = (Button)this.findViewById(R.id.ew_btn_expend);
        tvNodeKey= (TextView)this.findViewById(R.id.ew_tv_node_key);
        tvNodeSize= (TextView)this.findViewById(R.id.ew_tv_node_size);
        tvColon= (TextView)this.findViewById(R.id.ew_tv_colon);
        tvNodeValue= (TextView)this.findViewById(R.id.ew_tv_node_value);
        llChildContainer= (LinearLayout)this.findViewById(R.id.ew_ll_child_node_container);
        rootLayout.removeView(llChildContainer);

        View childView = rootLayout.getChildAt(1);
        btnExpend.setText(childView!=null && childView.getVisibility()==VISIBLE ? "-" : "+");
        btnExpend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View childView = rootLayout.getChildAt(1);
                if(childView!=null) {   
                    btnExpend.setText(childView.getVisibility() == VISIBLE ? "+" : "-");
                    childView.setVisibility(childView.getVisibility() == VISIBLE ? GONE : VISIBLE);
                }
            }
        });
    }

    public void setData(String key, String size, Object value,String btnText,Boolean isVirtualNode, Boolean isExpend){
        tvNodeKey.setText(key);
        if(size!=null && !size.isEmpty()){
            tvNodeSize.setText(size);
            tvNodeSize.setVisibility(VISIBLE);
        }
        if(value!=null) {
            tvNodeValue.setText(size);
            tvNodeValue.setVisibility(VISIBLE);
            if (value instanceof String) {
                tvNodeValue.setTextColor(0xff008000);
            } else if (value instanceof Integer || value instanceof Double || value instanceof Long) {
                tvNodeValue.setTextColor(0xffff8c30);
            } else if (value instanceof Boolean) {
                tvNodeValue.setTextColor(0xff3883FA);
            }
        }
        if(btnText!=null && !btnText.isEmpty()){
            btnExpend.setText(btnText);
        }
        if(isVirtualNode){
            tvNodeKey.setTextColor(0xff7F8081);
        }
        View childView = rootLayout.getChildAt(1);
        if(childView!=null){
            childView.setVisibility(isExpend?VISIBLE:GONE);
        }
    }

    public LinearLayout getChildContainer(){
        return llChildContainer;
    }
}
