package swimclub.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Billing {
    private int billingId; // id for each bill.
    private int memberId; // links to a specific member.
    private double amount;// the total charge for the bill.
    private double amountPaid; // the total amount of money that has been paid on the bill.
    private LocalDate dueDate; // deadline of the payment
    private LocalDate billingDate; // the date that the bill was issued to a member.
    private BillingStatus billingStatus; // can either be PAID, PARTIALLY_PAID, NOT_PAID or OVERDUE
    private List<Payment> payments = new ArrayList<>(); // a list of all the payments made toward a bill.

    /**
     * Constructor for Billing.
     *
     * @param billingId     Unique billing ID.
     * @param memberId       ID of the member the bill is for.
     * @param amount         Total amount of the bill.
     * @param billingDate    Date the bill was issued.
     * @param dueDate         Due date for the bill.
     * @throws IllegalArgumentException if amount is not positive.
     * @throws NullPointerException     if billingDate or dueDate is null.
     */
    public Billing(int billingId, int memberId, double amount, LocalDate dueDate, LocalDate billingDate) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (billingDate == null || dueDate == null) {
            throw new NullPointerException("Billing date and Due date cannot be null.");
        }

        this.billingId = billingId;
        this.memberId = memberId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.billingDate = billingDate;
        this.billingStatus = BillingStatus.NOT_PAID; // by default a bill is not paid. might change it later.
        this.amountPaid = 0; // 0 by default. might change it later.
    }

    //GetMethods-----------------------------------------------------------------------------------------------------------------------------
    public int getBillId() {return billingId;}

    public double getAmountDue() {return amount;}

    public LocalDate getBillingDate() {return billingDate;}

    public LocalDate getDueDate() {return dueDate;}

    public BillingStatus getBillingStatus() {return billingStatus;}

    public int getBillingMemberId() {return memberId;}

    public double getAmountPaid () {return this.amountPaid;}

    //SetMethods---------------------------------------------------------------------------------------------------
    public void setBillingStatus(BillingStatus billingStatus) {this.billingStatus = billingStatus;}


    //----------------------------------------------------------------------------------------------------------------

    public double getMissingAmount() {
        if (amount - amountPaid > 0) {
            return amount - amountPaid;
        }
        return 0;
    }
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate) && billingStatus != BillingStatus.PAID;
    }

    public void checkOverdueStatus() {
        if (isOverdue() && billingStatus != BillingStatus.PAID) {
            billingStatus = BillingStatus.OVERDUE;
        }
    }

    public void addPayment(Payment newPayment) {
        if (newPayment.getAmount() < 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative");
        }

        //adds the payment to the list of all the payments towards the bill.
        payments.add(newPayment);
        // Add the payment amount to the total paid for the bill
        this.amountPaid += newPayment.getAmount();

        // Update the billing status based on the amount paid
        if (this.amountPaid >= this.amount) {
            this.billingStatus = BillingStatus.PAID; // Bill is fully paid
        } else if (this.amountPaid > 0) {
            this.billingStatus = BillingStatus.PARTIALLY_PAID; // Bill is partially paid
        }

        checkOverdueStatus(); // status will always be up to date with this method.
    }

    public BillingStatus payBill(Payment newPayment) {
        if (newPayment.getAmount() < 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative");
        }

        // Add the payment to the bill
        addPayment(newPayment);

        // If the bill is paid, return the status
        if (billingStatus == BillingStatus.PAID) {
            return BillingStatus.PAID; // Bill is fully paid
        }

        // Check if the bill is overdue
        checkOverdueStatus();

        // Return current billing status (either PARTIALLY_PAID or OVERDUE)
        return billingStatus;
    }


    @Override
    public String toString() {
        return "Billing{" +
                "billingId=" + billingId +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", amountPaid=" + amountPaid +
                ", dueDate=" + dueDate +
                ", billingDate=" + billingDate +
                ", billingStatus=" + billingStatus +
                '}';
    }
}
