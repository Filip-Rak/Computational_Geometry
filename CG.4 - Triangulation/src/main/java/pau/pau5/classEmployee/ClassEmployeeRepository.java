package pau.pau5.classEmployee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassEmployeeRepository extends JpaRepository<ClassEmployee, Integer> { }
