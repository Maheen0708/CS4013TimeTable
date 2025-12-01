package ie.ul.timetable.models;

public class Enums {

    public enum UserRole {
        STUDENT("student"),
        LECTURER("lecturer"),
        ADMIN("admin");

        private final String value;

        UserRole(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UserRole fromString(String text) {
            for (UserRole role : UserRole.values()) {
                if (role.value.equalsIgnoreCase(text)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Unknown role: " + text);
        }
    }

    public enum RoomType {
        CLASSROOM("classroom"),
        LAB("lab");

        private final String value;

        RoomType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static RoomType fromString(String text) {
            for (RoomType type : RoomType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown room type: " + text);
        }
    }

    public enum SessionType {
        LECTURE("lecture"),
        TUTORIAL("tutorial"),
        LAB("lab");

        private final String value;

        SessionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SessionType fromString(String text) {
            for (SessionType type : SessionType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown session type: " + text);
        }
    }
}
