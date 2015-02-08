package com.axiell.ehub.loan;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.axiell.ehub.consumer.EhubConsumer;

/**
 * Simple CRUD Repository interface for {@link EhubLoan} instances. The interface is used to declare so called query
 * methods, methods to retrieve single entities or collections of them.
 * 
 * <p>
 * <b>NOTE:</b> Even though this interface is public it should only be used within the package
 * <code>com.axiell.axiell.loan</code>. Access to this repository outside this package should be done through the
 * {@link ILoanBusinessController}.
 * </p>
 * <p>
 * The reason why it is public when it should have been package private is due to that the proxy functionality of the Spring JPA framework requires public repository interfaces.
 * </p>
 */
public interface IEhubLoanRepository extends CrudRepository<EhubLoan, Long> {
    @Query("select l from EhubLoan l where l.ehubConsumer.id = :ehubConsumerId and l.lmsLoan.id = :lmsLoanId")
    EhubLoan findLoan(@Param("ehubConsumerId") Long ehubConsumerId, @Param("lmsLoanId") String lmsLoanId);
    

    @Query("select l from EhubLoan l where l.ehubConsumer.id = :ehubConsumerId and l.id = :readyLoanId")
    EhubLoan findLoan(@Param("ehubConsumerId") Long ehubConsumerId, @Param("readyLoanId") Long readyLoanId);
    
    /**
     * Counts the number of loans that have been created in a specific format.
     */
    @Query("select count(*) from EhubLoan l where l.contentProviderLoanMetadata.formatDecoration.id = :formatDecorationId")
    long countLoansByFormatDecorationId(@Param("formatDecorationId") Long formatDecorationId);
}
