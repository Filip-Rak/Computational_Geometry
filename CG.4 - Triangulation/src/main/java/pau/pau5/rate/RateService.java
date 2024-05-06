package pau.pau5.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pau.pau5.classEmployee.ClassEmployee;
import pau.pau5.classEmployee.ClassEmployeeRepository;

@Service
public class RateService
{
    // Attributes
    private final RateRepository rateRepository;
    private final ClassEmployeeRepository classEmployeeRepository;

    // Constructor
    @Autowired
    public RateService(RateRepository rateRepository, ClassEmployeeRepository classEmployeeRepository)
    {
        this.rateRepository = rateRepository;
        this.classEmployeeRepository = classEmployeeRepository;
    }

    // Methods
    public Rate addRate(RateDTO rateDTO)
    {
        ClassEmployee classEmployee = classEmployeeRepository.findById(rateDTO.classEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + rateDTO.classEmployeeId()));

        Rate rate = new Rate(rateDTO.rating(), classEmployee, rateDTO.comment());
        return rateRepository.save(rate);
    }
}