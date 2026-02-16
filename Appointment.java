import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private String type;
    private ZonedDateTime dateTime;
    private String notes;

    public Appointment(String type, ZonedDateTime dateTime, String notes) {
        this.type = type;
        this.dateTime = dateTime;
        this.notes = notes;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public ZonedDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDateTime = dateTime.format(formatter);

        return "Appointment details:\n" +
                "---------------------\n" +
                "Type: " + type + "\n" +
                "Date: " + formattedDateTime + "\n" +
                "Notes: " + (notes == null || notes.isEmpty() ? "None" : notes) + "\n";
    }
}
