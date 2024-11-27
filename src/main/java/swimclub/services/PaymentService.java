package swimclub.services;

import swimclub.models.Member;
import swimclub.models.MembershipStatus;
import swimclub.models.Payment;
import swimclub.models.PaymentStatus;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling payment-related operations.
 */
public class PaymentService {
    private final PaymentRepository paymentRepository; // Add PaymentRepository reference

    // Constructor accepts a PaymentRepository to interact with payment data
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Updates a member's payment status.
     *
     * @param member       The member whose payment status is being updated.
     * @param paymentStatus The new payment status to set.
     */
    public void updateMemberPaymentStatus(Member member, PaymentStatus paymentStatus) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (paymentStatus == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }
        member.setPaymentStatus(paymentStatus);
    }

    /**
     * Registers a payment for a member.
     *
     * @param memberId The ID of the member making the payment.
     * @param amount   The amount being paid.
     * @param memberRepository The repository used to fetch the member by ID.
     * @param billingId The ID of the billing this payment is being applied to.
     */
    public void registerPayment(int memberId, double amount, MemberRepository memberRepository, int billingId) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        // Find the member by ID
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        // Create a new payment instance with a unique ID and status set to COMPLETE
        Payment newPayment = new Payment(paymentRepository.getNextPaymentId(), PaymentStatus.COMPLETE, member, LocalDate.now(), amount, billingId);

        // Save the payment to the repository
        savePayment(newPayment);

        // Update the member's payment status
        updateMemberPaymentStatus(member, PaymentStatus.COMPLETE);

        System.out.println("Payment of " + amount + " registered for member ID: " + memberId);
    }

    /**
     * Saves the payment to the payment repository.
     *
     * @param payment The payment to be saved.
     */
    public void savePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        paymentRepository.savePayment(payment); // Save the payment using PaymentRepository
    }

    /**
     * Views payments for a member by their member ID.
     *
     * @param memberId The ID of the member whose payments are to be retrieved.
     */
    public void viewPaymentsForMember(int memberId) {
        // Validate memberId
        if (memberId <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }

        List<Payment> memberPayments = paymentRepository.findPaymentsByMemberId(memberId);

        if (memberPayments.isEmpty()) {
            System.out.println("No payments found for member ID: " + memberId);
        } else {
            System.out.println("--- Payments for Member ID: " + memberId + " ---");
            for (Payment payment : memberPayments) {
                System.out.println(payment.toString());
            }
        }
    }
}