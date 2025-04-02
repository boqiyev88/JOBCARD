package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.PdfFile;

import java.util.Optional;

@Repository
public interface PDFfileRepository extends JpaRepository<PdfFile, String> {

    @Query("select p from PdfFile p where p.id= :fileName and p.isDeleted=0")
    Optional<PdfFile> findById(String fileName);

}
