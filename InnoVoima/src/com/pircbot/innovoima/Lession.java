package com.pircbot.innovoima;

import org.joda.time.DateTime;

public class Lession {
	private String name;
	private String code;
	private DateTime starts;
	private DateTime ends;
	private String day;
	private String studyField;
	private String language;
	private int ops;
	private String teacher;
	private String campus;
	private String programme;
	private String group;
	private DateTime startDate;
	private DateTime endDate;
	private String rooms;
	
	public Lession (String name, String code, String studyField, String language, String teacher,
			String campus, String programme, String group, String rooms, DateTime starts, 
			DateTime ends, DateTime startDate, DateTime endDate,String day, int ops) {
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
		this("","","","","","","","","", new DateTime(), new DateTime(),
				new DateTime(), new DateTime(),"", 0);
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

	public DateTime getStarts() {
		return starts;
	}

	public void setStarts(DateTime starts) {
		this.starts = starts;
	}
	
	public String getDay() {
		return this.day;
	}
	
	public void setDay(String day) {
		this.day = day;
	}

	public DateTime getEnds() {
		return ends;
	}

	public void setEnds(DateTime ends) {
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

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public String getRooms() {
		return rooms;
	}

	public void setRooms(String rooms) {
		this.rooms = rooms;
	}
	@Override
	public String toString() {
		return "name: " + this.name + ", Code: " + this.code + ", room: " +this.rooms + ", For groups:" +
				this.group + ", Classes: " + this.day + " " + this.starts.getHourOfDay() + ":" + 
				this.starts.getMinuteOfHour() + " - " + this.ends.getHourOfDay() + ":" + this.ends.getMinuteOfDay() +
				", period: " + this.startDate.getDayOfMonth() + "." + this.startDate.getMonthOfYear() + "." + this.startDate.getYear() + " - "
				+ this.endDate.getDayOfMonth() + "." + this.endDate.getMonthOfYear() + ", Teacher: " + this.teacher + ", OP: " + this.ops + 
				", Campus: " + this.campus + ", Programme: " + this.programme + ", Study Field: " + this.studyField + ", Language: " + this.language;
	}

}
