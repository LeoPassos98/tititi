-- ============================================
-- ARQUIVO: criar_tabela.sql
-- TEMA: Linguagens de Programação
-- AUTOR: Leonardo Araujo Passos
-- DATA: 22/10/2025
-- DISCIPLINA: Trabalho Interdisciplinar - PUC Minas
-- ============================================

-- PASSO 1: Criar a tabela "linguagens_programacao"
-- Esta tabela vai armazenar informações sobre linguagens de programação
CREATE TABLE linguagens_programacao (
    id SERIAL PRIMARY KEY,                  -- ID único e automático
    nome VARCHAR(50) NOT NULL,              -- Nome da linguagem (obrigatório)
    paradigma VARCHAR(50),                  -- Paradigma: OOP, Funcional, etc.
    ano_criacao INT,                        -- Ano em que foi criada
    criador VARCHAR(100),                   -- Quem criou a linguagem
    popularidade_ranking INT,               -- Posição no ranking de popularidade
    usado_para VARCHAR(150),                -- Principais aplicações
    dificuldade VARCHAR(20)                 -- Nível: Fácil, Médio, Difícil
);

-- Verificar se a tabela foi criada com sucesso


-- ============================================
-- PASSO 2: Inserir linguagens na tabela
-- Vamos adicionar 8 linguagens diferentes
-- ============================================

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('Python', 'Multi-paradigma', 1991, 'Guido van Rossum', 1, 
 'Data Science, IA, Web, Automação', 'Fácil');

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('JavaScript', 'Multi-paradigma', 1995, 'Brendan Eich', 2, 
 'Desenvolvimento Web, Frontend, Backend', 'Médio');

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('Java', 'Orientada a Objetos', 1995, 'James Gosling', 3, 
 'Aplicações Enterprise, Android, Backend', 'Médio');

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('C++', 'Multi-paradigma', 1985, 'Bjarne Stroustrup', 4, 
 'Sistemas, Games, Software de Alto Desempenho', 'Difícil');

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('C#', 'Orientada a Objetos', 2000, 'Microsoft/Anders Hejlsberg', 5, 
 'Aplicações Windows, Unity, Web com .NET', 'Médio');

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('Go', 'Multi-paradigma', 2009, 'Google (Robert Griesemer)', 8, 
 'Cloud, Microserviços, Sistemas Distribuídos', 'Médio');

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('Rust', 'Multi-paradigma', 2010, 'Mozilla/Graydon Hoare', 12, 
 'Sistemas Seguros, Performance Crítica', 'Difícil');

INSERT INTO linguagens_programacao 
(nome, paradigma, ano_criacao, criador, popularidade_ranking, usado_para, dificuldade) 
VALUES 
('TypeScript', 'Multi-paradigma', 2012, 'Microsoft/Anders Hejlsberg', 7, 
 'Desenvolvimento Web, Frontend com tipos', 'Médio');


-- ============================================
-- PASSO 3: Verificar se os dados foram inseridos
-- Execute este SELECT para ver todos os resultados
-- ============================================

SELECT * FROM linguagens_programacao;


-- ============================================
-- CONSULTAS EXTRAS E ÚTEIS
-- Você pode testar essas queries depois!
-- ============================================

-- 1) Ver apenas linguagens fáceis de aprender
-- SELECT nome, ano_criacao, usado_para 
-- FROM linguagens_programacao 
-- WHERE dificuldade = 'Fácil';

-- 2) Ver linguagens ordenadas por popularidade
-- SELECT nome, popularidade_ranking, usado_para 
-- FROM linguagens_programacao 
-- ORDER BY popularidade_ranking ASC;

-- 3) Ver linguagens criadas após o ano 2000
-- SELECT nome, ano_criacao, criador 
-- FROM linguagens_programacao 
-- WHERE ano_criacao >= 2000;

-- 4) Ver linguagens modernas (últimos 15 anos) e suas aplicações
-- SELECT nome, ano_criacao, usado_para 
-- FROM linguagens_programacao 
-- WHERE ano_criacao >= 2010 
-- ORDER BY ano_criacao DESC;

-- 5) Contar quantas linguagens temos por nível de dificuldade
-- SELECT dificuldade, COUNT(*) as quantidade 
-- FROM linguagens_programacao 
-- GROUP BY dificuldade;

-- 6) Ver linguagens usadas para Web
-- SELECT nome, usado_para, dificuldade 
-- FROM linguagens_programacao 
-- WHERE usado_para LIKE '%Web%';

-- 7) Ver as 3 linguagens mais populares
-- SELECT nome, popularidade_ranking, usado_para 
-- FROM linguagens_programacao 
-- ORDER BY popularidade_ranking ASC 
-- LIMIT 3;

-- 8) Ver criadores e suas linguagens
-- SELECT criador, nome, ano_criacao 
-- FROM linguagens_programacao 
-- ORDER BY criador;


-- ============================================
-- ESTATÍSTICAS INTERESSANTES
-- ============================================

-- Idade média das linguagens
-- SELECT ROUND(AVG(2025 - ano_criacao), 1) as idade_media 
-- FROM linguagens_programacao;

-- Linguagem mais antiga e mais nova
-- SELECT 
--     (SELECT nome FROM linguagens_programacao ORDER BY ano_criacao ASC LIMIT 1) as mais_antiga,
--     (SELECT nome FROM linguagens_programacao ORDER BY ano_criacao DESC LIMIT 1) as mais_nova;

SELECT * FROM linguagens_programacao;