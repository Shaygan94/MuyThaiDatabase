import java.time.LocalDate;

public record Fighter(int idClub, String fighterName, String fighterStyle, int heightInCm, int weightInKg, LocalDate age, String codeCountry, String clubName) {
}
