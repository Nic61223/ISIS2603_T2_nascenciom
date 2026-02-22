package co.edu.uniandes.dse.TallerPruebas.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.TallerPruebas.entities.AccountEntity;
import co.edu.uniandes.dse.TallerPruebas.entities.PocketEntity;
import co.edu.uniandes.dse.TallerPruebas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.TallerPruebas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.TallerPruebas.repositories.AccountRepository;
import co.edu.uniandes.dse.TallerPruebas.repositories.PocketRepository;

@Service

public class AccountService {


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PocketRepository pocketRepository;
//regla 2
    @Transactional
    public void transference(Long accountId1, Long accountId2, double cant) throws EntityNotFoundException, BusinessLogicException{
        Optional<AccountEntity> account1 = accountRepository.findById(accountId1);
        Optional<AccountEntity> account2 = accountRepository.findById(accountId2);
            // verfica existencia e las 2 cuentas
            if(account1.isEmpty() || account2.isEmpty()){
                throw new  EntityNotFoundException("Una de las cuentas no existe");
            }
            // verifica que las 2 cuentas sean diferentes
            if (account1.equals(account2)){
                throw new BusinessLogicException("La cuenta destino y fuente son la misma");
            }
            // verifica que el saldo enviado no sea mayor al saldo de la cuenta saliente
            if (account1.get().getSaldo() < cant)
            {
                throw new BusinessLogicException("La cuenta tiene un saldo insuficioente");
            
            }
            // Actualiza la informacion en cada entidad
            account1.get().setSaldo(account1.get().getSaldo()-cant);
            account2.get().setSaldo(account2.get().getSaldo()+cant);
            accountRepository.save(account1.get());
            accountRepository.save(account2.get());
    }

// regla 1
    public void transferenciaABolsillo(Long accountId, double cantidad, Long bolsilloId ) throws EntityNotFoundException, BusinessLogicException{
        Optional<AccountEntity> cuenta = accountRepository.findById(accountId);
        Optional<PocketEntity> bolsillo = pocketRepository.findById(bolsilloId);

        if (cuenta.isEmpty()) {
            throw new EntityNotFoundException("No existe una cuenta con este Id");
        }
        if (bolsillo.isEmpty()) {
            throw new EntityNotFoundException("No se encontro ningun bolsillo con este Id");
        }
        if (cuenta.get().getSaldo() < cantidad) {
            throw new BusinessLogicException("El saldo no es suficioente");
        }

        cuenta.get().setSaldo(cuenta.get().getSaldo()- cantidad);
        bolsillo.get().setSaldo(bolsillo.get().getSaldo()+cantidad);

        accountRepository.save(cuenta.get());
        pocketRepository.save(bolsillo.get());


    }





}
