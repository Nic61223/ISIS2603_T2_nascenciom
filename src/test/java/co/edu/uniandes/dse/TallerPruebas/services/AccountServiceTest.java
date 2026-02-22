package co.edu.uniandes.dse.TallerPruebas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.TallerPruebas.entities.AccountEntity;
import co.edu.uniandes.dse.TallerPruebas.entities.PocketEntity;
import co.edu.uniandes.dse.TallerPruebas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.TallerPruebas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.TallerPruebas.repositories.AccountRepository;
import co.edu.uniandes.dse.TallerPruebas.repositories.PocketRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AccountService.class)
public class AccountServiceTest {
    
    @Autowired
    private AccountRepository repositorio;

    @Autowired
    private PocketRepository repositorio2;
    
    @Autowired
    private TestEntityManager manager;
    
    
    @Autowired
    private AccountService servicio;


    private PodamFactory factory = new PodamFactoryImpl();

    private List<Long> Ids = new ArrayList<>();


    @BeforeEach
    void setUp(){
        clearData();
        insertData();

    }

    private void clearData() {
        manager.getEntityManager().createQuery("delete from PocketEntity").executeUpdate();
        manager.getEntityManager().createQuery("delete from AccountEntity").executeUpdate();
    }
    private void insertData() {
        AccountEntity account1 = factory.manufacturePojo(AccountEntity.class);
        account1.setSaldo(1000.0);
        
        
        manager.persist(account1);
        Ids.add(account1.getId());

        AccountEntity account2 = factory.manufacturePojo(AccountEntity.class);
        account2.setSaldo(500.0);
        manager.persist(account2);
        
        Ids.add(account2.getId());


        PocketEntity bolsillo = factory.manufacturePojo(PocketEntity.class);
        bolsillo.setSaldo(0.0);

        manager.persist(bolsillo);
        Ids.add(bolsillo.getId());
    }



    @Test
    void testTransferencia() throws BusinessLogicException, EntityNotFoundException {
        servicio.transference(Ids.get(0), Ids.get(1), 200.0);

        Optional<AccountEntity> a1 = repositorio.findById(Ids.get(0));
        Optional<AccountEntity> a2 = repositorio.findById(Ids.get(1));




        assertEquals(800.0, a1.get().getSaldo());
        assertEquals(700.0, a2.get().getSaldo());
    }

    @Test
    void testTransferenceCuentaOrigenNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            servicio.transference(999L, Ids.get(1), 100.0);
        });
    }



    @Test
    void testTransferenceSaldoInsuficiente() {
        assertThrows(BusinessLogicException.class, () -> {
            servicio.transference(Ids.get(0), Ids.get(1), 9999.0);
        });
    }
    
    @Test
    void testTransferenceMismaCuenta() {
        assertThrows(BusinessLogicException.class, () -> {
            servicio.transference(Ids.get(0), Ids.get(0), 100.0);
        });
    }


    
    
    
    
    
    
    
    
    @Test
    void testTransferenciaABolsillo() throws EntityNotFoundException, BusinessLogicException {
        servicio.transferenciaABolsillo(Ids.get(0), 300.0, Ids.get(2));

        Optional<AccountEntity> a = repositorio.findById( Ids.get(0));
        Optional<PocketEntity> b = repositorio2.findById(Ids.get(2));

        assertEquals(700.0, a.get().getSaldo());
        assertEquals(300.0, b.get().getSaldo());
    }

    @Test
    void testTransferenciaABolsilloNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            servicio.transferenciaABolsillo(Ids.get(0), 100.0, 999L);
        });
    }

    @Test
    void testTransferenciaABolsilloSaldoInsuficiente() {
        assertThrows(BusinessLogicException.class, () -> {
            servicio.transferenciaABolsillo(Ids.get(0), 9999999999.0, Ids.get(2));
        });
    }

    @Test
    void testTransferenciaABolsilloCuentaNoExiste() {
        assertThrows(EntityNotFoundException.class, () -> {
            servicio.transferenciaABolsillo(999L, 100.0, Ids.get(2));
        });
    }


    }

