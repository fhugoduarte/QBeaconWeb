# QBeaconWeb

#### Sobre o projeto: 
**Parte Web** do meu projeto para o trabalho de conclusão do curso de Sistemas de Informação na Universidade Federal do Ceará.

---

#### Descrição:
Sistema web para realizar o cadastro e reserva de salas de aula, bem como gerenciar a frequência de alunos nas aulas. Este sistema é a visão administrativa, onde é possível gerenciar as principais entidades do sistema.  
*Exemplo: é possível reservar uma sala para uma determinada turma.*

---

#### Para executar o projeto você deve:
1. Criar um banco de dados com o nome **qbeacon_db**
2. Criar as seguintes funções de trigger a seguir

   *Essa função exclui todas as dependências que a entidade reserva possa ter*
   
   ```plpgsql
    create or replace function excluir_reserva() returns trigger as
    $excluir_reserva$
    Begin
	    delete from horario_reservas as hr where hr.reservas_id = old.id;

	    delete from sala_reservas as sr where sr.reservas_id = old.id;

	    update turma set reserva1_id = null where reserva1_id = old.id;

	    update turma set reserva2_id = null where reserva2_id = old.id;

	    update aula set reserva_id = null where reserva_id = old.id;

	    delete from reserva_aulas as ra where ra.reserva_id = old.id;

	    return old;
    end;
    $excluir_reserva$ language 'plpgsql';
    ````
    *Essa função exclui todas as dependências que a entidade sala possa ter*
    
    ```plpgsql
    create or replace function excluir_sala() returns trigger as
    $excluir_sala$
    Begin
	    update beacon set sala_id = null where sala_id = old.id;
	    delete from reserva where reserva.sala_id = old.id;
	    return old;
    end;
    $excluir_sala$ language 'plpgsql';
    ````
3. Criar as triggers a seguir

   *Essa trigger é executada antes da exclusão da entidade reserva, ela chama uma função que desfaz todos os relacionamentos dessa entidade para que ela possa ser excluida sem problemas*

   ```plpgsql
   create trigger excluir_dep_reserva before delete
      on reserva for each row
      execute procedure excluir_reserva();
   ````
    
   *Essa trigger é executada antes da exclusão da entidade sala, ela chama uma função que desfaz todos os relacionamentos dessa entidade para que ela possa ser excluida sem problemas*
    
   ```plpgsql
    create trigger excluir_dep_sala before delete
	    on sala for each row
	    execute procedure excluir_sala();
   ````
