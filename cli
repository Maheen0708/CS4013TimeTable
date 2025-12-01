/**
 * CLI View for the Timetabling System
 */
package ie.ul.timetable.views;

import ie.ul.timetable.models.*;
import ie.ul.timetable.models.Enums.*;
import java.util.*;

public class CLIView {
    private Scanner scanner;
    
    public CLIView() {
        this.scanner = new Scanner(System.in);
    }
    
    public void displayWelcome() {
        System.out.println("\n" + "--=====================================================================--"));
        System.out.println("                 " + "University of Limerick");
        System.out.println("                   " + "Timetabling System");
        System.out.println("--=====================================================================--" + "\n");
    }
    
    public String[] displayLoginPrompt() {
        System.out.println("Log in to continue:");
        System.out.print("User ID: ");
        String userId = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        return new String[]{userId, password};
    }
    
    public void displayError(String message) {
        System.out.println("\n Error: " + message + "\n");
    }
    
    public void displaySuccess(String message) {
        System.out.println("\n Success: " + message + "\n");
    }
    
    public void displayInfo(String message) {
        System.out.println("\n info: " + message + "\n");
    }
    
    public void displayMainMenu(User user) {
        System.out.println("\nWelcome, " + user.getName() + "!");
        System.out.println("\n--- Main Menu ---");
        
        if (user.getRole() == UserRole.STUDENT) {
            System.out.println("1. View My Timetable");
            System.out.println("2. View Module Timetable");
            System.out.println("3. View Programme Timetable");
            System.out.println("4. Logout");
        } else if (user.getRole() == UserRole.LECTURER) {
            System.out.println("1. View My Timetable");
            System.out.println("2. View Module Timetable");
            System.out.println("3. View Room Timetable");
            System.out.println("4. Logout");
        } else if (user.getRole() == UserRole.ADMIN) {
            System.out.println("1. View Student Timetable");
            System.out.println("2. View Lecturer Timetable");
            System.out.println("3. View Module Timetable");
            System.out.println("4. View Programme Timetable");
            System.out.println("5. View Room Timetable");
            System.out.println("6. Add Timetable Entry");
            System.out.println("7. Remove Timetable Entry");
            System.out.println("8. Logout");
        }
        System.out.println();
    }
    
    public String getMenuChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextLine().trim();
    }
    
    public String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }
    
    public void displayTimetable(List<TimetableEntry> entries, String title) {
        if (entries.isEmpty()) {
            System.out.println("\n" + title + ": No entries found.\n");
            return;
        }
        
        System.out.println("\n" + title + ":");
        System.out.println("====================================================================================================");
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days) {
            List<TimetableEntry> dayEntries = new ArrayList<>();
            for (TimetableEntry entry : entries) {
                if (entry.getTimeSlot().getDay().equals(day)) {
                    dayEntries.add(entry);
                }
            }
            
            if (!dayEntries.isEmpty()) {
                System.out.println("\n" + day + ":");
                System.out.println("----------------------------------------------------------------------------------------------------");
                
                dayEntries.sort(Comparator.comparing(e -> e.getTimeSlot().getStartTime()));
                
                for (TimetableEntry entry : dayEntries) {
                    System.out.printf("  %s-%s | %s | %s | Room: %s | Lecturer: %s%n",
                        entry.getTimeSlot().getStartTime(),
                        entry.getTimeSlot().getEndTime(),
                        entry.getModuleCode(),
                        entry.getSessionType().getValue().toUpperCase(),
                        entry.getRoomId(),
                        entry.getLecturerId());
                }
            }
        }
        
        System.out.println("====================================================================================================");
    }
    
    public void displayTimetableWithDetails(List<TimetableEntry> entries, Map<String, Module> modules,Map<String, Room> rooms, String title) {
        if (entries.isEmpty()) {
            System.out.println("\n" + title + ": No entries found.\n");
            return;
        }
        
        System.out.println("\n" + title + ":");
        System.out.println("=".repeat(120));
        
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String day : days) {
            List<TimetableEntry> dayEntries = new ArrayList<>();
            for (TimetableEntry entry : entries) {
                if (entry.getTimeSlot().getDay().equals(day)) {
                    dayEntries.add(entry);
                }
            }
            
            if (!dayEntries.isEmpty()) {
                System.out.println("\n" + day + ":");
                System.out.println("-".repeat(120));
                
                dayEntries.sort(Comparator.comparing(e -> e.getTimeSlot().getStartTime()));
                
                for (TimetableEntry entry : dayEntries) {
                    Module module = modules.get(entry.getModuleCode());
                    Room room = rooms.get(entry.getRoomId());
                    String moduleName = module != null ? module.getName() : "Unknown";
                    String roomName = room != null ? room.getName() : "Unknown";
                    
                    System.out.printf("  %s-%s | %s - %s | %s | %s (%s) | Lecturer: %s%n",
                        entry.getTimeSlot().getStartTime(),
                        entry.getTimeSlot().getEndTime(),
                        entry.getModuleCode(),
                        moduleName,
                        entry.getSessionType().getValue().toUpperCase(),
                        roomName,
                        entry.getRoomId(),
                        entry.getLecturerId());
                }
            }
        }
        
        System.out.println("====================================================================================================" + "\n");
    }
    
    public Map<String, String> displayAddEntryForm() {
        Map<String, String> data = new HashMap<>();
        System.out.println("\n--- Add Timetable Entry ---");
        data.put("entry_id", getInput("Entry ID"));
        data.put("module_code", getInput("Module Code"));
        data.put("session_type", getInput("Session Type (lecture/tutorial/lab)"));
        data.put("group_id", getInput("Group ID"));
        data.put("room_id", getInput("Room ID"));
        data.put("lecturer_id", getInput("Lecturer ID"));
        data.put("day", getInput("Day (Monday-Friday)"));
        data.put("start_time", getInput("Start Time (HH:MM)"));
        data.put("end_time", getInput("End Time (HH:MM)"));
        return data;
    }
    
    public boolean confirmAction(String message) {
        System.out.print(message + " (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y");
    }
    
    public void pause() {
        System.out.print("Press Enter to continue");
        scanner.nextLine();
    }
    
    public void close() {
        scanner.close();
    }
}
