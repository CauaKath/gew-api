ALTER TABLE secoes ADD CONSTRAINT fk_responsavel_secao
FOREIGN KEY (responsavel_id) REFERENCES funcionarios(numero_cracha);

ALTER TABLE secoes_pagantes ADD CONSTRAINT fk_secao_pagante
FOREIGN KEY (secao_id) REFERENCES secoes(id);

ALTER TABLE secoes_pagantes ADD CONSTRAINT fk_projeto_secoes
FOREIGN KEY (projeto_id) REFERENCES projetos(id);

ALTER TABLE despesas ADD CONSTRAINT fk_projeto_despesas
FOREIGN KEY (projeto_id) REFERENCES projetos(id);

ALTER TABLE funcionarios_secoes ADD CONSTRAINT fk_funcionario_cracha
FOREIGN KEY (funcionario_cracha) REFERENCES funcionarios(numero_cracha);

ALTER TABLE funcionarios_secoes ADD CONSTRAINT fk_secao_id
FOREIGN KEY (secao_id) REFERENCES secoes(id);

ALTER TABLE cargos_funcionarios ADD CONSTRAINT fk_funcionario_cargo
FOREIGN KEY (funcionario_cracha) REFERENCES funcionarios(numero_cracha);

ALTER TABLE cargos_funcionarios ADD CONSTRAINT fk_cargo_id
FOREIGN KEY (cargo_id) REFERENCES cargos(id);