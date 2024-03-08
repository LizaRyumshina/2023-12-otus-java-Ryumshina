package Banknote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseBanknoteTest {
    @Test
    @DisplayName("Проверяем, что класс Banknote не сломан")
    void createBaseBanknoteTest() {
        assertThat(Banknote.Denomination_100.getDenomination()).isEqualTo(100);
        assertThat(Banknote.Denomination_500.getDenomination()).isEqualTo(500);
        assertThat(Banknote.Denomination_1000.getDenomination()).isEqualTo(1000);
        assertThat(Banknote.Denomination_5000.getDenomination()).isEqualTo(5000);
    }
}
