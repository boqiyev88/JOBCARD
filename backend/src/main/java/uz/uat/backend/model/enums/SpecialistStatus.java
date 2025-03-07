package uz.uat.backend.model.enums;

public enum SpecialistStatus {

    NEW,         ///  yaratilganda
    PENDING,     ///  tasdiqlashga kelganda
    IN_PROCESS,  ///  ish bajarilyapti
    CONFIRMED,   ///  tasdiqlandi
    REJECTED,   ///   rad etildi
    COMPLETED,  ///   to'liq tugatildi
}
