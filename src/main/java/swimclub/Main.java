package swimclub;

import TreasurerDashboard.TreasurerDashboard;
import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.services.BillingService;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;
import swimclub.services.MemberService;
import swimclub.services.PaymentService;
import swimclub.ui.UserInterface;
import swimclub.utilities.FileHandler;


public class Main {
    public static void main(String[] args) {
        // Initialize the FileHandler, MemberRepository, and MemberService
        String filePath = "src/main/resources/members.txt"; // Adjust the file path as needed
        FileHandler fileHandler = new FileHandler(filePath);
        MemberRepository memberRepository = new MemberRepository(fileHandler);
        MemberService memberService = new MemberService(memberRepository);

        // Initialize the PaymentRepository and PaymentService
        PaymentRepository paymentRepository = new PaymentRepository(); // Payment repository for managing payments
        PaymentService paymentService = new PaymentService(paymentRepository); // Payment service for payment-related operations

        // Initialize the BillingService
        BillingService billingService = new BillingService(paymentRepository);


        // Instantiate the MemberController
        MemberController memberController = new MemberController(memberService, memberRepository, paymentRepository);

        // Instantiate the PaymentController
        PaymentController paymentController = new PaymentController(paymentService, billingService, memberRepository);

        // Instantiate the TreasurerDashboard
        TreasurerDashboard treasurerDashboard = new TreasurerDashboard(paymentController);

        // Instantiate the UserInterface and pass in the MemberController, PaymentController and TreasurerDashboard
        UserInterface userInterface = new UserInterface(memberController, paymentController, treasurerDashboard); // Pass paymentController to UI



        // Start the User Interface
        userInterface.start(); // This will now handle both member and payment actions
    }
}
