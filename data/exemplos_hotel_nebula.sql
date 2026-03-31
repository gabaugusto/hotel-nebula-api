-- Exemplos SQL (MySQL 8+) - Hotel Nebula
CREATE DATABASE IF NOT EXISTS hotel_nebula;
USE hotel_nebula;

DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS pagamentos;
DROP TABLE IF EXISTS hospedagem_servicos;
DROP TABLE IF EXISTS servicos;
DROP TABLE IF EXISTS hospedagens;
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS quartos;
DROP TABLE IF EXISTS funcionarios;
DROP TABLE IF EXISTS hospedes;

CREATE TABLE hospedes (
    id_hospede INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_nascimento DATE,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE funcionarios (
    id_funcionario INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    departamento VARCHAR(50),
    email VARCHAR(120) UNIQUE,
    telefone VARCHAR(20),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE quartos (
    id_quarto INT PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    tipo VARCHAR(40) NOT NULL,
    capacidade INT NOT NULL,
    preco_diaria DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    andar INT,
    vista VARCHAR(40)
) ENGINE=InnoDB;

CREATE TABLE reservas (
    id_reserva INT PRIMARY KEY,
    id_hospede INT NOT NULL,
    id_quarto INT NOT NULL,
    data_reserva DATETIME NOT NULL,
    data_checkin DATE NOT NULL,
    data_checkout DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    canal VARCHAR(30) NOT NULL,
    valor_previsto DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_reserva_hospede FOREIGN KEY (id_hospede) REFERENCES hospedes(id_hospede),
    CONSTRAINT fk_reserva_quarto FOREIGN KEY (id_quarto) REFERENCES quartos(id_quarto)
) ENGINE=InnoDB;

CREATE TABLE hospedagens (
    id_hospedagem INT PRIMARY KEY,
    id_reserva INT NOT NULL UNIQUE,
    id_funcionario_checkin INT,
    id_funcionario_checkout INT,
    checkin_real DATETIME,
    checkout_real DATETIME,
    status VARCHAR(20) NOT NULL,
    total_diarias DECIMAL(10,2) NOT NULL,
    total_servicos DECIMAL(10,2) NOT NULL,
    total_geral DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_hospedagem_reserva FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva),
    CONSTRAINT fk_hospedagem_func_checkin FOREIGN KEY (id_funcionario_checkin) REFERENCES funcionarios(id_funcionario),
    CONSTRAINT fk_hospedagem_func_checkout FOREIGN KEY (id_funcionario_checkout) REFERENCES funcionarios(id_funcionario)
) ENGINE=InnoDB;

CREATE TABLE servicos (
    id_servico INT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL,
    categoria VARCHAR(40) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE hospedagem_servicos (
    id_consumo INT PRIMARY KEY,
    id_hospedagem INT NOT NULL,
    id_servico INT NOT NULL,
    data_consumo DATETIME NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_consumo_hospedagem FOREIGN KEY (id_hospedagem) REFERENCES hospedagens(id_hospedagem),
    CONSTRAINT fk_consumo_servico FOREIGN KEY (id_servico) REFERENCES servicos(id_servico)
) ENGINE=InnoDB;

CREATE TABLE pagamentos (
    id_pagamento INT PRIMARY KEY,
    id_hospedagem INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    metodo VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_pagamento DATETIME,
    transacao_id VARCHAR(60),
    CONSTRAINT fk_pagamento_hospedagem FOREIGN KEY (id_hospedagem) REFERENCES hospedagens(id_hospedagem)
) ENGINE=InnoDB;

CREATE TABLE feedbacks (
    id_feedback INT PRIMARY KEY,
    id_hospede INT NOT NULL,
    id_quarto INT NOT NULL,
    id_hospedagem INT NOT NULL,
    nota_geral DECIMAL(2,1) NOT NULL,
    comentario TEXT,
    data_avaliacao DATETIME NOT NULL,
    CONSTRAINT fk_feedback_hospede FOREIGN KEY (id_hospede) REFERENCES hospedes(id_hospede),
    CONSTRAINT fk_feedback_quarto FOREIGN KEY (id_quarto) REFERENCES quartos(id_quarto),
    CONSTRAINT fk_feedback_hospedagem FOREIGN KEY (id_hospedagem) REFERENCES hospedagens(id_hospedagem)
) ENGINE=InnoDB;

INSERT INTO hospedes (id_hospede, nome, email, cpf, telefone, data_nascimento, data_cadastro, ativo) VALUES
(1, 'Carlos Andrade', 'carlos.andrade@email.com', '123.456.789-00', '+55 11 98765-4321', '1985-03-22', '2025-01-10 10:30:00', TRUE),
(2, 'Marina Costa', 'marina.costa@email.com', '456.789.123-55', '+55 21 99876-1111', '1992-08-15', '2025-01-18 14:10:00', TRUE),
(3, 'Pedro Lima', 'pedro.lima@email.com', '321.654.987-10', '+55 31 97777-2222', '1979-12-02', '2025-02-02 09:00:00', TRUE);

INSERT INTO funcionarios (id_funcionario, nome, cargo, departamento, email, telefone, ativo) VALUES
(1, 'Juliana Martins', 'Recepcionista', 'Recepção', 'juliana.martins@hotelnebula.com', '+55 21 99123-4567', TRUE),
(2, 'Rafael Nunes', 'Recepcionista', 'Recepção', 'rafael.nunes@hotelnebula.com', '+55 21 99222-1111', TRUE),
(3, 'Ana Prado', 'Supervisora', 'Operações', 'ana.prado@hotelnebula.com', '+55 21 99333-2222', TRUE);

INSERT INTO quartos (id_quarto, numero, tipo, capacidade, preco_diaria, status, andar, vista) VALUES
(1, '101', 'standard', 2, 250.00, 'disponivel', 1, 'jardim'),
(2, '202', 'luxo', 3, 420.00, 'ocupado', 2, 'mar'),
(3, '303', 'suite', 4, 680.00, 'disponivel', 3, 'mar');

INSERT INTO reservas (id_reserva, id_hospede, id_quarto, data_reserva, data_checkin, data_checkout, status, canal, valor_previsto) VALUES
(1, 1, 1, '2025-03-28 14:22:00', '2025-04-10', '2025-04-14', 'confirmada', 'site_proprio', 1000.00),
(2, 2, 2, '2025-03-30 16:00:00', '2025-04-12', '2025-04-15', 'confirmada', 'agencia_online', 1260.00),
(3, 3, 3, '2025-04-01 11:45:00', '2025-04-20', '2025-04-23', 'cancelada', 'telefone', 2040.00);

INSERT INTO hospedagens (id_hospedagem, id_reserva, id_funcionario_checkin, id_funcionario_checkout, checkin_real, checkout_real, status, total_diarias, total_servicos, total_geral) VALUES
(1, 1, 1, 2, '2025-04-10 14:35:00', '2025-04-14 11:10:00', 'encerrada', 1000.00, 245.00, 1245.00),
(2, 2, 2, NULL, '2025-04-12 15:20:00', NULL, 'em_andamento', 1260.00, 90.00, 1350.00);

INSERT INTO servicos (id_servico, nome, categoria, preco, disponivel) VALUES
(1, 'Café da manhã', 'alimentacao', 45.00, TRUE),
(2, 'Lavanderia', 'comodidade', 80.00, TRUE),
(3, 'Minibar', 'alimentacao', 25.00, TRUE),
(4, 'Transfer aeroporto', 'transporte', 120.00, TRUE);

INSERT INTO hospedagem_servicos (id_consumo, id_hospedagem, id_servico, data_consumo, quantidade, valor_unitario, valor_total) VALUES
(1, 1, 1, '2025-04-11 08:15:00', 2, 45.00, 90.00),
(2, 1, 2, '2025-04-12 10:00:00', 1, 80.00, 80.00),
(3, 1, 3, '2025-04-13 22:30:00', 3, 25.00, 75.00),
(4, 2, 1, '2025-04-13 08:20:00', 2, 45.00, 90.00);

INSERT INTO pagamentos (id_pagamento, id_hospedagem, valor, metodo, status, data_pagamento, transacao_id) VALUES
(1, 1, 1000.00, 'cartao_credito', 'aprovado', '2025-04-14 11:05:00', 'TXN-VISA-00392'),
(2, 1, 245.00, 'pix', 'aprovado', '2025-04-14 11:06:00', 'TXN-PIX-00841'),
(3, 2, 300.00, 'cartao_debito', 'pendente', NULL, 'TXN-DEB-00077');

INSERT INTO feedbacks (id_feedback, id_hospede, id_quarto, id_hospedagem, nota_geral, comentario, data_avaliacao) VALUES
(1, 1, 1, 1, 4.5, 'Ótima estadia, equipe atenciosa e quarto muito limpo.', '2025-04-15 09:00:00');
