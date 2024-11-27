package swimclub.controllers;

import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.services.BillingService;
import swimclub.services.PaymentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for handling payment-related operations.
 */
public class PaymentController {
    private final PaymentService paymentService;
    private final BillingService billingService;
    private final MemberRepository memberRepository;

    /**
     * Constructor to initialize the PaymentController.
     *
     * @param paymentService   The service handling payment-related logic.
     * @param billingService   The service handling billing-related logic.
     * @param memberRepository The repository for accessing member data.
     */
    public PaymentController(PaymentService paymentService, BillingService billingService, MemberRepository memberRepository) {
        this.paymentService = paymentService;
        this.billingService = billingService;
        this.memberRepository = memberRepository;
    }

    /**
     * Calculates the membership fee for a specific member by their ID.
     *
     * @param memberId The ID of the member whose fee is being calculated.
     * @return The calculated membership fee, or -1 if the member is not found.
     */
    public double calculateMembershipFee(int memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return -1;
        }
        return billingService.calculateMembershipFee(member);
    }

    /**
     * Calculates the total expected payments across all members.
     *
     * @return The total expected payments.
     */
    public double calculateTotalExpectedPayments() {
        List<Member> members = memberRepository.findAll();
        return billingService.calculateTotalExpectedPayments(members);
    }

    /**
     * Retrieves a list of members who have paid all their bills.
     *
     * @return A list of members with a payment status of ALL_BILLS_PAID.
     */
    public List<Member> getMembersWithAllBillsPaid() {
        List<Member> allMembers = memberRepository.findAll();
        List<Member> membersWithAllPaid = new ArrayList<>();
        for (Member member : allMembers) {
            if (member.getMemberPaymentStatus() == MemberPaymentStatus.ALL_BILLS_PAID) {
                membersWithAllPaid.add(member);
            }
        }
        if (membersWithAllPaid.isEmpty()) {
            System.out.println("No members have paid all their bills.");
        }
        return membersWithAllPaid;
    }

    /**
     * Retrieves a list of members who have pending payments.
     *
     * @return A list of members with a payment status of PENDING_PAYMENT.
     */
    public List<Member> getMembersWithPendingPayments() {
        List<Member> allMembers = memberRepository.findAll();
        List<Member> membersWithPendingPayments = new ArrayList<>();
        for (Member member : allMembers) {
            if (member.getMemberPaymentStatus() == MemberPaymentStatus.PENDING_PAYMENT) {
                membersWithPendingPayments.add(member);
            }
        }
        if (membersWithPendingPayments.isEmpty()) {
            System.out.println("No members have pending payments.");
        }
        return membersWithPendingPayments;
    }

    /**
     * Updates the payment status for a specific member by their ID.
     *
     * @param memberId        The ID of the member whose payment status is being updated.
     * @param updatedStatus   The new payment status to set.
     */
    public void updatePaymentStatus(int memberId, MemberPaymentStatus updatedStatus) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }
        paymentService.updateMemberPaymentStatus(member, updatedStatus);
        memberRepository.update(member); // Save changes to the repository.
        System.out.println("Payment status updated for member ID: " + memberId);
    }

    /**
     * Registers a new payment for a member by their ID and payment amount.
     *
     * @param memberId    The ID of the member making the payment.
     * @param paymentAmount The payment amount.
     * @param billingId     The ID of the billing item associated with this payment.
     */
    public void registerMemberPayment(int memberId, double paymentAmount, int billingId) {
        paymentService.registerPayment(memberId, paymentAmount, memberRepository, billingId);
    }

    /**
     * Displays all payments made by a specific member.
     *
     * @param memberId The ID of the member whose payments are being displayed.
     */
    public List<Payment> returnPaymentsForMember(int memberId) {
        return memberRepository.findById(memberId).getPaymentList();
    }


}