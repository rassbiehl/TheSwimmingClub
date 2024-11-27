package swimclub.services;

import swimclub.models.Billing;
import swimclub.models.BillingStatus;
import swimclub.models.Member;
import swimclub.models.MembershipStatus;
import swimclub.repositories.PaymentRepository;

import java.util.ArrayList;
import java.util.List;

public class BillingService {
    private final PaymentRepository paymentRepository;


    public BillingService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }




    /**
     * Calculates the annual membership fee based on the member's status and age.
     *
     * @param member The member whose membership fee is being calculated.
     * @return The calculated membership fee.
     */
    public static double calculateMembershipFee(Member member) {
        if (member.getMembershipStatus() == MembershipStatus.PASSIVE) {
            return 500; // Passive members pay a fixed fee of 500.
        }

        if (member.getMembershipStatus() == MembershipStatus.ACTIVE) {
            if (member.getAge() < 18) {
                return 1000; // Active members under 18 pay 1000.
            } else if (member.getAge() < 60) {
                return 1600; // Active members aged 18-59 pay 1600.
            } else {
                return 1600 * 0.75; // Active members 60+ receive a 25% discount.
            }
        }

        return 0; // Default case (unlikely to occur if member status is valid).
    }

    /**
     *
     * @param memberList all of the members on a list (member repository)
     * @param status - whatever status you want to get a list of
     * @return a list of members with a given status.
     */
    public List<Member> getMembersByBillingStatus(List<Member> memberList, BillingStatus status) {
        List<Member> members = new ArrayList<>();

        for (Member member : memberList) {
            Boolean x = false;
            for (Billing billing : member.getBillingsList()) {
                if (billing.getBillingStatus() == status) {
                    x = true;
                }
            }
            if (x) {
                members.add(member);
            }
        }

        return members;
    }

    /**
     * Calculates the total expected payment across all members.
     *
     * @param memberList The list of all members.
     * @return The total expected payment.
     */
    public double calculateTotalExpectedPayments(List<Member> memberList) {
        double total = 0;
        for (Member member : memberList) {
            total += calculateMembershipFee(member);
        }
        return total;
    }
}
