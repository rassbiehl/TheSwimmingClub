package swimclub;

import swimclub.controllers.CompetitionResultController;
import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.controllers.TeamController;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;
import swimclub.repositories.TeamRepository;  // Import TeamRepository
import swimclub.services.MemberService;
import swimclub.services.PaymentService;
import swimclub.services.TeamService;  // Import TeamService
import swimclub.ui.UserInterface;
import swimclub.Utilities.FileHandler;
import swimclub.repositories.CompetitionResultRepository;
import swimclub.services.CompetitionResultService;

public class Main {
    /**
     * The main method that initializes the application, loads data, and starts the user interface.
     * It also handles saving changes back to the files after the user interaction.
     *
     * @param args Command-line arguments (not used in this program).
     *             This parameter allows for passing arguments when running the program,
     *             but in this specific implementation, it is not used in the code.
     */
    public static void main(String[] args) {
        // File paths for member data, payment data, reminder data, and teams data
        String memberFilePath = "src/main/resources/members.dat";
        String paymentFilePath = "src/main/resources/payments.dat";
        String reminderFilePath = "src/main/resources/reminders.dat";
        String paymentRatesFilePath = "src/main/resources/paymentRates.dat";
        String teamsFilePath = "src/main/resources/teams.dat"; // Path for teams data
        String competitionResultsFilePath = "src/main/resources/competitionResults.dat";


        // Initialize the FileHandler for members, payments, reminders, and teams
        FileHandler fileHandler = new FileHandler(memberFilePath, paymentFilePath, reminderFilePath, paymentRatesFilePath, teamsFilePath, competitionResultsFilePath);

        // Initialize the repositories, passing the respective FileHandlers
        MemberRepository memberRepository = new MemberRepository(fileHandler);
        PaymentRepository paymentRepository = new PaymentRepository(reminderFilePath); // Pass the reminder file path
        CompetitionResultRepository competitionResultRepository = new CompetitionResultRepository(fileHandler, competitionResultsFilePath);

        // Load members, payments, and teams from the file
        memberRepository.reloadMembers(); // Load member data
        paymentRepository.loadPayments(paymentFilePath, memberRepository); // Load payment data

        competitionResultRepository.loadResults(memberRepository); // Load competition results




        // Initialize services for member, payment and competition results
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, fileHandler);
        CompetitionResultService competitionResultService = new CompetitionResultService(competitionResultRepository);

        // Initialize TeamRepository
        TeamRepository teamRepository = new TeamRepository(fileHandler);  // Pass FileHandler to TeamRepository

        // Initialize TeamService with TeamRepository
        TeamService teamService = new TeamService(teamRepository);

        // Instantiate the controllers
        MemberController memberController = new MemberController(memberService, memberRepository);
        TeamController teamController = new TeamController(teamService);  // Pass TeamService to TeamController
        PaymentController paymentController = new PaymentController(
                paymentService,
                memberRepository,
                fileHandler,
                paymentFilePath,
                paymentRatesFilePath
        );

        CompetitionResultController competitionResultController = new CompetitionResultController(competitionResultService);

        // Instantiate the UserInterface, passing all controllers (including teamController)
        UserInterface userInterface = new UserInterface(memberController, paymentController, teamController, competitionResultController);

        // Start the User Interface to handle interactions
        userInterface.start();

        // After user interaction, save any changes to file
        fileHandler.saveMembers(memberRepository.findAll()); // Save updated members
        fileHandler.savePayments(paymentRepository.findAll(), paymentFilePath); // Save updated payments
        // Reminder data is managed by PaymentRepository, so no need to handle it here
        // Save the teams after user interaction
        fileHandler.saveTeams(teamController.getAllTeams()); // Save teams to the file
        fileHandler.saveCompetitionResults(competitionResultRepository.getAllResults(), competitionResultsFilePath); // Save competition results to the file
    }
}
