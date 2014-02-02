package com.pircbot.innovoima;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Lession {
	private String name;
	private String code;
	private Calendar starts;
	private Calendar ends;
	private String day;
	private String studyField;
	private String language;
	private int ops;
	private String teacher;
	private String campus;
	private String programme;
	private String group;
	private Calendar startDate;
	private Calendar endDate;
	private String rooms;
	
	public Lession (String name, String code, String studyField, String language, String teacher,
			String campus, String programme, String group, String rooms, Calendar starts, 
			Calendar ends, Calendar startDate, Calendar endDate,String day, int ops) {
		this.name = name;
		this.code = code;
		this.starts = starts;
		this.ends = ends;
		this.studyField = studyField;
		this.language = language;
		this.ops = ops;
		this.teacher = teacher;
		this.campus = campus;
		this.programme = programme;
		this.group = group;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rooms = rooms;
		this.day = day;
	}
	
	public Lession () {
		this("","","","","","","","","", new GregorianCalendar(), new GregorianCalendar(),
				new GregorianCalendar(), new GregorianCalendar(),"", 0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Calendar getStarts() {
		return starts;
	}

	public void setStarts(Calendar starts) {
		this.starts = starts;
	}
	
	public String getDay() {
		return this.day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}

	public Calendar getEnds() {
		return ends;
	}

	public void setEnds(Calendar ends) {
		this.ends = ends;
	}

	public String getStudyField() {
		return studyField;
	}

	public void setStudyField(String studyField) {
		this.studyField = studyField;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getOps() {
		return ops;
	}

	public void setOps(int ops) {
		this.ops = ops;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public String getRooms() {
		return rooms;
	}

	public void setRooms(String rooms) {
		this.rooms = rooms;
	}

}
