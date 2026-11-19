package contabancaria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testes unitários para a classe Conta.
 *
 * PARTE 1 — Testes de exemplo (Construtor) já estão prontos.
 * Observe o padrão AAA e o uso de @Test e @ParameterizedTest.
 *
 * PARTE 2 — Você deve escrever os testes para os demais métodos
 * seguindo rigorosamente o ciclo TDD: Red → Green → Refactor.
 *
 * Para cada método da classe Conta, crie testes que cubram:
 * ✅ O cenário de sucesso (caminho feliz)
 * ❌ Cada regra de validação (cenários de exceção)
 * 🔄 Casos de borda (valores limites)
 */
class ContaTest {

    // =======================================================
    // PARTE 1 — EXEMPLO GUIADO: Testes do Construtor
    // Observe o padrão Arrange-Act-Assert (AAA)
    // =======================================================

    @Test
    void construtor_DadosValidos_CriaContaCorretamente() {
        // Arrange & Act
        var conta = new Conta("Maria", 100);

        // Assert
        assertEquals("Maria", conta.getTitular());
        assertEquals(100, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_SemSaldoInicial_CriaContaComSaldoZero() {
        // Arrange & Act
        var conta = new Conta("João");

        // Assert
        assertEquals("João", conta.getTitular());
        assertEquals(0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_TitularNulo_LancaIllegalArgumentException() {
        // Assert — verifica que a exceção é lançada
        assertThrows(IllegalArgumentException.class, () -> new Conta(null));
    }

    @Test
    void construtor_TitularVazio_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(""));
    }

    @Test
    void construtor_SaldoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("Maria", -50));
    }

    @ParameterizedTest
    @CsvSource({
            "Ana,    0",
            "Carlos, 1000",
            "Beatriz, 0.01"
    })
    void construtor_VariosValoresValidos_CriaContaCorretamente(String titular, double saldo) {
        // Act
        var conta = new Conta(titular, saldo);

        // Assert
        assertEquals(titular, conta.getTitular());
        assertEquals(saldo, conta.getSaldo(), 0.001);
        assertTrue(conta.isAtiva());
    }

    // =======================================================
    // PARTE 2 — ESCREVA OS TESTES ABAIXO (TDD)
    // Lembre-se: escreva o teste PRIMEIRO, veja FALHAR (Red),
    // depois implemente o código para PASSAR (Green),
    // e por fim faça Refactor se necessário.
    // =======================================================

    // =======================================================
    // Testes para depositar
    // Sugestão de testes:
    // - Depósito com valor válido atualiza o saldo
    @Test
    void depositar_ValorValido_AtualizaSaldo() {
        // Arrange
        var conta = new Conta("Maria", 100);

        // Act
        conta.depositar(50);

        // Assert
        assertEquals(150, conta.getSaldo());
    }

    // - Depósito com valor zero lança IllegalArgumentException
    @Test
    void depositar_ValorZero_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(0));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    // - Depósito com valor negativo lança IllegalArgumentException
    @Test
    void depositar_ValorNegativo_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(-50));
        assertEquals(100, conta.getSaldo());
    }

    // - Depósito em conta inativa lança IllegalStateException
    @Test
    void depositar_ContaInativa_LancaIllegalStateException() {
        // Arrange
        var conta = new Conta("Maria");
        conta.encerrar();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> conta.depositar(50));

        // Assert
        assertEquals(0, conta.getSaldo());
        assertTrue(!conta.isAtiva());
    }
    // =======================================================

    // =======================================================
    // Testes para sacar
    // Sugestão de testes:
    // - Saque com valor válido atualiza o saldo
    @Test
    void sacar_ValorValido_AtualizaSaldo() {
        // Arrange
        var conta = new Conta("Maria", 100);

        // Act
        conta.sacar(60);

        // Assert
        assertEquals(40, conta.getSaldo());
    }

    // - Saque com valor maior que saldo lança IllegalStateException
    @Test
    void sacar_ValorMaiorQueSaldo_LancaIllegalStateException() {
        // Arrange
        var conta = new Conta("Maria", 100);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> conta.sacar(101));
        assertEquals(100, conta.getSaldo());
    }

    // - Saque com valor zero lança IllegalArgumentException
    @Test
    void sacar_ValorZero_LancaIllegalArgumentException() {
        // Arrange
        var conta = new Conta("Maria", 100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(0));
        assertEquals(100, conta.getSaldo());
    }

    // - Saque com valor negativo lança IllegalArgumentException
    @Test
    void sacar_ValorNegativo_LancaIllegalArgumentException() {
        // Arrange
        var conta = new Conta("Maria", 100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> conta.sacar(-10));
        assertEquals(100, conta.getSaldo());
    }

    // - Saque em conta inativa lança IllegalStateException
    @Test
    void sacar_ContaInativa_LancaIllegalStateException() {
        // Arrange
        var conta = new Conta("Maria");
        conta.encerrar();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> conta.sacar(50));

        // Assert
        assertEquals(0, conta.getSaldo());
        assertTrue(!conta.isAtiva());
    }
    // =======================================================

    // =======================================================
    // Testes para transferir
    // Sugestão de testes:
    // - Transferência válida atualiza saldo de ambas as contas
    @Test
    void transferir_ValorValido_AtualizaSaldoDeAmbasAsContas() {
        // Arrange
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        // Act
        origem.transferir(destino, 50);

        // Assert
        assertEquals(150, origem.getSaldo());
        assertEquals(150, destino.getSaldo());
    }

    // - Transferência com saldo insuficiente lança exceção
    @Test
    void transferir_SaldoInsuficiente_LancaIllegalStateException() {
        // Arrange
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 201));

        // Assert (conferir que nada mudou)
        assertEquals(200, origem.getSaldo());
        assertEquals(100, destino.getSaldo());
    }

    // - Transferência com valor zero/negativo lança exceção
    @ParameterizedTest
    @ValueSource(doubles = { 0.0, -10.0 })
    void transferir_ValorNaoPositivo_LancaIllegalArgumentException(double valor) {
        // Arrange
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> origem.transferir(destino, valor));

        // Assert (nada mudou)
        assertEquals(200, origem.getSaldo());
        assertEquals(100, destino.getSaldo());

    }

    // - Transferência com conta origem inativa lança exceção
    @Test
    void transferir_ContaOrigemInativa_LancaIllegalStateException() {
        // Arrange
        var origem = new Conta("Maria");
        origem.encerrar();
        var destino = new Conta("João", 100);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 50));

        // Assert (nada mudou)
        assertEquals(0, origem.getSaldo());
        assertEquals(100, destino.getSaldo());

    }

    // - Transferência com conta destino inativa lança exceção
    @Test
    void transferir_ContaDestinoInativa_LancaIllegalStateException() {
        // Arrange
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João");
        destino.encerrar();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 50));

        // Assert (nada mudou)
        assertEquals(200, origem.getSaldo());
        assertEquals(0, destino.getSaldo());

    }
    // =======================================================

    // =======================================================
    // Testes para encerrar
    // Sugestão de testes:

    // - Encerrar conta com saldo zero funciona
    // - Conta encerrada tem isAtiva() == false
    @Test
    void encerrar_ContaSaldoZero_DesativaConta() {
        // Arrange
        var conta = new Conta("Maria");

        // Act
        conta.encerrar();

        // Assert
        assertTrue(!conta.isAtiva());
    }

    // - Encerrar conta com saldo lança IllegalStateException
    @Test
    void encerrar_ContaSaldoPositivo_LancaIllegalStateException() {
        // Arrange
        var conta = new Conta("Maria", 100);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }

    // - Encerrar conta já inativa lança IllegalStateException
    @Test
    void encerrar_ContaInativa_LancaIllegalStateException() {
        // Arrange
        var conta = new Conta("Maria");

        // Act
        conta.encerrar();

        // Assert
        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }
    // =======================================================

}
