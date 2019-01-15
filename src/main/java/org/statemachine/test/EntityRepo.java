package org.statemachine.test;

import org.springframework.data.jpa.repository.JpaRepository;

interface EntityRepo extends JpaRepository<MyEntity, Long> {
}
