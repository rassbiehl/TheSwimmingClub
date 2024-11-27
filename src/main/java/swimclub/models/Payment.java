package swimclub.models;

import java.time.LocalDate;

public class Payment {
    private int paymentId;                 // Unique ID for the payment
    private PaymentStatus paymentStatus;  // COMPLETE, PENDING, FAILED
    private Member member;                // Member associated with the payment
    private LocalDate paymentDate;        // Date of payment
    private double amount;         // Annual membership fee
    private int billingId;               // the id of the bill the payment is made towards.

    /**
     * Constructor for Payment.
     *
     * @param paymentId     Unique payment ID.
     * @param paymentStatus Status of the payment (COMPLETE, PENDING, FAILED).
     * @param member        Member associated with the payment.
     * @param paymentDate   Date of the payment.
     * @param amount Annual membership fee.
     * @param billingId     the id of the bill the payment is sent towards.
     * @throws IllegalArgumentException If amount is not positive.
     * @throws NullPointerException     If member or paymentDate is null.
     */
    public Payment(int paymentId, PaymentStatus paymentStatus, Member member, LocalDate paymentDate, double amount, int billingId) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        if (member == null) {
            throw new NullPointerException("Member cannot be null");
        }

        if (paymentDate == null) {
            throw new NullPointerException("Payment date cannot be null");
        }

        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.member = member;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.billingId = billingId;
    }

    // Get Methods
    public int getPaymentId() {
        return paymentId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public double getAmount() {return this.amount;}

    // Set Methods
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate == null) {
            throw new NullPointerException("Payment date cannot be null");
        }
        this.paymentDate = paymentDate;
    }

    public void setMember(Member member) {
        if (member == null) {
            throw new NullPointerException("Member cannot be null");
        }
        this.member = member;
    }

    /**
     * Provides a detailed string representation of the payment.
     *
     * @return A string with the payment details.
     */
    @Override
    public String toString() {
        return "Payment Details: {" +
                "Payment ID: " + paymentId +
                ", Member: " + member.getName() +
                ", Payment Date: " + paymentDate +
                ", Amount: $" + amount +
                ", Status: " + paymentStatus +
                ", Billing ID: " + billingId +
                "}";
    }
}
