import java.time.LocalDate;

public record FighterDb(int idClub, String fighterName, String fighterStyle, int heightInCm, int weightInKg, LocalDate age, String codeCountry) {
}
