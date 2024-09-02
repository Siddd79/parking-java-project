import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

class ParkingSlot {
    private int slotNumber;
    private boolean isOccupied;
    private String vehicleType;

    public ParkingSlot(int slotNumber, String vehicleType) {
        this.slotNumber = slotNumber;
        this.vehicleType = vehicleType;
        this.isOccupied = false;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void occupySlot() {
        this.isOccupied = true;
    }

    public void freeSlot() {
        this.isOccupied = false;
    }

    public String getVehicleType() {
        return vehicleType;
    }
}

class Vehicle {
    private String licensePlate;
    private String vehicleType;
    private Date entryTime;
    private Date exitTime;
    private int parkingSlot;

    public Vehicle(String licensePlate, String vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.entryTime = new Date();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setExitTime() {
        this.exitTime = new Date();
    }

    public Date getExitTime() {
        return exitTime;
    }

    public int getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(int parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public long calculateParkingDuration() {
        return (exitTime.getTime() - entryTime.getTime()) / (1000 * 60); // Duration in minutes
    }
}

public class ParkingManagementSystem {
    private static ArrayList<ParkingSlot> slots = new ArrayList<>();
    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static int carSlotCount = 5;
    private static int bikeSlotCount = 5;

    public static void main(String[] args) {
        initializeSlots();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nParking Management System");
            System.out.println("1. Vehicle Entry");
            System.out.println("2. Vehicle Exit");
            System.out.println("3. View Slots Availability");
            System.out.println("4. Generate Report");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    vehicleEntry(scanner);
                    break;
                case 2:
                    vehicleExit(scanner);
                    break;
                case 3:
                    viewSlotAvailability();
                    break;
                case 4:
                    generateReport();
                    break;
                case 5:
                    System.out.println("Exiting the system.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void initializeSlots() {
        for (int i = 1; i <= carSlotCount; i++) {
            slots.add(new ParkingSlot(i, "Car"));
        }
        for (int i = 1; i <= bikeSlotCount; i++) {
            slots.add(new ParkingSlot(i + carSlotCount, "Bike"));
        }
    }

    private static void vehicleEntry(Scanner scanner) {
        System.out.print("Enter vehicle license plate: ");
        String licensePlate = scanner.next();
        System.out.print("Enter vehicle type (Car/Bike): ");
        String vehicleType = scanner.next();

        int assignedSlot = assignSlot(vehicleType);
        if (assignedSlot != -1) {
            Vehicle vehicle = new Vehicle(licensePlate, vehicleType);
            vehicle.setParkingSlot(assignedSlot);
            vehicles.add(vehicle);
            slots.get(assignedSlot - 1).occupySlot();
            System.out.println("Vehicle parked at slot number: " + assignedSlot);
        } else {
            System.out.println("No available slots for " + vehicleType);
        }
    }

    private static int assignSlot(String vehicleType) {
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getVehicleType().equalsIgnoreCase(vehicleType)) {
                return slot.getSlotNumber();
            }
        }
        return -1;
    }

    private static void vehicleExit(Scanner scanner) {
        System.out.print("Enter vehicle license plate: ");
        String licensePlate = scanner.next();
        Vehicle vehicle = findVehicleByLicensePlate(licensePlate);

        if (vehicle != null) {
            vehicle.setExitTime();
            long duration = vehicle.calculateParkingDuration();
            System.out.println("Parking duration: " + duration + " minutes");

            double fee = calculateParkingFee(vehicle.getVehicleType(), duration);
            System.out.println("Total parking fee: $" + fee);

            slots.get(vehicle.getParkingSlot() - 1).freeSlot();
            vehicles.remove(vehicle);
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    private static Vehicle findVehicleByLicensePlate(String licensePlate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                return vehicle;
            }
        }
        return null;
    }

    private static double calculateParkingFee(String vehicleType, long duration) {
        double ratePerMinute = vehicleType.equalsIgnoreCase("Car") ? 0.05 : 0.03;
        return ratePerMinute * duration;
    }

    private static void viewSlotAvailability() {
        System.out.println("\nSlot Availability:");
        for (ParkingSlot slot : slots) {
            System.out.println("Slot " + slot.getSlotNumber() + " (" + slot.getVehicleType() + "): " +
                    (slot.isOccupied() ? "Occupied" : "Available"));
        }
    }

    private static void generateReport() {
        System.out.println("\nParking Report:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("Vehicle " + vehicle.getLicensePlate() +
                    " | Type: " + vehicle.getVehicleType() +
                    " | Slot: " + vehicle.getParkingSlot() +
                    " | Entry: " + vehicle.getEntryTime());
        }
    }
}
