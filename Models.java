package ie.ul.timetable.models;

import java.util.ArrayList;
import java.util.List;

class User {
    private String userId;
    private String name;
    private Enums.UserRole role;
    private String password;
    private String programmeYear;

    public User(String userId, String name, Enums.UserRole role, String password, String programmeYear) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.password = password;
        this.programmeYear = programmeYear;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public Enums.UserRole getRole() { return role; }
    public String getPassword() { return password; }
    public String getProgrammeYear() { return programmeYear; }

    public void setProgrammeYear(String programmeYear) { this.programmeYear = programmeYear; }
}

class Room {
    private String roomId;
    private String name;
    private Enums.RoomType roomType;
    private int capacity;

    public Room(String roomId, String name, Enums.RoomType roomType, int capacity) {
        this.roomId = roomId;
        this.name = name;
        this.roomType = roomType;
        this.capacity = capacity;
    }

    public String getRoomId() { return roomId; }
    public String getName() { return name; }
    public Enums.RoomType getRoomType() { return roomType; }
    public int getCapacity() { return capacity; }
}

class Module {
    private String code;
    private String name;
    private int lectureHours;
    private int tutorialHours;
    private int labHours;
    private List<String> lecturerIds = new ArrayList<>();

    public Module(String code, String name, int lectureHours, int tutorialHours, int labHours) {
        this.code = code;
        this.name = name;
        this.lectureHours = lectureHours;
        this.tutorialHours = tutorialHours;
        this.labHours = labHours;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getLectureHours() { return lectureHours; }
    public int getTutorialHours() { return tutorialHours; }
    public int getLabHours() { return labHours; }
    public List<String> getLecturerIds() { return lecturerIds; }
}

class Programme {
    private String code;
    private String name;
    private int years;

    public Programme(String code, String name, int years) {
        this.code = code;
        this.name = name;
        this.years = years;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getYears() { return years; }
}

class ProgrammeModule {
    private String programmeCode;
    private int year;
    private int semester;
    private String moduleCode;

    public ProgrammeModule(String programmeCode, int year, int semester, String moduleCode) {
        this.programmeCode = programmeCode;
        this.year = year;
        this.semester = semester;
        this.moduleCode = moduleCode;
    }

    public String getProgrammeCode() { return programmeCode; }
    public int getYear() { return year; }
    public int getSemester() { return semester; }
    public String getModuleCode() { return moduleCode; }
}

class StudentGroup {
    private String groupId;
    private String programmeCode;
    private int year;
    private int size;

    public StudentGroup(String groupId, String programmeCode, int year, int size) {
        this.groupId = groupId;
        this.programmeCode = programmeCode;
        this.year = year;
        this.size = size;
    }

    public String getGroupId() { return groupId; }
    public String getProgrammeCode() { return programmeCode; }
    public int getYear() { return year; }
    public int getSize() { return size; }
}

class Subgroup {
    private String subgroupId;
    private String parentGroupId;
    private int size;

    public Subgroup(String subgroupId, String parentGroupId, int size) {
        this.subgroupId = subgroupId;
        this.parentGroupId = parentGroupId;
        this.size = size;
    }

    public String getSubgroupId() { return subgroupId; }
    public String getParentGroupId() { return parentGroupId; }
    public int getSize() { return size; }
}

class TimeSlot {
    private String day;
    private String startTime;
    private String endTime;

    public TimeSlot(String day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    @Override
    public String toString() {
        return day + " " + startTime + "-" + endTime;
    }
}

class TimetableEntry {
    private String entryId;
    private String moduleCode;
    private Enums.SessionType sessionType;
    private String groupId;
    private String roomId;
    private String lecturerId;
    private TimeSlot timeSlot;

    public TimetableEntry(String entryId, String moduleCode, Enums.SessionType sessionType,
                          String groupId, String roomId, String lecturerId, TimeSlot timeSlot) {
        this.entryId = entryId;
        this.moduleCode = moduleCode;
        this.sessionType = sessionType;
        this.groupId = groupId;
        this.roomId = roomId;
        this.lecturerId = lecturerId;
        this.timeSlot = timeSlot;
    }

    public String getEntryId() { return entryId; }
    public String getModuleCode() { return moduleCode; }
    public Enums.SessionType getSessionType() { return sessionType; }
    public String getGroupId() { return groupId; }
    public String getRoomId() { return roomId; }
    public String getLecturerId() { return lecturerId; }
    public TimeSlot getTimeSlot() { return timeSlot; }

    public boolean conflictsWith(TimetableEntry other) {
        if (!this.timeSlot.getDay().equals(other.timeSlot.getDay())) {
            return false;
        }
        String s1 = this.timeSlot.getStartTime();
        String e1 = this.timeSlot.getEndTime();
        String s2 = other.timeSlot.getStartTime();
        String e2 = other.timeSlot.getEndTime();

        return s1.compareTo(e2) < 0 && s2.compareTo(e1) < 0;
    }
}
