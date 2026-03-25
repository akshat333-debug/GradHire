package com.gradhire.dao;

import com.gradhire.model.Skill;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillDaoIntegrationTest {
    private static final SkillDao SKILL_DAO = new SkillDao();

    @BeforeAll
    static void setup() {
        DaoIntegrationTestSupport.assumeDatabaseAvailable();
    }

    @Test
    void createAndUpdateSkill() throws SQLException {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        int skillId = SKILL_DAO.createSkill("Skill-" + suffix, "Testing");

        Optional<Skill> created = SKILL_DAO.findById(skillId);
        assertTrue(created.isPresent());
        assertEquals("Testing", created.get().getCategory());

        boolean updated = SKILL_DAO.updateSkill(skillId, "SkillUpdated-" + suffix, "Automation");
        assertTrue(updated);

        Optional<Skill> updatedSkill = SKILL_DAO.findById(skillId);
        assertTrue(updatedSkill.isPresent());
        assertEquals("SkillUpdated-" + suffix, updatedSkill.get().getSkillName());
        assertEquals("Automation", updatedSkill.get().getCategory());
    }
}
