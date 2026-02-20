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

    @Transactional
    public void transference(Long accountId1, Long accountId2, double cant) throws EntityNotFoundException, BusinessLogicException{
        // verificacion existencia cuentas
        Optional<AccountEntity> account1 = accountRepository.findById(accountId1);
        Optional<AccountEntity> account2 = accountRepository.findById(accountId2);

            if(account1.isEmpty() || account2.isEmpty()){
                throw new  EntityNotFoundException("Una de las cuentas no existe");
            }
            
            if (account1.equals(account2)){
                throw new BusinessLogicException("La cuenta destino y fuente son la misma");
            }

            if (account1.get().getSaldo()>0)
            {
                throw new BusinessLogicException("La cuenta destino y fuente son la misma");
            
            }

            






    }
}
