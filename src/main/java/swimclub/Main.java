package swimclub;

import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;
import swimclub.services.MemberService;
import swimclub.services.PaymentService;
import swimclub.ui.UserInterface;
import swimclub.Utilities.FileHandler;

public class Main {
    public static void main(String[] args) {
        // File paths for member data, payment data, and reminder data
        String memberFilePath = "src/main/resources/members.txt";
        String paymentFilePath = "src/main/resources/payments.txt";
        String reminderFilePath = "src/main/resources/reminders.txt";

        // Initialize the FileHandler for members, payments, and reminders
        FileHandler memberFileHandler = new FileHandler(memberFilePath, paymentFilePath, reminderFilePath);

        // Initialize the repositories, passing the respective FileHandlers
        MemberRepository memberRepository = new MemberRepository(memberFileHandler);
        PaymentRepository paymentRepository = new PaymentRepository();

        // Load members, payments, and reminders from the file
        memberRepository.reloadMembers(); // Assuming loadMembers method exists to load member data
        paymentRepository.loadPayments(paymentFilePath, memberRepository);

        // Initialize services for member and payment
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository);

        // Instantiate the controllers
        MemberController memberController = new MemberController(memberService, memberRepository);
        PaymentController paymentController = new PaymentController(
                paymentService,
                memberRepository,
                memberFileHandler,
                paymentFilePath
        );

        // Instantiate the UserInterface, passing both controllers
        UserInterface userInterface = new UserInterface(memberController, paymentController);

        // Start the User Interface to handle interactions
        userInterface.start();

        // After user interaction, save any changes to file
        memberFileHandler.saveMembers(memberRepository.findAll());
        memberFileHandler.savePayments(paymentRepository.findAll(), paymentFilePath);
        memberFileHandler.saveReminders(paymentService.getAllReminders()); // Save reminders after interaction
    }
}
