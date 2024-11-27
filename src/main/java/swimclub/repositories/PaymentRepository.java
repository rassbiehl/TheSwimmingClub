package swimclub.repositories;

import swimclub.models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing Payment entities.
 */
public class PaymentRepository {
    private final List<Payment> payments;
    private final List<Billing> billings;

    // Constructor initializes an empty list of payments and bills;
    public PaymentRepository() {
        this.payments = new ArrayList<>();
        this.billings = new ArrayList<>();
    }
//Payment methods and logic -----------------------------------------------------------------------------------------------------

    /**
     * Adds a new payment to the repository.
     *
     * @param payment The payment to add.
     */
    public void savePayment(Payment payment) {
        payments.add(payment);
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param paymentId The ID of the payment to retrieve.
     * @return The payment if found, or null otherwise.
     */
    public Payment findPaymentById(int paymentId) {

        return payments.stream()
                .filter(p -> p.getPaymentId() == paymentId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all payments in the repository.
     *
     * @return A list of all payments.
     */
    public List<Payment> findAllPayments() {
        return new ArrayList<>(payments); // Return a copy to avoid external modification
    }

    /**
     * Retrieves all payments for a specific member by their ID.
     *
     * @param memberId The member ID to search for payments.
     * @return A list of payments for the specified member.
     */
    public List<Payment> findPaymentsByMemberId(int memberId) {
        List<Payment> memberPayments = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getMember().getMemberId() == memberId) {
                memberPayments.add(payment);
            }
        }
        return memberPayments;
    }

    /**
     * Get the next available payment ID (similar to getNextMemberId)
     *
     * @return The next available payment ID.
     */
    public int getNextPaymentId() {
        return payments.size() + 1;  // Assuming payment IDs are sequential
    }

    //Billing methods and logic -----------------------------------------------------------------------------------------------------

    /**
     * Adds a new billing to the repository
     *
     * @param billing The billing to add.
     */
    public void save(Billing billing) {
        billings.add(billing);
    }


    /**
     * Finds a specific billing by it's id.
     *
     * @param billId The bill ID to search for billings.
     * @return the first bill id on the billings-list that matches with the bill id.
     */

    public Billing findBillById(int billId) {
        return billings.stream()  // Stream makes it easy to access a sequence of elements in an array. (You can also use loops.)

                .filter(b -> b.getBillId() == billId)  /* Filters the billings-list, and the condition is that the bill-id
                 of the parameter has to be equal to the id of a bill on the billing-list */

                .findFirst()  // Returns the first match
                .orElse(null);  // If no match is found, it returns null
    }

    /**
     * Finds all bills in the repository
     *
     * @return all the billings, not just for a specific member, but for all members registered in repository.
     */
    public List<Billing> findAllBills() {
        return new ArrayList<>(billings); // returns a copy of the existing billings
    }

    /**
     * Finds a specific bill by a member's id.
     *
     * @param memberId The member ID to search for all the member's billings.
     * @return a list of all the members billings;
     */
    public List<Billing> findBillsByMemberId(int memberId) {
        List<Billing> memberBills = new ArrayList<>();
        for (Billing billing : billings) {
            if (billing.getBillingMemberId() == memberId) {
                memberBills.add(billing);
            }
        }
        return memberBills;
    }

    /** makes sure that every id is unique and is a way of generating ids.
     *
     * @return a unique id for each billing.
     */
    public int getNextBillId() {
        return billings.size() + 1;
    }

    /**
     * the paybill method creates a payment, saves the payment, checks if the payment is enough to pay the billing, if so a new
     * billing will be created and the duedate will be one year later (we can can change that as an option later on) and this new billing
     * will be added to the billings-list.
     * @param billingId
     * @param paymentAmount
     * @param member
     */
    public void payBill (int billingId, double paymentAmount, Member member) {
        Billing billing = findBillById(billingId);

        if (billing == null) {
            System.out.println("Billing not found with ID: " + billingId);
            return;
        }

        // Process the payment by creating a Payment
        Payment newPayment = new Payment(getNextPaymentId(), PaymentStatus.COMPLETE, member, LocalDate.now(), paymentAmount, billingId);

        // Save the payment
        savePayment(newPayment);

        BillingStatus billingStatus = billing.payBill(newPayment);

        if (billingStatus == BillingStatus.NOT_PAID) {
            System.out.println("Nothing has been paid.");
        }
        else if (billingStatus == BillingStatus.PARTIALLY_PAID) {
            if (billing.getMissingAmount() > 0) {
                System.out.println("You have now payed " + paymentAmount + "kr, but you are missing" + billing.getMissingAmount() + "kr");
            }
        }
        else if (billingStatus == BillingStatus.PAID) {
            // Create the next bill for the member
            Billing newBill = createNextBill(billing);
            System.out.println("You now have to pay again " + newBill.getDueDate());
            save(newBill);

            System.out.println("Bill ID " + billingId + " has been paid. A new bill has been created.");
            System.out.println("Next billing: " + newBill);
        }

    }

    /**
     * when this method is used, it's when a bill is paid and you have to give the member another billing with the duedate after one year.
     * it creates a whole new billing object and assigns it to the member.
     * @param currentBill
     * @return
     */
    public Billing createNextBill (Billing currentBill) {
        LocalDate nextBillingDate = currentBill.getBillingDate().plusYears(1);
        LocalDate nextDueDate = currentBill.getDueDate().plusYears(1);

        Billing nextBill = new Billing(currentBill.getBillingMemberId(), currentBill.getBillingMemberId(), currentBill.getAmountDue(),nextBillingDate, nextDueDate);

        // Save the next bill in the repository
        save(nextBill);

        return nextBill;
    }
}
