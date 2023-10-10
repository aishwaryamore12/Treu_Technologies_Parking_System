import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class ParkingSpot {
    private int spotNumber;
    private boolean isAvailable;

    public ParkingSpot(int spotNumber) {
        this.spotNumber = spotNumber;
        this.isAvailable = true;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

class ParkingBooking {
    private static int nextBookingId = 1;
    private int bookingId;
    private User user;
    private ParkingSpot parkingSpot;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ParkingBooking(User user, ParkingSpot parkingSpot, LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingId = nextBookingId++;
        this.user = user;
        this.parkingSpot = parkingSpot;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}

class ParkingSystem {
    private List<ParkingSpot> parkingSpots;
    private List<ParkingBooking> bookings;
    private List<User> users;
    private Map<Integer, ParkingBooking> bookingMap;

    public ParkingSystem() {
        this.parkingSpots = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookingMap = new HashMap<>();
    }
    
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public void addParkingSpot(ParkingSpot parkingSpot) {
        parkingSpots.add(parkingSpot);
    }

    public List<ParkingSpot> searchAvailableSpots() {
        List<ParkingSpot> availableSpots = new ArrayList<>();
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isAvailable()) {
                availableSpots.add(spot);
            }
        }
        return availableSpots;
    }

    public ParkingBooking bookParkingSpot(User user, ParkingSpot parkingSpot, LocalDateTime startTime, LocalDateTime endTime) {
        if (!parkingSpot.isAvailable()) {
            return null;
        }

        ParkingBooking booking = new ParkingBooking(user, parkingSpot, startTime, endTime);
        bookings.add(booking);
        parkingSpot.setAvailable(false);
        bookingMap.put(booking.getBookingId(), booking);
        return booking;
    }

    public List<ParkingBooking> getUserBookings(User user) {
        List<ParkingBooking> userBookings = new ArrayList<>();
        for (ParkingBooking booking : bookings) {
            if (booking.getUser().equals(user)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public boolean isUserRegistered(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void registerUser(User user) {
        users.add(user);
    }

    public ParkingBooking getBookingById(int bookingId) {
        return bookingMap.get(bookingId);
    }
}

public class Parking {
    public static void main(String[] args) {
        // Create the parking system
        ParkingSystem parkingSystem = new ParkingSystem();

        // Add parking spots to the system
        parkingSystem.addParkingSpot(new ParkingSpot(1));
        parkingSystem.addParkingSpot(new ParkingSpot(2));
        parkingSystem.addParkingSpot(new ParkingSpot(3));
        parkingSystem.addParkingSpot(new ParkingSpot(4));

        // Interaction with the parking system
        Scanner scanner = new Scanner(System.in);
        int choice;
        String username, password;
        boolean loggedIn = false;
        User user = null;

        do {
            if (!loggedIn) {
                System.out.println("Welcome to the Online Parking System!");
                System.out.println("1. Register User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1/2/3): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after reading the choice

                switch (choice) {
                    case 1:
                        System.out.print("Enter your desired username: ");
                        username = scanner.nextLine();
                        if (parkingSystem.isUserRegistered(username)) {
                            System.out.println("Username is already taken. Please choose another username.");
                        } else {
                            System.out.print("Enter your password: ");
                            password = scanner.nextLine();
                            user = new User(username, password);
                            parkingSystem.registerUser(user);
                            System.out.println("User registration successful!");
                            loggedIn = true;
                        }
                        break;
                    case 2:
                        System.out.print("Enter your username: ");
                        username = scanner.nextLine();
                        System.out.print("Enter your password: ");
                        password = scanner.nextLine();
                        user = parkingSystem.getUser(username);
                        if (user != null && user.getPassword().equals(password)) {
                            System.out.println("Login successful!");
                            loggedIn = true;
                        } else {
                            System.out.println("Invalid username or password. Please try again.");
                        }
                        break;
                    case 3:
                        System.out.println("Thank you for using the Online Parking System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("1. Search for available parking spots");
                System.out.println("2. Book a parking spot");
                System.out.println("3. View your bookings");
                System.out.println("4. Logout");
                System.out.print("Enter your choice (1/2/3/4): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after reading the choice

                switch (choice) {
                    case 1:
                        // Parking spot search
                        List<ParkingSpot> availableSpots = parkingSystem.searchAvailableSpots();
                        if (!availableSpots.isEmpty()) {
                            System.out.println("Available Parking Spots:");
                            for (ParkingSpot spot : availableSpots) {
                                System.out.println("Spot: " + spot.getSpotNumber());
                            }
                        } else {
                            System.out.println("No available parking spots at the moment.");
                        }
                        break;
                    case 2:
                        // Parking spot booking
                        System.out.print("Enter the spot number to book: ");
                        int spotNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character after reading the spot number
                        ParkingSpot spotToBook = null;
                        for (ParkingSpot spot : parkingSystem.searchAvailableSpots()) {
                            if (spot.getSpotNumber() == spotNumber) {
                                spotToBook = spot;
                                break;
                            }
                        }
                        if (spotToBook != null) {
                            LocalDateTime startTime = LocalDateTime.now();
                            LocalDateTime endTime = startTime.plusHours(2);
                            ParkingBooking booking = parkingSystem.bookParkingSpot(user, spotToBook, startTime, endTime);
                            if (booking != null) {
                                System.out.println("Booking successful. Booking ID: " + booking.getBookingId());
                            } else {
                                System.out.println("Spot is already booked. Please choose another spot.");
                            }
                        } else {
                            System.out.println("Invalid spot number. Please choose an available spot.");
                        }
                        break;
                    case 3:
                        // View user bookings
                        List<ParkingBooking> userBookings = parkingSystem.getUserBookings(user);
                        if (!userBookings.isEmpty()) {
                            System.out.println("User Bookings for " + user.getUsername() + ":");
                            for (ParkingBooking booking : userBookings) {
                                System.out.println("Booking ID: " + booking.getBookingId() + ", Spot: " + booking.getParkingSpot().getSpotNumber()
                                        + ", Start Time: " + booking.getStartTime() + ", End Time: " + booking.getEndTime());
                            }
                        } else {
                            System.out.println("No bookings found for " + user.getUsername() + ".");
                        }
                        break;
                    case 4:
                        System.out.println("Logout successful. Goodbye!");
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            System.out.println();
        } while (choice != 3);
        scanner.close();
        
    }
}
