package com.jay.surpertableui;

import android.util.Log;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 课程描述类，包含课程的基本信息
 * 
 * @author Jay
 * 
 */
public class CourseBean implements Serializable {

	private static final long serialVersionUID = -2080529292063641408L;
	private String Course_name; // 课程名
	private String Course_address; // 上课地点
	private String Course_teacher; // 课程老师
	private String Course_type; // 课程种类 专业/专选/选修/实验
	private String Course_week; // 课程周数

	public CourseBean() {

	}

	public CourseBean(String course_name, String course_address,
			String course_teacher, String course_type, String course_week) {
		Course_name = course_name;
		Course_address = course_address;
		Course_teacher = course_teacher;
		Course_type = course_type;
		Course_week = course_week;
	}

	@Override
	public String toString() {
		return "课程信息 [课程名=" + Course_name + ", 上课地点=" + Course_address
				+ ", 课程老师=" + Course_teacher + ", 课程种类=" + Course_type
				+ ", 课程周数=" + Course_week + "]";
	}

	//courses_name=算法设计与分析, courses_type=专选, courses_time=1-9(5,6), courses_address=J2-208, courses_id=0, courses_year=0, courses_object=计算机类Q1541
	/**
	 * 
	 * 周二第1,2节{第11-19周} 获取这节课是星期几
	 * 
	 * @return 返回值为一二三四五六日
	 */
	public char getDayOfWeek() {
		return getCourse_week().charAt(1);
	}

	/**
	 * 周二第1,2节{第11-19周}
	 * 
	 * @return 返回值为这节课从第几节课开始
	 */
	public int getMinCourse() {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(getCourse_week());
		matcher.find();
		return Integer.parseInt(matcher.group(0));
	}

	/**
	 * 周二第1,2节{第11-19周}
	 * 
	 * @return 返回值为这节课从在几节课结束
	 */
	public int getMaxCourse() {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(getCourse_week());
		matcher.find();
		matcher.find();
		return Integer.parseInt(matcher.group());
	}

	/**
	 * @return 返回课程的长度
	 */
	public int getStep() {
		return getMaxCourse() - getMinCourse() + 1;
	}

	/**
	 * 获取本节课程从第几周开始
	 * 
	 * 周五第1,2节{第19-19周|单周}
	 * 
	 * @return 开始周
	 */
	public int getMinWeek() {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(getCourse_week());
		matcher.find();
		matcher.find();
		matcher.find();
		return Integer.parseInt(matcher.group());
	}

	/**
	 * 获取本节课程在第几周结束
	 * 
	 * 周五第1,2节{第19-19周|单周}
	 * 
	 * @return 结束周
	 */
	public int getMaxWeek() {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(getCourse_week());
		matcher.find();
		matcher.find();
		matcher.find();
		matcher.find();
		return Integer.parseInt(matcher.group());

	}

	/**
	 * 判断是否为单周
	 * 
	 * 周五第1,2节{第19-19周|单周}
	 * 
	 * @return true 如果为单周
	 */
	public Boolean isSingleWeek() {
		Boolean result = false;
		String week = getCourse_week();
		int begin = week.indexOf("|");
		if (begin != -1) {
			Character isWeek = week.charAt(begin + 1);
			Log.v("xxxx", isWeek + "");
			if (isWeek.equals('单')) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 判断是否为双周
	 * 
	 * 周五第1,2节{第19-19周|单周}
	 * 
	 * @return true 如果为双周
	 */
	public Boolean isDoubleWeek() {
		Boolean result = false;
		String week = getCourse_week();
		int begin = week.indexOf("|");
		if (begin != -1) {
			Character isWeek = week.charAt(begin + 1);
			if (isWeek.equals('双')) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 判断是否为单双周一起上
	 */
	public Boolean isAllWeek() {
		return !isSingleWeek() && !isDoubleWeek();
	}

	/**
	 * 判断本节课是否在指定周上
	 * 
	 * @param week
	 *            指定周
	 * @return ture 是
	 */
	public Boolean inThisWeek(int week) {
		Boolean result = false;
		// 在最大最小周之间
		if (week <= this.getMaxWeek() && week >= this.getMinWeek()) {
			if (this.isAllWeek())
				result = true;
			if (this.isSingleWeek() && week % 2 == 1) {
				result = true;
			}
			if (this.isDoubleWeek() && week % 2 == 0) {
				result = true;
			}
		}
		return result;
	}

	/*
	 * // 获取节数 public int getTime() { Pattern pattern = Pattern.compile("\\d");
	 * Matcher matcher = pattern.matcher(getCourse_week()); matcher.find();
	 * String result = matcher.group(); int r = -1; if (result != null) { r =
	 * Integer.parseInt(result); } return r; }
	 */

	public String getCourse_name() {
		return Course_name;
	}

	public void setCourse_name(String course_name) {
		Course_name = course_name;
	}

	public String getCourse_address() {
		return Course_address;
	}

	public void setCourse_address(String course_address) {
		Course_address = course_address;
	}

	public String getCourse_teacher() {
		return Course_teacher;
	}

	public void setCourse_teacher(String course_teacher) {
		Course_teacher = course_teacher;
	}

	public String getCourse_type() {
		return Course_type;
	}

	public void setCourse_type(String course_type) {
		Course_type = course_type;
	}

	public String getCourse_week() {
		return Course_week;
	}

	public void setCourse_week(String course_week) {
		Course_week = course_week;
	}

}
