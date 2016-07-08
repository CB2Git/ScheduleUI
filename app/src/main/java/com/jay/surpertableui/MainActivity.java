package com.jay.surpertableui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {
	private ArrayList<CourseBean> mCourse;
	private SharedPreferences mShaerPreferences;
	// 每天有多少节课
	private int mMaxCouese;
	// 一共有多少周
	private int mMaxWeek;
	// 现在是第几周
	private int mNowWeek;
	// 左边一节课的高度
	private float mLeftHeight;
	// 左边一节课的宽度
	private float mLeftWidth;

	private TextView mChangeWeek;
	private LinearLayout mLeftNo;
	private LinearLayout mMonday;
	private LinearLayout mTuesday;
	private LinearLayout mWednesday;
	private LinearLayout mThursday;
	private LinearLayout mFirday;
	private LinearLayout mSaturday;
	private LinearLayout mWeekend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supertable);
		// 实例化所有对象
		initCtrl();
		// 初始化数据
		initData();

		// 绘制左边的课程节数
		drawLeftNo();
		// 绘制当前周
		drawNowWeek();
		// 绘制所有课程 其实可以使用redrawAll替代三步
		drawAllCourse();
	}

	/**
	 * 实例化所有对象
	 */
	private void initCtrl() {
		mChangeWeek = (TextView) findViewById(R.id.changeWeek);
		mLeftNo = (LinearLayout) findViewById(R.id.leftNo);
		mMonday = (LinearLayout) findViewById(R.id.monday);
		mTuesday = (LinearLayout) findViewById(R.id.tuesday);
		mWednesday = (LinearLayout) findViewById(R.id.wednesday);
		mThursday = (LinearLayout) findViewById(R.id.thursday);
		mFirday = (LinearLayout) findViewById(R.id.firday);
		mSaturday = (LinearLayout) findViewById(R.id.saturday);
		mWeekend = (LinearLayout) findViewById(R.id.weekend);
	}

	/**
	 * 初始化所有数据
	 */
	private void initData() {
		// 初始化课表
		praseJson();
		// 读取配置信息
		readIniFile();

		// 点击选择切换周
		mChangeWeek.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChangeWeekDlg(v);
			}
		});
	}

	/**
	 * 绘制左边的课程节数
	 */
	private void drawLeftNo() {
		mLeftHeight = getResources().getDimension(R.dimen.left_height);
		mLeftWidth = getResources().getDimension(R.dimen.left_width);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				(int) mLeftWidth, (int) mLeftHeight);
		for (int i = 1; i <= mMaxCouese; i++) {
			TextView tv = new TextView(this);
			tv.setText(i + "");
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(getResources().getColor(R.color.font));
			tv.setBackgroundResource(R.drawable.boder);
			mLeftNo.addView(tv, lp);
		}
	}

	/**
	 * 绘制课表
	 * 
	 * @param ll
	 *            绘制课表到哪一个LinearLayout上
	 * @param dayOfWeek
	 *            绘制的数据来自周几 一二三四五六七
	 */
	private void drawCourse(LinearLayout ll, char dayOfWeek) {
		// 删除所有子View
		ll.removeAllViews();
		// 上一节课结束是第几节
		int perCourse = -1;
		for (CourseBean course : mCourse) {
			// 判断是否显示这节课
			// 是不是同一天 是不是这一周
			if (course.getDayOfWeek() != dayOfWeek
					|| !course.inThisWeek(mNowWeek))
				continue;

			// 设置TextView的属性样式
			TextView tv = new TextView(this);
			tv.setText(course.getCourse_name() + "\n@"
					+ course.getCourse_address());
			tv.setBackgroundResource(R.drawable.course);
			tv.setTextColor(getResources().getColor(R.color.course_font_color));

			// 将数据绑定到TextView上
			tv.setTag(course);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CourseBean tag = (CourseBean) v.getTag();
					showCouseDetails(tag);
				}
			});

			// 设置TextView的位置
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					(int) (course.getStep() * mLeftHeight));
			// 说明这节课为第一节课
			if (perCourse == -1) {
				lp.setMargins(1,
						(int) ((course.getMinCourse() - 1) * mLeftHeight), 1, 0);
				// useHeight = (int) ((course.getMaxCourse()-1) * mLeftHeight);
			} else {
				lp.setMargins(1, (course.getMinCourse() - perCourse - 1)
						* (int) mLeftHeight, 1, 0);
				// useHeight = useHeight + (course.getMaxCourse() - perCourse -
				// 1)* (int) mLeftHeight;
			}
			perCourse = course.getMaxCourse();
			ll.addView(tv, lp);
		}
	}

	/**
	 * 绘制当前周
	 */
	private void drawNowWeek() {
		mChangeWeek.setText("第" + mNowWeek + "周");
	}

	/**
	 * 重新绘制所有，不包括标题栏和星期几 在修改每天的节数后调用
	 */
	/*private void redrawAll() {
		drawLeftNo();
		drawNowWeek();
		drawAllCourse();
	}*/

	/**
	 * 绘制课程，用于周数切换以后
	 */
	private void drawAllCourse() {
		drawCourse(mMonday, '一');
		drawCourse(mTuesday, '二');
		drawCourse(mWednesday, '三');
		drawCourse(mThursday, '四');
		drawCourse(mFirday, '五');
		drawCourse(mSaturday, '六');
		drawCourse(mWeekend, '日');
	}

	/**
	 * 读取配置信息
	 */
	private void readIniFile() {
		mShaerPreferences = getSharedPreferences("iniFile",
				Context.MODE_PRIVATE);
		mMaxCouese = mShaerPreferences.getInt("mMaxCouese", -1);
		mMaxWeek = mShaerPreferences.getInt("mMaxWeek", -1);
		mNowWeek = mShaerPreferences.getInt("mNowWeek", -1);

		Editor edit = mShaerPreferences.edit();
		// 默认12节课
		if (mMaxCouese == -1) {
			edit.putInt("mMaxCouese", 12);
		}

		// 默认20周
		if (mMaxWeek == -1) {
			edit.putInt("mMaxWeek", 20);
		}

		// 默认第一周
		if (mNowWeek == -1) {
			edit.putInt("mNowWeek", 1);
		}
		edit.commit();
	}

	/**
	 * 解析来自strings.xml里面的Json课表数据
	 */
	private void praseJson() {
		String json = getResources().getString(R.string.kb);
		Gson gson = new Gson();
		mCourse = gson.fromJson(json, new TypeToken<ArrayList<CourseBean>>() {
		}.getType());
	}

	/**
	 * 弹出窗口，显示课程详细信息
	 * 
	 * @param bean
	 */
	public void showCouseDetails(CourseBean bean) {
		AlertDialog.Builder builder = new Builder(this);
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setContentView(R.layout.details_layout);
		TextView textView = (TextView) dialog.findViewById(R.id.name);
		textView.setText(bean.getCourse_name());
		textView = (TextView) dialog.findViewById(R.id.type);
		textView.setText(bean.getCourse_type());
		textView = (TextView) dialog.findViewById(R.id.teacher);
		textView.setText(bean.getCourse_teacher());
		textView = (TextView) dialog.findViewById(R.id.address);
		textView.setText(bean.getCourse_address());
		textView = (TextView) dialog.findViewById(R.id.week);
		textView.setText(bean.getCourse_week());
	}

	/**
	 * 显示切换当前周的窗口
	 */
	public void showChangeWeekDlg(View v) {
		View view = View.inflate(this, R.layout.changweek_layout, null);
		ListView weekList = (ListView) view.findViewById(R.id.weekList);

		ArrayList<String> strList = new ArrayList<String>();
		for (int i = 1; i < mMaxWeek; i++) {
			strList.add("第" + i + "周");
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item, strList);

		weekList.setAdapter(adapter);
		view.measure(0, 0);
		final PopupWindow pop = new PopupWindow(view, 300, 500, true);
		pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
		int xOffSet = -(pop.getWidth() - v.getWidth()) / 2;
		pop.showAsDropDown(v, xOffSet, 0);

		weekList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int positon, long id) {
				mNowWeek = positon + 1;
				pop.dismiss();
				drawNowWeek();
				drawAllCourse();
			}
		});
	}

}
