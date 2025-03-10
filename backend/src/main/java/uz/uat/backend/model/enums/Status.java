package uz.uat.backend.model.enums;

public enum Status {
    NEW,           ///  yaratilganda
    PENDING,      ///  tasdiqlashga kelganda
    AWAITING,     /// texnik kutish
    IN_PROCESS,   ///  ish bajarilyapti
    CONFIRMED,    ///  tasdiqlandi
    REJECTED,    ///   rad etildi
    COMPLETED,   ///   to'liq tugatildi
}
