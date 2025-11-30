/**
 * Controller for the Timetabling System
 */
package ie.ul.timetable.controllers;

import ie.ul.timetable.models.*;
import ie.ul.timetable.models.Enums.*;
import ie.ul.timetable.views.CLIView;
import java.util.List;
import java.util.Map;

public class TimetableController {
    private DataManager dataManager;
    private CLIView view;
    private User currentUser;
    
    public TimetableController(DataManager dataManager, CLIView view) {
        this.dataManager = dataManager;
        this.view = view;
        this.currentUser = null;
    }
    
    public void run() {
        view.displayWelcome();
        
        while (true) {
            if (currentUser == null) {
                if (!login()) {
                    continue;
                }
            }
            
            showMenu();
        }
    }
    
    private boolean login() {
        String[] credentials = view.displayLoginPrompt();
        String userId = credentials[0];
        String password = credentials[1];
        
        User user = dataManager.authenticateUser(userId, password);
        if (user != null) {
            currentUser = user;
            return true;
        } else {
            view.displayError("Invalid credentials. Please try again.");
            return false;
        }
    }
    
    private void logout() {
        view.displayInfo("Goodbye, " + currentUser.getName() + "!");
        currentUser = null;
    }
    
    private void showMenu() {
        view.displayMainMenu(currentUser);
        String choice = view.getMenuChoice();
        
        if (currentUser.getRole() == UserRole.STUDENT) {
            handleStudentMenu(choice);
        } else if (currentUser.getRole() == UserRole.LECTURER) {
            handleLecturerMenu(choice);
        } else if (currentUser.getRole() == UserRole.ADMIN) {
            handleAdminMenu(choice);
        }
    }
    
    private void handleStudentMenu(String choice) {
        switch (choice) {
            case "1":
                viewMyTimetable();
                break;
            case "2":
                viewModuleTimetable();
                break;
            case "3":
                viewProgrammeTimetable();
                break;
            case "4":
                logout();
                return;
            default:
                view.displayError("Invalid choice. Please try again.");
        }
        
        if (!choice.equals("4")) {
            view.pause();
        }
    }
    
    private void handleLecturerMenu(String choice) {
        switch (choice) {
            case "1":
                viewMyTimetable();
                break;
            case "2":
                viewModuleTimetable();
                break;
            case "3":
                viewRoomTimetable();
                break;
            case "4":
                logout();
                return;
            default:
                view.displayError("Invalid choice. Please try again.");
        }
        
        if (!choice.equals("4")) {
            view.pause();
        }
    }
    
    private void handleAdminMenu(String choice) {
        switch (choice) {
            case "1":
                viewStudentTimetableAdmin();
                break;
            case "2":
                viewLecturerTimetableAdmin();
                break;
            case "3":
                viewModuleTimetable();
                break;
            case "4":
                viewProgrammeTimetable();
                break;
            case "5":
                viewRoomTimetable();
                break;
            case "6":
                addTimetableEntry();
                break;
            case "7":
                removeTimetableEntry();
                break;
            case "8":
                logout();
                return;
            default:
                view.displayError("Invalid choice. Please try again.");
        }
        
        if (!choice.equals("8")) {
            view.pause();
        }
    }
    
    private void viewMyTimetable() {
        List<TimetableEntry> entries;
        String title;
        
        if (currentUser.getRole() == UserRole.STUDENT) {
            entries = dataManager.getStudentTimetable(currentUser.getUserId());
            title = "My Timetable (" + currentUser.getName() + ")";
        } else if (currentUser.getRole() == UserRole.LECTURER) {
            entries = dataManager.getLecturerTimetable(currentUser.getUserId());
            title = "My Timetable (" + currentUser.getName() + ")";
        } else {
            view.displayError("This option is not available for your role.");
            return;
        }
        
        view.displayTimetableWithDetails(entries, dataManager.getModules(), dataManager.getRooms(), title);
    }
    
    private void viewStudentTimetableAdmin() {
        String studentId = view.getInput("Enter Student ID");
        
        User student = dataManager.getUsers().get(studentId);
        if (student == null || student.getRole() != UserRole.STUDENT) {
            view.displayError("Student not found.");
            return;
        }
        
        List<TimetableEntry> entries = dataManager.getStudentTimetable(studentId);
        String title = "Timetable for " + student.getName() + " (" + studentId + ")";
        
        view.displayTimetableWithDetails(entries, dataManager.getModules(), dataManager.getRooms(), title);
    }
    
    private void viewLecturerTimetableAdmin() {
        String lecturerId = view.getInput("Enter Lecturer ID");
        
        User lecturer = dataManager.getUsers().get(lecturerId);
        if (lecturer == null || lecturer.getRole() != UserRole.LECTURER) {
            view.displayError("Lecturer not found.");
            return;
        }
        
        List<TimetableEntry> entries = dataManager.getLecturerTimetable(lecturerId);
        String title = "Timetable for " + lecturer.getName() + " (" + lecturerId + ")";
        
        view.displayTimetableWithDetails(entries, dataManager.getModules(), dataManager.getRooms(), title);
    }
    
    private void viewModuleTimetable() {
        String moduleCode = view.getInput("Enter Module Code");
        
        Module module = dataManager.getModules().get(moduleCode);
        if (module == null) {
            view.displayError("Module not found.");
            return;
        }
        
        List<TimetableEntry> entries = dataManager.getModuleTimetable(moduleCode);
        String title = "Timetable for " + module.getName() + " (" + moduleCode + ")";
        
        view.displayTimetableWithDetails(entries, dataManager.getModules(), dataManager.getRooms(), title);
    }
    
    private void viewProgrammeTimetable() {
        String programmeCode = view.getInput("Enter Programme Code");
        String yearStr = view.getInput("Enter Year");
        
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            view.displayError("Invalid year. Please enter a number.");
            return;
        }
        
        Programme programme = dataManager.getProgrammes().get(programmeCode);
        if (programme == null) {
            view.displayError("Programme not found.");
            return;
        }
        
        List<TimetableEntry> entries = dataManager.getProgrammeTimetable(programmeCode, year);
        String title = "Timetable for " + programme.getName() + " Year " + year;
        
        view.displayTimetableWithDetails(entries, dataManager.getModules(), dataManager.getRooms(), title);
    }
    
    private void viewRoomTimetable() {
        String roomId = view.getInput("Enter Room ID");
        
        Room room = dataManager.getRooms().get(roomId);
        if (room == null) {
            view.displayError("Room not found.");
            return;
        }
        
        List<TimetableEntry> entries = dataManager.getRoomTimetable(roomId);
        String title = "Timetable for " + room.getName() + " (" + roomId + ")";
        
        view.displayTimetableWithDetails(entries, dataManager.getModules(), dataManager.getRooms(), title);
    }
    
    private void addTimetableEntry() {
        Map<String, String> data = view.displayAddEntryForm();
        
        try {
            SessionType sessionType = SessionType.fromString(data.get("session_type"));
            
            // Validate room type for labs
            if (sessionType == SessionType.LAB) {
                Room room = dataManager.getRooms().get(data.get("room_id"));
                if (room == null || room.getRoomType() != RoomType.LAB) {
                    view.displayError("Lab sessions must be scheduled in lab rooms.");
                    return;
                }
            }
            
            TimeSlot timeSlot = new TimeSlot(
                data.get("day"),
                data.get("start_time"),
                data.get("end_time")
            );
            
            TimetableEntry entry = new TimetableEntry(
                data.get("entry_id"),
                data.get("module_code"),
                sessionType,
                data.get("group_id"),
                data.get("room_id"),
                data.get("lecturer_id"),
                timeSlot
            );
            
            DataManager.ValidationResult result = dataManager.addTimetableEntry(entry);
            
            if (result.isSuccess()) {
                view.displaySuccess(result.getMessage());
            } else {
                view.displayError(result.getMessage());
            }
        } catch (IllegalArgumentException e) {
            view.displayError("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            view.displayError("Error adding entry: " + e.getMessage());
        }
    }
    
    private void removeTimetableEntry() {
        String entryId = view.getInput("Enter Entry ID to remove");
        
        if (view.confirmAction("Are you sure you want to remove entry " + entryId + "?")) {
            try {
                boolean removed = dataManager.removeTimetableEntry(entryId);
                
                if (removed) {
                    view.displaySuccess("Entry " + entryId + " removed successfully.");
                } else {
                    view.displayError("Entry " + entryId + " not found.");
                }
            } catch (Exception e) {
                view.displayError("Error removing entry: " + e.getMessage());
            }
        }
    }
}
