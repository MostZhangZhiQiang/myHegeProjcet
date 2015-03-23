package com.hege.pts;

import java.util.ArrayList;
import java.util.List;

import com.hege.pts.fragment.CasesTabFragment;
import com.hege.pts.fragment.GroupPhotoTabFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProjectCaseActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mDatas;
	
	private TextView mCaseTextView;
	private TextView mGroupPhotoTextView;
	
	private ImageView mTabline;
	private int mScreen1_2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_case);
		setTitle("Project Cases");
		initTabLine();
		initView();
		addViewBack();
		addBottomListener();
	}
	
	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProjectCaseActivity.this.finish();// 结束当前activity以返回上一级
			}
		});
	}
    private void addBottomListener() {
    	Button  go_home = (Button) this.findViewById(R.id.go_home);
		go_home.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProjectCaseActivity.this,MainActivity.class);
				ProjectCaseActivity.this.finish();
				startActivity(intent);
			}
		});
		Button  go_products = (Button) this.findViewById(R.id.go_products);
		go_products.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProjectCaseActivity.this,ProductsActivity.class);
				startActivity(intent);
			}
		});
	}
	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}

	private void initTabLine()
	{
		mTabline = (ImageView) findViewById(R.id.id_iv_tabline);
		DisplayMetrics outMetrics = this.getResources().getDisplayMetrics();
		mScreen1_2 = outMetrics.widthPixels / 2;
		LayoutParams lp = mTabline.getLayoutParams();
		lp.width = mScreen1_2;
		mTabline.setLayoutParams(lp);
	}
	
	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mCaseTextView = (TextView) findViewById(R.id.id_tv_case);
		mGroupPhotoTextView = (TextView) findViewById(R.id.id_tv_group_photo);

		mDatas = new ArrayList<Fragment>();

		CasesTabFragment tab01 = new CasesTabFragment();
		GroupPhotoTabFragment tab02 = new GroupPhotoTabFragment();

		mDatas.add(tab01);
		mDatas.add(tab02);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mDatas.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mDatas.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				resetTextView();
				switch (position)
				{
				case 0:
					mCaseTextView.setTextColor(Color.parseColor("#0e447c"));
					break;
				case 1:
					mGroupPhotoTextView.setTextColor(Color.parseColor("#0e447c"));
					break;

				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPx)
			{
				
				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
						.getLayoutParams();
				lp.leftMargin = (int) (mScreen1_2*positionOffset+position*mScreen1_2);
				mTabline.setLayoutParams(lp);

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub

			}
		});

	}

	protected void resetTextView()
	{
		mCaseTextView.setTextColor(Color.parseColor("#55000000"));
		mGroupPhotoTextView.setTextColor(Color.parseColor("#55000000"));
	}
}
