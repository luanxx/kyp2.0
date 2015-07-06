package com.science.activity;

import com.example.science.R;
import com.science.adapter.CommonFragmentPagerAdapter;
import com.science.fragment.CoopReleaseSourceFragment;
import com.science.fragment.DocumentExpressFragment;
import com.science.view.MyHeaderView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class CoopReleaseActivity extends FragmentActivity{

	private ViewPager                   coop_release_view_pager;
	private MyHeaderView                    coop_release_header_view;
	private CommonFragmentPagerAdapter  coop_release_frgament_adapter;
	private CoopReleaseSourceFragment   coop_release_source_fragment;
	
     @Override
     public void onCreate(Bundle savedInstanceState){
    	 super.onCreate(savedInstanceState);
    	 setContentView(R.layout.coop_release);
    	 initView();
    	 
     }
     
     public void initView(){
    	 
    	 
    	 
    	 coop_release_header_view = (MyHeaderView) findViewById(R.id.coop_release_header);
    	 coop_release_header_view.SetHeaderText("合作研究");
    	 coop_release_header_view.SetHeaderButtons(new String[]{"发布资源","发布需求"});
    	 coop_release_header_view.SetSelected(0);
    	 
    	 
    	 coop_release_source_fragment = new CoopReleaseSourceFragment();
    	 coop_release_frgament_adapter = new CommonFragmentPagerAdapter(this.getSupportFragmentManager(),
    			                                                        new Fragment[]{coop_release_source_fragment});
    	
    	 coop_release_view_pager = (ViewPager) findViewById(R.id.coop_release_view_pager);
    	 coop_release_view_pager.setAdapter(coop_release_frgament_adapter);
    	 coop_release_view_pager.setCurrentItem(0);
    	 
    	 
//    	 OnClickListener coop_release_tab_listener = new OnClickListener(){
//
// 			@Override
// 			public void onClick(View v) {
// 				// TODO Auto-generated method stub
// 				
// 				doc_view_pager.setCurrentItem(v.getId());
// 				//DocumentExpressFragment last_frag = (DocumentExpressFragment) fragments[curr_fragment_index];
// 				//last_frag.saveDocFragment(frag_saved_data.get(curr_fragment_index));
// 				curr_fragment_index = v.getId();
// 				DocumentExpressFragment frag = (DocumentExpressFragment) fragments[curr_fragment_index];
// 				doc_type = DOC_CHI + curr_fragment_index;
// 				frag.loadDocument(pdate, doc_id, doc_type, current_keyword);
// 				//frag.reLoadDocFragment(frag_saved_data.get(v.getId()));
// 				//frag.updateDocListFragment(doc_id, doc_type, current_keyword);
// 				//frag.reLoadDocFragment();
// 			}
// 			
// 		};
 		
 //		coop_release_header_view.SetOnHeadButtonClickListener(coop_release_tab_listener, 0);
 //		coop_release_header_view.SetOnHeadButtonClickListener(coop_release_tab_listener, 1);
 //        coop_release_header_view.SetSelected(0);
 		
 		
     }
     
		
		
		

}
