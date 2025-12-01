// Business logic layer - handles timetabling operations without directly accessing data
package ie.ul.timetable.services;

import ie.ul.timetable.models.*;
import ie.ul.timetable.models.Enums.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TimetableService {
    private DataManager dataManager;
    
    // Constructor - initializes with data manager for accessing stored data
    public TimetableService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    // Gets all timetable entries for a specific student
    public List<TimetableEntry> getStudentTimetable(String userId) {
        return dataManager.getStudentTimetable(userId);
    }
    
    // Gets all timetable entries for a specific lecturer
    public List<TimetableEntry> getLecturerTimetable(String lecturerId) {
        return dataManager.getLecturerTimetable(lecturerId);
    }
    
    // Gets all sessions scheduled in a specific room
    public List<TimetableEntry> getRoomTimetable(String roomId) {
        return dataManager.getRoomTimetable(roomId);
    }
    
    // Gets all sessions for a specific module
    public List<TimetableEntry> getModuleTimetable(String moduleCode) {
        return dataManager.getModuleTimetable(moduleCode);
    }
    
    // Gets all sessions for students in a specific programme year
    public List<TimetableEntry> getProgrammeTimetable(String programmeCode, int year) {
        return dataManager.getProgrammeTimetable(programmeCode, year);
    }
    
    // Finds a single timetable entry by its ID
    public TimetableEntry getEntryById(String entryId) {
        for (TimetableEntry entry : dataManager.getTimetableEntries()) {
            if (entry.getEntryId().equals(entryId)) {
                return entry;
            }
        }
        return null;
    }
    
    // Searches timetable entries by module, room, lecturer, or group
    public List<TimetableEntry> searchEntries(String searchTerm) {
        String term = searchTerm.toLowerCase();
        List<TimetableEntry> results = new ArrayList<>();
        
        for (TimetableEntry entry : dataManager.getTimetableEntries()) {
            if (entry.getModuleCode().toLowerCase().contains(term) ||
                entry.getRoomId().toLowerCase().contains(term) ||
                entry.getLecturerId().toLowerCase().contains(term) ||
                entry.getGroupId().toLowerCase().contains(term)) {
                results.add(entry);
            }
        }
        
        return results;
    }
    
    // Adds a new timetable entry with validation
    public DataManager.ValidationResult addEntry(TimetableEntry entry) throws IOException {
        return dataManager.addTimetableEntry(entry);
    }
    
    // Removes a timetable entry by ID
    public boolean removeEntry(String entryId) throws IOException {
        return dataManager.removeTimetableEntry(entryId);
    }
    
    // Gets all modules taught in a specific programme year
    public List<Module> getModulesForProgrammeYear(String programmeCode, int year) {
        List<Module> modules = new ArrayList<>();
        
        for (ProgrammeModule pm : dataManager.getProgrammeModules()) {
            if (pm.getProgrammeCode().equals(programmeCode) && pm.getYear() == year) {
                Module module = dataManager.getModules().get(pm.getModuleCode());
                if (module != null) {
                    modules.add(module);
                }
            }
        }
        
        return modules;
    }
    
    // Gets all modules taught by a specific lecturer
    public List<Module> getModulesByLecturer(String lecturerId) {
        List<Module> modules = new ArrayList<>();
        
        for (Module module : dataManager.getModules().values()) {
            if (module.getLecturerIds().contains(lecturerId)) {
                modules.add(module);
            }
        }
        
        return modules;
    }
}

// Generates reports and analytics about timetable usage
class ReportService {
    private DataManager dataManager;
    
    // Constructor - initializes with data manager
    public ReportService(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    // Shows which rooms are occupied at what times on a given day
    public Map<String, List<String>> getRoomAvailabilityForDay(String day) {
        Map<String, List<String>> availability = new LinkedHashMap<>();
        
        // Initialize all rooms with empty lists
        for (Room room : dataManager.getRooms().values()) {
            availability.put(room.getRoomId() + " - " + room.getName(), new ArrayList<>());
        }
        
        // Add occupied time slots to each room
        for (TimetableEntry entry : dataManager.getTimetableEntries()) {
            if (entry.getTimeSlot().getDay().equalsIgnoreCase(day)) {
                Room room = dataManager.getRooms().get(entry.getRoomId());
                if (room != null) {
                    String key = room.getRoomId() + " - " + room.getName();
                    String timeSlot = entry.getTimeSlot().getStartTime() + "-" + 
                                    entry.getTimeSlot().getEndTime() + 
                                    " (" + entry.getModuleCode() + ")";
                    availability.get(key).add(timeSlot);
                }
            }
        }
        
        return availability;
    }
    
    // Calculates what percentage of time each room is being used
    public Map<String, Double> calculateRoomUtilization() {
        Map<String, Double> utilization = new HashMap<>();
        
        // Total hours available per week (5 days × 10 hours)
        final double TOTAL_HOURS_PER_WEEK = 50.0;
        
        for (Room room : dataManager.getRooms().values()) {
            double hoursUsed = 0.0;
            
            // Sum up all hours the room is booked
            for (TimetableEntry entry : dataManager.getTimetableEntries()) {
                if (entry.getRoomId().equals(room.getRoomId())) {
                    hoursUsed += calculateDuration(
                        entry.getTimeSlot().getStartTime(),
                        entry.getTimeSlot().getEndTime()
                    );
                }
            }
            
            // Calculate percentage
            double utilizationPercent = (hoursUsed / TOTAL_HOURS_PER_WEEK) * 100.0;
            utilization.put(room.getRoomId(), utilizationPercent);
        }
        
        return utilization;
    }
    
    // Calculates total teaching hours for each lecturer
    public Map<String, Integer> calculateLecturerWorkload() {
        Map<String, Integer> workload = new HashMap<>();
        
        for (User user : dataManager.getUsers().values()) {
            if (user.getRole() == UserRole.LECTURER) {
                int totalHours = 0;
                
                // Sum up all teaching hours for this lecturer
                for (TimetableEntry entry : dataManager.getTimetableEntries()) {
                    if (entry.getLecturerId().equals(user.getUserId())) {
                        totalHours += calculateDuration(
                            entry.getTimeSlot().getStartTime(),
                            entry.getTimeSlot().getEndTime()
                        );
                    }
                }
                
                workload.put(user.getUserId(), totalHours);
            }
        }
        
        return workload;
    }
    
    // Finds all scheduling conflicts (double bookings)
    public List<String> findAllConflicts() {
        List<String> conflicts = new ArrayList<>();
        List<TimetableEntry> entries = dataManager.getTimetableEntries();
        
        // Compare every entry with every other entry
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                TimetableEntry entry1 = entries.get(i);
                TimetableEntry entry2 = entries.get(j);
                
                if (entry1.conflictsWith(entry2)) {
                    // Check if same room is double-booked
                    if (entry1.getRoomId().equals(entry2.getRoomId())) {
                        conflicts.add(String.format(
                            "Room conflict: %s is double-booked on %s at %s-%s (Entries: %s, %s)",
                            entry1.getRoomId(),
                            entry1.getTimeSlot().getDay(),
                            entry1.getTimeSlot().getStartTime(),
                            entry1.getTimeSlot().getEndTime(),
                            entry1.getEntryId(),
                            entry2.getEntryId()
                        ));
                    }
                    
                    // Check if lecturer is teaching two classes at once
                    if (entry1.getLecturerId().equals(entry2.getLecturerId())) {
                        conflicts.add(String.format(
                            "Lecturer conflict: %s is double-booked on %s at %s-%s (Entries: %s, %s)",
                            entry1.getLecturerId(),
                            entry1.getTimeSlot().getDay(),
                            entry1.getTimeSlot().getStartTime(),
                            entry1.getTimeSlot().getEndTime(),
                            entry1.getEntryId(),
                            entry2.getEntryId()
                        ));
                    }
                    
                    // Check if student group has two classes at once
                    if (entry1.getGroupId().equals(entry2.getGroupId())) {
                        conflicts.add(String.format(
                            "Group conflict: %s is double-booked on %s at %s-%s (Entries: %s, %s)",
                            entry1.getGroupId(),
                            entry1.getTimeSlot().getDay(),
                            entry1.getTimeSlot().getStartTime(),
                            entry1.getTimeSlot().getEndTime(),
                            entry1.getEntryId(),
                            entry2.getEntryId()
                        ));
                    }
                }
            }
        }
        
        return conflicts;
    }
    
    // Helper method - calculates duration in hours between two times
    private int calculateDuration(String startTime, String endTime) {
        try {
            String[] startParts = startTime.split(":");
            String[] endParts = endTime.split(":");
            
            int startMinutes = Integer.parseInt(startParts[0]) * 60 + Integer.parseInt(startParts[1]);
            int endMinutes = Integer.parseInt(endParts[0]) * 60 + Integer.parseInt(endParts[1]);
            
            return (endMinutes - startMinutes) / 60;
        } catch (Exception e) {
            return 0;
        }
    }
}