package Banknote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseBanknoteTest {
    @Test
    @DisplayName("Проверяем, что класс Banknote не сломан")
    void createBaseBanknoteTest() {
        int denomination = 100;
        BaseBanknote banknote = new Banknote(denomination);
        assertThat(banknote.getDenomination()).isEqualTo(denomination);
    }
    @Test
    void lessZeroBaseBanknoteTest() {
        assertThrows(Throwable.class, () -> {
            BaseBanknote banknote = new Banknote(-1);
        });
    }
}
