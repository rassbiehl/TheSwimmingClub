package swimclub.models;

public enum MemberPaymentStatus {
    ALL_BILLS_PAID,       // All bills are fully paid
    MISSING_PAYMENT,      // Some bills are unpaid or partially paid
    OVERDUE_BILLS,        // Some bills are overdue
    PENDING_PAYMENT,      // Member has some pending payments (or in progress)
}
