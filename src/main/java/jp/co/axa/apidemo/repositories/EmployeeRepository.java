package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/* Mapping the JPA repository with Employee to access
 different functionality related to mapped database*/
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
