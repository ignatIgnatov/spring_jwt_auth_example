package net.codejava.role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleEntityRepositoryTests {
    @Autowired
    private RoleRepository repo;

    @Test
    public void testSaveRoles() {
        RoleEntity admin = new RoleEntity("ROLE_ADMIN");
        RoleEntity editor = new RoleEntity("ROLE_EDITOR");
        RoleEntity customer = new RoleEntity("ROLE_CUSTOMER");

        repo.saveAll(List.of(admin, editor, customer));

        long count = repo.count();
        assertEquals(3, count);
    }
}
