package ie.ul.timetable;

import ie.ul.timetable.controllers.DataManager;
import ie.ul.timetable.controllers.TimetableController;
import ie.ul.timetable.views.CLIView;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting UL Timetabling System...");

        DataManager dataManager = new DataManager();
        dataManager.loadAllData();

        CLIView view = new CLIView();
        TimetableController controller = new TimetableController(dataManager, view);

        controller.run();

        view.close();
        dataManager.saveAllData();

        System.out.println("Goodbye!");
    }
}
