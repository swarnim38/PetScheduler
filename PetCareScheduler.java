import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class PetCareScheduler {
    private static final Map<String, Pet> petMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "pet_records.txt";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    public static void main(String[] args) {
        loadDataFromFile();

        boolean running = true;
        while (running) {
            IO.println("\n--- Pet Care Management System ---");
            IO.println("1. Register Pet");
            IO.println("2. Schedule Appointment");
            IO.println("3. Store Data to File");
            IO.println("4. Display Records");
            IO.println("5. Generate Health Reports");
            IO.println("6. Exit");
            IO.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            // Using switch-case to invoke actions as requested
            switch (choice) {
                case "1" -> registerNewPet();
                case "2" -> addAppointmentForPet();
                case "3" -> storeDataToFile();
                case "4" -> displayRecords();
                case "5" -> generateReports();
                case "6" -> {
                    running = false;
                    IO.println("Exiting System...");
                    storeDataToFile(); // Auto-save before exit
                }
                default -> IO.println("Invalid selection. Please try again.");
            }
        }
        scanner.close();
    }

    // Method 1: Register a new pet and add to collection
    public static void registerNewPet() {
        IO.print("Enter Unique Pet ID: ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            IO.println("Error: Pet ID cannot be empty.");
            return;
        }

        if (petMap.containsKey(id)) {
            IO.println("Error: Pet ID already exists.");
            return;
        }

        IO.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            IO.println("Error: Name cannot be empty.");
            return;
        }

        IO.print("Species: ");
        String species = scanner.nextLine().trim();
        if (species.isEmpty()) {
            IO.println("Error: Species cannot be empty.");
            return;
        }

        IO.print("Age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
            if (age < 0) {
                IO.println("Error: Age must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            IO.println("Error: Age must be a valid number.");
            return;
        }

        IO.print("Owner: ");
        String owner = scanner.nextLine().trim();
        if (owner.isEmpty()) {
            IO.println("Error: Owner name cannot be empty.");
            return;
        }

        IO.print("Contact: ");
        String contact = scanner.nextLine().trim();
        if (contact.isEmpty()) {
            IO.println("Error: Contact information cannot be empty.");
            return;
        }

        // Parameterized constructor usage
        petMap.put(id, new Pet(id, name, species, age, owner, contact));
        IO.println("Pet successfully registered.");
    }
    public static void addAppointmentForPet() {
        IO.print("Enter Pet ID: ");
        String id = scanner.nextLine().trim();
        Pet pet = petMap.get(id); // Validation 1: Check if pet exists

        if (pet == null) {
            IO.println("Error: Pet ID does not exist.");
            return;
        }
        IO.print("Type (Vet Visit/Vaccination/Grooming): ");
        String type = scanner.nextLine().trim();
        List<String> validTypes = Arrays.asList("Vet Visit", "Vaccination", "Grooming");

        boolean isValidType = validTypes.stream()
                .anyMatch(t -> t.equalsIgnoreCase(type));

        if (!isValidType) {
            IO.println("Error: Invalid appointment type. Choose from: Vet Visit, Vaccination, Grooming");
            return;
        }

        // Capitalize type properly
        String capitalizedType = validTypes.stream()
                .filter(t -> t.equalsIgnoreCase(type))
                .findFirst()
                .orElse(type);

        IO.println("Enter Date/Time (format: yyyy-MM-dd'T'HH:mm:ssZ)");
        IO.println("Example: 2025-02-20T14:30:00+0530");
        IO.print("Date/Time: ");

        try {
            // Parsing ZonedDateTime with proper format handling
            String dateInput = scanner.nextLine().trim();
            ZonedDateTime dt = ZonedDateTime.parse(dateInput);

            if (dt.isBefore(ZonedDateTime.now())) {
                IO.println("Error: Appointment must be set for a future date.");
                return;
            }

            IO.print("Notes (optional, press Enter to skip): ");
            String notes = scanner.nextLine().trim();

            // FIX #9: Check for duplicate appointments
            List<Appointment> existingAppointments = (List<Appointment>) pet.getAppointments();
            boolean isDuplicate = existingAppointments.stream()
                    .anyMatch(a -> a.getType().equalsIgnoreCase(capitalizedType) &&
                            a.getDateTime().equals(dt));

            if (isDuplicate) {
                IO.println("Error: This appointment already exists for this pet.");
                return;
            }

            pet.addAppointment(new Appointment(capitalizedType, dt, notes));
            IO.println("Appointment scheduled successfully.");
        } catch (Exception e) {
            IO.println("Error: Invalid date/time format. Please use: yyyy-MM-dd'T'HH:mm:ssZ");
        }
    }

    // Method 3: Store data to file
    public static void storeDataToFile() {
        // Using BufferedWriter for efficient file operation
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Pet p : petMap.values()) {
                // FIX #10: Use CSV format with escaped delimiters to prevent corruption
                writer.write(String.format("PET|%s|%s|%s|%d|%s|%s\n",
                        escapeDelimiter(p.getPetId()),
                        escapeDelimiter(p.getName()),
                        escapeDelimiter(p.getSpecies()),
                        p.getAge(),
                        escapeDelimiter(p.getOwnerName()),
                        escapeDelimiter(p.getContactInfo())));

                List<Appointment> appointments = (List<Appointment>) p.getAppointments();
                for (Appointment a : appointments) {
                    writer.write(String.format("APP|%s|%s|%s\n",
                            escapeDelimiter(a.getType()),
                            a.getDateTime().format(dateFormatter),
                            escapeDelimiter(a.getNotes() != null ? a.getNotes() : "")));
                }
            }
            IO.println("All records saved to " + FILE_NAME);
        } catch (IOException e) {
            IO.printError("File I/O Error: " + e.getMessage());
        }
    }

    private static String escapeDelimiter(String value) {
        return value != null ? value.replace("|", "\\|") : "";
    }

    // Helper method to unescape delimiters
    private static String unescapeDelimiter(String value) {
        return value != null ? value.replace("\\|", "|") : "";
    }

    // Method 4: Display specific records and histories
    public static void displayRecords() {
        if (petMap.isEmpty()) {
            IO.println("No pets registered yet.");
            return;
        }

        IO.println("\n--- All Registered Pets ---");
        petMap.values().forEach(p -> IO.println("ID: " + p.getPetId() + " | Name: " + p.getName()));

        IO.print("\nEnter Pet ID for full history (or press Enter to skip): ");
        String id = scanner.nextLine().trim();

        if (id.isEmpty()) {
            return;
        }

        Pet p = petMap.get(id);

        if (p == null) {
            IO.println("Error: Pet ID not found.");
            return;
        }

        IO.println("\n--- Pet Details ---");
        IO.println("ID: " + p.getPetId());
        IO.println("Name: " + p.getName());
        IO.println("Species: " + p.getSpecies());
        IO.println("Age: " + p.getAge());
        IO.println("Owner: " + p.getOwnerName());
        IO.println("Contact: " + p.getContactInfo());
        IO.println("Registration Date: " + p.getRegistrationDate());

        List<Appointment> appointments = (List<Appointment>) p.getAppointments();

        IO.println("\n--- Upcoming Appointments ---");
        List<Appointment> upcomingAppointments = appointments.stream()
                .filter(a -> a.getDateTime().isAfter(ZonedDateTime.now()))
                .sorted(Comparator.comparing(Appointment::getDateTime))
                .toList();

        if (upcomingAppointments.isEmpty()) {
            IO.println("No upcoming appointments.");
        } else {
            upcomingAppointments.forEach(a -> IO.println(a.toString()));
        }

        IO.println("\n--- Past Appointment History ---");
        List<Appointment> pastAppointments = appointments.stream()
                .filter(a -> a.getDateTime().isBefore(ZonedDateTime.now()))
                .sorted(Comparator.comparing(Appointment::getDateTime).reversed())
                .toList();

        if (pastAppointments.isEmpty()) {
            IO.println("No past appointments.");
        } else {
            pastAppointments.forEach(a -> IO.println(a.toString()));
        }
    }

    // Method 5: Generate specific reports
    public static void generateReports() {
        if (petMap.isEmpty()) {
            IO.println("No pets registered yet.");
            return;
        }

        IO.println("\n--- Report Options ---");
        IO.println("1. Appointments in the Next 7 Days");
        IO.println("2. Pets Overdue for Vet Visit (No visit in last 6 months)");
        IO.println("3. All Pets by Species");
        IO.print("Select report type: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> generateUpcomingAppointmentsReport();
            case "2" -> generateOverdueVetVisitsReport();
            case "3" -> generatePetsBySpeciesReport();
            default -> IO.println("Invalid selection.");
        }
    }

    private static void generateUpcomingAppointmentsReport() {
        ZonedDateTime oneWeekFromNow = ZonedDateTime.now().plusWeeks(1);
        ZonedDateTime now = ZonedDateTime.now();

        IO.println("\n--- Report: Appointments in the Next 7 Days ---");
        boolean found = false;

        for (Pet p : petMap.values()) {
            List<Appointment> appointments = (List<Appointment>) p.getAppointments();
            List<Appointment> upcomingAppointments = appointments.stream()
                    .filter(a -> a.getDateTime().isAfter(now) && a.getDateTime().isBefore(oneWeekFromNow))
                    .sorted(Comparator.comparing(Appointment::getDateTime))
                    .toList();

            for (Appointment a : upcomingAppointments) {
                IO.println("Pet: " + p.getName() + " | Owner: " + p.getOwnerName() +
                        " | Task: " + a.getType() + " | DateTime: " + a.getDateTime());
                found = true;
            }
        }

        if (!found) {
            IO.println("No appointments scheduled for the next 7 days.");
        }
    }

    private static void generateOverdueVetVisitsReport() {
        ZonedDateTime sixMonthsAgo = ZonedDateTime.now().minusMonths(6);

        IO.println("\n--- Report: Pets Overdue for Vet (No visit in last 6 months) ---");
        boolean found = false;

        for (Pet p : petMap.values()) {
            List<Appointment> appointments = (List<Appointment>) p.getAppointments();
            boolean recentlySeen = appointments.stream()
                    .anyMatch(a -> a.getType().equalsIgnoreCase("Vet Visit") &&
                            a.getDateTime().isAfter(sixMonthsAgo));

            if (!recentlySeen) {
                IO.println("Pet: " + p.getName() + " | Owner: " + p.getOwnerName() +
                        " | Contact: " + p.getContactInfo());
                found = true;
            }
        }

        if (!found) {
            IO.println("All pets are up-to-date with vet visits.");
        }
    }

    private static void generatePetsBySpeciesReport() {
        IO.println("\n--- Report: Pets by Species ---");
        Map<String, List<Pet>> petsBySpecies = new TreeMap<>();

        for (Pet p : petMap.values()) {
            petsBySpecies.computeIfAbsent(p.getSpecies(), k -> new ArrayList<>()).add(p);
        }

        if (petsBySpecies.isEmpty()) {
            IO.println("No pets found.");
        } else {
            for (String species : petsBySpecies.keySet()) {
                IO.println("\n" + species + ":");
                petsBySpecies.get(species).forEach(p ->
                        IO.println("  - " + p.getName() + " (Age: " + p.getAge() + ", Owner: " + p.getOwnerName() + ")")
                );
            }
        }
    }

    private static void loadDataFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return; // File doesn't exist yet, start fresh
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            Pet currentPet = null;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts[0].equals("PET") && parts.length >= 7) {
                    String petId = unescapeDelimiter(parts[1]);
                    String name = unescapeDelimiter(parts[2]);
                    String species = unescapeDelimiter(parts[3]);
                    int age = Integer.parseInt(parts[4]);
                    String ownerName = unescapeDelimiter(parts[5]);
                    String contactInfo = unescapeDelimiter(parts[6]);

                    currentPet = new Pet(petId, name, species, age, ownerName, contactInfo);
                    petMap.put(petId, currentPet);

                } else if (parts[0].equals("APP") && currentPet != null && parts.length >= 4) {
                    String type = unescapeDelimiter(parts[1]);
                    ZonedDateTime dateTime = ZonedDateTime.parse(parts[2]);
                    String notes = unescapeDelimiter(parts[3]);

                    currentPet.addAppointment(new Appointment(type, dateTime, notes));
                }
            }
            IO.println("Data loaded successfully from " + FILE_NAME);
        } catch (IOException e) {
            IO.printError("Error loading data: " + e.getMessage());
        }
    }
}