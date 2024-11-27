package TreasurerDashboard;

import swimclub.controllers.PaymentController;
import swimclub.models.Member;
import swimclub.models.Billing;
import swimclub.models.MemberPaymentStatus;
import swimclub.services.BillingService;

import java.util.List;

public class TreasurerDashboard {
    private final PaymentController paymentController;

    // Constructor to initialize PaymentController
    public TreasurerDashboard(PaymentController paymentController) {
        this.paymentController = paymentController;
    }

    // Display the total expected amount from all members
    public void displayTotalExpectedAmount() {
        double total = paymentController.calculateTotalExpectedPayments();
        System.out.println("Total expected payments: " + total + " DKK");
    }

    // Display detailed payment information for all members
    public void displayMemberPayment() {
        System.out.println("Member payment details");

        // Fetch members with all bills paid
        List<Member> membersWithAllBillsPaid = paymentController.getMembersWithAllBillsPaid();
        if (membersWithAllBillsPaid.isEmpty()) {
            System.out.println("No members with all bills paid.");
        } else {
            membersWithAllBillsPaid.forEach(member -> {
                System.out.println("Member ID: " + member.getMemberId() +
                        ", Name: " + member.getName() +
                        ", Membership type: " + member.getMembershipType() +
                        ", Phone: " + member.getPhoneNumber() +
                        ", Email: " + member.getEmail() +
                        ", Billings: " + member.getBillingsList() +
                        ", Payment status: " + member.getMemberPaymentStatus());
            });
        }
    }

    // Filter members based on payment status (Complete, Pending)
    public void filterByPaymentStatus(String paymentStatus) {
        System.out.println("Members filtered by payment status: " + paymentStatus);

        // Handle Complete payment status
        if (paymentStatus.equalsIgnoreCase("Complete")) {
            List<Member> completeMembers = paymentController.getMembersWithAllBillsPaid();
            if (completeMembers.isEmpty()) {
                System.out.println("No members with complete payments.");
            } else {
                completeMembers.forEach(member -> {
                    double fee = BillingService.calculateMembershipFee(member);
                    System.out.println("Member ID: " + member.getMemberId() +
                            ", Name: " + member.getName() +
                            ", Fee: " + fee + " DKK");
                });
            }
        }
        // Handle Pending payment status
        else if (paymentStatus.equalsIgnoreCase("Pending")) {
            List<Member> pendingMembers = paymentController.getMembersWithPendingPayments();
            if (pendingMembers.isEmpty()) {
                System.out.println("No members with pending payments.");
            } else {
                pendingMembers.forEach(member -> {
                    double fee = BillingService.calculateMembershipFee(member);
                    System.out.println("Member ID: " + member.getMemberId() +
                            ", Name: " + member.getName() +
                            ", Phone number: " + member.getPhoneNumber() +
                            ", Fee: " + fee + " DKK");
                });
            }
        }
        // Handle invalid payment status
        else {
            System.out.println("Invalid payment status. Please enter either 'Complete' or 'Pending'");
        }
    }

    // Optionally, you can add more methods for other payment-related operations
}